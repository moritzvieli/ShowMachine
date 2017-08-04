package com.ascargon.rocketshow.song.file;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.video.VideoPlayer;

public class VideoFile extends File {

	final static Logger logger = Logger.getLogger(VideoFile.class);
	
	@Override
	public void load() {
		this.getManager().getVideoPlayer().load(this.getPath());
	}
	
	@Override
	public void play() throws IOException {
		VideoPlayer videoPlayer = this.getManager().getVideoPlayer();
		String path = this.getPath();
		
		if (this.getOffsetInMillis() >= 0) {
			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					try {
						videoPlayer.play();
					} catch (IOException e) {
						logger.error("Could not play video \"" + path + "\"");
						logger.error(e.getStackTrace());
					}
				}
			}, this.getOffsetInMillis());
		} else {
			videoPlayer.setPositionInMillis(this.getOffsetInMillis() * -1);
			videoPlayer.play();
		}
	}

	@Override
	public void pause() throws IOException {
		this.getManager().getVideoPlayer().pause();
	}
	
	@Override
	public void resume() throws IOException {
		this.getManager().getVideoPlayer().resume();
	}

}
