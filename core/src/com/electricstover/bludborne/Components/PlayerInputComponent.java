package com.electricstover.bludborne.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.electricstover.bludborne.Entity;

public class PlayerInputComponent extends InputComponent implements InputProcessor{
	private final String TAG=PlayerInputComponent.class.getSimpleName();
	private Vector3 lastMouseCoordinates;

	public PlayerInputComponent() {
		this.lastMouseCoordinates=new Vector3();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);

	}

	@Override
	public void receiveMessage(String message) {
		String[] string=message.split(MESSAGE_TOKEN);
		
		if( string.length == 0 ) return;

		if(string.length==2) {
			if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_DIRECTION.toString())) {
				currentDirection=json.fromJson(Entity.Direction.class,string[1]);
			}
		}

	}

	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		if(keycode==Input.Keys.LEFT||keycode==Input.Keys.A) {
			this.leftPressed();
		}
		if(keycode==Input.Keys.RIGHT||keycode==Input.Keys.D) {
			this.rightPressed();
		}
		if(keycode==Input.Keys.UP||keycode==Input.Keys.W) {
			this.upPressed();
		}
		if(keycode==Input.Keys.DOWN||keycode==Input.Keys.S) {
			this.downPressed();
		}
		if(keycode==Input.Keys.Q) {
			this.quitPressed();
		}

		return true;
	}

	private void quitPressed() {
		keys.put(Keys.QUIT, true);
	}

	private void downPressed() {
		keys.put(Keys.DOWN, true);

	}

	private void upPressed() {
		keys.put(Keys.UP, true);
	}

	private void rightPressed() {
		keys.put(Keys.RIGHT, true);
	}

	private void leftPressed() {
		keys.put(Keys.LEFT, true);
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode==Input.Keys.LEFT||keycode==Input.Keys.A) {
			this.leftReleased();
		}
		if(keycode==Input.Keys.RIGHT||keycode==Input.Keys.D) {
			this.rightReleased();
		}
		if(keycode==Input.Keys.UP||keycode==Input.Keys.W) {
			this.upReleased();
		}
		if(keycode==Input.Keys.DOWN||keycode==Input.Keys.S) {
			this.downReleased();
		}
		if(keycode==Input.Keys.Q) {
			this.quitReleased();
		}
		return true;
	}

	private void quitReleased() {
		keys.put(Keys.QUIT, false);
	}

	private void downReleased() {
		keys.put(Keys.DOWN, false);
	}

	private void upReleased() {
		keys.put(Keys.UP, false);
	}

	private void rightReleased() {
		keys.put(Keys.RIGHT, false);
	}

	private void leftReleased() {
		keys.put(Keys.LEFT, false);
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if(button==Input.Buttons.LEFT||button==Input.Buttons.RIGHT) {
			this.setClickedMouseCoordinates(screenX,screenY);
		}

		if(button==Input.Buttons.LEFT) {
			this.selectMouseButtonPressed(screenX,screenY);
		}
		if(button==Input.Buttons.RIGHT) {
			this.doActionButtonPressed(screenX,screenY);
		}
		return true;
	}

	private void doActionButtonPressed(int screenX, int screenY) {
		mouseButtons.put(Mouse.DOACTION, true);
	}

	private void selectMouseButtonPressed(int screenX, int screenY) {
		mouseButtons.put(Mouse.SELECT, true);
	}

	private void setClickedMouseCoordinates(int screenX, int screenY) {
		lastMouseCoordinates.set(screenX, screenY, 0);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(button==Input.Buttons.LEFT) {
			this.selectMouseButtonReleased(screenX,screenY);
		}
		if(button==Input.Buttons.RIGHT) {
			this.doActionButtonReleased(screenX,screenY);
		}
		return true;
	}

	private void doActionButtonReleased(int screenX, int screenY) {
		mouseButtons.put(Mouse.DOACTION, false);
	}

	private void selectMouseButtonReleased(int screenX, int screenY) {
		mouseButtons.put(Mouse.SELECT, false);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void update(Entity entity, float delta) {
		if(keys.get(Keys.LEFT)) {
			entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.WALKING));
			entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.LEFT));
		}
		else if(keys.get(Keys.RIGHT)) {
			entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.WALKING));
			entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.RIGHT));
		}
		else if(keys.get(Keys.UP)) {
			entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.WALKING));
			entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.UP));
		}
		else if(keys.get(Keys.DOWN)) {
			entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.WALKING));
			entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.DOWN));
		}
		else if(keys.get(Keys.QUIT)) {
			Gdx.app.exit();
		}
		else {
			entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.IDLE));
			if(currentDirection==null)
				entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.DOWN));
		}

	}



}
