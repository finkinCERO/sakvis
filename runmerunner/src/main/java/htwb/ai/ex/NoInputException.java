package htwb.ai.ex;

public class NoInputException extends Exception {
	static NoInputException ex;
	
    public NoInputException(String msg){
        super(msg);
        System.out.println();
    }
}
