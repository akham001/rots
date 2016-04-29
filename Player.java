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
		super( "man", "data/sheet2.png", 4, 17);

		//sprites are created in try catch blocks
		try{

		    setXY( 300, 100);

		    //initialise for gravity conditions
		    setGravityMode( true);
		 	  setGravityAngle( 90);
			  setGravity( 3);
        setCollision( true);

			  //when thrust is applied the direction will be directly up
				setThrustAngle( 270);

				//initialise for what animations to play while in which directions
				addAngleCondition( -10, 10, 25, 31);
				addAngleCondition( 170, 190, 18, 24);

				//initialise for jumping animations
				addJumpCondition( 1, 230, 100, 280, 51, 68);
				addJumpCondition( 1, 290, 100, 310, 34, 51);

				//hes a bit slow
				setVelocity( 4);
				setmaxVelocity( 20);

        //emitter
        addEmitter( "Gun", "data/ball.png", 10, 1, 600, 210, 30, false);

        //Set player Bounds to keep him within the screen
        setAllBounds( -20,  734, 900, 0);

			}catch(Exception e){

				System.out.println( "Error in Player constructor: " + e.toString());
		}
	}

    public void outOfBoundsCheck() {
        //System.out.println("x: " +getPosX() + " y: " + getPosY());

        if(rightBound()) {
            setXY( 734,getPosY());
            //System.out.println("out of right");
        }
        if(leftBound()) {

            setXY( -20, getPosY());
        //    System.out.println("our of left");
        }
        if(bottomBound()) {

            setXY( getPosX(), 0);
           System.out.println("out of bottom");
        }
        if(topBound()) {

            setThrustAcceleration( 0);
            //System.out.println("out of top");
        }
    }

    public void positionAdjust( Sprite _sprite) {

        if( checkCollision( _sprite)){

					//the bottom platfor is always going to be at 430 in pixels
          setXY( getPosX(), 430);
        }
    }

		//updates the angle of the emitter to be facing the same direction as player
		//and activates fire fuction for that emitter
    public void fireGun( double _angle) {

        changeEmitterAngle( "Gun", _angle);
        activateEmitterFountain( "Gun");
    }
}
