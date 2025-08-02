import javax.swing.*;
import java.awt.*;

public class HighScoreFrame extends JFrame {

    public HighScoreFrame(){
        setTitle("Top Scores");
        setLocationRelativeTo(null);
        setSize(400, 300);


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);
        this.add(panel);


        for (HighScore hs : HighScore.getTop10HighScores()) {
            JLabel ls = new JLabel("Nick: " + hs.getNick() + " Score: " + hs.getScore());
            ls.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            ls.setForeground(Color.GREEN);
            panel.add(ls);
        }

        setVisible(true);


    }

}
