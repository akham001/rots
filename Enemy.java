//just a simple test Baddy
public class Enemy extends Sprite{

	//int health, ammo, etc;
	int health;

	//if baddy is dead or not
	boolean isDead = false;


	//create player sprite here, throws an exception as base class also does
	public Enemy() throws Exception{

		//first thing to happen base class must be initialised
		super("crab", "data/sheet2.png", 4, 17);

		//sprites are created in try catch blocks
		try{

				//baddies health
				health = 100;

				//initialise for gravity conditions
				setGravityMode( true);
				setGravityAngle( 90);
				setGravity( 8);

				addState("RIGHT", 18, 25, getHeight(), getWidth(), 5, 2, 3, 3);
        addState("LEFT", 25, 31, getHeight(), getWidth(), 5, 2, 3, 180);

				//activate a state to start with
				activateState("RIGHT");

        //Set baddy bounds
        setAllBounds(40, 220, 600, 0);

				//add a blood fountain for when its hit
				addEmitter( "bloodFountain", "data/blood.png", 20, 20, 80, 270, 20, true);

				setThrustAngle( 260);
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

		//if the baddy is hit take some damage and draw a blood spurt, if the baddy is killed set dead to true
		public void hit(){

			activateEmitterFountain( "bloodFountain");
			setThrustAcceleration( 120);
			health -= 110;

			if( health < 0){

				isDead = true;
			}
		}

}
