package exceptions;

public class MachineNotDefinedException extends Error{

    @Override
    public String getMessage() {
        return "Machine not defined yet, first define initial code configuration";
    }
}
