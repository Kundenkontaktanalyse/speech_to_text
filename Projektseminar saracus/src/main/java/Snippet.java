
public class Snippet {
	
	private String role; // Rolle des Sprechers (K oder A).
	private String transcription; // der Text des Schnipsels.
	private double length; // laenge des Schnipsels mm.ss
	private float confidence; // Konfidenz-Score
	
	public Snippet(String role, String transcription, double length, float confidence){
		this.role=role;
		this.transcription=transcription;
		this.length=length;
		this.confidence=confidence;
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
	
	
	

}
