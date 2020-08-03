import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class HighScore {

    class Person {
        private String name;
        private int score;

        public Person(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }

    public HighScore() {
        JFrame f = new JFrame("Highscores");
        f.setSize(400, 400);

        JTextArea area = new JTextArea(createHighScoreString(getScores()));
        area.setFont(new Font("Serif", Font.PLAIN, 15));
        area.setBounds(10, 30, 380, 350);
        area.setEditable(false);
        f.add(area);

        System.out.println(getScores().get(0).getScore());

        f.setVisible(true);
    }

    public String createHighScoreString(ArrayList<Person> persons) {
        persons.sort(Comparator.comparingInt(Person::getScore));
        StringBuilder highScoreString = new StringBuilder("Highscores \n ------------------------------------------------------------------------------------");
        for (Person person : persons)
            highScoreString.append("\n").append(person.getName()).append(": ").append(person.getScore());
        return highScoreString.toString();
    }

    private ArrayList<Person> getScores() {
        ArrayList<Person> persons = new ArrayList<>();

        try {
            File myObj = new File("highscores.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                int splitterIndex = data.indexOf("-");
                persons.add(new Person(data.substring(0, splitterIndex), parseInt((data.substring(splitterIndex + 1)))));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return persons;
    }
}
