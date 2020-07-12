package Game;

import javax.swing.*;

public class Card extends JButton{
    Icon photo;
    Icon back;
    Boolean isVisible = false;

    public Card(Icon photo){
        this.photo = photo;
//        this.back = back;
        this.isVisible = false;
    }

    public void turnCard(){
        isVisible = !isVisible;
    }
}


