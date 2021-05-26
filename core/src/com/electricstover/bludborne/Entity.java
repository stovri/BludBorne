package com.electricstover.bludborne;

import java.util.ArrayList;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.electricstover.bludborne.Components.Component;
import com.electricstover.bludborne.Components.GraphicsComponent;
import com.electricstover.bludborne.Components.InputComponent;
import com.electricstover.bludborne.Components.NPCInputComponent;
import com.electricstover.bludborne.Components.PhysicsComponent;
import com.electricstover.bludborne.maps.MapManager;

public class Entity {

	private static final String TAG=Entity.class.getSimpleName();
	private static String entityConfigPath;
	private Json json;
	private EntityConfig entityConfig;

	public static enum Direction {
		UP, RIGHT, DOWN, LEFT;

		static public Direction getRandomNext() {
			return Direction.values()[MathUtils.random(Direction.values().length-1)];

		}

		public Direction getOpposite() {
			if(this==LEFT)
				return RIGHT;
			else if(this==RIGHT)
				return LEFT;
			else if(this==UP)
				return DOWN;
			else 
				return UP;

		}
	}

	public enum State {
		IDLE,WALKING,IMMOBILE;

		static public State getRandomNext() {
			return State.values()[MathUtils.random(State.values().length-2)];                 
		}		
	}

	public static enum AnimationType{
		WALK_LEFT,
		WALK_RIGHT,
		WALK_UP,
		WALK_DOWN,
		IDLE,
		IMMOBILE
	}

	public final static int FRAME_WIDTH=16;
	public final static int FRAME_HEIGHT=16;

	private static final int MAX_COMPONENTS=5;
	private Array<Component> components;

	private InputComponent inputComponent;
	private GraphicsComponent graphicsComponent;
	private PhysicsComponent physicsComponent;

	public Entity(InputComponent inputComponent,PhysicsComponent physicsComponent,GraphicsComponent graphicsComponent) {
		entityConfig=new EntityConfig();
		json=new Json();

		components=new Array<Component>(MAX_COMPONENTS);

		this.inputComponent=inputComponent;
		this.physicsComponent=physicsComponent;
		this.graphicsComponent=graphicsComponent;

		components.add(this.inputComponent);
		components.add(this.physicsComponent);
		components.add(this.graphicsComponent);
	}

	public EntityConfig getEntityConfig() {
		// TODO Auto-generated method stub
		return entityConfig;
	}

	public void sendMessage(Component.MESSAGE messageType, String ... args) {
		String fullMessage=messageType.toString();


		for(String string : args) {
			fullMessage+=Component.MESSAGE_TOKEN+string;
		}

		for(Component component:components) {
			component.receiveMessage(fullMessage);
		}
	}

	public void update(MapManager mapMgr, Batch batch, float delta) {
		inputComponent.update(this,delta);
		physicsComponent.update(this,mapMgr,delta);
		graphicsComponent.update(this,batch,delta);
	}

	public void dispose() {
		for(Component component:components) {
			component.dispose();
		}
	}

	public Rectangle getCurrentBoundingBox() {
		return physicsComponent.boundingBox;
	}
	public static EntityConfig getEntityConfig(String configFilePath) {
		Json json=new Json();
		return json.fromJson(EntityConfig.class, Gdx.files.internal(configFilePath));
	}

	public void setEntityConfig(EntityConfig entityConfig) {
		this.entityConfig=entityConfig;
	}

	static public Array<EntityConfig> getEntityConfigs(String configFilePath){
		Json json=new Json();
		Array<EntityConfig> configs=new Array<EntityConfig>();

		ArrayList<JsonValue> list=json.fromJson(ArrayList.class, Gdx.files.internal(configFilePath));

		for(JsonValue jsonVal:list) {
			configs.add(json.readValue(EntityConfig.class, jsonVal));
		}

		return configs;
	}
}
