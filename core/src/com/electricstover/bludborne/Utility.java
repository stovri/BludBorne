package com.electricstover.bludborne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * 
 * @author stovri
 * The Utility class represents a placeholder for various methods including 
 * dealing with the loading and unloading of game assets.
 */
public final class Utility {
	/**
	 * Manages the loading a storing of assets such as textures, bitmap fonts, 
	 * particle effects, pixmaps, UI skins, tile maps, sounds, and music.
	 */
	public static final AssetManager assetManager= new AssetManager();
	/**
	 * Stores the name of the class for use in the log files.
	 */
	private static final String TAG=Utility.class.getSimpleName();
	/**
	 * A convenience class for managing file handles when resolving paths with 
	 * assets relative to the current working directory.
	 */
	
	public static final boolean DEBUG_MODE=false;
	private static InternalFileHandleResolver filePathResolver=new InternalFileHandleResolver();

	/**
	 * 
	 * @param assetFileNamePath the name of the file, as a path
	 * 
	 * This method will check to see whether the asset is loaded, and if it is, 
	 * then it will unload the asset from memory.
	 */
	public static void unloadAsset(String assetFileNamePath) {
		if(assetManager.isLoaded(assetFileNamePath)) {
			assetManager.unload(assetFileNamePath);
		}
		else {
			Gdx.app.debug(TAG, "Asset is not loaded Nothing to unload: "+assetFileNamePath);
		}
	}

	/**
	 * This method will wrap the progress of AssetManager as a percentage of completion.
	 * @return the percentage of assets loaded.
	 */
	public static float loadCompleted() {
		return assetManager.getProgress();
	}

	/**
	 * Wraps the number of assets left to load from the AsseManager queue.
	 * @return the number of assets left to load
	 */
	public static int numberAssetsQueued() {
		return assetManager.getQueuedAssets();
	}

	/**
	 * Wraps the update call in AssetManger and can be called in a render loop,
	 * if loading assets asynchronously in order to process the preload queue. 
	 * @return true if all loading is finished
	 */
	public static boolean updateAssetLoading() {
		return assetManager.update();
	}

	/**
	 * Wraps the AssetManager method isLoaded
	 * @param fileName the name of the file holding the asset
	 * @return true if the asset is loaded.
	 */
	public static boolean isAssetLoaded(String fileName) {
		return assetManager.isLoaded(fileName);
	}

	/**
	 * Takes a TMX filename path relative to the working directory and load the
	 *  TMX file as a TiledMap asset, blocking until finished. 
	 * @param mapFilenamePath The name of the TMX file relative to the working
	 * directory
	 */
	public static void loadMapAsset(String mapFilenamePath) {
		if(mapFilenamePath==null||mapFilenamePath.isEmpty()) {
			return;
		}
		//load asset
		if(filePathResolver.resolve(mapFilenamePath).exists()) {
			assetManager.setLoader(TiledMap.class, new TmxMapLoader(filePathResolver));
			assetManager.load(mapFilenamePath, TiledMap.class);
			//TODO fix after adding loading screen
			//just block until we load the map
			assetManager.finishLoadingAsset(mapFilenamePath);
			Gdx.app.debug(TAG, "Map Loaded!: "+mapFilenamePath);
		}
		else {
			Gdx.app.debug(TAG, "Map doesn't exist!: "+mapFilenamePath);
		}

	}
	
	/**
	 * Returns the TiledMap loaded from the provided filename path
	 * @param mapFilenamePath the name of the TMX file relative to the working
	 * directory.
	 * @return TiledMap object with the asset data.
	 */
	public static TiledMap getMapAsset(String mapFilenamePath) {
		TiledMap map=null;
		
		//once asset manager is done loading
		if(assetManager.isLoaded(mapFilenamePath)) {
		map=assetManager.get(mapFilenamePath, TiledMap.class);
		}
		else {
			Gdx.app.debug(TAG, "Map is not loaded.: "+mapFilenamePath);
		}
		return map;
	}
	
	/**
	 * Takes the image filename path relative to the working directory and load
	 * the image file as a Texture asset, blocking until finished.
	 * @param textureFilenamePath the filename path relative to the working 
	 * directory
	 */
	public static void loadTextureAsset(String textureFilenamePath) {
		if(textureFilenamePath==null||textureFilenamePath.isEmpty()) {
			return;
		}
		//load asset
		if(filePathResolver.resolve(textureFilenamePath).exists()) {
			assetManager.setLoader(Texture.class, new TextureLoader(filePathResolver));
			assetManager.load(textureFilenamePath, Texture.class);
			
			//TODO fix after adding loading screen
			assetManager.finishLoadingAsset(textureFilenamePath);
		}
		else {
			Gdx.app.debug(TAG, "Texture does not exist: "+textureFilenamePath);
		}
	}
	
	/**
	 * Loads the specified texture into a Texture object
	 * @param textureFilenamePath the filename path of the desired texture;
	 * @return Texture object loaded with the specified data
	 */
	public static Texture getTextureAsset(String textureFilenamePath) {
		Texture texture=null;
		
		//once the asset manager is done loading
		if(assetManager.isLoaded(textureFilenamePath)) {
			texture=assetManager.get(textureFilenamePath, Texture.class);
		}
		else {
			Gdx.app.debug(TAG, "Texture is not loaded: "+textureFilenamePath);
		}
		
		return texture;
	}

}
