package springboot.exception.auth;

public class UserAlreadyExistException extends RuntimeException {
    private static final long serialVersionUID = 7291385226058269111L;

	public UserAlreadyExistException() {
        super("Já existe um usuário cadastrado com este email!");
    }
}
