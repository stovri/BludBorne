package com.electricstover.bludborne.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.electricstover.bludborne.BludborneGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title="BludBorne";
		config.useGL30=false;
		config.width=800;
		config.height=600;
		Application app=new LwjglApplication(new BludborneGame(), config);
		Gdx.app=app;
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
	}
}
