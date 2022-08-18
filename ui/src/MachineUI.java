import exceptions.MultipleMappingException;
import exceptions.NoFileLoadedException;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MachineUI implements EnigmaMachineUI {

    private final EnigmaSystemEngine enigmaSystem = new EnigmaEngine();
    private final Scanner scanner = new Scanner(System.in);

    public void run() throws NoSuchMethodException {

        String input;
        AtomicInteger choice = new AtomicInteger();

        do {
            System.out.println(outputMessages.getMenuMsg());
            input = scanner.nextLine();
            try {
                choice.set(Integer.parseInt(input));
                Arrays.stream(EnigmaMachineUI.class.getDeclaredMethods()).filter(
                        method -> method.getAnnotation(SortedMethod.class).value() == choice.get()).findFirst().get().
                        invoke(this, null);

            }catch(InvocationTargetException e){
                System.out.println(e.getTargetException().getMessage());
            } catch (NoFileLoadedException | NumberFormatException e) {
                System.out.println(e.getMessage());
            } catch (IllegalAccessException | NoSuchElementException e) {
                e.printStackTrace();
                System.out.println(outputMessages.outOfRangeInputMsg());
            }

        } while (choice.get() != EnigmaMachineUI.class.getMethod("exit", null).getAnnotation(SortedMethod.class).value());

        System.out.println("BYE :)");
    }

    @Override
    public void loadNewXmlFile(){       // 1

        System.out.println(outputMessages.getPathMsg());
        String xmlPath = scanner.nextLine();

        while(!Files.exists(Paths.get(xmlPath)) && !xmlPath.toString().substring(xmlPath.lastIndexOf(".") + 1).equals("xml")){

            if(Files.exists(Paths.get(xmlPath))){
                System.out.println(xmlPath);
            }

            System.out.println(outputMessages.invalidPathMsg());
            xmlPath = scanner.nextLine();
        }

        try{
            enigmaSystem.loadXmlFile(xmlPath);
            System.out.println(outputMessages.getSuccessfullLoadMsg());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void displayingMachineSpecification(){       // 2 // work good

        EngineInfoDTO machineSpecification;

        try {
            machineSpecification = enigmaSystem.displayingMachineSpecification();
            System.out.println(outputMessages.machineSpecification(machineSpecification));
        }catch (NoFileLoadedException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void manualInitialCodeConfiguration(){        // 3

        checkFileLoaded();
        EngineInfoDTO machineSpecification = enigmaSystem.displayingMachineSpecification();

        try{
            List<Integer> rotorsIDs = UILogic.getRotorsIDsInput(scanner,machineSpecification.getNumOfUsedRotors(), machineSpecification.getNumOfOptionalRotors());
            List<Character> rotorsInitialPositions = UILogic.getRotorsInitialPositionsInput(enigmaSystem, scanner, machineSpecification.getNumOfUsedRotors());
            String reflectorId = UILogic.getReflectorIdInput(scanner, machineSpecification.getNumOfOptionalReflectors());
            Map<Character,Character> plugs =UILogic.getPlugsInput(enigmaSystem, scanner);

            MachineInfoDTO args = new MachineInfoDTO(rotorsIDs, null, rotorsInitialPositions,reflectorId, plugs);
            enigmaSystem.manualMachineInit(args);

        }catch (NumberFormatException e){
            System.out.println(e.getMessage() + " is not a valid ID");
        }catch (Error | MultipleMappingException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void automaticInitialCodeConfiguration() {      // 4

        try {
            enigmaSystem.automaticMachineInit();
        }catch (NoFileLoadedException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void encryptInput(){        // 5

        checkFileLoaded();
        System.out.println(outputMessages.getStringMsg());
        String msgToEncrypt = scanner.nextLine();

        try{
            String encryptedMsg = enigmaSystem.encryptString(msgToEncrypt);
            System.out.println(outputMessages.encryptedStringMsg(encryptedMsg));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void resetCurrentCode(){         // 6

        try{
            enigmaSystem.resetTheMachine();
            System.out.println(outputMessages.resetCodeMsg());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void getHistoryAndStatistics(){

        enigmaSystem.getHistoryAndStatistics();

        try {
            HistoryAndStatisticDTO details = enigmaSystem.getHistoryAndStatistics();
            System.out.println(outputMessages.historyMsg(details));
        }catch (NoFileLoadedException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void exit() {
    }

    private void checkFileLoaded(){

        if(enigmaSystem instanceof EnigmaEngine && !((EnigmaEngine) enigmaSystem).isEngineInitialized()){
            throw new NoFileLoadedException();
        }
    }
}
