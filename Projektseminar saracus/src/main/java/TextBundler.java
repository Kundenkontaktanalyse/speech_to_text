import java.io.*;

public class TextBundler {
	private String finalerOutput;

	public TextBundler() {
	};

	public String getFinalerOutput() {
		return finalerOutput;
	}

	public void setFinalerOutput(String finalerOutput) {
		this.finalerOutput = finalerOutput;
	}

	public void addTextSync(String a) {
		if (getFinalerOutput() == null) {
			setFinalerOutput(a);

		} else {
			setFinalerOutput(getFinalerOutput() + "\n " + a);
		}
		System.out.println(getFinalerOutput());
		System.out.println("-----------------");
		//speichereOutput();

	}

	public void addTextAsync(String a) {
		setFinalerOutput(a);
	//speichereOutput();
	}

	public void speichereOutput() {
		
			BufferedWriter out;
			try {
				out = new BufferedWriter(new FileWriter("C:/Users/d_tham01/workspace/hallo.txt"));
			
			out.write(getFinalerOutput()); 						
			out.close();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
