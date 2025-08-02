import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel {
    private String nick;
    private int aliensCount;
    private int alienRows;
    private String selectedShipPath;
    private boolean killable;
    private int speed;


    public StartPanel(GameFrame parent) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));

        // Nick
        JPanel nickPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        nickPanel.add(new JLabel("Nick:"));
        JTextField nickTextField = new JTextField(15);
        nickPanel.add(nickTextField);
        this.add(nickPanel);


        // Ship
        this.selectedShipPath = "pinkShip.png"; //domyslna wartosc
        JPanel shipPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel shipLabel = new JLabel("Pick a ship:");
        String[] imagePaths = new String[] {
                "blueShip.png",
                "greenShip.png",
                "pinkShip.png"
        };
        JComboBox<ImageIcon> imageSelector = new JComboBox<>(new ImageIcon[] {
                resizeIcon("blueShip.png", 100, 40),
                resizeIcon("greenShip.png", 100, 40),
                resizeIcon("pinkShip.png", 100, 40)
        });
        imageSelector.setPreferredSize(new Dimension(130, 50));
        imageSelector.addActionListener(e -> {
            int index = imageSelector.getSelectedIndex();
            this.selectedShipPath = imagePaths[index];
        });
        shipPanel.add(shipLabel);
        shipPanel.add(imageSelector);
        this.add(shipPanel);

        //killable
        this.killable = true;
        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        modePanel.add(new JLabel("Choose mode:"));
        JRadioButton normalMode = new JRadioButton("Normal");
        JRadioButton testMode = new JRadioButton("Test mode (invincible)");
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(normalMode);
        modeGroup.add(testMode);
        normalMode.setSelected(true);
        normalMode.addActionListener(e -> this.killable = true);
        testMode.addActionListener(e -> this.killable = false);
        modePanel.add(normalMode);
        modePanel.add(testMode);
        this.add(modePanel);


        //aliens row
        this.alienRows = 3;
        JPanel aliensRowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel alienRowLabel = new JLabel("Choose number of aliens rows:");
        JList<Integer> rows = new JList<>(new Integer[]{3, 4, 5});
        rows.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rows.setVisibleRowCount(3);
        JScrollPane scrollRowPane = new JScrollPane(rows);
        scrollRowPane.setPreferredSize(new Dimension(100, 60));
        rows.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                this.alienRows = rows.getSelectedValue();
            }
        });
        aliensRowPanel.add(alienRowLabel);
        aliensRowPanel.add(scrollRowPane);
        this.add(aliensRowPanel);

        // Aliens count
        this.aliensCount = 3;
        JPanel aliensPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel alienCountLabel = new JLabel("Choose number of aliens in a row:");
        JList<Integer> numbers = new JList<>(new Integer[]{3, 4, 5, 6, 7});
        numbers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        numbers.setVisibleRowCount(3);
        JScrollPane scrollPane = new JScrollPane(numbers);
        scrollPane.setPreferredSize(new Dimension(100, 60));
        numbers.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                this.aliensCount = numbers.getSelectedValue();
            }
        });
        aliensPanel.add(alienCountLabel);
        aliensPanel.add(scrollPane);
        this.add(aliensPanel);

        // Speed
        this.speed = 1;
        JPanel speedPanel = new JPanel(new BorderLayout());
        speedPanel.add(new JLabel("Speed:"), BorderLayout.WEST);
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 5, 1);
        speedPanel.add(speedSlider, BorderLayout.CENTER);
        speedPanel.setMaximumSize(new Dimension(300, 100));
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.addChangeListener(e -> {
            this.speed = speedSlider.getValue();
        });
        this.add(speedPanel);

        //rules and start
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton highScoreButton = new JButton("High Scores");
        JButton rulesButton = new JButton("Rules");
        JButton startButton = new JButton("Start");
        highScoreButton.addActionListener(e -> {
            new HighScoreFrame();
        });
        rulesButton.addActionListener(e -> {
            new RulesFrame();
        });
        startButton.addActionListener(e -> {
            this.nick = nickTextField.getText(); //pobieranie nicku z pola
            if (this.nick.isBlank()) //null lub pusty
                this.nick = "user";
            parent.createGamePanel(parent, nick, aliensCount, alienRows, selectedShipPath, killable, speed);
        });
        buttonPanel.add(highScoreButton);
        buttonPanel.add(rulesButton);
        buttonPanel.add(startButton);
        this.add(buttonPanel);
    }

    private ImageIcon resizeIcon(String path, int width, int height) { //dostosowanie wielkosci obrazka statku w jComboBox
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    public String getNick() {
        return nick;
    }

    public int getAliensCount() {
        return aliensCount;
    }

    public String getSelectedShip() {
        return selectedShipPath;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isKillable() {
        return killable;
    }
}
