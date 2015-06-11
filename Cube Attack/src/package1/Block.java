package package1;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Block extends Rectangle{
	
	private ImageIcon imageIcon;
	private Image image;
	public String color;
	
	public Block(String color) {
		this.color = color;
		switch (color) {
			case "RED":
				imageIcon = new ImageIcon("src/resources/RED.png");
				break;
			case "BLUE":
				imageIcon = new ImageIcon("src/resources/BLUE.png");
				break;
                        case "GREEN":
                                imageIcon = new ImageIcon("src/resources/GREEN.png");
                                break;
			case "WHITE":
				imageIcon = new ImageIcon("src/resources/WHITE.png");
				break;
			case "BLACK":
				imageIcon = new ImageIcon("src/resources/BLACK.png");
				break;
			case "EMPTY":
				imageIcon = new ImageIcon("src/resources/empty.png");
				break;
		}
		image = imageIcon.getImage();
		
	}
	
	public Block ()
	{
		int random = (int)(Math.random()*5);
		switch (random) {
			case 0 :
				imageIcon = new ImageIcon("src/resources/RED.png");
				this.color = "RED";
				break;
			case 1:
				imageIcon = new ImageIcon("src/resources/BLUE.png");
				this.color = "BLUE";
				break;
			case 2:
				imageIcon = new ImageIcon("src/resources/WHITE.png");
				this.color  = "WHITE";
				break;
			case 3:
				imageIcon = new ImageIcon("src/resources/BLACK.png");
				this.color = "BLACK";
				break;
                        case 4:
                                imageIcon = new ImageIcon("src/resources/GREEN.png");
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