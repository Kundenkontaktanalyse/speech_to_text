
import java.util.LinkedList;
import java.util.List;

public class JSONSetting {
	
	
	private String id;
	private String transkription;
	private double laenge;
	private String konfidenz;
	//private List<Float> konfidenzliste= new LinkedList<Float>();
	public JSONSetting(String id, String transkription, double laenge, String konfidenz ){
		this.konfidenz=konfidenz;
		this.laenge=laenge;
		this.id=id;
		this.transkription=transkription;
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
//	public double getLaenge() {
//		return laenge;
//	}
//	public void setLaenge(double laenge) {
//		this.laenge = laenge;
//	}
	

	
	

}
