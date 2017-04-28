
// Imports the Google Cloud client library
import com.google.cloud.speech.spi.v1.SpeechClient;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;import javax.swing.JFileChooser;

public class QuickstartSample {
 public static void main(String... args) throws Exception {
      
      // Initiates filePath
      String filePath=null;      
      // Instantiates a client
   SpeechClient speech = SpeechClient.create();
   // JFileChooser-Objekt erstellen
   JFileChooser chooser = new JFileChooser();
   // Dialog zum Oeffnen von Dateien anzeigen
   int rueckgabeWert = chooser.showOpenDialog(null);
   
   /* Abfrage, ob auf "Öffnen" geklickt wurde */
   if(rueckgabeWert == JFileChooser.APPROVE_OPTION)
   {
        // Ausgabe der ausgewaehlten Datei
       //Hier eigentlich Aufruf der Funktion
     
       filePath = chooser.getSelectedFile().getPath().toString();
   }
   
//FileSplitter a=new FileSplitter();

   Transcription a= new Transcription(filePath);
 } 

}