import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel{
    private int aliensCount;
    private int alienRows;
    private String selectedShipPath;
    private boolean killable;
    private int speed;
    private Player player;
    private Timer timer;
    JLabel timeLabel;
    JLabel scoreLabel;
    JLabel livesLabel;
    private final GameFrame parent;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean aliensMovingRight = true;
    private long lastDropTime; //2 zmienne do kontroli czasu spadanie alienow
    private final int dropIntervalMs;
    private long gameStartMs; //zmienna do kontroli czasu gry
    private int secondsNow;
    private java.util.List<Alien> aliens = new ArrayList<>();
    private java.util.List<Bullet> aliensBullets = new ArrayList<>();



    public GamePanel(GameFrame parent, String nick, int aliensCount,int alienRows, String selectedShipPath, boolean killable, int speed) {
        this.aliensCount = aliensCount;
        this.alienRows = alienRows;
        this.selectedShipPath = selectedShipPath;
        this.killable = killable;
        this.speed = speed;
        this.parent = parent;
        lastDropTime  = System.currentTimeMillis();
        dropIntervalMs = (6 - speed) * 1000;   //speed 1→ spadanie co 5000 ms, speed 5→ spadanie co 1000 ms
        gameStartMs = System.currentTimeMillis();

        setLayout(null);
        setBackground(Color.BLACK);
        setFocusable(true);

//tworzymy obiekty
        player = new Player(nick, killable, selectedShipPath);
        for (int row = 0; row < alienRows; row++) {
            for (int i = 0; i < aliensCount; i++) {
                int x = 50 + i * (Alien.getWidth() + 10);
                int y = 50 + row * (Alien.getHeight() + 10);
                aliens.add(new Alien(x, y, "greenAlien.png"));
            }
        }

//guziki na dole
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        controlPanel.setBounds(0, parent.getHeight() - 100, parent.getWidth(), 60);
        controlPanel.setBackground(Color.BLACK);

        JButton leftButton = new JButton("<-");
        JButton shootButton = new JButton("shoot");
        JButton rightButton = new JButton("->");
        leftButton.setFocusable(false); //by mozna bylo na zmiane sterowac guzikami i klawiszami (po klikneieciu guzika fokus przechodzil na button z keylistener)
        shootButton.setFocusable(false);
        rightButton.setFocusable(false);

        controlPanel.add(leftButton);
        controlPanel.add(shootButton);
        controlPanel.add(rightButton);
        this.add(controlPanel);

//guziki na gorze
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBounds(0, 0, parent.getWidth(), 40);
        topPanel.setBackground(Color.BLACK);

        timeLabel = new JLabel("Time: 0s");
        timeLabel.setForeground(Color.WHITE);
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        livesLabel = new JLabel(player.isKillable() ? "Lives: 3" : "Lives: immortal");
        livesLabel.setForeground(Color.WHITE);
        JButton pauseButton = new JButton("Pause");

        pauseButton.setFocusable(false);
        pauseButton.addActionListener(e -> {
            if (timer.isRunning()) {
                timer.stop();
                pauseButton.setText("Resume");
            } else {
                timer.start();
                pauseButton.setText("Pause");
            }
        });

        topPanel.add(timeLabel);
        topPanel.add(Box.createHorizontalStrut(50)); //odstep miedzy elementami w toppanel
        topPanel.add(scoreLabel);
        topPanel.add(Box.createHorizontalStrut(50));
        topPanel.add(livesLabel);
        topPanel.add(Box.createHorizontalStrut(50));
        topPanel.add(pauseButton);

        this.add(topPanel);


//poruszanie sie

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> movingLeft = true;
                    case KeyEvent.VK_RIGHT -> movingRight = true;
                    case KeyEvent.VK_SPACE -> player.shoot();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> movingLeft = false;
                    case KeyEvent.VK_RIGHT -> movingRight = false;
                }
            }
        });

        leftButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                movingLeft = true;
            }
            public void mouseReleased(MouseEvent e) {
                movingLeft = false;
            }
        });
        rightButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                movingRight = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                movingRight = false;
            }
        });

//strzelanie
        shootButton.addActionListener(e -> player.shoot());

//timer
        timer = new Timer(30, e -> {  //1 tik na 30 milisekund
            updateLogic();
            repaint();
        });
        timer.start();

    }
//======================================================================================================

    public void updateLogic(){
        if (movingLeft)
            player.moveLeft();
        if (movingRight)
            player.moveRight();
        moveAliens();
        movePlayerBullet();  //utworzony w player pocisk przesuwa sie
        moveAlienBullets(); //dba o to by pociski alienow stale sie przesuwaly a nie tylko tworzyly
        maybeShootAlienBullet();
        updateTimer();
        checkCollisions();
        checkEndConditions();
    }

    private void moveAliens() {
        boolean hitWall = false;
        for (Alien a : aliens) {
            if (aliensMovingRight && a.getX() + Alien.getWidth() >= getWidth()) {
                hitWall = true;
            } else if (!aliensMovingRight && a.getX() <= 0) {
                hitWall = true;
            }
        }
        if (hitWall)
            aliensMovingRight = !aliensMovingRight; //zmiana kierunku

        int dx = (aliensMovingRight ? 1 : -1) * (2 + speed);   //zmiana w poruszaniu sie w poziomie
        int dy = 0;                                            //inicjacja zmiany w pionie

        long now = System.currentTimeMillis();
        if (now - lastDropTime >= dropIntervalMs) {            //zmiana opadanie w zaleznosci od minionych sekund i predkosci
            dy = 10 + 3 * speed;
            lastDropTime = now;
        }
        for (Alien a : aliens) {
            a.setX(a.getX() + dx);
            a.setY(a.getY() + dy);
        }
    }

    private void movePlayerBullet() {
        java.util.List<Bullet> toRemove = new ArrayList<>();
        for (Bullet b : player.getBullets()) {
            b.move();
            if (!b.isAlive())
                toRemove.add(b);  //nie beda juz rysowane
        }
        player.getBullets().removeAll(toRemove);
    }

    public void moveAlienBullets() {
        java.util.List<Bullet> toRemove = new ArrayList<>();
        for (Bullet b : aliensBullets) {
            b.move();
            if (!b.isAlive())
                toRemove.add(b);
        }
        aliensBullets.removeAll(toRemove);
    }

    public void maybeShootAlienBullet(){
        double chance = 0.01 + 0.01 * speed; //losowanie szansy na strzal
        if (Math.random() < chance) {
            shootAlienBullet();
        }
    }

    private void shootAlienBullet() {
        // wybierz dowolnego żywego obcego
        java.util.List<Alien> alive = new ArrayList<>();
        for (Alien a : aliens)
            if (a.isAlive())
                alive.add(a);

        if (alive.isEmpty()) return;

        Alien shooter = alive.get((int)(Math.random() * alive.size()));
        int cx = shooter.getX() + Alien.getWidth() / 2;
        int cy = shooter.getY() + Alien.getHeight();

        aliensBullets.add(new Bullet(cx, cy,false));

    }


    public void updateTimer() {
        long elapsedMs  = System.currentTimeMillis() - gameStartMs;
        secondsNow = (int) (elapsedMs / 1000);
        timeLabel.setText("Time: " + secondsNow + " s");   // nadpisuej sie co 30 ms
    }

    public void checkCollisions() {
        java.util.List<Bullet> playerBulletsToRemove = new ArrayList<>();  //kolizje pociskow gracza z alienami
        for (Bullet b : player.getBullets()) {
            Rectangle br = b.getBounds();
            for (Alien a : aliens) {
                if (a.isAlive() && br.intersects(
                        new Rectangle(a.getX(), a.getY(), Alien.getWidth(), Alien.getHeight()))) {
                    a.kill();
                    playerBulletsToRemove.add(b);
                    player.setScore(player.getScore() + 1);
                }
            }
        }
        player.getBullets().removeAll(playerBulletsToRemove);
        scoreLabel.setText("Score: " + player.getScore());

        java.util.List<Bullet> alienBulletsToRemove = new ArrayList<>();  //kolizje pociskow alienow z graczem

        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        for (Bullet b : aliensBullets) {
            if (b.getBounds().intersects(playerRect)) {
                alienBulletsToRemove.add(b);
                if (player.isKillable()) {
                    player.setLives(player.getLives() - 1);
                    livesLabel.setText("Lives: " + player.getLives());
                }
            }
        }
        aliensBullets.removeAll(alienBulletsToRemove);
    }

    public void checkEndConditions(){
        boolean allAliensDead = aliens.stream().noneMatch(Alien::isAlive);       //stream
        boolean playerDead = player.isKillable() && player.getLives() <= 0;

        if (allAliensDead){
            player.setScore(player.getScore() * (1000 / secondsNow));
            parent.createEndPanel(player.getNick(), player.getScore(),true);
            timer.stop();
        } else if (playerDead){
            player.setScore(player.getScore() * (1000 / secondsNow));
            parent.createEndPanel(player.getNick(), player.getScore(),false);
            timer.stop();
        }
        for (Alien a : aliens){
            if (a.getY() >= player.getY() - player.getHeight()){ //alieny doszly do pozycji gracza - koniec gry
                player.setScore(player.getScore() * (1000 / secondsNow));
                parent.createEndPanel(player.getNick(), player.getScore(), false);
                timer.stop();
            }
        }
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.draw(g);
        for (Alien alien : aliens)
            alien.draw(g);
        for (Bullet b : player.getBullets())
            b.draw(g);
        for (Bullet b : aliensBullets)
            b.draw(g);
    }




}