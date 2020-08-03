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
        int dif_cards = (dim[0] * dim[1])/2 - 1;
        Image bomb = ImageIO.read(getClass().getResource("Images/" + "bomb" + ".png"));
        ImageIcon iconbomb = new ImageIcon(bomb);


        for(int i = 0; i < dif_cards; i++) {
            System.out.println(i);
            Image pic = ImageIO.read(getClass().getResource("Images/" + i + ".png"));
            ImageIcon icon = new ImageIcon(pic);

            Card card1 = createCard(icon, i);
            cards.add(card1);
            cards.add(card1);
        }
        return cards;
    }

    private Card createCard(ImageIcon icon, int i){
        Card card = new Card(icon, i);
        card.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.actionPerformed(e);
            }
        });
        return card;
    }

    public ArrayList<Card> getCards(){
        return cards;
    }
}
