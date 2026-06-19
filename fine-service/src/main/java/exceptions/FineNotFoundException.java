package exceptions;

public class FineNotFoundException extends RuntimeException{
	public FineNotFoundException(Long id) {
		super("Fine not found with id : " + id);
	}
}
