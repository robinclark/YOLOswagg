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

public class MovingPlat extends Character {

	float speedY = 2.0f;
	float speedX = 2.0f;
	float spawnX;
	float spawnY;
	int motion;
	
	public MovingPlat(World world, String path, float x, float y,int moveType,int direction) {
		super(world, x, y);
		motion = moveType;
		speedY*=direction;
		BodyDef entityDef = new BodyDef();
		entity = world.createBody(entityDef);
		entityDef.type = BodyDef.BodyType.KinematicBody;
		entityDef.position.set(x, y);
		/** * Load up the overall texture and chop it in to pieces. In this case,* piece.*/
		
		texture = new Texture(Gdx.files.internal(path));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		sprite = new Sprite(texture, 0, 0, 128, 32);
		rect = new Rectangle(x,y,texture.getWidth(),texture.getHeight());
		/**
		 * Boxes are defined by their "half width" and "half height", hence the
		 * 2 multiplier.
		 */
		PolygonShape shape = new PolygonShape();
	    shape.setAsBox(sprite.getWidth() / (2 * PIXELS_PER_METER),
				sprite.getHeight() / (2 * PIXELS_PER_METER));

	
		entity.setFixedRotation(true);
		

	
		FixtureDef entityFix = new FixtureDef();
		entityFix.shape = shape;
		entityFix.density = 0.0f;
		entityFix.friction = 0.2f;
		entityFix.restitution = 0.0f;
		entity = world.createBody(entityDef);
		entity.createFixture(entityFix);
		spawnX = entity.getPosition().x;
		spawnY = entity.getPosition().y;
		entity.setUserData("GROUND");
		shape.dispose();
	}
	
	public MovingPlat getObstacle() {
		return this;
	}

	@Override
	public void move() {
		
		if(motion==0){
		if(entity.getPosition().y<(spawnY-2.5f)) 
			speedY = 2.0f;
       if (entity.getPosition().y>(spawnY+2.5f)) 
			speedY = -2.0f;
		entity.setLinearVelocity(0, speedY);
		
		
		}
		if(motion==1){
				if(entity.getPosition().x<(spawnX-2.5f)) 
					speedY = 2.0f;
		       if (entity.getPosition().x>(spawnX+2.5f)) 
					speedY = -2.0f;
				entity.setLinearVelocity(speedY, 0);
			}
		}
	
		
	

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
