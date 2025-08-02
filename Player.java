import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Player implements Character{
    private String nick;
    private int score;
    private int lives;
    private boolean killable;
    private Image image;
    private int x,y;
    private final static List<Player> allPlayers = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    private int width;
    private int height;

    public Player(String nick, boolean killable, String path) {
        this.nick = nick;
        this.lives = 3;
        this.score = 0;
        this.killable = killable;
        this.image = new ImageIcon(path).getImage();
        this.x = 250;
        this.y = 550;
        this.width = 60;
        this.height = 40;
        allPlayers.add(this);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setLives(int lives){
        this.lives = lives;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isKillable() {
        return killable;
    }

    public void setKillable(boolean killable) {
        this.killable = killable;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public int getLives() {
        return lives;
    }

    public void moveLeft() {
        if(this.x >= 5)
            x -= 5;
    }
    public void moveRight() {
        if(this.x <= 595)
            x += 5;
    }

    public void shoot() {
        bullets.add(new Bullet(x+30, y, true));
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }

    @Override
    public void kill() {
        if(killable)
            lives = 0;
    }
}
