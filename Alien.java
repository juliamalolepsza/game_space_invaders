import javax.swing.*;
import java.awt.*;

public class Alien implements Character{
    private boolean alive;
    private Image image;
    private int x,y;
    private final static int width = 40;
    private final static int height = 40;



    public Alien(int x, int y, String path) {
        this.alive = true;
        this.image = new ImageIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        this.x = x;
        this.y = y;
    }


    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public static int getHeight() {
        return height;
    }
    public static int getWidth() {
        return width;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public void kill() {
        alive = false;
    }

    public void draw(Graphics g) {
        if (alive) {
            g.drawImage(image, x, y, null);
        }
    }

}
