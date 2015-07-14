package package1;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Block extends Rectangle {

    private ImageIcon imageIcon;
    private Image image;
    public String color;
    public int movesNeeded = 0;
    public int delayTime = 10;
    public Boolean needsRemoval = false;
    public boolean falling = false;

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
            case "EXCLAM":
                imageIcon = new ImageIcon("src/resources/EXCLAM.png");
                break;
            case "EMPTY":
                imageIcon = new ImageIcon("src/resources/empty.png");
                break;
            case "NONE":
                imageIcon = new ImageIcon("src/resources/EXCLAM.png");
                break;
        }
        image = imageIcon.getImage();

    }

    public Block() {
        int random = (int) (Math.random() * 26);
        if (random < 5) {
            imageIcon = new ImageIcon("src/resources/RED.png");
            this.color = "RED";
        } else if (random < 10) {
            imageIcon = new ImageIcon("src/resources/BLUE.png");
            this.color = "BLUE";
        } else if (random < 15) {
            imageIcon = new ImageIcon("src/resources/WHITE.png");
            this.color = "WHITE";
        } else if (random < 20) {
            imageIcon = new ImageIcon("src/resources/BLACK.png");
            this.color = "BLACK";
        } else if (random < 25) {
            imageIcon = new ImageIcon("src/resources/GREEN.png");
            this.color = "GREEN";
        } else if (random == 25) {
            imageIcon = new ImageIcon("src/resources/EXCLAM.png");
            this.color = "EXCLAM";
        }
        image = imageIcon.getImage();
    }

    public void nextSprite() {
        switch (color) {
            case "RED":
                imageIcon = new ImageIcon("src/resources/RED_2.png");
                break;
            case "BLUE":
                imageIcon = new ImageIcon("src/resources/BLUE_2.png");
                break;
            case "GREEN":
                imageIcon = new ImageIcon("src/resources/GREEN_2.png");
                break;
            case "WHITE":
                imageIcon = new ImageIcon("src/resources/WHITE_2.png");
                break;
            case "BLACK":
                imageIcon = new ImageIcon("src/resources/BLACK_2.png");
                break;
            case "EXCLAM":
                imageIcon = new ImageIcon("src/resources/EXCLAM_2.png");
                break;
        }
        needsRemoval = true;
        image = imageIcon.getImage();
    }

    public Image getImage() {
        return image;
    }

    public String getColor() {
        return color;
    }

}
