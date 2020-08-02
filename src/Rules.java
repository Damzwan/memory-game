import javax.swing.*;
import java.awt.*;

public class Rules {

    public Rules() {
        JFrame f = new JFrame("Rules");
        f.setSize(400, 400);

        JTextArea area=new JTextArea("Rules \n -------------------------------------------------------------------------- \n You win by flipping some cards \n Yanan schrijf de regels uit xd");
        area.setFont(new Font("Serif", Font.PLAIN,  15));
        area.setBounds(10,30, 380,350);
        area.setEditable(false);
        f.add(area);

        f.setVisible(true);
    }
}
