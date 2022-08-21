package exceptions;

public class reflectOutOfRangeException extends Error {

    public reflectOutOfRangeException(int outOfRangeNum, String id){
        super("Reflector with id - " + id + " have out of range reflect - \"" + outOfRangeNum + "\"");
    }
}
