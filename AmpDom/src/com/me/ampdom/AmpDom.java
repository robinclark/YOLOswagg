package com.me.ampdom;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Logger;
import com.me.ampdom.CurrentLevel;

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
	public static int state = 0;
	Sprite shellSprite;
	Texture shellText;
	SpriteBatch batch;
	
	MyInputProcessor input = new MyInputProcessor();	

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
		//menu
		camera = new OrthographicCamera(1, 600);
		m = new Menu(camera);
	
		world = new World(new Vector2(0.0f, -10.0f), true);
		detect = new EnemyContact();
		world.setContactListener(detect);
		
		level = new LevelMap();		
		level.create(world, level.currentLevel, screenWidth, screenHeight,detect);
		
		frog = new Alabaster(world, 1.0f, 5.0f);
        lastRender = System.nanoTime();
        debugRenderer = new Box2DDebugRenderer();		
		
		damage = Gdx.audio.newSound(Gdx.files.getFileHandle("data/sounds/hit.wav", FileType.Internal));
		flyjar = Gdx.audio.newSound(Gdx.files.getFileHandle("data/sounds/jar.wav", FileType.Internal));
		
		tiledMapHelper = level.getMap();		
		
		
		
		/*************************************************************/
		//--read in enemy types and locations
		/*MapBodyManager mapBodyManager = new MapBodyManager(world, PIXELS_PER_METER, "data/materials.xml", Logger.DEBUG);
		mapBodyManager.createPhysics(tiledMapHelper.getMap());*/
		
		/*************************************************************/        
	}

	@Override
	public void resume() {
	}

	@Override
	public void render() {		
		level.currentLevel = 0;/*CurrentLevel.DINAMI_MOUNTAIN;*/
		m.display();

		
		//clear buffer to run faster
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	
		if(state >= 0 ){

			//if(level.test.isPlaying() == false)
			//level.test.play();

			long now = System.nanoTime();			
			
			frog.move(input);
			input.tick();
			


			//System.out.println(frog.entity.getPosition());

			world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);

			//System.out.println(frog.entity.getPosition());
			//camera follows player
			tiledMapHelper.getCamera().position.x = PIXELS_PER_METER
					* frog.entity.getPosition().x;
			
			if(frog.powerLegs)
			{
				//System.out.println("doublej: " + frog.doubleCount);
				if(detect.grounded && frog.doubleCount == 0)
				{
					frog.resetJumps();
				}
				if(detect.grounded && frog.doubleCount == 1)
				{
					
				}
			}
			else
			{
				if(detect.grounded && frog.singleCount == 0)
				{
					frog.resetJumps();
				}
			}
			
			if(detect.enemyDmg && detect.isHit)
			{
					if(frog.shell)
					{
						System.out.println("SHELLMODE ACTIVATE");						
					}else
					{
						//frog.takeDamage(10);	
						detect.isHit = false;
						damage.play();
						System.out.println("youve been hit by an enemy!"+ frog.getHealth());
					}
			}
			
			//System.out.println(frog.entity.getLinearVelocity().y);
			if(detect.obstacleDmg && detect.isHit){
				//frog.takeDamage(50);	
				detect.isHit = false;
				damage.play();
				System.out.println("youve been hit by spikes!"+ frog.getHealth());
				}
			
			if(detect.collectJar){
				 System.out.println("jar----"+LevelMap.jar.sprite.toString());
				frog.incHealth(20);
				flyjar.play();
				// world.destroyBody(LevelMap.jar.entity);
				LevelMap.jar.entity.setActive(false);
				LevelMap.jar.sprite.setScale(0f,0f);
				detect.collectJar= false;
			}

			
			if(frog.getHealth()<=0){
				frog.setHealth(0);
				System.out.println("youre dead! NEW LIFE");
			  world.destroyBody(frog.entity);
			  frog = new Alabaster(world, 1.0f, 2.0f);
				
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

			// need some array to load all creatures or something
			tiledMapHelper.getCamera().update();
			tiledMapHelper.render();


			for(FlyingEnemy f : LevelMap.flyers){

				if(detect.enemPos == f.entity.getPosition()){
					f.speak();
				
					
				  detect.enemPos = new Vector2(0,0);
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
				f = null;
			}
			
			for(Enemy e : LevelMap.enemies){
				if(detect.enemPos == e.entity.getPosition())
					e.speak();
				if(Math.sqrt( Math.pow( frog.entity.getPosition().x - e.entity.getPosition().x , 2 )
						+ Math.pow(frog.entity.getPosition().y - e.entity.getPosition().y , 2 ) ) <= 1.5) {
					e.spawnX = frog.entity.getPosition().x;
					e.spawnY = frog.entity.getPosition().y;
					
				}
				else {
					//f.directMove = false;
					//f.entity.setLinearVelocity(2.0f, 1.0f);
					e.spawnX = e.spawnX0;
					e.spawnY = e.spawnY0;
				}
				e.move();
				e.batchRender(tiledMapHelper);
				e=null;
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
			
			frog.batchRender(tiledMapHelper);

			if(level.currentLevel==0){//?
			LevelMap.endlevelpt1.batchRender(tiledMapHelper);
			
			LevelMap.jar.batchRender(tiledMapHelper);
			}
			if(detect.endLevel){
				  System.out.println("iio");
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
			//debugger
		     debugRenderer.render(world,tiledMapHelper.getCamera().combined.scale(
		    		 AmpDom.PIXELS_PER_METER,
		    		 AmpDom.PIXELS_PER_METER,
		    		 AmpDom.PIXELS_PER_METER));
			lastRender = now;
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
	}
	public void reset(){		
		level.currentLevel = state;/*CurrentLevel.DINAMI_MOUNTAIN;*/
		
		level.deleteLevel();
		
	    world.destroyBody(frog.entity);

	    world.destroyBody(LevelMap.jar.entity);
	    world.destroyBody(LevelMap.endlevelpt1.entity);
	  // world.destroyBody(LevelMap.plat.entity);
	    if(state >= 0 ){    	
		    level = new LevelMap();
		    //level.currentLevel = state + 1; //what is going on here
		    world = new World(new Vector2(0.0f, -10.0f), true);
			level.create(world, level.currentLevel, screenWidth, screenHeight,detect);
	        tiledMapHelper = level.getMap();		
	        frog = new Alabaster(world, 1.0f, 10.0f);
	        lastRender = System.nanoTime();
	    }
	}
}