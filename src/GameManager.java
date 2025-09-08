package src;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class GameManager {

    private Scanner scanner;
    private static final int QUESTIONS_PER_GAME = 10;

    public GameManager() {
        scanner = new Scanner(System.in);
    }

    public void startGame() {
        boolean playAgain = true;

        while (playAgain) {
            System.out.println("\n🎵 Welcome to the SOUND-MATCH GAME! 🎵\n");

            QuestionBank qb = new QuestionBank();
            List<Question> allQuestions = qb.getShuffledQuestions();

            // Limit number of questions
            List<Question> questions = allQuestions.size() > QUESTIONS_PER_GAME
                    ? allQuestions.subList(0, QUESTIONS_PER_GAME)
                    : allQuestions;

            int score = 0;

            for (Question q : questions) {
                System.out.println("Playing sound...");
                SoundPlayer player = new SoundPlayer("sounds/" + q.getSoundFile());
                player.play();

                // Shuffle options
                List<String> optsList = new ArrayList<>();
                Collections.addAll(optsList, q.getOptions());
                Collections.shuffle(optsList);

                // Display options
                for (int i = 0; i < optsList.size(); i++) {
                    System.out.println((i + 1) + ". " + optsList.get(i));
                }

                // Get user input
                int choice = 0;
                while (true) {
                    System.out.print("Your answer (1-" + optsList.size() + "): ");
                    if (scanner.hasNextInt()) {
                        choice = scanner.nextInt();
                        if (choice >= 1 && choice <= optsList.size()) break;
                        else System.out.println("Please enter a valid option number.");
                    } else {
                        System.out.println("Invalid input. Enter a number.");
                        scanner.next();
                    }
                }

                // ✅ Stop sound immediately after answering
                player.stop();

                // Check answer
                if (optsList.get(choice - 1).equalsIgnoreCase(q.getCorrectAnswer())) {
                    System.out.println("✅ Correct!\n");
                    score++;
                    new SoundPlayer("sounds/correct.mp3").play();
                } else {
                    System.out.println("❌ Wrong! Correct answer: " + q.getCorrectAnswer() + "\n");
                    new SoundPlayer("sounds/wrong.mp3").play();
                }

                // Small delay
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("🎉 Game Over! Your score: " + score + "/" + questions.size());

            // Ask if player wants to play again
            System.out.print("Do you want to play again? (Y/N): ");
            String response = scanner.next().trim().toUpperCase();
            playAgain = response.equals("Y");
        }

        System.out.println("Thanks for playing! 🎵");
    }

    public static void main(String[] args) {
        GameManager game = new GameManager();
        game.startGame();
    }
}