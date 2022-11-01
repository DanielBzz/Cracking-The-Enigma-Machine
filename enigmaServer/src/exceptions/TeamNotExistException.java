package exceptions;

public class TeamNotExistException extends UserNotExistException {

    public TeamNotExistException(String username) {
        super(username);
    }
}
