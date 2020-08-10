import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Menu {
    private int[] size;
    private Difficulty selectedDifficulty;
    private Mode mode;
    private Theme theme;
    private int[] gameSize = new int[]{2, 2};
    private static int height = 700;
    private static int width = 1000;

    //a Linked Hashmap that is being used to link a function to every button. At the creation of a button, we fill the label with the key and the action listener with the value
    //This way prevents code duplication and allows us to easily extend the functionality of our game
    private LinkedHashMap<String, ButtonMethod> buttonMethodMap = new LinkedHashMap<>();


    public static void main(String[] args) {
        new Menu(width, height);
    }


    public Menu(int width, int height) {
        this.size = new int[]{width, height};

        // set initial settings
        setSelectedDifficulty(Difficulty.EASY);
        setMode(Mode.COMPUTER);

        //create Window
        JFrame f = new JFrame("Memory Game");
        f.setSize(width, height);
        JPanel mainPanel = new JPanel();

        //Fill Window with components
        fillButtonMethodMap();
        mainPanel.add(createTitlePanel());
        mainPanel.add(createDifficultyPanel());
        mainPanel.add(createModePanel());
        mainPanel.add(createThemePanel());
        mainPanel.add(createSizePanel());
        mainPanel.add(createButtonPanel());


        mainPanel.setLayout(new GridLayout(6, 1));
        f.add(mainPanel);
        f.setVisible(true); //make the window visible
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
        panel.add(new JLabel("Difficulty:", SwingConstants.LEFT));
        ButtonGroup grp = new ButtonGroup(); //We make use of a button group so that only one Button can be selected

        for (Difficulty difficulty : Difficulty.values()) {
            JRadioButton btn = new JRadioButton(difficulty.getName()); //Create the RadioButton with as title the name of the difficulty
            btn.addActionListener(actionEvent -> setSelectedDifficulty(difficulty)); //add an onclick action listener

            grp.add(btn); //add the button to the button group
            panel.add(btn); //add it to the panel to make it visible
            if (difficulty == Difficulty.EASY) btn.setSelected(true);
        }
        return panel;
    }

    //Same as createDifficultyPanel() --> TODO perhaps generalize for cleaner code
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

    //Same as createDifficultyPanel() --> TODO perhaps generalize for cleaner code
    public JPanel createModePanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Select your opponent:", SwingConstants.LEFT));
        ButtonGroup grp = new ButtonGroup();

        for (Mode mode : Mode.values()) {
            JRadioButton btn = new JRadioButton(mode.getName());
            btn.addActionListener(actionEvent -> setMode(mode));
            grp.add(btn);
            panel.add(btn);
            if (mode == Mode.COMPUTER) btn.setSelected(true);
        }

        return panel;
    }

    public JPanel createSizePanel() {
        JPanel panel = new JPanel();
        SpinnerModel rowValue = new SpinnerNumberModel(3, 1, 4, 1); //all possible row values
        SpinnerModel colValue = new SpinnerNumberModel(4, 1, 4, 1); //all possible column values

        //add a row spinner
        panel.add(new JLabel("Rows:", SwingConstants.LEFT));
        JSpinner rowSpinner = new JSpinner(rowValue);
        rowSpinner.addChangeListener(changeEvent -> setGameSize((Integer) rowSpinner.getValue(), gameSize[1]));
        panel.add(rowSpinner);

        //add a column spinner
        panel.add(new JLabel("Cols:", SwingConstants.LEFT));
        JSpinner colSpinner = new JSpinner(colValue);
        colSpinner.addChangeListener(changeEvent -> setGameSize(gameSize[0], (Integer) colSpinner.getValue()));
        setGameSize((Integer) rowSpinner.getValue(), (Integer) colSpinner.getValue());

        panel.add(colSpinner);
        return panel;
    }

    public JPanel createButtonPanel() {
        JPanel panel = new JPanel();

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

        //small check to see if the game size is valid
        if (gameSize[0] * gameSize[1] % 2 != 0) {
            System.out.println("invalid game size");
            return;
        }
        new Game(gameSize, mode, selectedDifficulty); //The game size is valid, we create a new game instance
    }

    //an interface that is being used for the buttonMethodMap field to link an action to a string
    interface ButtonMethod {
        void execute();
    }
}

