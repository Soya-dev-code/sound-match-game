import javax.sound.sampled.*;
import java.io.File;

public class Soundplayer {
    public static void playSound(String filePath) {
        try {
            File file = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

            Thread.sleep(clip.getMicrosecondLength() / 1000);
        } catch (Exception e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }
}