
public class DialogueData {
private double dialogueLength;
private String dialogueID;
private String memoryLocationAudioCustomer;
private String memoryLocationAudioAgent;
private int speakerChanges;

public DialogueData(double dialogueLength, String dialogueID, int speakerChanges){
	this.dialogueID=dialogueID;
	this.dialogueLength=dialogueLength;
	this.speakerChanges=speakerChanges;
	memoryLocationAudioCustomer="placeholder";
	memoryLocationAudioAgent="placeholer";
	
}

}
