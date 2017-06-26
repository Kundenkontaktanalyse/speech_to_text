import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/*Klasse zur Durchführung der Speakerseperation
 * Erwartet: FileChooser-Übergabe eines Verzeichnesses mit Unterordnern A und B mit jeweilig gecutteten Unterdateien A.1, A.2 etc 
 * 
 * 
 * Zusammenspiel der Klassen: VERALTET!
 * Momentan:
 * main ruft AudioProcessing.processAudio auf
 * diese in line 64 den filechooser, der ausgewähltes FILE zurückliefert
 * anzahl der Schnitte wird ermittelt, bestimmt iterationsanzahl der for-schleife
 * pro iteration wird in line 115 eine neue Datei erstellt, der Audioteil hineingeschrieben und das file an recognize übergeben. l 117
 * diese übersetzt und ermittelt Werte, von denen der übersetzte Text in line 53 dem textbundler übergeben wird
 * dieser buffert die einzelnen textteile und schreibt sie in eine vom FileWriter erstellte (?) neue Datei
 *  
 *  
 * Ziel / to-do: DONE! (?)
 * 
 *  processFiles startet Prozess
 *  FileChooser nimmt einen Audioschnitt an CHECK
 *  Sprung ins Überverzeichnis CHECK
 *  Ablegen aller Audio-Dateien (Filter!) im Verzeichnis (inklusive initial gewählter) in File[] CHECK
 *  Sortieren der Files im Array nach alphabetischer reihenfolge (nummer aufsteigend am ende) CHECK
 *  Auslesen der Startzeiten aus TXT-Datei in Startzeiten[] CHECK
 *  Erzeugen von File][]KU und File[]CA  CHECK
 *  
 *  Transkription der einzelnen Dateien in bufferedReader +
 *  Abspeichern der einzelnen Outputs mit "Dialogtrenner" CHECK
 *  
 *  XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
 *  
 *  
 *  ffmpeg textdateien:
 *  1: CA-Startzeiten = C1.txt
 *  2: CA-Endzeiten = C2.txt
 *  3: KU-Startzeiten = K1.txt
 *  4: KU-Endzeiten = K2.txt
 *  
 *  
 *  TODO:
 *  - input datei beim schneiden löschen (UNNÖTIG? JA!)
 *  - cutMethode ins gesamtsystem einbetten / KONTROLLIEREN!
 *  - identifyStarter 0.0 fixen (leere sekunden einfügen vor beiden channeln) oder (??) vergleich anzahl start und endzeiten: siehe todo2
 *    falls mehr endzeiten pro channel als startzeiten -> start fehlt ABER: falls keine stille am ende des gesprächs endzeitenanzahl = -1!! (egal?)
 *  - textbundler outputdestination dynamisch machen
 *  
 */



public class SpeakerSeperation extends AudioProcessing {

	File sourceFile;
	File ParentsourceFile;
	File[] audioChannels;
	File[] audiofiles;
	File[] audiofilesKU;
	File[] audiofilesCA;
	File[] textfiles;
	// arrays zum updaten, eigentlich besser mit listen umsetzbar...
	double[] startzeitenKU;
	double[] startzeitenCA;
	double[] endzeitenKU;
	double[] endzeitenCA;
	double[] startzeitenKUrdy;
	double[] startzeitenCArdy;
	double[] endzeitenKUrdy;
	double[] endzeitenCArdy;
	double audioLength;

	public SpeakerSeperation(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	// Methode zum Schneiden von AudioDateien: Legt die AudioSchnitte im
	// Verzeichnis der Audiodatei ab
	// Bennennung der Dateien mit originalNamen + 00X für Anzahl
	public void cutAudio(File audioSource, double[] startzeiten, double[] endzeiten, char channel) {
	       
        System.out.println("cutAudio:");
 
        String sourceFilePath = audioSource.getPath().toString();
        String destinationFilePath = sourceFilePath;
        AudioInputStream inputStream = null;
        AudioInputStream shortenedStream = null;
        int AnzahlSchnitte = startzeiten.length;
 
        //
 
       
        // Abschnitt zum einfügen der letzten Endzeit,
        AudioFileFormat fileFormatFuerLaenge = null;
        try {
            fileFormatFuerLaenge = AudioSystem.getAudioFileFormat(audioSource);
        } catch (UnsupportedAudioFileException | IOException e1) {
            e1.printStackTrace();
        }
 
        AudioFormat formatFuerLaenge = fileFormatFuerLaenge.getFormat();
        double audioFileLength = audioSource.length(); // get Length des inputs
 
        int bytesPerSecondFuerLaenge = formatFuerLaenge.getFrameSize() * (int) formatFuerLaenge.getFrameRate(); // hz*bytes/frames
        double audioSekunden = audioFileLength / bytesPerSecondFuerLaenge;
        audioLength=audioSekunden;
 
        double[] endzeitenrdy = null;
 
        if (startzeiten.length != endzeiten.length) {
            System.out.println("Fehler bei Start/Endzeiten" + channel);
            if (startzeiten.length > endzeiten.length) {
                System.out.println("letzte Endzeit fehlt: Wurde eingesetzt");
                endzeitenrdy = new double[endzeiten.length + 1];
                endzeitenrdy[endzeitenrdy.length - 1] = audioSekunden;
                for (int i = 0; i < endzeiten.length; i++) {
                    endzeitenrdy[i] = endzeiten[i];
                }
            }
//          System.out.println("endzeiten nach cut-verbesserung");
//          for (int i = 0; i < endzeitenrdy.length; i++) {
//              System.out.print(endzeitenrdy[i] + ", ");
//          }
//          System.out.println();
        } else {
            endzeitenrdy = endzeiten;
        }
 
        // Ermittlung der Länge der Einzelnen Gesprächsabschnitte und
        // Gesprächspausen
        double[] längeSchnitteSekunden = new double[startzeiten.length];
        for (int i = 0; i < längeSchnitteSekunden.length; i++) {
            längeSchnitteSekunden[i] = endzeitenrdy[i] - startzeiten[i];
        }
        // for (int i = 0; i < längeSchnitte.length; i++) {
        // System.out.println(längeSchnitte[i]);
        // }
 
        double[] längeSkipsSekunden = new double[startzeiten.length];
        längeSkipsSekunden[0] = startzeiten[0];
        for (int i = 1; i < längeSkipsSekunden.length; i++) {
            längeSkipsSekunden[i] = startzeiten[i] - endzeitenrdy[i - 1];
        }
 
       
//      // ausdruck
//      System.out.println("längeSchnitteSekunden" + channel);
//      for (int i = 0; i < längeSchnitteSekunden.length; i++){
//          System.out.print(längeSchnitteSekunden[i] + ", ");
//      }
//      System.out.println();
//      System.out.println("längeSkipsSekunden" + channel);
//      for (int i = 0; i < längeSkipsSekunden.length; i++){
//          System.out.print(längeSkipsSekunden[i] + ", ");
//      }
//      System.out.println();
 
       
        // anpassung der Zeiten in den Arrays durch Puffer, ffmpeg einstellung silenceduration = 1s -> stille kann nur am anfang
        // kleiner sein.
        double puffer = 0.3;
       
        if (längeSkipsSekunden[0] > puffer){
        längeSkipsSekunden[0] = längeSkipsSekunden[0] - puffer;
        }
        for (int i = 1; i < längeSkipsSekunden.length; i++) {
            längeSkipsSekunden[i] = längeSkipsSekunden[i] - (2*puffer); // Extra Sekunde am Ende (s.o.) und Anfang lassen
        }
       
        if (längeSkipsSekunden[0] < puffer){ //kein skip am anfang
            längeSchnitteSekunden[0] = längeSchnitteSekunden[0] + längeSkipsSekunden[0] + puffer;
            // verfügbare stille mit transkibieren
            } else {
                längeSchnitteSekunden[0] = längeSchnitteSekunden[0] + (2*puffer);
            }
       
        for (int i = 1; i < längeSchnitteSekunden.length; i++) {
            längeSchnitteSekunden[i] = längeSchnitteSekunden[i] + (2*puffer);//Extra Sekunden am Anfang und Ende,
                                                                            // um leise Satzenden mit zu tranksribieren.
        }
       
       
//      // ausdruck
//      System.out.println("längeSchnitteSekundenDANCH" + channel);
//      for (int i = 0; i < längeSchnitteSekunden.length; i++){
//          System.out.print(längeSchnitteSekunden[i] + ", ");
//      }
//      System.out.println();
//      System.out.println("längeSkipsSekundenDANACH" + channel);
//      for (int i = 0; i < längeSkipsSekunden.length; i++){
//          System.out.print(längeSkipsSekunden[i] + ", ");
//      }
       
       
        try {
            File file = new File(sourceFilePath);
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
 
            AudioFormat format = fileFormat.getFormat();
            inputStream = AudioSystem.getAudioInputStream(file); // eine ID
            // double audioFileLength = file.length(); // get byte-Length des
            // inputs
 
            int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate(); // hz*bytes/frames
            // double audioSekunden = audioFileLength / bytesPerSecond;
 
//          long[] längeSkipsBytes = new long[längeSkipsSekunden.length];
//          for (int i = 0; i < längeSkipsBytes.length; i++) {
//              // cast zu long ohne +1, damit nicht zuviel geskippt wird
//              längeSkipsBytes[i] = (long) (längeSkipsSekunden[i] * bytesPerSecond);
//          }
 
            String origdestinationFilePath = destinationFilePath;
 
           
            // Achtung: erste Iteration bei i=1 für Dateibenennung!
            for (int i = 1; i <= AnzahlSchnitte; i++) {
               
                                   
                    inputStream.skip((long) (längeSkipsSekunden[i - 1] * bytesPerSecond));
                    float framesOfAudioToCopy = (float) (längeSchnitteSekunden[i - 1] * format.getFrameRate());
 
                   
//                  System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//                  System.out.println((längeSkipsSekunden[i - 1] * bytesPerSecond));
//                  System.out.println((long) (längeSkipsSekunden[i - 1] * bytesPerSecond));
//                  System.out.println((längeSchnitteSekunden[i - 1] * format.getFrameRate()));
//                  System.out.println((float) (längeSchnitteSekunden[i - 1] * format.getFrameRate()));
//                  System.out.println("-------------------");
//                  System.out.println((längeSkipsSekunden[i - 1]) + "/ " + i + channel);
//                  System.out.println("...............................");
//                  System.out.println((längeSchnitteSekunden[i - 1]) + "/ " + i + channel);
 
                           
                // getFrameRate liefert float zurück, daher cast in l.54. (?)
                // AudioInputStream erwartet long, daher cast zu long: frames im 10.000 bereich, meist x.0
               
                destinationFilePath = origdestinationFilePath;
 
                shortenedStream = new AudioInputStream(inputStream, format, (long) (framesOfAudioToCopy));
               
//              System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//              System.out.println(framesOfAudioToCopy);
//              System.out.println((long) (framesOfAudioToCopy));
               
 
                int pfadlaenge = destinationFilePath.length();
 
                String insert = null;
                if (i < 10) {
                    insert = channel + "_00";
                } else {
                    if (i < 100) {
                        insert = channel + "_0";
                    } else {
                        insert = channel + "_";
                    }
 
                }
                destinationFilePath = destinationFilePath.substring(0, pfadlaenge - 4) + insert + i
                        + destinationFilePath.substring(pfadlaenge - 4, pfadlaenge);
 
                File destinationFile = new File(destinationFilePath);
                AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);
 
               
               
               
 
            }
           
            System.out.println("channel " + channel + " wurde geschnitten");
 
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            if (shortenedStream != null)
                try {
                    shortenedStream.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            System.out.println("streams wurden geschlossen");
            System.out.println("");
        }
    }

	/*
	 * Methode zum Verarbeiten der Input-Dateien: Dateien werden nach Audio und
	 * Text gefiltert und in jeweilige Arrays abgelegt
	 */
	public File[] getAudioFilesToArray() {
		
		System.out.println("getAudioFilesToArray:");

		ParentsourceFile = sourceFile.getParentFile();

		// Filter zur kontrolle ob Datei mit .wav endet
		class AudioFilter implements FileFilter {
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(".wav");
			}
		}

		// Ablage aller Audiodateien des Verzeichnisses in audiofiles[]
		File[] files = ParentsourceFile.listFiles(new AudioFilter());
		Arrays.sort(files);

		// for (int i = 0; i < audiofiles.length; i++) {
		// System.out.println(audiofiles[i].getAbsolutePath());
		// }
		// }
		System.out.println("audio in array abgelegt");

        System.out.println("");
		
		
		
		return files;
	}

	public void getTextFilesToArray() {

		ParentsourceFile = sourceFile.getParentFile();

		
		System.out.println("getTextFilesToArray:");
		// Filter zur kontrolle ob Datei mit .txt endet
		class TextFilter implements FileFilter {
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(".txt");
			}
		}

		// Ablage aller Textdateien des Verzeichnisses in textfiles[]
		textfiles = ParentsourceFile.listFiles(new TextFilter());
		Arrays.sort(textfiles);

//		 for (int i = 0; i < textfiles.length; i++) {
//		 System.out.println(textfiles[i].getAbsolutePath());
//		 System.out.println(textfiles[i].getName());
//		
//		 }
		
		System.out.println("Txt-dateien in filearray abgelegt");
        System.out.println("");

	}

	// Methode zum Umwandeln der Werte einer einzeiligen Textdatei in ein
	// double[]
	public double[] TextToTime(File textZeiten) {

		double[] zeiten;
		String inputString = null;

		// Abschnitt zur Bestimmung der Anzahl der Startzeiten
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(textZeiten)));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			inputString = br.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// InputString bei Leerzeichen trennen ACHTUNG: Ein String mehr als
		// Zeiten, da Trennung "vor" erstem String
		String[] StringZeiten = inputString.split("\\s+");

		 for (int i = 0; i < StringZeiten.length; i++) {
		 System.out.println("ziffernString = " + StringZeiten[i]);
		 }

		// Strings in Integer parsen (stringArray besitzt als ersten string nur
		// leerzeichen durch trennung, daher indexverschiebung)
		zeiten = new double[StringZeiten.length - 1];
		for (int i = 1; i < StringZeiten.length; i++) {
			zeiten[i - 1] = Double.parseDouble(StringZeiten[i]);
		}

		// for (int i = 0; i < zeiten.length; i++) {
		// System.out.println("doublewert = " + zeiten[i]);
		// }

		return zeiten;
	}

	public void getTimeArrays() {

		
		System.out.println("getTimeArrays:");
		startzeitenCA = TextToTime(textfiles[0]);
		endzeitenCA = TextToTime(textfiles[1]);
		startzeitenKU = TextToTime(textfiles[2]);
		endzeitenKU = TextToTime(textfiles[3]);

		// System.out.println("startzeitenCA");
		// for (int i = 0; i < startzeitenCA.length; i++) {
		// System.out.print(startzeitenCA[i] + ", ");
		// }
		// System.out.println();
		// System.out.println("---------------------");
		//
		// System.out.println("endzeitenCA");
		// for (int i = 0; i < endzeitenCA.length; i++) {
		// System.out.print(endzeitenCA[i] + ", ");
		// }
		// System.out.println();
		// System.out.println("---------------------");
		//
		// System.out.println("startzeitenKU");
		// for (int i = 0; i < startzeitenKU.length; i++) {
		// System.out.print(startzeitenKU[i] + ", ");
		// }
		// System.out.println();
		// System.out.println("---------------------");
		//
		// System.out.println("endzeitenKU");
		// for (int i = 0; i < endzeitenKU.length; i++) {
		// System.out.print(endzeitenKU[i] + ", ");
		// }
		// System.out.println();
		// System.out.println("---------------------");
		System.out.println("Start und Endzeiten aus txt-datein extrahiert");
        System.out.println("");
	}

	// Methode liest durch getDataToArrays() die Dateien ein und generiert die
	// ZeitenArrays
	public void initalizeData() {
		System.out.println("initalizeData:");
		getTextFilesToArray();
		getTimeArrays();
		prepareTimes();
		audioChannels = getAudioFilesToArray();
		cutAudio(audioChannels[0], startzeitenCArdy, endzeitenCArdy, 'C');
		cutAudio(audioChannels[1], startzeitenKUrdy, endzeitenKUrdy, 'K');
		audiofiles = getAudioFilesToArray();

		splitSpeaker();

		System.out.println(textfiles[0]);
		System.out.println(textfiles[1]);
		System.out.println(textfiles[2]);
		System.out.println(textfiles[3]);
		System.out.println("---------------");

		System.out.println("startzeitenCArdy");
		for (int i = 0; i < startzeitenCArdy.length; i++) {
			System.out.print(startzeitenCArdy[i] + ", ");
		}
		System.out.println();
		System.out.println("---------------------");

		System.out.println("endzeitenCArdy");
		for (int i = 0; i < endzeitenCArdy.length; i++) {
			System.out.print(endzeitenCArdy[i] + ", ");
		}
		System.out.println();
		System.out.println("---------------------");

		System.out.println("startzeitenKUrdy");
		for (int i = 0; i < startzeitenKUrdy.length; i++) {
			System.out.print(startzeitenKUrdy[i] + ", ");
		}
		System.out.println();
		System.out.println("---------------------");

		System.out.println("endzeitenKUrdy");
		for (int i = 0; i < endzeitenKUrdy.length; i++) {
			System.out.print(endzeitenKUrdy[i] + ", ");
		}
		System.out.println();
		System.out.println("---------------------");
		
		System.out.println("initialisierung der Daten abgeschlossen");
        System.out.println("---------------------");
	}

	// Methode zur Anpassung der double[] startzeiten:
	// 1)Einfügen einer 0.0 beim Beginner des Gesprächs
	// 2) wenn channel mit silence beginnt bisher negative zahl -> rausschneiden
	public void prepareTimes() {

		// 3 fehler bei ffmpeg am Anfang oder Ende:

		// 1) Falls gespräch sofort beginnt fehlt 0.0 als Startzeit
		// 2) Falls gespräch ohne silence sofort endet fehlt letzte Endzeit
		// 3) Der Channel der mit Silence beginnt erhält negative Endzeit als
		// erste Zeit
		// Lösung:
		// 3) leichtes manuelles rausschneiden durch if abfrage: endzeit[] ->
		// endzeitrdy[])
		// 1) Falls Anfang falsch und rest richtig: erste Starzeit liegt hinter
		// erster endzeitrdy ->
		// Identifizierung und Einfügen von 0.0: startzeit[] -> startzeitrdy[]
		// 2) Falls Ende falsch und rest richtig (durch 3 und 1 eventuelle
		// Fehler jz behoben: if abfrage und vergleich
		// gegen gesamtdauer des audios geschieht in cutAudio
		//

		// negative Zeit rausschneiden
		if (endzeitenCA[0] < 0.0) {
			endzeitenCArdy = new double[endzeitenCA.length - 1];
			for (int i = 0; i < endzeitenCArdy.length; i++) {
				endzeitenCArdy[i] = endzeitenCA[i + 1];

			}
			System.out.println("negative Endzeit beim CA geschnitten");
		} else {
			endzeitenCArdy = endzeitenCA;
		}
		// negative Zeit rausschneiden
		if (endzeitenKU[0] < 0.0) {
			endzeitenKUrdy = new double[endzeitenKU.length - 1];
			for (int i = 0; i < endzeitenKUrdy.length; i++) {
				endzeitenKUrdy[i] = endzeitenKU[i + 1];

			}
			System.out.println("negative Endzeit beim KU geschnitten");
		} else {
			endzeitenKUrdy = endzeitenKU;
		}

		// ffmpeg-fehler beim CA: Gespräch beginnt sofort und erste Startzeit
		// fehlt
		if (startzeitenCA[0] > endzeitenCArdy[0]) {
			startzeitenCArdy = new double[startzeitenCA.length + 1];
			startzeitenCArdy[0] = 0.0;
			for (int i = 1; i < startzeitenCArdy.length; i++) {
				startzeitenCArdy[i] = startzeitenCA[i - 1];

			}
			System.out.println("anfangsnull beim CA eingefügt");
		} else {
			startzeitenCArdy = startzeitenCA;
		}
		// ffmpeg-fehler beim KU: Gespräch beginnt sofort und erste Startzeit
		// fehlt
		if (startzeitenKU[0] > endzeitenKUrdy[0]) {
			startzeitenKUrdy = new double[startzeitenKU.length + 1];
			startzeitenKUrdy[0] = 0.0;
			for (int i = 1; i < startzeitenKUrdy.length; i++) {
				startzeitenKUrdy[i] = startzeitenKU[i - 1];

			}
			System.out.println("anfangsnull beim KU eingefügt");

		} else {
			startzeitenKUrdy = startzeitenKU;
		}

		System.out.println("zeiten in arrays überprüft und korrigiert");
        System.out.println("");
	}

	// Methode zum Trennen der Redner:
	// audioFiles[] wird aufgeteilt in audiofilesKU und audiofilesCA
	public void splitSpeaker() {

		audiofilesKU = new File[startzeitenKUrdy.length];
		audiofilesCA = new File[startzeitenCArdy.length];
		int insertIntoKU = 0;
		int insertIntoCA = 0;

		for (int i = 0; i < audiofiles.length; i++) {
			// identifikation der Sprecher anhand dateiname: K_001.wav ->
			// Speaker
			// Kunde, C_001.wav -> Speaker Agent => 9t letzte Stelle des Strings
			if (audiofiles[i].toString()
					.substring(audiofiles[i].toString().length() - 9, audiofiles[i].toString().length() - 8)
					.equals("K")) {
				audiofilesKU[insertIntoKU] = audiofiles[i];
				insertIntoKU++;
			} else {
				if (audiofiles[i].toString()
						.substring(audiofiles[i].toString().length() - 9, audiofiles[i].toString().length() - 8)
						.equals("C")) {
					audiofilesCA[insertIntoCA] = audiofiles[i];
					insertIntoCA++;
				} else {
					System.out.println("Audiodatei in unerkanntem Format");
				}
			}
		}

		// for (int i = 0; i < audiofilesKU.length; i++) {
		// System.out.print(audiofilesKU[i].getAbsolutePath());
		// if (audiofilesKU[i].isDirectory()) {
		// System.out.print(" (Ordner)\n");
		// } else {
		// System.out.print(" (Datei)\n");
		// }
		// }
		// System.out.println("-------------------");
		// for (int i = 0; i < audiofilesCA.length; i++) {
		// System.out.print(audiofilesCA[i].getAbsolutePath());
		// if (audiofilesCA[i].isDirectory()) {
		// System.out.print(" (Ordner)\n");
		// } else {
		// System.out.print(" (Datei)\n");
		// }
		// }

		System.out.println("Speaker-Seperation abgeschlossen");
	}

	/*
	 * =========================================================================
	 * ================================================
	 * =========================================================================
	 * ================================================
	 * 
	 * Bis hierhin: Audio-Dateien werden in audiofilesKU[] (FILE) und
	 * audiofilesCA[] eingelesen, Trennung der Speaker anhand Dateinamen
	 * (siebt-letzter Character des Namens) Textdateien werden in
	 * startzeitenKU[] (INT) und startzeitenCA eingelesen Funktionalität durch
	 * auskommentierte Syso's kontrolliert.
	 * 
	 */
	public void processFiles(String speicherdestination) throws IOException {

		initalizeData();
		System.out.println("Beginn processFiles:");
        System.out.println("---------------------------------------------");
		bundler.setSnippetListSize(audiofiles.length-2);
		System.out.println("SnippetSize:" + (audiofiles.length-2));

		// Idee eines Stack:
		// Zeiger auf jeweiligen Feldern, "abnehmen" des aktuellsten Elemts
		// beider felder

		// Problem der Indizes: Falls ein Stack leer ist / der pointer eines
		// Felds am Ende steht, vergleicht das Element des anderes Felds
		// gegen Null(hinter das andere durch ++).
		// Daher boolean falls komplett durchlaufen

		int currentPositionKU = 0;
		int currentPositionCA = 0;
		boolean KUfinished = false;
		boolean CAfinished = false;

		for (int i = 0; i < (audiofilesKU.length + audiofilesCA.length); i++) {
			if ((startzeitenKUrdy[currentPositionKU] <= startzeitenCArdy[currentPositionCA]) || (CAfinished)) {
				bundler.addGespraechsStruktur("\n Kunde:  " + currentPositionKU);
				processAudio(audiofilesKU[currentPositionKU], "Customer");

				if (currentPositionKU < startzeitenKUrdy.length - 1) {
					currentPositionKU++;
				} else {
					startzeitenKUrdy[currentPositionKU] = startzeitenCArdy[currentPositionCA] + 99999;
					// KUfinished = true;
				}
			} else {
				if ((startzeitenCArdy[currentPositionCA] <= startzeitenKUrdy[currentPositionKU]) || (KUfinished)) {
					bundler.addGespraechsStruktur("\n Agent:  " + currentPositionCA);
					processAudio(audiofilesCA[currentPositionCA], "Agent");

					if (currentPositionCA < startzeitenCArdy.length - 1) {
						currentPositionCA++;
					} else {
						startzeitenCArdy[currentPositionCA] = startzeitenKUrdy[currentPositionKU] + 99999;
						// CAfinished = true;
					}
				}

			}
		}

	}
}
