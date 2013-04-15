package com.me.ampdom;

import java.util.ArrayList;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class Alabaster extends Character {
	//position
	float x,y;
	
	//body offset
	final float bodyOffset = 0.5f;
	
	//double jump
	static int count=2;
	
	//bools	
	boolean doubleJump;
	boolean shell = false;
	boolean shout = false;
	boolean tongueAct=false;
	boolean powerLegs=true;
	boolean spit = false;
	boolean tongueOut = false;

	//boolean shoutOut = false;
	//boolean doubleJump;

	//shell stuff
	Sprite shellSprite;
	Texture shellText;
	Sound shellSound;
	
	//tongue stuff
	Sprite tongueSprite;
	Texture tongueText;
	FixtureDef tongueFixtureDef;
	Body tongueBody;
	BodyDef tongueBodyDef;
	Sound tongueSound;
	
	//shout stuff
	Sprite shoutSprite;
	Texture shoutText;
	FixtureDef shoutFixtureDef;
	Body shoutBody;
	BodyDef shoutBodyDef;
	Sound shoutSound;
	
	//foot
	FixtureDef footFix;
	Body foot;
	BodyDef footDef;
	
	//spit stuff
	Sprite leftSpitSprite;
	Texture leftSpitTexture;
	FixtureDef leftSpitFixtureDef;
	
	Sprite spitSprite;
	Texture spitTexture;
	FixtureDef spitFixtureDef;
	Body spitBody;
	BodyDef spitBodyDef;
	Sound spitSound;
	
	ArrayList<Spit> spits = new ArrayList<Spit>();
	ArrayList<Spit> spitsToDestroy = new ArrayList<Spit>();
	
	//sounds
	Sound jumpSound;	
	
	//doublejump sensor
	FixtureDef doubleJumpFixtureDef;
	Body doubleJumpBody;
	BodyDef doubleJumpBodyDef;

	//foot block body
	FixtureDef wallFixDef;
	Body wallBody;
	BodyDef wallBodyDef;

	
	// HUD
	private float k;
	protected SpriteBatch icon = AmpDom.batch;
	private SpriteBatch healthText = AmpDom.batch;
	private BitmapFont font;
	private ShapeRenderer shapeRenderer;
	protected int shellCharge = 100;
	protected int spitCharge = 100;
	protected int shoutCharge = 100;
	private Texture iconTexture;
	Texture textureMove;
	
	final float spitVel = 10.0f;
	
	long now, last, shoutTime;
	
	public Alabaster(World world, float x, float y) {
		super(world, x, y);	
		/*setup physics n sounds*/
		
		//--alabaster
		textureMove = new Texture(Gdx.files.internal("data/Alabaster/alabasterM.png"));
		texture = new Texture(Gdx.files.internal("data/Alabaster/alabasterS.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textureMove.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		sprite = new Sprite(texture, 0, 0, 64, 51);
		
		
		entityDef.position.set(x, y);
		entity = world.createBody(entityDef);
		
		PolygonShape shape = new PolygonShape();
	    shape.setAsBox(sprite.getWidth() / (2 * PIXELS_PER_METER),
				sprite.getHeight() / (2 * PIXELS_PER_METER));
	    
	    entity.setFixedRotation(true);		
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 1.0f;
		
		entity.createFixture(fixtureDef);		
		//shape.dispose();
		
		entity.setUserData("PLAYER");
		
		/******************************************************/

		//--shout
		 shoutText = new Texture(Gdx.files.internal("data/shockwave.png"));
		 shoutText.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	     shoutSprite = new Sprite(shoutText, 0, 0, 64, 128);
	     
	     shoutBodyDef = new BodyDef();
		 shoutBodyDef.type = BodyDef.BodyType.StaticBody;
		 shoutBodyDef.position.set(x, y);
		 
	   	 shoutBody = world.createBody(shoutBodyDef);
	   	 
	   	 shoutSound = Gdx.audio.newSound(Gdx.files.getFileHandle("data/sounds/RandomSound.wav", FileType.Internal));
	   	 
	   	 PolygonShape shoutShape = new PolygonShape();
		    shoutShape.setAsBox(shoutSprite.getWidth() / ( 2f*PIXELS_PER_METER),
					sprite.getHeight() / PIXELS_PER_METER);			
			
		shoutFixtureDef = new FixtureDef();
		shoutFixtureDef.shape = shoutShape;
		shoutFixtureDef.density = 1.0f;
		shoutFixtureDef.friction = 1.0f;

		
		
		shoutBody.createFixture(shoutFixtureDef);

		shoutShape.dispose();
		
		shoutBody.setUserData("SHOUT");
		
		//--tongue
	   	tongueText = new Texture(Gdx.files.internal("data/alabaster/tongue.png"));
	    tongueSprite = new Sprite(tongueText, 0, 0, 64, 64);
	   	 
	   	tongueBodyDef = new BodyDef();
	   	tongueBodyDef.type = BodyDef.BodyType.StaticBody;
	   	tongueBodyDef.position.set(entity.getPosition().x,entity.getPosition().y);
	   	
		tongueBody = world.createBody(tongueBodyDef);
		
		tongueSound = Gdx.audio.newSound(Gdx.files.getFileHandle("data/sounds/jump.wav", FileType.Internal));
		
		PolygonShape tongueShape = new PolygonShape();
		tongueShape.setAsBox(tongueSprite.getWidth() / (2 * PIXELS_PER_METER),
					tongueSprite.getHeight() / (5 * PIXELS_PER_METER));
		
		tongueBody.setFixedRotation(true);
		    
		tongueFixtureDef = new FixtureDef();
		tongueFixtureDef.shape = tongueShape;
		tongueFixtureDef.density = 1.0f;
		tongueFixtureDef.friction = 1.0f;
		
		tongueBody.createFixture(tongueFixtureDef);
		
		tongueShape.dispose();
		
		tongueBody.setUserData("TONGUE");
		
		//--foot
		
		 footDef = new BodyDef();
		 footDef.type = BodyDef.BodyType.DynamicBody;
		 footDef.position.set(entity.getPosition().x,entity.getPosition().y);
	   	 foot = world.createBody(footDef);
	   	 PolygonShape footShape = new PolygonShape();
		    footShape.setAsBox(sprite.getWidth() / (2.3F * PIXELS_PER_METER),
					sprite.getHeight() / (8f * PIXELS_PER_METER));
		    foot.setFixedRotation(true);
			 
		footFix = new FixtureDef();
	    footFix.shape= footShape;
	    footFix.isSensor=true;
		foot.createFixture(footFix);
		footShape.dispose();
		
		foot.setUserData("FOOT");
		    
		//--spit		
		leftSpitTexture = new Texture(Gdx.files.internal("data/leftSpit.png"));
		leftSpitSprite = new Sprite(leftSpitTexture, 0, 0, 64, 64);
		
		spitTexture = new Texture(Gdx.files.internal("data/spit.png"));
	    spitSprite = new Sprite(spitTexture, 0, 0, 64, 64);
	   	 
	   	spitBodyDef = new BodyDef();
	   	spitBodyDef.type = BodyDef.BodyType.KinematicBody;
	   	//spitBodyDef.position.set(entity.getPosition().x+bodyOffset,entity.getPosition().y + bodyOffset);
	   	
		spitBody = world.createBody(spitBodyDef);
		
		spitSound = Gdx.audio.newSound(Gdx.files.getFileHandle("data/sounds/bearLaser.wav", FileType.Internal));
		
		PolygonShape spitShape = new PolygonShape();
		spitShape.setAsBox(spitSprite.getWidth() / (2*PIXELS_PER_METER),
					spitSprite.getHeight() / (2*PIXELS_PER_METER));
		
		spitBody.setFixedRotation(true);
		    
		spitFixtureDef = new FixtureDef();
		
		/*set bullet as sensor*/
		spitFixtureDef.isSensor = true;		
		spitFixtureDef.shape = spitShape;
		spitFixtureDef.density = 1.0f;
		spitFixtureDef.friction = 1.0f;
		
		spitBody.createFixture(spitFixtureDef);
		
		//spitShape.dispose();
		
		spitBody.setUserData("SPIT");
		
		//--shell			     
		shellText = new Texture(Gdx.files.internal("data/flyJar.png"));
	    shellSprite = new Sprite(shellText, 0, 0, 32, 32);
	    shellSound = Gdx.audio.newSound(Gdx.files.getFileHandle("data/sounds/shellSound.wav", FileType.Internal));
	    
	    /*sounds*/
	    //--jump
	    jumpSound = Gdx.audio.newSound(Gdx.files.getFileHandle("data/sounds/jump.wav", FileType.Internal));
		
		// HUD
		healthText = new SpriteBatch();
		font = new BitmapFont();
	    font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		shapeRenderer = new ShapeRenderer();
		
		// alabaster head
		iconTexture = new Texture(Gdx.files.internal("data/Alabaster/alabasterFace.png"));
		icon = new SpriteBatch();//texture, 0, 0, 0, 32);
		//icon.flip(true, false);
	}

public void displayHUD() {   
	// display health
	k = (100f - health) / 100f;
	
	shapeRenderer.begin(ShapeType.FilledRectangle);
	
	// health
	shapeRenderer.setColor(0f, 0f, 0f, 1.0f);
	shapeRenderer.identity();
	shapeRenderer.translate(40.0f, (680-45), 0.f);
	shapeRenderer.filledRect(0f, 0f, 200, 20f);
	shapeRenderer.setColor((255*k)/100, 255*(1.0f - k)/100, 0f, 1.0f);
	shapeRenderer.identity();
	shapeRenderer.translate(40.0f, (680-45), 0.f);
	shapeRenderer.filledRect(0f, 0f, 2*health, 20f);
	
	// shell
	shapeRenderer.setColor(0f, 0f, 0f, 1.0f);
	shapeRenderer.identity();
	
	if(health > 100)
		shapeRenderer.translate(2*health+10+40, (680-40), 0.f);
	else
		shapeRenderer.translate(2*100+10+40, (680-40), 0.f);
	
	shapeRenderer.filledRect(0f, 0f, 100, 15f);
	shapeRenderer.setColor(1.0f, .77f, .05f, 1.0f);
	shapeRenderer.identity();
	
	if(health > 100)
		shapeRenderer.translate(2*health+10+40, (680-40), 0.f);
	else
		shapeRenderer.translate(2*100+10+40, (680-40), 0.f);
	
	shapeRenderer.filledRect(0f, 0f, shellCharge, 15f);
	
	// spit
	shapeRenderer.setColor(0f, 0f, 0f, 1.0f);
	shapeRenderer.identity();
	
	if(health > 100)
		shapeRenderer.translate(2*health+40+120, (680-40), 0.f);
	else
		shapeRenderer.translate(2*100+40+120, (680-40), 0.f);
	
	shapeRenderer.filledRect(0f, 0f, 100, 15f);
	shapeRenderer.setColor(0.52f, 0.38f, .53f, 1.0f);
	shapeRenderer.identity();
	
	if(health > 100)
		shapeRenderer.translate(2*health+40+120, (680-40), 0.f);
	else
		shapeRenderer.translate(2*100+40+120, (680-40), 0.f);
	
	shapeRenderer.filledRect(0f, 0f, spitCharge, 15f);
	
	// shock-wave
	shapeRenderer.setColor(0f, 0f, 0f, 1.0f);
	shapeRenderer.identity();
	
	if(health > 100)
		shapeRenderer.translate(2*health+40+230, (680-40), 0.f);
	else
		shapeRenderer.translate(2*100+40+230, (680-40), 0.f);
	
	shapeRenderer.filledRect(0f, 0f, 100, 15f);
	shapeRenderer.setColor(0.18f, 0.46f, 1.0f, 1.0f);
	shapeRenderer.identity();
	
	if(health > 100)
		shapeRenderer.translate(2*health+40+230, (680-40), 0.f);
	else
		shapeRenderer.translate(2*100+40+230, (680-40), 0.f);
	
	shapeRenderer.filledRect(0f, 0f, shoutCharge, 15f);
	
	// Icon Background
	shapeRenderer.setColor(0f, 0f, 0f, 1.0f);
	shapeRenderer.identity();
	shapeRenderer.translate(0f, (680-50), 0.f);
	shapeRenderer.filledRect(0f, 0f, 40f, 40f);
	shapeRenderer.end();
	
	// BORDER
	shapeRenderer.begin(ShapeType.Rectangle);
	
	// health
	shapeRenderer.setColor((255*k)/100, 255*(1.0f - k)/100, 0f, 1.0f);
	shapeRenderer.identity();
	shapeRenderer.translate(40.0f, (680-45), 0.f);
	if(health > 100)
		shapeRenderer.rect(0f, 0f, 2*health, 20f);
	else
		shapeRenderer.rect(0f, 0f, 2*100, 20f);
	
	// shell 
	shapeRenderer.setColor(1.0f, .77f, .05f, 1.0f);
	shapeRenderer.identity();
	if(health > 100)
		shapeRenderer.translate(2*health+40+120, (680-40), 0.f);
	else
		shapeRenderer.translate(2*100+40+120, (680-40), 0.f);
	shapeRenderer.rect(0f, 0f, 100, 15f);
	
	// spit
	shapeRenderer.setColor(0.52f, 0.38f, .53f, 1.0f);
	shapeRenderer.identity();
	if(health > 100)
		shapeRenderer.translate(2*health+40+120, (680-40), 0.f);
	else
		shapeRenderer.translate(2*100+40+120, (680-40), 0.f);
	shapeRenderer.rect(0f, 0f, 100, 15f);
	
	// shock-wave
	shapeRenderer.setColor(0.18f, 0.46f, 1.0f, 1.0f);
	shapeRenderer.identity();
	if(health > 100)
		shapeRenderer.translate(2*health+40+230, (680-40), 0.f);
	else
		shapeRenderer.translate(2*100+40+230, (680-40), 0.f);
	shapeRenderer.rect(0f, 0f, 100, 15f);
	shapeRenderer.end();
	
	// Print TEXT
	healthText.begin();
	font.draw(healthText, "HEALTH" , 40.0f, 670);
	if(health > 100)
		font.draw(healthText, "SHELL" , 2*health+10+40, 670);
	else
		font.draw(healthText, "SHELL" , 2*100+10+40, 670);
	if(health > 100)
		font.draw(healthText, "SPIT" , 2*health+40+120, 670);
	else
		font.draw(healthText, "SPIT" , 2*100+40+120, 670);
	if(health > 100)
		font.draw(healthText, "SHOCKWAVE" , 2*health+40+230, 670);
	else
		font.draw(healthText, "SHOCKWAVE" , 2*100+40+230, 670);
	healthText.end();
	
	// draw Alabaster Icon
	icon.begin();
	icon.draw(iconTexture,4,680-45,32,32);
	icon.end();	
}

	
public void move(MyInputProcessor input) 
{	  				
	//System.out.println(count);
	foot.setTransform(entity.getPosition().x,entity.getPosition().y-.36f,0);
	foot.applyForceToCenter(0.0f,10f);
	tongueBody.setActive(false);
	//shoutBody.setActive(false);
	boolean moveLeft = false;
	boolean moveRight = false;	
	shell = false;
	sprite.setTexture(texture);
	sprite.setSize(64f, 51f);
	
	
	//shout = false;
	//spit = false;	
		//jumping

		if(!input.buttons[MyInputProcessor.JUMP] && input.oldButtons[MyInputProcessor.JUMP])
	    {

			
			EnemyContact.grounded=false;
			if(powerLegs)
			{
				
				if(count > 0&&doubleJump)
				{
			
					jumpSound.setLooping(1, false);
					jumpSound.play();
					entity.applyLinearImpulse(new Vector2(0.0f,4.8f),entity.getWorldCenter());
					if(count==1)doubleJump = false;
				    count--;
					
				}
			}
			else
			{	
              if(count==2)
              {
					jumpSound.setLooping(1, false);
					jumpSound.play();
					entity.applyLinearImpulse(new Vector2(0.0f,4.8f),entity.getWorldCenter());
				count--;
              }	
			}
		}


		if(EnemyContact.grounded)
		{
			count=2; 
			doubleJump=true;
		}
		if(count==0){		
			count=2;
			
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN))
			System.out.println("frog: " + entity.getPosition().x + ", " + entity.getPosition().y);
		
	    //move left
  		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) 
  		{
  			moveLeft = true;
  			sprite.setTexture(textureMove);
  			sprite.setSize(128,51);
  			moveRight=false;
  		}
  					
  	
  		//move right
  		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) 
  		{
  			moveRight = true;
  			sprite.setTexture(textureMove);
  			sprite.setSize(128,51);
  			moveLeft=false;
  		}
  		
  		if (Gdx.input.isKeyPressed(Input.Keys.S)) {

	  		if(shellCharge > 0) {
	  			EnemyContact.grounded= true;
				count=2;
				moveRight = false;
				moveLeft=false;
		// prevent tongue or shout usage
				tongueAct = false;
				tongueOut = false;
				tongueBody.setActive(false);
				shout = false;
				shoutBody.setActive(false);
				shell = true;
				entity.applyLinearImpulse(new Vector2(0, -3.4f), entity.getPosition());
				shellSound.setLooping(1,false);
				shellSound.play();
			}
			else {
				
			}
  		}
		
//		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//			if(shoutCharge > 0) {
//	// prevent tongue or shout usage
//			tongueAct = false;
//			tongueOut = false;
//			tongueBody.setActive(false);
//			shout = true;
//			shoutBody.setActive(true);
//			shoutSound.play();
//		}
//		else {
//			
//		}
//
//	}
  		
  		if (moveRight) 
  		{
  			if(entity.getLinearVelocity().x<2.5f)
  			entity.applyLinearImpulse(new Vector2(1.5f, 0.0f),entity.getWorldCenter());
  			else{}
  			
  			
  			if (facingRight == false)
  			{
  				sprite.flip(true, false);
  				tongueSprite.flip(true, false);
  				shoutSprite.flip(true,false);
  				//spitSprite.flip(true, false);
  			}
  		
  			facingRight = true;
  			
  		} 
  		else if (moveLeft)
  		{
  			if(entity.getLinearVelocity().x>-2.5f)
  			entity.applyLinearImpulse(new Vector2(-1.5f, 0.0f),entity.getWorldCenter());
  			else{}
  			
  			if (facingRight == true)
  			{
  				
  				sprite.flip(true, false);
  				tongueSprite.flip(true, false);
  				shoutSprite.flip(true,false);
  				//spitSprite.flip(true, false);
  			}
  			facingRight = false;
  			
  		}

  		/****************************************************************
  		 * SPIT INPUT  		 											* 
  		 ***************************************************************/
	    if(!input.buttons[MyInputProcessor.SPIT] && input.oldButtons[MyInputProcessor.SPIT] && !shell)
	    {
	    	//if(spitCharge > 0) 
	    	//{
	    		spitSound.play();
		    	spit = true;		    	
		    	spitSound.play();
		    	
		    	//spitBody.setActive(true);
		    	//System.out.print("spit");
		    	//spitBody.setLinearVelocity(new Vector2(2.0f, 0.0f));
		    	
				//Body b = world.createBody(spitBodyDef);
				//b.createFixture(spitFixtureDef);
					    	
				//handle left or right spit
//				if(facingRight)
//					b.setUserData("RIGHT_SPIT");
//				else
//					b.setUserData("LEFT_SPIT");
//				
//	
//				if(facingRight)
//				{
//					b.setTransform(entity.getPosition().x+bodyOffset,entity.getPosition().y,0);
//					b.setLinearVelocity(spitVel, 0.0f);
//				}
//				else
//				{
//					b.setTransform(entity.getPosition().x-bodyOffset,entity.getPosition().y,0);
//					b.setLinearVelocity(-spitVel, 0.0f);
//				}	
				
				spits.add(new Spit(world, spitBodyDef, spitFixtureDef, facingRight, entity.getPosition().x+bodyOffset, entity.getPosition().y));
	    	}
		//}	
	    /****************************************************************
  		 * TONGUE INPUT  		 										* 
  		 ***************************************************************/
	    //--hold out tongue .25 secs
	    if(!input.buttons[MyInputProcessor.TONGUE] && input.oldButtons[MyInputProcessor.TONGUE] && !tongueOut &&!shell)
	    {
	    	tongueOut = true;
	    	now = System.nanoTime();
//	    	System.out.println("tongue");
	    	tongueBody.setActive(true);
	    	if(facingRight)
  				tongueBody.setTransform(entity.getPosition().x+1f,entity.getPosition().y,0);	
  			else
  				tongueBody.setTransform(entity.getPosition().x-1f,entity.getPosition().y,0);    	
	    }
	    
	    /****************************************************************
  		 * SHOUT INPUT  hi		 										* 
  		 ***************************************************************/
	    if(input.buttons[MyInputProcessor.SHOUT] && !input.oldButtons[MyInputProcessor.SHOUT] && !shell)
	    {	    	
	    	if(shoutCharge > 0) 
	    	{
	    		shoutTime = System.nanoTime();
		    	shout = true;
		    	shoutBody.setActive(true);
		        shoutSound.play();
	    	
				if(facingRight)
					shoutBody.setTransform(entity.getPosition().x+1.1f,entity.getPosition().y,0);	
				else
					shoutBody.setTransform(entity.getPosition().x-1.1f,entity.getPosition().y,0);
	    	}
	     }    
	    
	    if(tongueOut)
	    {
	    	if((System.nanoTime() - now)/1000000 > 500)
    		{
	    		tongueOut = false;
	    		tongueBody.setActive(false);
    		}
	    }
	    
	    if(shout)
	    {
	    	last = System.nanoTime();
	    	long time = (last - shoutTime)/1000000;
	    	if((System.nanoTime() - shoutTime)/1000000 > 500)
    		{
	    		shout = false;
	    		shoutBody.setActive(false);
    		}
	    }
	    

	    /****************************************************************
  		 * UPDATE SPITS		 											* 
  		 ***************************************************************/
	    //destroy spit if too far or collided w/something
        for(Spit b: spits)
        {
        	
        	if(b == null) continue;        	
	        	//System.out.println(spits.size());
	        	if(b.body.getPosition().x < b.x/*entity.getPosition().x*/ - 2*100/PIXELS_PER_METER || b.body.getPosition().x > b.x/*entity.getPosition().x*/ + 2*100/PIXELS_PER_METER)

	        	{
	        		//System.out.println("removing");	        		
	        		int i = spits.indexOf(b);
	        		
	        		//spits.set(i,null);
	        		//world.destroyBody(b);
	        		
	        		spitsToDestroy.add(b);
	        		
	        		
	        		b.body.setActive(false);
	        		System.out.println("deactivate");
	        		
	        		spits.set(i,null);
	        		//spits.remove(b);
	        	}
	        	else
	        	{

	        	}
        }
        
        /*if(spitBody.getLinearVelocity().x < 2.0f)
        {
        	spitBody.setLinearVelocity(2.0f, 0.0f);
        }*/
        
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
		setHealth(0);
		world.destroyBody(shoutBody);
		world.destroyBody(tongueBody);
		spitSound.dispose();
		shoutSound.dispose();
		shellSound.dispose();
		jumpSound.dispose();
		System.out.println("youre dead! NEW LIFE");
	}

public void batchRender(TiledMapHelper tiledMapHelper) {	
	batch.setProjectionMatrix(tiledMapHelper.getCamera().combined);
	batch.begin();
	
		if(!shell){
				sprite.setPosition(
						PIXELS_PER_METER * entity.getPosition().x
								- sprite.getWidth() / 2,
						PIXELS_PER_METER * entity.getPosition().y
								- sprite.getHeight() / 2);
				sprite.draw(batch);
		}else{
			shellSprite.setPosition(
					PIXELS_PER_METER * entity.getPosition().x
							- shellSprite.getWidth() / 2,
					PIXELS_PER_METER * entity.getPosition().y
							- shellSprite.getHeight() / 2);
				shellSprite.draw(batch);
				
		}
		if(tongueOut){			
			if(facingRight){		
				tongueSprite.setPosition(
						PIXELS_PER_METER * entity.getPosition().x+60
								- sprite.getWidth() / 2,
						PIXELS_PER_METER * entity.getPosition().y-22
								- sprite.getHeight() / 2);			
				tongueSprite.draw(batch);
			}
			else{
				
		    	tongueSprite.setPosition(
						PIXELS_PER_METER * entity.getPosition().x-58
								- sprite.getWidth() /2,
						PIXELS_PER_METER * entity.getPosition().y-22
								- sprite.getHeight() / 2);
		    	 tongueSprite.draw(batch);
				}			   
		}
			if(shout){
				if(facingRight){
					shoutSprite.setPosition(
							PIXELS_PER_METER * entity.getPosition().x+64f
									- shoutSprite.getWidth() / 2,
							PIXELS_PER_METER * entity.getPosition().y+10f
									- shoutSprite.getHeight() / 2);				
					shoutSprite.draw(batch);
				}
				else{	  		
			    	shoutSprite.setPosition(
							PIXELS_PER_METER * entity.getPosition().x-64f
									- shoutSprite.getWidth() /2,
							PIXELS_PER_METER * entity.getPosition().y+10f
									- shoutSprite.getHeight() / 2);
			    	 shoutSprite.draw(batch);
					}			 
			}
			for(Spit b: spits)
			{
				if(b == null) continue;
				
					if(b.body.getUserData() == "RIGHT_SPIT")
					{
						spitSprite.setPosition(
								PIXELS_PER_METER * b.body.getPosition().x
										- spitSprite.getWidth() / 2,
								PIXELS_PER_METER * b.body.getPosition().y
										- spitSprite.getHeight() / 2);				
						spitSprite.draw(batch);
					}
					if(b.body.getUserData() == "LEFT_SPIT")
					{
						leftSpitSprite.setPosition(
								PIXELS_PER_METER * b.body.getPosition().x
										- leftSpitSprite.getWidth() / 2,
								PIXELS_PER_METER * b.body.getPosition().y
										- leftSpitSprite.getHeight() / 2);				
						leftSpitSprite.draw(batch);
					}					
			}
		batch.end();
}

	public void dispose()
	{
		for(int i = 0; i < spits.size(); i++)
		{
			world.destroyBody(spitsToDestroy.get(i).body);
		}
	}

	@Override
	public void reset(float x, float y) {
		entityDef.position.set(x, y);
		System.out.println(x+"+"+y);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}	
}
