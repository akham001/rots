// example Player class inherits from sprite, the image is loaded into super but manipulated 
// in player class
//
//
//							REFERENCES 
//
//https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html 
//http://stackoverflow.com/questions/3505140/calling-a-base-class-constructor-from-derived-class-in-java
//http://stackoverflow.com/questions/23932442/how-to-handle-an-exception-thrown-by-superclass-constructor-in-java

//THe player class acts as a wrapper for sprite class methods
public class Player extends Sprite{

	//int health, ammo, etc;

	
	//create player sprite here, throws an exception as base class also does
	public Player() throws Exception{

		//first thing to happen base class must be initialised
		super("man", "data/states.png", 4, 12);

		//sprites are created in try catch blocks
		try{

				//initialise for gravity conditions
				setGravityMode( true);
				setGravityAngle( 90);
				setGravity( 5);

				//when thrust is applied the direction will be directly up
				setThrustAngle( 270);

				//initialise for what animations to play while in which directions
				addAngleCondition( -1, 10, 24, 33);
				addAngleCondition( 170, 190, 12, 23);

				//hes a bit slow
				setVelocity( 4);
				setmaxVelocity( 20);

			}catch(Exception e){

				System.out.println("Error in Player constructor: " + e.toString());
		}
	}


}