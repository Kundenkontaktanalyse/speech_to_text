import java.io.File;

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
 */

public class SpeakerSeperation extends AudioProcessing {

	double Gesprächslänge;
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
