
import java.util.ArrayList;
import java.awt.Graphics;


public class Menu{
    
    //array of button objects for menu
    public ArrayList<Sprite> buttons = new ArrayList<Sprite>();
    
    //create player sprite here, throws an exception as base class also does
    public Menu() throws Exception{
        
        //sprites are created in try catch blocks
        try{
            
            //first thing to happen base class must be initialised
            buttons.add(new Sprite("start", "data/start.png", 1, 2));
            buttons.add(new Sprite("load", "data/load.png", 1, 2));
            buttons.add(new Sprite("instructions", "data/instructions.png", 1, 2));
            buttons.add(new Sprite("options", "data/options.png", 1, 2));
            buttons.add(new Sprite("highscore", "data/highscore.png", 1, 2));
            buttons.add(new Sprite("credit", "data/credit.png", 1, 2));
            buttons.add(new Sprite("exit", "data/exit.png", 1, 2));
            buttons.add(new Sprite("pointer", "data/pointer.png", 1, 1));

            initMenu();
            
        }catch(Exception e){
            
            System.out.println("Error in Player constructor: " + e.toString());
        }
    }

    //loops for each button initialising its y position and drawing at same x margin
    private void initMenu(){

    	for( int x = 0; x < buttons.size(); x++){

    		buttons.get(x).setXY( 100, 60 * x);
    	}
    }
    
    public void drawMenu( Graphics gr2){
    
        for( Sprite x : buttons){
        
            //draws graphics to pased graphics variable, each tile is being drawn here
            gr2.drawImage( x.getFrame(0), x.getPosX(), x.getPosY(), x.getWidth(), x.getHeight()-1, null);
        }
    }
    
    public int getButton(){
        
        for( int x = 0; x < buttons.size(); x++){
            for( int y = 0; y < buttons.size(); y++){
                
                if( buttons.get(x).checkCollision( buttons.get(y))){
                
                    if( buttons.get(x).getName().equals("pointer") && buttons.get(y).getName().equals("start")){
                    
                        return 1;
                    }
                }
            }
        }

        //returns minus one as an error message or null return
        return -1;
    }

}