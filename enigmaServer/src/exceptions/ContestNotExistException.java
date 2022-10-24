package exceptions;

public class ContestNotExistException extends Error{

    public ContestNotExistException(String username){
        super("for " + username + " still not define contest");
    }
}
