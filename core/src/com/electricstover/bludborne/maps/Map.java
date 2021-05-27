package com.electricstover.bludborne.maps;

import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.electricstover.bludborne.Entity;
import com.electricstover.bludborne.EntityFactory;
import com.electricstover.bludborne.EntityFactory.EntityType;
import com.electricstover.bludborne.Utility;
import com.electricstover.bludborne.maps.MapFactory.MapType;

public abstract class Map {
	private static final String TAG=Map.class.getSimpleName();
	public static final float UNIT_SCALE = 1/16f;

	private final static String MAP_COLLISION_LAYER="Ground_Layer";
	private final static String MAP_SPAWNS_LAYER="MAP_SPAWNS_LAYER";
	private final static String MAP_PORTAL_LAYER="MAP_PORTAL_LAYER";

	public static String PLAYER_START="TOWN";
	private final static String NPC_START="NPC_START";
	public static final String DIVIDER = "|";

	protected Json json;

	private Vector2 playerStartPositionRect;
	private Vector2 closestPlayerStartPosition;
	private Vector2 convertedUnits;
	private TiledMap currentMap;
	private Vector2 playerStart;
	protected Array<Vector2> npcStartPositions;
	protected Hashtable <String,Vector2>specialNPCStartPositions;
	private TiledMapTileLayer collisionLayer;
	private MapLayer portalLayer;
	private MapLayer spawnsLayer;

	private MapType currentMapType;

	protected Array<Entity> mapEntities;


	public Map(MapType mapType, String fullMapPath) {
		// TODO Auto-generated constructor stub
		currentMapType=mapType;
		json = new Json();
		mapEntities = new Array<Entity>(10);
		currentMapType = mapType;
		playerStart = new Vector2(14*16,9*16);
		playerStartPositionRect = new Vector2(0,0);
		closestPlayerStartPosition = new Vector2(0,0);
		convertedUnits = new Vector2(0,0);

		if( fullMapPath == null || fullMapPath.isEmpty() ) {
			Gdx.app.debug(TAG, "Map is invalid");
			return;
		}

		Utility.loadMapAsset(fullMapPath);
		if( Utility.isAssetLoaded(fullMapPath) ) {
			currentMap = Utility.getMapAsset(fullMapPath);
		}
		else{
			Gdx.app.debug(TAG, "Map not loaded");
			return;
		}

		collisionLayer = (TiledMapTileLayer) currentMap.getLayers().get(MAP_COLLISION_LAYER);
		if( collisionLayer == null ){
			Gdx.app.debug(TAG, "No collision layer!");
		}

		portalLayer = currentMap.getLayers().get(MAP_PORTAL_LAYER);
		if( getPortalLayer() == null ){
			Gdx.app.debug(TAG, "No portal layer!");
		}

		spawnsLayer = currentMap.getLayers().get(MAP_SPAWNS_LAYER);
		if( spawnsLayer == null ){
			Gdx.app.debug(TAG, "No spawn layer!");
		}
		else{
			setClosestStartPosition(playerStart);
		}

		npcStartPositions = getNPCStartPositions();
		specialNPCStartPositions = getSpecialNPCStartPositions();
	}

	public Array<Entity> getMapEntities(){
		return mapEntities;
	}

	public Vector2 getPlayerStart() {
		return playerStart;
	}

	public abstract void updateMapEntities(MapManager mapMgr, Batch batch, float delta);

	public TiledMapTileLayer getCollisionLayer(){
		return collisionLayer;
	}

	public MapLayer getPortalLayer(){
		return portalLayer;
	}

	public TiledMap getCurrentTiledMap() {
		return currentMap;
	}

	public Vector2 getPlayerStartUnitScaled(){
		Vector2 playerStart = this.playerStart.cpy();
		playerStart.set(playerStart.x * UNIT_SCALE, playerStart.y * UNIT_SCALE);
		return playerStart;
	}

	private Array<Vector2> getNPCStartPositions(){
		Array<Vector2> npcStartPositions = new Array<Vector2>();

		for( MapObject object: spawnsLayer.getObjects()){
			String objectName = object.getName();

			if( objectName == null || objectName.isEmpty() ){
				continue;
			}
			//Gdx.app.debug(TAG,objectName);
			if( objectName.equalsIgnoreCase("TOWN_GUARD") ){
				//Get center of rectangle
				Gdx.app.debug(TAG,objectName);
				float x = ((RectangleMapObject)object).getRectangle().getX();
				float y = ((RectangleMapObject)object).getRectangle().getY();

				//scale by the unit to convert from map coordinates
				x *= UNIT_SCALE;
				y *= UNIT_SCALE;

				npcStartPositions.add(new Vector2(x,y));
			}
		}
		return npcStartPositions;
	}
	private Hashtable<String, Vector2> getSpecialNPCStartPositions(){
		Hashtable<String, Vector2> specialNPCStartPositions = new Hashtable<String, Vector2>();

		for( MapObject object: spawnsLayer.getObjects()){
			String objectName = object.getName();

			if( objectName == null || objectName.isEmpty() ){
				continue;
			}

			//This is meant for all the special spawn locations, a catch all, so ignore known ones
			if(     objectName.equalsIgnoreCase(NPC_START) ||
					objectName.equalsIgnoreCase(PLAYER_START) ){
				continue;
			}

			//Get center of rectangle
			float x = ((RectangleMapObject)object).getRectangle().getX();
			float y = ((RectangleMapObject)object).getRectangle().getY();

			//scale by the unit to convert from map coordinates
			x *= UNIT_SCALE;
			y *= UNIT_SCALE;

			specialNPCStartPositions.put(objectName, new Vector2(x,y));
		}
		return specialNPCStartPositions;
	}
	public Vector2 getClosestPlayerStartPosition() {
		return closestPlayerStartPosition;
	}

	private void setClosestStartPosition(final Vector2 position){
		Gdx.app.debug(TAG, PLAYER_START+"setClosestStartPosition INPUT: (" + position.x + "," + position.y + ") " + currentMapType.toString());

		//Get last known position on this map
		playerStartPositionRect.set(0,0);
		closestPlayerStartPosition.set(0,0);
		float shortestDistance = 0f;

		//Go through all player start positions and choose closest to last known position
		for( MapObject object: spawnsLayer.getObjects()){
			String objectName = object.getName();

			if( objectName == null || objectName.isEmpty() ){
				continue;
			}
			Gdx.app.debug(TAG,object.getName()+"="+PLAYER_START);
			if( objectName.equalsIgnoreCase(PLAYER_START) ){
				((RectangleMapObject)object).getRectangle().getPosition(playerStartPositionRect);
				closestPlayerStartPosition.set(playerStartPositionRect);
				Gdx.app.debug(TAG, "closest START is: (" + closestPlayerStartPosition.x + "," + closestPlayerStartPosition.y + ") " +  currentMapType.toString());

			}
		}
		playerStart =  closestPlayerStartPosition.cpy();

	}
	public void setClosestStartPositionFromScaledUnits(Vector2 position){
		if( UNIT_SCALE <= 0 )
			return;

		convertedUnits.set(position.x/UNIT_SCALE, position.y/UNIT_SCALE);
		setClosestStartPosition(convertedUnits);
	}
	public TiledMap getCurrentMap() {
		return currentMap;
	}

	public MapType getCurrentMapType() {
		// TODO Auto-generated method stub
		return currentMapType;
	}

	public void setStartPosition() {
		// TODO Auto-generated method stub
		for( MapObject object: spawnsLayer.getObjects()){
			String objectName = object.getName();

			if( objectName == null || objectName.isEmpty() ){
				continue;
			}
			Gdx.app.debug(TAG,object.getName()+"="+PLAYER_START);
			if( objectName.equalsIgnoreCase(PLAYER_START) ){
				((RectangleMapObject)object).getRectangle().getPosition(playerStartPositionRect);
				closestPlayerStartPosition.set(playerStartPositionRect);
				Gdx.app.debug(TAG, "closest START is: (" + closestPlayerStartPosition.x + "," + closestPlayerStartPosition.y + ") " +  currentMapType.toString());

			}
		}
		playerStart =  closestPlayerStartPosition.cpy();
	}
}
