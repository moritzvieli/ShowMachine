package com.ascargon.rocketshow.audio;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;

public class AudioFile extends com.ascargon.rocketshow.composition.File {

    final static Logger logger = Logger.getLogger(AudioFile.class);

    public final static String AUDIO_PATH = "audio/";

    private boolean isSample = false;

    private AlsaPlayer alsaPlayer;

    private String outputBus;

    @XmlTransient
    public String getPath() {
        return Manager.BASE_PATH + MEDIA_PATH + AUDIO_PATH + getName();
    }

    public void load(boolean isSample) throws Exception {
        logger.debug("Loading file '" + this.getName() + "...");

        this.isSample = isSample;

        // Play samples with alsa, because it's important to play it faster but
        // sync is less important
        // TODO Make this setting configurable
        if (isSample) {
            alsaPlayer = new AlsaPlayer();
        }
    }

    @XmlTransient
    public int getFullOffsetMillis() {
        return this.getOffsetMillis() + this.getManager().getSettings().getOffsetMillisAudio();
    }

    public void play() throws Exception {
        String path = getPath();

        if (isSample) {
            alsaPlayer.play(this.getPath(), this.getManager().getSettings().getAlsaDeviceFromOutputBus(outputBus));
        }
    }

    public void close() throws Exception {
        if (alsaPlayer != null) {
            alsaPlayer.close();
            alsaPlayer = null;
        }
    }

    public String getOutputBus() {
        return outputBus;
    }

    public void setOutputBus(String outputBus) {
        this.outputBus = outputBus;
    }

    public FileType getType() {
        return FileType.AUDIO;
    }

}
