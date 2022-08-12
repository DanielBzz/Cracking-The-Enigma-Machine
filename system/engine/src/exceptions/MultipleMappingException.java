package exceptions;

public class MultipleMappingException extends Exception{

    String exceptionMsg;

    public MultipleMappingException(Object obj, Object id){

        exceptionMsg = String.format(obj.getClass().getSimpleName() + " with id number - " + id + " have invalid Conversion map");
    }


    public MultipleMappingException(Object obj, Object id, Object sign){

        exceptionMsg = String.format(obj.getClass().getSimpleName() + " with id number - " + id + " have multiple mapping for" + sign);
    }

    @Override
    public String getMessage() {
        return exceptionMsg;
    }
}
