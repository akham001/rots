//just a simple test Baddy
public class Baddy extends Sprite{

	//int health, ammo, etc;

	
	//create player sprite here, throws an exception as base class also does
	public Baddy() throws Exception{

		//first thing to happen base class must be initialised
		super("crab", "data/crab.png", 1, 1);

		//sprites are created in try catch blocks
		try{

				setGravityMode( true);
				
				addState("RIGHT", 0, 0, getHeight(), getWidth(), 5, 2, 3, 3);
	
				//activate a state to start with
				activateState("RIGHT");
							
			}catch(Exception e){

				System.out.println("Error in Baddy constructor: " + e.toString());
		}
	}


}