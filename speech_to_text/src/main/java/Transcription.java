import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.cloud.speech.spi.v1.SpeechClient;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.protobuf.ByteString;

public class Transcription {
	
	// Initiates filePath
	 private String filePath=null;  
	public Transcription(String path) throws Exception{
		this.filePath=path;
		transcribe();
		
	}
	
	
	private void transcribe() throws Exception{
	  
       
    // Instantiates a client
 SpeechClient speech = SpeechClient.create();
 
 
	 Path path = Paths.get(filePath);
	 
	   byte[] data = Files.readAllBytes(path);
	   
	ByteString audioBytes = ByteString.copyFrom(data);
	// Builds the sync recognize request
	
	   RecognitionConfig config = RecognitionConfig.newBuilder()
	       .setEncoding(AudioEncoding.LINEAR16)
	       .setSampleRateHertz(44100)
	       .setLanguageCode("de-DE")
	       .build();
	   RecognitionAudio audio = RecognitionAudio.newBuilder()
	       .setContent(audioBytes)
	       .build();    // Performs speech recognition on the audio file
	   RecognizeResponse response = speech.recognize(config, audio);
	   List<SpeechRecognitionResult> results = response.getResultsList();  
	   for (SpeechRecognitionResult result: results) {
	     List<SpeechRecognitionAlternative> alternatives = result.getAlternativesList();
	     for (SpeechRecognitionAlternative alternative: alternatives) {
	       System.out.printf("Transcription: %s%n", alternative.getTranscript());
	       TextBundler a= new TextBundler(alternative.getTranscript());
	       a.printErgebnis();
	     }
	   }
	   speech.close();
}
}

