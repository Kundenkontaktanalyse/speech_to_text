import java.util.Date;

public class JSONSetting {
	
	
	private String id;
	private String transkription;
	private double laenge;
	
	public JSONSetting(String id, String transkription, double laenge ){
		this.id=id;
		this.transkription=transkription;
		this.laenge=laenge;
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
	

	
	

}
