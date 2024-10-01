package springboot.exception.data;

public class RegisterNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 7218176289464335813L;

	public RegisterNotFoundException(String message) {
		super(message);
	}
}