import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class test extends FileChooser {

	File audioCA = choose();
	File audioKU = choose();

	String audioCAFilePath = audioCA.getPath().toString();
	String audioKUFilePath = audioKU.getPath().toString();
	String destinationCAFilePath = audioCAFilePath;
	String destinationKUFilePath = audioKUFilePath;

	AudioInputStream inputStreamCA = null;
	AudioInputStream shortenedStreamCA = null;
	AudioInputStream inputStreamKU = null;
	AudioInputStream shortenedStreamKU = null;

	long framesOfAudioToCopy;
	int cutAnzahlCA;
	int cutAnzahlKU;

	double[] secondsToCopyCA;
	double[] secondsToSkipCA;
	double[] secondsToCopyKU;
	double[] secondsToSkipKU;

	public void audioCutting(double[] startzeitenCALL, double[] startzeitenKUNDE) {

		// bestimmung der länge des letzten teils durch subtraktion von
		// gesamtlänge des audio
		// daher warnung falls channelgröße mehr als 5000 byte Unterschied
		if (Math.abs(audioCA.length() - audioKU.length()) < 5000) {
			System.out.println("Channels Unterschiedlich lang");
		}

		cutAnzahlCA = startzeitenCALL.length;
		cutAnzahlKU = startzeitenKUNDE.length;

		int secondsToCopy2 = 50;

		try {
			File file = new File(audioCAFilePath);
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
			AudioFormat format = fileFormat.getFormat();

			int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate(); // hz*bytes/frames
			double audioFileLength = file.length();
			double audioSekunden = audioFileLength / bytesPerSecond;

			inputStream = AudioSystem.getAudioInputStream(file); // eine ID
			double audioFileLength = file.length(); // get Length des inputs

			int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate(); // hz*bytes/frames

			String origdestinationFilePath = destinationFilePath;

			// Gedankenfalle skip:
			// Vermutung: Durch die Erstellung des shortenedStream wird intern
			// ein Marker weitergesetzt, der das skip unnötig werden lässt
			// ansonsten zweimaliges Überspringen mit lücken

			for (int i = 1; i <= cutAnzahl; i++) {

				framesOfAudioToCopy = secondsToCopy * (int) format.getFrameRate();
				// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
				destinationFilePath = origdestinationFilePath;

				shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);

				// bisher nur .wav, später flexibel gestalten evt.
				int pfadlaenge = destinationFilePath.length();

				String insert = null;
				if (i < 10) {
					insert = "_0";
				} else {
					insert = "_";
				}
				destinationFilePath = destinationFilePath.substring(0, pfadlaenge - 4) + insert + i
						+ destinationFilePath.substring(pfadlaenge - 4, pfadlaenge);

				File destinationFile = new File(destinationFilePath);
				AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);

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

	
	
	/* XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	 * NONESENCE!!!!!!!!!!! Startzeiten der Stille aus ffmpeg lesen, nicht aus arrays ermitteln.
	 * format am besten: startzeiten in einer zeile.
	 * format faken bis ffmpeg angepasst
	 * 
	 * 
	 * Methode bestimmung der Audiozeiten in denen geredet wird auf beiden channeln
	 * ändert:	double[] secondsToCopyCA;
				double[] secondsToSkipCA;
				double[] secondsToCopyKU;
				double[] secondsToSkipKU;
	 * 	
	 * XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	 */
	public void startzeitenToSkipAndCopy(double[] startzeitenCALL, double[] startzeitenKUNDE) {
		
		// Unterscheidung des Beginners: Gesprächsteillänge des zweiten Redners durch Vegleich mit nächstem Wert des anderen
		if(startzeitenKUNDE[0] == 0.0){
			
		for (int i = 0; i < Math.max(startzeitenCALL.length, startzeitenKUNDE.length ); i++) {
			secondsToCopyKU[i] =  startzeitenCALL[i] - startzeitenKUNDE[i];
			secondsToCopyCA[i] = startzeitenKUNDE[i+1] - startzeitenCALL[i];
		}
		} else {
			
			for (int i = 0; i < Math.max(startzeitenCALL.length, startzeitenKUNDE.length ); i++) {
				secondsToCopyKU[i] =  startzeitenCALL[i+1] - startzeitenKUNDE[i];
				secondsToCopyCA[i] = startzeitenKUNDE[i] - startzeitenCALL[i];
			}
			
		}
		// Werte zum herausschneiden der Stille um geringere bearbeitungszeit der API zu erhalten; genausolang wie die eine Spur
		// redet muss die andere übersprungen
		secondsToSkipCA = secondsToCopyKU;
		secondsToSkipKU = secondsToCopyCA;
		

	}

}