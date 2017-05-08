import java.io.*;
import com.google.gson.*;


public class TextBundler {
	 private String finalerOutput;
	 Gson gson= new GsonBuilder().setPrettyPrinting().create();
	 GenerateUUID uuid=new GenerateUUID();
	 


public String getFinalerOutput() {
	return finalerOutput;
}
public void setFinalerOutput(String finalerOutput) {
	this.finalerOutput = finalerOutput;
}

public void addTextSync(String a){
if(getFinalerOutput()==null){ setFinalerOutput(a);

}
else{setFinalerOutput(getFinalerOutput()+" "+a);}
System.out.printf(getFinalerOutput());
speichereOutput();

}


public void addTextAsync(String a) throws JsonIOException, IOException{
	setFinalerOutput(a);
	
	speichereOutput();
	generiereJSON();
	
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
	JSONSetting jsonSettings=new JSONSetting(id, getFinalerOutput(), 3);
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



