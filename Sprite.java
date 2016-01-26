/*

	Author: Jethro Holcroft. 
	Start Date: 16/01/2016.

	The purpose of this class is to draw animated sprites that are easy to use in a game type environment, these sprites should be able to function as projectiles, pickups
	animated caracters that are controled by the player with keys or mouse, animations in the background or effects such as explosions, buttons, scrollable text boxes or even as a splash screen.

	This class will contain functions that allow detection of collisions with other objects of Sprite, detection of keypresses and mouse actions.

	Objects of this class will contain an array of images, these will be frames for animation, integers to store height width and positions.

	Each object will be initialised with a name, this is so the object can be assigned a unique ID for various reasons, it will also help identify this object in debug messages.

	Comments will reference changes of state, a state is the frames for an animation to loop between, the position to draw the next iteration, the width and height dimensions if they need to change,
	in this case a state will be an enumaration that will store these details and newState() will contain arguments that will add a new state to sprite where as changeState() will contain the argument in the form
	of a string to change that state too, this is so the class can be simple to use and understand by everyone, if no states are added, then only the default state will be used, the width and height of the object will
	be that of each buffered image in the frames array and the posX and posY will be 0 , 0.

	it is recomended that objects of this class will be stored in array lists or singularly in a wrapper class that is likley to be used in the main application loop using an MVC style functionality, however a lone
	object right at the start of a program can be used as a splash screen, a timer class and a start stop boolean will be included to aid extra functionality.

	an additional button and menu class will also be incuded in the sprite class along with a special constructor, this is to take advantage of some of the functionality of the sprite class to add a little
	gui support, therefore default font variables and font loading support will also be included.

	There will be a lot of debug messages and error handling, this is to help make this class easy to use.

	comments will aim to explain variables and functions as though the reader is learning how to use the code, and functions and variables will be written to best describe their purpose and allow as readable code
	as possible, however a lot of Javas paint methods and graphics functionality/overides are best loosley referenced to. 



																							TO-DO:        

	convert velocity and max velocity to integers.

	add display size of sprite, create seperate display size from collision size and functions to support changes, possibly even allow easing to show gradual change in size! maybe.

	add a perpixel collision function

	add list of sprite names to ignore collisions with or to simply repel away from

	add a function to automatically call another state once a set state is finished, have a switch to have this sprite react to end of a state after a function is called or every time

	consider converting images with a set colour to be converted to alpha

	consider having a function that allows a contact sheet to be appended onto the existing one, consider adding more functionality to it and have an add state and animation function

	add key and mouse detection add switch to toggle wether or not to higlight outline of sprite if it is mouse selected

	add subclasses that will use this sprite class to create a menu button array, text buttons, consider having an inventory style menu!

	add particle fountains

	add jump functions relative to speed and movement

	add gravity effects functions also relative to movement, consider having sprite types ie flying, walking

	add dragable functionality to sprite 

	add sound functionality, call in state change, have sound loaded in state data



																Please add Ideas below to consider adding to the TO-DO list:

*/

import java.util.BitSet;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.Font;
import java.lang.Math;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.io.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.PixelGrabber;
import java.awt.image.RGBImageFilter;


public class Sprite{

	//this stores a contact sheet image, this will contain all frames of animation in one image
	private BufferedImage contactSheet;

	//this contains each frame of animation in the form of an array.
	private ArrayList<BufferedImage> frames = new ArrayList<BufferedImage>();

	//stores various states for the sprite
	private ArrayList<stateData> states = new ArrayList<stateData>();

	//these store dimensions of the sprite, frame stores the present frameNum the sprite will draw, frameStart and frameEnd are the start and end loops the
	//animation will cycle through, default state has start at 0 and end at the number of frames
	private int posX, posY, width, height, frameNum, frameStart, frameEnd;

	//maxVelocity, velocity, angle and acceleration are for smooth movement functions of the sprite, acceleration is to add a spot of easing to the movement functions
	//to create a natural movement maxVelocity is the set speed velocity will build up or down to based on acceleration 
	private float maxVelocity, velocity, angle, acceleration;  

	//these are to check if the sprite is colliding with another sprite/colliding has been set to true, if the mouse is over the spite and if the sprite
	//has been selected (mouse over sprite and a mouse click has been detected selected is toggled to true and false)
	private boolean colliding, mouseOver, constantSpeed;

	public boolean selected;

	//this stores the present state of the object, its set to default
	private String state = "default";

	//default font is 'serif' 
	private Font font = new Font("serif", Font.PLAIN, 30);

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS; Several different types of constructor for several different types of sprite, the first is a standard animated sprite, the second takes an argument that determines  //
	//	what kind of sprite to produce, if the argument for '_functionality'is:																											  //
	//																																													  //
	//																																													  //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//This constructor is for a standard animated sprite,
	//constructor requires the image name in the form of a string, a path to load the image from, and the rows and columns the contact sheet is split up into.
	//this constructor can throw an exception if it is not initialised properly.
	public Sprite(String _name, String _path, int _rows, int _cols) throws Exception{

		try{

			//temporary load image function
			contactSheet = ImageIO.read(new File(_path));

			//init frames array
			initFrames(_rows, _cols);

			//set default state of sprite
			addState( "default", 0, ( _rows * _cols) - 1, height, width, 0, 0, 0, 0);

			//activate the default state
			activateState("default");

		}catch(Exception e){

			System.out.println("Initialisation of " +  _name + " failed: " + e.toString());
		}

	}


	public Sprite(String _functionality, String _name, String _path, int _rows, int _cols) throws Exception{

		try{

			//temporary load image function
			contactSheet = ImageIO.read(new File(_path));

			//init frames array
			initFrames(_rows, _cols);

			//set default state of sprite
			addState( "default", 0, ( _rows * _cols) - 1, height, width, 0, 0, 0, 0);

			//activate the default state
			activateState("default");

			

		}catch(Exception e){

			System.out.println("Initialisation of " +  _name + " failed: " + e.toString());
		}

	}

	//this function initialises the array list frames with images cut from the contactSheet and will be called in the constructor, 
	//rows and cols along with the dimensions of contactSheet are used to work out the size of each frame to cut out, this only works if the
	//contactSheet contains images divided evenly into rows and columns and also in the same relative position within it.
	//the function returns a boolean if it was successful or not.
	private void initFrames(int _rows, int _cols){

		//this initialises the width and heigh of each sub image for use cutting up the contact sheet, it does not have to be the width and height
		//of the sprite itself however it will remain so unless any states are added.
		width = contactSheet.getWidth() / _cols;
		height = contactSheet.getHeight() / _rows;

		for(int r = 0; r < _rows; r++ ){
			for(int c = 0; c < _cols; c++){

				//this function uses the available variables to slice up the contact sheet in even parts
				frames.add(contactSheet.getSubimage( c * width, r * height, width, height));
			}
		}		
	}

	//this function adds a state to the sprite, the first state is always the default state, other states are added by the user, if you want 
	//a value to stay the same when you add it use the state in args for example in height use sprite.getHeight() in your code.
	public void addState( String _stateName, int _fStart, int _fEnd, int _height, int _width, float _mvel, float _vel, float _acc, float _ang){

		states.add(new stateData( _stateName, _fStart, _fEnd, _height, _width, _mvel, _vel, _acc, _ang));
	}

	//this function activates the state with the same string passed in args, if it is not found, debug error message is returned
	//and default state is set instead
	public void activateState(String _setStateTo){

		//if found is never set to true, state was never found and default state is used
		boolean found = false;

		//loops for each state in states array
		for(stateData sd: states){

			//if state with same name in args is found then 
			if(sd.dName == _setStateTo){

				//change frames and size of sprite
				frameStart = sd.dStart;
				frameEnd = sd.dEnd;
				width = sd.dWidth;
				height = sd.dHeight;
				maxVelocity = sd.dmaxVelocity;
				velocity = sd.dvelocity;
				acceleration = sd.dacceleration;
				angle = sd.dangle;

				//set to animate frames from new states animation loop
				frameNum = frameStart + 1;

				found = true;
				break;
			}
		}

		//if not found then set to default state
		if(!found){

			frameStart = 0;
			maxVelocity = 0;
			velocity = 0;
			angle = 0;
			acceleration = 0;
			frameEnd = states.get(0).dEnd;
			width = states.get(0).dWidth;
			height = states.get(0).dHeight;
		}
	}

	//as above function however if the state is allready active then the function is not called at all
	public void continue_activateState(String _setStateTo){

		if(getState() != _setStateTo){

			activateState(_setStateTo);
		}
	}

	//this function also activates state almost the same as above however it does not change
	//velocity, maxVelocity, angle or speed, this is to keep fluid movements
	public void semi_activateState(String _setStateTo){

		//if found is never set to true, state was never found and default state is used
		boolean found = false;

		//set temp variables
		float _maxVelocity = getmaxVelocity();
		float _velocity = getVelocity();
		float _acceleration = getAcceleration();
		float _angle = getAngle();

		//call activate state as normal
		activateState(_setStateTo);

		//set member variables with tems as they were
		setmaxVelocity(_maxVelocity);
		setVelocity(_velocity);
		setAcceleration(_acceleration);
		setAngle(_angle);
	}

	//this function returns the next frame in the animation loop
	public BufferedImage nextFrame(){

		//if the frame is not at the end of the loop 
		if(frameNum < frameEnd-1){

			//iterate present frame by one and return it
        	frameNum++;
        	return frames.get(frameNum);
    	}else{

    		//reset frame to first frame in loop and return it
        	frameNum = frameStart;
        	return frames.get(frameNum);
    	}
	}

	//gets present frame
	public BufferedImage getFrame(){

		return frames.get(frameNum);
	}

	//iterates next frame in loop but does not return it
	public void nextFrame_noReturn(){

		//if the frame is not at the end of the loop 
		if(frameNum > 0){

			//iterate present frame by one 
        	frameNum++;
    	}else{

    		//reset frame to first frame in loop 
        	frameNum = frameStart;
    	}
	}

	//this function returns the previous frame in the animation loop
	public BufferedImage previousFrame(){

		//if the frame is not at the end of the loop 
		if(frameNum > 0){

			//iterate present frame by one and return it
        	frameNum--;
        	return frames.get(frameNum);
    	}else{

    		//reset frame to first frame in loop and return it
        	frameNum = frames.size();
        	return frames.get(frameNum);
    	}
	}

	//this function detects a collision with the sprite that is passed in args, if there is a collision 
	//colliding is set to true else false
	private void checkCollision(Sprite _spr){

		//this is a standard collision detetion algorithm using the sprites dimensions
		if(getPosX() + getWidth() > _spr.getPosX() && getPosX() < _spr.getPosX() + _spr.getWidth() &&
       getPosY() + getHeight() > _spr.getPosY() && getPosY() <  _spr.getPosY() + _spr.getHeight()){

			colliding = false;
		}else{

			colliding = true;
		}
	}

	//this function moves the sprite one unit towards its present vector each time it is called each time it is called
	public void moveSprite(){

		//set maxVelocity based on acceleration
		accelerate();

		//increment positionX and Y using trig, I always found this link exceptionally usefull http://www.helixsoft.nl/articles/circle/sincos.htm
		//when first learning to use this
		setXY( posX += velocity * Math.cos(Math.toRadians(angle)), posY += velocity * Math.sin(Math.toRadians(angle)));
	}

	//manages acceleration easing if speed has not been set to constant
	public void accelerate(){

		//is this less overhead than a single if statment with this block nested in?
		if(velocity < maxVelocity && !constantSpeed){

			velocity += acceleration;
		}else if(velocity > maxVelocity && !constantSpeed){

			velocity -= acceleration;
		}
	}

	//////////////////////////////
	//	Getters and Setters		//
	//////////////////////////////

	//the object can just be set to collide for other collision situations
	public void setCollision(boolean _collide){

		colliding = _collide;
	}

	//this functin returns true of the object is colliding and false if it is not
	public boolean getCollision(){

		return colliding;
	}

	//returns width
	public int getWidth(){

		return width;
	}

	//returns height
	public int getHeight(){

		return height;
	}

	//returns position X
	public int getPosX(){

		return posX;
	}

	//returns position Y
	public int getPosY(){

		return posY;
	}

	//returns angle
	public float getAngle(){

		return angle;
	}

	//returns maxVelocity
	public float getmaxVelocity(){

		return maxVelocity;
	}

	//returns maxVelocity
	public float getVelocity(){

		return velocity;
	}

	//returns acceleration
	public float getAcceleration(){

		return acceleration;
	}

	//sets angle 
	public void setAngle(float _angle){

		angle = _angle;
	}

	//sets the present speed for the sprite
	public void setVelocity(float _Velocity){

			velocity = _Velocity;
	}

	//sets the max speed for the sprite
	public void setmaxVelocity(float _maxVelocity){

			maxVelocity = _maxVelocity;
	}

	//sets acceleration, there is a danger of dividing by zero if movement functions are not robustly made
	//please be observant
	public void setAcceleration(float _acceleration){

			acceleration = _acceleration;
	}

	//this function returns the present state of the object
	public String getState(){

		return state;
	}

	//set speed to be constant or eased with velocity
	public void toConstantSpeed(boolean _cspeed){

		constantSpeed = _cspeed;
	} 

	//this re-sets the X and y positions
	public void setXY( int _x, int _y ){

		posX = _x;
		posY = _y;
	}

	//////////////////////////////////////////////////////////////////////////////
	//	Extra classes and auto functions; stateData structure,paint     		//
	//////////////////////////////////////////////////////////////////////////////

	//this class manages the data for each state, objects of this class are to be saved in the stateArray
	public class stateData{

		//variables to store state data 
		String dName;
		int dStart, dEnd, dHeight, dWidth;
		float dmaxVelocity, dvelocity, dacceleration, dangle;

		//initialise variables in constructor
		public stateData(String _dname, int _dstart, int _dend, int _dheight, int _dwidth, float _dmvel, float _dvel, float _dacc, float _dangle){

			dName = _dname;
			dStart = _dstart;
			dEnd = _dend;
			dHeight = _dheight;
			dWidth = _dwidth;
			dmaxVelocity = _dmvel;
			dvelocity = _dvel;
			dacceleration = _dacc;
			dangle = _dangle;
		}
	}
}