import java.text.SimpleDateFormat;
import java.util.Date;

public class LogEntry {
	
	private String dialogueID;
	private Date processStart;
	private Date processEnd;
	private long processDuration;
//	private String 
	
	
	private String ffmpegSuccess = "ffmpeg successfully completed";
	private String cutSuccess = "audiocutting successfully completed";
	private String transcriptionSuccess = "transcription successfully completed";
	private String outputBuildSuccess = "";
	// private String outputBuildSuccess = "output build successfully completed";
	private boolean ffmpegSuccessState = true;
	private boolean cutSuccessState = true;
	private boolean transcriptionSuccessState = true;
	private boolean outputBuildSuccessState = true;

	public LogEntry(String dialogueID, Date processStart) {
		this.dialogueID = dialogueID;
		this.processStart = processStart;
	}

	public String getDialogueID() {
		return dialogueID;
	}

	public void setDialogueID(String dialogueID) {
		this.dialogueID = dialogueID;
	}

	public String getProcessStartSdf() {
		SimpleDateFormat time= new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	    String processStartSdf= time.format(processStart);		
		return processStartSdf;
	}
	
	public Date getProcessStart() {
		return processStart;
	}

	public void setProcessStart(Date processStart) {
		this.processStart = processStart;
	}

	public String getProcessEndSdf() {
		SimpleDateFormat time= new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	    String processEndSdf= time.format(processEnd);		
		return processEndSdf;
	}

	public Date getProcessEnd() {
		return processEnd;
	}

	public void setProcessEnd(Date processEnd) {
		this.processEnd = processEnd;
	}

	public long getProcessDuration() {
		return processDuration;
	}

	public void setProcessDuration(long processDuration) {
		this.processDuration = processDuration;
	}

	public String getFfmpegSuccess() {
		return ffmpegSuccess;
	}

	public void setFfmpegSuccess(String ffmpegSuccess) {
		this.ffmpegSuccess = ffmpegSuccess;
	}

	public String getCutSuccess() {
		return cutSuccess;
	}

	public void setCutSuccess(String cutSuccess) {
		this.cutSuccess = cutSuccess;
	}

	public String getTranscriptionSuccess() {
		return transcriptionSuccess;
	}

	public void setTranscriptionSuccess(String transcriptionSuccess) {
		this.transcriptionSuccess = transcriptionSuccess;
	}

	public String getOutputBuildSuccess() {
		return outputBuildSuccess;
	}

	public void setOutputBuildSuccessError(String outputBuildSuccess) {
		this.outputBuildSuccess = this.outputBuildSuccess + outputBuildSuccess + "\n";
	}

	public void setOutputBuildSuccess(String outputBuildSuccess) {
		this.outputBuildSuccess = outputBuildSuccess;
	}

	public boolean isFfmpegSuccessState() {
		return ffmpegSuccessState;
	}

	public void setFfmpegSuccessState(boolean ffmpegSuccessState) {
		this.ffmpegSuccessState = ffmpegSuccessState;
	}

	public boolean isCutSuccessState() {
		return cutSuccessState;
	}

	public void setCutSuccessState(boolean cutSuccessState) {
		this.cutSuccessState = cutSuccessState;
	}

	public boolean isTranscriptionSuccessState() {
		return transcriptionSuccessState;
	}

	public void setTranscriptionSuccessState(boolean transcriptionSuccessState) {
		this.transcriptionSuccessState = transcriptionSuccessState;
	}

	public boolean isOutputBuildSuccessState() {
		return outputBuildSuccessState;
	}

	public void setOutputBuildSuccessState(boolean outputBuildSuccessState) {
		this.outputBuildSuccessState = outputBuildSuccessState;
	}
}