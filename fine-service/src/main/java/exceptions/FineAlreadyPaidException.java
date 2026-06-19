package exceptions;

public class FineAlreadyPaidException extends RuntimeException{
		public FineAlreadyPaidException(Long id) {
		super("Fine with id : " + id + " has already been paid");
	}
}
