package exceptions;

public class ReflectorSelfMapException extends  Error{


    public ReflectorSelfMapException(String id ,int selfReflect){
        super("Reflector with id - " + id + " have a self reflect for \""+ selfReflect + "\"");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
