import java.util.UUID;
public class GenerateUUID {
	public String generiereStringID(){
		UUID idOne=UUID.randomUUID();
		String sidOne=idOne.toString();
		return sidOne;
		
	}

}
