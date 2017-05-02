import java.io.*;
import java.util.*;

public class TextBundler{
	 private String finalerOutput;

public TextBundler(){};

public String getFinalerOutput() {
	return finalerOutput;
}
public void setFinalerOutput(String finalerOutput) {
	this.finalerOutput = finalerOutput;
}

public void addText(String a){
if(getFinalerOutput()==null){ setFinalerOutput(a);

}
else{setFinalerOutput(getFinalerOutput()+" "+a);}
System.out.printf(getFinalerOutput());

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
}