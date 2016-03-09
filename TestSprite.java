import javax.swing.*;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

class TestSprite extends JFrame{

	Game game, menu;

	public TestSprite(){

		
		//this.setUndecorated(true);
		game = new Game(800, 600);
        menu = new Game(800, 600);
		//this.add(game);
        this.add(menu);
		this.pack();
		this.setSize(800,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		//calls runnable function
		game.run();

	}
	
	public static void main(String[] args) throws InterruptedException {
				
		new TestSprite();
	}
}
