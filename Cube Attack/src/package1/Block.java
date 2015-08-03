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
    public boolean justSpawned = true;
    public boolean needsRemoval = false;
    public boolean falling = false;

    public Block(String color) {
        this.color = color;
        switch (color) {
            case "RED":
                imageIcon = new ImageIcon(getClass().getResource("RED.png"));
                break;
            case "BLUE":
                imageIcon = new ImageIcon(getClass().getResource("BLUE.png"));
                break;
            case "GREEN":
                imageIcon = new ImageIcon(getClass().getResource("GREEN.png"));
                break;
            case "WHITE":
                imageIcon = new ImageIcon(getClass().getResource("WHITE.png"));
                break;
            case "BLACK":
                imageIcon = new ImageIcon(getClass().getResource("BLACK.png"));
                break;
            case "EXCLAM":
                imageIcon = new ImageIcon(getClass().getResource("EXCLAM.png"));
                break;
            case "EMPTY":
                imageIcon = new ImageIcon(getClass().getResource("empty.png"));
                break;
            case "NONE":
                imageIcon = new ImageIcon(getClass().getResource("EXCLAM.png"));
                break;
        }
        justSpawned = true;
        image = imageIcon.getImage();

    }

    public Block() {
        int random = (int) (Math.random() * 26);
        if (random < 5) {
            imageIcon = new ImageIcon(getClass().getResource("RED.png"));
            this.color = "RED";
        } else if (random < 10) {
            imageIcon = new ImageIcon(getClass().getResource("BLUE.png"));
            this.color = "BLUE";
        } else if (random < 15) {
            imageIcon = new ImageIcon(getClass().getResource("WHITE.png"));
            this.color = "WHITE";
        } else if (random < 20) {
            imageIcon = new ImageIcon(getClass().getResource("BLACK.png"));
            this.color = "BLACK";
        } else if (random < 25) {
            imageIcon = new ImageIcon(getClass().getResource("GREEN.png"));
            this.color = "GREEN";
        } else if (random == 25) {
            imageIcon = new ImageIcon(getClass().getResource("EXCLAM.png"));
            this.color = "EXCLAM";
        }
        justSpawned = true;
        image = imageIcon.getImage();
    }

    public void nextSprite() {
        switch (color) {
            case "RED":
                imageIcon = new ImageIcon(getClass().getResource("RED_2.png"));
                break;
            case "BLUE":
                imageIcon = new ImageIcon(getClass().getResource("BLUE_2.png"));
                break;
            case "GREEN":
                imageIcon = new ImageIcon(getClass().getResource("GREEN_2.png"));
                break;
            case "WHITE":
                imageIcon = new ImageIcon(getClass().getResource("WHITE_2.png"));
                break;
            case "BLACK":
                imageIcon = new ImageIcon(getClass().getResource("BLACK_2.png"));
                break;
            case "EXCLAM":
                imageIcon = new ImageIcon(getClass().getResource("EXCLAM.png"));
                break;
        }
        this.color = "PURP";
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
