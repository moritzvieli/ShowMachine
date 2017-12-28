package com.ascargon.rocketshow.song.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;

public class FileManager {

	final static Logger logger = Logger.getLogger(FileManager.class);

	public List<com.ascargon.rocketshow.song.file.File> getAllFiles() throws Exception {
		List<com.ascargon.rocketshow.song.file.File> returnFileList = new ArrayList<com.ascargon.rocketshow.song.file.File>();
		File folder;
		File[] fileList;

		// Audio files
		folder = new File(Manager.BASE_PATH + com.ascargon.rocketshow.song.file.File.MEDIA_PATH + AudioFile.AUDIO_PATH);
		fileList = folder.listFiles();

		for (File file : fileList) {
			if (file.isFile()) {
				AudioFile audioFile = new AudioFile();
				audioFile.setName(file.getName());
				returnFileList.add(audioFile);
			}
		}

		// MIDI files
		folder = new File(Manager.BASE_PATH + com.ascargon.rocketshow.song.file.File.MEDIA_PATH + MidiFile.MIDI_PATH);
		fileList = folder.listFiles();

		for (File file : fileList) {
			if (file.isFile()) {
				MidiFile midiFile = new MidiFile();
				midiFile.setName(file.getName());
				returnFileList.add(midiFile);
			}
		}

		// Video files
		folder = new File(Manager.BASE_PATH + com.ascargon.rocketshow.song.file.File.MEDIA_PATH + VideoFile.VIDEO_PATH);
		fileList = folder.listFiles();

		for (File file : fileList) {
			if (file.isFile()) {
				VideoFile videoFile = new VideoFile();
				videoFile.setName(file.getName());
				returnFileList.add(videoFile);
			}
		}

		return returnFileList;
	}

	public void saveFile(InputStream uploadedInputStream, String fileName) {
		String[] midiFormats = { "midi", "mid" };
		String[] audioFormats = { "wav", "wave", "mp3", "aac", "ogg", "oga", "mogg", "wma" };
		String[] videoFormats = { "avi", "mpg", "mpeg", "mkv", "mp4", "mov", "m4a" };

		// Compute the path according to the file extension
		String extension = FilenameUtils.getExtension(fileName).toLowerCase().trim();
		String path = Manager.BASE_PATH + com.ascargon.rocketshow.song.file.File.MEDIA_PATH;

		if (Arrays.asList(midiFormats).contains(extension)) {
			path += MidiFile.MIDI_PATH;
		} else if (Arrays.asList(audioFormats).contains(extension)) {
			path += AudioFile.AUDIO_PATH;
		} else if (Arrays.asList(videoFormats).contains(extension)) {
			path += VideoFile.VIDEO_PATH;
		}
		
		path += fileName;

		try {
			OutputStream out = new FileOutputStream(new File(path));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(path));
			
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("Could not save file '" + fileName + "'", e);
		}
	}

}
