import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;


public class silenceAuslesen {

	final static String VERZEICHNIS = ("C:\\Users\\Alex\\Desktop\\newfile.txt");
	

	public static void main(String[] args) throws Exception {

		BufferedReader bReader = new BufferedReader(new FileReader(VERZEICHNIS));
		BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\Alex\\Desktop\\newfile2.txt"));
		
		while (true) {
			String Zeile = bReader.readLine();
			if (Zeile == null) { break; }
			if (Zeile.contains("silence_end")) {
				int indexDoppelpunkt = Zeile.indexOf(":") + 1; 
				int indexSonderzeichen = Zeile.indexOf("|") - 1;
				Zeile = Zeile.substring(indexDoppelpunkt,indexSonderzeichen);
				Zeile.replaceAll("[^.0-9"+ "]", "");
				out.write(Zeile);	
			}
		}
		

		out.close();
		bReader.close();
		
	}
}
