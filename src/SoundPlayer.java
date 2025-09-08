package src;

import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.io.BufferedInputStream;

public class SoundPlayer {

    private String filePath;

    public SoundPlayer(String filePath) {
        this.filePath = filePath;
    }

    // Play the sound asynchronously
    public void play() {
        new Thread(() -> {
            try (FileInputStream fis = new FileInputStream(filePath);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {

                Player player = new Player(bis);
                player.play();

            } catch (Exception e) {
                System.out.println("Error playing sound: " + e.getMessage());
            }
        }).start();
    }

    // Example usage
    public static void main(String[] args) {
        SoundPlayer correctSound = new SoundPlayer("sounds/correct.mp3");
        SoundPlayer wrongSound = new SoundPlayer("sounds/wrong.mp3");

        correctSound.play();  // plays first sound
        wrongSound.play();    // can play simultaneously
    }
}
