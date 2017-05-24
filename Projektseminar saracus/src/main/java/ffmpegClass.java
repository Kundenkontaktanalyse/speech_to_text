import java.io.File;
import java.io.IOException;

public class ffmpegClass extends FileChooser {

	String settingsFfmpeg = " -af silencedetect=noise=-35dB:d=1 -f null -";

	public void audioToMetaData(String inputFileCA, String inputFileKun, File caOut, File kunOut,
			String ffmpegPfadCMD) {
		// Prozessinstanz f�r ffmpeg
		ProcessBuilder ffmpeg = new ProcessBuilder(

				// CMD ausf�hren
				"cmd.exe", "/c",
				// Command Line Eintr�ge
				ffmpegPfadCMD + "&&" + "ffmpeg -i " + inputFileCA + settingsFfmpeg + " 2> " + caOut.toString() + " &&"
						+ ffmpegPfadCMD + "&&" + "ffmpeg -i " + inputFileKun + settingsFfmpeg + " 2> "
						+ kunOut.toString());

		// Fehlermeldung der CMD weiterleiten an JAVA
		ffmpeg.redirectErrorStream(true);

		// Start der Prozessinstanz, welche die command line ausf�hrt
		try {
			ffmpeg.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//
}