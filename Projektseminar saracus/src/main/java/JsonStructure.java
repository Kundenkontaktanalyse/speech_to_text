import com.google.gson.JsonElement;

public class JsonStructure {
	
	private Snippet []snippetlist; //Liste von Schnipsels. 
	private JsonElement metadata;  // Json-Input z.B. generiert durch CRM oder Genesys.
	
	
	public JsonStructure (Snippet[]snippetlist, JsonElement metadata){
		this.snippetlist=snippetlist;
		this.metadata=metadata;
	}
	
	public Snippet[] getSnippetlist() {
		return snippetlist;
	}


	public void setSnippetlist(Snippet[] snippetlist) {
		this.snippetlist = snippetlist;
	}


	public JsonElement getMetadata() {
		return metadata;
	}


	public void setMetadata(JsonElement metadata) {
		this.metadata = metadata;
	}


	
	

}
