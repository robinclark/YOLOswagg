package com.me.ampdom;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Spit {
	public Body body;
	public float x, y;
	public float bodyOffset = 0.5f;
	public final float spitVel = 10.0f;
	
	public Spit(World w, BodyDef spitBodyDef, FixtureDef spitFixtureDef, boolean facingRight, float x, float y)
	{
		this.body = w.createBody(spitBodyDef);		
		this.body.createFixture(spitFixtureDef);
		
		if(facingRight)
			this.body.setUserData("RIGHT_SPIT");
		else
			this.body.setUserData("LEFT_SPIT");
		
		if(facingRight)
		{
			body.setTransform(x, y,0);
			body.setLinearVelocity(spitVel, 0.0f);
		}
		else
		{
			body.setTransform(x, y,0);
			body.setLinearVelocity(-spitVel, 0.0f);
		}	
		
		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;		
	}
}
