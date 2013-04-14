package com.me.ampdom;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class WaterLog extends SpecialBody{
	float initHeight;
	
	public WaterLog(World world, String path, float x, float y, float range, int width, int height)
	{
		super(world, path, x, y, range, width, height);
		body.setType(BodyDef.BodyType.DynamicBody);
		body.setUserData("WATER_LOG");
		initHeight = y;
	}
	
	public void fall()
	{
		float yLoc = body.getPosition().y;
		
		if(yLoc <= initHeight-range)
		{
			body.setTransform(x, initHeight, 0);
		}
	}
}
