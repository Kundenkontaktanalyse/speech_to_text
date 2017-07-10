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

public class AsyncProcess {

	public void asyncRecognizeGcs(String gcsUri) throws Exception, IOException {
		
//		TextBundler bundler=new TextBundler();
		  // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
		  SpeechClient speech = SpeechClient.create();

		  // Configure remote file request for Linear16
		  RecognitionConfig config = RecognitionConfig.newBuilder()
		      .setEncoding(AudioEncoding.LINEAR16)
		      .setLanguageCode("de-DE")
		      //.setSampleRateHertz(44100)
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
		      System.out.printf("Transcription: %s%n", alternative.getTranscript());
		    }
		  }
		  speech.close();
		}	
	
//	main aufruf:
	// AudioProcessing audio= new AudioProcessing();
			// audio.processAudio();
			// AsyncProcess process= new AsyncProcess();
			// process.asyncRecognizeGcs("gs://audio_projektseminar/TestDateien/channel1.wav");
	
	
}
