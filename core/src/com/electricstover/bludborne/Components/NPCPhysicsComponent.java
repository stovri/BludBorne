package com.electricstover.bludborne.Components;

import com.badlogic.gdx.math.Vector2;
import com.electricstover.bludborne.Entity;
import com.electricstover.bludborne.maps.MapManager;

public class NPCPhysicsComponent extends PhysicsComponent {
    private static final String TAG = NPCPhysicsComponent.class.getSimpleName();

    private Entity.State state;

    public NPCPhysicsComponent(){
    }

    @Override
    public void dispose(){
    }

    @Override
    public void receiveMessage(String message) {
        //Gdx.app.debug(TAG, "Got message " + message);
        String[] string = message.split(Component.MESSAGE_TOKEN);

        if( string.length == 0 ) return;

        //Specifically for messages with 1 object payload
        if( string.length == 2 ) {
            if (string[0].equalsIgnoreCase(MESSAGE.INIT_START_POSITION.toString())) {
                currentEntityPosition = json.fromJson(Vector2.class, string[1]);
                nextEntityPosition.set(currentEntityPosition.x, currentEntityPosition.y);
            } else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_STATE.toString())) {
                state = json.fromJson(Entity.State.class, string[1]);
            } else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_DIRECTION.toString())) {
                currentDirection = json.fromJson(Entity.Direction.class, string[1]);
            }
        }
    }

    @Override
    public void update(Entity entity, MapManager mapMgr, float delta) {
    	  setBoundingBoxSize(entity, 0f, 0f);

          if( state == Entity.State.IMMOBILE ) return;

          if (    !isCollisionWithMapLayer(entity, mapMgr) &&
                  !isCollisionWithMapEntities(entity, mapMgr) &&
                  state == Entity.State.WALKING){
              setNextPositionToCurrent(entity);
          }

          calculateNextPosition(delta);
      }

      @Override
      protected boolean isCollisionWithMapEntities(Entity entity, MapManager mapMgr){
          if( super.isCollisionWithMapEntities(entity, mapMgr) ){
              return true;
          }
          //Test against player
          if( isCollision(entity, mapMgr.getPlayer()) ) {
              return true;
          }
          return false;
      }
}