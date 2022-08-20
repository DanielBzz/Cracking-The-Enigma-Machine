package exceptions;

public class MissingMapException extends Exception{

    private final String exceptionMsg;

    public MissingMapException(Object obj, Object id){
        exceptionMsg = obj.getClass().getSimpleName() + " with id number - " + id + " is missing mappings";
    }

    @Override
    public String getMessage() {
        return exceptionMsg;
    }
}
