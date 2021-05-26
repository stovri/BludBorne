package com.electricstover.bludborne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Json;
import com.electricstover.bludborne.BludborneGame;
import com.electricstover.bludborne.Entity;
import com.electricstover.bludborne.EntityFactory;
import com.electricstover.bludborne.PlayerController;
import com.electricstover.bludborne.Utility;
import com.electricstover.bludborne.Components.Component;
import com.electricstover.bludborne.maps.Map;
import com.electricstover.bludborne.maps.MapManager;

/**
 * The main game screen usded to display the game map, player avatar, and 
 * any UI components.
 */
public class MainGameScreen implements Screen {
	/**
	 * Stores the name of the class for use in the log files.
	 */
	private static final String TAG=MainGameScreen.class.getSimpleName();

	/**
	 * Stores the attributes of the viewport
	 * @author stovri
	 *
	 */
	public static class VIEWPORT{
		public static float viewportWidth;
		public static float viewportHeight;
		static float virtualWidth;
		static float virtualHeight;
		static float physicalWidth;
		static float physicalHeight;
		static float aspectRatio;
	}

	private PlayerController controller;
	private TextureRegion currentPlayerFrame;
	private Sprite currentPlayerSprite;

	private OrthogonalTiledMapRenderer mapRenderer=null;
	private OrthographicCamera camera=null;
	private BludborneGame game;
	private Json json;
	private static MapManager mapMgr;

	public MainGameScreen() {
		mapMgr=new MapManager();
		json=new Json();		
	}

	private static Entity player;

	@Override
	public void show() {
		// camera setup
		setupViewport(10, 10);

		//get the current size
		camera = new OrthographicCamera();
		camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

		mapRenderer=new OrthogonalTiledMapRenderer(mapMgr.getCurrentTiledMap(),Map.UNIT_SCALE);
		mapRenderer.setView(camera);
		
		mapMgr.setCamera(camera);

		Gdx.app.debug(TAG,"UnitScale value is "+mapRenderer.getUnitScale());

		player=EntityFactory.getEntity(EntityFactory.EntityType.PLAYER);
		mapMgr.setPlayer(player);
		

	}

	private void setupViewport(int width, int height) {
		// TODO Auto-generated method stub
		VIEWPORT.virtualWidth=width;
		VIEWPORT.virtualHeight=height;

		VIEWPORT.viewportWidth=width;
		VIEWPORT.viewportHeight=height;

		VIEWPORT.physicalWidth=Gdx.graphics.getWidth();
		VIEWPORT.physicalHeight=Gdx.graphics.getHeight();

		VIEWPORT.aspectRatio=(VIEWPORT.viewportWidth/VIEWPORT.virtualHeight);

		if(VIEWPORT.physicalWidth/VIEWPORT.physicalHeight>=VIEWPORT.aspectRatio) {
			VIEWPORT.viewportWidth=VIEWPORT.viewportHeight*(VIEWPORT.physicalWidth/VIEWPORT.physicalHeight);
			VIEWPORT.viewportHeight=VIEWPORT.virtualHeight;
		}
		else {
			VIEWPORT.viewportWidth=VIEWPORT.virtualWidth;
			VIEWPORT.viewportHeight=VIEWPORT.viewportWidth*(VIEWPORT.physicalHeight/VIEWPORT.physicalWidth);
		}

		Gdx.app.debug(TAG, "WorldRenderer: virtual: ("+VIEWPORT.virtualWidth+","+VIEWPORT.viewportWidth+")");
		Gdx.app.debug(TAG, "WorldRenderer: viewport :("+VIEWPORT.virtualWidth+","+VIEWPORT.viewportHeight+")");
		Gdx.app.debug(TAG, "Worldrenderer: physical :("+VIEWPORT.physicalWidth+","+VIEWPORT.physicalHeight+")");
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mapRenderer.setView(camera);
		if( mapMgr.hasMapChanged() ){
			mapRenderer.setMap(mapMgr.getCurrentTiledMap());
			player.sendMessage(Component.MESSAGE.INIT_START_POSITION, json.toJson(mapMgr.getPlayerStartUnitScaled()));

			float x=0;
			float y=0;
			if(mapMgr.getPlayerStartUnitScaled().x<VIEWPORT.viewportWidth/2)
				x=VIEWPORT.viewportWidth/2;
			else if(mapMgr.getPlayerStartUnitScaled().x>mapMgr.getCurrentTiledMap().getProperties().get("width", Integer.class)-VIEWPORT.viewportWidth/2)
				x=mapMgr.getCurrentTiledMap().getProperties().get("width", Integer.class)-VIEWPORT.viewportWidth/2;
			else
				x=mapMgr.getPlayerStartUnitScaled().x;
			if(mapMgr.getPlayerStartUnitScaled().y<VIEWPORT.viewportHeight/2)
				y=VIEWPORT.viewportHeight/2;
			else if(mapMgr.getPlayerStartUnitScaled().y>mapMgr.getCurrentTiledMap().getProperties().get("height", Integer.class)-VIEWPORT.viewportHeight/2)
				y=mapMgr.getCurrentTiledMap().getProperties().get("height", Integer.class)-VIEWPORT.viewportHeight/2;
			else
				y=mapMgr.getPlayerStartUnitScaled().y;
			camera.position.set(x, y, 0f);

			camera.update();

			mapMgr.setMapChanged(false);
		}

		mapRenderer.render();

		mapMgr.updateCurrentMapEntities(mapMgr, mapRenderer.getBatch(), delta );

		player.update(mapMgr, mapRenderer.getBatch(), delta);
	}

	/*private void drawRects(ShapeRenderer sRender, Rectangle boundingBox) {
		// TODO Auto-generated method stub
		TiledMapTileLayer mapCollisionLayer=mapMgr.getCollisionLayer();

		Rectangle rectangle = null;
		for(int i=0;i<mapCollisionLayer.getWidth();i++) {
			for(int j=0;j<mapCollisionLayer.getHeight();j++) {
				Cell cell=mapCollisionLayer.getCell(i, j);
				if(cell!=null) {
					for(MapObject object:cell.getTile().getObjects()) {
						if(object instanceof RectangleMapObject) {
							rectangle=new Rectangle(((RectangleMapObject)object).getRectangle());
							float x=(i*16)+rectangle.x;
							float y=(j*16)+rectangle.y;
							rectangle.setPosition(x, y);

							sRender.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
						}
					}
				}
			}
		}
		sRender.rect(boundingBox.getX(), boundingBox.getY(), boundingBox.getWidth(), boundingBox.getHeight());
	}

	*/

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		player.dispose();
		mapRenderer.dispose();
	}

}
