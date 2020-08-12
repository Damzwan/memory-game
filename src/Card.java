import javax.swing.*;
import java.awt.*;


/*In this class, the cards are linked to an ID and an ImageIcon. A card is the lowest entity of this game and also an extension of JButton.
 *Each cards is defined a picture (ImageIcon) and an ID. The picture is used by players of the game to find matches of cards and
 *the ID's are used in the program to check if 2 cards are a match.
 */
public class Card extends JButton {
    private ImageIcon picture;
    private int ID;

    //The constructor for Cards uses as variables a given picture and ID to create and define them. Also, a background picture colored pink is given.
    Card(ImageIcon photo, int id) {
        this.setBackground(Color.PINK);
        this.picture = photo;
        this.ID = id;
    }

    //Method turns the card, the Icon of this JButton becomes either the photo or null depending on the previous setting of the card.
    void turnCard() {
        this.setIcon(this.getIcon() == null ? getCardIcon() : null);
    }

    //Getter methods of the card's icon and ID.
    private ImageIcon getCardIcon() {
        return picture;
    }

    int getID() {
        return ID;
    }
}


