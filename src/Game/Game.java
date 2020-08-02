package Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Set;

public class Game{
    int[] dim;
    String theme = "Some Theme";
    private ArrayList<Card> tocompare = new ArrayList<>();
    private String mode = "Some mode";
    private String difficulty;
    private int scorePlayer1;
    private int scorePlayer2;
    private boolean isPlayer1 = true;
    private ArrayList<Card> cards;

    private static final Set<String> valid_difficulties = Set.of("Easy","Medium","Hard");
    private static final Set<String> valid_modes = Set.of("Easy","Medium","Hard");

    public Game(int[] dim, String theme, String mode, String difficulty)throws IllegalStateException{
        Board board = new Board(dim, theme);
        if(!(valid_difficulties.contains(difficulty)))throw new IllegalArgumentException("invalid difficulty");
        if(!(valid_modes.contains(mode))) throw new IllegalStateException("This is an Illegal mode");
        this.mode = mode;
        this.difficulty = difficulty;
        this.cards = board.getCards();

    }

    public void actionPerformed(ActionEvent e) {


        //If this is the exit button we quit the game
//        if (((JButton) e.getSource()).getText() == "Exit the game"){
//            memoryLayout.dispose();
//            return;
//        }

        Card card = (Card) e.getSource();
        card.setIcon(card.getIcon());
        tocompare.add(card);

        if(tocompare.size() == 2) {
            isMatch(tocompare.get(0), tocompare.get(1));
        }
        tocompare.clear();

        if(isfinished()) {
            System.out.println("game finished");
            int highScore = scorePlayer2;
            if (scorePlayer1 > scorePlayer2) {
                System.out.println("Player 1 won!");
                highScore = scorePlayer1;
            }
            else if (scorePlayer1 < scorePlayer2) {System.out.println("Player 2 won!");}
            else {System.out.println("It's a draw!");}
        }
        //If we have 2 images we start comparing them
    }

    private void isMatch(Card card1, Card card2){
        if(card1.getID() == card2.getID()){
            if(isPlayer1) scorePlayer1 += 100;
            else scorePlayer2 += 100;
        }
        else {
            card1.setIcon(null);
            card2.setIcon(null);
            isPlayer1 = !isPlayer1;
        }
    }

    private boolean isfinished(){
        for (Card card : cards) {
            if (card.getIcon() == null) {
                return false;
            }
        }
        return true;
    }
}

