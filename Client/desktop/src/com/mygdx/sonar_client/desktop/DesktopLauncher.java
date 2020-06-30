package com.mygdx.sonar_client.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.sonar_client.Client;
import com.mygdx.sonar_client.Settings;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "temp";
		config.width = Settings.RES_WIDTH;
		config.height = Settings.RES_HEIGHT;

		new LwjglApplication(new Client(), config);
	}
}
