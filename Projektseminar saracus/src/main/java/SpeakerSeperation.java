import java.io.File;

/*Klasse zur Durchf�hrung der Speakerseperation
 * Erwartet: FileChooser-�bergabe eines Verzeichnesses mit Unterordnern A und B mit jeweilig gecutteten Unterdateien A.1, A.2 etc 
 * 
 * 
 * Zusammenspiel der Klassen:
 * Momentan:
 * main ruft AudioProcessing.processAudio auf
 * diese in line 64 den filechooser, der ausgew�hltes FILE zur�ckliefert
 * anzahl der Schnitte wird ermittelt, bestimmt iterationsanzahl der for-schleife
 * pro iteration wird in line 115 eine neue Datei erstellt, der Audioteil hineingeschrieben und das file an recognize �bergeben. l 117
 * diese �bersetzt und ermittelt Werte, von denen der �bersetzte Text in line 53 dem textbundler �bergeben wird
 * dieser buffert die einzelnen textteile und schreibt sie in eine vom FileWriter erstellte (?) neue Datei
 *  
 *  
 * Ziel / to-do:
 * 
 *  FileChooser nimmt einen Audioschnitt an
 *  Sprung ins �berverzeichnis
 *  Ablegen aller Audio-Dateien (Filter!) im Verzeichnis (inklusive initial gew�hlter) in File[]
 *  Sortieren der Files im Array nach alphabetischer reihenfolge (nummer aufsteigend am ende)
 *  Auslesen der Startzeiten aus TXT-Datei in Startzeiten[]
 *  Sortieren der Files nach Zeit
 *  Transkription der einzelnen Dateien 
 *  Abspeichern der einzelnen Outputs mit "Dialogtrenner" per Current_speaker variable
 */

public class SpeakerSeperation extends AudioProcessing {

	double Gespr�chsl�nge;
	int Anzahl_A;
	int Anzahl_B;
	boolean A_is_beginner;
	File sourceFile = choose() ;
	File ParentsourceFile = sourceFile.getParentFile();
	
	public void listDir() {

		File[] files = ParentsourceFile.listFiles();
		if (files != null) { // Erforderliche Berechtigungen etc. sind vorhanden
		for (int i = 0; i < files.length; i++) {
			System.out.print(files[i].getAbsolutePath());
			if (files[i].isDirectory()) { System.out.print(" (Ordner)\n"); }
			else {
				System.out.print(" (Datei)\n");
				}
			}
		}
		}

}
