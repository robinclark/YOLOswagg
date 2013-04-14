package com.me.ampdom;

import com.badlogic.gdx.physics.box2d.World;

public class MovingPlatform extends SpecialBody{
	float velocity = 2.0f;
	boolean vert;
	public MovingPlatform(World world, String path, float x, float y, float range, boolean vert, int width, int height)
	{
		super(world, path, x, y, range, width, height);		
		body.setUserData("MOVING_PLATFORM");
		this.vert = vert;
		
		if(vert)
			body.setLinearVelocity(0.0f, velocity);
		else
			body.setLinearVelocity(velocity, 0.0f);
	}
	
	public void move()
	{
		float yUpper, yLower, xUpper, xLower;
		yUpper = (y + range);
		yLower = (y - range);
		xUpper = (x + range);
		xLower = (x - range);
		
		if(!vert)
		{
			System.out.println("x: " + xLower + ", " + xUpper);
		}
		
		if(vert)
		{
			if(body.getPosition().y > (y + range))
			{
				body.setLinearVelocity(0.0f, -velocity);
			}
			if(body.getPosition().y < (y - range))
			{
				body.setLinearVelocity(0.0f, velocity);
			}
		}
		else
		{
			if(body.getPosition().x > (x + range))
			{
				//System.out.println("negative");
				body.setLinearVelocity(-velocity, 0.0f);
			}
			if(body.getPosition().x < (x - range))
			{				
				body.setLinearVelocity(velocity, 0.0f);
			}
		}
	}
}
