package com.electricstover.bludborne.maps;

import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.electricstover.bludborne.Entity;
import com.electricstover.bludborne.Utility;
import com.electricstover.bludborne.maps.MapFactory.MapType;
import com.electricstover.bludborne.Components.Component;

public class MapManager {

	private static final String TAG=MapManager.class.getSimpleName();
	private Camera camera;
	private boolean mapChanged = false;
	private Map currentMap;
	private Entity player;

	public MapManager() {
	}

	public void loadMap(MapType mapType) {
		Map map = MapFactory.getMap(mapType);

		if( map == null ){
			Gdx.app.debug(TAG, "Map does not exist!  ");
			return;
		}



		currentMap = map;
		mapChanged = true;
		Gdx.app.debug(TAG, "Player Start: (" + currentMap.getPlayerStart().x + "," + currentMap.getPlayerStart().y + ")");
	}

	public void setClosestStartPositionFromScaledUnits(Vector2 position) {
        currentMap.setClosestStartPositionFromScaledUnits(position);
    }

    public TiledMapTileLayer getCollisionLayer(){
        return currentMap.getCollisionLayer();
    }

    public MapLayer getPortalLayer(){
        return currentMap.getPortalLayer();
    }
    public Vector2 getPlayerStartUnitScaled() {
        return currentMap.getPlayerStartUnitScaled();
    }

    public TiledMap getCurrentTiledMap(){
        if( currentMap == null ) {
            loadMap(MapFactory.MapType.TOWN);
        }
        return currentMap.getCurrentTiledMap();
    }

    public void updateCurrentMapEntities(MapManager mapMgr, Batch batch, float delta){
        currentMap.updateMapEntities(mapMgr, batch, delta);
    }

    public final Array<Entity> getCurrentMapEntities(){
        return currentMap.getMapEntities();
    }

    public void setPlayer(Entity entity){
        this.player = entity;
    }

    public Entity getPlayer(){
        return this.player;
    }

    public void setCamera(Camera camera){
        this.camera = camera;
    }

    public Camera getCamera(){
        return camera;
    }

    public boolean hasMapChanged(){
        return mapChanged;
    }

    public void setMapChanged(boolean hasMapChanged){
        this.mapChanged = hasMapChanged;
    }


}
