import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//Klasse nimmt eine Textdatei als Output von ffmpeg wieder
//und filtert aus dieser Text Datei die "silence-end"-Zeiten heraus
//als Output gibt die Klasse eine Textdatei wieder, welche die entsprechenden Zeiten abspeichert
//z.B.:
//"[silencedetect @ 000000000257ac60] silence_end: 3.584 | silence_duration: 1.384
// [silencedetect @ 000000000257ac60] silence_end: 20.352 | silence_duration: 5.224"
//wird zu 
//" 3.584 20.352"

public class silenceAuslesen {

	public static void logic() throws IOException {

		File f = new File("C:\\Users\\a_orsc01\\Desktop\\ffmpeg\\bin\\caOut.txt");
		while (!f.exists()) {
		}

		BufferedReader caReader = new BufferedReader(new FileReader("C:\\Users\\a_orsc01\\Desktop\\ffmpeg\\bin\\caOut.txt"));
		BufferedWriter caOut = new BufferedWriter(new FileWriter("C:/Users/a_orsc01/Desktop/ffmpeg/bin/caTimes.txt"));

		while (true) {

			String caZeile = caReader.readLine();

			if (caZeile == null) {
				break;
			}

			// entsprechende Stringzeilen ("Silence_end") so zerstückeln, dass
			// nur noch dessen
			// Zeiten über bleiben
			if (caZeile.contains("silence_end")) {
				int indexDoppelpunkt = caZeile.indexOf(":") + 1;
				int indexSonderzeichen = caZeile.indexOf("|") - 1;
				caZeile = caZeile.substring(indexDoppelpunkt, indexSonderzeichen);
				caZeile.replaceAll("[^.0-9" + "]", "");
				caOut.write(caZeile);

			}

			caOut.close();
			caReader.close();
		}

		BufferedReader kunReader = new BufferedReader(
				new FileReader("C:\\Users\\a_orsc01\\Desktop\\ffmpeg\\bin\\kunOut.txt"));
		BufferedWriter kunOut = new BufferedWriter(
				new FileWriter("C:\\Users\\a_orsc01\\Desktop\\ffmpeg\\bin\\kunTimes.txt"));

		while (true) {

			String kunZeile = kunReader.readLine();

			if (kunZeile == null) {
				break;
			}

			// entsprechende Stringzeilen ("Silence_end") so zerstückeln, dass
			// nur noch dessen
			// Zeiten über bleiben
			if (kunZeile.contains("silence_end")) {
				int indexDoppelpunkt = kunZeile.indexOf(":") + 1;
				int indexSonderzeichen = kunZeile.indexOf("|") - 1;
				kunZeile = kunZeile.substring(indexDoppelpunkt, indexSonderzeichen);
				kunZeile.replaceAll("[^.0-9" + "]", "");
				kunOut.write(kunZeile);

			}

			kunOut.close();
			kunReader.close();
		}
	}

	public static void main(String[] args) {

		// try {
		// pBuild();
		// } catch (IOException e) {
		//
		// e.printStackTrace();
		// }

		//
		try {
			logic();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}