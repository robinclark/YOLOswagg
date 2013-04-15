package com.me.ampdom;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Dropper extends SpecialBody{
	
	public Dropper(World world, String path, float x, float y, float range, int width, int height)
	{
		super(world, path, x, y, range, width, height);
		body.getFixtureList().get(0).setSensor(true);
		body.setUserData("DROPPER");
	}
	
	public void check(float x, float y)
	{
		float xLoc = body.getPosition().x;
		float yLoc = body.getPosition().y;
				
		if(y < yLoc)
		{
			//if alabaster w/in range change body to dynamic
			if(x > (xLoc -1.5*range))
			{
				System.out.println("in range");
				body.setType(BodyDef.BodyType.DynamicBody);
			}
		}
	}
}

