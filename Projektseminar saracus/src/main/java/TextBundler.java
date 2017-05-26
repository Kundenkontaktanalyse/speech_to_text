import java.io.*;
import com.google.gson.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class TextBundler {
	 private String finalerOutput;
	 private float konfidenz;
	 private double dauer;
	 LinkedList<Float> konfidenzListe=new LinkedList<Float>();
	 LinkedList<Double> dauerListe=new LinkedList<Double>();	
	 LinkedList <Float> konfidenzListeDurchschnitt= new  LinkedList <Float>();
	 Gson gson= new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	 GenerateUUID uuid=new GenerateUUID();
	 Gson gsonIn= new Gson();
	 JsonElement fromGson;
	 
	// TextBundler ()throws FileNotFoundException{};
	 
public String getFinalerOutput() {
	return finalerOutput;
}
public void setFinalerOutput(String finalerOutput) {
	this.finalerOutput = finalerOutput;
}
public void setKonfidenz(float konfidenz){
	this.konfidenz=konfidenz;
}

private double sekUndMin(double sek){
	double sekTemp=(sek%60.0)*0.01;
	int sekMin=(int)sek/60;
	return (sekTemp+sekMin);
}

public void konfidenzDurschnittErmitteln(){
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
}

public void addTextSync(String a, float k){
	konfidenzListe.add(k);
	
if(getFinalerOutput()==null){ setFinalerOutput(a);
//dauerListe.add(d);
}
else{setFinalerOutput(getFinalerOutput()+" "+a);}
//System.out.printf(getFinalerOutput());
}


public double getDauer() {
	return dauer;
}
public void setDauer(double dauer) {
	this.dauer = dauer;
}
public float getKonfidenz() {
	return konfidenz;
}

public void fuegeDauerHinzu(double dauer){
	dauerListe.add(Math.round(sekUndMin(dauer)*10000)/10000.0);
}


// INAKTIV!
public void addTextAsync(String a) throws JsonIOException, IOException{
	setFinalerOutput(a);
	
	// speichereDialoginTXT();	
}

public void speichereDialoginTXT(String speicherdestination) {

	BufferedWriter out;
	try {
		out = new BufferedWriter(new FileWriter(speicherdestination + "\\gespraechsdialog.txt"));

		out.write(getFinalerOutput());
		out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

	



public void generiereJSON() throws JsonIOException, IOException{
	String id=uuid.generiereStringID();
	String filename="Gespräch"+id+".json";
	//System.out.println(konfidenzListenListe.getFirst().toString());
	try{
		 fromGson= gsonIn.fromJson
				 (new FileReader("C:/Users/d_tham01/Desktop/JSONinputs/JSONinput1.json"), JsonElement.class);	
	}catch (FileNotFoundException e){
		e.printStackTrace();}
	
	//System.out.println(konfidenzListeOutput);
	JSONSetting jsonSettings=new JSONSetting(id, getFinalerOutput(), dauerListe, konfidenzListeDurchschnitt, fromGson);
	String json=gson.toJson(jsonSettings);
	String jsonInTeast=fromGson.toString();
	System.out.println(jsonInTeast);
	
	try{ 
	FileWriter writer = new FileWriter("C:/Users/d_tham01/Desktop/testordner/"+filename);
	writer.write(json);
	writer.close();
	
	
	}catch(IOException e){
		e.printStackTrace();
	}
	System.out.println(json);

	
}



}




