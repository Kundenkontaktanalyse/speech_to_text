
public interface TranslatorInterface {

	public void recognize(String filePath);

	public String getTranscript();

	public float getConfidence();

	public double getLengthOfAudio();

	public void resetInitials();
}
