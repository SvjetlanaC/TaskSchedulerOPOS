package tasks;

public class IllegalTaskStateException extends Exception{
	
	private static final long serialVersionUID = 1L;
	public static final String MESSAGE = "Illegal task state!";
	
	public IllegalTaskStateException() { 
		super(MESSAGE); 
	}

    public IllegalTaskStateException(String message) { 
    	super(message); 
    }

}
