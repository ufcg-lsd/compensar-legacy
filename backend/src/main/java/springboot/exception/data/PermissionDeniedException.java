package springboot.exception.data;

public class PermissionDeniedException extends RuntimeException {
    private static final long serialVersionUID = 2035267621092903695L;

	public PermissionDeniedException(String message) {
        super(message);
    }
}
