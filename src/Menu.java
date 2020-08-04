import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Menu {
    private int[] size;
    private LinkedHashMap<String, ButtonMethod> buttonMethodMap = new LinkedHashMap<>();
    private Difficulty selectedDifficulty;
    private Mode mode;
    private Theme theme;
    private int[] gameSize = new int[]{2, 2};
    private static int height = 700;
    private static int width = 1000;

    public static void main(String[] args) {
        new Menu(width, height);
    }


    public Menu(int width, int height) {
        this.size = new int[]{width, height};
        JFrame f = new JFrame("Memory Game");
        f.setSize(width, height);
        JPanel mainPanel = new JPanel();

        fillButtonMethodMap();
        mainPanel.add(createTitlePanel());
        mainPanel.add(createDifficultyPanel());
        mainPanel.add(createModePanel());
        mainPanel.add(createThemePanel());
        mainPanel.add(createSizePanel());
        mainPanel.add(createButtonPanel());
        mainPanel.setLayout(new GridLayout(6, 1));
        f.add(mainPanel);
        f.setVisible(true);
    }

    public JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setSize(width, height/6);
        JLabel title = new JLabel("Epic Memory Game", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, getSize()[0] / 20));
        panel.add(title);
        return panel;
    }

    public JPanel createDifficultyPanel() {
        JPanel panel = new JPanel();
        panel.setSize(width, height/7);
        panel.add(new JLabel("Difficulty:", SwingConstants.LEFT));
        ButtonGroup grp = new ButtonGroup();

        for (Difficulty difficulty : Difficulty.values()) {
            JRadioButton btn = new JRadioButton(difficulty.getName());
            btn.addActionListener(actionEvent -> setSelectedDifficulty(difficulty));
            grp.add(btn);
            panel.add(btn);
        }

        return panel;
    }

    public JPanel createThemePanel(){
        JPanel panel = new JPanel();
        panel.setSize(width, height/7);
        panel.add(new JLabel("Select your theme:", SwingConstants.LEFT));
        ButtonGroup grp = new ButtonGroup();

        for (Theme theme : Theme.values()) {
            JRadioButton btn = new JRadioButton(theme.getName());
            btn.addActionListener(actionEvent -> setTheme(theme));
            grp.add(btn);
            panel.add(btn);
        }
        return panel;
    }

    //TODO perhaps merge with difficulty panel
    public JPanel createModePanel() {
        JPanel panel = new JPanel();
        panel.setSize(width, height/7);
        panel.add(new JLabel("Select your opponent:", SwingConstants.LEFT));
        ButtonGroup grp = new ButtonGroup();

        for (Mode mode : Mode.values()) {
            JRadioButton btn = new JRadioButton(mode.getName());
            btn.addActionListener(actionEvent -> setMode(mode));
            grp.add(btn);
            panel.add(btn);
        }

        return panel;
    }

    public JPanel createSizePanel() {
        JPanel panel = new JPanel();
        panel.setSize(width, height/7);
        SpinnerModel rowValue = new SpinnerNumberModel(2, 1, 4, 1);
        SpinnerModel colValue = new SpinnerNumberModel(2, 1, 4, 1);

        //TODO code duplication
        panel.add(new JLabel("Rows:", SwingConstants.LEFT));
        JSpinner rowSpinner = new JSpinner(rowValue);
        rowSpinner.addChangeListener(changeEvent -> setGameSize((Integer) rowSpinner.getValue(), gameSize[1]));
        panel.add(rowSpinner);

        panel.add(new JLabel("Cols:", SwingConstants.LEFT));
        JSpinner colSpinner = new JSpinner(colValue);
        colSpinner.addChangeListener(changeEvent -> setGameSize(gameSize[0], (Integer) colSpinner.getValue()));
        panel.add(colSpinner);
        return panel;
    }

    public JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setSize(width, height/7);

        for (Map.Entry<String, ButtonMethod> entry : buttonMethodMap.entrySet()) {
            JButton btn = new JButton(entry.getKey());
            btn.addActionListener(actionEvent -> entry.getValue().execute());
            panel.add(btn);
        }
        return panel;
    }

    public int[] getSize() {
        return size;
    }

    private void fillButtonMethodMap() {
        buttonMethodMap.put("High Scores", this::showHighScores);
        buttonMethodMap.put("Rules", this::showRules);
        buttonMethodMap.put("Start", this::startGame);
    }

    private void setSelectedDifficulty(Difficulty selectedDifficulty) {
        this.selectedDifficulty = selectedDifficulty;
        System.out.println(this.selectedDifficulty);
    }

    private void setMode(Mode mode) {
        this.mode = mode;
    }

    private void setTheme(Theme theme){
        this.theme = theme;
    }

    private void changeTheme() {
        System.out.println("should change theme");
    }

    public void setGameSize(int rows, int cols) {
        gameSize = new int[]{rows, cols};
    }

    private void showHighScores() {
        new HighScore();
    }

    private void showRules() {
        new Rules();
    }

    private void startGame() {
        if (gameSize[0] * gameSize[1] % 2 != 0) {
            System.out.println("invalid game size");
            return;
        }
        System.out.println("should start game");
        Game game = new Game(gameSize, mode, selectedDifficulty);
    }

    interface ButtonMethod {
        void execute();
    }
}

