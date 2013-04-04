
package com.me.ampdom;

import com.badlogic.
gdx.backends.lwjgl.
LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Amphibian Dominion";
		cfg.useGL20 = false;
		cfg.width = 1028;
		cfg.height = 680;
		
		new LwjglApplication(new AmpDom(), cfg);
	}
}
