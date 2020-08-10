import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


//This class defines a board, each game is played on a board and a board contains multiple cards
//Each board consists of a theme, a Game and a dimension
class Board {
    private int[] dimension;
    private ImageIcon back = null;
    private String theme;
    private ArrayList<Card> cards;
    private Game game;
    static int BombID = -1;
    static int shuffleId = -2;

    //The constructor for board requires a dimension, a theme and a Game.
    //The dimension is used to generate the right amount of cards, needed on this board.
    //The theme is used to select the right Images used to create the cards.
    //after the cards are generated the Arraylist which contains the cards, gets shuffled to ensure a random sequence
    //of cards every game.
    Board(int[] dim, String theme, Game game) {
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

    //This method returns an arraylist containing all the cards, each card is used twice to create pairs and there
    // are 2 special cards. First the 2 special cards are created and then the rest of the cards.
    private ArrayList<Card> generateCards(int[] dim) throws IOException {
        ArrayList<Card> cards = new ArrayList<>();
        int dif_cards = (dim[0] * dim[1]) / 2 - 2;

        //This bomb is the first special card
        Image bomb = ImageIO.read(getClass().getResource("Images/bomb.png"));
        ImageIcon iconbomb = new ImageIcon(resizeImage(bomb));
        cards.add(createCard(iconbomb, BombID));

        //the shuffle card is the second special card
        Image shuffle = ImageIO.read(getClass().getResource("Images/Cards.png"));
        ImageIcon iconShuffle = new ImageIcon(resizeImage(shuffle));
        cards.add(createCard(iconShuffle, shuffleId));

        //Here all other necessary cards are created twice to have pairs
        for (int i = 0; i <= dif_cards; i++) {
            Image pic = ImageIO.read(getClass().getResource("Images/" + i + ".png"));
            ImageIcon icon = new ImageIcon(resizeImage(pic));
            cards.add(createCard(icon, i));
            cards.add(createCard(icon, i));
        }
        return cards;
    }

    //this method receives an Image and reScales it based on the dimensions of the game
    private Image resizeImage(Image image){
        int newWidth = Game.windowWidth/dimension[0];
        int newHeigth = (Game.windowHeight  - Game.scorePanelHeight)/dimension[1];
        return image.getScaledInstance(newWidth, newHeigth, Image.SCALE_SMOOTH);
    }

    //this method creates a new card with the given ImageIcon and ID and adds an actionlistener to the Card
    private Card createCard(ImageIcon icon, int i) {
        Card card = new Card(icon, i);
        card.addActionListener(e -> game.turnCard((Card) e.getSource()));
        return card;
    }

    //This method returns the cards of the board
    ArrayList<Card> getCards() {
        return cards;
    }
}
