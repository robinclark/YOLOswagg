package com.me.ampdom;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Menu {
	public ArrayList<Button> buttons;
	public OrthographicCamera cam;
	public Texture start, main, htp, cred, cut1;
	public Texture back_main, credits, htpb, pause, play;
	public Texture play_main, son, soff, startb, next;
	public Sprite icon;
	public SpriteBatch sb;
	public Music test;
	
	//creates a menu with a specified number of buttons
	public Menu(OrthographicCamera cam){
		
		this.cam = cam;
		buttons = new ArrayList<Button>();
		//screen textures
		cut1 = new Texture(Gdx.files.internal("data/Cutscenes/cutscene_test.png"));
		start = new Texture(Gdx.files.internal("data/Menu/Start_Screen copy.png"));
		main = new Texture(Gdx.files.internal("data/Menu/Main_Screen copy.png"));
		htp = new Texture(Gdx.files.internal("data/Menu/HTP_Screen copy.png"));
		cred = new Texture(Gdx.files.internal("data/Menu/Credits_Screen copy.png"));
		//button textures
		back_main = new Texture(Gdx.files.internal("data/Menu/back_main copy.png"));
		credits = new Texture(Gdx.files.internal("data/Menu/credits_main copy.png"));
		htpb = new Texture(Gdx.files.internal("data/Menu/HTP_main copy.png"));
		//pause = new Texture(Gdx.files.internal("data/Menu/pause copy.png"));
		//play = new Texture(Gdx.files.internal("data/Menu/play copy.png"));
		play_main = new Texture(Gdx.files.internal("data/Menu/play_main copy.png"));
		son = new Texture(Gdx.files.internal("data/Menu/sound1 copy.png"));
		soff = new Texture(Gdx.files.internal("data/Menu/sound2 copy.png"));
		startb = new Texture(Gdx.files.internal("data/Menu/start_button copy.png"));
		next = new Texture(Gdx.files.internal("data/Menu/next copy.png"));
		addBackground();
		initializeButtons();
//		test = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/sounds/beat.mp3", FileType.Internal));
//		test.setLooping(true);
//		test.play();
	}
	
	public void initializeButtons(){
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		
		//clickable buttons
		buttons.add(new Button("next",w-80,10,true,next,next, cam));
		buttons.add( new Button("start",w/2 - 50,h - (h - 50),true,startb,startb, cam) );
		buttons.add( new Button("sound",w - 50,h - 50,true,son,soff, cam) );
		//buttons.add( new Button("play",760,580,true,pause,play, cam) );
		buttons.add( new Button("play_main",w/2 - 100,h/2,true,play_main, play_main, cam) );
		buttons.add( new Button("back",10, 10,true,back_main,back_main, cam) );
		buttons.add( new Button("credits",w - 80,10,true,credits,credits, cam) );
		buttons.add( new Button("htp",w/2+50,h/2,true,htpb,htpb, cam) );
		//non-clickable buttons
		
		
	}
	
	public void addButton(Button b){
		buttons.add(b);
	
	}
	
	public void addBackground(){		
		icon = new Sprite(start);
		icon.setPosition(0, 0);
		sb = new SpriteBatch();	
		icon.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
	}	
	
	public void changeBackground(Texture t){
		icon = new Sprite(t);
		icon.setPosition(0, 0);
		sb = new SpriteBatch();
		icon.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
	}
	
	public void display(){
		draw();
		for(Button b : buttons){
			
			//menu states: (-5) - start 
			//(-4) - main 
			//(-3) - how to play 
			//(-2) - credits
			//(-1) - first cut scene
			//(evens) - play screens
			//(odds) - cut scenes
				if(AmpDom.state == -5){
					if(b.name.equals("start")){
						b.draw();
					}
					if(b.clicked() && b.name.equals("start")) {
						b.draw();	
						AmpDom.state = -4;
					}
				}else if(AmpDom.state == -4){
					if(b.name.equals("sound") || b.name.equals("htp") || b.name.equals("play_main") || b.name.equals("credits")){
						b.draw();
					}					
					if(b.clicked() && b.name.equals("htp")){
						b.draw();	
						AmpDom.state = -3;
					}
					if(b.clicked() && b.name.equals("credits")){
						b.draw();	
						AmpDom.state = -2;
					}
					
					if(b.clicked() && b.name.equals("play_main")){
						b.draw();	
						AmpDom.state = -1;
					}
					changeBackground(main);					
				}else if(AmpDom.state == -3 || AmpDom.state == -2){
					if(b.name.equals("sound") || b.name.equals("back")){
						b.draw();
					}
					if(b.clicked() && b.name.equals("back")){
						b.draw();	
						AmpDom.state = -4;
					}
					if(AmpDom.state == -3)
						changeBackground(htp);
					else if(AmpDom.state == -2)
						changeBackground(cred);
					
				}
				//game play screens
				else if(AmpDom.state%2 == 0 && AmpDom.state > 0){
					
					if(b.name.equals("sound")){						
						b.draw();
						b.clicked();
						if(b.toggle == -1) {
						//	test.setVolume(100);
						}
						else {
							test.setVolume(0);
						}
					}
					
				}//all cut scenes
				else if((AmpDom.state%2 == 1 && AmpDom.state > 0) || AmpDom.state == -1){
					if(b.name.equals("next")){
						b.draw();
					}
					if(b.clicked() && b.name.equals("next")){
						b.draw();	
						if(AmpDom.state == -1)
							AmpDom.state = 0;
						else if(AmpDom.state == 1)
							AmpDom.state = 2;
						else if(AmpDom.state == 3)
							AmpDom.state = 4;
						else if(AmpDom.state == 5)
							AmpDom.state = 6;
						else if(AmpDom.state == 7)
							AmpDom.state = 8;
						else if(AmpDom.state == 9)
							AmpDom.state = 10;
						else if(AmpDom.state == 11)
							AmpDom.state = 12;
						else if(AmpDom.state == 13)
							AmpDom.state = 14;
						else if(AmpDom.state == 15)
							AmpDom.state = 16;
						else if(AmpDom.state == 17)
							AmpDom.state = 18;
						else if(AmpDom.state == 19)
							AmpDom.state = -4;
						
					}
					
					changeBackground(cut1);
					
				}
			
			}
	}
	
	public void draw(){
		sb.begin();
		if(AmpDom.state % 2 == 0 && AmpDom.state >= 0){
			icon.draw(sb, 0.0f);
		}else{
			icon.draw(sb, 1.0f);
		}
		sb.end();
	}
	

	public void dispose(){
		sb.dispose();
		start.dispose();
		main.dispose();
		htp.dispose();
		cred.dispose();
		back_main.dispose();
		credits.dispose();
		htpb.dispose();
//		pause.dispose();
//		play.dispose();
		play_main.dispose();
		son.dispose();
		soff.dispose();
		for(Button b : buttons){
			b.dispose();
		}
	}
}
