import java.io.File;
import java.io.IOException;

public class ffmpegClass extends FileChooser {

	String ffmpegPfad = "cd \"C:\\Users\\d_tham01\\Desktop\\ffmpeg\\bin\"";
	String inputFileCA = choose().getName();
	String inputFileKun = choose().getName();
	String settingsFfmpeg = " -af silencedetect=noise=-50dB:d=1 -f null -";

	public void pBuild(File outputCa, File outputKun) throws IOException {
		// Prozessinstanz für ffmpeg
		ProcessBuilder ffmpeg = new ProcessBuilder(

				// CMD ausführen
				"cmd.exe", "/c",
				// Command Line Einträge
				ffmpegPfad + "&&" + "ffmpeg -i " + inputFileCA + settingsFfmpeg + " 2> " + outputCa.toString() 
				+ " &&" + ffmpegPfad
				+ "&&" + "ffmpeg -i " + inputFileKun + settingsFfmpeg + " 2> " + outputKun.toString() );

		// Fehlermeldung der CMD weiterleiten an JAVA
		ffmpeg.redirectErrorStream(true);

		// Start der Prozessinstanz, welche die command line ausführt
		ffmpeg.start();
	}

	
//	public static void main (String [] args) {
		
//		File catext = new File("C:\\Users\\a_orsc01\\Desktop\\caOut.txt");
//		
//		
//		File kuntext = new File("C:\\Users\\a_orsc01\\Desktop\\kunOut.txt");
//		
//		
//		ffmpegClass test = new ffmpegClass();
//		try {
//			test.pBuild(catext, kuntext);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}