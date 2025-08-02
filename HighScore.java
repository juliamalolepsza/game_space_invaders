import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HighScore {

    private String nick;
    private int score;
    private static String filePath = "highScores.txt";
    private static final List<HighScore> highScores = new ArrayList<>();

    public HighScore(String nick, int score) {
        this.nick = nick;
        this.score = score;

        highScores.add(this);
    }

    public static void readScoreFromFile() {
        try {
            File file = new File(filePath);
            file.createNewFile(); //upewnienie sie ze plik istnieje

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                String nick = parts[0];
                int score = Integer.parseInt(parts[1]);
                HighScore highScore = new HighScore(nick, score); //doda sie do listy w konst
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void WriteBestScoresToFile(){   //aktualizacja, wywoluje sie po utworzeniu nowego wyniku w EndPanel
        try {
            File file = new File(filePath);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));

            for (HighScore hs : getTop10HighScores()) {
                bw.write(hs.getNick() + ":" + hs.getScore());
                bw.newLine();
            }
            bw.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean isTop10(HighScore newScore){
        return getTop10HighScores().contains(newScore);
    }

    public static List<HighScore> getTop10HighScores() {
        return highScores.stream() //stream kopii oryginalnej listy
                .sorted(Comparator.comparingInt(HighScore::getScore).reversed())
                .limit(10)
                .toList();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}