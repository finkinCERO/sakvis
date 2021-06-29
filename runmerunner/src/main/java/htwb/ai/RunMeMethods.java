package htwb.ai;

public class RunMeMethods extends ParentClass {
	
	// -------------RUNME - METHODS --------------//
	
	// PUBLIC
	@RunMe
	public void findMe1 () {
		System.out.println("findMe1 - RnR - public method: done");
	}
	
	@RunMe
	public void findMe2 () {
		System.out.println("findMe2 - RnR - public method: done");
	}
	
	@RunMe
	public void methodOk1 () {

		System.out.println("RnR - methodOk1: done");
	}
	
	@RunMe
	public void methodOk2 () {

		System.out.println("RnR - methodOk2: done");
	}
	
	// PUBLIC - STATIC
	@RunMe
	public static String findMe3 () {
		System.out.println("findMe3 - RnR - public and static: done");
		return "findMe3".toUpperCase();
	}
	
	// PRIVATE
	@RunMe
	private void findMe4 () {
		System.out.println("findMe4 - RnR - private  method: done");
		//IllegalAccessException
	}
	
	// PACKAGE-PRIVATE
	@RunMe
	void findMe5 () {
		System.out.println("findMe5 - RnR - package-private method: done");
	}
	
	// PROTECTED
	@RunMe
	protected void methodProtected() {
		System.out.println("RnR - methodProtected: done");
	}
	
	// PRIVATE-PROTECTED & throws InvocationTargetException
	@Override
	@RunMe
	public int methodBOOM(){
		System.out.println("methodBOOM: done");
		return 1/0;
	}
	
	// WITH ARGUMENTS
	@RunMe 
	public void methodWithArg (String str) {
		System.out.println("RnR - methodWithArg: done");
		
	}

	// -------------NON-RUNME - METHODS --------------//
	
	// PUBLIC 1
	public void testWithoutRunMe() {
		System.out.println("nonRnR - testWithoutRunMe: done");
	}
	
	// PACKAGE-PROTECTED
	void testNoRM() {
		System.out.println("nonRnR - testNoRM: done");
	}

	// PRIVATE
	private void nonRunMePrivate() {

		System.out.println("nonRnR - private: done");
	}
	 
	// PUBLIC 2
	public void noRunMe1() {

		System.out.println("noRunMe1: done");
	}
	
	public void noRunMe2 () {

		System.out.println("noRunMe2: done");
	}
	
	// PACKAGE-PROTECTED
	void noRunMe3 () {
		System.out.println("noRunMe3 - package protected: done");
	}
	
	// PROTECTED
	protected void noRunMe4 () {
		System.out.println("noRunMe4 - protected: done");
	}
}
