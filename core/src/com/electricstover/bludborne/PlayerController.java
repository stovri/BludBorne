package com.electricstover.bludborne;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

public class PlayerController implements InputProcessor {

	private final static String TAG=PlayerController.class.getSimpleName();

	enum Keys {
		LEFT, RIGHT, UP, DOWN, QUIT
	}

	enum Mouse {
		SELECT, DOACTION
	}

	private static Map<Keys, Boolean> keys=new HashMap <PlayerController.Keys,Boolean>();
	private static Map<Mouse, Boolean> mouseButtons=new HashMap<PlayerController.Mouse,Boolean>();
	private Vector3 lastMouseCoordinates;

	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
		keys.put(Keys.QUIT, false);
	}

	static {
		mouseButtons.put(Mouse.SELECT, false);
		mouseButtons.put(Mouse.DOACTION, false);
	}

	private Entity player;
	public PlayerController(Entity player) {
		// TODO Auto-generated constructor stub
		this.lastMouseCoordinates=new Vector3();
		this.player=player;
	}

	@Override
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

	public void update(float delta) {
		//processInput(delta);
	}
	
	public static void hide() {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
		keys.put(Keys.QUIT, false);
	}
/*
	private void processInput(float delta) {
		if(keys.get(Keys.LEFT)) {
			player.calculateNextPosition(Entity.Direction.LEFT, delta);
			player.setState(Entity.State.WALKING);
			player.setDirection(Entity.Direction.LEFT, delta);			
		}
		else if(keys.get(Keys.RIGHT)) {
			player.calculateNextPosition(Entity.Direction.RIGHT, delta);
			player.setState(Entity.State.WALKING);
			player.setDirection(Entity.Direction.RIGHT, delta);			
		}
		else if(keys.get(Keys.UP)) {
			player.calculateNextPosition(Entity.Direction.UP, delta);
			player.setState(Entity.State.WALKING);
			player.setDirection(Entity.Direction.UP, delta);			
		}
		else if(keys.get(Keys.DOWN)) {
			player.calculateNextPosition(Entity.Direction.DOWN, delta);
			player.setState(Entity.State.WALKING);
			player.setDirection(Entity.Direction.DOWN, delta);			
		}
		else if(keys.get(Keys.QUIT)) {
			Gdx.app.exit();
		} else {
			player.setState(Entity.State.IDLE);
		}
		
		if(mouseButtons.get(Mouse.SELECT)) {
			mouseButtons.put(Mouse.SELECT, false);
		}
	}
*/
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
