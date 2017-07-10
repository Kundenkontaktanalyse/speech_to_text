
public class DialogueData {
	private double dialogueLength;
	private String dialogueID;
	private String memoryLocationAudioCustomer;
	private String memoryLocationAudioAgent;
	private int speakerChanges;
	private int dialogueFragmentsOfAgent;
	private int dialogueFragmentsOfCustomer;
	private double numberOfWordsAgent;
	private double numberOfWordsCustomer;
	private double speechProportionCustomerToAgent;
	private double avgWordsPerDialogueFragmentAgent;
	private double avgWordsPerDialogueFragmentCustomer;
	private double avgWordsUntilSpeakerChange;
	private double speechTimeAgentInSec;
	private double speechTimeCustomerInSec;
	private double avgWordsPerMinuteAgent;
	private double avgWordsPerMinuteCustomer;

	public DialogueData(double dialogueLength, String dialogueID, int speakerChanges, double wordsAgent,
			double wordsCustomer, double wordsPerDialogueA, double wordsPerDialogueC, int dialoguePartsOfAgent,
			int dialoguePartsOfCustomer, double speechTimeAgentInSec, double speechTimeCustomerInSec) {
		this.dialogueID = dialogueID;
		this.dialogueLength = dialogueLength;
		this.speakerChanges = speakerChanges;
		memoryLocationAudioCustomer = "placeholder";
		memoryLocationAudioAgent = "placeholer";
		this.numberOfWordsAgent = wordsAgent;
		this.numberOfWordsCustomer = wordsCustomer;
		this.avgWordsPerDialogueFragmentAgent = wordsPerDialogueA;
		this.avgWordsPerDialogueFragmentCustomer = wordsPerDialogueC;
		this.avgWordsUntilSpeakerChange = (this.avgWordsPerDialogueFragmentAgent
				+ this.avgWordsPerDialogueFragmentCustomer) / 2;
		this.speechProportionCustomerToAgent = getProportionCustomerToAgent();
		this.dialogueFragmentsOfAgent = dialoguePartsOfAgent;
		this.dialogueFragmentsOfCustomer = dialoguePartsOfCustomer;
		this.speechTimeAgentInSec=speechTimeAgentInSec;
		this.speechTimeCustomerInSec=speechTimeCustomerInSec;
		this.avgWordsPerMinuteAgent=getAvgWordsPerMinAgent();
		this.avgWordsPerMinuteCustomer=getAvgWordsPerMinCustomer();
	}

	public double getProportionCustomerToAgent() {
		return (numberOfWordsCustomer / (numberOfWordsCustomer + numberOfWordsAgent));
	}
	
	public double getAvgWordsPerMinCustomer (){
		return (numberOfWordsCustomer/speechTimeCustomerInSec)*60;
		
	}
	public double getAvgWordsPerMinAgent (){
		return (numberOfWordsAgent/speechTimeAgentInSec)*60;
		
	}
}
