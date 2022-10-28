package exceptions;

public class ReadyForContestException extends Error{

    public ReadyForContestException(){
        super("your status is ready for contest/in contest, you can't do this action");
    }
}
