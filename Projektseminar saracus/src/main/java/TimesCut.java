import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimesCut extends FileChooser {

	public void identifyStartTimes(File inputfile, File outputdestination ) throws IOException {

		

		BufferedReader bReader = new BufferedReader(new FileReader(inputfile));
		BufferedWriter out = new BufferedWriter(new FileWriter(outputdestination));

		while (true) {
			String Zeile = bReader.readLine();
			if (Zeile == null) {
				break;
			}
			if (Zeile.contains("silence_end")) {
				int indexDoppelpunkt = Zeile.indexOf(":") + 1;
				int indexSonderzeichen = Zeile.indexOf("|") - 1;
				Zeile = Zeile.substring(indexDoppelpunkt, indexSonderzeichen);
				Zeile.replaceAll("[^.0-9" + "]", "");
				out.write(Zeile);
			}
		}
		out.close();
		bReader.close();

	}
// to be tested XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	public void identifyEndTimes(File output) throws IOException {

		File file = choose();

		BufferedReader bReader = new BufferedReader(new FileReader(file));
		BufferedWriter out = new BufferedWriter(new FileWriter(output));

		while (true) {
			String Zeile = bReader.readLine();
			if (Zeile == null) {
				break;
			}
			if (Zeile.contains("silence_start")) { // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
				int indexDoppelpunkt = Zeile.indexOf(":") + 1;
				int indexSonderzeichen = Zeile.length() - 1;
				Zeile = Zeile.substring(indexDoppelpunkt, indexSonderzeichen);
				Zeile.replaceAll("[^.0-9" + "]", "");
				out.write(Zeile);
			}
		}
		out.close();
		bReader.close();

	}

	// public static void main(String[] args) {
	//
	// File catext = new File("C:\\Users\\d_tham01\\Desktop\\catimes.txt");
	// File kuntext = new File("C:\\Users\\d_tham01\\Desktop\\kuntimes.txt");
	//
	// TimesCut mytest = new TimesCut();
	//
	// try {
	// mytest.logic(catext);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// try {
	// mytest.logic(kuntext);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// }
}