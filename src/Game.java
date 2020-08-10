import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.*;

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
    public static int windowWidth = 1000;
    public static int windowHeight = 700;
    public static int scorePanelHeight = 24;
    private JPanel scorePanel = new JPanel();
    private int bombDamage = -200;

    //The memory of the computer. As soon as a card is drawn the computer will remember that this card has been drawn by remembering its id.
    // When the computer draws a card it will check whether it has the id of this card stored in its memory. If this is the case it will turn this card instead of a random one
    private HashMap<Integer, Card> computerMemory = new HashMap<>();
    private HashMap<Difficulty, Double> chanceMap = new HashMap<>();


    Game(int[] dim, Mode mode, Difficulty difficulty) throws IllegalStateException {
        Board board = new Board(dim, theme, this);
//        if(!(valid_difficulties.contains(difficulty)))throw new IllegalArgumentException("invalid difficulty");
//        if(!(valid_modes.contains(mode))) throw new IllegalStateException("This is an Illegal mode");
        fillChanceMap();
        this.mode = mode;
        this.difficulty = difficulty;
        this.cards = board.getCards();
        this.dim = dim;

        this.window = new JFrame("Game");
        window.setSize(windowWidth, windowHeight);
        window.add(createCardPanel());
        window.add(createScorePanel(), BorderLayout.NORTH);
        window.setVisible(true);
        System.out.print(scorePanel.getHeight());
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
        scorePanel.setSize(windowWidth, scorePanelHeight);
        return scorePanel;
    }

    private void addCardsToPanel(JPanel panel) {
        for (Card card : cards) {

            panel.add(card);
        }
    }

    public void turnCard(Card card) {

        computerMemory.putIfAbsent(card.getID(), card); //save the turned card into the memory of the computer
        if ((timerrunning && isPlayer1) || card.getIcon() != null)
            return; //check whether we are eligible to turn a card
        card.turnCard();

        // We execute a card action instantly if no cards have been turned yet in a turn. After that we wait a small amount so that the player can see whether the second card is a match
        if (cardsToCompare.size() == 1) {
            int delay = 500;
            Timer timer = new Timer(delay, ex -> {
                timerrunning = false;
                actionHandler(card);
            });
            timer.setRepeats(false);//make sure the timer only runs once
            timerrunning = true;
            timer.start();
        } else actionHandler(card);
    }

    //Check what action should be executed depending on the id of the card
    private void actionHandler(Card card) {
        if (card.getID() == Board.BombID) updateScore(bombDamage);
        else if (card.getID() == Board.shuffleId) createPostShuffleWindow();
        else {
            cardsToCompare.add(card);
            if (cardsToCompare.size() == 2) isMatch(cardsToCompare.get(0), cardsToCompare.get(1));
        }
        if (isFinished()) endGame();
    }

    //The computer must draw 2 non-special cards in total. This function is responsible for drawing the first card
    // If a special card is turned this function should start again
    private void computerTurnFirstNormalCard() {
        Card card = getRandomCard();
        turnCard(card);

        //The turned card is special, we will restart this function after a small delay
        if (card.getID() == Board.BombID || card.getID() == Board.shuffleId) {
            Timer newTimer = new Timer(500, ex2 -> computerTurnFirstNormalCard()); //wait a small amount before starting again
            newTimer.setRepeats(false);//make sure the timer only runs once
            newTimer.start();
            return;
        }

        //The turned card is a normal one, wait a bit
        Timer timer = new Timer(1000, ex -> {
            //The computer does not know where the other card is, it will turn a random one again
            if (computerMemory.get(card.getID()) == null || (computerMemory.get(card.getID()) == card))
                computerTurnSecondNormalCard();
            else {
                //The computer knows where the other card is
                Random random = new Random();
                double r = random.nextDouble();

                //Depending on the selected difficulty the computer has a chance that it will ignore the fact that it knows the matching card
                //When put on easy the computer has a 66% chance of ignoring this. 33% on Medium and 0% on Hard
                if (r < chanceMap.get(difficulty)) turnCard(computerMemory.get(card.getID()));
                else computerTurnSecondNormalCard(); //TODO merge with first if?
            }
        });

        timer.setRepeats(false);//make sure the timer only runs once
        timer.start();
    }

    //The computer must draw 2 non-special cards in total. This function is responsible for drawing the second card
    // If a special card is turned this function should start again
    private void computerTurnSecondNormalCard() {
        Card card = getRandomCard();
        turnCard(card);

        //The turned card is special, we will restart this function after a small delay
        if (card.getID() == Board.BombID || card.getID() == Board.shuffleId) {
            Timer timer = new Timer(1000, ex -> computerTurnSecondNormalCard());
            timer.setRepeats(false);//make sure the timer only runs once
            timer.start();
        }
    }

    //Get a random non-turned card out of the cards array list
    private Card getRandomCard() {
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(cards.size());
        while (cards.get(index).getIcon() != null)
            index = randomGenerator.nextInt(cards.size()); //keep choosing a random card until we find a card that has not been turned yet
        return cards.get(index);
    }

    private void isMatch(Card card1, Card card2) {
        cardsToCompare.clear();
        if (card1.getID() == card2.getID()) {
            updateScore(100);
            if (!isPlayer1 && !isFinished()) computerTurnFirstNormalCard();
        } else endTurn(card1, card2);
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
        switchPlayer();
        if (!isPlayer1 && this.mode == Mode.COMPUTER) computerTurnFirstNormalCard();
    }

    private void switchPlayer() {
        isPlayer1 = !isPlayer1;
        playerTurn.setText(isPlayer1 ? "Player 1's Turn" : "Player 2's Turn");
    }

    private boolean isFinished() {
        for (Card card : cards)
            if (card.getIcon() == null) return false;
        return true;
    }

    private void endGame() {
        new GameOver(scorePlayer1, scorePlayer2);
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        postShuffleWindow.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }

    private void createPostShuffleWindow() {
        computerMemory = new HashMap<>(); //reset the memory of the computer
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING)); //Close the previous window
        this.postShuffleWindow = new JFrame("Post-Shuffle");
        postShuffleWindow.setSize(windowWidth, windowHeight);
        JPanel mainPanel = new JPanel();
        Collections.shuffle(cards);
        addCardsToPanel(mainPanel);
        mainPanel.setLayout(new GridLayout(dim[0], dim[1]));
        postShuffleWindow.add(mainPanel);
        postShuffleWindow.add(scorePanel, BorderLayout.NORTH);
        postShuffleWindow.setVisible(true);
    }

    private void fillChanceMap() {
        int i = 1;
        for (Difficulty difficulty : Difficulty.values()) chanceMap.put(difficulty, i++ * 0.33);
    }
}

