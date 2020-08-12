import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


/*This class defines the board of the memory game.Each game develops a new board containing multiple cards.
 * The cards are shuffled each time and the dimensions can change as chosen in the Menu. 
 * Each object of the class Board consists of an object of the class Theme and Game and a dimension
 */
class Board {
    private int[] dimension;
    private Theme theme;
    private ArrayList<Card> cards;
    private Game game;
    static int BombID = -1;
    static int shuffleId = -2;
    //2 class variables, one for each theme that can be chosen and the path to the images needed.
    private String animalPath = "Images/Animals/";
    private String christmasPath = "Images/Christmas/";

    /*The constructor for board requires a dimension, a theme and a Game.
     *The dimension is used to generate the right amount of cards, needed on this board.
     *The theme is used to select the right Images used to create the cards.
     *after the cards are generated the Array list which contains the cards, gets shuffled to ensure a random sequence
     *of cards every game.
     */
    Board(int[] dim, Theme theme, Game game) {
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

    /*This method returns an array list containing all the cards, both normal and special. Each card is used twice 
     *to create pairs and there are 2 special cards. First the 2 special cards are created and then the normal pairs.
     */
    private ArrayList<Card> generateCards(int[] dim) throws IOException {
        ArrayList<Card> cards = new ArrayList<>();
        //???????
        int dif_cards = (dim[0] * dim[1]) / 2 - 2;

        //This bomb is the first special card. It gives the player who turns it a total of -200 points.
        Image bomb = ImageIO.read(getClass().getResource("Images/Animals/Bomb.png"));
        ImageIcon iconbomb = new ImageIcon(resizeImage(bomb));
        cards.add(createCard(iconbomb, BombID));

        //The shuffle card is the second special card, reshuffling the cards which are still turned down.
        Image shuffle = ImageIO.read(getClass().getResource("Images/Animals/Cards.png"));
        ImageIcon iconShuffle = new ImageIcon(resizeImage(shuffle));
        cards.add(createCard(iconShuffle, shuffleId));
        
        //Create local variable to connect a path to using the Theme class to get the variable (using the getter method).
        String path;
        if(theme == Theme.ANIMALS) {
        	path = animalPath;
        }
        else { 
        	path = christmasPath;
        }
        
        //All normal pairs/cards necessary are created twice here using the path variable again.
        for (int i = 0; i <= dif_cards; i++) {
            Image pic = ImageIO.read(getClass().getResource(path + i + ".png"));
            ImageIcon icon = new ImageIcon(resizeImage(pic));
            cards.add(createCard(icon, i));
            cards.add(createCard(icon, i));
        }
        return cards;
    }

    //This method receives an Image and reScales it based on the dimensions of the game.
    private Image resizeImage(Image image){
        int newWidth = Game.windowWidth/dimension[0];
        int newHeigth = (Game.windowHeight  - Game.scorePanelHeight)/dimension[1];
        return image.getScaledInstance(newWidth, newHeigth, Image.SCALE_SMOOTH);
    }

    //This method creates a new card with the given ImageIcon, ID and action listener to the Card.
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
