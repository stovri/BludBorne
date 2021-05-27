package com.electricstover.bludborne.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.electricstover.bludborne.Entity;
import com.electricstover.bludborne.maps.Map;
import com.electricstover.bludborne.maps.MapFactory;
import com.electricstover.bludborne.maps.MapManager;
import com.electricstover.bludborne.screens.MainGameScreen;
import com.electricstover.bludborne.screens.MainGameScreen.VIEWPORT;

public class PlayerPhysicsComponent extends PhysicsComponent{
	private static final String TAG = PlayerPhysicsComponent.class.getSimpleName();

	private Entity.State state;
	private String lastMap="TOWN";
	public PlayerPhysicsComponent() {
		velocity.x=4;
		velocity.y=4;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveMessage(String message) {
		String[] string=message.split(Component.MESSAGE_TOKEN);

		if(string.length==0)
			return;

		//Specifically for messages with 1 object payload
		if(string.length==2) {
			if(string[0].equalsIgnoreCase(MESSAGE.INIT_START_POSITION.toString())) {
				currentEntityPosition=json.fromJson(Vector2.class, string[1]);
				nextEntityPosition.set(currentEntityPosition.x,currentEntityPosition.y);
			}
			else if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_STATE.toString())) {
				state=json.fromJson(Entity.State.class, string[1]);
			}
			else if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_DIRECTION.toString())) {
				currentDirection=json.fromJson(Entity.Direction.class, string[1]);
			}
		}

	}

	 public void update(Entity entity, MapManager mapMgr, float delta) {
	        //We want the hitbox to be at the feet for a better feel
	        setBoundingBoxSize(entity, 0.3f, 0.5f);

	        updatePortalLayerActivation(mapMgr, boundingBox);

	        if (    !isCollisionWithMapLayer(entity, mapMgr) &&
	                !isCollisionWithMapEntities(entity, mapMgr) &&
	                state == Entity.State.WALKING){
	            setNextPositionToCurrent(entity);

	            Camera camera = mapMgr.getCamera();
	            float x=0;
	            float y=0;
				if(currentEntityPosition.x<VIEWPORT.viewportWidth/2)
					x=MainGameScreen.VIEWPORT.viewportWidth/2;
				else if(currentEntityPosition.x>mapMgr.getCurrentTiledMap().getProperties().get("width", Integer.class)-VIEWPORT.viewportWidth/2)
					x=mapMgr.getCurrentTiledMap().getProperties().get("width", Integer.class)-VIEWPORT.viewportWidth/2;
				else
					x=currentEntityPosition.x;
				if(currentEntityPosition.y<VIEWPORT.viewportHeight/2)
					y=VIEWPORT.viewportHeight/2;
				else if(currentEntityPosition.y>mapMgr.getCurrentTiledMap().getProperties().get("height", Integer.class)-VIEWPORT.viewportHeight/2)
					y=mapMgr.getCurrentTiledMap().getProperties().get("height", Integer.class)-VIEWPORT.viewportHeight/2;
				else
					y=currentEntityPosition.y;
				camera.position.set(x, y, 0f);
	            camera.update();
	        }

	        calculateNextPosition(delta);
	    }

	    private boolean updatePortalLayerActivation(MapManager mapMgr, Rectangle boundingBox){
	        MapLayer mapPortalLayer =  mapMgr.getPortalLayer();

	        if( mapPortalLayer == null ){
	            Gdx.app.debug(TAG, "Portal Layer doesn't exist!");
	            return false;
	        }

	        Rectangle rectangle = null;

	        for( MapObject object: mapPortalLayer.getObjects()){
	            if(object instanceof RectangleMapObject) {
	                rectangle = ((RectangleMapObject)object).getRectangle();
	                //Gdx.app.debug(TAG,object.getName());
	                if (boundingBox.overlaps(rectangle) ){
	                    String mapName = object.getName();
	                    Map.PLAYER_START=object.getName();
	                    int index=mapName.indexOf(Map.DIVIDER);
	                    if(index>=0)
	                    	mapName=mapName.substring(index+1);
	                    if( mapName == null ) {
	                        return false;
	                    }

	                    //mapMgr.setClosestStartPositionFromScaledUnits(currentEntityPosition);
	    	            mapMgr.loadMap(MapFactory.MapType.valueOf(mapName));

	                    currentEntityPosition.x = mapMgr.getPlayerStartUnitScaled().x;
	                    currentEntityPosition.y = mapMgr.getPlayerStartUnitScaled().y;
	                    nextEntityPosition.x = mapMgr.getPlayerStartUnitScaled().x;
	                    nextEntityPosition.y = mapMgr.getPlayerStartUnitScaled().y;

	                    Gdx.app.debug(TAG, "Portal Activated:"+Map.PLAYER_START);
	                    return true;
	                }
	            }
	        }
	        return false;
	    }

}
