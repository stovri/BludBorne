package com.electricstover.bludborne.Components;

import java.util.Hashtable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.electricstover.bludborne.Entity;
import com.electricstover.bludborne.EntityConfig;
import com.electricstover.bludborne.EntityConfig.AnimationConfig;
import com.electricstover.bludborne.maps.MapManager;

public class PlayerGraphicsComponent extends GraphicsComponent{
	private static final String TAG = PlayerGraphicsComponent.class.getSimpleName();

	protected Vector2 currentPosition;
	private Entity.State currentState;
	private Entity.Direction currentDirection;
	private Json json;

	private float frameTime = 0f;
	private TextureRegion currentFrame = null;
	public PlayerGraphicsComponent(){
		currentPosition = new Vector2(0,0);
		currentState = Entity.State.WALKING;
		currentDirection = Entity.Direction.DOWN;
		animations = new Hashtable<Entity.AnimationType, Animation>();
		json = new Json();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveMessage(String message) {
		String[] string = message.split(MESSAGE_TOKEN);

		if( string.length == 0 ) 
			return;

		//Specifically for messages with 1 object payload
		if( string.length == 2 ) {
			if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_POSITION.toString())) {
				currentPosition = json.fromJson(Vector2.class, string[1]);
			} else if (string[0].equalsIgnoreCase(MESSAGE.INIT_START_POSITION.toString())) {
				currentPosition = json.fromJson(Vector2.class, string[1]);
			} else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_STATE.toString())) {
				currentState = json.fromJson(Entity.State.class, string[1]);
			} else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_DIRECTION.toString())) {
				currentDirection = json.fromJson(Entity.Direction.class, string[1]);
			} else if (string[0].equalsIgnoreCase(MESSAGE.LOAD_ANIMATIONS.toString())) {
				EntityConfig entityConfig = json.fromJson(EntityConfig.class, string[1]);
				Array<AnimationConfig> animationConfigs = entityConfig.getAnimationConfig();

				for( AnimationConfig animationConfig : animationConfigs ){
					Array<String> textureNames = animationConfig.getTexturePaths();
					Array<GridPoint2> points = animationConfig.getGridPoints();
					Entity.AnimationType animationType = animationConfig.getAnimationType();
					float frameDuration = animationConfig.getFrameDuration();
					Animation<TextureRegion> animation = null;

					if( textureNames.size == 1) {
						animation = loadAnimation(textureNames.get(0), points, frameDuration);
					}else if( textureNames.size == 2){
						animation = loadAnimation(textureNames.get(0), textureNames.get(1), points, frameDuration);
					}

					animations.put(animationType, animation);
				}
			}
		}
	}

	@Override
	public void update(Entity entity, Batch batch, float delta) {
		frameTime = (frameTime + delta)%5; //Want to avoid overflow

		//Look into the appropriate variable when changing position
		switch (currentDirection) {
		case DOWN:
			if (currentState == Entity.State.WALKING) {
				Animation animation = animations.get(Entity.AnimationType.WALK_DOWN);
				if( animation == null ) return;
				currentFrame = (TextureRegion) animation.getKeyFrame(frameTime);
			} else if(currentState == Entity.State.IDLE) {
				Animation animation = animations.get(Entity.AnimationType.WALK_DOWN);
				if( animation == null ) return;
				currentFrame = (TextureRegion) animation.getKeyFrames()[0];
			} else if(currentState == Entity.State.IMMOBILE) {
				Animation animation = animations.get(Entity.AnimationType.IMMOBILE);
				if( animation == null ) return;
				currentFrame = (TextureRegion) animation.getKeyFrame(frameTime);
			}
			break;
		case LEFT:
			if (currentState == Entity.State.WALKING) {
				Animation animation = animations.get(Entity.AnimationType.WALK_LEFT);
				if( animation == null ) return;
				currentFrame = (TextureRegion) animation.getKeyFrame(frameTime);
			} else if(currentState == Entity.State.IDLE) {
				Animation animation = animations.get(Entity.AnimationType.WALK_LEFT);
				if( animation == null ) return;
				currentFrame = (TextureRegion) animation.getKeyFrames()[0];
			} else if(currentState == Entity.State.IMMOBILE) {
				Animation animation= animations.get(Entity.AnimationType.IMMOBILE);
				if( animation == null ) return;
				currentFrame = (TextureRegion) animation.getKeyFrame(frameTime);
			}
			break;
		case UP:
			if (currentState == Entity.State.WALKING) {
				Animation animation = animations.get(Entity.AnimationType.WALK_UP);
				if( animation == null ) return;
				currentFrame = (TextureRegion) animation.getKeyFrame(frameTime);
			} else if(currentState == Entity.State.IDLE) {
				Animation animation = animations.get(Entity.AnimationType.WALK_UP);
				if( animation == null ) return;
				currentFrame = (TextureRegion) animation.getKeyFrames()[0];
			} else if(currentState == Entity.State.IMMOBILE) {
				Animation animation = animations.get(Entity.AnimationType.IMMOBILE);
				if( animation == null ) return;
				currentFrame = (TextureRegion) animation.getKeyFrame(frameTime);
			}
			break;
		case RIGHT:
			if (currentState == Entity.State.WALKING) {
				Animation animation = animations.get(Entity.AnimationType.WALK_RIGHT);
				if( animation == null ) return;
				currentFrame = (TextureRegion) animation.getKeyFrame(frameTime);
			} else if(currentState == Entity.State.IDLE) {
				Animation animation = animations.get(Entity.AnimationType.WALK_RIGHT);
				if( animation == null ) return;
				currentFrame = (TextureRegion) animation.getKeyFrames()[0];
			} else if(currentState == Entity.State.IMMOBILE) {
				Animation animation = animations.get(Entity.AnimationType.IMMOBILE);
				if( animation == null ) return;
				currentFrame = (TextureRegion) animation.getKeyFrame(frameTime);
			}
			break;
		default:
			break;
		}

		batch.begin();
		batch.draw(currentFrame, currentPosition.x, currentPosition.y, 1, 1);
		batch.end();
	}
}

