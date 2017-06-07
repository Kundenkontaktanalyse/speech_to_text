 
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.sound.sampled.*;

import com.google.cloud.speech.spi.v1.SpeechClient;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechContext;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.protobuf.ByteString;

// Stand: 27.04.
// letzte Bearbeitung durch: Daniel
// 
// Splitter arbeitet nur mit WAV Dateien bisher.
// Benennung korrekt
// Aufteilung funktioniert 


class AudioProcessing extends FileChooser {	
	static TextBundler bundler= new TextBundler();
	String sourceFileName;
	int sampleRate;
	double dauerges;
	String role;
	String transcript;
	float confidence;
	
	
	public void recognize(String fileName) throws Exception, IOException {
		 
		  SpeechClient speech = SpeechClient.create();
		  
		  Path path = Paths.get(fileName);
		  byte[] data = Files.readAllBytes(path);
		  ByteString audioBytes = ByteString.copyFrom(data);
		  // Configure request with local raw PCM audio
		  SpeechContext context=SpeechContext.newBuilder()
				  .addPhrases("Boolean" )
				  .addPhrases("building")
				  .addPhrases("circuits")
				  .addPhrases("switching")
				  .addPhrases("installment")
				  .build();
		  
		  RecognitionConfig config = RecognitionConfig.newBuilder()
		      .setEncoding(AudioEncoding.LINEAR16)
		      .setLanguageCode("de-DE")
		      .setSampleRateHertz(sampleRate)
		      .addSpeechContexts(0, context)
		      .build();
				  
		  RecognitionAudio audio = RecognitionAudio.newBuilder()
		      .setContent(audioBytes)
		      .build();

		  // Use blocking call to get audio transcript
		  RecognizeResponse response = speech.recognize(config, audio);
		  
		  List<SpeechRecognitionResult> results = response.getResultsList();

		  for (SpeechRecognitionResult result: results) {
		    List<SpeechRecognitionAlternative> alternatives = result.getAlternativesList();
		    for (SpeechRecognitionAlternative alternative: alternatives) {
		    	
		    	if(transcript==null){transcript=alternative.getTranscript();
		    	}
		    	else transcript=transcript+ alternative.getTranscript();
		    	confidence=confidence+alternative.getConfidence();
		      bundler.addTextSync(alternative.getTranscript());
		      System.out.println(alternative.getConfidence());
		    }
		  }
		  speech.close();
		
		}
	
	
	public void processAudio(File sourceFile, String role) {
		
		String sourceFilePath = sourceFile.getPath().toString();
		String destinationFileName = sourceFilePath;
		AudioInputStream inputStream = null;
		AudioInputStream shortenedStream = null;
		int secondsToCopy = 50;
		System.out.println("processAudio");
        System.out.println("");

		try {
			File file = new File(sourceFilePath);
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
			AudioFormat format = fileFormat.getFormat(); // eine ID
			double audioFileLength = file.length(); // get Length des inputs
			inputStream = AudioSystem.getAudioInputStream(file);
			sampleRate=(int) format.getSampleRate();
			dauerges=(inputStream.getFrameLength()/format.getFrameRate());
			
			
			
			int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate(); // hz*bytes/frames
			double audioSekunden = audioFileLength / bytesPerSecond;
			int längeAudio = (int) audioSekunden + 1; // aufrunden der Sekunden
			long framesOfAudioToCopy = secondsToCopy * (int) format.getFrameRate();
			int AnzahlSchnitte = (längeAudio / secondsToCopy);
			
			// Korrektur der Anzahl von Schnitten im Falle von Restsekunden
			if ((längeAudio % secondsToCopy) != 0) {
				AnzahlSchnitte += 1;
			}

			String origdestinationFileName = destinationFileName;

			
			
			// Gedankenfalle skip:
			// Vermutung: Durch die Erstellung des shortenedStream wird intern ein Marker weitergesetzt, der das skip unnötig werden lässt
			// ansonsten zweimaliges Überspringen mit lücken

			for (int i = 1; i <= AnzahlSchnitte; i++) {
				
				
				destinationFileName = origdestinationFileName;
				
				

				shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
				

				// bisher nur .wav, später flexibel gestalten evt.
				int pfadlaenge = destinationFileName.length();
				
				String insert = null;
				if (i<10){
					insert = "_0";
				} else {
					insert = "_";
				}
				destinationFileName = destinationFileName.substring(0, pfadlaenge - 4) + insert + i
						+ destinationFileName.substring(pfadlaenge - 4, pfadlaenge);

				File destinationFile = new File(destinationFileName);
				AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);
				this.recognize(destinationFileName);
				destinationFile.delete();			
				

			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (Exception e) {
					System.out.println(e);
				}
			if (shortenedStream != null)
				try {
					shortenedStream.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		System.out.println("role"+role+ " Tra:" + transcript);
		bundler.addSnippet(role, transcript, dauerges, (confidence/((int)(dauerges/15)+1)));
		transcript=null;
		confidence=0;
//		bundler.generiereJSON();
		
	}
	
	
	
}
