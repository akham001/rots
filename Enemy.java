//just a simple test Baddy
public class Enemy extends Sprite{

	//int health, ammo, etc;
	int health;

	//if baddy is dead or not
	boolean isDead = false;


	//create player sprite here, throws an exception as base class also does
	public Enemy() throws Exception{

		//first thing to happen base class must be initialised
		super("enemy", "data/sheet2.png", 4, 17);

		//sprites are created in try catch blocks
		try{

				//baddies health
				health = 100;

				//initialise for gravity conditions
				setGravityMode( true);
				setGravityAngle( 90);
				setGravity( 8);

				addState("RIGHT", 25, 31, getHeight(), getWidth(), 5, 2, 3, 3);
                addState("LEFT", 18, 25, getHeight(), getWidth(), 5, 2, 3, 180);

				//activate a state to start with
				activateState("RIGHT");

        		//Set baddy bounds
        		setAllBounds(500, 780, 50, 200);

				//add a blood fountain for when its hit
				addEmitter( "bloodFountain", "data/blood.png", 20, 20, 80, 270, 20, true);
				//emitter
        		addEmitter( "Gun", "data/ball.png", 10, 1, 2000, 210, 30, false);

				setThrustAngle( 270);
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

    public void positionAdjust( Sprite _sprite) {

        if( checkCollision( _sprite)){

					//the bottom platfor is always going to be at 430 in pixels
          setXY( getPosX(), 230);
        }
    }


    	//takes player as an argument so that the enemy can 
    	public void shootPlayer( Sprite _sprite){

    		double _angle = getAngleTo( _sprite);
    		changeEmitterAngle( "Gun", _angle);
        	activateEmitterFountain( "Gun");

    	}

		//if the baddy is hit take some damage and draw a blood spurt, if the baddy is killed set dead to true
		public void hit(){

			activateEmitterFountain( "bloodFountain");
			setThrustAcceleration( 300);
			health -= 110;

			if( health < 0){

				isDead = true;
			}
		}

}
