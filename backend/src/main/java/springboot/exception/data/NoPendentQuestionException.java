package springboot.exception.data;

public class NoPendentQuestionException extends RuntimeException {
    public NoPendentQuestionException(String message) {
        super(message);
    }
}
