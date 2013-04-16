package com.me.ampdom;

import com.badlogic.gdx.physics.box2d.World;

public class Sandstorm extends SpecialBody{
	float velocity = -10.0f;
	
	public Sandstorm(World world, String path, float x, float y, float range, int width, int height)
	{
		super(world, path, x, y, range, width, height);		
		body.getFixtureList().get(0).setSensor(true);
		body.setUserData("SANDSTORM");
		range *=2;
	}
	
	//attack when frog in range
	public void attack(float x)
	{
		if(x > this.x - range)
		{
			body.setLinearVelocity(velocity, 0.0f);
		}
		if(this.x < (x))
		{
			body.setActive(false);
		}
	}

}
