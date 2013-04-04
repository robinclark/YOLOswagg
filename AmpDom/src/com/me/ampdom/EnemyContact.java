package com.me.ampdom;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Shape;

public class EnemyContact implements ContactListener  {

	

boolean enemyDmg=false;
boolean isHit = false;
boolean obstacleDmg = false;
boolean endLevel = false;
boolean collectJar = false;
boolean attackEnem = false;
static boolean grounded = false;
Vector2 enemPos;

@Override
public void beginContact(Contact contact) {
		final Fixture a=contact.getFixtureA();
		final Fixture b = contact.getFixtureB();
		
		/**********************************************************/
		//--bullet sensor contact
		if(a.getBody().getUserData()=="SPIT" || b.getBody().getUserData()=="SPIT")
		{
			System.out.println("spit collision");
			if(a.getBody().getUserData()=="ENEMY") //register damage on enemy
			{
				
			}				
			//destroy spit
		}
		
		//--floor contact
		if(a.getType() == Shape.Type.Edge || b.getType() == Shape.Type.Edge)
		{
			if(a.getBody().getUserData()=="PLAYER")
				System.out.println("a");
			if(b.getBody().getUserData()=="PLAYER") 
				if(b.getType() == Shape.Type.Edge)
					System.out.println("b");
				//if(b.getType() == Shape.Type.Polygon)
					//System.out.println("b");
			//System.exit(0);			
		}
		
		/**********************************************************/
	if(a.getBody().getUserData()!= null &&  b.getBody().getUserData()!=null)
	{		
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="ENEMY")
		{
	//		System.out.println("enemy contact 1");
			enemyDmg =true;
			isHit = true;
			
//			a.getBody().applyLinearImpulse(new Vector2(.5f,0.f),//0.8f),
//		    		new Vector2(a.getBody().getPosition().x, a.getBody().getPosition().y));
		}
		
		if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="PLAYER")
		{
		//    System.out.println("enemy contact 2");
		    enemyDmg =true;
			isHit = true;
			
//			b.getBody().applyLinearImpulse(new Vector2(.5f,0.f),//0.8f),
//		    		new Vector2(b.getBody().getPosition().x, b.getBody().getPosition().y));
		}
		
		
		
		if(a.getBody().getUserData()=="FOOT" && b.getBody().getUserData()=="GROUND")
			   grounded=true;
		if(a.getBody().getUserData()=="GROUND" && b.getBody().getUserData()=="FOOT")
			   grounded=true;
		
		
		

		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="FLYJAR")
		{
		  //  System.out.println("JAR");
		   collectJar=true;
		}
		
		if(a.getBody().getUserData()=="FLYJAR" && b.getBody().getUserData()=="PLAYER")
		{
		    //System.out.println("JAR");
		   collectJar=true;
		}
		
		if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="TONGUE")
		{
		    System.out.println("TONGUE");
		    enemPos=a.getBody().getPosition();
		}
		
		if(a.getBody().getUserData()=="TONGUE" && b.getBody().getUserData()=="ENEMY")
		{
		    System.out.println("TONGUE");
		    enemPos=b.getBody().getPosition();
		}

		/*why is this duplicated here*/
		if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="TONGUE")
		{
				  attackEnem = true;
		}
		if(a.getBody().getUserData()=="TONUGE" && b.getBody().getUserData()=="ENEMY")
		{
			  attackEnem = true;
		}
		/*why is this duplicated here*/
		
		
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="OBSTACLE")
		{
		    //System.out.println("spike contact 1");
		    obstacleDmg =true;
		    isHit = true;
//		    a.getBody().applyLinearImpulse(new Vector2(0.0f,0.95f),//0.8f),
//		    		a.getBody().getWorldCenter());
		}
			
		if(a.getBody().getUserData()=="OBSTACLE" && b.getBody().getUserData()=="PLAYER")
		{
		    //System.out.println("spike contact 2");
		    obstacleDmg =true;
			   isHit = true;
//			   b.getBody().applyLinearImpulse(new Vector2(0.0f,.95f),//0.8f),
//					   b.getBody().getWorldCenter());
		}
			
			
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="ENDLEVELTRIGGER")
		{
			//System.out.println("spike contact 1");
			endLevel =true;
			//a.getBody().applyLinearImpulse(new Vector2(0f,4.f),//0.8f),
		    	//	new Vector2(b.getBody().getPosition().x, b.getBody().getPosition().y));
			 
		}
				
		if(a.getBody().getUserData()=="ENDLEVELTRIGGER" && b.getBody().getUserData()=="PLAYER")
		{
		    endLevel = true;
		    //b.getBody().applyLinearImpulse(new Vector2(0f,4.f),//0.8f),
		    	//	new Vector2(a.getBody().getPosition().x, a.getBody().getPosition().y));
		    
		}	
	}	
}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		final Fixture a = contact.getFixtureA();
		final Fixture b = contact.getFixtureB();
		
			if(contact.getFixtureA().getBody().getUserData()!= null && 
				contact.getFixtureB().getBody().getUserData()!=null){
			
			if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="ENEMY"){
			    //System.out.println("enemy contact 1");
			   enemyDmg =false;
			   isHit = false;
		
			}
				
			if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="PLAYER"){
		//	    System.out.println("enemy contact 2");
			    enemyDmg =false;
				   isHit = false;
					
			}
				
				

				if(a.getBody().getUserData()=="FOOT" && b.getBody().getUserData()=="GROUND")
					   grounded=false;
				if(a.getBody().getUserData()=="GROUND" && b.getBody().getUserData()=="FOOT")
					   grounded=false;
				
				
				
				if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="TONGUE"){
						  attackEnem = true;
						  enemPos=a.getBody().getPosition();
				}
				if(a.getBody().getUserData()=="TONUGE" && b.getBody().getUserData()=="ENEMY"){
					  attackEnem = true;
					  enemPos=a.getBody().getPosition();
				}
			
			if(a.getBody().getUserData()=="TONUGE" && b.getBody().getUserData()=="ENEMY"){
				  attackEnem = true;
				  enemPos=b.getBody().getPosition();
			}
				
			if(a.getBody().getUserData()=="TONGUE" && b.getBody().getUserData()=="PLAYER"){
			    System.out.println("TONGUE");
			  
			}
			if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="TONGUE"){
			    System.out.println("TONGUE");
			  
			}
				
			if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="OBSTACLE"){
			//    System.out.println("spike contact 1");
			   obstacleDmg =false;
			   isHit = false;
			   
			}
					
			if(a.getBody().getUserData()=="OBSTACLE" && b.getBody().getUserData()=="PLAYER"){
			//    System.out.println("spike contact 2");
			    obstacleDmg =false;
				   isHit = false;
			}
					
					
			if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="ENDLEVELTRIGGER"){
		  //  System.out.println("spike contact 1");
		   endLevel =true;
		   
			}
			
			if(a.getBody().getUserData()=="ENDLEVELTRIGGER" && b.getBody().getUserData()=="PLAYER"){
			    endLevel = true;
			    System.out.println("END LEVEL");
				  
			}
			if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="FLYJAR"){
			  //  System.out.println("JAR");
			   collectJar=true;
			}
			
			if(a.getBody().getUserData()=="FLYJAR" && b.getBody().getUserData()=="PLAYER"){
			//    System.out.println("JAR");
			   collectJar=true;
			}				 
	   }
	}
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		final Fixture a=contact.getFixtureA();
		final Fixture b = contact.getFixtureB();
		
		if(contact.getFixtureA().getBody().getUserData()!= null && 
		   contact.getFixtureB().getBody().getUserData()!=null){
			
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="SHOUT")
				contact.setEnabled(false);

	    if(a.getBody().getUserData()=="SHOUT" && b.getBody().getUserData()=="PLAYER")
		    contact.setEnabled(false);
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="ENEMY")
			contact.setEnabled(false);
	    if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="PLAYER")
		    contact.setEnabled(false);
	    
	    if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="MOVINGPLAT")
			contact.setEnabled(false);

	    
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="OBSTACLE")
			contact.setEnabled(false);
	    if(a.getBody().getUserData()=="OBSTACLE" && b.getBody().getUserData()=="PLAYER")
		    contact.setEnabled(false);
	    
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="ENDLEVELTRIGGER")
			contact.setEnabled(false);

		    if(a.getBody().getUserData()=="MOVINGPLAT" && b.getBody().getUserData()=="ENEMY")
			    contact.setEnabled(false);
		    

		    if(a.getBody().getUserData()=="OBSTACLE" && b.getBody().getUserData()=="PLAYER")
			    contact.setEnabled(false);
		    
		    if(a.getBody().getUserData()=="FOOT" && b.getBody().getUserData()=="PLAYER")
		    	contact.setEnabled(false);
			if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="FOOT")
				contact.setEnabled(false);
		    
			if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="ENDLEVELTRIGGER")
				contact.setEnabled(false);
			if(a.getBody().getUserData()=="ENDLEVELTRIGGER" && b.getBody().getUserData()=="PLAYER")
			   contact.setEnabled(false);
					  
		if(a.getBody().getUserData()=="FLYJAR" && b.getBody().getUserData()=="PLAYER")
			   contact.setEnabled(false);
				
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="FLYJAR")
			   contact.setEnabled(false);
		if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="FLYJAR")
			   contact.setEnabled(false);
		if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="ENEMY")
			   contact.setEnabled(false);
		if(a.getBody().getUserData()=="FLYJAR" && b.getBody().getUserData()=="ENEMY")
			   contact.setEnabled(false);
		if(a.getBody().getUserData()=="TONGUE" && b.getBody().getUserData()=="PLAYER")
			   contact.setEnabled(false);
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="TONGUE")
			   contact.setEnabled(false);
	
		}
	}

	//why do we need this?
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		final Fixture a=contact.getFixtureA();
		final Fixture b = contact.getFixtureB();
		
		
		if(contact.getFixtureA().getBody().getUserData()!= null && contact.getFixtureB().getBody().getUserData()!=null){
		
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="ENEMY")
			contact.setEnabled(false);
	    if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="PLAYER")
		    contact.setEnabled(false);
	    
	    if(a.getBody().getUserData()=="FOOT" && b.getBody().getUserData()=="PLAYER")
	    	contact.setEnabled(false);
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="FOOT")
			contact.setEnabled(false);
	    
	    if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="SHOUT")
			contact.setEnabled(false);
	    if(a.getBody().getUserData()=="SHOUT" && b.getBody().getUserData()=="PLAYER")
		    contact.setEnabled(false);
	    
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="OBSTACLE")
			contact.setEnabled(false);
	    if(a.getBody().getUserData()=="OBSTACLE" && b.getBody().getUserData()=="PLAYER")
		    contact.setEnabled(false);
	    
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="ENDLEVELTRIGGER")
			contact.setEnabled(false);
		if(a.getBody().getUserData()=="ENDLEVELTRIGGER" && b.getBody().getUserData()=="PLAYER")
		   contact.setEnabled(false);
		if(a.getBody().getUserData()=="FLYJAR" && b.getBody().getUserData()=="PLAYER")
			   contact.setEnabled(false);
				
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="FLYJAR")
			   contact.setEnabled(false);
		
		if(a.getBody().getUserData()=="FLYJAR" && b.getBody().getUserData()=="PLAYER")
			   contact.setEnabled(false);
			
		if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="FLYJAR")
			   contact.setEnabled(false);
		if(a.getBody().getUserData()=="FLYJAR" && b.getBody().getUserData()=="ENEMY")
			   contact.setEnabled(false);
		if(a.getBody().getUserData()=="TONGUE" && b.getBody().getUserData()=="PLAYER")
			   contact.setEnabled(false);
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="TONGUE")
			   contact.setEnabled(false);
		}
	}

}
