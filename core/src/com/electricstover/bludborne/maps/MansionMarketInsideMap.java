package com.electricstover.bludborne.maps;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.electricstover.bludborne.Entity;
import com.electricstover.bludborne.EntityConfig;
import com.electricstover.bludborne.EntityFactory;
import com.electricstover.bludborne.Components.Component;

public class MansionMarketInsideMap extends Map {
	private static final String TAG=TopWorldMap.class.getSimpleName();
	
	private static String mapPath="maps/mansion_market_inside.tmx";
	private static String weaponsmith="scripts/weaponsmith.json";
	private static String armorsmith="scripts/armorsmith.json";
	private static String spellsmith="scripts/spellsmith.json";
	private static String potionsmith="scripts/potionsmith.json";
	private static String townGuardWalking="scripts/town_guard_walking.json";


	public MansionMarketInsideMap() {
		super(MapFactory.MapType.MANSION_MARKET_INSIDE,mapPath);
		for(Vector2 position:npcStartPositions) {
			mapEntities.add(initEntity(Entity.getEntityConfig(townGuardWalking),position));
		}
		//Special cases
        mapEntities.add(initSpecialEntity(Entity.getEntityConfig(weaponsmith)));
        mapEntities.add(initSpecialEntity(Entity.getEntityConfig(armorsmith)));
        mapEntities.add(initSpecialEntity(Entity.getEntityConfig(spellsmith)));
        mapEntities.add(initSpecialEntity(Entity.getEntityConfig(potionsmith)));
        Array<EntityConfig> configs = Entity.getEntityConfigs(potionsmith);
        for(EntityConfig config: configs){
            mapEntities.add(initSpecialEntity(config));
        }
	}
	
	public void updateMapEntities(MapManager mapMgr, Batch batch, float delta) {
		for(int i=0;i<mapEntities.size;i++) {
			mapEntities.get(i).update(mapMgr, batch, delta);
		}
	}

	private Entity initEntity(EntityConfig entityConfig, Vector2 position) {
		Entity entity=EntityFactory.getEntity(EntityFactory.EntityType.NPC);
		entity.setEntityConfig(entityConfig);
		entity.sendMessage(Component.MESSAGE.LOAD_ANIMATIONS, json.toJson(entity.getEntityConfig()));
		entity.sendMessage(Component.MESSAGE.INIT_START_POSITION, json.toJson(position));
		entity.sendMessage(Component.MESSAGE.INIT_STATE, json.toJson(entity.getEntityConfig().getState()));
		entity.sendMessage(Component.MESSAGE.INIT_DIRECTION, json.toJson(entity.getEntityConfig().getDirection()));

		return entity;
	}
	
	private Entity initSpecialEntity(EntityConfig entityConfig) {
		Vector2 position=new Vector2(0,0);
		
		if(specialNPCStartPositions.containsKey(entityConfig.getEntityID())) {
			position=specialNPCStartPositions.get(entityConfig.getEntityID());
		}
		return initEntity(entityConfig, position);
	}

}
