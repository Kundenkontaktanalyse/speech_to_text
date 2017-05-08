import java.io.*;
import com.google.gson.*;
import com.google.gson.stream.JsonWriter;

import java.io.*;

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
	FileOutputStream outputStream;
	String filename="test.json";
	JSONSetting jsonSettings=new JSONSetting(uuid.generiereStringID(), getFinalerOutput(), 3);
	String json=gson.toJson(jsonSettings);

	
	try{ 
	FileWriter writer = new FileWriter("C:/Users/t_diek09/Desktop/testdaten/test.json");
	writer.write(json);
	writer.close();
	}catch(IOException e){
		e.printStackTrace();
	}
	System.out.println(json);
	
}


}



