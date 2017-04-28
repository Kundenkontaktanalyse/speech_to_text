/**
 * FileSplitter unfertig; Ich verstehe nicht, was zu was gehört.
 */
import java.io.*;
import javax.sound.sampled.*;

// Stand: 27.04.
// letzte Bearbeitung durch: Daniel
// 
// Splitter arbeitet nur mit WAV Dateien bisher.
// Benennung korrekt
// Aufteilung funktioniert 

class FileSplitter {
	public static void main(String[] args) {
		copyAudio("C:/Users/kathi/Desktop/testdaten/Teil1.wav",
				"C:/Users/kathi/Desktop/testdaten/Teil1.wav");
	}

	public static void copyAudio(String sourceFileName, String destinationFileName) {
		AudioInputStream inputStream = null;
		AudioInputStream shortenedStream = null;
		int secondsToCopy = 30;

		try {
			File file = new File(sourceFileName);
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
			System.out.println(fileFormat);
			
			AudioFormat format = fileFormat.getFormat();
			inputStream = AudioSystem.getAudioInputStream(file); // eine ID
			double audioFileLength = file.length(); // get Length des inputs
			System.out.println("Bytes des OriginalAudio:" + audioFileLength);
			
			int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate(); // hz*bytes/frames
			double audioSekunden = audioFileLength / bytesPerSecond;
			int längeAudio = (int) audioSekunden + 1; // aufrunden der Sekunden
			long framesOfAudioToCopy = secondsToCopy * (int) format.getFrameRate();
			int AnzahlSchnitte = (längeAudio / secondsToCopy);

			// Korrektur der Anzahl von Schnitten im Falle von Restsekunden
			if ((längeAudio % secondsToCopy) != 0) {
				AnzahlSchnitte += 1;
			}

			String origdestinationFileName = destinationFileName;

			System.out.println();
			
			// Gedankenfalle skip:
			// Vermutung: Durch die Erstellung des shortenedStream wird intern ein Marker weitergesetzt, der das skip unnötig werden lässt
			// ansonsten zweimaliges Überspringen mit lücken

			for (int i = 1; i <= AnzahlSchnitte; i++) {
				System.out.println("Iteration:" + i);
				
				destinationFileName = origdestinationFileName;
				int inputstreamVorher = inputStream.available();
				System.out.println("inputstream-vorher:" + inputstreamVorher);

				shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
				System.out.println("Länge shortenedStream bei erstellung:" + shortenedStream.available());

				// Einfügen des Laufindex i an der viertletzten Stelle (.wav /
				// TODO: Allgemein formulieren falls Dateiendung != .wav, falls später auch andere Dateien gecuttet werden sollen
				int pfadlaenge = destinationFileName.length();
				
				String insert = null;
				if (i<10){
					insert = "_0";
				} else {
					insert = "_";
				}
				destinationFileName = destinationFileName.substring(0, pfadlaenge - 4) + insert + i
						+ destinationFileName.substring(pfadlaenge - 4, pfadlaenge);

				File destinationFile = new File(destinationFileName);
				AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);
				System.out.println("Länge shortenedStream nach schreiben:" + shortenedStream.available());
				
				int inputstreamNachher = inputStream.available();
				System.out.println("inputstream-nachher:" + inputstreamNachher);
				
				int diff = inputstreamVorher - inputstreamNachher;
				System.out.println("Differenz inputstream vorher/nachher:" + diff);
				
				System.out.println();

			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (Exception e) {
					System.out.println(e);
				}
			if (shortenedStream != null)
				try {
					shortenedStream.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
	}
}