package com.me.ampdom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class SpecialBody {
	
	Body body;
	BodyDef bodyDef;
	FixtureDef fixtureDef;
	
	Texture texture;
	Sprite sprite;
	
	SpriteBatch batch = AmpDom.batch;
	
	float x;
	float y;
	float range;
	
	SpecialBody(World world, String path, float x, float y, float range, int width, int height)
	{
		//load tex
		texture = new Texture(Gdx.files.internal(path));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		sprite = new Sprite(texture, 0, 0, width, height);
		sprite.setScale(1.2f,1.0f);
		
		//body
		bodyDef = new BodyDef();
		body = world.createBody(bodyDef);
		bodyDef.type = BodyDef.BodyType.KinematicBody;
		bodyDef.position.set(x, y);
		
		body = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
	    shape.setAsBox(sprite.getWidth() / (2 * Character.PIXELS_PER_METER),
				sprite.getHeight() / (2 * Character.PIXELS_PER_METER));
	    
	    body.setFixedRotation(true);		
		
	    fixtureDef = new FixtureDef();
	    fixtureDef.shape = shape;
	    fixtureDef.density = 1.0f;
	    fixtureDef.friction = 0.2f;
		
		body.createFixture(fixtureDef);		
		shape.dispose();
		
		//batch = new SpriteBatch();
		
		this.x = x;
		this.y = y;
		this.range = range;
	}
	
	public void batchRender(TiledMapHelper tiledMapHelper) {
		batch.setProjectionMatrix(tiledMapHelper.getCamera().combined);
		batch.begin();
	
		sprite.setPosition(Character.PIXELS_PER_METER * body.getPosition().x- sprite.getWidth() / 2,
				Character.PIXELS_PER_METER * body.getPosition().y
						- sprite.getHeight() / 2);
		sprite.draw(batch);
		batch.end();		
	}
}

