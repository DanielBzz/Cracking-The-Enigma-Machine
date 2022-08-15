import com.sun.org.apache.xml.internal.serialize.LineSeparator;
import scheme.generated.CTEReflector;

public class MainClass {

    public static void main(String[] args) {

        EnigmaMachineUI machineUI = new EnigmaMachineUI();

        machineUI.getXmlFile();

        machineUI.displayingMachineSpecification();

    }

}
