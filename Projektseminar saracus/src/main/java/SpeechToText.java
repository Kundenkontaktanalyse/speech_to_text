import java.io.File;
import java.io.IOException;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class SpeechToText extends AudioProcessing {
	public static void main(String... args) throws Exception {

//		File caOut = new File("C:\\Users\\Daniel\\Desktop\\Meeting\\caOut.txt");
//		File kunOut = new File("C:\\Users\\Daniel\\Desktop\\Meeting\\kunOut.txt");
//		ffmpegClass myffmpegClass = new ffmpegClass();
//		myffmpegClass.pBuild(caOut, kunOut);

		// -----------------------------------------------------------------------------------

//		File catimes = new File("C:\\Users\\Daniel\\Desktop\\Meeting\\GeschnitteneAudios\\catimes.txt");
//		File kuntimes = new File("C:\\Users\\Daniel\\Desktop\\Meeting\\GeschnitteneAudios\\kuntimes.txt");
//
//		TimesCut myTimesCut = new TimesCut();
//
//		myTimesCut.logic(catimes);
//
//		myTimesCut.logic(kuntimes);

		// ----------------------------------------------------------------------------------
//
//		SpeakerSeperation spsep = new SpeakerSeperation();
//		spsep.processFiles();

		
		 FileChooser myC = new FileChooser();
		 test2 mytest2 = new test2();
		 SpeakerSeperation SS = new SpeakerSeperation();
		 
		 File startzeitenTXT = myC.choose();
		 File endzeitenTXT = myC.choose();
		 double[] startzeiten = SS.TextToTime(startzeitenTXT);
		 double[] endzeiten = SS.TextToTime(endzeitenTXT);
		
		 mytest2.cutAudio(startzeiten, endzeiten);
		 
		// AudioProcessing audio= new AudioProcessing();
		// audio.processAudio();
		// AsyncProcess process= new AsyncProcess();
		// process.asyncRecognizeGcs("gs://audio_projektseminar/TestDateien/channel1.wav");

	}
}
