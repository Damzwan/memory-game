import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


class Board {
    private int[] dimension;
    private ImageIcon back = null;
    private String theme;
    private ArrayList<Card> cards;
    private Game game;
    public static int BombID = -1;
    public static int shuffelID = -2;

    public Board(int[] dim, String theme, Game game) {
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
        int dif_cards = (dim[0] * dim[1]) / 2 - 2;

        Image bomb = ImageIO.read(getClass().getResource("Images/bomb.png"));
        ImageIcon iconbomb = new ImageIcon(resizeImage(bomb));
        cards.add(createCard(iconbomb, BombID));

        Image shuffle = ImageIO.read(getClass().getResource("Images/Cards.png"));
        ImageIcon iconShuffle = new ImageIcon(resizeImage(shuffle));
        cards.add(createCard(iconShuffle, shuffelID));


        for (int i = 0; i <= dif_cards; i++) {
            Image pic = ImageIO.read(getClass().getResource("Images/" + i + ".png"));
            ImageIcon icon = new ImageIcon(resizeImage(pic));
            cards.add(createCard(icon, i));
            cards.add(createCard(icon, i));
        }
        return cards;
    }

    private Image resizeImage(Image image){
        int newWidth = Game.windowWidth/dimension[0];
        int newHeigth = (Game.windowHeight  - Game.scorePanelHeight)/dimension[1];
        return image.getScaledInstance(newWidth, newHeigth, Image.SCALE_SMOOTH);
    }

    private Card createCard(ImageIcon icon, int i) {
        Card card = new Card(icon, i);
        card.addActionListener(e -> game.turnCard((Card) e.getSource()));
        return card;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
