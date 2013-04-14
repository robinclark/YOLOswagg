package com.me.ampdom;

import java.util.ArrayList;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObjectGroup;
import com.badlogic.gdx.physics.box2d.World;

public class LevelMap {
	
	protected TiledMapHelper tiledMapHelper;
	int screenWidth;
	int screenHeight;
	//CurrentLevel currentLevel;//RC added
	int currentLevel; //RC removed. using enums easier 
	EnemyContact detect;
	Music bgMusic;
	World world;
    public static final float PIXELS_PER_METER = 60.0f;	
    static EndLevelTrigger endlevelpt1;
	static ArrayList<FlyingEnemy> flyers;
	static ArrayList<Enemy> enemies;
	static ArrayList<Obstacle> spikes;
	static ArrayList<MovingPlat> plats;

	static ArrayList<WaterLog> fallingLogs;
	static ArrayList<Sandstorm> sandstorms;
	static ArrayList<MovingPlatform> movingPlatforms;
	//droppers 
	static ArrayList<Dropper> droppers;
	//stationary platfms
	static ArrayList<StationaryPlatform> stationaryPlatforms;
	
	//static ArrayList<Enemy> enemies;
    static FlyJar jar;
    static  Texture forestbg;
    static Sprite forestbgSprite;
    protected  Texture levelbg;
    protected Sprite levelSprite;

	
	public LevelMap(){
		this.currentLevel=0;//CurrentLevel.ESCAPE_FROM_THE_CASTLE;
		this.screenHeight = -1;
		this.screenWidth = -1;
		this.tiledMapHelper = new TiledMapHelper();
		
		fallingLogs = new ArrayList<WaterLog>();
		droppers = new ArrayList<Dropper>();
		stationaryPlatforms = new ArrayList<StationaryPlatform>();
		flyers = new ArrayList<FlyingEnemy>();
		enemies = new ArrayList<Enemy>();
		spikes = new ArrayList<Obstacle>();
		plats = new ArrayList<MovingPlat>();
		sandstorms = new ArrayList<Sandstorm>();
		movingPlatforms = new ArrayList<MovingPlatform>();
	}
	
	public void create(World world, /*CurrentLevel*/int currentLevel, int screenWidth, int screenHeight,EnemyContact detect) {
		this.world = world;
		this.currentLevel = currentLevel;
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.detect = detect;
		this.tiledMapHelper = new TiledMapHelper();		
		
		switch(currentLevel)
		{
		case 0:

			
			//test = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/sounds/beat.mp3", FileType.Internal));
			fallingLogs = new ArrayList<WaterLog>();
			droppers = new ArrayList<Dropper>();
			stationaryPlatforms = new ArrayList<StationaryPlatform>();

			bgMusic = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/sounds/dungeon.mp3", FileType.Internal));
			bgMusic.setLooping(true);
			bgMusic.play();
			levelbg = new Texture(Gdx.files.internal("data/world/level1/dungeonbg.png"));
			levelSprite = new Sprite(levelbg,0,0,8192,1024);
			levelSprite.setScale(1.1f,1.0f);
			
			//bgMusic = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/sounds/beat.mp3", FileType.Internal));

			flyers = new ArrayList<FlyingEnemy>();
			enemies = new ArrayList<Enemy>();
			spikes = new ArrayList<Obstacle>();
			plats = new ArrayList<MovingPlat>();
			sandstorms = new ArrayList<Sandstorm>();
			movingPlatforms = new ArrayList<MovingPlatform>();
			
			tiledMapHelper.prepareCamera(screenWidth, screenHeight);

			tiledMapHelper.setPackerDirectory("data/packer/level");
			tiledMapHelper.loadMap("data/world/level1/level.tmx");
			
			tiledMapHelper.loadCollisions("data/packer/level/collisions.txt", world,
					PIXELS_PER_METER);
			
			ArrayList<TiledObjectGroup> objectGroup = tiledMapHelper.getMap().objectGroups;
			for(TiledObjectGroup tG: objectGroup)
			{
				for(TiledObject tO: tG.objects)
				{
					if(tO.name.equals("movingPlatform"))
					{
						float range = 0.0f;
						boolean vert = false;
						
						if(tO.width > tO.height)
						{
							range = tO.width/AmpDom.PIXELS_PER_METER;
							vert = false;
						}
						else
						{
							range = tO.height/AmpDom.PIXELS_PER_METER;
							vert = true;
						}
						System.out.println("x: " + tO.x/AmpDom.PIXELS_PER_METER);
						System.out.println("y: " + (320 - tO.y)/AmpDom.PIXELS_PER_METER);
						
						System.out.println("range: " + range);
						movingPlatforms.add(new MovingPlatform(world, "data/Objects/dungeonPlatform.png", tO.x/AmpDom.PIXELS_PER_METER, (320 - tO.y)/AmpDom.PIXELS_PER_METER, range/4.0f, vert, 128, 32));
					}
					if(tO.name.equals("wolf"))
					{
						enemies.add(new Enemy(world, "data/Enemies/wolf.png", tO.x/AmpDom.PIXELS_PER_METER, tO.y/AmpDom.PIXELS_PER_METER, .7f, .7f,128,64));
					}
					if(tO.name.equals("snake"))
					{
						enemies.add(new Enemy(world, "data/Enemies/snake.png", tO.x/AmpDom.PIXELS_PER_METER, tO.y/AmpDom.PIXELS_PER_METER, .7f, .7f,128,64));
					}
					if(tO.name.equals("sandstorm"))
					{
						sandstorms.add(new Sandstorm(world, "data/Objects/sandstorm.png", tO.x/AmpDom.PIXELS_PER_METER, (320 - tO.y)/AmpDom.PIXELS_PER_METER, tO.width/AmpDom.PIXELS_PER_METER, 128, 64));
					}
					if(tO.name.equals("log"))//stationary platform
					{
						fallingLogs.add(new WaterLog(world, "data/Enemies/wolf.png", tO.x/AmpDom.PIXELS_PER_METER, (320 - tO.y)/AmpDom.PIXELS_PER_METER, tO.height/AmpDom.PIXELS_PER_METER/2, 128, 64));
					}
					if(tO.name.equals("leaf"))//stationary platform
					{						
						stationaryPlatforms.add(new StationaryPlatform(world, "data/Enemies/wolf.png", tO.x/AmpDom.PIXELS_PER_METER, (320 - tO.y)/AmpDom.PIXELS_PER_METER, tO.width/AmpDom.PIXELS_PER_METER, 128, 64));
					}
					if(tO.name.equals("bees"))//beehive
					{
						droppers.add(new Dropper(world, "data/Objects/beehive.png", tO.x/AmpDom.PIXELS_PER_METER, ((320 - tO.y)/AmpDom.PIXELS_PER_METER), tO.width/AmpDom.PIXELS_PER_METER, 64, 64));
					}
					if(tO.name.equals("owl"))
					{
						flyers.add(new FlyingEnemy(world, "data/Enemies/bat.png", tO.x/AmpDom.PIXELS_PER_METER, tO.y/AmpDom.PIXELS_PER_METER,64,64));
					}
					if(tO.name.equals("flyjar"))
					{
						jar = new FlyJar(world, "data/Objects/flyjar.png", tO.x/AmpDom.PIXELS_PER_METER, tO.y/AmpDom.PIXELS_PER_METER);
					}
				}
			}
			
			/*plats.add(new MovingPlat(world,"data/Objects/dungeonPlatform.png", 71.29f, 5.2f,0,1));
			plats.add(new MovingPlat(world,"data/Objects/dungeonPlatform.png", 73.5f, 5.2f,0,-1));
			plats.add(new MovingPlat(world,"data/Objects/dungeonPlatform.png", 82.64f, 5.2f,0,-1));
			plats.add(new MovingPlat(world,"data/Objects/dungeonPlatform.png", 85.07f, 5.2f,0,1));
			plats.add(new MovingPlat(world,"data/Objects/dungeonPlatform.png", 93.95f, 5.2f,0,1));
			plats.add(new MovingPlat(world,"data/Objects/dungeonPlatform.png", 102.53f, 5.2f,0,1));
			plats.add(new MovingPlat(world,"data/Objects/dungeonPlatform.png", 111.33f, 5.2f,0,1));
			
			
			
			
			jar = new FlyJar(world, "data/Objects/flyjar.png", 78.96f, 7.65f);
			
	     	flyers.add(new FlyingEnemy(world, "data/Enemies/bat.png", 36.0f, 6.5f,64,64));
	    	flyers.add(new FlyingEnemy(world, "data/Enemies/bat.png", 51.3f, 6.5f,64,64));
	    	flyers.add(new FlyingEnemy(world, "data/Enemies/bat.png", 67.17f, 9.0f,64,64));
	    	flyers.add(new FlyingEnemy(world, "data/Enemies/bat.png", 98.25f, 7.0f,64,64));
	    	flyers.add(new FlyingEnemy(world, "data/Enemies/bat.png", 111.0f, 8.5f,64,64));
	    	flyers.add(new FlyingEnemy(world, "data/Enemies/bat.png", 125.3f, 6.0f,64,64));
			*/
			/*****************************************************************************************/
			//--!!!add flyers from object layer!!!
			/*ArrayList<TiledObjectGroup> objectGroups = tiledMapHelper.getMap().objectGroups;
			for(TiledObjectGroup tG: objectGroups)
			{
				for(TiledObject tO: tG.objects)
				{
					if(tO.name.equals("flyer"))
					{
						flyers.add(new FlyingEnemy(world, "data/Enemies/bat.png", tO.x/AmpDom.PIXELS_PER_METER, tO.y/AmpDom.PIXELS_PER_METER,64,64));
					}
				}
			}*/

			/*****************************************************************************************/
	    	
		
			/*enemies.add(new Enemy(world, "data/Enemies/rat2.png", 11.0f, 3.43f, .7f, .7f,128,64));
			enemies.add(new Enemy(world, "data/Enemies/rat2.png", 16.59f, 4.3f, .7f, .7f,128,64));
			enemies.add(new Enemy(world, "data/Enemies/rat2.png", 21.82f, 3.43f,1.42f,1.42f,128,64));
			enemies.add(new Enemy(world, "data/Enemies/rat2.png", 28.69f, 1.29f,1.62f,1.62f,128,64));
			enemies.add(new Enemy(world, "data/Enemies/rat2.png", 36.2f, 3.44f,1.42f,1.42f,128,64));
			enemies.add(new Enemy(world, "data/Enemies/rat2.png", 44.62f, 1.29f,1.6f,5.1f,128,64));
			enemies.add(new Enemy(world, "data/Enemies/rat2.png", 56.2f, 4.49f,1.30f,1.30f,128,64));
			enemies.add(new Enemy(world, "data/Enemies/rat2.png", 61.59f, 5.56f,1.30f,1.30f,128,64));
			enemies.add(new Enemy(world, "data/Enemies/rat2.png", 67.21f, 6.63f,1.42f,1.42f,128,64));
			enemies.add(new Enemy(world, "data/Enemies/rat2.png", 89.57f, 6.63f,1.42f,1.42f,128,64));
			enemies.add(new Enemy(world, "data/Enemies/rat2.png", 98.08f, 4.49f,1.42f,1.42f,128,64));
			enemies.add(new Enemy(world, "data/Enemies/rat2.png", 106.74f, 6.63f,1.42f,1.42f,128,64));
			enemies.add(new Enemy(world, "data/Enemies/rat2.png", 124.11f, 1.5f,1.42f,1.42f,128,64));
			
			for(float i=0;i<6;i++)
		    spikes.add(new Obstacle(world, "data/Objects/spikes.png", 14f+i, 2.0f));
			
			for(float i=0;i<22;i++)
			spikes.add(new Obstacle(world, "data/Objects/spikes.png", 54f+i, 2.0f));
			
			for(float i=0;i<30;i++)
		    spikes.add(new Obstacle(world, "data/Objects/spikes.png", 83f+i, 2.0f));
			
			endlevelpt1 = new EndLevelTrigger(world,"data/Objects/endGoalPost.png", 134.9f,6.62f);
		    //test.play();
			*/
			break;
		case 2:
			tiledMapHelper.prepareCamera(screenWidth, screenHeight);
		    tiledMapHelper.setPackerDirectory("data/packer/level2");
			tiledMapHelper.loadMap("data/world/level2/level2.tmx");
	        tiledMapHelper.loadCollisions("data/packer/level2/collision2", world,
					PIXELS_PER_METER);
			levelbg = new Texture(Gdx.files.internal("data/world/level2/forestbg.png"));
			levelSprite = new Sprite(levelbg,0,0,8192,1024);
			bgMusic = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/sounds/forest.mp3", FileType.Internal));
			bgMusic.setLooping(true);
			bgMusic.play();
			
			//			ArrayList<TiledObjectGroup> objectGroups = tiledMapHelper.getMap().objectGroups;
//			for(TiledObjectGroup tG: objectGroups)
//			{
//				for(TiledObject tO: tG.objects)
//				{
//					if(tO.name.equals("flyer"))
//					{
//						flyers.add(new FlyingEnemy(world, "data/Enemies/bat.png", tO.x/AmpDom.PIXELS_PER_METER, tO.y/AmpDom.PIXELS_PER_METER,64,64));
//					}
//				}
//			}
			ArrayList<TiledObjectGroup> objectGroup1 = tiledMapHelper.getMap().objectGroups;
			for(TiledObjectGroup tG: objectGroup1)
			{
				for(TiledObject tO: tG.objects)
				{
					if(tO.name.equals("wolf"))
					{
						enemies.add(new Enemy(world, "data/Enemies/wolf.png", tO.x/AmpDom.PIXELS_PER_METER, tO.y/AmpDom.PIXELS_PER_METER, .7f, .7f,128,64));
					}
					if(tO.name.equals("snake"))
					{
						enemies.add(new Enemy(world, "data/Enemies/snake.png", tO.x/AmpDom.PIXELS_PER_METER, tO.y/AmpDom.PIXELS_PER_METER, .7f, .7f,128,64));
					}
					if(tO.name.equals("leaf"))//stationary platform
					{
						stationaryPlatforms.add(new StationaryPlatform(world, "data/Enemies/wolf.png", tO.x/AmpDom.PIXELS_PER_METER, tO.y/AmpDom.PIXELS_PER_METER, tO.width/AmpDom.PIXELS_PER_METER, 64, 21));
					}
					if(tO.name.equals("bees"))//beehive
					{
						droppers.add(new Dropper(world, "data/Objects/beehive.png", tO.x/AmpDom.PIXELS_PER_METER, tO.y/AmpDom.PIXELS_PER_METER, tO.width/AmpDom.PIXELS_PER_METER, 64, 21));
					}
					if(tO.name.equals("owl"))
					{
						flyers.add(new FlyingEnemy(world, "data/Enemies/bat.png", tO.x/AmpDom.PIXELS_PER_METER, tO.y/AmpDom.PIXELS_PER_METER,64,64));
					}
					if(tO.name.equals("flyjar"))
					{
						jar = new FlyJar(world, "data/Objects/flyjar.png", tO.x/AmpDom.PIXELS_PER_METER, tO.y/AmpDom.PIXELS_PER_METER);
					}
				}
			}
			
			tiledMapHelper.prepareCamera(screenWidth, screenHeight);
		    tiledMapHelper.setPackerDirectory("data/packer/level2");
			tiledMapHelper.loadMap("data/world/level2/level2.tmx");
	        tiledMapHelper.loadCollisions("data/packer/level2/collision2", world,
					PIXELS_PER_METER);
			break;
		case 4:
			bgMusic = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/sounds/forest.mp3", FileType.Internal));
			bgMusic.setLooping(true);
			bgMusic.play();
			levelbg = new Texture(Gdx.files.internal("data/world/level2/forestbg.png"));
			levelSprite = new Sprite(levelbg,0,0,8192,1024);
			tiledMapHelper.prepareCamera(screenWidth, screenHeight);
		    tiledMapHelper.setPackerDirectory("data/packer/level2");
			tiledMapHelper.loadMap("data/world/level2/level2.2.tmx");
			tiledMapHelper.loadCollisions("data/packer/level2/collision2", world,
					PIXELS_PER_METER);
			break;
		case 6:
			bgMusic = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/sounds/mountain.mp3", FileType.Internal));
			bgMusic.setLooping(true);
			bgMusic.play();
			levelbg = new Texture(Gdx.files.internal("data/world/level3/mountainbg.png"));
			levelSprite = new Sprite(levelbg,0,0,1024,8192);
			levelSprite.setScale(1.8f,2.0f);
			
			tiledMapHelper.prepareCamera(screenWidth, screenHeight);
			tiledMapHelper.setPackerDirectory("data/packer/level3");
			tiledMapHelper.loadMap("data/world/level3/level3.tmx");
			tiledMapHelper.loadCollisions("data/packer/level3/collisions3.txt", world,
					PIXELS_PER_METER);
			
			break;
		case 8:
			bgMusic = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/sounds/mountain.mp3", FileType.Internal));
			bgMusic.setLooping(true);
			bgMusic.play();
			levelbg = new Texture(Gdx.files.internal("data/world/level3/mountainbg.png"));
			levelSprite = new Sprite(levelbg,0,0,1024,8192);
		    levelSprite.setScale(1.8f,2.0f);
			
			tiledMapHelper.prepareCamera(screenWidth, screenHeight);
			tiledMapHelper.setPackerDirectory("data/packer/level3");
			tiledMapHelper.loadMap("data/world/level3/level3.2.tmx");
			
			tiledMapHelper.loadCollisions("data/packer/level3/collisions3.txt", world,
					PIXELS_PER_METER);
			
			break;
		case 10:
			bgMusic = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/sounds/desert.mp3", FileType.Internal));
			bgMusic.setLooping(true);
			bgMusic.play();
			levelbg = new Texture(Gdx.files.internal("data/world/level4/desertbg.png"));
			levelSprite = new Sprite(levelbg,0,0,8192,1024);
			levelSprite.setScale(2.5f,.75f);
			levelSprite.setPosition(0,-65);
			
			tiledMapHelper.prepareCamera(screenWidth, screenHeight);
			tiledMapHelper.setPackerDirectory("data/packer/level4");
			tiledMapHelper.loadMap("data/world/level4/level4.tmx");
			
			tiledMapHelper.loadCollisions("data/packer/level4/collisions4.txt", world,
					PIXELS_PER_METER);
			
			break;
		case 12:
			bgMusic = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/sounds/desert.mp3", FileType.Internal));
			bgMusic.setLooping(true);
			bgMusic.play();
			levelbg = new Texture(Gdx.files.internal("data/world/level4/desertbg.png"));
			levelSprite = new Sprite(levelbg,0,0,8192,1024);
			levelSprite.setScale(2.5f,.75f);
			levelSprite.setPosition(0,-65);
			
			tiledMapHelper.prepareCamera(screenWidth, screenHeight);
			tiledMapHelper.setPackerDirectory("data/packer/level4");
			tiledMapHelper.loadMap("data/world/level4/level4.2.tmx");
			
			tiledMapHelper.loadCollisions("data/packer/level4/collisions4.txt", world,
					PIXELS_PER_METER);
			break;
		case 14:
			bgMusic = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/sounds/jungle.mp3", FileType.Internal));
			bgMusic.setLooping(true);
			bgMusic.play();
			levelbg = new Texture(Gdx.files.internal("data/world/level5/junglebg.png"));
			levelSprite = new Sprite(levelbg,0,0,8192,1024);
		    levelSprite.setScale(2.5f,1.0f);
			
			tiledMapHelper.prepareCamera(screenWidth, screenHeight);
			tiledMapHelper.setPackerDirectory("data/packer/level5");
			tiledMapHelper.loadMap("data/world/level5/level5.tmx");
			
			tiledMapHelper.loadCollisions("data/packer/level5/collisions5.txt", world,
					PIXELS_PER_METER);
			break;
		case 16:
			bgMusic = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/sounds/jungle.mp3", FileType.Internal));
			bgMusic.setLooping(true);
			bgMusic.play();
			levelbg = new Texture(Gdx.files.internal("data/world/level5/junglebg.png"));
			levelSprite = new Sprite(levelbg,0,0,8192,1024);
		    levelSprite.setScale(2.5f,1.0f);
			
			tiledMapHelper.prepareCamera(screenWidth, screenHeight);
			tiledMapHelper.setPackerDirectory("data/packer/level5");
			tiledMapHelper.loadMap("data/world/level5/level5.2.tmx");
			
			tiledMapHelper.loadCollisions("data/packer/level5/collisions5.txt", world,
					PIXELS_PER_METER);
			break;
		case 18:
			bgMusic = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/sounds/castle.mp3", FileType.Internal));
			bgMusic.setLooping(true);
			bgMusic.play();
			levelbg = new Texture(Gdx.files.internal("data/world/level6/castlebg.png"));
			levelSprite = new Sprite(levelbg,0,0,8192,1024);
			levelSprite.setScale(2.5f,1.0f);
			levelSprite.setPosition(6000f, 0);
			
			tiledMapHelper.prepareCamera(screenWidth, screenHeight);
			tiledMapHelper.setPackerDirectory("data/packer/level6");
			tiledMapHelper.loadMap("data/world/level6/level6.tmx");
			
			tiledMapHelper.loadCollisions("data/packer/level6/collisions6.txt", world,
					PIXELS_PER_METER);
			break;
		} 
	}
	
	
	public TiledMapHelper getMap(){
		return tiledMapHelper;
	}
	public void setLevel(/*CurrentLevel*/int currentLevel){
		this.currentLevel = currentLevel;
	}
	
	public void deleteLevel(){
		
		for(FlyingEnemy f : flyers){
			if(f != null)
	       world.destroyBody(f.entity);
			}
			
			for(Enemy e : LevelMap.enemies){
				if(e != null)
				  world.destroyBody(e.entity);
			}
			
			for(Obstacle s : LevelMap.spikes){
				if(s != null)
				  world.destroyBody(s.entity);
			}
			
			for(MovingPlat m: LevelMap.plats){
				if(m!=null)
					world.destroyBody(m.entity);
				
			}
			//falling elements
			for(Dropper d: LevelMap.droppers)
			{
				world.destroyBody(d.body);
			}
			
			//stationary platforms
			for(StationaryPlatform s: LevelMap.stationaryPlatforms)
			{
				world.destroyBody(s.body);
			}			
			
			//falling logs
			for(WaterLog w: LevelMap.fallingLogs)
			{
				world.destroyBody(w.body);
			}
			
			//sandstorms
			for(Sandstorm s: LevelMap.sandstorms)
			{
				world.destroyBody(s.body);
			}			
			//moving platforms
			for(MovingPlatform m: LevelMap.movingPlatforms)
			{
				world.destroyBody(m.body);
			}
			
		levelbg.dispose();
		levelSprite.getTexture().dispose();
		endlevelpt1.batch.dispose();
        jar.batch.dispose();
	    flyers.clear();
		spikes.clear();
		enemies.clear();
		plats.clear();
		fallingLogs.clear();
		droppers.clear();
		stationaryPlatforms.clear();
		sandstorms.clear();
		movingPlatforms.clear();
		bgMusic.dispose();
		System.out.println("before dispose  "+tiledMapHelper);
        tiledMapHelper.dispose();	
	}
	
	public void setCurrentLevel(int currentLevel)
	{
		this.currentLevel = currentLevel;
	}
}
	

