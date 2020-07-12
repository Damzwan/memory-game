package Game;

import java.util.Set;

public class Game{
    int[] dim;
    String theme = "Some Theme";

    private String mode = "Some mode";
    private String difficulty;

    private static final Set<String> valid_difficulties = Set.of("Easy","Medium","Hard");
    private static final Set<String> valid_modes = Set.of("Easy","Medium","Hard");

    public Game(int[] dim, String theme, String mode, String difficulty)throws IllegalStateException{
        Board board = new Board(dim, theme);
        if(!(valid_difficulties.contains(difficulty)))throw new IllegalArgumentException("invalid difficulty");
        if(!(valid_modes.contains(mode))) throw new IllegalStateException("This is an Illegal mode");
        this.mode = mode;
        this.difficulty = difficulty;

    }



}
