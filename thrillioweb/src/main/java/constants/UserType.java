package constants;

/*public class UserType {
   private UserType() {}
   
   public static final String USER="user";
   public static final String EDITOR="editor";
   public static final String CHIEF_EDITOR="chiefeditor";
   
}
*/
public enum UserType{
	USER("user"),
	EDITOR("editor"),
	CHIEF_EDITOR("chiefeditor");
	
	private UserType(String userType) {
		this.type=userType;
	}
	
	private String type;
	public String getType() {
		return type;
	}
}