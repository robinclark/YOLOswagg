package com.me.ampdom;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Spit {
	Body b;

	Spit(World world, BodyDef bDef, FixtureDef fDef)
	{
		b = world.createBody(bDef);
		b.createFixture(fDef);
	}
	
	
}
