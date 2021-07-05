package htwb.ai;

public class PrivateObject {

	private String privateString = null;
	public PrivateObject(String privateString) {
		System.setSecurityManager(new SecurityManager());
		this.privateString = privateString;
	}
	
	
}
