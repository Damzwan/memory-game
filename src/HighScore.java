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
	
	//This is a nested class Person.
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

    //A string is created based on the collected high scores which are put into a text file as well. The array is sorted based on the scores.
    //The lowest scores are on top, so a next command reverses this way making the highest score come first and then the lower ones.
    public String createHighScoreString(ArrayList<Person> persons) {
        persons.sort(Comparator.comparingInt(Person::getScore));
        Collections.reverse(persons); //reverse the array, we should start with the highest score first

        StringBuilder highScoreString = new StringBuilder("   Highscores \n   ------------------------------------------------------------------------------------");
        for (Person person : persons)
            highScoreString.append("\n   ").append(person.getName()).append(": ").append(person.getScore());
        return highScoreString.toString();
    }

    //All of the high scores are read from the text file via the Person Array List.
    private ArrayList<Person> getScores() {
        ArrayList<Person> persons = new ArrayList<>(); 

        try {
        	//The file is selected and then a reader is created to read the file
            File myObj = new File("highscores.txt");
            Scanner myReader = new Scanner(myObj);

            //This constraint checks for a next available line and adds the variable data to this line. Data contains the name and score of the person. 
            //The splitter is used to separate the name and score of the person, which is then added to the person Array. Then the reader is closed.
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
