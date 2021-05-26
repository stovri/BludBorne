package com.electricstover.bludborne;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.electricstover.bludborne.Entity.AnimationType;
import com.electricstover.bludborne.Entity.Direction;
import com.electricstover.bludborne.Entity.State;

public class EntityConfig {

	private Entity.State state = Entity.State.IDLE;
	private Array<AnimationConfig> animationConfig;
	private String entityID;
	private Direction direction = Entity.Direction.DOWN;;

	public EntityConfig() {
		animationConfig = new Array<AnimationConfig>();
	}

	public String getEntityID() {
		return entityID;
	}

	public void setEntityID(String entityID) {
		this.entityID = entityID;
	}

	public Entity.Direction getDirection() {
		return direction;
	}

	public void setDirection(Entity.Direction direction) {
		this.direction = direction;
	}

	public State getState() {
		// TODO Auto-generated method stub
		return state;
	}
	public void setState(Entity.State state) {
		this.state = state;
	}
	public Array<AnimationConfig> getAnimationConfig() {
		return animationConfig;
	}

	public void addAnimationConfig(AnimationConfig animationConfig) {
		this.animationConfig.add(animationConfig);
	}
	static public class AnimationConfig{
		private float frameDuration = 1.0f;
		private AnimationType animationType;
		private Array<String> texturePaths;
		private Array<GridPoint2> gridPoints;

		public AnimationConfig(){
			animationType = AnimationType.IDLE;
			texturePaths = new Array<String>();
			gridPoints = new Array<GridPoint2>();
		}

		public float getFrameDuration() {
			return frameDuration;
		}

		public void setFrameDuration(float frameDuration) {
			this.frameDuration = frameDuration;
		}

		public Array<String> getTexturePaths() {
			return texturePaths;
		}

		public void setTexturePaths(Array<String> texturePaths) {
			this.texturePaths = texturePaths;
		}

		public Array<GridPoint2> getGridPoints() {
			return gridPoints;
		}

		public void setGridPoints(Array<GridPoint2> gridPoints) {
			this.gridPoints = gridPoints;
		}

		public AnimationType getAnimationType() {
			return animationType;
		}

		public void setAnimationType(AnimationType animationType) {
			this.animationType = animationType;
		}
	}

}
