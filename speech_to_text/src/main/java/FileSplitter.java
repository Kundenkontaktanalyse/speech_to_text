/**
 * FileSplitter unfertig; Ich verstehe nicht, was zu was gehört.
 */
import java.io.*;
import javax.sound.sampled.*;

class FileSplitter {
	public static void main(String[] args) {
		copyAudio("C:/Users/t_diek09/Desktop/AudioBeispiele/Teil2.wav",
				"C:/Users/t_diek09/Desktop/AudioBeispiele/Teil2.wav");
	}

	public static void copyAudio(String sourceFileName, String destinationFileName) {
		AudioInputStream inputStream = null;
		AudioInputStream shortenedStream = null;
		int secondsToCopy = 30;

		try {
			File file = new File(sourceFileName);
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
			AudioFormat format = fileFormat.getFormat();
			inputStream = AudioSystem.getAudioInputStream(file); // eine ID
			double audioFileLength = file.length(); // get Length des inputs
			int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate(); // hz*bytes/frames
			double audioSekunden = audioFileLength / bytesPerSecond;
			int längeAudio = (int) audioSekunden + 1;
			long framesOfAudioToCopy = secondsToCopy * (int) format.getFrameRate();
			int AnzahlSchnipsel = (längeAudio / secondsToCopy); // An Modulo
																// denken später
			for (int i = 0; i < AnzahlSchnipsel; i++) {
				String Dateiende = Integer.toString(i);
				inputStream.skip(secondsToCopy * i * bytesPerSecond);
				shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
				destinationFileName = destinationFileName + Dateiende + ".wav"; // UMÄNDERN
				File destinationFile = new File(destinationFileName); // Neue
																		// Datei
																		// im
																		// Ordner
																		// anlegen
				AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);
			}

		} catch (Exception e) {
			println(e);
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (Exception e) {
					println(e);
				}
			if (shortenedStream != null)
				try {
					shortenedStream.close();
				} catch (Exception e) {
					println(e);
				}
		}
	}
}