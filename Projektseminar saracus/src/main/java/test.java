import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class test {

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
}
