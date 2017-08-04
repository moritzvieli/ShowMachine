package com.ascargon.rocketshow;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.dmx.DmxSignalSender;
import com.ascargon.rocketshow.dmx.Midi2DmxConverter;
import com.ascargon.rocketshow.image.ImageDisplayer;
import com.ascargon.rocketshow.midi.Startup;
import com.ascargon.rocketshow.song.SetList;
import com.ascargon.rocketshow.song.Song;
import com.ascargon.rocketshow.video.VideoPlayer;

public class Manager {

	final static Logger logger = Logger.getLogger(Manager.class);

	public final String BASE_PATH = "/opt/rocketshow/";

	private DmxSignalSender dmxSignalSender;
	private Midi2DmxConverter midi2DmxConverter;

	private VideoPlayer videoPlayer;
	private ImageDisplayer imageDisplayer;

	private Session session = new Session();
	private Settings settings = new Settings();

	private SetList currentSetList;
	private Song currentSong;

	public void setSongIndex(int index) {
		if (currentSetList != null) {
			if (currentSetList.getSongList().size() >= index) {
				currentSong = currentSetList.getSongList().get(index);
				currentSetList.setCurrentSongIndex(index);

				logger.info("Set song index " + index);
			}
		}
	}

	public void loadSetlist(String path) throws Exception {
		logger.info("Loading setlist " + path + "...");

		// Load a setlist
		JAXBContext jaxbContext = JAXBContext.newInstance(SetList.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		currentSetList = (SetList) jaxbUnmarshaller.unmarshal(new File(path));
		currentSetList.setManager(this);
		currentSetList.setPath(path);
		currentSetList.load();

		setSongIndex(0);

		logger.info("Setlist " + path + " successfully loaded");
	}

	public void load() {
		logger.info("Initialize RocketShow...");

		// Initialize the DMX sender
		dmxSignalSender = new DmxSignalSender();
		midi2DmxConverter = new Midi2DmxConverter(dmxSignalSender);

		// Initialize the video player
		videoPlayer = new VideoPlayer();

		// Initialize the image displayer
		try {
			imageDisplayer = new ImageDisplayer();
		} catch (IOException e2) {
			logger.error("Could not initialize image displayer");
			logger.error(e2.getStackTrace());
		}

		// Load the settings
		try {
			loadSettings();
		} catch (JAXBException e1) {
			logger.error(e1.getStackTrace());
		}

		// Save the settings (in case none were already existant)
		try {
			saveSettings();
		} catch (JAXBException e1) {
			logger.error("Could not save settings");
			logger.error(e1.getStackTrace());
		}

		// Restore the session from the file
		try {
			restoreSession();
		} catch (Exception e1) {
			logger.error("Could not restore session");
			logger.error(e1.getStackTrace());
		}

		logger.info("RocketShow initialized");

		// TODO Initialize the MIDI system
		Startup s = new Startup();
		String[] args = new String[1];
		args[0] = "-l";
		try {
			s.main(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveSettings() throws JAXBException {
		File file = new File(BASE_PATH + "settings");
		JAXBContext jaxbContext = JAXBContext.newInstance(Settings.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(settings, file);

		logger.info("Settings saved");
	}

	public void loadSettings() throws JAXBException {
		File file = new File(BASE_PATH + "settings");
		if (!file.exists() || file.isDirectory()) {
			return;
		}

		// Restore the session from the file
		JAXBContext jaxbContext = JAXBContext.newInstance(Settings.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		settings = (Settings) jaxbUnmarshaller.unmarshal(file);

		if (settings.getDefaultImagePath() != null) {
			try {
				imageDisplayer.display(settings.getDefaultImagePath());
			} catch (IOException e) {
				logger.error("Could not display default image \"" + settings.getDefaultImagePath() + "\"");
				logger.error(e.getStackTrace());
			}
		}

		logger.info("Settings loaded");
	}

	public void saveSession() {
		if (currentSetList != null) {
			session.setCurrentSetListPath(currentSetList.getPath());
			session.setCurrentSongIndex(currentSetList.getCurrentSongIndex());
		}

		try {
			File file = new File(BASE_PATH + "session");
			JAXBContext jaxbContext = JAXBContext.newInstance(Session.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(session, file);

			logger.info("Session saved");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private void restoreSession() throws Exception {
		File file = new File(BASE_PATH + "session");
		if (!file.exists() || file.isDirectory()) {
			return;
		}

		// Restore the session from the file
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Session.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			session = (Session) jaxbUnmarshaller.unmarshal(file);

			if (session.getCurrentSetListPath() != null) {
				loadSetlist(session.getCurrentSetListPath());

				if (session.getCurrentSongIndex() != null) {
					setSongIndex(session.getCurrentSongIndex());
				}
			}

			logger.info("Session restored");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public Midi2DmxConverter getMidi2DmxConverter() {
		return midi2DmxConverter;
	}

	public void setMidi2DmxConverter(Midi2DmxConverter midi2DmxConverter) {
		this.midi2DmxConverter = midi2DmxConverter;
	}

	public VideoPlayer getVideoPlayer() {
		return videoPlayer;
	}

	public void setVideoPlayer(VideoPlayer videoPlayer) {
		this.videoPlayer = videoPlayer;
	}

	public SetList getCurrentSetList() {
		return currentSetList;
	}

	public void setCurrentSetList(SetList currentSetList) {
		this.currentSetList = currentSetList;
	}

	public Song getCurrentSong() {
		return currentSong;
	}

	public void setCurrentSong(Song currentSong) {
		this.currentSong = currentSong;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

}
