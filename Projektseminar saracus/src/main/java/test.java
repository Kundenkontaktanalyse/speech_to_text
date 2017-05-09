import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class test {

	
	// Methode zum Auslesen der Startzeiten aus einer Textdatei in ein Int[]
	public void TextToTime(File startingtimes) throws IOException {

		

		int[] zeiten1;

		// Abschnitt zur Bestimmung der Anzahl der Startzeiten
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(startingtimes)));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int count = 0;
		while (br.readLine() != null) {
			count++;
		}
		System.out.println("Anzahl: " + count);
		zeiten1 = new int[count];

		// Abschnitt zum Schreiben der Zeiten in ein Int[], erneutes Einlesen
		// der Datei da Marker nach Durchlauf am Ende der Datei

		br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(
				"C:/Users/d_tham01/sciebo/Lehre - PS KundenKontakt saracus/Projektarbeit/speech-to-text/testdaten/audio_files/eigene aufnahmen/kundengespräch/gesamtschnitte/starting1.txt"))));

		for (int i = 0; i < count; i++) {
			zeiten1[i] = Integer.parseInt(br.readLine());
		}

		for (int i = 0; i < zeiten1.length; i++) {
			System.out.println(zeiten1[i]);
		}

	}
}
