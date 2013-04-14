package com.me.ampdom;

import com.badlogic.gdx.physics.box2d.World;

public class StationaryPlatform extends SpecialBody{
	
	public StationaryPlatform(World world, String path, float x, float y, float range, int width, int height)
	{
		super(world, path, x, y, range, width, height);
		
		body.setUserData("STATIONARY_PLATFORM");
	}
}
