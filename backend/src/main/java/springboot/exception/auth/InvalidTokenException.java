package springboot.exception.auth;

public class InvalidTokenException extends RuntimeException {
    private static final long serialVersionUID = 5907806120761844240L;

	public InvalidTokenException() {
        super("Token inválido ou expirado!");
    }
}
