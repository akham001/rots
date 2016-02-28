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
import java.awt.event.KeyListener;

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
	private int WIDTH, HEIGHT;

	//thses two objects are the key lelements of the clipping blitting method
	private BufferStrategy bs;
	private Graphics graphics;

	//this stores the last up down left or right arrow to be pressed as lUP, lDown, lRight, lLeft, or if the arrows are activley being pressed it stores UP DOWN LEFT or RIGHT
	//starts with none as default
	private String direction = "none";

	//this stores the last key pressed default is dash
	private char lastPressed = '-';

	//this stores a typed keycode, it is returned with get typed, default is zero
	private int typed = 0;

	//static global Player object initialised in constructor
	public Player p1;

	//constructor for game class
	public Game( int _W, int _H){

		//adding key and mouse listener class to this window
		this.addMouseListener(new mouselisten());
		this.addMouseMotionListener(new mouseMotion());
		this.addKeyListener(new keyListen());
		this.setFocusable(true);

		//allows the buffer and window to be sized to args
		Dimension size = new Dimension( WIDTH = _W, HEIGHT = _H);
		setPreferredSize(size);

		try{

			p1 = new Player();
		}catch(Exception e){

			System.out.println("Player failed to initialise");
		}
	}


	//this function draws each sprite to a buffer and then flips it
	private void draw(){

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


        	//background
	 		graphics.setColor(Color.white);
        	graphics.fillRect( 0, 0, WIDTH, HEIGHT);

        	
        	if(getDirection() == "LEFT" ||getDirection() == "RIGHT" || getDirection() == "UP" || getDirection() == "DOWN"){

        		p1.continue_activateState(getDirection());
        		       		
        	}

        	p1.moveSprite();
        	graphics.drawImage( p1.nextFrame(), p1.getPosX(), p1.getPosY(), null);
      	 		


	 		////////////////---------------------> end of drawring space <-----------------------------\\\\\\\\\\\\\\\\\


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



	//run function can run in own thread, draws each frame
	public void run(){
      
      while(true){

      	//calls draw function
      	draw();

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
			// TODO Auto-generated method stub

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

		}

	}

	class mouseMotion implements MouseMotionListener {

		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub

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