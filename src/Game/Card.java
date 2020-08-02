package Game;

import javax.swing.*;

public class Card extends JButton{
    Icon photo;
    Icon back;
    Boolean isVisible = false;
    int ID;

    public Card(Icon photo, int id){
        this.photo = photo;
//        this.back = back;
        this.isVisible = false;
        this.ID = id;
    }

    public void turnCard(){
        isVisible = !isVisible;
    }

    public Icon getIcon(){
        return photo;
    }

    public int getID(){
        return ID;
    }
}


