import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.MouseInfo;
import java.awt.event.KeyListener;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/*

	This class will contain the main game loop and is required to draw each sprites image onto one buffer and 
	call it with the draw() member function, this method is called double buffering and it is needed to avoid 
	constantly calling draw functions per-sprite.

	The Class also accomodates mouse and keyboard input with innerclasses and functions
	that return the last key pressed or being held down

	I found this tutorial useful http://content.gpwiki.org/index.php/Java:Tutorials:Double_Buffering
	and http://www.java-forums.org/new-java/51185-copied-code-tutorial-bufferstrategy.html
	and this idiot https://www.youtube.com/watch?v=dwu4DAoac-I

*/

public class Game extends Canvas implements Runnable{

	//screen size variables initialised in constructor
	private int WIDTH, HEIGHT, mouseX, mouseY;

	//thses two objects are the key lelements of the clipping blitting method
	private BufferStrategy bs;
	private Graphics graphics;

	//a menu class object 
	Menu options;

	//flag to end menu options
	boolean menu;

	//this stores the last up down left or right arrow to be pressed as lUP, lDown, lRight, lLeft, or if the arrows are activley being pressed it stores UP DOWN LEFT or RIGHT
	//starts with none as default
	private String direction = "none";

	//some operations on the program are detected here, escape will set menu mode to true and pause the program
	private String operation = "none";

	//this stores the last key pressed default is dash
	private char lastPressed = '-';

	//this stores a typed keycode, it is returned with get typed, default is zero
	private int typed = 0;

	//static global Player object initialised in constructor
	public Player p1;

	//stores a single sprite to be used as a platform, level class is presently in temporary state
	public Platform plat1, plat2;

	//stores a single baddy, this is for demo
	public Baddy crab;

	//this will store the background, an image loading class will be used in the future, for now, the image
	//is initialised in the constructor
	public Image background;

	//constructor for game class
	public Game( int _W, int _H){

		//adding key and mouse listener class to this window
		this.addMouseListener( new mouselisten());
		this.addMouseMotionListener( new mouseMotion());
		this.addKeyListener( new keyListen());
		this.setFocusable( true);

		//allows the buffer and window to be sized to args
		Dimension size = new Dimension( WIDTH = _W, HEIGHT = _H);
		setPreferredSize(size);

		//setting menu flag to default true, this starts draw in menu mode
		menu = true;

		try{

			//new instances of sprite must be constructed in try catch blocks as the y throw exceptions
			options = new Menu( WIDTH, HEIGHT);
			p1 = new Player();
			crab = new Baddy();

			//so crab doesnt fall too hard on the first platform at this stage
			crab.setXY( WIDTH/20, HEIGHT/20);

			//starting position for player
			p1.setXY( WIDTH - p1.getWidth(), HEIGHT - ( HEIGHT/2 + p1.getHeight()));
            
			//starting angle for player
			p1.setAngle(0);

			//platform must take in relative dimensions, x position is from one tenth from the left, height is starting at the bootom of the screen minus
			//the height of the platform plus a little bit to show the base of the platform, width is width of screen minus two tenths and height can stay the same
			plat1 = new Platform( 0 , HEIGHT - HEIGHT/20);

			plat2 = new Platform( WIDTH/20 , HEIGHT/2);

			plat1.setWH( WIDTH , HEIGHT/20);

			plat2.setWH( WIDTH/3, HEIGHT/20);

			//safley initialise background image here
			background = ImageIO.read(new File("data/background.png"));

		}catch(Exception e){

			System.out.println("Game failed to initialise correctly.");
		}
	}


	//this function draws each sprite to a buffer and then flips it
	private void drawGame(){

		//manages multiple drawing to buffer
	    bs = getBufferStrategy();
 
 		//try catch block 
		try {

			//if buffer is not initialised ccreates new buffer to draw over
			if(bs == null){

				//two layers to buffer
	    		createBufferStrategy(2);

	    		//returns buffer to canvas for draw
	    		return;
	   		}

	   		//initialises graphics with drawable objects from this class
	 		graphics = bs.getDrawGraphics();

        	//////////////////////////////////////////////////////////////////////////
        	//																		//
        	//		GAME FUNCTIONS AND UPDATES HERE									//
        	//																		//
        	//////////////////////////////////////////////////////////////////////////


        	//background image
	 		graphics.drawImage( background, 0, 0, WIDTH, HEIGHT, null);
        	
        	if( getDirection() == "LEFT"){

        		p1.setAngle(180);
        		p1.setVelocity(5);
        		graphics.drawImage( p1.nextFrame(), p1.getPosX(), p1.getPosY(), null);
        	}else if( getDirection() == "RIGHT"){
        			
        		p1.setAngle(0);
        		p1.setVelocity(5);
        		graphics.drawImage( p1.nextFrame(), p1.getPosX(), p1.getPosY(), null);
        	}else{

        		graphics.drawImage( p1.getFrame(0), p1.getPosX(), p1.getPosY(), null);
        		p1.setVelocity(0);
        	}

        	p1.pollConditions("ANGLE");
        	p1.moveSprite();
            p1.outOfBoundsCheck();
            p1.positionAdjust(plat1);

        	//sets menu to true, not calling this version of events stores them all exactly as they are, this could allow easy saving and loading mechanism
        	//in menu
        	if( operation == "ESCAPE"){

        		menu = true;
        	}else if( operation == "JUMP" && p1.getCollision() && !p1.checkCollisionBelow( plat2)){

        		p1.setThrustAcceleration( 47);

        		//reset operation to none
        		operation = "NONE";
        	}

        	graphics.drawImage( crab.getFrame(0), crab.getPosX(), crab.getPosY(), crab.getWidth(), crab.getHeight(), null);
          //  crab.outOfBoundsCheck();
        	graphics.drawImage( plat1.getFrame(0), plat1.getPosX(), plat1.getPosY(), plat1.getWidth(), plat1.getHeight(), null);
        	graphics.drawImage( plat2.getFrame(0), plat2.getPosX(), plat2.getPosY(), plat2.getWidth(), plat2.getHeight(), null);
            p1.drawEmitter("Gun", graphics);
        	

        	//must set colliding to true if player is colliding with any other sprite
  			if(p1.checkCollision( plat1) || p1.checkCollision( plat2)){

  				p1.setCollision( true);
  			}

  			if(p1.checkCollisionLeft( plat2)){

  				System.out.println(" DANGLEH LEFT");
  			}

  			if(p1.checkCollisionRight( plat2)){

  				System.out.println(" DANGLEH RIGHT");
  			}

  			if(p1.checkCollisionAbove( plat2)){

  				System.out.println(" DANGLEH UP");
  			}

  			if(p1.checkCollisionBelow( plat2)){

  				System.out.println(" DANGLEH DAHN");
  			}
            
            crab.outOfBoundsCheck();
  			//must set colliding to true if baddy is colliding with any other sprite
  			if(crab.checkCollision( plat1) || crab.checkCollision( plat2)){

  				crab.setCollision( true);
  			}

  			crab.moveSprite(); 
            

            //System.out.println(direction);
	 		////////////////////---------------------> end of drawring space <-----------------------------\\\\\\\\\\\\\\\\\\\\\\\\\\


	 		//clears graphics object once has been drawn to buffer to save memory leak
	 		graphics.dispose();	

	 		//shows image from buffer
	 		bs.show();		
	 			
		}catch(Exception e){

			System.out.println("Error in draw function of Game: " + e.toString());
		}
	 
        //Synchronises drawring on the screen with for smoother graphics bliting, try commenting out to see difference, seems
        //as though frames being drawn evenly in time but not without.
        Toolkit.getDefaultToolkit().sync();	
	}

	//this function draws each sprite to a buffer and then flips it
	private void drawMenu(){

		//manages multiple drawing to buffer
	    bs = getBufferStrategy();
 
 		//try catch block 
		try {

			//if buffer is not initialised ccreates new buffer to draw over
			if(bs == null){

				//two layers to buffer
	    		createBufferStrategy(2);

	    		//returns buffer to canvas for draw
	    		return;
	   		}

	   		//initialises graphics with drawable objects from this class
	 		graphics = bs.getDrawGraphics();

        	//////////////////////////////////////////////////////////////////////////
        	//																		//
        	//		MENU FUNCTIONS AND UPDATES HERE									//
        	//																		//
        	//////////////////////////////////////////////////////////////////////////


        	//background
	 		graphics.setColor(Color.BLACK);
        	graphics.fillRect( 0, 0, WIDTH, HEIGHT);

        	//sets last element of buttons array (an image of a mouse pointer) to be at same position as mouse x and y
        	options.buttons.get(options.buttons.size()-1).setXY( mouseX - options.buttons.get(options.buttons.size()-1).getWidth()/2, mouseY - options.buttons.get(options.buttons.size()-1).getHeight()/2);

        	//draws the menu buttons
        	options.drawMenu( graphics);

	 		//clears graphics object once has been drawn to buffer to save memory leak
	 		graphics.dispose();	

	 		//shows image from buffer
	 		bs.show();		
	 			
		}catch(Exception e){

			System.out.println("Error in draw function of Menu: " + e.toString());
		}
	 
        //Synchronises drawring on the screen with for smoother graphics bliting, try commenting out to see difference, seems
        //as though frames being drawn evenly in time but not without.
        Toolkit.getDefaultToolkit().sync();	
	}



	//run function can run in own thread, draws each frame
	public void run(){
      
      while(true){

      	if(menu){

      		//calls draw function of menu
	      	drawMenu();

	      	//resets operation to none, so that hitting escape can bring up menu again
	      	operation = "NONE";
	    }else{


	    	//calls draw function of game
	      	drawGame();
	    }

      	try{
                
            //temporary delay, must be calculated in relation to CPU speed of users computer
        	Thread.sleep(50);
        }catch(Exception e){ 

        	System.out.println("error in main game thread");
        }
      }
    }

    //returns key code of last key that was typed
	public int getTyped(){

		return typed;
	}

	//returns last arrow key, if it is being pressed it will be UP, if it is not being pressed and the last key to be pressed was up it will return lUP or lRIGHT etc
	public String getDirection(){

		return direction;
	}

	//returns last key press in for of char pressed
	public char getPressed(){

		return lastPressed;
	}


    //Key press and mouse functions
	class mouselisten implements MouseListener {

		public void mouseClicked(MouseEvent e) {
	
			if(options.getButton() == 1){

				menu = false;
			}else if(options.getButton() == 6){

				System.out.println("Program deliberatley exited by user, chill is fine.");

				//potentially unclean exit but exit all the same, if threads are present must kill threads
				//network connection must also be considered, must kill those threads too
				System.exit(0);
			}
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mousePressed(MouseEvent e) {


		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
            p1.fireGun(p1.getAngle());
		}

	}

	class mouseMotion implements MouseMotionListener {

		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub

			mouseX = arg0.getX();
            
            mouseY = arg0.getY();

			//System.out.println(mouseX + "  " + mouseY);
		}

	}


    //used oracle documentation for this keypress block -> https://docs.oracle.com/javase/tutorial/uiswing/events/keylistener.html
	public class keyListen extends KeyAdapter {			

		public void keyPressed(KeyEvent e) {
	
			int id = e.getID();

			if(id == KeyEvent.KEY_TYPED){

				lastPressed = e.getKeyChar();

			}else{

				int keyCode = e.getKeyCode();
	    		
	    		switch( keyCode ) { 

	        		case KeyEvent.VK_UP:

	        			direction = "UP";
	           		break;

	        		case KeyEvent.VK_DOWN:
	          
	          			direction = "DOWN";
	            	break;

	        		case KeyEvent.VK_LEFT:
	           
	           			direction = "LEFT";
	            	break;

	        		case KeyEvent.VK_RIGHT :
	            
	            		direction = "RIGHT";
	            	break;

	            	case KeyEvent.VK_SPACE :

	            		operation = "JUMP";
	            	break;

	            	case KeyEvent.VK_ESCAPE :

	            		operation = "ESCAPE";
	            	break;
	     		}
	     	}
		}

		public void keyReleased(KeyEvent e) {

			int keyCode = e.getKeyCode();

			switch( keyCode ) { 

        		case KeyEvent.VK_UP:

        			direction = "lUP";
           		break;

        		case KeyEvent.VK_DOWN:
          
          			direction = "lDOWN";
            	break;

        		case KeyEvent.VK_LEFT:
           
           			direction = "lLEFT";
            	break;

        		case KeyEvent.VK_RIGHT :
            
            		direction = "lRIGHT";
            	break;
     			}
		}

		public void keyTyped(KeyEvent e) {

			typed = e.getKeyCode();
		}
	}
}