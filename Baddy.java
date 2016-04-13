//just a simple test Baddy
public class Baddy extends Sprite{

	//int health, ammo, etc;

	
	//create player sprite here, throws an exception as base class also does
	public Baddy() throws Exception{

		//first thing to happen base class must be initialised
		super("crab", "data/crab.png", 1, 1);

		//sprites are created in try catch blocks
		try{

				//initialise for gravity conditions
				setGravityMode( true);
				setGravityAngle( 90);
				setGravity( 8);
				
				addState("RIGHT", 0, 0, getHeight(), getWidth(), 5, 2, 3, 3);
                addState("LEFT", 0, 0, getHeight(), getWidth(), 5, 2, 3, 180);

				//activate a state to start with
				activateState("RIGHT");
            
                //Set baddy bounds
            setAllBounds(40, 220, 600, 0);
							
			}catch(Exception e){

				System.out.println("Error in Baddy constructor: " + e.toString());
		}
	}
    
    public void outOfBoundsCheck() {
        if(rightBound()) {
            activateState("LEFT");
        }
        if(leftBound()) {
            activateState("RIGHT");
        }
        if(bottomBound()) {
            setXY(getPosX(), 0);
        }
        if(topBound()) {
            setThrustAcceleration(0);
        }
    }

}