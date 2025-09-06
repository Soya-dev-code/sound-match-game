import java.util.List;
import java.util.Scanner;

public class GameManager {
    private Scanner scanner;

    public GameManager() {
        scanner = new Scanner(System.in);
    }

    public void startGame() {
        System.out.println("🎵 Game Started! 🎵\n");

        // Create QuestionBank
        QuestionBank qb = new QuestionBank();

        // Get all questions
        List<Question> questions = qb.getAllQuestions();

        int score = 0;

        // Loop through questions
        for (Question q : questions) {
            System.out.println("Playing sound...");
            SoundPlayer.playSound(q.getSoundFile());  // <-- plays the sound

            // Print options
            String[] opts = q.getOptions();
            for (int i = 0; i < opts.length; i++) {
                System.out.println((i + 1) + ". " + opts[i]);
            }

            // Get user choice
            System.out.print("Your answer: ");
            int choice = scanner.nextInt();

            // Check answer
            if (opts[choice - 1].equalsIgnoreCase(q.getCorrectAnswer())) {
                System.out.println("✅ Correct!\n");
                score++;
            } else {
                System.out.println("❌ Wrong! Correct answer: " + q.getCorrectAnswer() + "\n");
            }
        }

        System.out.println("🎉 Game Over! Your score: " + score + "/" + questions.size());
    }
}