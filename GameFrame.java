import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GameFrame extends JFrame {
    private static final int width = 600;
    private static final int height = 700;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private WelcomePanel welcomePanel;
    private StartPanel startPanel;
    private GamePanel gamePanel;
    private EndPanel endPanel;

    public GameFrame() {
        this.setTitle("Area Intruders");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);
        setResizable(false);

        HighScore.readScoreFromFile(); //zaczynamy od zczytania pliku z wynikami
        createWelcomePanel();
    }


    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public void showPanel(String name) {
        cardLayout.show(mainPanel, name); //na main panelu GameFrame wyswietlamy wybrany JPanel. metoda uzywana przy tworzeniu kolejnych paneli
    }                                 //pokazanie na podstawie String name ze wzgeldu na wymaganie String w metodzie show. celem jest identyfikacja panelu po nazwie



    public void restartGame(){
        WelcomePanel welcomePanel= new WelcomePanel(this);
        mainPanel.add(welcomePanel, "welcomePanel");
        showPanel("welcomePanel");
    }

    public void createWelcomePanel(){
        checkPanelDuplicate("welcomePanel");
        welcomePanel = new WelcomePanel(this); //ten GameFrame jest podawany w konstruktorze kazdego panelu jako parent
        mainPanel.add(welcomePanel, "welcomePanel");
        showPanel("welcomePanel");
    }

    public void createStartPanel(){
        checkPanelDuplicate("startPanel");
        startPanel = new StartPanel(this);
        mainPanel.add(startPanel, "startPanel");
        showPanel("startPanel");
    }

    public void createGamePanel(GameFrame parent, String nick, int aliensCount,int alienRows, String selectedShipPath, boolean killable, int speed){
        checkPanelDuplicate("gamePanel");
        gamePanel = new GamePanel(parent, nick, aliensCount, alienRows, selectedShipPath, killable, speed);
        mainPanel.add(gamePanel, "gamePanel");
        showPanel("gamePanel");
        setJMenuBar(createMenuBar());
        SwingUtilities.invokeLater(gamePanel::requestFocusInWindow); //by przyciski dzialaly, ustawim dopiero gdy gui bedzie widoczne
        //zaczynamy gre
    }

    public void createEndPanel(String nick, int score, boolean won){
        checkPanelDuplicate("endPanel");
        endPanel = new EndPanel(nick, score, won);
        mainPanel.add(endPanel, "endPanel");
        showPanel("endPanel");
    }

    public void checkPanelDuplicate(String name){ //metoda ma na celu sprawdzanie i usuwanie powtarzajacyh sie paneli po restarcie
        Arrays.stream(mainPanel.getComponents())
                .filter(c -> name.equals(c.getName()))
                .forEach(mainPanel::remove);
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenuItem restartItem = new JMenuItem("Restart");
        JMenuItem exitItem = new JMenuItem("Exit");

        restartItem.addActionListener(e -> {
            restartGame();
        });

        exitItem.addActionListener(e -> System.exit(0));

        gameMenu.add(restartItem);
        gameMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem rulesItem = new JMenuItem("Rules");
        rulesItem.addActionListener(e -> new RulesFrame());

        helpMenu.add(rulesItem);

        menuBar.add(gameMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }




}
