package com.me.ampdom;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Shape;

public class EnemyContact implements ContactListener  {
static boolean enemyDmg=false;
static boolean isHit = false;
boolean obstacleDmg = false;
boolean endLevel = false;
boolean collectJar = false;
boolean attackEnem = false;
static boolean grounded = false;
Vector2 enemPos;
boolean dropperHit;
boolean spitEnem = false;
boolean shoutEnem = false;
static boolean insideEnemy = false;

@Override
public void beginContact(Contact contact) {
		final Fixture a=contact.getFixtureA();
		final Fixture b = contact.getFixtureB();
		
		/*special bodies*/
		/**********************************************************/
		//--moving platform contact
		if(a.getBody().getUserData()=="FOOT" && b.getBody().getUserData()=="MOVING_PLATFORM")
			   grounded=true;
		if(a.getBody().getUserData()=="MOVING_PLATFORM" && b.getBody().getUserData()=="FOOT")
			   grounded=true;
		
		//--stationary platform
		if(a.getBody().getUserData()=="FOOT" && b.getBody().getUserData()=="STATIONARY_PLATFORM")
			   grounded=true;
		if(a.getBody().getUserData()=="STATIONARY_PLATFORM" && b.getBody().getUserData()=="FOOT")
			   grounded=true;		
		/**********************************************************/
		
	if(a.getBody().getUserData()!= null &&  b.getBody().getUserData()!=null)
	{	
		/*player enemy touch*/
		/*******************************************************************************/
		//--tongue
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="ENEMY")
		{
			enemyDmg =true;
			isHit = true;
			insideEnemy=true;
		
		}
		
		if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="PLAYER")
		{
		    enemyDmg =true;
			isHit = true;
			insideEnemy=true;
		
		}
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="GROUND")
			   grounded=false;
		if(a.getBody().getUserData()=="GROUND" && b.getBody().getUserData()=="PLAYER")
			   grounded=false;	
		
		if(a.getBody().getUserData()=="FOOT" && b.getBody().getUserData()=="GROUND")
			   grounded=true;
		if(a.getBody().getUserData()=="GROUND" && b.getBody().getUserData()=="FOOT")
			   grounded=true;	
		
		/*flyjar*/
		/*******************************************************************************/
		//--flyjar
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
		
		/*alabaster attacks*/
		/*******************************************************************************/
		//--tongue
		if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="TONGUE")
		{
		    System.out.println("TONGUE");
		    enemPos=a.getBody().getPosition();
		    attackEnem = true;
		}
		
		if(a.getBody().getUserData()=="TONGUE" && b.getBody().getUserData()=="ENEMY")
		{
		    System.out.println("TONGUE");
		    enemPos=b.getBody().getPosition();
		    attackEnem = true;
		}		
		
		//--spit
		if((a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="LEFT_SPIT") || (a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="RIGHT_SPIT"))
		{
			System.out.println("spit");
			enemPos=a.getBody().getPosition();
				  //attackEnem = true;
				  spitEnem = true;
		}
		if((b.getBody().getUserData()=="ENEMY" && a.getBody().getUserData()=="LEFT_SPIT") || (b.getBody().getUserData()=="ENEMY" && a.getBody().getUserData()=="RIGHT_SPIT"))
		{
			System.out.println("spit");
			enemPos=b.getBody().getPosition();			
			  //attackEnem = true;
			  spitEnem = true;
		}		
		
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="DROPPER"){
			dropperHit=true;
	    System.out.println("dopper");
		}
		if(a.getBody().getUserData()=="DROPPER" && b.getBody().getUserData()=="PLAYER"){
			dropperHit=true;	
	    System.out.println("dopper");
		}
		
		//--shout
		if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="SHOUT")
		{
			if(spitEnem) System.out.println("spit true");
			enemPos=a.getBody().getPosition();
				  shoutEnem = true;
		}
		if(a.getBody().getUserData()=="SHOUT" && b.getBody().getUserData()=="ENEMY")
		{
			if(spitEnem) System.out.println("spit true");
			enemPos=b.getBody().getPosition();
			shoutEnem = true;
		}
		
		/*obstacles*/
		/*******************************************************************************/
		//--spikes, poison, plants
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="OBSTACLE")
		{
		    obstacleDmg =true;
		    isHit = true;
		}			
		if(a.getBody().getUserData()=="OBSTACLE" && b.getBody().getUserData()=="PLAYER")
		{
		    obstacleDmg =true;
			isHit = true;
		}		
		
		/*end level trigger*/
		/*******************************************************************************/		
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
		/*******************************************************************************/
	}	
}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		final Fixture a = contact.getFixtureA();
		final Fixture b = contact.getFixtureB();
		
			if(contact.getFixtureA().getBody().getUserData()!= null && 
				contact.getFixtureB().getBody().getUserData()!=null){
			
				
				if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="DROPPER"){
					dropperHit=false;
				}
				if(a.getBody().getUserData()=="DROPPER" && b.getBody().getUserData()=="PLAYER"){
					dropperHit=false;	
				}
				
				
			if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="ENEMY"){
			    //System.out.println("enemy contact 1");
			  ///enemyDmg =true;
				System.out.println("inside enemy");
				insideEnemy=false;
			    isHit = false;
			    enemyDmg=false;
		
			}
				
			if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="PLAYER"){
		//	    System.out.println("enemy contact 2");
			 // enemyDmg =true;
				System.out.println("inside enemy");
				   insideEnemy=false;
				   isHit = false;
				   enemyDmg=false;
					
			}
				
			if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="TONGUE"){
					  attackEnem = true;
					  enemPos=a.getBody().getPosition();
			}
			if(a.getBody().getUserData()=="TONGUE" && b.getBody().getUserData()=="ENEMY"){
				  attackEnem = true;
				  enemPos=a.getBody().getPosition();
			}
						
			if(a.getBody().getUserData()=="TONGUE" && b.getBody().getUserData()=="PLAYER"){
			    System.out.println("TONGUE");
			  
			}
			if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="TONGUE"){
			    System.out.println("TONGUE");
			  
			}
			
			if((a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="LEFT_SPIT") || (a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="RIGHT_SPIT"))
			{
					  System.out.println("spit false");					 
					  spitEnem = false;
					  //attackEnem = true;
					  enemPos=a.getBody().getPosition();
			}
			
			if((b.getBody().getUserData()=="ENEMY" && a.getBody().getUserData()=="LEFT_SPIT") || (b.getBody().getUserData()=="ENEMY" && a.getBody().getUserData()=="RIGHT_SPIT"))
			{
				System.out.println("spit false");
					spitEnem = false;
				  //attackEnem = true;
				  enemPos=a.getBody().getPosition();
			}

			if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="SHOUT")
			{
				
				shoutEnem = true;
					  enemPos=a.getBody().getPosition();
			}
			if(a.getBody().getUserData()=="SHOUT" && b.getBody().getUserData()=="ENEMY")
			{
				shoutEnem = true;
				  enemPos=a.getBody().getPosition();
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
			
 

		/* 572f3d9df0a003f0887501802299d7ca083ed6d2
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="SHOUT")
				contact.setEnabled(false);

	    if(a.getBody().getUserData()=="SHOUT" && b.getBody().getUserData()=="PLAYER")
		    contact.setEnabled(false);
	    
	    if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="SPIT")
			contact.setEnabled(false);

	    if(a.getBody().getUserData()=="SPIT" && b.getBody().getUserData()=="PLAYER")
	    	contact.setEnabled(false);
	    */
		/*********************************************************************************************/	
		//--leaves n shat
		if(a.getBody().getUserData()=="PLAYER"  && (b.getBody().getUserData()=="STATIONARY_PLATFORM"))
		{
			//if player lower than body, turn off collision
			if(a.getBody().getPosition().y > b.getBody().getPosition().y)
				contact.setEnabled(false);
		}		
	    if((a.getBody().getUserData()=="STATIONARY_PLATFORM") && (b.getBody().getUserData()=="PLAYER"))
	    {    	
	    	if(a.getBody().getPosition().y > b.getBody().getPosition().y)
				contact.setEnabled(false);
	    }
	    
	    //--moving platforms
	    if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="MOVING_PLATFORM")
		{
			//if player lower than body, turn off collision
			if(a.getBody().getPosition().y > b.getBody().getPosition().y)
			{
				System.out.println("enable contact");
				contact.setEnabled(false);
			}
		}		
	    if((a.getBody().getUserData()=="MOVING_PLATFORM") && (b.getBody().getUserData()=="PLAYER"))
	    {    	
	    	if(a.getBody().getPosition().y > b.getBody().getPosition().y)
	    	{
				System.out.println("enable contact");
				contact.setEnabled(false);
			}
	    }
	    
	    //--player log 
	    if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="WATER_LOG")
		{
			//if player lower than body, turn off collision
			if(a.getBody().getPosition().y > b.getBody().getPosition().y)
			{
				System.out.println("enable contact foot");
				contact.setEnabled(false);
			}
		}		
	    
	    if((a.getBody().getUserData()=="WATER_LOG") && (b.getBody().getUserData()=="PLAYER"))
	    {    	
	    	if(a.getBody().getPosition().y > b.getBody().getPosition().y)
	    	{
				System.out.println("enable contact foot");
				contact.setEnabled(false);
			}
	    }
	    /*********************************************************************************************/	
	    
	    //--foot ground
	    if(a.getBody().getUserData()=="FOOT" && b.getBody().getUserData()=="GROUND")
			contact.setEnabled(false);
	    if(a.getBody().getUserData()=="GROUND" && b.getBody().getUserData()=="FOOT")
		    contact.setEnabled(false);
	    
	    //--character enemy
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="ENEMY")
			contact.setEnabled(false);
	    if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="PLAYER")
		    contact.setEnabled(false);
	    
	    //--moving plat
	    if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="MOVINGPLAT")
			contact.setEnabled(false);
	    if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="MOVINGPLAT")
			contact.setEnabled(false);
	    
	    //--player obstacle
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="OBSTACLE")
			contact.setEnabled(false);
	    if(a.getBody().getUserData()=="OBSTACLE" && b.getBody().getUserData()=="PLAYER")
		    contact.setEnabled(false);
	    
	    //--tongue obstacle
	    if(a.getBody().getUserData()=="TONGUE" && b.getBody().getUserData()=="OBSTACLE")
			contact.setEnabled(false);
	    if(a.getBody().getUserData()=="OBSTACLE" && b.getBody().getUserData()=="TONGUE")
		    contact.setEnabled(false);
	    
	    //--shout obstacle
	    if(a.getBody().getUserData()=="SHOUT" && b.getBody().getUserData()=="OBSTACLE")
			contact.setEnabled(false);
	    if(a.getBody().getUserData()=="OBSTACLE" && b.getBody().getUserData()=="SHOUT")
		    contact.setEnabled(false);
	    
	    //--player endlevel trigger
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="ENDLEVELTRIGGER")
			contact.setEnabled(false);
	    if(a.getBody().getUserData()=="MOVINGPLAT" && b.getBody().getUserData()=="ENEMY")
		    contact.setEnabled(false);
		    
		//--foot player  
	    if(a.getBody().getUserData()=="FOOT" && b.getBody().getUserData()=="PLAYER")
	    	contact.setEnabled(false);
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="FOOT")
			contact.setEnabled(false);
	    
		//--player end level trigger
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="ENDLEVELTRIGGER")
			contact.setEnabled(false);
		if(a.getBody().getUserData()=="ENDLEVELTRIGGER" && b.getBody().getUserData()=="PLAYER")
		   contact.setEnabled(false);
		
		//--player flyjar
		if(a.getBody().getUserData()=="FLYJAR" && b.getBody().getUserData()=="PLAYER")
			   contact.setEnabled(false);				
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="FLYJAR")
			   contact.setEnabled(false);
		
		//--enemy flyjar
		if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="FLYJAR")
			   contact.setEnabled(false);
		if(a.getBody().getUserData()=="FLYJAR" && b.getBody().getUserData()=="ENEMY")
			   contact.setEnabled(false);
		
		//--enemy enemy
		if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="ENEMY")
			   contact.setEnabled(false);
		
		//--player tongue
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
		
		//--
		if(contact.getFixtureA().getBody().getUserData()!= null && contact.getFixtureB().getBody().getUserData()!=null){
			
			/**********************************************************************************************/
			//--leaf player
			if(a.getBody().getUserData()=="PLAYER" && (b.getBody().getUserData()=="STATIONARY_PLATFORM"))
			{
				if(a.getBody().getPosition().y < b.getBody().getPosition().y)
					contact.setEnabled(true);
			}
		    if((a.getBody().getUserData()=="STATIONARY_PLATFORM") && b.getBody().getUserData()=="PLAYER")
		    {
		    	if(a.getBody().getPosition().y < b.getBody().getPosition().y)
					contact.setEnabled(true);
		    }	
		    
		  //--moving platforms
		    if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="WATER_LOG")
			{
				//if player lower than body, turn off collision
				if(a.getBody().getPosition().y < b.getBody().getPosition().y)
				{
					System.out.println("enable contact");
					contact.setEnabled(true);
				}
			}		
		    if((a.getBody().getUserData()=="WATER_LOG") && (b.getBody().getUserData()=="PLAYER"))
		    {    	
		    	if(a.getBody().getPosition().y < b.getBody().getPosition().y)
		    	{
					System.out.println("enable contact");
					contact.setEnabled(true);
				}
		    }
		    
		  //--moving platform foot
		    if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="MOVING_PLATFORM")
			{
				//if player lower than body, turn off collision
				if(a.getBody().getPosition().y < b.getBody().getPosition().y)
				{
					System.out.println("enable contact foot");
					contact.setEnabled(true);
				}
			}		
		    if((a.getBody().getUserData()=="PLAYER") && (b.getBody().getUserData()=="FOOT"))
		    {    	
		    	if(a.getBody().getPosition().y < b.getBody().getPosition().y)
		    	{
					System.out.println("enable contact foot");
					contact.setEnabled(true);
				}
		    }
		    /**********************************************************************************************/
		    
//		 if(a.getBody().getUserData()=="FOOT" && b.getBody().getUserData()=="GROUND")
//				contact.setEnabled(false);
//		 if(a.getBody().getUserData()=="GROUND" && b.getBody().getUserData()=="FOOT")
//			    contact.setEnabled(false);
//		 
//		 if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="STATIONARY_PLATFORM")
//				contact.setEnabled(true);
//		 if(a.getBody().getUserData()=="STATIONARY_PLATFORM" && b.getBody().getUserData()=="PLAYER")
//			    contact.setEnabled(true);
		
		if(a.getBody().getUserData()=="PLAYER" && b.getBody().getUserData()=="ENEMY")
			contact.setEnabled(false);
	    if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="PLAYER")
		    contact.setEnabled(false);
	    
	    
	    if(a.getBody().getUserData()=="SPIT" && b.getBody().getUserData()=="ENEMY")
			contact.setEnabled(false);
	    if(a.getBody().getUserData()=="ENEMY" && b.getBody().getUserData()=="SPIT")
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
	    if(a.getBody().getUserData()=="TONGUE" && b.getBody().getUserData()=="OBSTACLE")
			contact.setEnabled(false);
	    if(a.getBody().getUserData()=="OBSTACLE" && b.getBody().getUserData()=="TONGUE")
		    contact.setEnabled(false);
	    if(a.getBody().getUserData()=="SHOUT" && b.getBody().getUserData()=="OBSTACLE")
			contact.setEnabled(false);
	    if(a.getBody().getUserData()=="OBSTACLE" && b.getBody().getUserData()=="SHOUT")
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
