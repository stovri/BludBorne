package com.electricstover.bludborne.Components;

public interface Component {

	String MESSAGE_TOKEN = ":::::";
	public static enum MESSAGE {
		CURRENT_POSITION,
        INIT_START_POSITION,
        CURRENT_DIRECTION,
        CURRENT_STATE,
        COLLISION_WITH_MAP,
        COLLISION_WITH_ENTITY,
        LOAD_ANIMATIONS,
        INIT_DIRECTION,
        INIT_STATE,
        INIT_SELECT_ENTITY,
        ENTITY_SELECTED,
        ENTITY_DESELECTED
	}
	void dispose();
	void receiveMessage(String message);


}
