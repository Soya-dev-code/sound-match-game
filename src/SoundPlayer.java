package src;

import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.io.BufferedInputStream;

public class SoundPlayer {
    private Player player;
    private Thread playThread;

    // Play a sound asynchronously
    public void play(String filePath) {
        stop(); // stop any currently playing sound

        playThread = new Thread(() -> {
            try (FileInputStream fis = new FileInputStream(filePath);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {

                player = new Player(bis);
                player.play();

            } catch (Exception e) {
                System.out.println("Error playing sound: " + e.getMessage());
            }
        });
        playThread.start();
    }

    // Play a sound synchronously (blocking, useful for correct/wrong feedback)
    public void playSync(String filePath) {
        stop();
        try (FileInputStream fis = new FileInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            player = new Player(bis);
            player.play();

        } catch (Exception e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }

    // Stop any currently playing sound
    public void stop() {
        try {
            if (player != null) player.close();
            if (playThread != null && playThread.isAlive()) playThread.interrupt();
        } catch (Exception e) {
            System.out.println("Error stopping sound: " + e.getMessage());
        }
    }
}
