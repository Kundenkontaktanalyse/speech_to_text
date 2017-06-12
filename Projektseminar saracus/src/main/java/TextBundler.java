import java.io.*;
import com.google.gson.*;
import java.util.ArrayList;

public class TextBundler {
	 private String finalerOutputDialog; //Output, der in .txt-Datei eingebunden ist.
	 Gson gson= new GsonBuilder().create(); //gsonBuilder
	 GenerateUUID uuid=new GenerateUUID(); // Java-Klasse zum Erzeugen einer UUID.
	 String uuidString;
	 Gson gsonIn= new Gson();			// Gson-Element
	 JsonElement fromGson;				//Json-Input-Element mit Metadaten
	 Snippet[] snippetlist;				//Schnipselliste
	 private int counter=0;				//Counter zum befüllen des Arrays
	 private double audioLength;

	 
	 public TextBundler (){
		uuidString=uuid.generiereStringID(); 
	 }
/*
 *Getter- und Setter-Methoden 	 
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
 * @param s die Rolle mit Leerzeichen.
 */
public void addGespraechsStruktur(String s){
	if(getFinalerOutputDialog()==null){setFinalerOutputDialog("\n"+s);
									   }
	else{setFinalerOutputDialog(getFinalerOutputDialog()+"\n"+" "+s);
	}
}

/**
* Fügt Text für den txt-Output hinzu.
 * @param a der transkribierte Text.
 */
public void addTextSync(String a){
	
if(getFinalerOutputDialog()==null){
	setFinalerOutputDialog(a);
}
else{
	 setFinalerOutputDialog(getFinalerOutputDialog()+" "+a);
	}
}

/**
 * speichert das Telefongespraech in einer .txt Datei.
 * @param speicherdestination
 */
public void speichereDialoginTXT(String speicherdestination) {

	BufferedWriter out;
	try {
		out = new BufferedWriter(new FileWriter(speicherdestination + "\\gespraechsdialog.txt"));

		out.write(getFinalerOutputDialog());
		out.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
}

/**
 *  Generiert einen Json-Output, der aus den Meta-Inputdaten im json-Format besteht und einer Liste der transkribierten Schnipsel besteht. 
 *  Der Datei-Name besteht aus einer generierten UUID.
 * @param fileDestination der Speicherort des Outputs
 * @param jsonInput die Input-Datei; besteht aus Metadaten, z.B. aus CRM-System
 */
public void generiereJSON(String fileDestination, String jsonInput){
	String filename="Gespraech"+uuidString+".json";
	DialogueData dialoguedata=new DialogueData(getAudioLength() , uuidString);
	
	//System.out.println(konfidenzListenListe.getFirst().toString());
	try{
		 fromGson= gsonIn.fromJson
				 (new FileReader(jsonInput), JsonElement.class);	
	}catch (FileNotFoundException e){
		e.printStackTrace();}
	
	//System.out.println(konfidenzListeOutput);
//	JSONSetting jsonSettings=new JSONSetting(id, getFinalerOutputJson(), dauerListe, konfidenzListeDurchschnitt, fromGson);
	JsonStructure jsonStructure=new JsonStructure(adaptSnippetlist(), fromGson, dialoguedata);
	String json=gson.toJson(jsonStructure);
	try{ 
	FileWriter writer = new FileWriter(fileDestination+"//"+filename);
	writer.write(json);
	writer.close();
	
	
	}catch(IOException e){
		e.printStackTrace();
	}
	System.out.println(json);	
}

/**
 * setzt das Schnipselarray auf die richtige groeße (z.B. hier: Anzahl der Audioschnipsel insgesamt).
 * @param i groeße des arrays.
 */
public void setSnippetListSize(int i){
	snippetlist=new Snippet[i];
}

/**
 * Macht aus Array mit Schnipseln eine Array-List. In ihr sind nur noch Schnipsel enthalten, die nicht leer sind und dessen Transkript
 * nicht leer ist.
 * @return
 */
public ArrayList<Snippet> adaptSnippetlist(){
	ArrayList<Snippet> cuttedSnippetlist=new ArrayList<Snippet>();
	int idcounter=0;
	for (int i=0; i<snippetlist.length;i++){
		if(snippetlist[i]!= null && snippetlist[i].getTranscription()!=null){
		cuttedSnippetlist.add(snippetlist[i]);
		String idcounterString=String.valueOf(idcounter);
		cuttedSnippetlist.get(idcounter).setSnippetId(uuidString+"-"+cuttedSnippetlist.get(idcounter).getRole() +
				idcounterString);
		idcounter++;
		}
	}
	
return cuttedSnippetlist;
}

/**
 * Fügt dem Schnipsel-array einen weiteren Schnipsel(snippet) hinzu.
 * @param role die Rolle des Sprechers
 * @param transcript der gesagte Text
 * @param length die Laenge des Audio-Schnipsels
 * @param confidence die von der Google-Api übermittelte Sicherheit, dass es sich um das richtige Ergebnishandelt (zwischen 0 und 1)
 */
public void addSnippet(String role, String transcript, double length, float confidence){
	snippetlist[counter]=new Snippet(role, transcript, length, confidence);
	counter++;
}

}






