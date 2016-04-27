// example Player class inherits from sprite, the image is loaded into super but manipulated
// in player class
//
//
//							REFERENCES
//
//https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html
//http://stackoverflow.com/questions/3505140/calling-a-base-class-constructor-from-derived-class-in-java
//http://stackoverflow.com/questions/23932442/how-to-handle-an-exception-thrown-by-superclass-constructor-in-java
import java.util.BitSet;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.Font;
import java.lang.Math;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.PixelGrabber;
import java.awt.image.RGBImageFilter;
import java.util.Random;
//THe player class acts as a wrapper for sprite class methods
public class Player extends Sprite{

	//int health, ammo, etc;


	//create player sprite here, throws an exception as base class also does
	public Player() throws Exception{

		//first thing to happen base class must be initialised
		super( "man", "data/crosshair.png", 1, 1);
        loadImages("data/jump", "jumping_left", ".png");

		//sprites are created in try catch blocks
		try{

			setXY( 300, 100);
            setWH( 100, 100);
				//initialise for gravity conditions
				setGravityMode( true);
				setGravityAngle( 90);
				setGravity( 5);

				//when thrust is applied the direction will be directly up
				setThrustAngle( 270);

				//initialise for what animations to play while in which directions
				addAngleCondition( -10, 10, 2, 10);
				addAngleCondition( 170, 190, 2, 10);

				//hes a bit slow
				setVelocity( 4);
				setmaxVelocity( 20);

        //emitter
        addEmitter( "Gun", "data/ball.png", 10, 1, 60, 210, 70, false);

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
          //  System.out.println("out of bottom");
        }
        if(topBound()) {
            setThrustAcceleration( 0);
            //System.out.println("out of top");
        }
    }

    public void positionAdjust( Sprite _sprite) {

        if(checkCollision( _sprite)){
            int temp = getPosY();
            setXY( getPosX(), 420);
           // System.out.println("true");
        }
    }

		//updates the angle of the emitter to be facing the same direction as player
		//and activates fire fuction for that emitter
    public void fireGun( double _angle) {

        changeEmitterAngle( "Gun", _angle);
        activateEmitterFountain( "Gun");
    }
    
    public void loadImages(String _path, String _name, String _extention) {
        
        int _FILES = new File(_path).listFiles().length;
        try {
        for(int x=0; x< _FILES; x++) {
            addFrame(ImageIO.read(new File( _path + _name+ _FILES + _extention)));
        }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
