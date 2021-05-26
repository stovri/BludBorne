package com.electricstover.bludborne.maps;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.electricstover.bludborne.Entity;
import com.electricstover.bludborne.Components.PlayerPhysicsComponent;
import com.electricstover.bludborne.maps.MapFactory.MapType;

public class TopWorldMap extends Map {

	private static String mapPath="maps/topworld.tmx";

	public TopWorldMap() {
		super(MapFactory.MapType.TOP_WORLD, mapPath);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateMapEntities(MapManager mapMgr, Batch batch, float delta) {
		// TODO Auto-generated method stub
		 for( Entity entity : mapEntities ){
	            entity.update(mapMgr, batch, delta);
	        }
	}
	
}
