package com.infiniterunner;

import java.io.InputStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * @author Tomasz Baslyk
 */

public class FxPlayer {
    InputStream music;
    AudioStream audios;

    public FxPlayer(String filepath) {
        try {
            music = getClass().getResourceAsStream(filepath);
            audios = new AudioStream(music);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void play() {

        AudioPlayer.player.start(audios);

    }

    public void stop() {

        AudioPlayer.player.stop(audios);
    }
}
