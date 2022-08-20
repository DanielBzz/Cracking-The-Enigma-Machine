package exceptions;

public class NotchOutOfRangeException extends Exception{

    private final String exceptionMsg;

    public NotchOutOfRangeException(int id, int notch, int size){

        exceptionMsg = "Rotor with id - " + id + " have notch in position \"" + notch + "\" the range is between 1 - " + size;
    }

    @Override
    public String getMessage(){
        return exceptionMsg;
    }
}
