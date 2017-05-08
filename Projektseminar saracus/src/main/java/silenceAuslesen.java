import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;


public class silenceAuslesen {

	static String fileName = "channel1.wav"; // Variable für Dateinamen
	final static String VERZEICHNIS = ("C:\\Users\\a_orsc01\\Desktop\\newfile.txt");

	public static void main(String[] args) throws Exception {

		BufferedReader bReader = new BufferedReader(new FileReader(VERZEICHNIS));
		BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\a_orsc01\\Desktop\\newfile2.txt"));
		
		
		
		
		
		while (true) {
			String Zeile = bReader.readLine();
			if (Zeile == null) { break; }
			if (Zeile.contains("silence_end")) {
				out.write(Zeile);
				
			}
			
		}
out.close();
bReader.close();
	}
}
