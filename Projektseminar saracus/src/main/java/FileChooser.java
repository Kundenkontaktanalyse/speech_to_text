//hallasdf

import javax.swing.JFileChooser;



public class FileChooser {
	public static String choose() {
		String filePath=null;
		JFileChooser chooser = new JFileChooser();
	    // Dialog zum Oeffnen von Dateien anzeigen
	    int rueckgabeWert = chooser.showOpenDialog(null);
	    
	    /* Abfrage, ob auf "Öffnen" geklickt wurde */
	    if(rueckgabeWert == JFileChooser.APPROVE_OPTION)
	    {
	         // Ausgabe der ausgewaehlten Datei
	    	//Hier eigentlich Aufruf der Funktion
	       
	    	filePath = chooser.getSelectedFile().getPath().toString();
	    }
	    return filePath;
	    
	}
	
	
}
