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
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Alabaster extends Character {
	//position
	float x,y;
	
	//body offset
	final float bodyOffset = 0.5f;
	
	//double jump
	static int doubleCount=2;
	static int singleCount=1;
	
	//bools
	boolean upKey;
	boolean shell = false;
	boolean shout = false;
	boolean tongueAct=false;
	boolean powerLegs=true;
	boolean spit = false;
	boolean tongueOut = false;
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
	Sprite spitSprite;
	Texture spitTexture;
	FixtureDef spitFixtureDef;
	Body spitBody;
	BodyDef spitBodyDef;
	Sound spitSound;
	ArrayList<Body> spits = new ArrayList<Body>();
	
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
	protected Sprite icon;
	private SpriteBatch healthText;
	private BitmapFont font;
	private ShapeRenderer ShapeRenderer;
	private boolean shoutAvailable = true;
	//private long actionBeginTime;
	private float shoutElapsedTime;
	protected int shellCharge = 100;
	protected int spitCharge = 100;
	protected int shockwaveCharge = 100;

	
	final float spitVel = 10.0f;

	
	long now, last;
	
	public Alabaster(World world, float x, float y) {
		super(world, x, y);	
		/*setup physics n sounds*/
		//--alabaster
		texture = new Texture(Gdx.files.internal("data/Alabaster/alabasterS.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		sprite = new Sprite(texture, 0, 0, 64, 51);
		
		entityDef.position.set(x, y);
		entity = world.createBody(entityDef);
		
		PolygonShape shape = new PolygonShape();
		
		//some triangle verts
		Vector2 verts1[] = new Vector2[4];
		for(int i = 0; i <4; i++)
		{
			verts1[i] = new Vector2();
			//shape.getVertex(i, verts1[i]);
		}
		verts1[0] = new Vector2(x - sprite.getWidth()/(2*PIXELS_PER_METER)-1.0f - 0.1f, y - sprite.getHeight()/(2*PIXELS_PER_METER)-5.0f);
		verts1[1] = new Vector2(x + sprite.getWidth()/(2*PIXELS_PER_METER)-1.0f + 0.1f, y - sprite.getHeight()/(2*PIXELS_PER_METER)-5.0f);
		verts1[2] = new Vector2(x + sprite.getWidth()/(2*PIXELS_PER_METER)-1.0f + 0.1f, y + sprite.getHeight()/(2*PIXELS_PER_METER)-5.0f);		
		verts1[3] = new Vector2(x - sprite.getWidth()/(2*PIXELS_PER_METER)-1.0f - 0.1f, y + sprite.getHeight()/(2*PIXELS_PER_METER)-5.0f);
				
		
	    shape.setAsBox(sprite.getWidth() / (2 * PIXELS_PER_METER),
				sprite.getHeight() / (2 * PIXELS_PER_METER));
	    
		//shape.set(verts1);
	    
	    entity.setFixedRotation(true);		
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 1.0f;
		
		entity.createFixture(fixtureDef);		
		//shape.dispose();
		
		entity.setUserData("PLAYER");
		
			/******************************************************/
			//--double jump sensor			
			/*EdgeShape doubleJumpSensorShape = new EdgeShape();
			float xOffset = sprite.getWidth() / (2 * PIXELS_PER_METER);
			float yOffset = sprite.getHeight() / (2 * PIXELS_PER_METER);*/
			
			/*doubleJumpBodyDef = new BodyDef();
			doubleJumpBodyDef.position.set(x, y-sprite.getHeight()/(2 * PIXELS_PER_METER));
			doubleJumpBodyDef.type = BodyDef.BodyType.KinematicBody;
			
			doubleJumpBody = world.createBody(doubleJumpBodyDef);
			doubleJumpBody.setUserData("SENSOR");			*/
			
			//doubleJumpSensorShape.set((x-60)/PIXELS_PER_METER, (y-sprite.getHeight())/(PIXELS_PER_METER), (x+60)/PIXELS_PER_METER, (y-sprite.getHeight())/(PIXELS_PER_METER));
			
			/*float yVal = (y-sprite.getHeight())/PIXELS_PER_METER;			
			
		    doubleJumpFixtureDef = new FixtureDef();
		   // doubleJumpFixtureDef.isSensor = true;
		    doubleJumpFixtureDef.shape = doubleJumpSensorShape;
		    doubleJumpFixtureDef.density = 1.0f;
		    doubleJumpFixtureDef.friction = 1.0f;
			
		    doubleJumpSensorShape.set((x-60)/PIXELS_PER_METER, yVal, (x+60)/PIXELS_PER_METER, yVal);
		    
		    //doubleJumpSensorShape.set((x + sprite.getWidth()/32)/PIXELS_PER_METER, y/PIXELS_PER_METER, (x + sprite.getWidth()/32)/PIXELS_PER_METER, (y-sprite.getHeight())/PIXELS_PER_METER);
		    
		    //attach to frog body
			entity.createFixture(doubleJumpSensorShape, 0);		
			
			doubleJumpSensorShape.dispose();*/
			
			//--line seg
			//LineSegment lSeg;
			
			
			//--foot
			 footDef = new BodyDef();
			 footDef.type = BodyDef.BodyType.DynamicBody;
			 footDef.position.set(entity.getPosition().x,entity.getPosition().y);
		   	 foot = world.createBody(footDef);
		   	
		   	 PolygonShape footShape = new PolygonShape();
			/* footShape.setAsBox(sprite.getWidth() / (2 * PIXELS_PER_METER),
						sprite.getHeight() / (2* PIXELS_PER_METER));*/
			 foot.setFixedRotation(true);
			 
			footShape.set(verts1);
				 
			footFix = new FixtureDef();
		    footFix.shape= footShape;
		    //footFix.isSensor=true;
		    footFix.density = 1.0f;
			footFix.friction = 1.0f;
			
			foot.createFixture(footFix);
			footShape.dispose();
			//foot.setActive(false);
			
			foot.setUserData("FOOT");
			
			MassData md = entity.getMassData();
			
			foot.setMassData(md);
			
			//--detect if wall touching
			/*wallBodyDef= new BodyDef();
			wallBodyDef.type = BodyDef.BodyType.DynamicBody;
			wallBodyDef.position.set(entity.getPosition().x,entity.getPosition().y);
		   	wallBody = world.createBody(wallBodyDef);
		   	
		   	 PolygonShape wallShape = new PolygonShape();
			/* footShape.setAsBox(sprite.getWidth() / (2 * PIXELS_PER_METER),
						sprite.getHeight() / (2* PIXELS_PER_METER));*/
			/* wallBody.setFixedRotation(true);
			 
			 wallShape.set(verts1);
				 
			wallFixDef = new FixtureDef();
			wallFixDef.shape= footShape;
		    //footFix.isSensor=true;
			wallFixDef.density = 1.0f;
			wallFixDef.friction = 1.0f;
			
			wallBody.createFixture(wallFixDef);
			wallShape.dispose();
			//foot.setActive(false);
			
			wallBody.setUserData("WALL");
			
			MassData md1 = entity.getMassData();
			
			wallBody.setMassData(md1);*/
			
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
		
		tongueBody.setUserData("TONGUE");
		
		
		    
		//--spit			
		spitTexture = new Texture(Gdx.files.internal("data/spit.png"));
	    spitSprite = new Sprite(spitTexture, 0, 0, 64, 64);
	   	 
	   	spitBodyDef = new BodyDef();
	   	spitBodyDef.type = BodyDef.BodyType.KinematicBody;
	   	//spitBodyDef.position.set(entity.getPosition().x+bodyOffset,entity.getPosition().y + bodyOffset);
	   	
		spitBody = world.createBody(spitBodyDef);
		
		spitSound = Gdx.audio.newSound(Gdx.files.getFileHandle("data/sounds/spitSound.wav", FileType.Internal));
		
		PolygonShape spitShape = new PolygonShape();
		spitShape.setAsBox(spitSprite.getWidth() / (5 * PIXELS_PER_METER),
					spitSprite.getHeight() / (2 * PIXELS_PER_METER));
		
		spitBody.setFixedRotation(true);
		    
		spitFixtureDef = new FixtureDef();
		
		/*set bullet as sensor*/
		spitFixtureDef.isSensor = true;		
		spitFixtureDef.shape = tongueShape;
		spitFixtureDef.density = 1.0f;
		spitFixtureDef.friction = 1.0f;
		
		spitBody.createFixture(spitFixtureDef);
		
		spitShape.dispose();
		
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
		ShapeRenderer = new ShapeRenderer();
		
		// alabaster head
		icon = new Sprite(texture, 0, 0, 32, 32);
		icon.flip(true, false);


	}

public void displayHUD() {   
	// display health
	k = (100f - health) / 100f;
	
	ShapeRenderer.begin(ShapeType.FilledRectangle);
	
	// health
	ShapeRenderer.setColor(0f, 0f, 0f, 1.0f);
	ShapeRenderer.identity();
	ShapeRenderer.translate(40.0f, (680-35), 0.f);
	ShapeRenderer.filledRect(0f, 0f, 200, 20f);
	ShapeRenderer.setColor((255*k)/100, 255*(1.0f - k)/100, 0f, 1.0f);
	ShapeRenderer.identity();
	ShapeRenderer.translate(40.0f, (680-35), 0.f);
	ShapeRenderer.filledRect(0f, 0f, 2*health, 20f);
	
	// shell
	ShapeRenderer.setColor(0f, 0f, 0f, 1.0f);
	ShapeRenderer.identity();
	if(health > 100)
		ShapeRenderer.translate(2*health+10+40, (680-30), 0.f);
	else
		ShapeRenderer.translate(2*100+10+40, (680-30), 0.f);
	ShapeRenderer.filledRect(0f, 0f, 100, 15f);
	ShapeRenderer.setColor(1.0f, .77f, .05f, 1.0f);
	ShapeRenderer.identity();
	if(health > 100)
		ShapeRenderer.translate(2*health+10+40, (680-30), 0.f);
	else
		ShapeRenderer.translate(2*100+10+40, (680-30), 0.f);
	ShapeRenderer.filledRect(0f, 0f, shellCharge, 15f);
	
	// spit
	ShapeRenderer.setColor(0f, 0f, 0f, 1.0f);
	ShapeRenderer.identity();
	if(health > 100)
		ShapeRenderer.translate(2*health+40+120, (680-30), 0.f);
	else
		ShapeRenderer.translate(2*100+40+120, (680-30), 0.f);
	ShapeRenderer.filledRect(0f, 0f, 100, 15f);
	ShapeRenderer.setColor(0.52f, 0.38f, .53f, 1.0f);
	ShapeRenderer.identity();
	if(health > 100)
		ShapeRenderer.translate(2*health+40+120, (680-30), 0.f);
	else
		ShapeRenderer.translate(2*100+40+120, (680-30), 0.f);
	ShapeRenderer.filledRect(0f, 0f, spitCharge, 15f);
	
	// shock-wave
	ShapeRenderer.setColor(0f, 0f, 0f, 1.0f);
	ShapeRenderer.identity();
	if(health > 100)
		ShapeRenderer.translate(2*health+40+230, (680-30), 0.f);
	else
		ShapeRenderer.translate(2*100+40+230, (680-30), 0.f);
	ShapeRenderer.filledRect(0f, 0f, 100, 15f);
	ShapeRenderer.setColor(0.18f, 0.46f, 1.0f, 1.0f);
	ShapeRenderer.identity();
	if(health > 100)
		ShapeRenderer.translate(2*health+40+230, (680-30), 0.f);
	else
		ShapeRenderer.translate(2*100+40+230, (680-30), 0.f);
	ShapeRenderer.filledRect(0f, 0f, shockwaveCharge, 15f);
	
	// Icon Background
	ShapeRenderer.setColor(0f, 0f, 0f, 1.0f);
	ShapeRenderer.identity();
	ShapeRenderer.translate(0f, (680-40), 0.f);
	ShapeRenderer.filledRect(0f, 0f, 40f, 40f);
	ShapeRenderer.end();
	
	// BORDER
	ShapeRenderer.begin(ShapeType.Rectangle);
	
	// health
	ShapeRenderer.setColor((255*k)/100, 255*(1.0f - k)/100, 0f, 1.0f);
	ShapeRenderer.identity();
	ShapeRenderer.translate(40.0f, (680-35), 0.f);
	if(health > 100)
		ShapeRenderer.rect(0f, 0f, 2*health, 20f);
	else
		ShapeRenderer.rect(0f, 0f, 2*100, 20f);
	
	// shell 
	ShapeRenderer.setColor(1.0f, .77f, .05f, 1.0f);
	ShapeRenderer.identity();
	if(health > 100)
		ShapeRenderer.translate(2*health+40+120, (680-30), 0.f);
	else
		ShapeRenderer.translate(2*100+40+120, (680-30), 0.f);
	ShapeRenderer.rect(0f, 0f, 100, 15f);
	
	// spit
	ShapeRenderer.setColor(0.52f, 0.38f, .53f, 1.0f);
	ShapeRenderer.identity();
	if(health > 100)
		ShapeRenderer.translate(2*health+40+120, (680-30), 0.f);
	else
		ShapeRenderer.translate(2*100+40+120, (680-30), 0.f);
	ShapeRenderer.rect(0f, 0f, 100, 15f);
	
	// shock-wave
	ShapeRenderer.setColor(0.18f, 0.46f, 1.0f, 1.0f);
	ShapeRenderer.identity();
	if(health > 100)
		ShapeRenderer.translate(2*health+40+230, (680-30), 0.f);
	else
		ShapeRenderer.translate(2*100+40+230, (680-30), 0.f);
	ShapeRenderer.rect(0f, 0f, 100, 15f);
	ShapeRenderer.end();
	
	// Print TEXT
	healthText.begin();
	font.draw(healthText, "HEALTH" , 40.0f, 680);
	if(health > 100)
		font.draw(healthText, "SHELL" , 2*health+10+40, 680);
	else
		font.draw(healthText, "SHELL" , 2*100+10+40, 680);
	if(health > 100)
		font.draw(healthText, "SPIT" , 2*health+40+120, 680);
	else
		font.draw(healthText, "SPIT" , 2*100+40+120, 680);
	if(health > 100)
		font.draw(healthText, "SHOCKWAVE" , 2*health+40+230, 680);
	else
		font.draw(healthText, "SHOCKWAVE" , 2*100+40+230, 680);
	healthText.end();
	
	// draw Alabaster Icon
	batch.begin();
	icon.setPosition(0,650f);
	icon.draw(batch);
	batch.end();
}

	
public void move(MyInputProcessor input) 
{	  				
		tongueBody.setActive(false);
		boolean doJump = false;
		boolean moveLeft = false;
		boolean moveRight = false;
		shout = false;
		shoutBody.setActive(false);
		
		 /*if(EnemyContact.grounded)
		 {
			  	upKey = false;
		    	doubleJump = true;
		 }*/
		 
		 shell= false;
		
		//jumping

		if(!input.buttons[input.JUMP] && input.oldButtons[input.JUMP] && !shell)
	    {

			//entity.applyLinearImpulse(new Vector2(0.0f,6.0f),entity.getWorldCenter());
			//doubleJump
			if(powerLegs)
			{
				if(doubleCount > 0)
				{
					entity.applyLinearImpulse(new Vector2(0.0f,4.5f),entity.getWorldCenter());
					foot.applyLinearImpulse(new Vector2(0.0f,4.5f),entity.getWorldCenter());
					
					doubleCount--;
					System.out.println("doublecount: " + doubleCount);
				}				

				jumpSound.setLooping(1, false);
				jumpSound.play();
			}
			//single jump
			else 
			{
				if(singleCount > 0)
				{
					//if on ground apply linear impulse
					entity.applyLinearImpulse(new Vector2(0.0f,4.5f),entity.getWorldCenter());
					singleCount--;
					System.out.println("doublecount: " + doubleCount);
				}
			}

	    }
		
	    //move left
  		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) 
  			moveLeft = true;		
  	
  		//move right
  		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) 
  			moveRight = true;	
  		
  		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
  			if(shellCharge > 0) {
				moveRight = false;
				moveLeft=false;
				doJump=false;
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
  		
  		if (moveRight) 
  		{
  			entity.applyLinearImpulse(new Vector2(.15f, 0.0f),
  			entity.getWorldCenter());
  			
			foot.applyLinearImpulse(new Vector2(.15f, 0.0f),
	  			foot.getWorldCenter());
  			
  			if (facingRight == false)
  			{
  				sprite.flip(true, false);
  				tongueSprite.flip(true, false);
  				shoutSprite.flip(true,false);
  				spitSprite.flip(true, false);
  			}
  		
  			facingRight = true;
  		} 
  		else if (moveLeft)
  		{
  			entity.applyLinearImpulse(new Vector2(-.15f, 0.0f),
  			entity.getWorldCenter());
  			
  			foot.applyLinearImpulse(new Vector2(-.15f, 0.0f),
  		  			foot.getWorldCenter());
  			
  			if (facingRight == true)
  			{
  				sprite.flip(true, false);
  				tongueSprite.flip(true, false);
  				shoutSprite.flip(true,false);
  				spitSprite.flip(true, false);
  			}
  			facingRight = false;
  		}

  		/****************************************************************
  		 * SPIT INPUT  		 											* 
  		 ***************************************************************/
	    if(!input.buttons[input.SPIT] && input.oldButtons[input.SPIT] && !shell)
	    {
	    	spitSound.play();
	    	//System.out.print("spit");
	    	//spitBody.setLinearVelocity(new Vector2(2.0f, 0.0f));
			Body b = world.createBody(spitBodyDef);
			b.createFixture(spitFixtureDef);
			
			b.setUserData("SPIT");
			
			if(facingRight)
			{
				b.setTransform(entity.getPosition().x+bodyOffset,entity.getPosition().y,0);
				b.setLinearVelocity(spitVel, 0.0f);
				
			}
			else
			{
				b.setTransform(entity.getPosition().x-bodyOffset,entity.getPosition().y,0);
				b.setLinearVelocity(-spitVel, 0.0f);
			}			
			spits.add(b);	    	
		}	
	    /****************************************************************
  		 * TONGUE INPUT  		 											* 
  		 ***************************************************************/
	    //--hold out tongue .25 secs
	    if(!input.buttons[input.TONGUE] && input.oldButtons[input.TONGUE] && !tongueOut &&!shell)
	    {
	    	tongueOut = true;
	    	now = System.nanoTime();
	    	System.out.println("tongue");
	    	tongueBody.setActive(true);
	    	if(facingRight)
  				tongueBody.setTransform(entity.getPosition().x+1f,entity.getPosition().y,0);	
  			else
  				tongueBody.setTransform(entity.getPosition().x-1f,entity.getPosition().y,0);    	
	    }
	    
	    /****************************************************************
  		 * SHOUT INPUT  		 											* 
  		 ***************************************************************/
	    if(input.buttons[input.SHOUT] && !input.oldButtons[input.SHOUT] && !shell)
	    	
	    { 
	    	shout = true;
	    	shoutBody.setActive(true);
	    	//shellSound.setLooping(3,false);
	        shoutSound.play();
			if(facingRight)
				shoutBody.setTransform(entity.getPosition().x+1.1f,entity.getPosition().y,0);	
			else
				shoutBody.setTransform(entity.getPosition().x-1.1f,entity.getPosition().y,0);
		
	     }
	    		    	
	    //foot.setTransform(entity.getPosition().x, entity.getPosition().y,0);
	    //foot.setLinearVelocity(new Vector2(2.0f, 0.0f));
	    //foot.applyForce(new Vector2(0.0f, 9.0f), new Vector2(foot.getPosition().x, foot.getPosition().y));
	    
	    if(tongueOut)
	    {
	    	if((System.nanoTime() - now)/1000000 > 250)
    		{
	    		tongueOut = false;
	    		tongueBody.setActive(false);
    		}
	    }

	    /****************************************************************
  		 * UPDATE SPITS		 											* 
  		 ***************************************************************/
	    //destroy spit if too far or collided w/something
        for(Body b: spits)
        {
        	if(b == null) break;        	
	        	//System.out.println(spits.size());
	        	if(b.getPosition().x < entity.getPosition().x - 2*800/PIXELS_PER_METER || b.getPosition().x > entity.getPosition().x + 2*800/PIXELS_PER_METER)
	        	{
	        		//System.out.println("removing");
	        		
	        		//world.destroyBody(b);
	        		//System.out.println(spits.remove(b));
	        		//spits.clear();
	        		
	        		int i = spits.indexOf(b);
	        		//spits.set(i, null);
	        		
	        		//world.destroyBody(b);
	        		spits.set(i,null);
	        		world.destroyBody(b);
	        		//b.setActive(false);
	        	}
	        	else
	        	{
	        		/*if(b.getLinearVelocity().x > 0)
	        			b.setLinearVelocity(spitVel, 0.0f);
	        		else if (b.getLinearVelocity().x < 0)
	        			b.setLinearVelocity(-spitVel, 0.0f);
	        		
	        		if(b.getLinearVelocity().x ==0)
	        		{
	        			b.setLinearVelocity(spitVel, 0.0f);
	        			//b.applyLinearImpulse(new Vector2(1.0f,0.0f),entity.getWorldCenter());
	        		}*/
	        	}
        }
        
        if(spitBody.getLinearVelocity().x < 2.0f)
        {
        	spitBody.setLinearVelocity(2.0f, 0.0f);
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
		setHealth(0);
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
			if(spit)
			{
				if(facingRight){
					spitSprite.setPosition(
							PIXELS_PER_METER * spitBody.getPosition().x+64f
									- spitSprite.getWidth() / 2,
							PIXELS_PER_METER * spitBody.getPosition().y+10f
									- spitSprite.getHeight() / 2);				
					spitSprite.draw(batch);
				}
				else{	  		
					spitSprite.setPosition(
							PIXELS_PER_METER * spitBody.getPosition().x-64f
									- spitSprite.getWidth() /2,
							PIXELS_PER_METER * spitBody.getPosition().y+10f
									- spitSprite.getHeight() / 2);
					spitSprite.draw(batch);
					}			 
			}
			for(Body b: spits)
			{
				if(b == null) break;
				
					spitSprite.setPosition(
							PIXELS_PER_METER * b.getPosition().x
									- spitSprite.getWidth() / 2,
							PIXELS_PER_METER * b.getPosition().y
									- spitSprite.getHeight() / 2);				
					spitSprite.draw(batch);
			}
		batch.end();
}

	//reset jumps
	public void resetJumps()
	{
		System.out.println("resetting");
		doubleCount = 2;
		singleCount = 1;
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
