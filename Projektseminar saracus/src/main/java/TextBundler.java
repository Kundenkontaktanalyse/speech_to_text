import java.io.*;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.LinkedList;

public class TextBundler {
	 private String finalerOutputJson;
	 private String finalerOutputDialog;
	 LinkedList<Float> konfidenzListe=new LinkedList<Float>();
	 LinkedList<Double> dauerListe=new LinkedList<Double>();	
	 LinkedList <Float> konfidenzListeDurchschnitt= new  LinkedList <Float>();
	 Gson gson= new GsonBuilder().create();
	 GenerateUUID uuid=new GenerateUUID();
	 Gson gsonIn= new Gson();
	 JsonElement fromGson;	
	 Snippet[] snippetlist;
	 int counter=0;
	 

private double sekUndMin(double sek){
	double sekTemp=(sek%60.0)*0.01;
	int sekMin=(int)sek/60;
	return (sekTemp+sekMin);
}

public void addGespraechsStruktur(String s){
	if(getFinalerOutputDialog()==null){setFinalerOutputDialog("\n"+s);
									   setFinalerOutputJson(s);}
	else{setFinalerOutputDialog(getFinalerOutputDialog()+"\n"+" "+s);
		 setFinalerOutputJson(getFinalerOutputJson()+" "+s);
	}
}
public float konfidenzDurschnittErmitteln(){
	float summe=0;
	while(!konfidenzListe.isEmpty()){
		//System.out.println(getKonfidenzListe().getFirst().toString());
		int i=konfidenzListe.size();
		while(!konfidenzListe.isEmpty()){
			summe=summe+konfidenzListe.getFirst();
			konfidenzListe.remove();
		}
		summe=summe/i;
	}
	 konfidenzListeDurchschnitt.add(summe);
	 return summe;
}

public void addTextSync(String a, float k){
	konfidenzListe.add(k);
	
if(getFinalerOutputJson()==null){ setFinalerOutputJson(a);
								  setFinalerOutputDialog(a);

}
else{setFinalerOutputJson(getFinalerOutputJson()+" "+a);
	 setFinalerOutputDialog(getFinalerOutputDialog()+" "+a);
	}
}

public void fuegeDauerHinzu(double dauer){
	dauerListe.add(Math.round(sekUndMin(dauer)*10000)/10000.0);
}

// INAKTIV!
public void addTextAsync(String a) throws JsonIOException, IOException{
	setFinalerOutputJson(a);
	
	// speichereDialoginTXT();	
}

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

public void generiereJSON(String fileDestination, String jsonInput) throws JsonIOException, IOException{
	String id=uuid.generiereStringID();
	String filename="Gespraech"+id+".json";
	
	//System.out.println(konfidenzListenListe.getFirst().toString());
	try{
		 fromGson= gsonIn.fromJson
				 (new FileReader(jsonInput), JsonElement.class);	
	}catch (FileNotFoundException e){
		e.printStackTrace();}
	
	//System.out.println(konfidenzListeOutput);
//	JSONSetting jsonSettings=new JSONSetting(id, getFinalerOutputJson(), dauerListe, konfidenzListeDurchschnitt, fromGson);
	JsonStructure jsonStructure=new JsonStructure(adaptSnippetlist(), fromGson);
	String json=gson.toJson(jsonStructure);
	String jsonInTeast=fromGson.toString();
	System.out.println(jsonInTeast);
	
	try{ 
	FileWriter writer = new FileWriter(fileDestination+"//"+filename);
	writer.write(json);
	writer.close();
	
	
	}catch(IOException e){
		e.printStackTrace();
	}
	System.out.println(json);

	
}

public String getFinalerOutputJson() {
	return finalerOutputJson;
}

public void setFinalerOutputJson(String finalerOutputJson) {
	this.finalerOutputJson = finalerOutputJson;
}

public String getFinalerOutputDialog() {
	return finalerOutputDialog;
}

public void setFinalerOutputDialog(String finalerOutputDialog) {
	this.finalerOutputDialog = finalerOutputDialog;
}

/**
 * setzt das schnipselarray auf die richtige groeﬂe.
 * @param i groeﬂe des arrays.
 */
public void setSnippetListSize(int i){
	snippetlist=new Snippet[i];
}

public ArrayList<Snippet> adaptSnippetlist(){
	ArrayList<Snippet> cuttedSnippetlist=new ArrayList<Snippet>();
	for (int i=0; i<snippetlist.length;i++){
		if(snippetlist[i]!= null){
		cuttedSnippetlist.add(snippetlist[i]);
		}
	}
	
return cuttedSnippetlist;
}
public void addSnippet(String role, String transcript, double length, float confidence){
	snippetlist[counter]=new Snippet(role, transcript, length, confidence);
	counter++;
}





















}






