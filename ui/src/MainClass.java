import com.sun.org.apache.xml.internal.serialize.LineSeparator;
import scheme.generated.CTEReflector;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainClass {

    public static void main(String[] args) {

       /* EnigmaMachineUI machineUI = new EnigmaMachineUI();

        machineUI.getXmlFile();

        machineUI.displayingMachineSpecification();
*/
        String input = "ABCDDDDAFEFBRWDEFH";
        String ABC = "ABCEFGL";
        Set<Character> a = input.chars().mapToObj(c->(char)c).filter(c->!ABC.contains(c.toString())).collect(Collectors.toSet());

        System.out.println(a);
    }

}
