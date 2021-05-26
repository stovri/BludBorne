package com.electricstover.bludborne;

import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.electricstover.bludborne.Components.Component;
import com.electricstover.bludborne.Components.NPCGraphicsComponent;
import com.electricstover.bludborne.Components.NPCInputComponent;
import com.electricstover.bludborne.Components.NPCPhysicsComponent;
import com.electricstover.bludborne.Components.PlayerGraphicsComponent;
import com.electricstover.bludborne.Components.PlayerInputComponent;
import com.electricstover.bludborne.Components.PlayerPhysicsComponent;

public class EntityFactory {
	private static String TAG=EntityFactory.class.getSimpleName();

	private static Json json=new Json();

	public static enum EntityType{
		PLAYER,
		DEMO_PLAYER,
		NPC
	}
	public static String PLAYER_CONFIG = "scripts/player.json";
    
 	static public Entity getEntity(EntityType entityType) {
		
		Entity entity=null;
		switch(entityType) {
		case PLAYER:
			entity=new Entity(new PlayerInputComponent(),new PlayerPhysicsComponent(),new PlayerGraphicsComponent());
			entity.setEntityConfig(Entity.getEntityConfig(EntityFactory.PLAYER_CONFIG));
			entity.sendMessage(Component.MESSAGE.LOAD_ANIMATIONS,json.toJson(entity.getEntityConfig()));
			return entity;
		case DEMO_PLAYER:
			entity=new Entity(new NPCInputComponent(),new PlayerPhysicsComponent(),new PlayerGraphicsComponent());
			return entity;
		case NPC:
			entity=new Entity(new NPCInputComponent(),new NPCPhysicsComponent(),new NPCGraphicsComponent());
			return entity;
		default:
			return null;
		}
	}

}
