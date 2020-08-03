import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Set;

public class Game {
    int[] dim;
    String theme = "Some Theme";
    private ArrayList<Card> tocompare = new ArrayList<>();
    private Mode mode;
    private Difficulty difficulty;
    private int scorePlayer1;
    private int scorePlayer2;
    private boolean isPlayer1 = true;
    private ArrayList<Card> cards;
    private JLabel scorePlayer1Label;
    private JLabel scorePlayer2Label;
    private JLabel playerTurn;

    private static final Set<String> valid_difficulties = Set.of("Easy", "Medium", "Hard");
    private static final Set<String> valid_modes = Set.of("Easy", "Medium", "Hard");

    public Game(int[] dim, Mode mode, Difficulty difficulty, int width, int height) throws IllegalStateException {
        Board board = new Board(dim, theme, this);
//        if(!(valid_difficulties.contains(difficulty)))throw new IllegalArgumentException("invalid difficulty");
//        if(!(valid_modes.contains(mode))) throw new IllegalStateException("This is an Illegal mode");
        this.mode = mode;
        this.difficulty = difficulty;
        this.cards = board.getCards();

        JFrame f = new JFrame("Game");
        f.setSize(width, height);
        JPanel mainPanel = new JPanel();
        addButtons(mainPanel);
        mainPanel.setLayout(new GridLayout(dim[0], dim[1]));
        f.add(mainPanel);


        JPanel scorePanel = new JPanel();
        scorePlayer1Label = new JLabel();
        scorePlayer1Label.setText("Player P1: " + "000");
        scorePlayer2Label = new JLabel();
        scorePlayer2Label.setText("Score P2: " + "000");
        playerTurn = new JLabel();
        playerTurn.setText("Player 1's Turn");
        scorePanel.add(scorePlayer1Label);
        scorePanel.add(scorePlayer2Label);
        scorePanel.add(playerTurn);
        f.add(scorePanel, BorderLayout.NORTH);

        f.setVisible(true);


    }

    private void addButtons(JPanel panel) {
        for (Card card : cards) {
            panel.add(card);
        }
    }

    public void actionPerformed(ActionEvent e) {


        //If this is the exit button we quit the game
//        if (((JButton) e.getSource()).getText() == "Exit the game"){
//            memoryLayout.dispose();
//            return;
//        }

        Card card = (Card) e.getSource();
        if (card.getIcon() == null) {
            card.turnCard();
            tocompare.add(card);
            if (tocompare.size() == 2) isMatch(tocompare.get(0), tocompare.get(1));
        }

        if (isfinished()) {
            System.out.println("game finished");
            int highScore = scorePlayer2;
            if (scorePlayer1 > scorePlayer2) {
                System.out.println("Player 1 won!");
                highScore = scorePlayer1;
            } else if (scorePlayer1 < scorePlayer2) {
                System.out.println("Player 2 won!");
            } else {
                System.out.println("It's a draw!");
            }
        }
        //If we have 2 images we start comparing them
    }

    private void isMatch(Card card1, Card card2) {
        if (card1.getID() == card2.getID()) {
            if (isPlayer1) scorePlayer1 += 100;
            else scorePlayer2 += 100;
            System.out.println("match");
        } else {
            card1.setIcon(null);
//            card1.setRip(false);
            card2.setIcon(null);
//            card2.setRip(false);
            isPlayer1 = !isPlayer1;
            System.out.println("no Match");
        }
        tocompare.clear();
    }

    private boolean isfinished() {
        for (Card card : cards) {
            if (card.getIcon() == null) {
                return false;
            }
        }
        return true;
    }
}

