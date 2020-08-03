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
            Image pic = ImageIO.read(getClass().getResource("Images/" + i + ".png"));
            ImageIcon icon = new ImageIcon(pic);

            for (int j = 0; j < 2; j++){
                Card card = new Card(icon, i);
                card.addActionListener(e -> game.actionPerformed(e));
                cards.add(card);
            }
        }
        return cards;
    }
    public ArrayList<Card> getCards(){
        return cards;
    }
}
