import scheme.generated.*;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class EnigmaEngine implements EnigmaSystemEngine{

    CTEEnigma enigmaMachine;
    List<Rotor> optionalRotors = new ArrayList<>();
    List<Reflector> optionalReflectors = new ArrayList<>();

    @Override
    public void loadXmlFile(String path) {

        enigmaMachine = EngineLogic.createEnigmaFromFile(path.trim());

        System.out.println(EngineLogic.checkMachineIsValid(enigmaMachine.getCTEMachine()));

    }


    @Override
    public void displayingMachineSpecification() {

    }

    @Override
    public void manualMachineInit() {

    }

    @Override
    public void automaticMachineInit() {

    }

    @Override
    public void encryptString(String input) {

    }

    @Override
    public void resetTheMachine() {

    }

    @Override
    public void getHistoryAndStatistics() {

    }

    @Override
    public void exit() {

    }
}
