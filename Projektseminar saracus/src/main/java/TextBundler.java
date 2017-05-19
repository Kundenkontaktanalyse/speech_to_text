import java.io.*;
import com.google.gson.*;
import java.util.LinkedList;


public class TextBundler {
	 private String finalerOutput;
	 private float konfidenz;
	 private double dauer;
	 private String konfidenzListeOutput;
	 LinkedList<Float> konfidenzListe=new LinkedList<Float>();
	 LinkedList<Double> dauerListe=new LinkedList<Double>();	
	 LinkedList <LinkedList<Float>> konfidenzListenListe= new  LinkedList <LinkedList<Float>>();
	 Gson gson= new GsonBuilder().create();
	 GenerateUUID uuid=new GenerateUUID();
	 


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

public void aktualisiereListOList(){
	konfidenzListenListe.add(konfidenzListe);
}

public void addTextAsync(String a) throws JsonIOException, IOException{
	setFinalerOutput(a);
	
	speichereOutput();	
}

private void speichereOutput(){
	try {
	    BufferedWriter out = new BufferedWriter(new FileWriter("test.txt"));
	    out.write(getFinalerOutput());  //Replace with the string 
	                                             //you are trying to write  
	    out.close();
	}
	catch (IOException e)
	{
	    System.out.println("Exception ");

	}

	
}


public void generiereJSON() throws JsonIOException, IOException{
	String id=uuid.generiereStringID();
	String filename="Gespräch"+id+".json";
	aktualisiereListOList();
	System.out.println(konfidenzListeOutput);

	
	//System.out.println(konfidenzListeOutput);
	JSONSetting jsonSettings=new JSONSetting(id, getFinalerOutput(), dauerListe, konfidenzListenListe);
	String json=gson.toJson(jsonSettings);

	
	try{ 
	FileWriter writer = new FileWriter("C:/Users/t_diek09/Desktop/testdaten/"+filename);
	writer.write(json);
	writer.close();
	}catch(IOException e){
		e.printStackTrace();
	}
	System.out.println(json);
	
}



}



