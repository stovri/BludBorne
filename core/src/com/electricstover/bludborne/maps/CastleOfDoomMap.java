package com.electricstover.bludborne.maps;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.electricstover.bludborne.Entity;
import com.electricstover.bludborne.maps.MapFactory.MapType;

public class CastleOfDoomMap extends Map {
	private static String _mapPath = "maps/castle_of_doom.tmx";

    CastleOfDoomMap(){
        super(MapFactory.MapType.CASTLE_OF_DOOM, _mapPath);
    }

    @Override
    public void updateMapEntities(MapManager mapMgr, Batch batch, float delta){
        for( Entity entity : mapEntities ){
            entity.update(mapMgr, batch, delta);
        }
    }

}
