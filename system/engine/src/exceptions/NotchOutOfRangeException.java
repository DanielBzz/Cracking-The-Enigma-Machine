package exceptions;

public class NotchOutOfRangeException extends Exception{

    private final String exceptionMsg;

    public NotchOutOfRangeException(int id, int notch, int size){

        exceptionMsg = "rotor with the id" + id + "have notch number " + notch + "the range is between 0 - " + size;
    }

    @Override
    public String getMessage(){
        return exceptionMsg;
    }
}
