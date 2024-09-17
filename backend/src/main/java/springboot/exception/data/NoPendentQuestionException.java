package springboot.exception.data;

public class NoPendentQuestionException extends RuntimeException {
    private static final long serialVersionUID = 1481888860192498156L;

	public NoPendentQuestionException(String message) {
        super(message);
    }
}
