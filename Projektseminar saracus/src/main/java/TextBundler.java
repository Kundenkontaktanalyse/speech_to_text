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
			setFinalerOutput(getFinalerOutput() + " " + a);
		}
		System.out.printf(getFinalerOutput());
		speichereOutput();

	}

	public void addTextAsync(String a) {
		setFinalerOutput(a);
		speichereOutput();
	}

	private void speichereOutput() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("test.txt"));
			out.write(getFinalerOutput()); 						
			out.close();
		} catch (IOException e) {
			System.out.println("Exception ");

		}

	}
}