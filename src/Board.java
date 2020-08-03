import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;



class Board {
    private int[] dimension;
    private ImageIcon back = null;
    private String theme;
    private ArrayList<Card> cards;
    private Game game;

    public Board(int[] dim, String theme, Game game){
        this.dimension = dim;
        this.theme = theme;
        this.game = game;
        try {
            this.cards = generateCards(dim);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.shuffle(cards);
    }

    private ArrayList<Card> generateCards(int[] dim) throws IOException {
        ArrayList<Card> cards = new ArrayList<>();
        int dif_cards = (dim[0] * dim[1])/2;
        for(int i = 0; i < dif_cards; i++) {
            System.out.println(i);
            Image pic = ImageIO.read(getClass().getResource("Images/" + i + ".png"));
            ImageIcon icon = new ImageIcon(pic);

            Card card1 = new Card(icon, back, i);
            card1.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    game.actionPerformed(e);
                }
            });
            card1.setForeground(Color.BLUE);
            cards.add(card1);

            Card card2 = new Card(icon, back, i);
            card2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    game.actionPerformed(e);
                }
            });
            card2.setForeground(Color.BLUE);
            cards.add(card2);
        }
        return cards;
    }
    public ArrayList<Card> getCards(){
        return cards;
    }
}
