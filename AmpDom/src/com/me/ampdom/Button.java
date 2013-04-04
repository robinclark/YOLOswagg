package com.me.ampdom;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Button{
	public OrthographicCamera guiCam;
	public int xLoc, yLoc;
	public int width, height;
	public Sprite icon;
	public SpriteBatch sb;
	public Texture t, t2;
	public final boolean clickable;
	public Vector3 touchpoint;
	public String name;
	public int toggle =-1;
	
	public Button(String name, int x, int y, boolean clickable, Texture t, Texture t2, OrthographicCamera c){
		this.name = name;		
		this.t = t;
		this.t2 = t2;
		
		xLoc = x;
		yLoc = y;
		guiCam = c;
		guiCam.setToOrtho(false);
		this.clickable = clickable;
		touchpoint = new Vector3(-1,-1,-1);
		initialize();
	}
	
	public void initialize(){
		icon = new Sprite(t);
		icon.scale(0.02f);
		icon.setPosition(xLoc, yLoc);
		
		width = t.getWidth()/7;
		height = t.getHeight()/7;
		icon.setSize(width,height);
		sb = new SpriteBatch();	
	}
	
	public void draw(){
		sb.begin();
		icon.draw(sb);
		sb.end();
	}
	
	public void changeIcon(Texture t){
		icon = new Sprite(t);
		icon.setPosition(xLoc, yLoc);
		width = t.getWidth()/7;
		height = t.getHeight()/7;
		icon.setSize(width,height);
		sb = new SpriteBatch();
	}
	
	public boolean clicked(){
		boolean clicked = false;
		if(clickable){
			if (Gdx.input.justTouched()){
			      guiCam.unproject(touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				if(inBounds(touchpoint.x, touchpoint.y)){
					//toggle button icon
					switch(toggle){
						case -1:
							toggle = 1;
							changeIcon(t2);
							clicked = true;
							break;
						case 1:
							toggle = -1;
							changeIcon(t);
							clicked = true;
							break;						
					}
				}
			}
		}
		return clicked;
	}
	
	public boolean inBounds(float x, float y){
		 if(x >= xLoc && x <= (xLoc + width)){
			 if(y >= yLoc && y <= (yLoc + height)){
				 return true;
			 }else{
				 return false;
			 }
		 }else{
			 return false;
		 }
	}
	
	public void dispose(){
		sb.dispose();
		t.dispose();
		t2.dispose();
		
	}
}
