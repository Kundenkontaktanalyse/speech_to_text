

import java.io.File;

import javax.swing.JFileChooser;

public class FileChooser {

	//XXXXXXX
	String fileName = "xyt";
	
	
	File file = null;

	public File choose() {

		JFileChooser chooser = new JFileChooser();
		// Dialog zum Oeffnen von Dateien anzeigen
		int rueckgabeWert = chooser.showOpenDialog(null);

		/* Abfrage, ob auf "Öffnen" geklickt wurde */
		if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
			// Ausgabe der ausgewaehlten Datei
			// Hier eigentlich Aufruf der Funktion

			file= chooser.getSelectedFile(); //. getPath().toString();

			// XXXXXXXXXXX
			this.fileName = chooser.getName(file); // funktioniert noch nicht
			// System.out.println(fileName);
		}
		return file;

	}

}