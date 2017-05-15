// Imports the Google Cloud client library
import com.google.cloud.speech.spi.v1.SpeechClient;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.LongRunningRecognizeResponse;
import com.google.api.gax.grpc.OperationFuture;

import java.io.*;
import java.util.List;
import javax.sound.sampled.*;

public class AsyncProcess {
	int sampleRate;

	public void asyncRecognizeGcs(String gcsUri) throws Exception, IOException {
		
		try{
			File file=new File(gcsUri);
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
			AudioFormat format = fileFormat.getFormat(); // eine ID
			 sampleRate=(int) format.getSampleRate();
		}
		catch (Exception e) {System.out.println(e);}
		
		TextBundler bundler=new TextBundler();
		  // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
		  SpeechClient speech = SpeechClient.create();
		//  System.out.println(sampleRate);
		  // Configure remote file request for Linear16
		  RecognitionConfig config = RecognitionConfig.newBuilder()
		      .setEncoding(AudioEncoding.LINEAR16)
		      .setLanguageCode("de-DE")
		      .setSampleRateHertz(16000)
		      .build();
		  RecognitionAudio audio = RecognitionAudio.newBuilder()
		      .setUri(gcsUri)
		      .build();

		  // Use non-blocking call for getting file transcription
		  OperationFuture<LongRunningRecognizeResponse> response =
		      speech.longRunningRecognizeAsync(config, audio);
		  while (!response.isDone()) {
		    System.out.println("Waiting for response...");
		    Thread.sleep(10000);
		  }

		  List<SpeechRecognitionResult> results = response.get().getResultsList();
		 

		  for (SpeechRecognitionResult result: results) {
		    List<SpeechRecognitionAlternative> alternatives = result.getAlternativesList();
		    for (SpeechRecognitionAlternative alternative: alternatives) {
		    //  System.out.printf("Transcription: %s%n", alternative.getTranscript());
		      bundler.addTextAsync(alternative.getTranscript());
		      bundler.setKonfidenz(alternative.getConfidence());
		    }
		  }
		  speech.close();
		}	
	
}
