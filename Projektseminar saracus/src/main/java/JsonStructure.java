import java.util.ArrayList;

import com.google.gson.JsonElement;

public class JsonStructure {
	
	private ArrayList<Snippet>snippetlist; //Liste von Schnipsels. 
	private JsonElement inputData;  // Json-Input z.B. generiert durch CRM oder Genesys.
	private DialogueData dialogueData; //dialogueData.
	
	
	public JsonStructure (ArrayList<Snippet>snippetlist, JsonElement metadata,
			DialogueData dialogueData){
		this.snippetlist=snippetlist;
		this.inputData=metadata;
		this.dialogueData=dialogueData;
	}
	
	public DialogueData getDialogueData() {
		return dialogueData;
	}

	public void setDialogueData(DialogueData dialogueData) {
		this.dialogueData = dialogueData;
	}

	public ArrayList<Snippet> getSnippetlist() {
		return snippetlist;
	}


	public void setSnippetlist(ArrayList<Snippet>snippetlist) {
		this.snippetlist = snippetlist;
	}


	public JsonElement getInputData() {
		return inputData;
	}


	public void setInputData(JsonElement metadata) {
		this.inputData = metadata;
	}


	
	

}
