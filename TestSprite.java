import javax.swing.JFrame;


class TestSprite{

	
	public static void main(String[] args) throws InterruptedException {

		JFrame frame = new JFrame("test that sprite");

		Sprite sprite = null;
		

		//sprites are created in try catch blocks
		try{

			sprite = new Sprite("USER_INPUT" ,"man", "data/states.png", 4, 12);

			
		}catch(Exception e){

			System.out.println(e.toString());
		}

		//adding states, this is just an example the sprite is going no where
		sprite.addState("DOWN", 0, 12, sprite.getHeight(), sprite.getWidth(), 0,0,0,0);
		sprite.addState("LEFT", 13, 24, sprite.getHeight(), sprite.getWidth(), 0,0,0,0);
		sprite.addState("RIGHT", 25, 36, sprite.getHeight(), sprite.getWidth(), 0,0,0,0);
		sprite.addState("UP", 37, 48, sprite.getHeight(), sprite.getWidth(), 0,0,0,0);

		//activate a state to start with
		sprite.activateState("LEFT");



		//add sprite to frame
		frame.add(sprite);
		frame.setSize(300, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		//this simulates the game loop for now
		while (true) {

			//sprite.moveSprite();   <- remove comment to watch sprite wander off
			sprite.repaint();
			Thread.sleep(50);

			switch(sprite.getDirection()){

				case "LEFT":
					sprite.activateState("LEFT");
				break;

				case "RIGHT":
					sprite.activateState("RIGHT");
				break;

				case "UP":
					sprite.activateState("UP");
				break;

				case "DOWN":
					sprite.activateState("DOWN");
				break;
			}

			
		}
	}

	

}
