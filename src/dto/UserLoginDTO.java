package dto;

public class UserLoginDTO {
	public String password;
	public  String userName;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public UserLoginDTO() {
		super();
		
	}
	@Override
	public String toString() {
		return "UserLoginDTO [password=" + password + ", userName=" + userName + "]";
	}
	
  
}
