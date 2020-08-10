import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.*;

//This Class defines a Game, a game is created through the Menu and this is the essence of the project.
//A game contains a difficulty, a mode, a theme and a Dimension. All of these are provided depending on what
// chooses the user makes in the menu
class Game {
    private int[] dim;
    private String theme = "Some Theme";
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
    static int windowWidth = 1000;
    static int windowHeight = 700;
    public static int scorePanelHeight = 24;
    private JPanel scorePanel;
    private int bombDamage = -200;
    private HashMap<Integer, Card> computerMemory = new HashMap<>();
    private HashMap<Difficulty, Double> chanceMap = new HashMap<>();

    //the constructor uses the variables given by the menu and saves these in the global variables
    //a new Jframe is made for the current game, this frame contains a scorepanel and a cardpanel
    Game(int[] dim, Mode mode, Difficulty difficulty) throws IllegalStateException {
        Board board = new Board(dim, theme, this);
//        if(!(valid_difficulties.contains(difficulty)))throw new IllegalArgumentException("invalid difficulty");
//        if(!(valid_modes.contains(mode))) throw new IllegalStateException("This is an Illegal mode");
        fillChanceMap();
        this.mode = mode;
        this.difficulty = difficulty;
        this.cards = board.getCards();
        this.dim = dim;

        //a
        this.window = new JFrame("Game");
        window.setSize(windowWidth, windowHeight);
        window.add(createCardPanel());
        window.add(createScorePanel(), BorderLayout.NORTH);
        window.setVisible(true);
        scorePanel = new JPanel();
        System.out.print(scorePanel.getHeight());
    }

    //this method creeates a panel in which all cards are stored
    private JPanel createCardPanel() {
        JPanel cardPanel = new JPanel();
        addCardsToPanel(cardPanel);
        cardPanel.setLayout(new GridLayout(dim[0], dim[1]));
        return cardPanel;
    }

    //This method creates a scorePanel containing the scores of both players and who's turn it is.
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

    //This method adds the cards of the game to the current panel, this is a helper method of the method createCardpanel
    private void addCardsToPanel(JPanel panel) {
        for (Card card : cards) {
            panel.add(card);
        }
    }



    void turnCard(Card card) {

        computerMemory.putIfAbsent(card.getID(), card);
        if ((timerrunning && isPlayer1) || card.getIcon() != null) return;
        card.turnCard();

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


    private void actionHandler(Card card) {
        if (card.getID() == Board.BombID) updateScore(bombDamage);
        else if (card.getID() == Board.shuffleId) createPostShuffleWindow();
        else {
            cardsToCompare.add(card);
            if (cardsToCompare.size() == 2) isMatch(cardsToCompare.get(0), cardsToCompare.get(1));
        }
        if (isFinished()) endGame();
    }

    private void computerAction() {
        Card card = getRandomCard();
        turnCard(card);

        if (card.getID() == Board.BombID || card.getID() == Board.shuffleId) {
            Timer newTimer = new Timer(500, ex2 -> computerAction());
            newTimer.setRepeats(false);//make sure the timer only runs once
            newTimer.start();
            return;
        }

        Timer timer = new Timer(1000, ex -> {
            if (computerMemory.get(card.getID()) == null || (computerMemory.get(card.getID()) == card))
                secondComputerAction();
            else {
                Random random = new Random();
                double r = random.nextDouble();
                if (r < chanceMap.get(difficulty)) turnCard(computerMemory.get(card.getID()));
                else secondComputerAction(); //TODO merge with first if?
            }
        });

        timer.setRepeats(false);//make sure the timer only runs once
        timer.start();
    }

    private void secondComputerAction() {
        Card card = getRandomCard();
        turnCard(card);
        if (card.getID() == Board.BombID || card.getID() == Board.shuffleId) {
            Timer timer = new Timer(1000, ex -> secondComputerAction());
            timer.setRepeats(false);//make sure the timer only runs once
            timer.start();
        }
    }

    private Card getRandomCard() {
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(cards.size());
        while (cards.get(index).getIcon() != null) index = randomGenerator.nextInt(cards.size());
        return cards.get(index);
    }

    private void isMatch(Card card1, Card card2) {
        cardsToCompare.clear();
        if (card1.getID() == card2.getID()) {
            updateScore(100);
            if (!isPlayer1 && !isFinished()) computerAction();
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
        if (!isPlayer1 && this.mode == Mode.COMPUTER) computerAction();
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

