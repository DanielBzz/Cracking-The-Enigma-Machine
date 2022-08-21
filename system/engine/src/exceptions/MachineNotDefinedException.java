package exceptions;

public class MachineNotDefinedException extends Error{

    public MachineNotDefinedException(){
        super("Machine not defined yet, first define initial code configuration");
    }
}
