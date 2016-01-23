import javax.swing.JFrame;


class TestSprite{

	
	public static void main(String[] args) throws InterruptedException {

		JFrame frame = new JFrame("test that sprite");

		Sprite sprite = null;
		

		//sprites are created in try catch blocks
		try{

			sprite = new Sprite("man", "data/walksequence.png", 5, 6);

			
		}catch(Exception e){

			System.out.println(e.toString());
		}

		//adding states, this is just an example the sprite is going no where
		sprite.addState("LEFT", 0, 7, sprite.getHeight(), sprite.getWidth(), 0,0,0,0);
		sprite.addState("DOWN", 8, 15, sprite.getHeight(), sprite.getWidth(), 0,0,0,0);
		sprite.addState("UP", 16, 23, sprite.getHeight(), sprite.getWidth(), 0,0,0,0);

		//activate a state to start with
		sprite.activateState("LEFT");




		frame.add(sprite);
		frame.setSize(300, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
	
		while (true) {

			//sprite.moveSprite();   <- remove comment to watch sprite run off
			sprite.repaint();
			Thread.sleep(50);

			if(sprite.test == 1 ){

				sprite.activateState("LEFT");
			}
			if(sprite.test == 2 ){

				sprite.activateState("UP");
			}
			if(sprite.test == 3 ){

				sprite.activateState("DOWN");
			}
		}
	}

	

}
