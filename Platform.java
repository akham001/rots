//THe player class acts as a wrapper for sprite class methods
public class Platform extends Sprite{


	public Platform( int _x, int _y) throws Exception{

		//first thing to happen base class must be initialised
		super("platform1", "data/platform1.png", 1, 1);

		//sprites are created in try catch blocks
		try{

			setXY( _x, _y);	
			
			}catch(Exception e){

				System.out.println("Error in " + getName() + " constructor: " + e.toString());
		}
	}


}