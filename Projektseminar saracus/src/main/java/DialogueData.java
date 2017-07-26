
public class DialogueData {
	private double dialogueLength;
	private String dialogueID;
	private String memoryLocationAudioCustomer;
	private String memoryLocationAudioAgent;
	private int speakerChanges;
	private int dialogueFragmentsOfAgent;
	private int dialogueFragmentsOfCustomer;
	private int numberOfWordsAgent;
	private int numberOfWordsCustomer;
	private double speechProportionCustomerToAgent;
	private double avgWordsPerDialogueFragmentAgent;
	private double avgWordsPerDialogueFragmentCustomer;
	private double avgWordsUntilSpeakerChange;
	private double speechTimeAgentInSec;
	private double speechTimeCustomerInSec;
	private double avgWordsPerMinuteAgent;
	private double avgWordsPerMinuteCustomer;
	private int overlappingSpeechOccurances;
	private double aggrOverlappinSpeechDuration;
	private int unnaturalPauseOccurancesCustomer;
	private double aggrUnnaturalPauseDurationCustomer;
	private int unnaturalPauseOccurancesAgent;
	private double aggrUnnaturalPauseDurationAgent;

	public DialogueData(double dialogueLength, String dialogueID, int speakerChanges, double wordsAgent,
			double wordsCustomer, double wordsPerDialogueA, double wordsPerDialogueC, int dialoguePartsOfAgent,
			int dialoguePartsOfCustomer, double speechTimeAgentInSec, double speechTimeCustomerInSec,
			int overlappingSpeechOccurances, double overlappinSpeechDuration, int unnaturalPauseOccurancesCustomer,
			double unnaturalPauseDurationCustomer, int unnaturalPauseOccurancesAgent, double aggrUnnaturalPauseDurationAgent) {
		this.dialogueID = dialogueID;
		this.dialogueLength = roundToThirdDigit(dialogueLength);
		this.speakerChanges = speakerChanges;
		memoryLocationAudioCustomer = "placeholder";
		memoryLocationAudioAgent = "placeholer";
		this.numberOfWordsAgent = (int)wordsAgent;
		this.numberOfWordsCustomer = (int)wordsCustomer;
		this.avgWordsPerDialogueFragmentAgent = roundToThirdDigit(wordsPerDialogueA);
		this.avgWordsPerDialogueFragmentCustomer = roundToThirdDigit(wordsPerDialogueC);
		this.avgWordsUntilSpeakerChange = roundToThirdDigit((this.avgWordsPerDialogueFragmentAgent
				+ this.avgWordsPerDialogueFragmentCustomer) / 2);
		this.speechProportionCustomerToAgent = roundToThirdDigit(getProportionCustomerToAgent());
		this.dialogueFragmentsOfAgent = dialoguePartsOfAgent;
		this.dialogueFragmentsOfCustomer = dialoguePartsOfCustomer;
		this.speechTimeAgentInSec=roundToThirdDigit(speechTimeAgentInSec);
		this.speechTimeCustomerInSec=roundToThirdDigit(speechTimeCustomerInSec);
		this.avgWordsPerMinuteAgent=roundToThirdDigit(getAvgWordsPerMinAgent());
		this.avgWordsPerMinuteCustomer=roundToThirdDigit(getAvgWordsPerMinCustomer());
		this.overlappingSpeechOccurances = overlappingSpeechOccurances;
		this.aggrOverlappinSpeechDuration = overlappinSpeechDuration;
		this.unnaturalPauseOccurancesCustomer = unnaturalPauseOccurancesCustomer;
		this.aggrUnnaturalPauseDurationCustomer = unnaturalPauseDurationCustomer;
		this.unnaturalPauseOccurancesAgent = unnaturalPauseOccurancesAgent;
		this.aggrUnnaturalPauseDurationAgent = aggrUnnaturalPauseDurationAgent;

	}

	public double getProportionCustomerToAgent() {
		
		
		return ((double) numberOfWordsCustomer / ((double)numberOfWordsCustomer + (double) numberOfWordsAgent));
	}
	
	public double getAvgWordsPerMinCustomer (){
		return (numberOfWordsCustomer/speechTimeCustomerInSec)*60;
		
	}
	public double getAvgWordsPerMinAgent (){
		return (numberOfWordsAgent/speechTimeAgentInSec)*60;
		
	}
	
	public double roundToThirdDigit(double number){
		return Math.round(number*1000)/1000.0;
	}
}
