package springboot.exception.data;

public class RegisterNotFoundException extends RuntimeException {
	public RegisterNotFoundException(String message) {
		super(message);
	}
}