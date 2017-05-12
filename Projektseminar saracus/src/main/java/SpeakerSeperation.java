import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

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
 *  FileChooser nimmt einen Audioschnitt an CHECK
 *  Sprung ins Überverzeichnis CHECK
 *  Ablegen aller Audio-Dateien (Filter!) im Verzeichnis (inklusive initial gewählter) in File[] CHECK
 *  Sortieren der Files im Array nach alphabetischer reihenfolge (nummer aufsteigend am ende) CHECK
 *  Auslesen der Startzeiten aus TXT-Datei in Startzeiten[] CHECK
 *  Erzeugen von File][]KU und File[]CA  CHECK
 *  
 *  Transkription der einzelnen Dateien in String[]
 *  Abspeichern der einzelnen Outputs mit "Dialogtrenner" CHECK
 *  
 *  XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
 *  TODO:
 *  - Input benennen, sodass 10ter schnitt keine stellenverschiebung generiert
 *  - int[] der startzeiten zu double[] ändern
 *  - + 99999 ausbessern mit "still-to-go" variable
 */

public class SpeakerSeperation extends AudioProcessing {

	File sourceFile;
	File ParentsourceFile;
	File[] audiofiles;
	File[] audiofilesKU;
	File[] audiofilesCA;
	File[] textfiles;
	int[] startzeitenKU;
	int[] startzeitenCA;

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
//		if (textfiles != null) { // Erforderliche Berechtigungen etc. sind
									// vorhanden
			// for (int i = 0; i < textfiles.length; i++) {
			// System.out.println(textfiles[i].getAbsolutePath());
			// }
//		}
	}

	// Methode zum Umwandeln einer Textdatei in ein Int[]
	public int[] TextToTime(File startingtimes) {

		int[] zeiten;
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
//		int count = 0;
//		try {
//			while (br.readLine() != null) {
//				count++;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		// System.out.println("Anzahl: " + count);
//		zeiten = new int[count];
		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		

		// InputString bei Leerzeichen trennen ACHTUNG: Ein String mehr als Zeiten, da Trennung "vor" erstem String
		String[] StringZeiten = inputString.split("\\s+");

//		for (int i = 0; i < StringZeiten.length; i++) {
//			System.out.println("ziffernString = " + StringZeiten[i]);
//		}

		// Strings in Integer parsen (stringArray besitzt als ersten string nur leerzeichen durch trennung, daher indexverschiebung)
		zeiten = new int[StringZeiten.length - 1];
		for (int i = 1; i < StringZeiten.length; i++) {
			zeiten[i-1] = Integer.parseInt(StringZeiten[i]);
		}

//		for (int i = 0; i < zeiten.length; i++) {
//			System.out.println("integerwert = " + zeiten[i]);
//		}

//		XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		// Abschnitt zum Schreiben der Zeiten in ein Int[], erneutes Einlesen
		// der Datei da Marker nach Durchlauf am Ende der Datei (Alte Versoin: Textdatei Zeilenweise einlesen)

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
//		XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		return zeiten;
	}

	// Methode liest durch getDataToArrays() die Dateien ein und generiert die
	// StartzeitenArrays
	public void initalizeData() {
		getDataToArrays();
		startzeitenKU = TextToTime(textfiles[0]);
		startzeitenCA = TextToTime(textfiles[1]);
//		 System.out.println(textfiles[0]);
//		 System.out.println(textfiles[1]);
//		 System.out.println("---------------");
//		 System.out.println(startzeitenKU.length);
//		 System.out.println(startzeitenCA.length);
//		 for(int i = 0; i<startzeitenKU.length;i++){
//		 System.out.println(startzeitenKU[i]);
//		 }
//		 System.out.println();
//		 for(int i = 0; i<startzeitenCA.length;i++){
//		 System.out.println(startzeitenCA[i]);
//		 }
		splitSpeaker();
	}

	public void splitSpeaker() {

		audiofilesKU = new File[startzeitenKU.length];
		audiofilesCA = new File[startzeitenCA.length];
		int insertIntoKU = 0;
		int insertIntoCA = 0;

		for (int i = 0; i < audiofiles.length; i++) {
			// identifikation der Sprecher anhand dateiname: 1.1.wav -> Speaker
			// Kunde, 2.1.wav -> Speaker Agent
			if (audiofiles[i].toString()
					.substring(audiofiles[i].toString().length() - 7, audiofiles[i].toString().length() - 6)
					.equals("1")) {
				audiofilesKU[insertIntoKU] = audiofiles[i];
				insertIntoKU++;
			} else {
				if (audiofiles[i].toString()
						.substring(audiofiles[i].toString().length() - 7, audiofiles[i].toString().length() - 6)
						.equals("2")) {
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
		
		//Problem der Indizes: Falls ein Stack leer ist / der pointer eines Felds am Ende steht, vergleicht das Element des anderes Felds
		// gegen Null(hinter das andere durch ++). Daher bisher unsauber: Falls letztes Element erreicht wurde startzeit des letzten Elements verändern sodass diese
		// auf jeden fall höher als das andere Element ist
		// TODO : Anpassen
		
		int currentPositionKU = 0;
		int currentPositionCA = 0;
//test-komment git
		for (int i = 0; i < audiofiles.length; i++) {
			if (startzeitenKU[currentPositionKU] < startzeitenCA[currentPositionCA]) {
				bundler.addTextSync("\n Kunde:  " + currentPositionKU);
				processAudio(audiofilesKU[currentPositionKU]);

				if (currentPositionKU < startzeitenKU.length - 1) {
					currentPositionKU++;
				} else {
					startzeitenKU[currentPositionKU] = startzeitenCA[currentPositionCA] + 99999;
				}
			} else {
				if (startzeitenCA[currentPositionCA] < startzeitenKU[currentPositionKU])
					bundler.addTextSync("\n Agent:  " + currentPositionCA);
				processAudio(audiofilesCA[currentPositionCA]);
				
				if (currentPositionCA < startzeitenCA.length - 1) {
					currentPositionCA++;
				} else {
					startzeitenCA[currentPositionCA] = startzeitenKU[currentPositionKU] + 99999;
				}

			}
		}
		bundler.speichereOutput();
	}
}
