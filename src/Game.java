import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.*;

/*This class defines a Game, created by the class Menu and also the essence of the project.
 *A game has a difficulty, a mode (second player or computer), a theme (Christmas or animals) and a Dimension (rows and columns). 
 *All of these depend on what the user chooses in the menu.
 */
class Game {
    private int[] dim;
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

    //Hashmap computerMemory contains the turned cards saved by the computer. As soon as a card is drawn the computer will remember that this card has been drawn by remembering its id.
    //When the computer draws a card it will check whether it has the id of this card stored in its memory. If this is the case it will turn this card instead of a random one.
    private HashMap<Integer, Card> computerMemory = new HashMap<>();
    //????
    private HashMap<Difficulty, Double> chanceMap = new HashMap<>();

    /*Creating a new Game object, the constructor needs 3 variables. They are chosen in the Menu frame and saved as 
     * the global variables. A new JFrame is made for each new game containing a score panel and card panel.
     */
    Game(int[] dim, Mode mode, Difficulty difficulty, Theme theme) throws IllegalStateException {
        Board board = new Board(dim, theme, this);
        fillChanceMap();
        this.mode = mode;
        this.difficulty = difficulty;
        this.cards = board.getCards();
        this.dim = dim;

        //A new default frame is created with two panels created by two different methods.
        this.window = new JFrame("Game");
        window.setSize(windowWidth, windowHeight);
        window.add(createCardPanel());
        scorePanel = new JPanel();
        window.add(createScorePanel(), BorderLayout.NORTH);
        window.setVisible(true);
        System.out.print(scorePanel.getHeight());
    }

    //This method creates a new panel in which all cards are stored.
    private JPanel createCardPanel() {
        JPanel cardPanel = new JPanel();
        addCardsToPanel(cardPanel);
        cardPanel.setLayout(new GridLayout(dim[0], dim[1]));
        return cardPanel;
    }

    //This method creates a new scorePanel containing the scores of both players and whose turn it is both using labels.
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

    //The method adds the cards of the game to the current panel. It is a helper method of the method createCardpanel
    private void addCardsToPanel(JPanel panel) {
        for (Card card : cards) {
            panel.add(card);
        }
    }


    //The method turns the card upside down or downside up.
    void turnCard(Card card) {
    	
    	//This command saves the turned card into the HashMap computerMemory
        computerMemory.putIfAbsent(card.getID(), card);
        //This is to check if the player is allowed to turn a card.
        if ((timerrunning && isPlayer1) || card.getIcon() != null)
            return;
        card.turnCard();

        /*The card action is executed instantly if no cards have been turned yet in a turn. 
         *Then, there is a small waiting period to check if it is a match or not.
         */
        if (cardsToCompare.size() == 1) {
            int delay = 500;
            Timer timer = new Timer(delay, ex -> {
                timerrunning = false;
                actionHandler(card);
            });
            //To make sure the timer will only run once.
            timer.setRepeats(false);
            timerrunning = true;
            timer.start();
        } else actionHandler(card);
    }

    //ActionHandler checks whether the action should be executed using the id of the card
    private void actionHandler(Card card) {
        if (card.getID() == Board.BombID) updateScore(bombDamage);
        else if (card.getID() == Board.shuffleId) createPostShuffleWindow();
        else {
            cardsToCompare.add(card);
            if (cardsToCompare.size() == 2) isMatch(cardsToCompare.get(0), cardsToCompare.get(1));
        }
        if (isFinished()) endGame();
    }

    /*When playing against the computer, 2 non-special cards have to be drawn automatically. 
     *This function is responsible for drawing the first card. 
     *If a special card is turned this function will start again.
    */
    private void computerTurnFirstNormalCard() {
        Card card = getRandomCard();
        turnCard(card);

        //If the turned card is special, the function will restart after a small delay.
        if (card.getID() == Board.BombID || card.getID() == Board.shuffleId) {
        	//The timer makes sure there is a waiting period before starting again???
            Timer newTimer = new Timer(500, ex2 -> computerTurnFirstNormalCard());
            newTimer.setRepeats(false);//make sure the timer only runs once
            newTimer.start();
            return;
        }

        //If the turned card is normal, there is a waiting period
        Timer timer = new Timer(1000, ex -> {
            //If the computer does not know where the other card is it will turn a random card, executing this code.
            if (computerMemory.get(card.getID()) == null || (computerMemory.get(card.getID()) == card))
                computerTurnSecondNormalCard();
            else {
                //The computer knows where the other card is (depends on the difficulty level), this part of code will be executed.
                Random random = new Random();
                double r = random.nextDouble();

                //Depending on the selected difficulty the computer has a chance that it will ignore the fact that it knows the matching card
                //When put on easy the computer has a 66% chance of ignoring this. 33% on Medium and 0% on Hard
                if (r < chanceMap.get(difficulty)) turnCard(computerMemory.get(card.getID()));
                else computerTurnSecondNormalCard();
            }
        });
        
        //This command makes sure the timer only runs once.
        timer.setRepeats(false);
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
        // this command makes sure to keep choosing a random card until finding a card that has not been turned yet.
        while (cards.get(index).getIcon() != null)
            index = randomGenerator.nextInt(cards.size());
        return cards.get(index);
    }
    
    //This method checks if the two turned cards are a match via the getter method of ID and thus equal.
    private void isMatch(Card card1, Card card2) {
        cardsToCompare.clear();
        if (card1.getID() == card2.getID()) {
            updateScore(100);
            if (!isPlayer1 && !isFinished()) computerTurnFirstNormalCard();
        } else endTurn(card1, card2);
    }
    
    //At the end of each turn this method updates the score of the appropriate player if the two cards are a match.
    private void updateScore(int amount) {
        if (isPlayer1) scorePlayer1 += amount;
        else scorePlayer2 += amount;
        scorePlayer1Label.setText("Player P1: " + scorePlayer1);
        scorePlayer2Label.setText("Player P2: " + scorePlayer2);
    }
    
    //Method endTurn turns the cards that are not a match and switches from player via switchPlayer method.
    private void endTurn(Card card1, Card card2) {
        card1.turnCard();
        card2.turnCard();
        switchPlayer();
        if (!isPlayer1 && this.mode == Mode.COMPUTER) computerTurnFirstNormalCard();
    }
    
    //This method simply switches to the opposite player.
    private void switchPlayer() {
        isPlayer1 = !isPlayer1;
        playerTurn.setText(isPlayer1 ? "Player 1's Turn" : "Player 2's Turn");
    }

    //This private method checks if all cards are turned and the game is thus finished or if the other player still has to play.
    private boolean isFinished() {
        for (Card card : cards)
            if (card.getIcon() == null) return false;
        return true;
    }
    
    //Method endGame creates an object of class GameOver.
    private void endGame() {
        new GameOver(scorePlayer1, scorePlayer2);
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        postShuffleWindow.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }
    
    /*This method shuffles the cards which are not found yet, because the special card 'Cards' has been activated. 
     *First, the memory of the computer has been reset as the cards will now be in another order.
     *Secondly, the previous play window is closed, a new window is opened (same size) and the closed cards are shuffled.
     */
    private void createPostShuffleWindow() {
        computerMemory = new HashMap<>();
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
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
    
    //??? Chances the computer gets it right??? DAMIAN
    private void fillChanceMap() {
        int i = 1;
        for (Difficulty difficulty : Difficulty.values()) chanceMap.put(difficulty, i++ * 0.33);
    }
}

