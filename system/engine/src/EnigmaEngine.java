import jdk.nashorn.internal.runtime.ECMAException;
import scheme.generated.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnigmaEngine implements EnigmaSystemEngine{

    private Machine enigmaMachine = null;
    private MachineInfoDTO currentMachineInfo = null;
    private String ABC;
    private List<Rotor> optionalRotors = new ArrayList<>();
    private List<Reflector> optionalReflectors = new ArrayList<>();
    private int rotorsCount;
    private Map<Map<String,String>,Integer> encryptedStrings;


    @Override
    public void loadXmlFile(String path) throws Exception {

        CTEEnigma enigmaMachineCTE = EngineLogic.createEnigmaFromFile(path.trim());
        EngineLogic.checkMachineIsValid(enigmaMachineCTE.getCTEMachine());
        engineInit(enigmaMachineCTE.getCTEMachine());
    }

    private void engineInit(CTEMachine enigmaMachineCTE){

        ABC = enigmaMachineCTE.getABC().trim();
        rotorsCount = enigmaMachineCTE.getRotorsCount();

        for (CTERotor rotor : enigmaMachineCTE.getCTERotors().getCTERotor()) {

            Conversion conversionTable = new Conversion();
            rotor.getCTEPositioning().forEach((position) ->
                    conversionTable.add(position.getRight().charAt(0), position.getLeft().charAt(0)));
            optionalRotors.add(new Rotor(rotor.getId(), rotor.getNotch(), conversionTable));
        }

        for (CTEReflector reflector : enigmaMachineCTE.getCTEReflectors().getCTEReflector()){

            Map<Integer,Integer> reflectorMap = new HashMap<>();
            reflector.getCTEReflect().forEach((reflect)-> reflectorMap.put(reflect.getInput(), reflect.getOutput()));
            optionalReflectors.add(new Reflector(reflector.getId(), reflectorMap));
        }
    }

    @Override
    public EngineInfoDTO displayingMachineSpecification() {

        return new EngineInfoDTO(optionalRotors.size(), optionalReflectors.size(), rotorsCount, encryptedStrings.size(), currentMachineInfo);
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
