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

        JPanel scorePanel = new JPanel();
        JTextArea area = new JTextArea(createScoreString(score1, score2));
        area.setFont(new Font("Serif", Font.PLAIN, 15));
        area.setBounds(10, 30, 380, 200);
        area.setEditable(false);
        scorePanel.add(area);

        JPanel savePanel = new JPanel();
        savePanel.add(new JLabel("Enter Name: ", SwingConstants.LEFT));
        JTextArea name = new JTextArea("Yanan Bonte");
        JButton btn = new JButton("Save!");
        btn.addActionListener(actionEvent -> saveHighScore(score1, score2, name.getText()));
        savePanel.add(name);
        savePanel.add(btn);

        window.add(scorePanel, BorderLayout.CENTER);
        window.add(savePanel, BorderLayout.PAGE_END);
        window.setVisible(true);
    }

    private String createScoreString(int score1, int score2) {
        String winner;
        if (score1 > score2) winner = "Player 1 wins!";
        else if (score2 > score1) winner = "Player 2 wins!";
        else winner = "It's a draw!";
        return winner + "\n ------------------------------------------------------------------------------------ \n Score Player1: " + score1 + "\n Score Player 2: " + score2;
    }

    private void saveHighScore(int score1, int score2, String name) {
        int highestScore = Math.max(score1, score2);
        try {
            File file = new File("highscores.txt");
            FileWriter myWriter = new FileWriter(file, true);
            myWriter.write(name + "-" + highestScore + "\n");
            myWriter.close();
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


}
