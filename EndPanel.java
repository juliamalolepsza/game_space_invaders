import javax.swing.*;
import java.awt.*;

public class EndPanel extends JPanel {
    private boolean won;
    
    public EndPanel(String nick, int score, boolean won) {
        setBackground(Color.BLACK);
        JLabel label = new JLabel(won ? "YOU WON!" : "GAME OVER");
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
        label.setForeground(Color.WHITE);
        add(Box.createVerticalStrut(50)); //przerwa
        add(label);

        JLabel scoreLabel = new JLabel("Nick: " + nick + " Score: " + score);
        scoreLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        scoreLabel.setForeground(Color.WHITE);
        add(scoreLabel);

        HighScore myScore = new HighScore(nick, score);
        HighScore.WriteBestScoresToFile();

        JLabel infoLabel = new JLabel(HighScore.isTop10(myScore) ? "YOU'RE TOP 10 PLAYERS!" : "THIS TIME YOU DIDN'T MAKE IT TO TOP10");
        infoLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        infoLabel.setForeground(Color.WHITE);
        add(infoLabel);

        if(HighScore.isTop10(myScore)){
            new HighScoreFrame(); //wyswietla juz wyfiltrowane top10
        }
    }

}
