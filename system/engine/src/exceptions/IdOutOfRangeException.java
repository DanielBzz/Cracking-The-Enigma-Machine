package exceptions;

import java.util.Collection;

public class IdOutOfRangeException extends Error {

    //String exceptionMsg;

    public IdOutOfRangeException(Collection<Integer> collection){
        super("The next IDs are out of range " + collection);
        //exceptionMsg = "The next IDs are out of range" + collection;
    }
}
