import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/*Klasse zur Durchführung der Speakerseperation
 * Erwartet: FileChooser-Übergabe eines Verzeichnesses mit Unterordnern A und B mit jeweilig gecutteten Unterdateien A.1, A.2 etc 
 * 
 * 
 * Zusammenspiel der Klassen:
 * Momentan:
 * main ruft AudioProcessing.processAudio auf
 * diese in line 64 den filechooser, der ausgewähltes FILE zurückliefert
 * anzahl der Schnitte wird ermittelt, bestimmt iterationsanzahl der for-schleife
 * pro iteration wird in line 115 eine neue Datei erstellt, der Audioteil hineingeschrieben und das file an recognize übergeben. l 117
 * diese übersetzt und ermittelt Werte, von denen der übersetzte Text in line 53 dem textbundler übergeben wird
 * dieser buffert die einzelnen textteile und schreibt sie in eine vom FileWriter erstellte (?) neue Datei
 *  
 *  
 * Ziel / to-do:
 * 
 *  processFiles startet Prozess
 *  FileChooser nimmt einen Audioschnitt an CHECK
 *  Sprung ins Überverzeichnis CHECK
 *  Ablegen aller Audio-Dateien (Filter!) im Verzeichnis (inklusive initial gewählter) in File[] CHECK
 *  Sortieren der Files im Array nach alphabetischer reihenfolge (nummer aufsteigend am ende) CHECK
 *  Auslesen der Startzeiten aus TXT-Datei in Startzeiten[] CHECK
 *  Erzeugen von File][]KU und File[]CA  CHECK
 *  
 *  Transkription der einzelnen Dateien in bufferedReader +
 *  Abspeichern der einzelnen Outputs mit "Dialogtrenner" CHECK
 *  
 *  XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
 *  
 *  
 *  ffmpeg textdateien:
 *  1: CA-Startzeiten = C1.txt
 *  2: CA-Endzeiten = C2.txt
 *  3: KU-Startzeiten = K1.txt
 *  4: KU-Endzeiten = K2.txt
 *  
 *  
 *  TODO:
 *  input datei beim schneiden löschen
 *  cutMethode ins gesamtsystem einbetten
 *  identifyStarter 0.0 fixen
 *  
 */

public class SpeakerSeperation extends AudioProcessing {

	File sourceFile;
	File ParentsourceFile;
	File[] audiofiles;
	File[] audiofilesKU;
	File[] audiofilesCA;
	File[] textfiles;
	double[] startzeitenKU;
	double[] startzeitenCA;
	double[] endzeitenKU;
	double[] endzeitenCA;
	double[] startzeitenKUrdy;
	double[] startzeitenCArdy;

	// Methode zum Schneiden von AudioDateien: Legt die AudioSchnitte im
	// Verzeichnis der Audiodatei ab
	// Bennennung der Dateien mit originalNamen + 00X für Anzahl
	public void cutAudio(File sourceFile, double[] startzeiten, double[] endzeiten) {

		String sourceFilePath = sourceFile.getPath().toString();
		String destinationFilePath = sourceFilePath;
		AudioInputStream inputStream = null;
		AudioInputStream shortenedStream = null;
		int AnzahlSchnitte = startzeiten.length;

		if (startzeiten.length != endzeiten.length) {
			System.out.println("Fehler bei Start/Endzeiten");
		}

		// Ermittlung der Länge der Einzelnen Gesprächsabschnitte und
		// Gesprächspausen
		double[] längeSchnitteSekunden = new double[startzeiten.length];
		for (int i = 0; i < längeSchnitteSekunden.length; i++) {
			längeSchnitteSekunden[i] = endzeiten[i] - startzeiten[i];
		}
		// for (int i = 0; i < längeSchnitte.length; i++) {
		// System.out.println(längeSchnitte[i]);
		// }

		double[] längeSkipsSekunden = new double[startzeiten.length];
		längeSkipsSekunden[0] = startzeiten[0];
		for (int i = 1; i < längeSkipsSekunden.length; i++) {
			längeSkipsSekunden[i] = startzeiten[i] - endzeiten[i - 1];
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

			long[] längeSkipsBytes = new long[längeSkipsSekunden.length];
			for (int i = 0; i < längeSkipsBytes.length; i++) {
				// cast zu long ohne +1, damit nicht zuviel geskippt wird
				längeSkipsBytes[i] = (long) (längeSkipsSekunden[i] * bytesPerSecond);
			}

			String origdestinationFilePath = destinationFilePath;

			for (int i = 1; i <= AnzahlSchnitte; i++) {

				inputStream.skip(längeSkipsBytes[i - 1]);
				// getFrameRate liefert float zurück, daher cast in l.54.
				// AudioInputStream erwartet long, daher cast und +1 zum
				// aufrunden
				float framesOfAudioToCopy = (float) längeSchnitteSekunden[i - 1] * format.getFrameRate();

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

				// nach letztem Gesprächsanteil muss nicht mehr geskippt werden

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

	/*
	 * Methode zum Verarbeiten der Input-Dateien: Dateien werden nach Audio und
	 * Text gefiltert und in jeweilige Arrays abgelegt
	 */
	public void getDataToArrays() {

		sourceFile = choose();
		ParentsourceFile = sourceFile.getParentFile();

		// Filter zur kontrolle ob Datei mit .wav endet
		class AudioFilter implements FileFilter {
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(".wav");
			}
		}

		// Filter zur kontrolle ob Datei mit .txt endet
		class TextFilter implements FileFilter {
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(".txt");
			}
		}

		// Ablage aller Audiodateien des Verzeichnisses in audiofiles[]
		audiofiles = ParentsourceFile.listFiles(new AudioFilter());
		Arrays.sort(audiofiles); // alphabetische Sortierung scheint automatisch
									// zu erfolgen, wegen hinweis der API
									// nochmal zur Sicherheit
		// if (audiofiles != null) {
		// for (int i = 0; i < audiofiles.length; i++) {
		// System.out.println(audiofiles[i].getAbsolutePath());
		// }
		// }

		// Ablage aller Textdateien des Verzeichnisses in textfiles[]
		textfiles = ParentsourceFile.listFiles(new TextFilter());
		Arrays.sort(textfiles); // alphabetische Sortierung scheint automatisch
								// zu erfolgen, wegen hinweis der API nochmal
								// zur Sicherheit
		// if (textfiles != null) { // Erforderliche Berechtigungen etc. sind
		// vorhanden
		// for (int i = 0; i < textfiles.length; i++) {
		// System.out.println(textfiles[i].getAbsolutePath());
		// }
		// }
	}

	// Methode zum Umwandeln der Werte einer einzeiligen Textdatei in ein
	// double[]
	public double[] TextToTime(File startingtimes) {

		double[] zeiten;
		String inputString = null;

		// Abschnitt zur Bestimmung der Anzahl der Startzeiten
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(startingtimes)));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			inputString = br.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		// ALTE VESION: Zeilenweises Auslesen der Zeiten
		// int count = 0;
		// try {
		// while (br.readLine() != null) {
		// count++;
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// // System.out.println("Anzahl: " + count);
		// zeiten = new int[count];
		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

		// InputString bei Leerzeichen trennen ACHTUNG: Ein String mehr als
		// Zeiten, da Trennung "vor" erstem String
		String[] StringZeiten = inputString.split("\\s+");

		// for (int i = 0; i < StringZeiten.length; i++) {
		// System.out.println("ziffernString = " + StringZeiten[i]);
		// }

		// Strings in Integer parsen (stringArray besitzt als ersten string nur
		// leerzeichen durch trennung, daher indexverschiebung)
		zeiten = new double[StringZeiten.length - 1];
		for (int i = 1; i < StringZeiten.length; i++) {
			zeiten[i - 1] = Double.parseDouble(StringZeiten[i]);
		}

		// for (int i = 0; i < zeiten.length; i++) {
		// System.out.println("doublewert = " + zeiten[i]);
		// }

		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		// Abschnitt zum Schreiben der Zeiten in ein Int[], erneutes Einlesen
		// der Datei da Marker nach Durchlauf am Ende der Datei (Alte Versoin:
		// Textdatei Zeilenweise einlesen)

		// try {
		// br = new BufferedReader(new InputStreamReader(new
		// FileInputStream(startingtimes)));
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		//
		// for (int i = 0; i < anzahlZeiten; i++) {
		// try {
		// zeiten[i] = Integer.parseInt(br.readLine());
		// } catch (NumberFormatException | IOException e) {
		// e.printStackTrace();
		// }
		// }
		//
		// // for (int i = 0; i < zeiten.length; i++) {
		// // System.out.println(zeiten[i]);
		// // }
		// return zeiten;
		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		return zeiten;
	}

	// to be tested XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	public void getTimeArrays() {

		for (int i = 0; i < textfiles.length; i++) {

			if (textfiles[i].getName() == "C1.txt") {
				startzeitenCA = TextToTime(textfiles[i]);
			}
			if (textfiles[i].getName() == "C2.txt") {
				endzeitenCA = TextToTime(textfiles[i]);
			}
			if (textfiles[i].getName() == "K1.txt") {
				startzeitenKU = TextToTime(textfiles[i]);
			}
			if (textfiles[i].getName() == "K2.txt") {
				endzeitenKU = TextToTime(textfiles[i]);
			}
			// falls keine der if-s getriggert wurde fehler
			if ( !(textfiles[i].getName() == "C1.txt")||(textfiles[i].getName() == "C2.txt")||(textfiles[i].getName() == "K1.txt")|| (textfiles[i].getName() == "K2.txt") ){
				System.out.println("Fehler in Textdateibenennung");
			}
			
			
			
			

		}
	}

	// Methode liest durch getDataToArrays() die Dateien ein und generiert die
	// StartzeitenArrays
	public void initalizeData() {
		getDataToArrays();
		getTimeArrays(); // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		identifyStarter();

		// System.out.println(textfiles[0]);
		// System.out.println(textfiles[1]);
		// System.out.println("---------------");
		// System.out.println(startzeitenKUrdy.length);
		// System.out.println(startzeitenCArdy.length);
		// for(int i = 0; i<startzeitenKUrdy.length;i++){
		// System.out.println(startzeitenKUrdy[i]);
		// }
		// System.out.println();
		// for(int i = 0; i<startzeitenCArdy.length;i++){
		// System.out.println(startzeitenCArdy[i]);
		// }

		splitSpeaker();
	}

	// Methode zur Anpassung der double[] startzeiten: Einfügen einer 0.0 beim
	// Beginner des Gesprächs
	// Bei stille in beiden channeln, 0.0 einfügen kommt in falschen
	// channel!!!!!!!!
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	public void identifyStarter() {

		if (startzeitenCA[0] < startzeitenKU[0]) {

			startzeitenCArdy = startzeitenCA;

			startzeitenKUrdy = new double[startzeitenKU.length + 1];
			startzeitenKUrdy[0] = 0.0;
			for (int i = 1; i < startzeitenKUrdy.length; i++) {
				startzeitenKUrdy[i] = startzeitenKU[i - 1];

			}

		} else {
			startzeitenKUrdy = startzeitenKU;

			startzeitenCArdy = new double[startzeitenCA.length + 1];
			startzeitenCArdy[0] = 0.0;
			for (int i = 1; i < startzeitenCArdy.length; i++) {
				startzeitenCArdy[i] = startzeitenCA[i - 1];

			}

		}
	}

	// Methode zum Trennen der Redner:
	// audioFiles[] wird aufgeteilt in audiofilesKU und audiofilesCA
	public void splitSpeaker() {

		audiofilesKU = new File[startzeitenKUrdy.length];
		audiofilesCA = new File[startzeitenCArdy.length];
		int insertIntoKU = 0;
		int insertIntoCA = 0;

		for (int i = 0; i < audiofiles.length; i++) {
			// identifikation der Sprecher anhand dateiname: K_001.wav ->
			// Speaker
			// Kunde, C_001.wav -> Speaker Agent => 9t letzte Stelle des Strings
			if (audiofiles[i].toString()
					.substring(audiofiles[i].toString().length() - 9, audiofiles[i].toString().length() - 8)
					.equals("K")) {
				audiofilesKU[insertIntoKU] = audiofiles[i];
				insertIntoKU++;
			} else {
				if (audiofiles[i].toString()
						.substring(audiofiles[i].toString().length() - 9, audiofiles[i].toString().length() - 8)
						.equals("C")) {
					audiofilesCA[insertIntoCA] = audiofiles[i];
					insertIntoCA++;
				} else {
					System.out.println("Fehler bei splitSpeaker");
				}
			}
		}

		// for (int i = 0; i < audiofilesKU.length; i++) {
		// System.out.print(audiofilesKU[i].getAbsolutePath());
		// if (audiofilesKU[i].isDirectory()) {
		// System.out.print(" (Ordner)\n");
		// } else {
		// System.out.print(" (Datei)\n");
		// }
		// }
		// System.out.println("-------------------");
		// for (int i = 0; i < audiofilesCA.length; i++) {
		// System.out.print(audiofilesCA[i].getAbsolutePath());
		// if (audiofilesCA[i].isDirectory()) {
		// System.out.print(" (Ordner)\n");
		// } else {
		// System.out.print(" (Datei)\n");
		// }
		// }

	}

	/*
	 * =========================================================================
	 * ================================================
	 * =========================================================================
	 * ================================================
	 * 
	 * Bis hierhin: Audio-Dateien werden in audiofilesKU[] (FILE) und
	 * audiofilesCA[] eingelesen, Trennung der Speaker anhand Dateinamen
	 * (siebt-letzter Character des Namens) Textdateien werden in
	 * startzeitenKU[] (INT) und startzeitenCA eingelesen Funktionalität durch
	 * auskommentierte Syso's kontrolliert.
	 * 
	 */
	public void processFiles() {

		initalizeData();

		// Idee eines Stack:
		// Zeiger auf jeweiligen Feldern, "abnehmen" des aktuellsten Elemts
		// beider felder

		// Problem der Indizes: Falls ein Stack leer ist / der pointer eines
		// Felds am Ende steht, vergleicht das Element des anderes Felds
		// gegen Null(hinter das andere durch ++).
		// Daher boolean falls komplett durchlaufen

		int currentPositionKU = 0;
		int currentPositionCA = 0;
		boolean KUfinished = false;
		boolean CAfinished = false;

		for (int i = 0; i < audiofiles.length; i++) {
			if ((startzeitenKUrdy[currentPositionKU] < startzeitenCArdy[currentPositionCA]) || (CAfinished)) {
				bundler.addTextSync("\n Kunde:  " + currentPositionKU);
				processAudio(audiofilesKU[currentPositionKU]);

				if (currentPositionKU < startzeitenKUrdy.length - 1) {
					currentPositionKU++;
				} else {
					// startzeitenKUrdy[currentPositionKU] =
					// startzeitenCArdy[currentPositionCA] + 99999;
					KUfinished = true;
				}
			} else {
				if ((startzeitenCArdy[currentPositionCA] < startzeitenKUrdy[currentPositionKU]) || (KUfinished)) {
					bundler.addTextSync("\n Agent:  " + currentPositionCA);
					processAudio(audiofilesCA[currentPositionCA]);

					if (currentPositionCA < startzeitenCArdy.length - 1) {
						currentPositionCA++;
					} else {
						// startzeitenCArdy[currentPositionCA] =
						// startzeitenKUrdy[currentPositionKU] + 99999;
						CAfinished = true;
					}
				}

			}
		}
		bundler.speichereOutput();
	}
}
