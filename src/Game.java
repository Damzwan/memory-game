import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class Game {
    int[] dim;
    String theme = "Some Theme";
    private JFrame window;
    private JFrame postShuffleWindow;
    private ArrayList<Card> cardsToCompare = new ArrayList<>();
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
    private int width;
    private int height;
    private JPanel scorePanel = new JPanel();
    private int bombDamage = -200;


    Game(int[] dim, Mode mode, Difficulty difficulty, int width, int height) throws IllegalStateException {
        Board board = new Board(dim, theme, this);
//        if(!(valid_difficulties.contains(difficulty)))throw new IllegalArgumentException("invalid difficulty");
//        if(!(valid_modes.contains(mode))) throw new IllegalStateException("This is an Illegal mode");
        this.mode = mode;
        this.difficulty = difficulty;
        this.cards = board.getCards();
        this.height = height;
        this.width = width;
        this.dim = dim;


        this.window = new JFrame("Game");
        window.setSize(width, height);
        window.add(createCardPanel());
        window.add(createScorePanel(), BorderLayout.NORTH);
        window.setVisible(true);
    }

    private JPanel createCardPanel() {
        JPanel cardPanel = new JPanel();
        addCardsToPanel(cardPanel);
        cardPanel.setLayout(new GridLayout(dim[0], dim[1]));
        return cardPanel;
    }

    private JPanel createScorePanel() {
        scorePlayer1Label = new JLabel();
        scorePlayer1Label.setText("Player P1: " + "000");
        scorePlayer2Label = new JLabel();
        scorePlayer2Label.setText("Score P2: " + "000");
        playerTurn = new JLabel();
        playerTurn.setText("Player 1's Turn");
        scorePanel.add(scorePlayer1Label);
        scorePanel.add(scorePlayer2Label);
        scorePanel.add(playerTurn);
        return scorePanel;
    }

    private void addCardsToPanel(JPanel panel) {
        for (Card card : cards) {
            panel.add(card);
        }
    }

    void cardClicked(ActionEvent e) {

        if (timerrunning) return;
        Card card = (Card) e.getSource();
        if (card.getIcon() != null) return;
        card.turnCard();

        if (isSpecialCard(card)){
            isFinished();
            return;
        }


        cardsToCompare.add(card);
        if (cardsToCompare.size() != 2) return;

        int delay = 500;
        Timer timer = new Timer(delay, ex -> {
            isMatch(cardsToCompare.get(0), cardsToCompare.get(1));
            timerrunning = false;
        });

        timer.setRepeats(false);//make sure the timer only runs once
        timerrunning = true;
        timer.start();
    }



    private void isMatch(Card card1, Card card2) {
        if (card1.getID() == card2.getID()){
            updateScore(100);
            isFinished();
        }
        else endTurn(card1, card2);
        cardsToCompare.clear();
    }

    private void updateScore(int amount) {
        if (isPlayer1) scorePlayer1 += amount;
        else scorePlayer2 += amount;
        scorePlayer1Label.setText("Player P1: " + scorePlayer1);
        scorePlayer2Label.setText("Player P2: " + scorePlayer2);
    }

    private void endTurn(Card card1, Card card2) {
        card1.turnCard();
        card2.turnCard();
        isPlayer1 = !isPlayer1;
        playerTurn.setText(isPlayer1 ? "Player 1's Turn" : "Player 2's Turn");
    }

    private void isFinished() {
        for (Card card : cards)
            if (card.getIcon() == null) return;
        endGame();
    }

    private void endGame(){
        new GameOver(scorePlayer1, scorePlayer2);
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        postShuffleWindow.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }

    private boolean isSpecialCard(Card card){
        if (card.getID() == Board.BombID) {
            updateScore(bombDamage);
            return true;
        }

        if (card.getID() == Board.shuffelID) {
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
            createPostShuffleWindow();
            return true;
        }
        return false;
    }

    private void createPostShuffleWindow(){
        this.postShuffleWindow = new JFrame("Post-Shuffle");
        postShuffleWindow.setSize(width, height);
        JPanel mainPanel = new JPanel();
        Collections.shuffle(cards);
        addCardsToPanel(mainPanel);
        mainPanel.setLayout(new GridLayout(dim[0], dim[1]));
        postShuffleWindow.add(mainPanel);
        postShuffleWindow.add(scorePanel, BorderLayout.NORTH);
        postShuffleWindow.setVisible(true);
    }
}

