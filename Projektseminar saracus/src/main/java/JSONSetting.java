
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

public class JSONSetting {
	
	
@Expose	private String id;
@Expose	private String transkription;
	//private double laenge;
//@Expose	private float konfidenzDurchschnitt;
private Date date=new Date();
@Expose private String transkriptZeitpunkt;
@Expose	private LinkedList<Float> konfidenzListe= new LinkedList<Float>();
@Expose	private LinkedList<Double> laengenListe= new LinkedList<Double>();
@Expose private JsonElement initialDaten;
	
	
	


	public LinkedList<Double> getLaengenListe() {
		return laengenListe;
	}



	public void setLaengenListe(LinkedList<Double> laengenListe) {
		this.laengenListe = laengenListe;
	}


	public JSONSetting(String id, String transkription, LinkedList<Double> laengenListe , LinkedList<Float> konfidenzListe, JsonElement intialDaten ){
		this.konfidenzListe=konfidenzListe;
		this.initialDaten=intialDaten;
		this.laengenListe=laengenListe;
		SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.YYYY hh:mm:ss");
		this.transkriptZeitpunkt=sdf.format(date);
	//	this.laenge=Math.round(sekUndMin(laenge)*10000)/10000.0;
		this.id=id;
		this.transkription=transkription;
//		durchschnittsKonfidenzErmitteln();
	//	System.out.println(getKonfidenzListe().getFirst().toString());
	}
	

	
//	private void durchschnittsKonfidenzErmitteln(){		
//		LinkedList<LinkedList<Float>> temp= new LinkedList<LinkedList<Float>>();
//	    LinkedList<Float> durchschnittsListe= new LinkedList<Float>();
//		temp= kopiereListe();
////		System.out.println("Test:" +getKonfidenzListe().get(0).get(0));
////		System.out.println(temp);
//		float summe=0;
//	while(!temp.isEmpty()){
//		//System.out.println(getKonfidenzListe().getFirst().toString());
//		int i=temp.getFirst().size();
//		while(!temp.getFirst().isEmpty()){
//			summe=summe+temp.getFirst().getFirst();
//			temp.getFirst().remove();
//		}
//		summe=summe/i;
//		durchschnittsListe.addLast(summe);
//		temp.remove();
//	}
//	getKonfidenzListe().addLast(durchschnittsListe);
//	System.out.println(getKonfidenzListe().size());
//	//System.out.println(getKonfidenzListe().getFirst().toString());
//	
//	}
//	
//	public LinkedList <LinkedList<Float>> kopiereListe(){
//		LinkedList<LinkedList<Float>> temp= new LinkedList<LinkedList<Float>>();
//		
//		for (int i=0;i<getKonfidenzListe().size(); i++){
//			
//			temp.addLast(null);
//			for (int j=0; j<getKonfidenzListe().get(i).size();j++){
//				temp.get(i).addLast(getKonfidenzListe().get(i).get(j));
//			}
//		}
//		System.out.println(temp.toString());
//	return 	temp;
//	}
//	
	public String getTranskription() {
		return transkription;
	}
	public void setTranskription(String transkription) {
		this.transkription = transkription;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

//	public double getLaenge() {
//		return laenge;
//	}
//
//	public void setLaenge(double laenge) {
//		this.laenge = laenge;
//	}

//	public float getKonfidenzDurchschnitt() {
//		return konfidenzDurchschnitt;
//	}
//
//	public void setKonfidenzDurchschnitt(float konfidenzDurchschnitt) {
//		this.konfidenzDurchschnitt = konfidenzDurchschnitt;
//	}

	public LinkedList<Float> getKonfidenzListe() {
		return konfidenzListe;
	}

	public void setKonfidenzListe(LinkedList<Float> konfidenzListe) {
		this.konfidenzListe = konfidenzListe;
	}
	
	

}
