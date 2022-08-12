import exceptions.CharacterNotInAbcException;
import exceptions.MachineNotDefinedException;
import scheme.generated.*;

import java.util.*;
import java.util.stream.Collectors;

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

        optionalRotors.clear();
        for (CTERotor rotor : enigmaMachineCTE.getCTERotors().getCTERotor()) {

            Conversion conversionTable = new Conversion();
            rotor.getCTEPositioning().forEach((position) ->
                    conversionTable.add(position.getRight().charAt(0), position.getLeft().charAt(0)));
            optionalRotors.add(new Rotor(rotor.getId(), rotor.getNotch(), conversionTable));
        }

        optionalReflectors.clear();
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

        Random rand = new Random();
        List<Rotor> rotorsForMachine = new ArrayList<>();
        List<Integer> rotorsPositionsForMachine = new ArrayList<>();
        Reflector reflectorForMachine = optionalReflectors.get(rand.nextInt(optionalReflectors.size()));;
        PlugBoard plugBoardForMachine = automaticCreatePlugBoard(rand);

        for(int i=0; i < rotorsCount; ++i){
            rotorsForMachine.add((optionalRotors.get(rand.nextInt(optionalRotors.size()))));
            rotorsPositionsForMachine.add(rand.nextInt(ABC.length()));
        }

        enigmaMachine = new Machine(rotorsForMachine,rotorsPositionsForMachine,reflectorForMachine,ABC,plugBoardForMachine);
    }

    private PlugBoard automaticCreatePlugBoard(Random rand){

        PlugBoard plugBoard = new PlugBoard();

        int numOfPlugs = rand.nextInt(ABC.length()/2 + 1);
        List<Character> listOfAvailableChars = ABC.chars().mapToObj(e->(char)e).collect(Collectors.toList());
        Character first,second;

        for(int i=0; i < numOfPlugs; ++i) {

            first = listOfAvailableChars.get(rand.nextInt(listOfAvailableChars.size()));
            listOfAvailableChars.remove(first);
            second = listOfAvailableChars.get(rand.nextInt(listOfAvailableChars.size()));
            listOfAvailableChars.remove(second);
            plugBoard.add(first,second);
        }

        return plugBoard;
    }

    @Override
    public String encryptString(String input) {

        StringBuilder encryptedString = new StringBuilder();

        if(enigmaMachine == null){
            throw new MachineNotDefinedException();
        }

        for (Character c:input.toCharArray()) {
            if(!ABC.contains(c.toString().toUpperCase())){
                throw new CharacterNotInAbcException(c);
            }
            encryptedString.append(enigmaMachine.encryption(c));
        }

        return encryptedString.toString();
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
