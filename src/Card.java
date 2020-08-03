import javax.swing.*;

public class Card extends JButton{
    private ImageIcon background;
    private ImageIcon photo;
    private Boolean isVisible;
    private int ID;

    Card(ImageIcon photo, ImageIcon back, int id){
        super(back);
        background = back;
        this.photo = photo;
        this.isVisible = false;
        this.ID = id;
    }

    public void turnCard(){
        isVisible = !isVisible;
        this.setIcon(getCardIcon());
    }

    public ImageIcon getCardIcon(){
        return photo;
    }

    public boolean isVisible(){
        return isVisible;
    }

    public void setIsVisible(boolean bool){
        isVisible = bool;
    }

    public void setCardIcon(ImageIcon icon){
        photo = icon;
    }

    public int getID(){
        return ID;
    }
}


