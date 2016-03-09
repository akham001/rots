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
public class Start extends Sprite{
    
    //array of button objects for menu
    ArrayList<Sprite> buttons;
    
    //create player sprite here, throws an exception as base class also does
    public Start() throws Exception{
        
        //sprites are created in try catch blocks
        try{
            
            //first thing to happen base class must be initialised
            buttons.add(new Sprite("start", "data/start.png", 1, 2));
            buttons.add(new Sprite("load", "data/load.png", 1, 2));
            buttons.add(new Sprite("instructions", "data/instructions.png", 1, 2));
            buttons.add(new Sprite("options", "data/options.png", 1, 2));
            buttons.add(new Sprite("highscore", "data/highscore.png", 1, 2));
            buttons.add(new Sprite("credit", "data/credit.png", 1, 2));
            buttons.add(new Sprtie("exit", "data/exit.png", 1, 2));
            buttons.add(new Sprite("pointer", "data/pointer.png", 1, 1));
            
        }catch(Exception e){
            
            System.out.println("Error in Player constructor: " + e.toString());
        }
    }
    
    public void drawMenu( Graphics gr2){
    
        for( buttons x : Sprite){
        
            //draws graphics to pased graphics variable, each tile is being drawn here
            gr2.drawImage( x.getFrame(0), x.getPosX(), x.getPosY(), x.getWidth(), x.getHeight()-1, null);
        }
    }
    
    int void getButton(){
        
        for( int x = 0; x < buttons.size(); x++){
            for( int y = 0; y < buttons.size(); y++){
                
                if( buttons.get(x).circularCollision( y, 10)){
                
                    if( buttons(x).getName().equals("ponter") && buttons.get(y).getName().equals("start")){
                    
                        return 1;
                    }
                }
            }
        }
    }

}