import javax.swing.*;
import java.awt.*;

public class Card extends JButton {
    private ImageIcon photo;
    private int ID;

    Card(ImageIcon photo, int id) {
        this.setBackground(Color.PINK);
        this.photo = photo;
        this.ID = id;
    }

    public void turnCard() {
        this.setIcon(this.getIcon() == null ? getCardIcon() : null);
    }

    public ImageIcon getCardIcon() {
        return photo;
    }

    public int getID() {
        return ID;
    }
}


