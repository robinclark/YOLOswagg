package com.me.ampdom;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class FlyingEnemy extends Character {

	float speedX = 2.0f;
	float speedY = 1.0f;
	int count=0;
	float spawnX;
	float spawnY;
	float spawnX0;
	float spawnY0;
	Body directPos;
	float patX,patY;
	boolean directMove = false;
    protected float ElapsedTime;
    protected long BeginTime;
	
	ParticleEffect particleEffect;
    float fpsCounter;
	Sound explosion;
	
	public FlyingEnemy(World world, String path, float x, float y,int sizeW,int sizeH) {
		super(world, x, y);
		
		BodyDef entityDef = new BodyDef();
		entity = world.createBody(entityDef);
		entityDef.type = BodyDef.BodyType.DynamicBody;
		entityDef.position.set(x, y);
		
		texture = new Texture(Gdx.files.internal(path));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		sprite = new Sprite(texture, 0, 0, sizeW, sizeH);
		sprite.setScale(1.5f,1.5f);
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
		fixtureDef.density = 1.0f;
		//FixtureDef.friction = 5.0f;
		fixtureDef.restitution = 0.0f;
		fixtureDef.isSensor = false;
		entity = world.createBody(entityDef);
		entity.createFixture(fixtureDef);
		spawnX = entity.getPosition().x;
		spawnY = entity.getPosition().y;
		spawnX0 = spawnX;
		spawnY0 = spawnY;
		entity.setUserData("ENEMY");
		shape.dispose();
		facingRight = false;
		
		particleEffect = new ParticleEffect();
	    particleEffect.load(Gdx.files.internal("data/redFire.p"), Gdx.files.internal("data"));
	    particleEffect.setPosition(PIXELS_PER_METER * entity.getPosition().x,
				PIXELS_PER_METER * entity.getPosition().y-3
				);
		explosion = Gdx.audio.newSound(Gdx.files.getFileHandle("data/sounds/Explosion.wav", FileType.Internal));
	}
	
	public FlyingEnemy getFlyingEnemy() {
		return this;
	}

	@Override
	public void move() {
		
		if(directMove == false) {
			if(entity.getPosition().x<(spawnX-3.0f)) {
				speedX = 2.0f;
				//facingRight = true;
				
			}
			else if (entity.getPosition().x>(spawnX+3.0f)) {
				speedX = -2.0f;
				//facingRight = false;
				
			}

			if(entity.getPosition().y<(spawnY-1.5f))
				speedY = 1.0f;
			if(entity.getPosition().y>(spawnY+1.5f))
				speedY = -1.0f;

		
		entity.setLinearVelocity(speedX, speedY);
		
		//if (facingRight) {
			//sprite.flip(true, false);
			//facingRight = true;
		//}
		//else {//(facingRight == true) {
			//sprite.flip(true, false);
			//facingRight = false;
		//}
	}

	else {	
	if(entity.getPosition().x<(spawnX)) {
			speedX = 2.0f;
			//facingRight = true;
		}
		else if (entity.getPosition().x>(spawnX)) {
			speedX = -2.0f;
			//facingRight = false;
		}

		if(entity.getPosition().y<(spawnY))
			speedY = 1.0f;
		if(entity.getPosition().y>(spawnY))
			speedY = -1.0f;

	entity.setLinearVelocity(1.6f*speedX, 1.8f*speedY);
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
	
	public void die() {
		BeginTime = System.nanoTime();
		EnemyContact.enemyDmg=false;
		EnemyContact.isHit=false;
		EnemyContact.insideEnemy=false;
		sprite.setScale(0, 0);
	    entity.setActive(false);
	    //ElapsedTime = (System.nanoTime() - BeginTime)/1000000000.0f;
	    explosion.play();
	}

	public void batchRender(TiledMapHelper tiledMapHelper) {
		batch.setProjectionMatrix(tiledMapHelper.getCamera().combined);
		batch.begin();
		
		float delta = Gdx.graphics.getDeltaTime();
		sprite.setPosition(PIXELS_PER_METER * entity.getPosition().x- sprite.getWidth() / 2,
				PIXELS_PER_METER * entity.getPosition().y
						- sprite.getHeight() / 2);
		sprite.draw(batch);
	
		fpsCounter += delta;
		if (fpsCounter > 3 )
			fpsCounter = 0;
		if(isKilled) {
			particleEffect.start();
			particleEffect.draw(batch, delta);
			ElapsedTime = (System.nanoTime() - BeginTime)/1000000000.0f;
		}
		
		if(ElapsedTime > 2) {
			isKilled = false;
			BeginTime = 0;
			ElapsedTime = 0;
		}
		batch.end();
		
	}
	
	@Override
	public void reset(float x, float y) {
		entityDef.position.set(x, y);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	
	
	
}

