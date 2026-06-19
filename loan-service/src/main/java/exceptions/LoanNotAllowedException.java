package exceptions;

public class LoanNotAllowedException extends RuntimeException{
	public LoanNotAllowedException(String message) {
		super(message);
	}
}
