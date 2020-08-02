import javax.swing.*;
import java.awt.*;

public class HighScore {

    public HighScore() {
        JFrame f = new JFrame("Highscores");
        f.setSize(400, 400);

        JTextArea area=new JTextArea(createHighscoreString());
        area.setFont(new Font("Serif", Font.PLAIN,  15));
        area.setBounds(10,30, 380,350);
        area.setEditable(false);
        f.add(area);

        f.setVisible(true);
    }

    public String createHighscoreString(){
        return "Highscores \n -------------------------------------------------------------------------- \n Damian: 50000 points \n Yanan: -500 points \n Robi: -30000 points";
    }
}
