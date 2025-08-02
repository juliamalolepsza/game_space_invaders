import javax.swing.*;
import java.awt.*;

public class RulesFrame extends JFrame {
    public RulesFrame() {
        setTitle("Rules");
        setLocationRelativeTo(null);
        setSize(400, 300);


        JTextArea rulesArea = new JTextArea();
        rulesArea.setText("""
                Space Invaders Rules:

                your goal is to destroy all aliens.
                Fire bullets with button or space keypress
                and avoid alien bullets moving with arrows on keyboard
                or buttons.
                Aliens progressively move down with the speed
                given in the start panel.
                There is no time limit, except for the fact that
                if you do not shoot them all before they reach you
                the game ends.

                Good luck!
                """);
        rulesArea.setEditable(false);
        rulesArea.setLineWrap(true);
        rulesArea.setWrapStyleWord(true);
        rulesArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(rulesArea);
        add(scrollPane);

        setVisible(true);
    }


}
