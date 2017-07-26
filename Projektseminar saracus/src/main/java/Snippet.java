
public class Snippet {

	private String role; // Rolle des Sprechers (K oder A).
	private String snippetID; // SchnipselID.
	private String transcription; // der Text des Schnipsels.
	private double length; // laenge des Schnipsels mm.ss
	private float confidence; // Konfidenz-Score.
	private int wordCount; // Anzahl Wörter im Schnipsel.
	private double secSinceEndOfProceedingSnippet;
	

	private double endTime;

	private String startTimeMinSec;
	private String endTimeMinSec;

	public Snippet(String role, String transcription, double length, float confidence, double startTime) {
		this.role = role;
		this.transcription = transcription;
		this.length = Math.round(length*1000)/1000.0;
		this.startTimeMinSec = secToMinSec(startTime);
		this.endTimeMinSec = secToMinSec(startTime + length);
		this.endTime = startTime + length;
//		this.secSinceEndOfProceedingSnippet = secSinceEndOfProceedingSnippet;
		
		this.confidence = Double.valueOf(Math.round(confidence*1000)/1000.0).floatValue();
		if (!(this.transcription == null)) {
			this.wordCount = countWords();
		} else
			this.wordCount = 0;

	}

	/**
	 * Method to convert a double value of seconds to a Min:Sec String
	 * 
	 * @param seconds
	 * @return the String in Min:Sec format
	 */
	public String secToMinSec(double seconds) {

		
//		long sec = Math.round(seconds % 60);
		double sec = seconds % 60;
		int mins = (int) (seconds / 60);

		String insert;

		if (sec < 10) {
			insert = ":0";
		} else {
			insert = ":";
		}

		String minSec = mins + insert + sec;

		return minSec;
	}

	public String getStartingTimeMinSec() {
		return startTimeMinSec;
	}

	public void setStartingTimeMinSec(String timeStamp) {
		this.startTimeMinSec = timeStamp;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	public String getSnippetId() {
		return snippetID;
	}

	public void setSnippetId(String id) {
		this.snippetID = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getTranscription() {
		return transcription;
	}

	public void setTranscription(String transcription) {
		this.transcription = transcription;
	}

	public double getLenght() {
		return length;
	}

	public void setLenght(double lenght) {
		this.length = lenght;
	}

	public float getConfidence() {
		return confidence;
	}

	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}
	
	
	public double secSinceEndOfProceedingSnippet() {
		return secSinceEndOfProceedingSnippet;
	}
	
	public String getStartTimeMinSec() {
		return startTimeMinSec;
	}

	public String getEndTimeMinSec() {
		return endTimeMinSec;
	}
	
	public double getEndTime() {
		return endTime;
	}
	
	public void setSecSinceEndOfProceedingSnippet(double secSinceEndOfProceedingSnippet) {
		this.secSinceEndOfProceedingSnippet = secSinceEndOfProceedingSnippet;
	}
	


	public int countWords() {

		String inputString = transcription;

		// Abschnitt zur Bestimmung der Anzahl der Startzeiten

		String[] stringWords = inputString.split("\\s+");

		return stringWords.length;

	}

}
