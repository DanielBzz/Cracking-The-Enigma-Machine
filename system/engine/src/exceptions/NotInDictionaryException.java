package exceptions;

public class NotInDictionaryException extends Error {

    public NotInDictionaryException(String msg){
        super("The word: " + msg + " does not appear in the dictionary");
    }
}
