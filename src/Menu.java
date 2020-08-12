import javax.swing.*;
import java.awt.*;
import java.io.IOException;
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

    /*A Linked Hashmap is used to link a method to every button. At the creation of a button, the label is filled with the key and the action listener with the value
     *This way a duplication of the code is prevented which allows for an easy and functional extension of the game. 
     */
    private LinkedHashMap<String, ButtonMethod> buttonMethodMap = new LinkedHashMap<>();

    
    //The main method creates a new object of this class by executing the constructor method for a chosen width and height.
    public static void main(String[] args) {
		new Menu(width, height);
    }

    //This constructor uses the class variables width and height to create a frame which is the menu of the game when executing the code.
    public Menu(int width, int height) {
        this.size = new int[]{width, height};

        //Difficulty is set by default on easy and the mode to computer which is the default opponent. 
        setSelectedDifficulty(Difficulty.EASY);
        setMode(Mode.COMPUTER);
        setTheme(Theme.CHRISTMAS);

        //A new frame is created with window name Memory game with a certain size. Also, a general panel is set up where all
        //other panels will be added to.
        JFrame f = new JFrame("Memory Game");
        f.setSize(width, height);
        JPanel mainPanel = new JPanel();

        //The frame has different components which are created by different methods to create all smaller panels.
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
    
    //This method creates the first panel with the title of the game using a JLabel.
    public JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        JLabel title = new JLabel("Memory Game", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, getSize()[0] / 20));
        panel.add(title);
        return panel;
    }
    
    //The second panel gives the choice of difficulty. A button group is used so only one choice can be made.
    public JPanel createDifficultyPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Difficulty:", SwingConstants.LEFT));
        ButtonGroup grp = new ButtonGroup();

        //For each different difficulty a button is created with an action listener to set the chosen difficulty and added to the button group.
        for (Difficulty difficulty : Difficulty.values()) {
            JRadioButton btn = new JRadioButton(difficulty.getName());
            btn.addActionListener(actionEvent -> setSelectedDifficulty(difficulty));           
            grp.add(btn); 
            panel.add(btn); 
            //This command selects the default option easy, so a game can be started immediately after clicking start.
            if (difficulty == Difficulty.EASY) btn.setSelected(true);
        }
        return panel;
    }

    //This is the same as for createDifficultyPanel aside from choosing the theme instead of the difficulty.
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
            if (theme == Theme.CHRISTMAS) btn.setSelected(true);
        }
        return panel;
    }

    //Similar to createDifficultyPanel and createThemePanel.
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
    
    //Method to create a panel with two spinners to choose the number of rows and columns of the game. Default values have been added:
    //3 rows and 4 columns. 
    public JPanel createSizePanel() {
        JPanel panel = new JPanel();
        //Possible row and column values
        SpinnerModel rowValue = new SpinnerNumberModel(3, 1, 4, 1);
        SpinnerModel colValue = new SpinnerNumberModel(4, 1, 4, 1);
        
        panel.add(new JLabel("Rows:", SwingConstants.LEFT));
        JSpinner rowSpinner = new JSpinner(rowValue);
        rowSpinner.addChangeListener(changeEvent -> setGameSize((Integer) rowSpinner.getValue(), gameSize[1]));
        panel.add(rowSpinner);
        
        //Similar as to the row spinner, a spinner is created to choose the number of columns.
        panel.add(new JLabel("Columns:", SwingConstants.LEFT));
        JSpinner colSpinner = new JSpinner(colValue);
        colSpinner.addChangeListener(changeEvent -> setGameSize(gameSize[0], (Integer) colSpinner.getValue()));
        setGameSize((Integer) rowSpinner.getValue(), (Integer) colSpinner.getValue());
        panel.add(colSpinner);
        return panel;
    }

    //This method contains the panel for the ?????????????? Method creates the game?
    public JPanel createButtonPanel() {
        JPanel panel = new JPanel();

        for (Map.Entry<String, ButtonMethod> entry : buttonMethodMap.entrySet()) {
            JButton btn = new JButton(entry.getKey());
            btn.addActionListener(actionEvent -> entry.getValue().execute());
            panel.add(btn);
        }
        return panel;
    }

    //This method is the last panel in the menu and gives the option to see the high scores, rules or start the game via a button. 
    private void fillButtonMethodMap() {
        buttonMethodMap.put("High Scores", this::showHighScores);
        buttonMethodMap.put("Rules", () -> {
			try {
				showRules();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
        buttonMethodMap.put("Start", this::startGame);
    }
    
    
    //This method creates a new object of the class HighScore
    private void showHighScores() {
        new HighScore();
    }
    
    //The method showRules creates a new object to show a frame holding the rules.
    private void showRules() throws IOException {
        new Rules();
    }
    
    //This method makes sure the dimension of the game is valid so it is not possible to enter 3 rows and 3 columns and start a game.
    //If the game size is valid, a new Game object is created and three variables size, mode and difficulty are sent to the constructor.
    private void startGame() {
        if (gameSize[0] * gameSize[1] % 2 != 0) {
            System.out.println("invalid game size");
            return;
        }
        new Game(gameSize, mode, selectedDifficulty, theme);
    }

    //This interface is used for the buttonMethodMap field to link an action to a string
    //?????????????????????????
    interface ButtonMethod {
        void execute();
    }
    
    //Getter method for size
    public int[] getSize() {
        return size;
    }   

    //These are the different setters for the size, difficulty, mode and theme.
    private void setSelectedDifficulty(Difficulty selectedDifficulty) {
        this.selectedDifficulty = selectedDifficulty;
    }

    private void setMode(Mode mode) {
        this.mode = mode;
    }

    private void setTheme(Theme theme){
        this.theme = theme;
    }
    

    public void setGameSize(int rows, int cols) {
        gameSize = new int[]{rows, cols};
    }
    
}

