package com.electricstover.bludborne;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.electricstover.bludborne.screens.MainGameScreen;

public class BludborneGame extends Game {
	public static final MainGameScreen mainGameScreen = new MainGameScreen();
	@Override
	public void create () {
		setScreen(mainGameScreen);
	}

	@Override
	public void dispose () {
		mainGameScreen.dispose();
	}
}
