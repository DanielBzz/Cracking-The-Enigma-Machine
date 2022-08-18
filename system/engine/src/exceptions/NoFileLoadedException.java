package exceptions;

public class NoFileLoadedException extends Error {

    @Override
    public String getMessage() {
        return "You need to load file";
    }
}
