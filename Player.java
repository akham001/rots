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

				//adding states, this is just an example the sprite is going no where
				addState("DOWN", 0, 12, getHeight(), getWidth(), 5,2, 1, 90);
				addState("LEFT", 13, 24, getHeight(), getWidth(), 5,2, 1, 180);
				addState("RIGHT", 25, 36, getHeight(), getWidth(), 5,2, 1, 0);
				addState("UP", 37, 48, getHeight(), getWidth(), 5, 2, 1,270);

				//activate a state to start with
				activateState("DOWN");

				
			}catch(Exception e){

				System.out.println("Error in Player constructor: " + e.toString());
		}
	}


}