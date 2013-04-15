package com.me.ampdom;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class AmpDom implements ApplicationListener {
	/**
	 * The time the last frame was rendered, used for throttling frame rate
	 */
	Sound damage;
	Sound flyjar;
	private long lastRender;
	
	private TiledMapHelper tiledMapHelper;
	private Alabaster frog;
   
	private World world;
	boolean hit = false;
	EnemyContact detect;
	private Box2DDebugRenderer debugRenderer;
    public static final float PIXELS_PER_METER = 60.0f;
    private int screenWidth; // width of game screen
	private int screenHeight; // width of game height
	private LevelMap level;
	
	//menu 
	private Menu m;
	private OrthographicCamera camera;
	public static int state = -5;
	Sprite shellSprite;
	Texture shellText;
	static SpriteBatch batch;
	
	// HUD
	float shellElapsedTime;
	float shoutElapsedTime;
	float spitElapsedTime;
	long shellBeginTime;
	long shoutBeginTime; 
	long spitBeginTime; 
	float hitElapsedTime;
	long hitBeginTime;
	long isHitBeginTime;
	float isHitElapsedTime;
	static boolean insideCheck;

	// detect keyboard
	MyInputProcessor input = new MyInputProcessor();
	float ElapsedTime;
	long BeginTime;
	
	//background tex
	Texture backgroundTex;

	public AmpDom() {
		super();

		// Defer until create() when Gdx is initialized.
		screenWidth = -1;
		screenHeight = -1;
	}

	public AmpDom(int width, int height) {
		super();

		screenWidth = width;
		screenHeight = height;
	}

	@Override
	public void create() {
		
		Gdx.input.setInputProcessor(input);
		
		/**
		 * If the viewport's size is not yet known, determine it here.
		 */
		if (screenWidth == -1) {
			screenWidth = Gdx.graphics.getWidth();
			screenHeight = Gdx.graphics.getHeight();
		}
		//batch
		batch = new SpriteBatch();
		//menu
		camera = new OrthographicCamera(1, 600);
		m = new Menu(camera);
	
		world = new World(new Vector2(0.0f, -10.0f), true);
		detect = new EnemyContact();
		world.setContactListener(detect);

		level = new LevelMap();
		int lev = 0;
		if(state > 0)
			lev = state;
		level.create(world,lev, screenWidth, screenHeight,detect);
		frog = new Alabaster(world, 1.5f, 6.0f);

        lastRender = System.nanoTime();
        debugRenderer = new Box2DDebugRenderer();		
		damage = Gdx.audio.newSound(Gdx.files.getFileHandle("data/sounds/hit.wav", FileType.Internal));
		flyjar = Gdx.audio.newSound(Gdx.files.getFileHandle("data/sounds/jar.wav", FileType.Internal));
		tiledMapHelper = level.getMap();				
		
		//load background
		backgroundTex = new Texture(Gdx.files.internal("data/background.png"));
		
		/*************************************************************/
		//--read in enemy types and locations
		/*MapBodyManager mapBodyManager = new MapBodyManager(world, PIXELS_PER_METER, "data/materials.xml", Logger.DEBUG);
		mapBodyManager.createPhysics(tiledMapHelper.getMap());*/
		
		/*************************************************************/        
	}

	@Override
	public void resume() {
	}

	// blink baster
	public void getHit() {
		
		hitBeginTime = System.nanoTime();
		hitElapsedTime = (System.nanoTime() - hitBeginTime)/1000000000.0f;

		//if(hitElapsedTime > 0 && hitElapsedTime < 3) {
			//if((int)hitElapsedTime%2 != 0 )
				frog.sprite.setColor(1f, 1f, 1f, 0.2f);
			//else
				//frog.sprite.setColor(1f, 1f, 1f, 1f);
			hitBeginTime = System.nanoTime();
		//}
		
	}
	
	@Override
	public void render() {		
		
		//clear buffer to run faster
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//	System.out.println(insideCheck);
		if(state >= 0 && state%2 == 0 && level.currentLevel%2 == 0){

			//if(level.test.isPlaying() == false)
			//level.test.play();
			
			//load background
			batch.begin();
			batch.draw(backgroundTex, 0, 0);
			batch.end();

			long now = System.nanoTime();			
			
			frog.move(input);
			input.tick();
			isHitBeginTime=System.nanoTime();


			//System.out.println(frog.entity.getPosition());

			world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);

			//System.out.println(frog.entity.getPosition());
			//camera follows player
			tiledMapHelper.getCamera().position.x = PIXELS_PER_METER
					* frog.entity.getPosition().x;
			tiledMapHelper.getCamera().position.y = PIXELS_PER_METER
					* frog.entity.getPosition().y;
//			if(frog.entity.getPosition().y > 680) {
//				tiledMapHelper.getCamera().position.y = PIXELS_PER_METER
//						* frog.entity.getPosition().y;
//			}
			
			if(EnemyContact.enemyDmg && EnemyContact.isHit){
				isHitBeginTime = System.nanoTime();
				if(frog.shell) {
					if(frog.shellCharge-1 >= 0) {
						frog.shellCharge -= 1;
						System.out.println("SHELL HEALTH: " + frog.shellCharge);
						
						// initiate timer
						if(frog.shellCharge == 0)
							shellBeginTime = System.nanoTime();
					}
				} else {
					if(!insideCheck)
					frog.takeDamage(20);
					EnemyContact.enemyDmg=false;
					EnemyContact.isHit=false;
//					System.out.println(" time" + isHitBeginTime/1000000000f%2)
			
					frog.icon.setColor(Color.RED);
					frog.sprite.setColor(1.0f, 1.0f, 1.0f, .2f);
//					detect.enemyDmg=false;
//					detect.isHit = false;
					damage.play();
				//	System.out.println(detect.insideEnemy);
					
				}
			
			}
			if(EnemyContact.insideEnemy){
               insideCheck=true;
//				System.out.println(isHitBeginTime/1000000000f);
			    isHitElapsedTime = (System.nanoTime() - isHitBeginTime)/1000000000.0f;
				System.out.println(isHitElapsedTime);
				if(isHitBeginTime/1000000000f%2==0.0)
				{
					System.out.println(" time" + isHitBeginTime/1000000000f%2);
					frog.takeDamage(20);

				}
			}
			if(EnemyContact.insideEnemy==false)insideCheck=false;
			if(detect.obstacleDmg && EnemyContact.isHit){
				frog.takeDamage(100);	
				detect.obstacleDmg=false;
				EnemyContact.isHit = false;
				damage.play();
				System.out.println("youve been hit by spikes!"+ frog.getHealth());
				}
		//	System.out.println(EnemyContact.grounded);
			if(detect.collectJar){
				 System.out.println("jar----"+LevelMap.jar.sprite.toString());
				frog.incHealth(20);
				flyjar.play();
				LevelMap.jar.entity.setActive(false);
				LevelMap.jar.sprite.setScale(0f,0f);
				detect.collectJar= false;
			}
			
			if(frog.getHealth()<=0){
				frog.die();
				world.destroyBody(frog.entity);
				frog = new Alabaster(world, 1.0f, 6.0f);
				frog.shell = false;
				frog.shout = false;
				EnemyContact.enemyDmg=false;
				EnemyContact.isHit = false;
				EnemyContact.insideEnemy=false;
			}
			if(detect.dropperHit){
				frog.takeDamage(20);
				detect.dropperHit=false;
				System.out.println("lol");
			}
			if(!frog.shell) {
				//if(frog.shellHealth == 0) {
					shellElapsedTime = (System.nanoTime() - shellBeginTime)/1000000000.0f;

					if(shellElapsedTime > 2 && shellElapsedTime < 5 && frog.shellCharge <100) {
						frog.shellCharge += 10;
						shellBeginTime = System.nanoTime();
					}
			}
			
			if(frog.shout) {
				
				if(frog.shoutCharge-20 >= 0) {
					frog.shoutCharge -= 20;
					System.out.println("SHOUT HEALTH: " + frog.shoutCharge);

					// initiate timer
					if(frog.shoutCharge == 0)
						shoutBeginTime = System.nanoTime();
				}
				
			}

			if(!frog.shout) {
					shoutElapsedTime = (System.nanoTime() - shoutBeginTime)/1000000000.0f;

					if(shoutElapsedTime > 5 && frog.shoutCharge <100) {
						frog.shoutCharge += 20;
						shoutBeginTime = System.nanoTime();
						//shoutElapsedTime = (System.nanoTime() - shoutBeginTime)/1000000000.0f;
					}
			}

			if(frog.spit) {
				
				if(frog.spitCharge-20 >= 0) {
					frog.spitCharge -= 20;
					frog.spit=false;
					System.out.println("SPIT HEALTH: " + frog.spitCharge);

					// initiate timer
					if(frog.spitCharge == 0)
						spitBeginTime = System.nanoTime();
				}
			}

			if(!frog.spit && frog.spitCharge == 0) {
					spitElapsedTime = (System.nanoTime() - spitBeginTime)/1000000000.0f;

					if(spitElapsedTime > 5 && frog.spitCharge <100) {
						frog.spitCharge += 20;
						spitBeginTime = System.nanoTime();
						//spitElapsedTime = (System.nanoTime() - spitBeginTime)/1000000000.0f;
					}
			}



			
			/**
			 * Ensure that the camera is only showing the map, nothing outside.
			 */
			if (tiledMapHelper.getCamera().position.x < Gdx.graphics.getWidth() / 2) {
				tiledMapHelper.getCamera().position.x = Gdx.graphics.getWidth() / 2;
			}
			if (tiledMapHelper.getCamera().position.x >= tiledMapHelper.getWidth()
					- Gdx.graphics.getWidth() / 2) {
				tiledMapHelper.getCamera().position.x = tiledMapHelper.getWidth()
						- Gdx.graphics.getWidth() / 2;
			}
			
			if (tiledMapHelper.getCamera().position.y < Gdx.graphics.getHeight() / 2) {
				tiledMapHelper.getCamera().position.y = Gdx.graphics.getHeight() / 2;
			}
			if (tiledMapHelper.getCamera().position.y >= tiledMapHelper.getHeight()
					- Gdx.graphics.getHeight() / 2) {
				tiledMapHelper.getCamera().position.y = tiledMapHelper.getHeight()
						- Gdx.graphics.getHeight() / 2;
			}

			// need some array to load all creatures or something wrd
			tiledMapHelper.getCamera().update();

			
			
			//load level bg
			    batch.begin();
				batch.setProjectionMatrix(tiledMapHelper.getCamera().combined);
				level.levelSprite.draw(batch);
				batch.end();
		
			tiledMapHelper.render();


			for(FlyingEnemy f : LevelMap.flyers){
				if(detect.enemPos == f.entity.getPosition() )
				{
					 if(detect.spitEnem)
					 {
				      	f.die();
					    detect.spitEnem = false;
	          	     }
					 
					 if(detect.attackEnem){
						 f.die();
						 detect.attackEnem=false;
					 }
				}
			

				f.directPos = frog.entity;
				if(Math.sqrt( Math.pow( frog.entity.getPosition().x - f.entity.getPosition().x , 2 )
						+ Math.pow(frog.entity.getPosition().y - f.entity.getPosition().y , 2 ) ) <= 4) {
					f.directMove = true;
					f.spawnX = frog.entity.getPosition().x;
					f.spawnY = frog.entity.getPosition().y;
					
				}
				else {
					f.directMove = false;
					f.spawnX = f.spawnX0;
					f.spawnY = f.spawnY0;
				}
				f.move();
				f.batchRender(tiledMapHelper);
			}
			
			for(Enemy e : LevelMap.enemies)
			{
				
				if(detect.enemPos == e.entity.getPosition())
				{
					
					if(detect.spitEnem)
					{
						detect.spitEnem = false;
						e.die();
					}
					if(detect.attackEnem){
						e.die();
						detect.attackEnem=false;
					}
					//alter enem vel
					if(detect.shoutEnem)
					{
						detect.shoutEnem = false;
						if(frog.facingRight)
						{
							e.entity.applyForce(new Vector2(5.0f, 0.0f), new Vector2(e.entity.getPosition().x, e.entity.getPosition().y));
						}
						else
						{
							e.entity.applyForce(new Vector2(-5.0f, 0.0f), new Vector2(e.entity.getPosition().x, e.entity.getPosition().y));
						}
					}
				}
				// ram movement
//				if(Math.sqrt( Math.pow( frog.entity.getPosition().x - e.entity.getPosition().x , 2 )
//						+ Math.pow(frog.entity.getPosition().y - e.entity.getPosition().y , 2 ) ) <= 4) {
//						e.ramMoveRight();
//				}
				e.move();
				e.batchRender(tiledMapHelper);
			}
			
			for(Obstacle s : LevelMap.spikes){
				s.move();
				s.batchRender(tiledMapHelper);
				s=null;
			}
			
			for(MovingPlat m : LevelMap.plats){
				m.move();
				m.batchRender(tiledMapHelper);
		
			}
			
			//falling elements
			for(Dropper d: LevelMap.droppers)
			{
				d.check(frog.entity.getPosition().x, frog.entity.getPosition().y);
				d.batchRender(tiledMapHelper);
			}
			
			//stationary platforms
			for(StationaryPlatform s: LevelMap.stationaryPlatforms)
			{
				s.batchRender(tiledMapHelper);
			}			
			
			//falling logs
			for(WaterLog w: LevelMap.fallingLogs)
			{
				w.fall();
				w.batchRender(tiledMapHelper);
			}
			
			//sandstorms
			for(Sandstorm s: LevelMap.sandstorms)
			{
				s.attack(frog.entity.getPosition().x);
				s.batchRender(tiledMapHelper);
			}
			
			//moving platforms
			for(MovingPlatform m: LevelMap.movingPlatforms)
			{
				m.move();
				m.batchRender(tiledMapHelper);
			}
			
			frog.batchRender(tiledMapHelper);


			LevelMap.endlevel.batchRender(tiledMapHelper);				
			LevelMap.jar.batchRender(tiledMapHelper);
			
			if(detect.endLevel){
				  
				  detect.endLevel = false;
				  try {
					  Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				state++;
				reset();
				 
			}
			
			now = System.nanoTime();
			if (now - lastRender < 30000000) { // 30 ms, ~33FPS
				try {
					Thread.sleep(30 - (now - lastRender) / 1000000);
				} catch (InterruptedException ie) {
				}
			}
			else {
				frog.sprite.setColor(1f, 1f, 1f, 1f);
				//frog.sprite.setColor(Color.WHITE);
				frog.icon.setColor(Color.WHITE);
			}

			//debugger
		     debugRenderer.render(world,tiledMapHelper.getCamera().combined.scale(
		    		 AmpDom.PIXELS_PER_METER,
		    		 AmpDom.PIXELS_PER_METER,
		    		 AmpDom.PIXELS_PER_METER));
			lastRender = now;
			
			frog.displayHUD();
			
		}
		

		 m.display();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void dispose() {
		m.dispose();
		frog.dispose();
	}
	
	public void reset(){		
		
		level.currentLevel = state;
		System.out.println("state: " + state);
		level.deleteLevel();
		
	    world.destroyBody(frog.entity);

	    world.destroyBody(LevelMap.jar.entity);
	    world.destroyBody(LevelMap.endlevel.entity);
	  // world.destroyBody(LevelMap.plat.entity);
	    if(state >= 0 && state%2 == 1 && level.currentLevel%2 == 1){    	
		    level = new LevelMap();
		    level.currentLevel = state + 1;
		    
		    System.out.println("state increment: " + level.currentLevel);
		    detect = new EnemyContact();
		    
		    world = new World(new Vector2(0.0f, -10.0f), true);
		    world.setContactListener(detect);
			level.create(world, level.currentLevel, screenWidth, screenHeight,detect);
	        tiledMapHelper = level.getMap();		
	        frog = new Alabaster(world, 1.0f, 6.0f);
	        lastRender = System.nanoTime();
	    }
	}
}