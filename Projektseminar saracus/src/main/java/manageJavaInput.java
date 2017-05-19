import java.io.File;

public class manageJavaInput extends FileChooser {

	public void manage() {

		
		// Identifikaiton des Prozessordners
		
		String fmmpegExeOrdner = choose().getParent();

// -------------------------------------------------------------------------------------------------------
		
		// Erstellen des Ffmpeg-Outputs aus beiden Channeln
		
//		File caChannel = choose();
//		File kuChannel = choose();
//		String ffmpegPfadCMD = "cd \"" + fmmpegExeOrdner + "\"";
//
//		ffmpegClass myffmpegClass = new ffmpegClass();
//		
//
//		File caOut = new File(fmmpegExeOrdner + "\\caOut.txt");
//		File kunOut = new File(fmmpegExeOrdner + "\\kunOut.txt");
//		myffmpegClass.audioToMetaData(caChannel.getName(),kuChannel.getName(),caOut, kunOut, ffmpegPfadCMD);

//	---------------------------------------------------------------------------------------------------------
		
		// Erstellen der gestutzten Text-dateien mit Start- und End-zeiten aus ffmpeg-output
		
		TimesCut myCutter = new TimesCut();
		
		File caOut = new File(fmmpegExeOrdner + "\\caOut.txt");
		File kunOut = new File(fmmpegExeOrdner + "\\kunOut.txt");
		
		File caStarttimes = new File(fmmpegExeOrdner + "\\C1.txt");
		File kunStarttimes = new File(fmmpegExeOrdner + "\\K1.txt");
		File caEndtimes = new File(fmmpegExeOrdner + "\\C2.txt");
		File kunEndtimes = new File(fmmpegExeOrdner + "\\K2.txt");

		myCutter.identifyStartTimes(caOut, caStarttimes);
		myCutter.identifyStartTimes(kunOut, kunStarttimes);
		myCutter.identifyEndTimes(caOut, caEndtimes);
		myCutter.identifyEndTimes(kunOut, kunEndtimes);

		caOut.delete();
		kunOut.delete();
	}
}
