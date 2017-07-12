import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Snippet {
	
	private String role; // Rolle des Sprechers (K oder A).
	private String snippetID; //SchnipselID.
	private String transcription; // der Text des Schnipsels.
	private double length; // laenge des Schnipsels mm.ss
	private float confidence; // Konfidenz-Score.
	private int wordCount; //Anzahl Wörter im Schnipsel.
	private String startingTimeMinSec = "2" ;
	
	
	public Snippet(String role, String transcription, double length, float confidence){
		this.role=role;
		this.transcription=transcription;
//		System.out.println("length:"+ (this.transcription.length()-1));
		this.length=length;
		this.confidence=confidence;
		if(!(this.transcription == null )){
			this.wordCount=countWords();
		}
		else
			this.wordCount=0;
		
	}
	
	
	
	
	public String getStartingTimeMinSec() {
		return startingTimeMinSec;
	}




	public void setStartingTimeMinSec(String timeStamp) {
		this.startingTimeMinSec = timeStamp;
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
	


public  int countWords(){
	

		String inputString = transcription;

		// Abschnitt zur Bestimmung der Anzahl der Startzeiten
		
		
		String[] stringWords = inputString.split("\\s+");

		
		return stringWords.length;
	

}


	

}
