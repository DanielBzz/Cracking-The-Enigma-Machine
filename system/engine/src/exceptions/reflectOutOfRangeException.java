package exceptions;

public class reflectOutOfRangeException extends Error {

    String exceptionMsg;

    public reflectOutOfRangeException(int outOfRangeNum, String id){

        exceptionMsg = "Reflector with id - " + id + " have out of range reflect - \"" + outOfRangeNum + "\"";
    }

    @Override
    public String getMessage() {
        return exceptionMsg;
    }
}
