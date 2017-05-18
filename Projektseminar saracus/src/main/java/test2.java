import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class test2 {

	public void cutAudio(File sourceFile, double[] startzeiten, double[] endzeiten) {

		String sourceFilePath = sourceFile.getPath().toString();
		String destinationFilePath = sourceFilePath;
		AudioInputStream inputStream = null;
		AudioInputStream shortenedStream = null;
		int AnzahlSchnitte = startzeiten.length;

		if (startzeiten.length != endzeiten.length) {
			System.out.println("Fehler bei Start/Endzeiten");
		}

		// Ermittlung der L�nge der Einzelnen Gespr�chsabschnitte und
		// Gespr�chspausen
		double[] l�ngeSchnitteSekunden = new double[startzeiten.length];
		for (int i = 0; i < l�ngeSchnitteSekunden.length; i++) {
			l�ngeSchnitteSekunden[i] = endzeiten[i] - startzeiten[i];
		}
		// for (int i = 0; i < l�ngeSchnitte.length; i++) {
		// System.out.println(l�ngeSchnitte[i]);
		// }

		double[] l�ngeSkipsSekunden = new double[startzeiten.length];
		l�ngeSkipsSekunden[0] = startzeiten[0];
		for (int i = 1; i < l�ngeSkipsSekunden.length; i++) {
			l�ngeSkipsSekunden[i] = startzeiten[i] - endzeiten[i - 1];
		}

		try {
			File file = new File(sourceFilePath);
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);

			AudioFormat format = fileFormat.getFormat();
			inputStream = AudioSystem.getAudioInputStream(file); // eine ID
			// double audioFileLength = file.length(); // get byte-Length des
			// inputs

			int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate(); // hz*bytes/frames
			// double audioSekunden = audioFileLength / bytesPerSecond;

			long[] l�ngeSkipsBytes = new long[l�ngeSkipsSekunden.length];
			for (int i = 0; i < l�ngeSkipsBytes.length; i++) {
				// cast zu long ohne +1, damit nicht zuviel geskippt wird
				l�ngeSkipsBytes[i] = (long) (l�ngeSkipsSekunden[i] * bytesPerSecond);
			}

			String origdestinationFilePath = destinationFilePath;

			for (int i = 1; i <= AnzahlSchnitte; i++) {
				
				inputStream.skip(l�ngeSkipsBytes[i - 1]);
				// getFrameRate liefert float zur�ck, daher cast in l.54.
				// AudioInputStream erwartet long, daher cast und +1 zum
				// aufrunden
				float framesOfAudioToCopy = (float) l�ngeSchnitteSekunden[i - 1] * format.getFrameRate();

				destinationFilePath = origdestinationFilePath;

				shortenedStream = new AudioInputStream(inputStream, format, (long) (framesOfAudioToCopy + 1));

				int pfadlaenge = destinationFilePath.length();

				String insert = null;
				if (i < 10) {
					insert = "_00";
				} else {
					if (i < 100) {
						insert = "_0";
					} else {
						insert = "_";
					}

				}
				destinationFilePath = destinationFilePath.substring(0, pfadlaenge - 4) + insert + i
						+ destinationFilePath.substring(pfadlaenge - 4, pfadlaenge);

				File destinationFile = new File(destinationFilePath);
				AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);

				// nach letztem Gespr�chsanteil muss nicht mehr geskippt werden
				
					
				
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
		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		// funktioniert noch nicht
		sourceFile.delete();
	}
}
