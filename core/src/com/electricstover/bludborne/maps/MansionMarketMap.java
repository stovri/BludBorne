package com.electricstover.bludborne.maps;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.electricstover.bludborne.Entity;
import com.electricstover.bludborne.EntityConfig;
import com.electricstover.bludborne.EntityFactory;
import com.electricstover.bludborne.Components.Component;

public class MansionMarketMap extends Map {
	private static final String TAG=TopWorldMap.class.getSimpleName();
	
	private static String mapPath="maps/mansion_market.tmx";
	private static String fisherman="scripts/fisherman.json";
	private static String grocer="scripts/grocer.json";
	private static String bossman="scripts/bossman.json";
	private static String townFolk="scripts/town_folk.json";


	public MansionMarketMap() {
		super(MapFactory.MapType.MANSION_MARKET,mapPath);
		//Special cases
        mapEntities.add(initSpecialEntity(Entity.getEntityConfig(fisherman)));
        mapEntities.add(initSpecialEntity(Entity.getEntityConfig(grocer)));
        mapEntities.add(initSpecialEntity(Entity.getEntityConfig(bossman)));
        Array<EntityConfig> configs = Entity.getEntityConfigs(townFolk);
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
