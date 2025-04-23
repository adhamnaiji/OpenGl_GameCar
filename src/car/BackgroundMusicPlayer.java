package car;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class BackgroundMusicPlayer {

    private Clip clip;
    private boolean isPlaying;
    private boolean isPaused;
    private long clipTimePosition;

    public BackgroundMusicPlayer(String musicFile) {
        try {
            File soundFile = new File(musicFile);
            if (!soundFile.exists()) {
                System.err.println("Audio file not found: " + musicFile);
                return;  // Early exit if file is not found
            }
            
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();

            if (clip != null) {
                clip.open(audioStream);
                isPaused = false;
                isPlaying = false;
                clipTimePosition = 0;
            } else {
                System.err.println("Failed to load audio clip.");
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null && !isPlaying) {
            clip.setMicrosecondPosition(clipTimePosition); // Set position if paused
            clip.loop(Clip.LOOP_CONTINUOUSLY);  // Looping the music continuously
            clip.start();
            isPlaying = true;
            isPaused = false;
        } else if (clip == null) {
            System.err.println("Cannot play music because the clip is null.");
        }
    }

    public void stop() {
        if (clip != null && isPlaying) {
            clip.stop();
            isPlaying = false;
            clipTimePosition = 0;  // Reset position after stopping
        } else if (clip == null) {
            System.err.println("Cannot stop music because the clip is null.");
        }
    }

    public void pause() {
        if (clip != null && isPlaying && !isPaused) {
            clip.stop();
            clipTimePosition = clip.getMicrosecondPosition();
            isPaused = true;
            isPlaying = false;
        } else if (clip == null) {
            System.err.println("Cannot pause music because the clip is null.");
        }
    }

    public void resume() {
        if (clip != null && !isPlaying && isPaused) {
            clip.setMicrosecondPosition(clipTimePosition);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            isPlaying = true;
            isPaused = false;
        } else if (clip == null) {
            System.err.println("Cannot resume music because the clip is null.");
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
