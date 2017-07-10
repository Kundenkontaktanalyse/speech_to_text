import java.io.*;
import com.google.gson.*;
import java.util.ArrayList;

public class TextBundler {
	private String finalerOutputDialog; // Output, der in .txt-Datei eingebunden
										// ist.
	private Gson gson = new GsonBuilder().create(); // gsonBuilder
	private GenerateUUID uuid = new GenerateUUID(); // Java-Klasse zum Erzeugen
													// einer
	// UUID.
	private ArrayList<Snippet> cuttedSnippetlist = new ArrayList<Snippet>();
	private String uuidString;
	private Gson gsonIn = new Gson(); // Gson-Element
	private JsonElement fromGson; // Json-Input-Element mit Metadaten
	private Snippet[] snippetlist; // Schnipselliste
	private int counter = 0; // Counter zum befüllen des Arrays
	private double audioLength;
	private String cuttedFinalDialogue;

	public TextBundler(String uuidString) {
		this.uuidString = uuidString;
	}
	/*
	 * Getter- und Setter-Methoden
	 *
	 */

	public String getFinalerOutputDialog() {
		return finalerOutputDialog;
	}

	public void setFinalerOutputDialog(String finalerOutputDialog) {
		this.finalerOutputDialog = finalerOutputDialog;
	}

	public double getAudioLength() {
		return audioLength;
	}

	public void setAudioLength(double audioLength) {
		this.audioLength = audioLength;
	}

	/**
	 * Strukturiert den Gespraechsoutput
	 * 
	 * @param s
	 *            die Rolle mit Leerzeichen.
	 */
	public void addGespraechsStruktur(String s) {
		if (getFinalerOutputDialog() == null) {
			setFinalerOutputDialog("\n" + s);
		} else {
			setFinalerOutputDialog(getFinalerOutputDialog() + "\n" + " " + s);
		}
	}

//	/**
//	 * Fügt Text für den txt-Output hinzu.
//	 * 
//	 * @param a
//	 *            der transkribierte Text.
//	 */
//	public void addTextSync(String a) {
//
//		if (getFinalerOutputDialog() == null) {
//			setFinalerOutputDialog(a);
//		} else {
//			setFinalerOutputDialog(getFinalerOutputDialog() + a);
//		}
//	}

	
	public void speichereDialoginTXT(String speicherdestination, String idName) {
		cuttedFinalDialogue = cutDialogue();
		
		try {
//			BufferedWriter out = new BufferedWriter(new FileWriter(speicherdestination + "\\" + idName + ".txt"));
//
//			out.write(cuttedFinalDialogue);
//			out.close();
			
			Writer writer = new OutputStreamWriter(new FileOutputStream(speicherdestination + "\\" + idName + ".txt"),
					"UTF-8");
			writer.write(cuttedFinalDialogue);
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int identifySpeakerChanges() {
		int speakerChanges = 0;

		String currentRole = cuttedSnippetlist.get(0).getRole();

		for (int i = 1; i < cuttedSnippetlist.size(); i++) {

			System.out.println("identifySpeakerChanges" + i);

			String newRole = cuttedSnippetlist.get(i).getRole();
			if (!newRole.equals(currentRole)) {
				speakerChanges++;
				currentRole = newRole;
			}
		}
		return speakerChanges;
	}

	public int identifySingleSpeakerOccurence(String role) {
		// String opRole;
		// if (role == "Agent") {
		// opRole = "Customer";
		// } else {
		// opRole = "Agent";
		// }
		int speakerOccurence = 0;
		// String start = cuttedSnippetlist.get(0).getRole();
		if (cuttedSnippetlist.get(0).getRole().equals(role))
			speakerOccurence += 1;
		for (int i = 1; i < cuttedSnippetlist.size(); i++) {
			String preCurrent = cuttedSnippetlist.get(i - 1).getRole();
			String current = cuttedSnippetlist.get(i).getRole();
			if (current == role && preCurrent != current) {
				speakerOccurence += 1;

			}
		}
		return speakerOccurence;
	}

	/**
	 * Generiert einen Json-Output, der aus den Meta-Inputdaten im json-Format
	 * besteht und einer Liste der transkribierten Schnipsel besteht. Der
	 * Datei-Name besteht aus einer generierten UUID.
	 * 
	 * @param fileDestination
	 *            der Speicherort des Outputs
	 * @param jsonInput
	 *            die Input-Datei; besteht aus Metadaten, z.B. aus CRM-System
	 */
	public void generiereJSON(String filename, String fileDestination, String jsonInput) {
//		adaptSnippetlist();
//		addStartingTimeToSnippet();
		int speakerChanges = identifySpeakerChanges();
		String filenameWithEnding = filename + ".json";

		double wordsPerAgentFragment = getWordsPerSpeaker("Agent") / identifySingleSpeakerOccurence("Agent");
		double wordsPerCustomerFragment = getWordsPerSpeaker("Customer") / identifySingleSpeakerOccurence("Customer");

		DialogueData dialoguedata = new DialogueData(getAudioLength(), uuidString, speakerChanges,
				getWordsPerSpeaker("Agent"), getWordsPerSpeaker("Customer"), wordsPerAgentFragment,
				wordsPerCustomerFragment, identifySingleSpeakerOccurence("Agent"),
				identifySingleSpeakerOccurence("Customer"), getSpeechTimeSinglePerson("Agent"),
				getSpeechTimeSinglePerson("Customer"));

		// System.out.println(konfidenzListenListe.getFirst().toString());
		try {
			fromGson = gsonIn.fromJson(new FileReader(jsonInput), JsonElement.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// System.out.println(konfidenzListeOutput);
		// JSONSetting jsonSettings=new JSONSetting(id, getFinalerOutputJson(),
		// dauerListe, konfidenzListeDurchschnitt, fromGson);
		JsonStructure jsonStructure = new JsonStructure(cuttedSnippetlist, fromGson, dialoguedata);
		String json = gson.toJson(jsonStructure);
		try {
			Writer writer = new OutputStreamWriter(new FileOutputStream(fileDestination + "//" + filenameWithEnding),
					"UTF-8");
			writer.write(json);
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(json);
//		counter = 0;
//		cuttedSnippetlist = null;
//		cuttedSnippetlist = new ArrayList<Snippet>();
	}

	/**
	 * setzt das Schnipselarray auf die richtige groeße (z.B. hier: Anzahl der
	 * Audioschnipsel insgesamt).
	 * 
	 * @param i
	 *            groeße des arrays.
	 */
	public void setSnippetListSize(int i) {
		snippetlist = new Snippet[i];
	}

	/**
	 * Macht aus Array mit Schnipseln eine Array-List. In ihr sind nur noch
	 * Schnipsel enthalten, die nicht leer sind und dessen Transkript nicht leer
	 * ist.
	 * 
	 * @return
	 */
	public ArrayList<Snippet> adaptSnippetlist() {
		int idcounter = 0;
		for (int i = 0; i < snippetlist.length; i++) {
			if (snippetlist[i] != null && snippetlist[i].getTranscription() != null) {
				cuttedSnippetlist.add(snippetlist[i]);
				String idcounterString = String.valueOf(idcounter);
				cuttedSnippetlist.get(idcounter)
						.setSnippetId(uuidString + "-" + cuttedSnippetlist.get(idcounter).getRole() + idcounterString);
				idcounter++;
			}
		}

		return cuttedSnippetlist;
	}

	/**
	 * Fügt dem Schnipsel-array einen weiteren Schnipsel(snippet) hinzu.
	 * 
	 * @param role
	 *            die Rolle des Sprechers
	 * @param transcript
	 *            der gesagte Text
	 * @param length
	 *            die Laenge des Audio-Schnipsels
	 * @param confidence
	 *            die von der Google-Api übermittelte Sicherheit, dass es sich
	 *            um das richtige Ergebnishandelt (zwischen 0 und 1)
	 */
	public void addSnippet(String role, String transcript, double length, float confidence) {
		snippetlist[counter] = new Snippet(role, transcript, length, confidence);

		// snippetlist[counter].countWords();
		counter++;
	}

	public double getWordsPerSpeaker(String role) {
		double wordsPerSpeaker = 0;
		for (int i = 0; i < cuttedSnippetlist.size(); i++) {
			if (cuttedSnippetlist.get(i).getRole().equals(role)) {
				wordsPerSpeaker = wordsPerSpeaker + cuttedSnippetlist.get(i).getWordCount();

			}
		}
		return wordsPerSpeaker;
	}

	public double getSpeechTimeSinglePerson(String role) {
		double speechTime = 0;
		for (int i = 0; i < cuttedSnippetlist.size(); i++) {
			if (cuttedSnippetlist.get(i).getRole() == role) {
				speechTime = speechTime + cuttedSnippetlist.get(i).getLenght();
			}
		}
		return speechTime;
	}
	
	public void addStartingTimeToSnippet (){
	double counterInSeconds=0;
	int timeInMin=0;
	int timeInSec=0;
	String timeMinSec;
	
	for(int i=0; i<cuttedSnippetlist.size();i++){
		timeInMin=(int)counterInSeconds/60;
		timeInSec=(int)counterInSeconds%60;
		timeMinSec=(timeInMin+ ":"+timeInSec);
	cuttedSnippetlist.get(i).setStartingTimeMinSec(timeMinSec);
	counterInSeconds=counterInSeconds+cuttedSnippetlist.get(i).getLenght();
	}
	}
	public String cutDialogue() {
		System.out.println("cutDialogue");
		String finalString = null;
		System.out.println(cuttedSnippetlist.size());
		for (int i = 0; i < cuttedSnippetlist.size(); i++) {
			if (cuttedSnippetlist.get(i).getRole().equals("Agent")) {
				if (finalString == null) {
					finalString = "Agent: " + cuttedSnippetlist.get(i).getTranscription() + "\n";
				} else {
					finalString = finalString + "Agent: " + cuttedSnippetlist.get(i).getTranscription() + "\n";
				}
			} else {
				if (finalString == null) {
					finalString = "Customer: " + cuttedSnippetlist.get(i).getTranscription() + "\n";
				} else {

					finalString = finalString + "Customer: " + cuttedSnippetlist.get(i).getTranscription() + "\n";
				}
			}
		}
		// TODO
		// erstel null abfangen

		return finalString;
	}
	
	public void reSetInitials(){
		counter = 0;
		cuttedSnippetlist = null;
		cuttedSnippetlist = new ArrayList<Snippet>();
	}

}
