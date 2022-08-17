import exceptions.CharacterNotInAbcException;
import exceptions.MachineNotDefinedException;
import exceptions.NoFileLoadedException;
import javafx.util.Pair;
import scheme.generated.*;

import java.beans.IntrospectionException;
import java.io.File;
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
    private File historyAndStatistics;
    private Map<MachineInfoDTO, Map<Pair<String,String>, Long>> historyAndStat = new LinkedHashMap<>();

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

            ConversionTable conversionTable = new ConversionTable();
            rotor.getCTEPositioning().forEach((position) ->
                    conversionTable.add(position.getRight().charAt(0), position.getLeft().charAt(0)));
            optionalRotors.add(new Rotor(rotor.getId(), rotor.getNotch() - 1, conversionTable));
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

        enigmaMachine = null;
        currentMachineInfo = null;
        historyAndStat = new LinkedHashMap<>();
    }

    @Override
    public EngineInfoDTO displayingMachineSpecification() {

        if(ABC == null){
            throw new NoFileLoadedException();
        }

        return new EngineInfoDTO(optionalRotors.size(), optionalReflectors.size(), rotorsCount, historyAndStat.size(), currentMachineInfo);
    }

    @Override
    public void manualMachineInit(EnigmaMachineDTO args) {

        if (!isEngineInitialized()) {
            throw new NoFileLoadedException();
        }

        List<Rotor> rotors = new ArrayList<>();
        List<Integer> rotorsPositions = new ArrayList<>();
        Reflector reflector;
        PlugBoard plugBoard = new PlugBoard();

        for (Integer id : args.getRotorsID()) {
            rotors.add(optionalRotors.stream().filter(rotor -> rotor.getId() == id).findFirst().get());
        }
        for (Character initPosition : args.getRotorsInitPosition()) {
            rotorsPositions.add(ABC.indexOf(initPosition));
        }

        reflector = optionalReflectors.stream().filter(reflector1 -> Objects.equals(reflector1.getId(), args.getReflectorID())).findFirst().get();

        for (Character keyPlug : args.getPlugs().keySet()) {
            plugBoard.add(keyPlug, args.getPlugs().get(keyPlug));
        }

        enigmaMachine = new Machine(rotors, rotorsPositions, reflector, ABC, plugBoard);
        initMachineInfo(rotors, rotorsPositions, reflector.getId(), plugBoard);

    }

    @Override
    public void automaticMachineInit() {

        if(!isEngineInitialized()){
            throw new NoFileLoadedException();
        }

        Random rand = new Random();
        List<Rotor> rotorsForMachine = new ArrayList<>();
        List<Integer> rotorsPositionsForMachine = new ArrayList<>();
        Reflector reflectorForMachine = optionalReflectors.get(rand.nextInt(optionalReflectors.size()));;
        PlugBoard plugBoardForMachine = automaticCreatePlugBoard(rand);

        boolean[] chosenRotors = new boolean[optionalRotors.size()];
        int rotorChosenIndex;

        for(int i=0; i < rotorsCount; ++i){
            rotorChosenIndex = rand.nextInt(optionalRotors.size());
            if(!chosenRotors[rotorChosenIndex]){

                rotorsForMachine.add((optionalRotors.get(rotorChosenIndex)));
                rotorsPositionsForMachine.add(rand.nextInt(ABC.length()));
                chosenRotors[rotorChosenIndex] = true;
            }
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
        historyAndStat.put(currentMachineInfo, new LinkedHashMap<>());
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
    public String encryptString(String message) {

        StringBuilder encryptedString = new StringBuilder();

        if(enigmaMachine == null){
            throw new MachineNotDefinedException();
        }

        message = message.toUpperCase();
        checkIfCharactersInABC(message);

        Long encryptedTime = System.nanoTime();
        for (Character c:message.toCharArray()) {
            encryptedString.append(enigmaMachine.encryption(c).toString());
        }

        encryptedTime = System.nanoTime() - encryptedTime;

        historyAndStat.get(currentMachineInfo).put(new Pair<>(message,encryptedString.toString()),encryptedTime);
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
    public HistoryAndStatisticDTO getHistoryAndStatistics() {

        if(!isEngineInitialized()){
            throw new NoFileLoadedException();
        }

        return new HistoryAndStatisticDTO(historyAndStat);
    }

    @Override
    public void exit() {

    }

    public boolean isEngineInitialized(){
        return ABC != null;
    }

    public void checkIfCharactersInABC(String input){       // maybe change it to Collection instead of string

        Set<Character> notInAbcChars = input.chars().mapToObj(c->(char)c).filter(c->ABC.contains(c.toString())).collect(Collectors.toSet());
        if(notInAbcChars.size() != 0){
            throw new CharacterNotInAbcException(notInAbcChars);
        }
    }
}
