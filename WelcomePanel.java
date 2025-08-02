import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WelcomePanel extends JPanel {
    public WelcomePanel(GameFrame parent) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
        ImageIcon img = new ImageIcon("welcome.png");
        JLabel imageLabel = new JLabel(img);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parent.createStartPanel();
            }
        });

        add(imageLabel, BorderLayout.CENTER);
    }

}
