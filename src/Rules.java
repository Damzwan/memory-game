import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/*
 * Rules is the class where the rules are explained and is used when pushing the Rules button in the menu.
 * A general frame is used and the text is put into a JText. At the bottom, a button is used to close the 
 * rules frame and return to the menu.
 */
public class Rules {


    public Rules() throws IOException {
        JFrame f = new JFrame("Rules");
        f.setSize(590, 400);
        
        JTextArea area = new JTextArea("   Rules \n   ------------------------------------------------------------------------------------------------------"+
        		"\n   A memory game starts with all cards faced down. The aim is to earn more points than"+
        		"\n   the opposite player by turning over two cards each time. When player 1 does not find a" + 
        		"\n   pair, it is player 2's turn. The game ends when all pairs are found and the cards are tur-" + 
        		"\n   ned. The player with the highest score wins. " +
        		"\n   There are also two special cards, which are illustrated below. One card is a bomb and" +
        		"\n   makes the player who clicks on it lose 200 points, while the other special card, a pictu-" +
        		"\n   re of some cards and reshuffles the pairs that are not found yet." +
        		"\n   ------------------------------------------------------------------------------------------------------");
        area.setFont(new Font("Serif", Font.PLAIN, 15));
        area.setBounds(10, 30, 380, 350);
        area.setEditable(false);
        f.add(area,BorderLayout.NORTH);
        
        //This panel adds the special cards as an illustration.
        JPanel panel = new JPanel();
        Image bomb = ImageIO.read(getClass().getResource("Images/Animals/Bomb.png"));
        bomb = bomb.getScaledInstance(80, 80,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon iconbomb = new ImageIcon(bomb);
        Image cards = ImageIO.read(getClass().getResource("Images/Animals/Cards.png"));
        cards = cards.getScaledInstance(80, 80,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon iconcards = new ImageIcon(cards);
        panel.add(new JLabel(iconbomb),BorderLayout.WEST);
        panel.add(new JLabel(iconcards),BorderLayout.EAST);
        f.add(panel,BorderLayout.CENTER);

        
        //This button is used to close the window of Rules to be more player friendly.
        JButton back = new JButton("Return back to the menu");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
               f.dispose();
            }
        });
        f.add(back,BorderLayout.SOUTH);

        f.setVisible(true);
    }




}



