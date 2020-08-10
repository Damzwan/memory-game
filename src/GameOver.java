import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GameOver {
    private JFrame window;

    public GameOver(int score1, int score2) {
        this.window = new JFrame("Game Finished");
        window.setSize(400, 400);

        //create the textarea responsible for showing the highscores
        JPanel scorePanel = new JPanel();
        JTextArea area = new JTextArea(createScoreString(score1, score2)); //create a highscore string and set the textarea value to this string
        area.setFont(new Font("Serif", Font.PLAIN, 15));
        area.setBounds(10, 30, 380, 200);
        area.setEditable(false);
        scorePanel.add(area);

        //create a save panel
        JPanel savePanel = new JPanel();
        savePanel.add(new JLabel("Enter Name: ", SwingConstants.LEFT));
        JTextArea name = new JTextArea("Yanan Bonte");
        JButton btn = new JButton("Save!");
        btn.addActionListener(actionEvent -> saveHighScore(score1, score2, name.getText())); //when the button is pressed we should save the score
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

    //write the highscore to the highscores file
    private void saveHighScore(int score1, int score2, String name) {
        int highestScore = Math.max(score1, score2); //get the highest score
        try {
            File file = new File("highscores.txt"); //get the file
            FileWriter myWriter = new FileWriter(file, true); //create a filewriter, the second argument is used so that we append to a file instead of starting a new one
            myWriter.write(name + "-" + highestScore + "\n"); //write the string
            myWriter.close(); //close the writer
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING)); //close the window
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


}
