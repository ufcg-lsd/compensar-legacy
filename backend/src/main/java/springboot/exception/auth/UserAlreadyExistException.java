package springboot.exception.auth;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException() {
        super("Já existe um usuário cadastrado com este email!");
    }
}
