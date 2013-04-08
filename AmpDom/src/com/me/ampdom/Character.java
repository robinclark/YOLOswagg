package com.me.ampdom;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Character  {
	
	protected Sprite sprite;
	protected SpriteBatch batch;
	protected Rectangle rect;
	protected int health = 100;
	protected boolean isKilled = false;
	protected Body entity;
    protected Texture texture;
    protected World world;
    protected boolean facingRight;
    protected boolean moveRight;
    protected boolean moveLeft;
    protected BodyDef entityDef;
    protected FixtureDef fixtureDef;
	protected float shape;
	protected float density;
	protected float friction;
	protected float restitution;
	protected ContactListener contactListener;
    public static final float PIXELS_PER_METER = 60.0f;
    
	public Character(World world, float x,float y){
//		texture = null;
//		rect = null;
//		sprite = null;
		//sprite = new Sprite(texture);
		//new World(new Vector2(0.0f, -10.0f), true);
		this.world = world;
		/**
		 * You can set the world's gravity in its constructor. Here, the gravity
		 * is negative in the y direction (as in, pulling things down).
		 */
		entityDef = new BodyDef();
		entityDef.type = BodyDef.BodyType.DynamicBody;
		//entityDef.position.set(2.0f, 2.0f);
		batch = new SpriteBatch();
//		world.createBody(entityDef);
	}

	public Character getCharacter() {
		return this;
	}
	
	public float getX(){
		return sprite.getX();
	}
	public float getY(){
		return sprite.getY();
	}
	public int getHealth(){
		return health;
		
	}
	
	public boolean isKilled(){
		return isKilled;
		
	}

	public boolean collision(Character c){
		return rect.overlaps(c.rect);
	}
	
	
	public void takeDamage(int dmg){
		health-=dmg;
	}
	public abstract void move();
	public abstract void attack();
    public abstract void speak();
    public abstract void reset();

    public void setHealth(int hp){
    	health = hp;
    }
    public void incHealth(int hp){
		health+=hp;
	}
    
    public void die() {
    }
    
	public void reset(float x, float y) {	
		
	}
    
}

	


