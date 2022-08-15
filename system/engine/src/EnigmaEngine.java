import exceptions.CharacterNotInAbcException;
import exceptions.MachineNotDefinedException;
import exceptions.NoFileLoadedException;
import scheme.generated.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class EnigmaEngine implements EnigmaSystemEngine{

    private Machine enigmaMachine = null;
    private MachineInfoDTO currentMachineInfo = null;
    private String ABC;
    private List<Rotor> optionalRotors = new ArrayList<>();
    private List<Reflector> optionalReflectors = new ArrayList<>();
    private int rotorsCount;
    private Map<Map<String,String>,Integer> encryptedStrings = new HashMap<>();

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
            reflector.getCTEReflect().forEach((reflect)-> {
                reflectorMap.put(reflect.getInput() - 1, reflect.getOutput() - 1);
                reflectorMap.put(reflect.getOutput() - 1,reflect.getInput() - 1);
            });
            optionalReflectors.add(new Reflector(reflector.getId(), reflectorMap));
        }
    }

    @Override
    public EngineInfoDTO displayingMachineSpecification() {

        if(ABC == null){
            throw new NoFileLoadedException();
        }

        return new EngineInfoDTO(optionalRotors.size(), optionalReflectors.size(), rotorsCount, encryptedStrings.size(), currentMachineInfo);
    }

    @Override
    public void manualMachineInit() {

    }

    @Override
    public void automaticMachineInit() {

        if(ABC == null){
            throw new NoFileLoadedException();
        }

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
        initMachineInfo(rotorsForMachine, rotorsPositionsForMachine, reflectorForMachine.getId(), plugBoardForMachine);
    }

    private void initMachineInfo(List<Rotor> rotors, List<Integer> positions, String reflectorId, PlugBoard plugs){

        List<Integer> rotorsId = new ArrayList<>();
        rotors.forEach(rotor-> rotorsId.add(0,rotor.getId()));

        List<Character> rotorsPositions = new ArrayList<>();
        positions.forEach(position->rotorsPositions.add(0,ABC.charAt(position)));

        List<Integer> notchDistanceInRotors =new ArrayList<>();

        for(int i = rotors.size() - 1 ; i >= 0 ; --i) {
            notchDistanceInRotors.add(enigmaMachine.getNotchDistanceFromPosition(i));
        }

        currentMachineInfo = new MachineInfoDTO(rotorsId,notchDistanceInRotors, rotorsPositions ,reflectorId, plugs.getPlugChars());
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

        input = input.toUpperCase();
        if(!input.chars().mapToObj(c->(char)c).allMatch(c->ABC.contains(c.toString()))){
            throw new CharacterNotInAbcException('A');
        }
        /*        for (Character c:input.toCharArray()) {
            if(!ABC.contains(c.toString().toUpperCase())){
                throw new CharacterNotInAbcException(c);
            }
        }*/

        for (Character c:input.toCharArray()) {
            encryptedString.append(enigmaMachine.encryption(c).toString());
        }

        return encryptedString.toString();
    }

    @Override
    public void resetTheMachine() {

        if(enigmaMachine == null){
            throw new MachineNotDefinedException();
        }

        List<Character> rotorsInitPositions = currentMachineInfo.getRotorsInitPosition();
        AtomicInteger rotorIndex = new AtomicInteger(rotorsInitPositions.size() - 1);

        rotorsInitPositions.forEach(C-> {
            enigmaMachine.setInitPositionForRotor(rotorIndex.get(), ABC.indexOf(C));
            rotorIndex.decrementAndGet();
        });
    }

    @Override
    public void getHistoryAndStatistics() {



    }

    @Override
    public void exit() {

    }
}
