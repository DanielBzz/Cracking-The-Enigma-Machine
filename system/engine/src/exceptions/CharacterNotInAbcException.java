package exceptions;

import java.util.Set;

public class CharacterNotInAbcException extends Error{

    private final String exceptionMsg;

    public CharacterNotInAbcException(char character){
        exceptionMsg = "The character \"" + character + "\" is not part of the ABC";
    }

    public CharacterNotInAbcException(Set<Character> characters){
        exceptionMsg = "The characters " + characters + " is not part of the ABC";
    }

    @Override
    public String getMessage() {
        return exceptionMsg;
    }
}
