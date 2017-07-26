import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class Logmanager {
	public LinkedList<LogEntry> logList = new LinkedList<LogEntry>();
	private String completeLog;

	public String getCompleteLog() {
		return completeLog;
	}

	public void addLogMessage(String message) {
		this.completeLog = message;
	}

	public void addLogEntry(String dialogueId) {
		logList.add(new LogEntry(dialogueId, new Date()));
	}

	public void calculateProcessDurations() {
		for (

				int i = 0; i < logList.size(); i++) {
			Date date1 = logList.get(i).getProcessStart();
			Date date2 = logList.get(i).getProcessEnd();
			logList.get(i).setProcessDuration(getDateDiff(date1, date2, TimeUnit.SECONDS));
		}
	}

	public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	public void printLogsInTxt(File logDir) throws IOException {

		SimpleDateFormat time = new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss");
		Date now = new Date();
		String sdf = time.format(now);

		File currentLog = new File(logDir.getPath() + "//LogData" + sdf + ".txt");
		String logString = "";

		for (int i = 0; i < logList.size(); i++) {
			LogEntry currentEntry = logList.get(i);

			logString = logString +

					"DialogueID: " + currentEntry.getDialogueID() + " // ProcessStart: "
					+ currentEntry.getProcessStartSdf() + " // ProcessEnd: " + currentEntry.getProcessEndSdf()
					+ " // ProcessDuration(Seconds): " + currentEntry.getProcessDuration() + "\n" + "Step 1) Ffmpeg-processing: "
					+ currentEntry.getFfmpegSuccess() + "\n" + "Step 2) AudioCutting: " + currentEntry.getCutSuccess()
					+ "\n" + "Step 3) Transcription: " + currentEntry.getTranscriptionSuccess() + "\n"
					+ "Step 4) Output: " + currentEntry.getOutputBuildSuccess() + "\n" 
					+ "=====================================================================================\n";

		}
		BufferedWriter out = new BufferedWriter(new FileWriter(currentLog));
		out.write(logString);
		out.close();
	}

}