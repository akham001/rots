/*

	Author: Jethro Holcroft. 
	Start Date: 16/01/2016.

	The purpose of this class is to draw animated sprites that are easy to use in a game type environment, these sprites should be able to function as projectiles, pickups
	animated caracters that are controled by the player with keys or mouse, animations in the background or effects such as explosions, buttons, scrollable text boxes or even as a splash screen.

	This class will contain functions that allow detection of collisions with other objects of Sprite, returns the angle between this Sprite and other objects of type sprite

	Objects of this class will contain an array of images, these will be frames for animation, integers to store height width and positions.

	Each object will be initialised with a name, this is so the object can be assigned a unique ID for various reasons, it will also help identify this object in debug messages.

	Comments will reference changes of state or conditions, a sub class called stateData manages these,
	a state is the frames for an animation to loop between, the position to draw the next iteration, the width and height dimensions if they need to change,

	in this case a state will these details and addState() will contain arguments that will add a new state to sprite 
	if no states are added, then only the default state will be used, the width and height of the object will be default and based on the size of the frames.
	A condition is an angle speed or velocity with data on which frames to loop through in case the sprites condition matches one saved in the array list 'conditions',
	'addAngle/speed/velocityCondition()' are three seperate functions that will add conditions, pollConditions() will do the checking to see if a condition has changed 

	it is recomended that objects of this class will be stored in array lists or singularly in a wrapper class that is likley to be used in the main application loop using an MVC style functionality, however a lone
	object right at the start of a program can be used as a splash screen, a timer class and a start stop boolean will be included to aid extra functionality.

	an additional button and menu class will also be incuded in the sprite class along with a special constructor, this is to take advantage of some of the functionality of the sprite class to add a little
	gui support, therefore default font variables and font loading support will also be included.

	There will be a lot of debug messages and error handling, this is to help make this class easy to use.

	comments will aim to explain variables and functions as though the reader is learning how to use the code, and functions and variables will be written to best describe their purpose and allow as readable code
	as possible, however a lot of Javas paint methods and graphics functionality/overides are best loosley referenced to. 



																							TO-DO:       

	add display size of sprite, create seperate display size from collision size and functions to support changes, possibly even allow easing to show gradual change in size! maybe.

	add a perpixel collision function

	add list of sprite names to ignore collisions with or to simply repel away from

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
import java.awt.*;
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

	//stores conditions of the sprite, calling 'pollState()' on each object in this array list
	//and inputing angle, speed or velocity of in args will return true or false if that condition has been stored, 
	//and this sprites angle, speed or velocity matches
	public ArrayList<stateData> conditions = new ArrayList<stateData>();

	//these store dimensions of the sprite, frame stores the present frameNum the sprite will draw, frameStart and frameEnd are the start and end loops the
	//animation will cycle through, default state has start at 0 and end at the number of frames, deltaX and Y are for 'deltaMove()' this function draws the sprite as normal but with added delta values
	//this can be used for moving grids of sprites in relation to something 
	private int posX, posY, width, height, frameNum, frameStart, frameEnd, deltaX, deltaY;

	//maxVelocity, velocity, angle and acceleration are for smooth movement functions of the sprite, acceleration is to add a spot of easing to the movement functions
	//to create a natural movement maxVelocity is the set speed velocity will build up or down to based on acceleration 
	private float maxVelocity, velocity, acceleration;  

	//angle variable, must be a double for easy conversion to degrees
	private double angle;

	//these are to check if the sprite is colliding with another sprite/colliding has been set to true, if the mouse is over the spite and if the sprite
	//has been selected (mouse over sprite and a mouse click has been detected selected is toggled to true and false)
	private boolean colliding, mouseOver, constantSpeed;

	public boolean selected;

	//this stores the present state of the object, its set to default. name identifies object
	private String state = "default", name;

	//default font is 'serif' 
	private Font font = new Font("serif", Font.PLAIN, 30);

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS; Several different types of constructor for several different types of sprite, the first is a standard animated sprite, the second takes an argument that determines  //
	//	what kind of sprite to produce based on the argument containting 'functionality'																								  //
	//																																													  //
	//	The second two are for plain images one takes a path to an image the other an image passed into it, this is for dynamic initialisation											  //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//This constructor is for a standard animated sprite,
	//constructor requires the image name in the form of a string, a path to load the image from, and the rows and columns the contact sheet is split up into.
	//this constructor can throw an exception if it is not initialised properly.
	public Sprite( String _name, String _path, int _rows, int _cols) throws Exception{

		try{

			//assign name
			name = _name;

			//temporary load image function
			contactSheet = ImageIO.read(new File(_path));

			//init frames array
			initFrames(_rows, _cols);

			//set default state of sprite, if an uneven table this will need to be over written
			//to create an alternative default state that will not draw the blank frames
			addState( "default", 0, ( _rows * _cols) - 1, height, width, 0, 0, 0, 0);

			//activate the default state
			activateState("default");

		}catch(Exception e){

			System.out.println("Initialisation of " +  _name + " failed: " + e.toString());
		}

	}


	public Sprite(String _functionality, String _name, String _path, int _rows, int _cols) throws Exception{

		try{

			//assign name
			name = _name;

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

	//this constructor is for a non animated sprite, just a rectangular image, this constructor 
	//takes a buffered image as an arg
	public Sprite(String _name, BufferedImage _img) throws Exception{

		try{

			//assign name
			name = _name;

			//initialises straight from args
			contactSheet = _img;

			//set default state of sprite
			addState( "default", 0, 0, height, width, 0, 0, 0, 0);

			//init frames array
			initFrames( 1, 1);

		}catch(Exception e){

			System.out.println("Initialisation of " +  _name + " failed: " + e.toString());
		}

	}

	//takes one image from path, no animation
	public Sprite(String _name, String _path) throws Exception{

		try{

			//assign name
			name = _name;

			//temporary load image function
			contactSheet = ImageIO.read(new File(_path));

			//set default state of sprite
			addState( "default", 0, 0, height, width, 0, 0, 0, 0);

			//init frames array
			initFrames( 0, 0);

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
	//this will also overwrite a state with the same name
	public void addState( String _stateName, int _fStart, int _fEnd, int _height, int _width, float _mvel, float _vel, float _acc, double _ang){
	

		//loops through states search for statename allready
		for(int x = 0; x < states.size() -1; x++){

			//if state name allrady exists
			if(states.get(x).dName == _stateName){

				//delete that state and break out the loop
				states.remove(x);

				//message alerts to name and state that is over written
				System.out.println(name + " state " + _stateName + " over written with new perameters!");

				break;
			}
		}

		//add state to 
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

		if(!(getState().equals(_setStateTo))){

			activateState(_setStateTo);
		}
	}

	//this function also activates state almost the same as above however it does not change
	//velocity, maxVelocity, angle or speed, this is to allow for just changing the frames
	//if the conditions change, ideal for moving sprites with keys as keys are held down
	public void semi_activateState(String _setStateTo){

		//if found is never set to true, state was never found and default state is used
		boolean found = false;

		//set temp variables
		float _maxVelocity = getmaxVelocity();
		float _velocity = getVelocity();
		float _acceleration = getAcceleration();
		double _angle = getAngle();

		//call activate state as normal
		activateState(_setStateTo);

		//set member variables with tems as they were
		setmaxVelocity(_maxVelocity);
		setVelocity(_velocity);
		setAcceleration(_acceleration);
		setAngle(_angle);
	}

	//adds angle condition to conditions array 
	public void addAngleCondition(double _startAngle, double _endAngle, int _frameStart, int _frameEnd){

		conditions.add(new stateData(_startAngle, _endAngle, _frameStart, _frameEnd));
	}

	//adds speed condition to the conditions array
	public void addSpeedCondition(int _speedStart, int _speedEnd, int _frameStart, int _frameEnd){

		conditions.add(new stateData(_speedStart, _speedEnd, _frameStart, _frameEnd));
	}

	//adds velocity conditions to the conditions array
	public void addVelocityCondition(boolean isIncreasing, int _velocity, int _frameStart, int _frameEnd){

		conditions.add(new stateData( isIncreasing, _velocity, _frameStart, _frameEnd));
	}

	//loops the conditions array to check if sprite matches any one of the saved conditions, if it does frame start and
	//frame end are updated
	public void pollConditions(String _condition){

		//for each connection
		for(int x = 0; x < conditions.size(); x++){

			//call each condition based on input
			conditions.get(x).checkCondition(_condition);
		}
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

	//overloaded gets frame in args
	public BufferedImage getFrame(int _frame){

		if(_frame < frames.size()){

			return frames.get(_frame);
		}

		System.out.println("Frame not found");
		return frames.get(0);
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

	//adds a new frame to frame array
	public void addFrame(BufferedImage _img){

		frames.add(_img);
	}

	//replaces a frame, first arg is frame second arg is position in array if frame to replace
	public void replaceFrame(BufferedImage _img, int _index){

		frames.set(_index, _img);
	}

	//this function detects a collision with the sprite that is passed in args, if there is a collision 
	//colliding is set to true else false
	boolean checkCollision(Sprite _spr){

		//this is a standard collision detetion algorithm using the sprites dimensions
		if((getPosX() + deltaX) + getWidth() > _spr.getPosX() && (getPosX() + deltaX) < _spr.getPosX() + _spr.getWidth() &&
       (getPosY() + deltaY) + getHeight() > _spr.getPosY() && (getPosY() + deltaY) <  _spr.getPosY() + _spr.getHeight()){

			colliding = false;
			return true;
		}else{

			colliding = true;
			return false;
		}
	}

	//this function detects a circular collision with the centre of the sprite that is passed in args, the second argument is the diameter of
	//the circle to collid with
	boolean circularCollision(Sprite _spr, int _radius){

		int squarex = deltaX + getPosX() + (getWidth()/2) - _spr.getPosX() + (getWidth()/2);
		int squarey = deltaY + getPosY() + (getHeight()/2) - _spr.getPosY() + (getHeight()/2);

		//this is a standard circular collision detetion algorithm using the sprites dimensions, the circular collision will collid with the center of
		//this sprite from a circle eminating from the center of the sprite to check
		if(Math.sqrt( (squarex * squarex) + (squarey * squarey)) < _radius){

			colliding = false;
			return true;
		}else{

			colliding = true;
			return false;
		}
	}

	//this function will set the angle to be pointing towards the sprite in args
	public void pointTo( Sprite _sprite){

		double angleTo = Math.toDegrees(Math.abs( Math.atan2( getPosX() - _sprite.getPosX(), getPosY() - _sprite.getPosY())));

		//inverse the angle as it is relative to
		setAngle( angleTo);
	}

	//this function moves the sprite one unit towards its present vector each time it is called each time it is called
	public void moveSprite(){

		//set maxVelocity based on acceleration
		accelerate();

		//increment positionX and Y using trig, I always found this link exceptionally usefull http://www.helixsoft.nl/articles/circle/sincos.htm
		//when first learning to use this
		setXY( posX += velocity * Math.cos(Math.toRadians(angle)), posY += velocity * Math.sin(Math.toRadians(angle)));
	}

	//moves the sprite with its presnt position plus a delta value
	public void deltaMove(){

		//adds delta value to existing value
		setXY( posX + deltaX, posY + deltaY);
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

	//will display text at an x and y distance from the top left corner from the sprite
	//function takes a graphics object to draw to, the text to display and the the x and y
	//offsets, remember to call after the sprite has been drawn to not draw the sprite over the text
	public void drawString(Graphics g2, String _text, int _x, int _y){

		g2.drawString( _text, getPosX() + _x, getPosY() + _y);
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
	public double getAngle(){

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

	//returns name
	public String getName(){

		return name;
	}

	//sets name 
	public void setName(String _name){

		name = _name;
	}

	//sets angle 
	public void setAngle(double _angle){

		angle = _angle;
	}

	//set width
	public void setWidth(int _width){

		width = _width;
	}

	//set height
	public void setHeight(int _height){

		height = _height;
	}

	//this re-sets the X and y positions
	public void setWH( int _w, int _h ){

		width = _w;
		height = _h;
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

	public void setDelta( int _dx, int _dy){

		deltaX = _dx;
		deltaY = _dy;
	}

	/**********************************************************************************************************************\\
						stateSata makes two sprite managment strategies possible, States and Conditions.

		CONDITIONS: if a sprite has reached a specific speed, angle or an increasing or decreasing velocity, the sprites
		animation states are made to change to ones that fit, this is done by adding a condition to the conditions array
		and having pollCOnditions in sprite check to see if the conditions of the sprite match any of the data, if they do the starting 
		and ending frames in frame loop are changed, ie;

		function in sprite class is called on sprite object like this: my_sprite.addCondition( 0.0, 180.0, 10, 15),
		this function  adds a 'stateData' object to the 'conditions' array list, 'pollConditions()' will poll this array if it is not empty
		to check if condition of the sprite would return true, if true is returned from the object then the object also owns the
		correct frames to loop between

		STATES: If a state is added with add state, then when 'activateState()' is called, the name of the state is found and the
		conditions of the state are updated from the same object, ie;

		 'goLeft' state is added to the state array with 'addState()', its arguments in constructor were to animate between frames 10 and 15
		 ( a sequence of images creating a leftwards walking stickman) and the angle was 180 degrees with a speed of 2 a total speed of ten and
		a velocity of 2, when my_sprite.activate('goLEFT') is called 'moveSprite()' will move the sprite in units of vector passed in args
		with increasing velocity till max velocity is reached and 'nextFrame()' will return the next frame in a loop between 10 and 15

	\*************************************************************************************************************************/

	//this class manages the data for each state, objects of this class are to be saved in the stateArray
	protected class stateData{

		//variables to store state data 
		String dName;
		int dStart, dEnd, dHeight, dWidth, speedStart, speedEnd;
		float dmaxVelocity, dvelocity, dacceleration;
		double dangle, angleStart, angleEnd;

		//initialise variables in constructor for automatic states, first arg is state name, second and third are animation frames to loop between, next is the height,
		//width, max velocity, velocity, acceleration speed and angle
		protected stateData(String _dname, int _dstart, int _dend, int _dheight, int _dwidth, float _dmvel, float _dvel, float _dacc, double _dangle){

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

		//initialise variables in constructor for initialising an animation state based on the angle:
		//if the angle is between _angleStart and _angleEnd then loop the animation between _dstart and _dend
		protected stateData(double _aStart, double _aEnd, int _dstart, int _dend){

		

			//assign data from constructor
			dStart = _dstart;
			dEnd = _dend;
			angleStart = _aStart;
			angleEnd = _aEnd;
		}

		//initialise variables in constructor for initialising an animation state based on the speed:
		//if the speed is between _speedStart and _speedEnd then loop the animation between _dstart and _dend
		public stateData(int _speedStart, int _speedEnd, int _dstart, int _dend){

		
			//assign data from constructor
			dStart = _dstart;
			dEnd = _dend;
			speedStart = _speedStart;
			speedEnd = _speedEnd;
		}

		//initialise variables in constructor for initialising an animation state based on the velocity:
		//if first arg is true then the state will have changed if the sprites velocity is
		//greater than that in the second argument, if it is false the change will occue when it is less than
		//if more than one of these are set then there are conflicts, this can be resolved by calling pollState strategicaly
		public stateData(boolean _ifIncreasing, int _dvelocity, int _dstart, int _dend){


			//assign data from constructor
			dStart = _dstart;
			dEnd = _dend;
			dvelocity = _dvelocity;
		}


		//if called on object returns true if the conditions are met to return variables associated with the state
		//argument is for checking against saved conditions relating to which state condition is activated
		//takes a double as args to satisfy angle, speed and velocity can convert into integer
		public void checkCondition(String _name){

			switch(_name){

				case "ANGLE":

					double _condition = getAngle();

					if( (_condition > angleStart && _condition < angleEnd) ){

						frameStart = dStart;
						frameEnd = dEnd;
					}
					
					break;

				case "SPEED":

					 _condition = getVelocity();

					if( _condition > speedStart && _condition < speedEnd){

						frameStart = dStart;
						frameEnd = dEnd;
					}
						break;

				case "INCREASING":

					_condition = getAcceleration();

					if( _condition > dvelocity){

						frameStart = dStart;
						frameEnd = dEnd;
					}
						break;
				case "DECREASING":

					 _condition = getAcceleration();

					if( _condition < dvelocity){

						frameStart = dStart;
						frameEnd = dEnd;
					}
						break;

				default:

					//no state conditions have been met
					break;
			}
		}

	}//end of stateData class
}//end of sprite class