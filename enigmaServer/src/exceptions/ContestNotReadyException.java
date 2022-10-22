package exceptions;

public class ContestNotReadyException extends Error{

    public ContestNotReadyException(){
        super("Not all the contest participants are ready");
    }
}
