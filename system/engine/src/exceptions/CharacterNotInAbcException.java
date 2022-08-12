package exceptions;

public class CharacterNotInAbcException extends Error{

    private final String exceptionMsg;

    public CharacterNotInAbcException(Character c){
        exceptionMsg = "The character" + c + "is not part of the ABC";
    }

    @Override
    public String getMessage() {
        return exceptionMsg;
    }
}
