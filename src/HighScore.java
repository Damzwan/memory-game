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

    //create a string based on the collected highscores
    public String createHighScoreString(ArrayList<Person> persons) {
        persons.sort(Comparator.comparingInt(Person::getScore)); //sort the array of persons based on the score --> lowest score first
        Collections.reverse(persons); //reverse the array, we should start with the highest score first

        StringBuilder highScoreString = new StringBuilder("Highscores \n ------------------------------------------------------------------------------------");
        for (Person person : persons)
            highScoreString.append("\n").append(person.getName()).append(": ").append(person.getScore());
        return highScoreString.toString();
    }

    //read the highscores from the highscores text file
    private ArrayList<Person> getScores() {
        ArrayList<Person> persons = new ArrayList<>(); //An array of perons containing the name and score of each person

        try {
            File myObj = new File("highscores.txt"); //find the file
            Scanner myReader = new Scanner(myObj); //create a reader

            //as long as a line is available in the text file we add a person to the persons array
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine(); //a string containing the name and score of a person
                int splitterIndex = data.indexOf("-"); //find where the name ends and where the score starts
                persons.add(new Person(data.substring(0, splitterIndex), parseInt((data.substring(splitterIndex + 1)))));
            }
            myReader.close(); //close the reader
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return persons;
    }
}
