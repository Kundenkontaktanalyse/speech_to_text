import java.io.File;

public class Verzeichnismanager extends FileChooser {

	public static void main(String... args) throws Exception {

//		FileChooser myChooser = new FileChooser();
		SpeechToText mySpeechToText = new SpeechToText();
		
		File[][] dataTriples;
		
		dataTriples = mySpeechToText.generateDataTriples();
		
		mySpeechToText.identifyTripleAmount();
		
//		File json;
//		File caInput;
//		File kunInput;
//		
//		json = myChooser.choose();
//		caInput = myChooser.choose();
//		kunInput = myChooser.choose();
	
		
//		for(int i = 0; i < dataTriples.length; i++){
//			
//			mySpeechToText.invokeTranslation(dataTriples[i][0], dataTriples[i][1], dataTriples[i][2]);
//			
//		}
		
		
		

	}

}