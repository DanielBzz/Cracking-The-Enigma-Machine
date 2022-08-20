import exceptions.CharacterNotInAbcException;
import exceptions.MachineNotDefinedException;
import exceptions.NoFileLoadedException;
import javafx.util.Pair;
import scheme.generated.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class EnigmaEngine implements EnigmaSystemEngine{

    private Machine enigmaMachine = null;
    private MachineInfoDTO currentInitialMachineInfo = null;
    private MachineInfoDTO currentMachineInfo = null;
    private String ABC;
    private final List<Rotor> optionalRotors = new ArrayList<>();
    private final List<Reflector> optionalReflectors = new ArrayList<>();
    private int rotorsCount;
    private Map<MachineInfoDTO, Map<Pair<String,String>, Long>> historyAndStat = new LinkedHashMap<>();

    @Override
    public void loadXmlFile(String path) throws Exception {

        CTEEnigma enigmaMachineCTE = EngineLogic.createEnigmaFromFile(path.trim());
        EngineLogic.checkMachineIsValid(enigmaMachineCTE.getCTEMachine());
        engineInit(enigmaMachineCTE.getCTEMachine());

    }

    private void engineInit(CTEMachine enigmaMachineCTE){

        ABC = enigmaMachineCTE.getABC().trim().toUpperCase();
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
        currentInitialMachineInfo = null;
        historyAndStat = new LinkedHashMap<>();
    }

    @Override
    public EngineInfoDTO displayingMachineSpecification() {

        if(!isEngineInitialized()){
            throw new NoFileLoadedException();
        }

        if(currentInitialMachineInfo!= null) {
            currentMachineInfo = initMachineInfo();
        }

        return new EngineInfoDTO(optionalRotors.size(), optionalReflectors.size(), rotorsCount, encryptedMsgSum() , currentInitialMachineInfo,currentMachineInfo);
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
        int i=0;
        for (Character initPosition : args.getRotorsInitPosition()) {
            rotorsPositions.add(rotors.get(i).getCharacterPosition(initPosition));
            i++;
        }


        reflector = optionalReflectors.stream().filter(reflector1 -> Objects.equals(reflector1.getId(), args.getReflectorID())).findFirst().get();

        for (Character keyPlug : args.getPlugs().keySet()) {
            plugBoard.add(keyPlug, args.getPlugs().get(keyPlug));
        }

        enigmaMachine = new Machine(rotors, rotorsPositions, reflector, ABC, plugBoard);
        currentInitialMachineInfo = initMachineInfo();
        historyAndStat.put(currentInitialMachineInfo, new LinkedHashMap<>());
    }

    @Override
    public void automaticMachineInit() {

        if(!isEngineInitialized()){
            throw new NoFileLoadedException();
        }

        Random rand = new Random();
        List<Rotor> rotors = new ArrayList<>();
        List<Integer> rotorsPositions = new ArrayList<>();
        Reflector reflector = optionalReflectors.get(rand.nextInt(optionalReflectors.size()));;
        PlugBoard plugBoard = automaticCreatePlugBoard(rand);

        boolean[] chosenRotors = new boolean[optionalRotors.size()];
        int rotorChosenIndex, i = 0;

        while(i != rotorsCount){
            rotorChosenIndex = rand.nextInt(optionalRotors.size());
            if(!chosenRotors[rotorChosenIndex]) {
                rotors.add((optionalRotors.get(rotorChosenIndex)));
                rotorsPositions.add(rand.nextInt(ABC.length()));
                chosenRotors[rotorChosenIndex] = true;
                i++;
            }
        }

        enigmaMachine = new Machine(rotors,rotorsPositions,reflector,ABC,plugBoard);
        currentInitialMachineInfo = initMachineInfo();
        historyAndStat.put(currentInitialMachineInfo, new LinkedHashMap<>());
    }

    private MachineInfoDTO initMachineInfo(){

        List<Integer> rotorsId = enigmaMachine.getRotorsId();
        List<Character> rotorsPositions = enigmaMachine.getRotorsPositions();
        List<Integer> notchDistanceInRotors = enigmaMachine.getNotchDistanceFromPositions();
        String reflectorId = enigmaMachine.getReflectorId();
        Map<Character,Character> plugs = enigmaMachine.getPlugs();
        return new MachineInfoDTO(rotorsId,notchDistanceInRotors, rotorsPositions ,reflectorId, plugs);
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

        historyAndStat.get(currentInitialMachineInfo).put(new Pair<>(message,encryptedString.toString()),encryptedTime);
        return encryptedString.toString();
    }

    @Override
    public void resetTheMachine() {

        if(enigmaMachine == null){
            throw new MachineNotDefinedException();
        }

        List<Character> rotorsInitPositions = currentInitialMachineInfo.getRotorsInitPosition();
        AtomicInteger rotorIndex = new AtomicInteger(0);

        rotorsInitPositions.forEach(C-> {
            enigmaMachine.setInitPositionForRotor(rotorIndex.get(), C);
            rotorIndex.incrementAndGet();
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

        Set<Character> notInAbcChars = input.chars().mapToObj(c->(char)c).filter(c->!ABC.contains(c.toString())).collect(Collectors.toSet());
        if(notInAbcChars.size() != 0){
            throw new CharacterNotInAbcException(notInAbcChars);
        }
    }

    private int encryptedMsgSum(){
        return historyAndStat.values().stream().mapToInt(Map::size).sum();
    }
//
//    //This method write the Engine details to a file.
//    public void writeToFile(String fileName) throws IOException{
//        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))){
//            out.writeObject(this);
//            out.flush();
//        }
//    }
//    //This method read the Engine details from a file.
//    public EngineImp readFromFile(String fileName) throws Exception{
//        try(ObjectInputStream in = (new ObjectInputStream(new FileInputStream(fileName)))){
//            EngineImp tempEngine = (EngineImp) in.readObject();
//            return tempEngine;
//        }
//
//    }
}
