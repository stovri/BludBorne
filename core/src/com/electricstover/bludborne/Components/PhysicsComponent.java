package com.electricstover.bludborne.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.electricstover.bludborne.Entity;
import com.electricstover.bludborne.Entity.Direction;
import com.electricstover.bludborne.maps.Map;
import com.electricstover.bludborne.maps.MapManager;

public abstract class PhysicsComponent implements Component{
	private static final String TAG = PhysicsComponent.class.getSimpleName();

	public abstract void update(Entity entity, MapManager mapMgr, float delta);

	protected Vector2 nextEntityPosition;
	protected Vector2 currentEntityPosition;
	protected Entity.Direction currentDirection;
	protected Json json;
	protected Vector2 velocity;

	public Rectangle boundingBox;

	PhysicsComponent(){
		this.currentEntityPosition = new Vector2(0,0);
		this.nextEntityPosition = new Vector2(0,0);
		this.velocity = new Vector2(2f,2f);
		this.boundingBox = new Rectangle();
		this.json = new Json();
	}

	protected boolean isCollisionWithMapEntities(Entity entity, MapManager mapMgr) {
		Array<Entity> entities=mapMgr.getCurrentMapEntities();
		boolean isCollisionWithMapEntity=false;
		for(Entity mapEntity:entities) {
			if(mapEntity.equals(entity)) {
				continue;
			}

			if(boundingBox.overlaps(mapEntity.getCurrentBoundingBox())) {
				//Collision
				entity.sendMessage(MESSAGE.COLLISION_WITH_ENTITY);
				isCollisionWithMapEntity=true;
				break;
			}
		}
		return isCollisionWithMapEntity;
	}

	public boolean isCollision(Entity entitySource, Entity entityTarget) {
		boolean isCollisionWithMapEntities=false;

		if(entitySource.equals(entityTarget)) {
			return false;
		}

		if(entitySource.getCurrentBoundingBox().overlaps(entityTarget.getCurrentBoundingBox())) {
			//collision
			entitySource.sendMessage(MESSAGE.COLLISION_WITH_ENTITY);
			isCollisionWithMapEntities=true;
		}
		return isCollisionWithMapEntities;
	}
	protected boolean isCollisionWithMapLayer(Entity entity, MapManager mapMgr){
		TiledMapTileLayer mapCollisionLayer =  mapMgr.getCollisionLayer();

		if( mapCollisionLayer == null ){
			return false;
		}



		for(int i=0;i<mapCollisionLayer.getHeight();i++) {
			for(int j=0;j<mapCollisionLayer.getWidth();j++) {
				Cell c=mapCollisionLayer.getCell(j,i);
				if(c!=null) {
					for(MapObject object: c.getTile().getObjects()){
						if(object instanceof RectangleMapObject) {
							float x=((RectangleMapObject)object).getRectangle().getX()+j/Map.UNIT_SCALE;
							float y=((RectangleMapObject)object).getRectangle().getY()+i/Map.UNIT_SCALE;


							Rectangle rectangle=new Rectangle(x,y,((RectangleMapObject)object).getRectangle().width,((RectangleMapObject)object).getRectangle().height);
							if( boundingBox.overlaps(rectangle) ){
								//Collision
								//Gdx.app.debug(TAG, "Collision cell:"+i+","+j+" of "+mapCollisionLayer.getTileWidth()+","+mapCollisionLayer.getTileHeight());
								entity.sendMessage(MESSAGE.COLLISION_WITH_MAP);
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	protected void setNextPositionToCurrent(Entity entity){
		this.currentEntityPosition.x = nextEntityPosition.x;
		this.currentEntityPosition.y = nextEntityPosition.y;

		//Gdx.app.debug(TAG, "SETTING Current Position " + entity.getEntityConfig().getEntityID() + ": (" + currentEntityPosition.x + "," + currentEntityPosition.y + ")");
		entity.sendMessage(MESSAGE.CURRENT_POSITION, json.toJson(currentEntityPosition));
	}

	protected void calculateNextPosition(float deltaTime){
		if( currentDirection == null ) return;

		if( deltaTime > .7) return;

		float testX = currentEntityPosition.x;
		float testY = currentEntityPosition.y;

		velocity.scl(deltaTime);

		switch (currentDirection) {
		case LEFT :
			testX -=  velocity.x;
			break;
		case RIGHT :
			testX += velocity.x;
			break;
		case UP :
			testY += velocity.y;
			break;
		case DOWN :
			testY -= velocity.y;
			break;
		default:
			break;
		}

		nextEntityPosition.x = testX;
		nextEntityPosition.y = testY;

		//velocity
		velocity.scl(1 / deltaTime);
	}

	protected void setBoundingBoxSize(Entity entity, float percentageWidthReduced, float percentageHeightReduced){
		//Update the current bounding box
		float width;
		float height;

		float widthReductionAmount = 1.0f - percentageWidthReduced; //.8f for 20% (1 - .20)
		float heightReductionAmount = 1.0f - percentageHeightReduced; //.8f for 20% (1 - .20)

		if( widthReductionAmount > 0 && widthReductionAmount < 1){
			width = entity.FRAME_WIDTH * widthReductionAmount;
		}else{
			width = entity.FRAME_WIDTH;
		}

		if( heightReductionAmount > 0 && heightReductionAmount < 1){
			height = entity.FRAME_HEIGHT * heightReductionAmount;
		}else{
			height = entity.FRAME_HEIGHT;
		}

		if( width == 0 || height == 0){
			Gdx.app.debug(TAG, "Width and Height are 0!! " + width + ":" + height);
		}

		//Need to account for the unitscale, since the map coordinates will be in pixels
		float minX;
		float minY;
		if( Map.UNIT_SCALE > 0 ) {
			minX = (nextEntityPosition.x / Map.UNIT_SCALE);
			minY = nextEntityPosition.y / Map.UNIT_SCALE;
		}else{
			minX = nextEntityPosition.x;
			minY = nextEntityPosition.y;
		}
		minX+=(entity.FRAME_WIDTH-width)/2;
		
		boundingBox.set(minX, minY, width, height);
		//Gdx.app.debug(TAG, "SETTING Bounding Box for " + entity.getEntityConfig().getEntityID() + ": (" + minX + "|"+(entity.FRAME_WIDTH-width)+"," + minY + ")  width: " + width + " height: " + height);
	}
}
