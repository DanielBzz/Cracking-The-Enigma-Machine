import java.io.IOException;

public class MainClass {

    public static void main(String[] args) {

        MachineUI enigmaMachine = new MachineUI();
        try {
            enigmaMachine.run();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
