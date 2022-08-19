package exceptions;

public class MultipleMappingException extends Exception{

    String exceptionMsg;

    public MultipleMappingException(Character c){

        exceptionMsg = "You have multiple mapping for " + c;
    }

    public MultipleMappingException(Object sign, Object objName, Object id){

        exceptionMsg = objName.getClass().getSimpleName() + " with id number - " + id + " have multiple mapping for " + sign;
    }

    @Override
    public String getMessage() {
        return exceptionMsg;
    }
}
