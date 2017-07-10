import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class manageJavaInput {

	public void manage(String temp, String fmmpegExeOrdner, File caInput, File kunInput, String idName) {

		System.out.println("manage");
		// -------------------------------------------------------------------------------------------------------

		// Erstellen des Ffmpeg-Outputs aus beiden Channeln

		File caChannel = caInput;
		File kuChannel = kunInput;
		String ffmpegPfadCMD = "cd \"" + fmmpegExeOrdner + "\"";

		ffmpegClass myffmpegClass = new ffmpegClass();

		File kunOut = new File(temp + "\\" +  idName + "_kunOut.txt");
		File caOut = new File(temp + "\\" +  idName + "caOut.txt");
		
				
		myffmpegClass.audioToMetaData(caChannel.getName(), kuChannel.getName(), caOut, kunOut, ffmpegPfadCMD);

		// ---------------------------------------------------------------------------------------------------------

		// warteschleife, bis ffmpeg output existiert

		while (true) {
			if ((kunOut.length() > 0) && (caOut.length() > 0) && (isFileClosed(caOut)) && (isFileClosed(kunOut))) {
				break;
			}
		}

		System.out.println("ffmpeg bearbeitung abgeschlossen");

		// Erstellen der gestutzten Text-dateien mit Start- und End-zeiten aus
		// ffmpeg-output

		TimesCut myCutter = new TimesCut();

		// File caOut = new File(fmmpegExeOrdner + "\\caOut.txt");
		// File kunOut = new File(fmmpegExeOrdner + "\\kunOut.txt");

		File kunStarttimes = new File(temp + "\\" +  idName + "_K1.txt");
		File kunEndtimes = new File(temp + "\\" +  idName + "_K2.txt");
		File caStarttimes = new File(temp + "\\" +  idName + "_C1.txt");
		File caEndtimes = new File(temp + "\\" +  idName + "_C2.txt");

		myCutter.identifyStartTimes(kunOut, kunStarttimes);
		myCutter.identifyEndTimes(kunOut, kunEndtimes);
		myCutter.identifyStartTimes(caOut, caStarttimes);
		myCutter.identifyEndTimes(caOut, caEndtimes);

		// warteschleiße zum Abwarten der input txt files
		while (true) {
			if (((kunStarttimes.length() > 0) && (kunEndtimes.length() > 0) && (caStarttimes.length() > 0)
					&& (caEndtimes.length() > 0))

					&& ((isFileClosed(kunStarttimes)) && (isFileClosed(kunEndtimes)) && (isFileClosed(caStarttimes))
							&& (isFileClosed(caEndtimes)))) {
				break;
			}
		}

		kunOut.delete();
		caOut.delete();
		System.out.println("ffmpeg output in txt-datei geschnitten");
	}

	@SuppressWarnings("resource")
	private boolean isFileClosed(File file) {

		boolean closed;
		FileChannel channel = null;

		try {
			channel = new RandomAccessFile(file, "rw").getChannel();
			closed = true;

		} catch (Exception ex) {
			closed = false;
		} finally {
			if (channel != null) {

				try {
					channel.close();
				} catch (IOException ex) {
					// exception handling
				}
			}
		}

		return closed;

	}
}