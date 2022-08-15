package exceptions;

public class NoFileLoadedException extends Error {

    @Override
    public String getMessage() {
        return "first able you need to load file";
    }
}
