import java.io.*;
import javax.sound.sampled.*;

class AudioFileProcessor {
	public static void main(String[] args) {
		copyAudio("C:/Users/t_diek09/Desktop/AudioBeispiele/kleinMono.wav",
				"C:/Users/t_diek09/Desktop/AudioBeispiele/kleinMono2.wav", 2, 5);
	}

	public static void copyAudio(String sourceFileName, String destinationFileName, int startSecond,
			int secondsToCopy) {
		AudioInputStream inputStream = null;
		AudioInputStream shortenedStream = null;

		try {
			File file = new File(sourceFileName);
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
			AudioFormat format = fileFormat.getFormat();
			inputStream = AudioSystem.getAudioInputStream(file);
			int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate();
			inputStream.skip(startSecond * bytesPerSecond);
			long framesOfAudioToCopy = secondsToCopy * (int) format.getFrameRate();
			shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
			File destinationFile = new File(destinationFileName);
			AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);
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

	public static void println(Object o) {
		System.out.println(o);
	}

	public static void print(Object o) {
		System.out.print(o);
	}
}