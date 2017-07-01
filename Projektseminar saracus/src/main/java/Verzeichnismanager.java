import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Properties;

public class Verzeichnismanager extends FileChooser {

	public static void main(String... args) throws Exception {

		Properties properties = new Properties();
		BufferedInputStream stream = new BufferedInputStream(new FileInputStream("s2tProperties.properties"));
		properties.load(stream);
		stream.close();
		String ffmpegpath = properties.getProperty("ffmpegpath");
		String gespraechsVerzeichnisPfad = properties.getProperty("gespraechsVerzeichnisPfad");

		File globalFfmpegExe = new File(ffmpegpath);
		File gespraechsVerzeichnisOrdner = new File(gespraechsVerzeichnisPfad);
		File[] gespraechsOrdner = gespraechsVerzeichnisOrdner.listFiles();
		String gespraechsUeberVerzeichnis = gespraechsVerzeichnisOrdner.getParent();

		// Outputordner auf gespraechsVerzeichnis-ebene erstellen
		File output = new File(gespraechsUeberVerzeichnis + "\\output");
		output.mkdir();

		FileChooser myChooser = new FileChooser();
		SpeechToText mySpeechToText = new SpeechToText();

		File json;
		File caInput;
		File kunInput;

		// --------------------------------------------------------------------------------------------------------------------

		// json = myChooser.choose();
		// caInput = myChooser.choose();
		// kunInput = myChooser.choose();

		// ---------------------------------------------------------------------------------------------------------------------

		for (int i = 0; i < gespraechsOrdner.length; i++) {

			File lokalFfmpeg = new File(gespraechsOrdner[i].toString() + "\\ffmpeg.exe");
			copyFile(globalFfmpegExe, lokalFfmpeg);

			File[] gespraechsDateien = gespraechsOrdner[i].listFiles();

			for (int j = 0; j < gespraechsDateien.length; j++) {
				System.out.println(gespraechsDateien[j]);
			}

			mySpeechToText.invokeTranslation(gespraechsOrdner[i].getName(), output.toString(), gespraechsDateien[1],
					gespraechsDateien[2], gespraechsDateien[3], lokalFfmpeg);

		}
	}

	@SuppressWarnings("resource")
	public static void copyFile(File in, File out) throws IOException {
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			inChannel = new FileInputStream(in).getChannel();
			outChannel = new FileOutputStream(out).getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (inChannel != null)
					inChannel.close();
				if (outChannel != null)
					outChannel.close();
			} catch (IOException e) {
			}
		}

	}

}