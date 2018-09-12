package springboot.exception;

public class RegisterNotFoundException extends NullPointerException {

	private static final long serialVersionUID = 1385185548090225157L;

	public RegisterNotFoundException(String message) {
		super(message);
	}

}