package Game.Multiplayer.MainLogicGame;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Клас - перерахувальник, що представляє собою типи музики
 * й прогірує їх
 * @author Vitalik
 */
public enum Music {
    SHOT,
    HIT,
    WINER,
    LOSE,
    FON_MENU,
    FON;

    /**
     * Зміна типу Sound для відторення музики
     */
    public static Sound s;
    /**
     * Змінна типу boolean для визначення, чи відігрувати музику
     */
    public static boolean playSound = true;
   
    /**
     * Метод, що запускає музику по назві
     * @param nameMusic Імя треку
     */
    public static void playMusic(String nameMusic) {
        if(playSound){
             s  = Sound.playSound("res\\sound\\" + nameMusic.toLowerCase() + ".wav");
        }
    }
    
    /**
     * Метод, що зупиняє відтворення музики
     */
    public static void stopPlayMusic() {
        if(playSound){
            s.stop();
            s.close();
        }
    }

    /**
     * Клас, що відтворює треки
     */
    static class Sound implements AutoCloseable {

        private boolean released = false;
        private AudioInputStream stream = null;
        private Clip clip = null;
        private FloatControl volumeControl = null;
        private boolean playing = false;

        /**
         * Констуркот класу play
         * @param f  файл, що потрібно відтворювати
         */
        public Sound(File f) {
            try {
                stream = AudioSystem.getAudioInputStream(f);
                clip = AudioSystem.getClip();
                clip.open(stream);
                clip.addLineListener(new Listener());
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                released = true;
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
                exc.printStackTrace();
                released = false;
                close();
            }
        }

        /**
         * метод, що повертає чи відтворюєся трек
         * @return повертає істину якщо трек відтворюєся
         */
        public boolean isPlaying() {
            return playing;
        }

        /**
         * Метод, що відтворює трек
         * @param breakOld чи відтворюваюся трек
         */
        public void play(boolean breakOld) {
            if (released) {
                if (breakOld) {
                    clip.stop();
                    clip.setFramePosition(0);
                    clip.start();
                    playing = true;
                } else if (!isPlaying()) {
                    clip.setFramePosition(0);
                    clip.start();
                    playing = true;
                }
            }
        }


        /**
         * Метод, що зупиняє відтворення треку
         */
        public void stop() {
            if (playing) {
                clip.stop();
            }
        }

        /**
         * Метод, що закриває потік із відтворення треку
         */
        public void close() {
            if (clip != null) {
                clip.close();
            }

            if (stream != null)
			try {
                stream.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }

        /**
         * Метод, що задає голос вітворення музики
         * @param x 
         */
        public void setVolume(float x) {
            if (x < 0) {
                x = 0;
            }
            if (x > 1) {
                x = 1;
            }
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            volumeControl.setValue((max - min) * x + min);
        }

        /**
         * Метод, що запускає відтворення треку
         * @param path шлях, до файлу
         * @return  екземпляр даного класу
         */
        public static Sound playSound(String path) {
            File f = new File(path);
            Sound snd = new Sound(f);
            snd.setVolume(0.7f);
            snd.play(true);
            return snd;
        }

        private class Listener implements LineListener {

            public void update(LineEvent ev) {
                if (ev.getType() == LineEvent.Type.STOP) {
                    playing = false;
                    synchronized (clip) {
                        clip.notify();
                    }
                }
            }
        }
    }
}
