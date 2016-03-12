import javax.swing.*;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
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


class TestSprite extends JFrame{

	Game game;

	public TestSprite(){

		
		//this.setUndecorated(true);
		game = new Game(800, 600);
		this.add(game);

		//custom cursor is blank, so own cursor can be used: http://stackoverflow.com/questions/1984071/how-to-hide-cursor-in-a-swing-application
		this.setCursor(this.getToolkit().createCustomCursor(
            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
            "null"));
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
