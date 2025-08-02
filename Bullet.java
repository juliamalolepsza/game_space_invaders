import java.awt.*;

public class Bullet {
    private int x, y;
    private int dy;
    private static final int WIDTH  = 4;
    private static final int HEIGHT = 10;
    private boolean alive = true;

    public Bullet(int x, int y, boolean isFromPlayer) {
        this.x  = x - WIDTH / 2;
        this.y  = y - HEIGHT;
        this.dy = isFromPlayer ? -10 : 10; //jesli bullet pochodzi z player to kierunek lotu w gore
    }

    public void move() {
        y += dy;
        if (y + HEIGHT < 0 || y > 600)
            alive = false;   // poza górna i dolna krawędź
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }

    public boolean isAlive() { return alive; }
    public void kill()        { alive = false; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}

