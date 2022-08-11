package exceptions;

public class InvalidAbcException extends Exception {

    private final int AbcSize;

    public InvalidAbcException(int abcSize){
        this.AbcSize = abcSize;
    }

    @Override
    public String getMessage(){

        return AbcSize == 0 ? "Invalid ABC - ABC input is empty" : "Invalid ABC - ABC input is odd";
    }
}
