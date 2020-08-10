import javax.swing.*;
import java.awt.*;


//In this class, cards are defined. A card is the lowest entity of a game and is an extension of Jbutton.
//Each class has a photo (ImageIcon) and an ID. The photo is used by players of the game to find matches of cards and
//the ID's are used in the program to check if 2 cards are a match.
public class Card extends JButton {
    private ImageIcon photo;
    private int ID;

    //The constructor for Cards, the photo and the ID are given and saved.
    Card(ImageIcon photo, int id) {
        this.setBackground(Color.PINK);
        this.photo = photo;
        this.ID = id;
    }

    //this method "turns" the card, the Icon of this Jbutton becomes either the photo or null depending on what it
    // was before the method was evoked
    void turnCard() {
        this.setIcon(this.getIcon() == null ? getCardIcon() : null);
    }

    //This method returns the ImageIcon of the card
    private ImageIcon getCardIcon() {
        return photo;
    }

    //this method returns the ID of the card
    int getID() {
        return ID;
    }
}


