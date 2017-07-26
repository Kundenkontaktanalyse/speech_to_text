import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


//	TODO: Arary out of bounds in initalizeData audioChannels bei transkription von 3 Gesprächen
// erste beiden kurzen gehen, der dritte lange nicht

public class Verzeichnismanager extends Object {
	
	Logmanager myLogmanager;

	public static void main(String... args) throws Exception {

		Verzeichnismanager myVerzeichnismanager = new Verzeichnismanager();
		myVerzeichnismanager.run();
	}

	public void run() throws Exception {
		
		myLogmanager = new Logmanager();

		Properties properties = new Properties();
		BufferedInputStream stream = new BufferedInputStream(new FileInputStream("s2tProperties.properties"));
		properties.load(stream);
		stream.close();
		String ffmpegpath = properties.getProperty("ffmpegpath");
		String gespraechsVerzeichnisPfad = properties.getProperty("gespraechsVerzeichnisPfad");
		String translator = properties.getProperty("translator");

		File globalFfmpegExe = new File(ffmpegpath);
		File gespraechsOrdnerVerzeichnis = new File(gespraechsVerzeichnisPfad);
		File[] gespraechsOrdnerArray = gespraechsOrdnerVerzeichnis.listFiles();
		String gespraechsUeberVerzeichnis = gespraechsOrdnerVerzeichnis.getParent();

		// Outputordner auf gespraechsVerzeichnis-ebene erstellen
		File temp = new File(gespraechsUeberVerzeichnis + "\\temp");
		temp.mkdir();

		// Outputordner auf gespraechsVerzeichnis-ebene erstellen
		File output = new File(gespraechsUeberVerzeichnis + "\\output");
		output.mkdir();
		
		
		File log = new File(gespraechsUeberVerzeichnis + "\\log");
        log.mkdir();
		// String[] translatedDailogIdNames =
		// getTranslatedDialogIdNames(output);
		// setIdToNewFiles(translatedDailogIdNames, gespraechsOrdnerArray,
		// gespraechsOrdnerVerzeichnis);

		SpeechToText mySpeechToText = new SpeechToText(translator);

		// File json;
		// File caInput;
		// File kunInput;

		// --------------------------------------------------------------------------------------------------------------------

		// json = myChooser.choose();
		// caInput = myChooser.choose();
		// kunInput = myChooser.choose();

		// ---------------------------------------------------------------------------------------------------------------------

		for (int i = 0; i < gespraechsOrdnerArray.length; i++) {

			
			// Kontrolle ob Gespraech bereits transkribiert wurde, innerhalb der
			// schleife um
			// Liste bei Durchlauf zu aktualisieren
			String[] translatedDailogIdNames = getTranslatedDialogIdNames(output);

			if (checkNameExistenceInOutput(gespraechsOrdnerArray[i].getName(), translatedDailogIdNames)) {
				System.out.println("Gespraech bereits transkribiert");
			}

			if (!checkNameExistenceInOutput(gespraechsOrdnerArray[i].getName(), translatedDailogIdNames)) {
				myLogmanager.addLogEntry(gespraechsOrdnerArray[i].getName());

				System.out.println(gespraechsOrdnerArray[i].getPath());
				File lokalFfmpeg = new File(gespraechsOrdnerArray[i].getPath() + "\\ffmpeg.exe");
				copyFile(globalFfmpegExe, lokalFfmpeg);

				File[] gespraechsDateien = gespraechsOrdnerArray[i].listFiles();

				for (int j = 0; j < gespraechsDateien.length; j++) {
					System.out.println(gespraechsDateien[j]);
				}
				Arrays.sort(gespraechsDateien);
				mySpeechToText.invokeTranslation(output.toString(), gespraechsDateien[1], gespraechsDateien[2],
						gespraechsDateien[3], gespraechsOrdnerArray[i], temp, lokalFfmpeg);
				
				myLogmanager.logList.getLast().setProcessEnd(new Date());
				
				myLogmanager.logList.getLast().setProcessDuration(myLogmanager.getDateDiff(
						myLogmanager.logList.getLast().getProcessStart(),
						myLogmanager.logList.getLast().getProcessEnd(),
						TimeUnit.SECONDS));
			}
		}
		
		myLogmanager.printLogsInTxt(log);
		
		System.out.println("SUCCESSFULLY COMPLETED");
	}

	@SuppressWarnings("resource")
	public void copyFile(File in, File out) throws IOException {
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

	/**
	 * Ermittelt die im Output-Ordner abgelegten Transkripte
	 * 
	 * @param output
	 *            der Output-Ordner
	 * @return String[] mit den IDNamen der transkribierten Gespraeche
	 */
	public String[] getTranslatedDialogIdNames(File output) {

		String[] translatedDailogFileNames = output.list();
		String[] translatedDailogIdNames = new String[translatedDailogFileNames.length];

		System.out.println("Bereits transkribierte Gespraeche:");
		for (int i = 0; i < translatedDailogFileNames.length; i++) {
			translatedDailogIdNames[i] = translatedDailogFileNames[i].substring(0,
					translatedDailogFileNames[i].length() - 5);

			System.out.println(translatedDailogIdNames[i]);
		}
		return translatedDailogIdNames;
	}

	/**
	 * Methode zum
	 * 
	 * @param DialogIdName
	 * @param translatedDailogIdNames
	 * @return
	 */
	public boolean checkNameExistenceInOutput(String DialogIdName, String[] translatedDailogIdNames) {

		boolean isTranslated = false;

		for (int i = 0; i < translatedDailogIdNames.length; i++) {
			if (DialogIdName.equals(translatedDailogIdNames[i])) {
				isTranslated = true;
			}
		}
		return isTranslated;
	}

	/**
	 * Method to set a unique ID to all dialoguefolders in the directory that
	 * have not been already transcripted
	 * 
	 * @param translatedDailogIdNames
	 *            an array containing the actually transcripted dialogues
	 *            identified by ID
	 * @param gespraechsOrdnerArray
	 *            an array containing all folders with the dialogues to be
	 *            checked for transcription
	 * @param gespraechsOrdnerVerzeichnis
	 *            the directory containing all folders with the dialogues to be
	 *            checked for transcription
	 */
	public void setIdToNewFiles(String[] translatedDailogIdNames, File[] gespraechsOrdnerArray,
			File gespraechsOrdnerVerzeichnis) {

		String[] endings = { ".json", ".wav", ".wav" };
		for (int i = 0; i < gespraechsOrdnerArray.length; i++) {

			// Kontrolle ob bereits transkribiert
			if (!checkNameExistenceInOutput(gespraechsOrdnerArray[i].getName(), translatedDailogIdNames)) {

				String newId = generateNewId(translatedDailogIdNames);

				File newIdName = new File(gespraechsOrdnerVerzeichnis.getPath() + "\\" + newId);
				newIdName.mkdir();
				// gespraechsOrdnerArray[i].renameTo(newIdName);

				System.out.println("RRRRRRRRRRRRRRRR" + newIdName.getPath());

				File[] files = gespraechsOrdnerArray[i].listFiles();
				Arrays.sort(files);
				// Dateien im Ordner umbenennen
				for (int j = 0; j < files.length; j++) {
					File newNameFile = new File(
							newIdName.toString() + "\\" + newIdName.getName() + "_" + j + endings[j]);
					// files[j].renameTo(newNameFile);
					try {
						copyFile(files[j], newNameFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					files[j].delete();
				}

				gespraechsOrdnerArray[i].delete();
			}
		}
		System.out.println("RENAMED");
	}

	public String generateNewId(String[] existingIds) {

		GenerateUUID newIDgenerator = new GenerateUUID();
		String newId = newIDgenerator.generiereStringID();

		if (checkNameExistenceInOutput(newId, existingIds)) {
			newId = generateNewId(existingIds);
		}

		return newId;
	}

}