package exceptions;

public class UserNotExistException extends Exception{

    public UserNotExistException(String username){
        super("user:" + username + " not exist in the system!");
    }
}
