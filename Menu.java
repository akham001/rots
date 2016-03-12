
import java.util.ArrayList;
import java.awt.Graphics;


public class Menu{
    
    //array of button objects for menu
    public ArrayList<Sprite> buttons = new ArrayList<Sprite>();
    
    //create player sprite here, throws an exception as base class also does
    public Menu(String _media_folder) throws Exception{
        
        //sprites are created in try catch blocks
        try{
            
            //first thing to happen base class must be initialised
            buttons.add(new Sprite("play", _media_folder + "/play.png", 1, 2));
            buttons.add(new Sprite("saved", _media_folder + "/saved.png", 1, 2));
            buttons.add(new Sprite("instructions", _media_folder + "/instructions.png", 1, 2));
            buttons.add(new Sprite("options", _media_folder + "/options.png", 1, 2));
            buttons.add(new Sprite("highscore", _media_folder + "/highscore.png", 1, 2));
            buttons.add(new Sprite("credits", _media_folder + "/credits.png", 1, 2));
            buttons.add(new Sprite("exit", _media_folder + "/exit.png", 1, 2));
            buttons.add(new Sprite("pointer", _media_folder + "/pointer.png", 1, 1));

            initMenu();
            
        }catch(Exception e){
            
            System.out.println("Error in Menu constructor: " + e.toString());
        }
    }

    //loops for each button initialising its y position and drawing at same x margin
    private void initMenu(){

    	for( int x = 0; x < buttons.size() ; x++){

            //they are a little big, half the size
            buttons.get(x).setWH( buttons.get(x).getWidth()/2, buttons.get(x).getHeight() / 2);

            //sts the poitions
    		buttons.get(x).setXY( 250, (buttons.get(x).getHeight() + 5) * (x + 1));
    	}
    }
    
    public void drawMenu( Graphics gr2){
    
        for( Sprite x : buttons){
        
            //draws graphics to pased graphics variable, each tile is being drawn here
            gr2.drawImage( x.getFrame(0), x.getPosX(), x.getPosY(), x.getWidth(), x.getHeight()-1, null);
        }
    }

    //highlights buttons if mouse is over
    public void highlightButtons(){
    
        for( int x = 0; x < buttons.size(); x++){
            for( int y = 0; y < buttons.size(); y++){
               
                if( buttons.get(x).getName().equals("pointer")){
                    
                    buttons.get(y).getFrame(0);
                }     
            }
        }
    }
    
    public int getButton(){
        
        for( int x = 0; x < buttons.size(); x++){
            for( int y = 0; y < buttons.size(); y++){
                
                if( buttons.get(x).checkCollision( buttons.get(y))){
      
                    if( buttons.get(x).getName().equals("pointer") && buttons.get(y).getName().equals("play")){
                    
                        System.out.println("mow");
                        return 1;
                    }
                }
            }
        }

        //returns minus one as an error message or null return
        return -1;
    }

}