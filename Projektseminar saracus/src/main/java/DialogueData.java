
public class DialogueData {
private double dialogueLength;
private String dialogueID;
private String memoryLocationAudioCustomer;
private String memoryLocationAudioAgent;
private int speakerChanges;
private double avgWordsPerDialogueFragmentAgent;
private double avgWordsPerDialogueFragmentCustomer;
private double avgWordsUntilSpeakerChange;



public DialogueData(double dialogueLength, String dialogueID, int speakerChanges, double wordsPerDialogueA, double wordsPerDialogueC){
	this.dialogueID=dialogueID;
	this.dialogueLength=dialogueLength;
	this.speakerChanges=speakerChanges;
	memoryLocationAudioCustomer="placeholder";
	memoryLocationAudioAgent="placeholer";
	this.avgWordsPerDialogueFragmentAgent=wordsPerDialogueA;
	this.avgWordsPerDialogueFragmentCustomer=wordsPerDialogueC;
	this.avgWordsUntilSpeakerChange= (this.avgWordsPerDialogueFragmentAgent+this.avgWordsPerDialogueFragmentCustomer)/2;
}

}
