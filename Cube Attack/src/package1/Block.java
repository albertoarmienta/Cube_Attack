package package1;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Block {
	
	private ImageIcon imageIcon;
	private Image image;
	public String color;
	
	public Block(String color) {
		this.color = color;
		switch (color) {
			case "RED":
				imageIcon = new ImageIcon("src/resources/blockRD.png");
				break;
			case "BLUE":
				imageIcon = new ImageIcon("src/resources/block.png");
				break;
			case "WHITE":
				imageIcon = new ImageIcon("src/resources/blockGR.png");
				break;
			case "BLACK":
				imageIcon = new ImageIcon("src/resources/blockYL.png");
				break;
			case "EMPTY":
				imageIcon = new ImageIcon("src/resources/empty.png");
				break;
		}
		image = imageIcon.getImage();
		
	}
	
	public Block ()
	{
		int random = (int)(Math.random()*4);
		System.out.println(random);
		switch (random) {
			case 0 :
				imageIcon = new ImageIcon("src/resources/blockRD.png");
				this.color = "RED";
				break;
			case 1:
				imageIcon = new ImageIcon("src/resources/block.png");
				this.color = "BLUE";
				break;
			case 2:
				imageIcon = new ImageIcon("src/resources/blockGR.png");
				this.color  = "WHITE";
				break;
			case 3:
				imageIcon = new ImageIcon("src/resources/blockYL.png");
				this.color = "BLACK";
				break;
		}
		image = imageIcon.getImage();
	}
	
	
	
	public Image getImage() {
		return image;
	}
	
	public String getColor() {
		return color;
	}
}
