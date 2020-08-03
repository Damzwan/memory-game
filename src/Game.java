import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Set;

public class Game {
    int[] dim;
    String theme = "Some Theme";
    private JFrame window;
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
    private boolean timerrunning = false;
    private static final Set<String> valid_difficulties = Set.of("Easy", "Medium", "Hard");
    private static final Set<String> valid_modes = Set.of("Easy", "Medium", "Hard");

    public Game(int[] dim, Mode mode, Difficulty difficulty, int width, int height) throws IllegalStateException {
        Board board = new Board(dim, theme, this);
//        if(!(valid_difficulties.contains(difficulty)))throw new IllegalArgumentException("invalid difficulty");
//        if(!(valid_modes.contains(mode))) throw new IllegalStateException("This is an Illegal mode");
        this.mode = mode;
        this.difficulty = difficulty;
        this.cards = board.getCards();

        this.window = new JFrame("Game");
        window.setSize(width, height);
        JPanel mainPanel = new JPanel();
        addButtons(mainPanel);
        mainPanel.setLayout(new GridLayout(dim[0], dim[1]));
        window.add(mainPanel);


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
        window.add(scorePanel, BorderLayout.NORTH);

        window.setVisible(true);


    }

    private void addButtons(JPanel panel) {
        for (Card card : cards) {
            panel.add(card);
        }
    }

    public void cardClicked(ActionEvent e) {

        if (timerrunning) return;
        Card card = (Card) e.getSource();
        if (card.getIcon() != null) return;

        card.turnCard();
        tocompare.add(card);
        if (tocompare.size() != 2) return;

        int delay = 500;
        Timer timer = new Timer(delay, ex -> {
            isMatch(tocompare.get(0), tocompare.get(1));
            timerrunning = false;

            if (!isFinished()) return;
            new GameOver(scorePlayer1, scorePlayer2);
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        });

        timer.setRepeats(false);//make sure the timer only runs once
        timerrunning = true;
        timer.start();
    }

    private void isMatch(Card card1, Card card2) {
        if (card1.getID() == card2.getID()) {
            if (isPlayer1) scorePlayer1 += 100;
            else scorePlayer2 += 100;
            System.out.println("match");
            scorePlayer1Label.setText("Player P1: " + scorePlayer1);
            scorePlayer2Label.setText("Player P2: " + scorePlayer2);
        } else {
            card1.turnCard();
            card2.turnCard();
            isPlayer1 = !isPlayer1;
            playerTurn.setText(isPlayer1 ? "Player 1's Turn" : "Player 2's Turn");
            System.out.println("no Match");
        }
        tocompare.clear();
    }

    private boolean isFinished() {
        for (Card card : cards)
            if (card.getIcon() == null) return false;
        return true;
    }
}

