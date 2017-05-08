import java.io.BufferedReader;
import java.io.InputStreamReader;

public class silenceAuslesen {
	
	static String fileName = "channel1.wav"; // Variable für Dateinamen
	
    public static void main(String[] args) throws Exception {
  
    	//ProcessBuilder Objekt erstellen
        ProcessBuilder silenceAusleser = new ProcessBuilder(
        		
        	
        		"cmd.exe", "/c", //CMD Befehl
        		"cd \"C:\\Users\\a_orsc01\\Desktop\\ffmpeg\\bin\" &&" //Pfad für Programm
        		+ "ffmpeg " + "-i " + fileName + " -af silencedetect=noise=-50dB:d=1 -f null -"); //Befehl
        
       // TODO silence mit
         
        silenceAusleser.redirectErrorStream(true);
        Process p = silenceAusleser.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }
    }
    
}
