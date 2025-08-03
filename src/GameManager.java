import java.util.List;
import java.util.Scanner;

public class GameManager {
    public void startGame() {
        System.out.println("Game Started!");

        // Create QuestionBank
        QuestionBank qb = new QuestionBank();

        // Get all questions
        List<Question> questions = qb.getAllQuestions();

        // Print each question for testing
        for (Question q : questions) {
            System.out.println("Sound file: " + q.getSoundFile());
            System.out.println("Options:");
            for (String opt : q.getOptions()) {
                System.out.println("- " + opt);
            }
            System.out.println("Correct Answer: " + q.getCorrectAnswer());
            System.out.println("---------------------------");
        }

        // Later you'll add game logic here
    }
}