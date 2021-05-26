package com.electricstover.bludborne.maps;

import java.util.Hashtable;

public class MapFactory {
	private static Hashtable<MapType, Map> mapTable=new Hashtable<MapType,Map>();
	
	public static enum MapType{
		TOP_WORLD,
		TOWN,
		CASTLE_OF_DOOM, HERMIT, MANSION_MARKET
	}
	
	static public Map getMap(MapType mapType) {
		Map map=null;
		
		switch(mapType) {
		case TOP_WORLD:
			map=mapTable.get(MapType.TOP_WORLD);
			if(map==null) {
				map=new TopWorldMap();
				mapTable.put(MapType.TOP_WORLD,map);
			}
			break;
		case TOWN:
			map=mapTable.get(MapType.TOWN);
			if(map==null) {
				map=new TownMap();
				mapTable.put(MapType.TOWN,map);
			}
			break;
		case CASTLE_OF_DOOM:
			map=mapTable.get(MapType.CASTLE_OF_DOOM);
			if(map==null) {
				map=new CastleOfDoomMap();
				mapTable.put(MapType.CASTLE_OF_DOOM,map);
			}
			break;
		case HERMIT:
			map=mapTable.get(MapType.HERMIT);
			if(map==null) {
				map=new HermitMap();
				mapTable.put(MapType.HERMIT,map);
			}
			break;
		case MANSION_MARKET:
			map=mapTable.get(MapType.MANSION_MARKET);
			if(map==null) {
				map=new MansionMarketMap();
				mapTable.put(MapType.MANSION_MARKET,map);
			}
			break;
		}
		
		return map;
	}

}
