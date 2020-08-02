package Game;

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
    private String theme;
    private ArrayList<Card> cards;

    public Board(int[] dim, String theme){
        this.dimension = dim;
        this.theme = theme;
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
            Image pic = ImageIO.read(getClass().getResource("../Images/" + i + ".png"));
            ImageIcon icon = new ImageIcon(pic);

            Card card1 = new Card(icon, i);
            card1.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    //todo Add code haha
                }
            });
            card1.setForeground(Color.BLUE);
            cards.add(card1);

            Card card2 = new Card(icon, i);
            card1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //todo Add code haha
                }
            });
            card2.setForeground(Color.blue);
            cards.add(card2);
        }
        return cards;
    }
    public ArrayList<Card> getCards(){
        return cards;
    }
}
