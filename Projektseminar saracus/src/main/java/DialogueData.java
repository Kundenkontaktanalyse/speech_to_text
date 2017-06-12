
public class DialogueData {
private double dialogueLength;
private String dialogueID;
private String memoryLocationAudioCustomer;
private String memoryLocationAudioAgent;

public DialogueData(double dialogueLength, String dialogueID){
	this.dialogueID=dialogueID;
	this.dialogueLength=dialogueLength;
	memoryLocationAudioCustomer="placeholder";
	memoryLocationAudioAgent="placeholer";
}

}
