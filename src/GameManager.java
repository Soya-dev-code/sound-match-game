package src;
import java.util.*;

public class GameManager {

    private Scanner scanner;
    private static final int QUESTIONS_PER_GAME = 10;
    private SoundPlayer soundPlayer = new SoundPlayer(); // single global player

    public GameManager() {
        scanner = new Scanner(System.in);
    }

    public void startGame() {
        boolean playAgain = true;

        while (playAgain) {
            System.out.println("\n🎵 Welcome to the SOUND-MATCH GAME! 🎵\n");

            QuestionBank qb = new QuestionBank();
            List<Question> allQuestions = qb.getShuffledQuestions();

            List<Question> questions = allQuestions.size() > QUESTIONS_PER_GAME
                    ? allQuestions.subList(0, QUESTIONS_PER_GAME)
                    : allQuestions;

            int score = 0;

            for (Question q : questions) {
                System.out.println("Playing sound... (enter 0 to skip)");
                soundPlayer.play("sounds/" + q.getSoundFile());

                // Shuffle options
                List<String> optsList = new ArrayList<>();
                Collections.addAll(optsList, q.getOptions());
                Collections.shuffle(optsList);

                // Display options
                for (int i = 0; i < optsList.size(); i++) {
                    System.out.println((i + 1) + ". " + optsList.get(i));
                }

                // Get user input
                int choice = -1;
                while (true) {
                    System.out.print("Your answer (1-" + optsList.size() + ", 0 to skip): ");
                    if (scanner.hasNextInt()) {
                        choice = scanner.nextInt();
                        if (choice == 0) {
                            soundPlayer.stop(); // stop current sound
                            System.out.println("⏭ Skipped!\n");
                            break;
                        } else if (choice >= 1 && choice <= optsList.size()) {
                            soundPlayer.stop(); // stop main sound after answering
                            // Check answer
                            if (optsList.get(choice - 1).equalsIgnoreCase(q.getCorrectAnswer())) {
                                System.out.println("✅ Correct!\n");
                                score++;
                                soundPlayer.playSync("sounds/correct.mp3");
                            } else {
                                System.out.println("❌ Wrong! Correct answer: " + q.getCorrectAnswer() + "\n");
                                soundPlayer.playSync("sounds/wrong.mp3");
                            }
                            break;
                        } else {
                            System.out.println("Please enter a valid option number.");
                        }
                    } else {
                        System.out.println("Invalid input. Enter a number.");
                        scanner.next();
                    }
                }
            }

            System.out.println("🎉 Game Over! Your score: " + score + "/" + questions.size());

            System.out.print("Do you want to play again? (Y/N): ");
            String response = scanner.next().trim().toUpperCase();
            playAgain = response.equals("Y");
        }

        System.out.println("Thanks for playing! 🎵");
    }

    public static void main(String[] args) {
        new GameManager().startGame();
    }
}