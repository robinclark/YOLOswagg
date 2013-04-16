package com.me.ampdom;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Obstacle extends Character {

	float speed = 2.0f;
	int count=0;
	float spawnX;
	float spawnY;
	
	public Obstacle(World world, String path, float x, float y,int sizeW,int sizeH) {
		super(world, x, y);
		
		BodyDef entityDef = new BodyDef();
		entity = world.createBody(entityDef);
		entityDef.type = BodyDef.BodyType.DynamicBody;
		entityDef.position.set(x, y);
		/** * Load up the overall texture and chop it in to pieces. In this case,* piece.*/
		
		texture = new Texture(Gdx.files.internal(path));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		sprite = new Sprite(texture, 0, 0, sizeW, sizeH);
		rect = new Rectangle(x,y,texture.getWidth(),texture.getHeight());
		/**
		 * Boxes are defined by their "half width" and "half height", hence the
		 * 2 multiplier.
		 */
		PolygonShape shape = new PolygonShape();
	    shape.setAsBox(sprite.getWidth() / (2 * PIXELS_PER_METER),
				sprite.getHeight() / (2 * PIXELS_PER_METER));

	
		entity.setFixedRotation(true);
		

	
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.0f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 0.0f;
		entity = world.createBody(entityDef);
		entity.createFixture(fixtureDef);
		spawnX = entity.getPosition().x;
		spawnY = entity.getPosition().y;
		entity.setUserData("OBSTACLE");
		shape.dispose();
		facingRight = false;
	}
	
	public Obstacle getObstacle() {
		return this;
	}

	@Override
	public void move() {
	
	
	}
		// TODO Auto-generated method stub
		
	

	@Override
	public void attack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void speak() {
		// TODO Auto-generated method stub
		
	}

	public void batchRender(TiledMapHelper tiledMapHelper) {
		batch.setProjectionMatrix(tiledMapHelper.getCamera().combined);
		batch.begin();
	
		sprite.setPosition(PIXELS_PER_METER * entity.getPosition().x- sprite.getWidth() / 2,
				PIXELS_PER_METER * entity.getPosition().y
						- sprite.getHeight() / 2);
		sprite.draw(batch);
		batch.end();
		
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}


	
	public void reset(float x, float y) {
		entityDef.position.set(x, y);
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}

