
import java.util.LinkedList;
import java.util.List;

public class JSONSetting {
	
	
	private String id;
	private String transkription;
	private double laenge;
	private float konfidenzDurchschnitt;
	private LinkedList<Float> konfidenzListe= new LinkedList<Float>();
	
	


	public JSONSetting(String id, String transkription, double laenge, LinkedList<Float> konfidenzListe ){
		this.konfidenzListe=konfidenzListe;
		this.laenge=Math.round((laenge/60)*10000)/10000.0;
		this.id=id;
		this.transkription=transkription;
		durchschnittsKonfidenzErmitteln();
	}
	

	
	private void durchschnittsKonfidenzErmitteln(){
		LinkedList<Float> temp= new LinkedList<Float>();
		temp=(LinkedList<Float>) getKonfidenzListe().clone();
		int i=getKonfidenzListe().size();
		
		float summe=0;
	while(!temp.isEmpty()){
		Float b=temp.remove();
		summe=summe+b;
	}
	setKonfidenzDurchschnitt(summe/i);
		
	}
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

	public double getLaenge() {
		return laenge;
	}

	public void setLaenge(double laenge) {
		this.laenge = laenge;
	}

	public float getKonfidenzDurchschnitt() {
		return konfidenzDurchschnitt;
	}

	public void setKonfidenzDurchschnitt(float konfidenzDurchschnitt) {
		this.konfidenzDurchschnitt = konfidenzDurchschnitt;
	}

	public LinkedList<Float> getKonfidenzListe() {
		return konfidenzListe;
	}

	public void setKonfidenzListe(LinkedList<Float> konfidenzListe) {
		this.konfidenzListe = konfidenzListe;
	}
	
	

}
