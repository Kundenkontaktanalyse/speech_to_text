
public class TextBundler {
private String finalText;
	public TextBundler(String a) {
		if (this.finalText==null){this.finalText=a;}
		else{this.finalText=this.finalText+a;}
		
		
	}
	

//	public String getFinalText() {
//		return finalText;
//	}
//
//	public void setFinalText(String finalText) {
//		this.finalText = finalText;
//		addText(finalText);
//	}
//
//	
//
//	private void addText(String b) {
//		setFinalText(getFinalText() + b);
//	}

	public void printErgebnis() {
		System.out.println("Gesamte Datei:" + finalText);
	}

}
