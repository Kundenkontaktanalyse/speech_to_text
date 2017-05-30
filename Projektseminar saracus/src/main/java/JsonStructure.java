import java.util.ArrayList;

import com.google.gson.JsonElement;

public class JsonStructure {
	
	private ArrayList<Snippet>snippetlist; //Liste von Schnipsels. 
	private JsonElement metadata;  // Json-Input z.B. generiert durch CRM oder Genesys.
	
	
	public JsonStructure (ArrayList<Snippet>snippetlist, JsonElement metadata){
		this.snippetlist=snippetlist;
		this.metadata=metadata;
	}
	
	public ArrayList<Snippet> getSnippetlist() {
		return snippetlist;
	}


	public void setSnippetlist(ArrayList<Snippet>snippetlist) {
		this.snippetlist = snippetlist;
	}


	public JsonElement getMetadata() {
		return metadata;
	}


	public void setMetadata(JsonElement metadata) {
		this.metadata = metadata;
	}


	
	

}
