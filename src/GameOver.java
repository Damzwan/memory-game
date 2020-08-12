import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * The class GameOver is used to create the window after the game is finished and show the end scores.
 */
public class GameOver {
    private JFrame window;

    public GameOver(int score1, int score2) {
        this.window = new JFrame("Game Finished");
        window.setSize(600, 400);

        //A panel is created in the frame using a text area to show the scores of the different player in the game.
        JPanel scorePanel = new JPanel();
        //With this text area a high score can be set which can be seen in the menu when pushing the High Score button
        JTextArea area = new JTextArea(createScoreString(score1, score2));
        area.setFont(new Font("Serif", Font.PLAIN, 15));
        area.setBounds(10, 30, 380, 200);
        area.setEditable(false);
        scorePanel.add(area);

        //This panel gives the option create a save panel
        JPanel savePanel = new JPanel();
        savePanel.add(new JLabel("Enter Name: ", SwingConstants.LEFT));
        JTextArea name = new JTextArea("Yanan Bonte");
        JButton btn = new JButton("Save!");
        //Pressing the button sends an action to save the score
        btn.addActionListener(actionEvent -> saveHighScore(score1, score2, name.getText()));
        savePanel.add(name);
        savePanel.add(btn);

        window.add(scorePanel, BorderLayout.CENTER);
        window.add(savePanel, BorderLayout.PAGE_END);
        window.setVisible(true);
    }

    //Create a string that shows which player has won
    private String createScoreString(int score1, int score2) {
        String winner;
        if (score1 > score2) winner = "Player 1 wins!";
        else if (score2 > score1) winner = "Player 2 wins!";
        else winner = "It's a draw!";
        return winner + "\n ------------------------------------------------------------------------------------ \n Score Player1: " + score1 + "\n Score Player 2: " + score2;
    }

    //This method is executed when the Save button is clicked (action event).
    //The name and score are used as input. 
    private void saveHighScore(int score1, int score2, String name) {
    	//Retrieve the highest score of the two (the player with the lowest score cannot put his/her score into the file!
        int highestScore = Math.max(score1, score2);
        try {
        	//Create the file and FileWriter. Also when the file already exists, append do not create a new one (second argument of the FileWriter)
            File file = new File("highscores.txt"); 
            FileWriter myWriter = new FileWriter(file, true);
            myWriter.write(name + "-" + highestScore + "\n");
            //Close the writer and window (so the high score cannot be added twice
            myWriter.close();
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


}
