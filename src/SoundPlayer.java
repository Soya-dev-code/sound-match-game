
package src;

import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.io.BufferedInputStream;

public class SoundPlayer {

    private String filePath;
    private Player player;
    private Thread playThread;

    public SoundPlayer(String filePath) {
        this.filePath = filePath;
    }

    // Play the sound asynchronously
    public void play() {
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

    // Stop the sound if it's still playing
    public void stop() {
        try {
            if (player != null) {
                player.close(); // this stops playback
            }
            if (playThread != null && playThread.isAlive()) {
                playThread.interrupt();
            }
        } catch (Exception e) {
            System.out.println("Error stopping sound: " + e.getMessage());
        }
    }
}
