import javax.swing.*;
import java.awt.*;

public class Menu {
    private int[] size;

    public Menu(int width, int height) {
        this.size = new int[]{width, height};
        JFrame f = new JFrame("Memory Game");
        f.setSize(width, height);
        JPanel mainPanel = new JPanel();

        mainPanel.add(createTitlePanel());
        mainPanel.add(createDifficultyPanel());
        mainPanel.add(createSettingsPanel());
        mainPanel.add(createSizePanel());
        mainPanel.add(createButtonPanel());
        mainPanel.setLayout(new GridLayout(5, 1));
        f.add(mainPanel);
        f.setVisible(true);
    }

    public JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        JLabel title = new JLabel("Epic Memory Game", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, getSize()[0] / 20));
        panel.add(title);
        return panel;
    }

    public JPanel createDifficultyPanel() {
        JPanel panel = new JPanel();
        ButtonGroup bg = new ButtonGroup();
        panel.add(new JLabel("Difficulty:", SwingConstants.LEFT));
        JRadioButton[] radioButtons = new JRadioButton[]{new JRadioButton("Peasant"),
                new JRadioButton("Noob"), new JRadioButton("Medium"), new JRadioButton("Hard")};
        for (var radioButton : radioButtons) {
            bg.add(radioButton);
            panel.add(radioButton);
        }
        return panel;
    }

    //TODO perhaps merge with difficulty panel
    public JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        ButtonGroup bg = new ButtonGroup();
        panel.add(new JLabel("Select your opponent:", SwingConstants.LEFT));
        JRadioButton[] radioButtons = new JRadioButton[]{new JRadioButton("Player"), new JRadioButton("Computer")};
        for (var radioButton : radioButtons) {
            bg.add(radioButton);
            panel.add(radioButton);
        }
        return panel;
    }

    public JPanel createSizePanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Rows:", SwingConstants.LEFT));
        SpinnerModel value = new SpinnerNumberModel(4, 0, 10, 1);
        JSpinner spinner = new JSpinner(value);
        panel.add(spinner);
        panel.add(new JLabel("Cols:", SwingConstants.LEFT));
        JSpinner spinner2 = new JSpinner(value);
        panel.add(spinner2);
        return panel;
    }

    public JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        JButton[] buttons = new JButton[] {new JButton("Change Theme"), new JButton("High Scores"), new JButton("Rules"), new JButton("Start")};

        for (var button: buttons){
            panel.add(button);
        }
        return panel;
    }

    public int[] getSize() {
        return size;
    }

    public static void main(String[] args) {
        new Menu(1000, 700);
    }
}

