import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class silenceAuslesen {
	
	static String fileName = "channel1.wav"; // Variable für Dateinamen
	final static String VERZEICHNIS = ("C:\\Users\\a_orsc01\\Desktop\\newfile.txt\");
	
    public static void main(String[] args) throws Exception {
    	
    	 
              try
              {
                  BufferedReader bReader = new BufferedReader(new FileReader(VERZEICHNIS));
                  
                  String Hallo = null;
                  String Zeile = bReader.readLine();
                  while (true)
                  {
                      Zeile = bReader.readLine();
                      if (Zeile.contains("silence_start"))
                      {
                    	  Hallo = Zeile;
                      }  
                       
                      if (Zeile == null) { break; }
                      
                      System.out.println(Hallo);
                     
                  }
              }
              catch(IOException ioe)
              {
                  ioe.printStackTrace();
              }
          
    }}
    

