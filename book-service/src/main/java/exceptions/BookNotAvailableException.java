package exceptions;

public class BookNotAvailableException extends RuntimeException{
	public BookNotAvailableException(String id) {
		super("No available copies for book with id : " + id);
	}
}
