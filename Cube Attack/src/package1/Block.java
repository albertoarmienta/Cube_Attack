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
    private String color;

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

    public Image getImage() {
        return image;
    }

    public String getColor() {
        return color;
    }
}
