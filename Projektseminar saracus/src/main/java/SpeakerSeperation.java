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
 *  Sortieren der Files nach Zeit
 *  Transkription der einzelnen Dateien in String[]
 *  Abspeichern der einzelnen Outputs mit "Dialogtrenner" per Current_speaker variable in finalem output
 */

public class SpeakerSeperation extends AudioProcessing {

	File sourceFile;
	File ParentsourceFile;
	File[] audiofiles;
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
		if (audiofiles != null) { // Erforderliche Berechtigungen etc. sind
									// vorhanden
//			for (int i = 0; i < audiofiles.length; i++) {
//				System.out.println(audiofiles[i].getAbsolutePath());
//			}
		}

		// Ablage aller Textdateien des Verzeichnisses in textfiles[]
		textfiles = ParentsourceFile.listFiles(new TextFilter());
		Arrays.sort(textfiles); // alphabetische Sortierung scheint automatisch
								// zu erfolgen, wegen hinweis der API nochmal
								// zur Sicherheit
		if (textfiles != null) { // Erforderliche Berechtigungen etc. sind
									// vorhanden
//			for (int i = 0; i < textfiles.length; i++) {
//				System.out.println(textfiles[i].getAbsolutePath());
//			}
		}
	}

	// Methode zum Umwandeln einer Textdatei in ein Int[]
	public int[] TextToTime(File startingtimes) {

		int[] zeiten;

		// Abschnitt zur Bestimmung der Anzahl der Startzeiten
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(startingtimes)));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int count = 0;
		try {
			while (br.readLine() != null) {
				count++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println("Anzahl: " + count);
		zeiten = new int[count];

		// Abschnitt zum Schreiben der Zeiten in ein Int[], erneutes Einlesen
		// der Datei da Marker nach Durchlauf am Ende der Datei

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(startingtimes)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < count; i++) {
			try {
				zeiten[i] = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
		}

//		for (int i = 0; i < zeiten.length; i++) {
//			System.out.println(zeiten[i]);
//		}
		return zeiten;

	}

	public void initalizeData() {
		getDataToArrays();
		startzeitenKU = TextToTime(textfiles[0]);
		startzeitenCA = TextToTime(textfiles[1]);
//		System.out.println(textfiles[0]);
//		System.out.println(textfiles[1]);
//		for(int i = 0; i<startzeitenKU.length;i++){
//			System.out.println(startzeitenKU[i]);
//		}
//		System.out.println();
//		for(int i = 0; i<startzeitenCA.length;i++){
//			System.out.println(startzeitenCA[i]);
//		}

	}

}
