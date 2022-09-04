import exceptions.*;
import logic.EnigmaEngine;
import logic.EnigmaMachineUI;
import logic.EnigmaSystemEngine;
import machineDtos.EngineInfoDTO;
import machineDtos.HistoryAndStatisticDTO;
import machineDtos.MachineInfoDTO;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MachineUI implements EnigmaMachineUI {

    private EnigmaSystemEngine enigmaSystem = new EnigmaEngine();
    private final Scanner scanner = new Scanner(System.in);

    public void run() throws NoSuchMethodException {

        outputMessages.initMenuMsg();
        String input;
        AtomicInteger choice = new AtomicInteger();

        do {
            System.out.println(outputMessages.getMenuMsg());
            input = scanner.nextLine();
            try {
                choice.set(Integer.parseInt(input));
                Arrays.stream(MachineUI.class.getDeclaredMethods()).filter(
                                method -> method.isAnnotationPresent(SortedMethod.class) &&
                                        method.getAnnotation(SortedMethod.class).value() == choice.get()).findFirst().get().
                        invoke(this, null);

            } catch (InvocationTargetException e) {
                System.out.println(e.getTargetException().getMessage());
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage() + " - invalid input");
            } catch (IllegalAccessException | NoSuchElementException e) {
                System.out.println(outputMessages.outOfRangeInputMsg());
            } catch (Exception | Error e) {
                System.out.println(e.getMessage());
            }

        } while (choice.get() != MachineUI.class.getMethod("exit", null).getAnnotation(SortedMethod.class).value());

        System.out.println("BYE :)");
    }

    @Override
    @SortedMethod(value = 1)
    public void loadNewXmlFile() throws Exception {

        System.out.println(outputMessages.getPathMsg());
        String xmlPath = scanner.nextLine();

        while (!Files.exists(Paths.get(xmlPath)) || !xmlPath.toString().substring(xmlPath.lastIndexOf(".") + 1).equals("xml")) {

            System.out.println(outputMessages.invalidPathMsg());
            xmlPath = scanner.nextLine();
            if (xmlPath.toUpperCase().equals("Q")) {
                return;
            }
        }

        enigmaSystem.loadXmlFile(xmlPath);
        System.out.println(outputMessages.getSuccessfulLoadMsg());
    }

    @Override
    @SortedMethod(value = 2)
    public void displayingMachineSpecification() {

        EngineInfoDTO machineSpecification = enigmaSystem.displayingMachineSpecification();
        System.out.println(outputMessages.machineSpecification(machineSpecification));
    }

    @Override
    @SortedMethod(value = 3)
    public void manualInitialCodeConfiguration() throws MultipleMappingException {

        checkFileLoaded();
        EngineInfoDTO machineSpecification = enigmaSystem.displayingMachineSpecification();

        List<Integer> rotorsIDs = UILogic.getRotorsIDsInput(scanner, machineSpecification.getNumOfUsedRotors(), machineSpecification.getNumOfOptionalRotors());
        List<Character> rotorsInitialPositions = UILogic.getRotorsInitialPositionsInput(enigmaSystem, scanner, machineSpecification.getNumOfUsedRotors());
        String reflectorId = UILogic.getReflectorIdInput(scanner, machineSpecification.getNumOfOptionalReflectors());
        Map<Character, Character> plugs = UILogic.getPlugsInput(enigmaSystem, scanner);

        MachineInfoDTO args = new MachineInfoDTO(rotorsIDs, null, rotorsInitialPositions, reflectorId, plugs);
        enigmaSystem.manualMachineInit(args);
    }

    @Override
    @SortedMethod(value = 4)
    public void automaticInitialCodeConfiguration() {

        enigmaSystem.automaticMachineInit();
    }

    @Override
    @SortedMethod(value = 5)
    public void encryptInput(){

        checkFileLoaded();
        System.out.println(outputMessages.getStringMsg());

        String msgToEncrypt = scanner.nextLine();
        String encryptedMsg = enigmaSystem.encryptString(msgToEncrypt);
        System.out.println(outputMessages.encryptedStringMsg(encryptedMsg));
    }

    @Override
    @SortedMethod(value = 6)
    public void resetCurrentCode(){

        enigmaSystem.resetTheMachine();
        System.out.println(outputMessages.resetCodeMsg());
    }

    @Override
    @SortedMethod(value = 7)
    public void getHistoryAndStatistics() {

        HistoryAndStatisticDTO details = enigmaSystem.getHistoryAndStatistics();
        System.out.println(outputMessages.historyMsg(details));
    }

    @Override
    @SortedMethod(value = 8)
    public void exit() {}

    @SortedMethod(value = 9)
    public void writeCurrentMachineStateToFile() throws IOException {

        System.out.println(outputMessages.getSaveFileMsg());
        String fileName = scanner.nextLine();

        if(enigmaSystem instanceof EnigmaEngine){
            ((EnigmaEngine) enigmaSystem).writeEngineToFile(fileName);
            System.out.println(outputMessages.getSuccessfulSaveMsg());
        }
    }

    @SortedMethod(value = 10)
    public void readMachineStateFromFile() throws Exception{

        System.out.println(outputMessages.getSaveFileMsg());
        String fileName = scanner.nextLine();

        if(enigmaSystem instanceof EnigmaEngine) {
            enigmaSystem = EnigmaEngine.readEngineFromFile(fileName);
            System.out.println(outputMessages.getSuccessfulLoadMsg());
        }
    }

    private void checkFileLoaded(){

        if(enigmaSystem instanceof EnigmaEngine && !((EnigmaEngine) enigmaSystem).isEngineInitialized()){
            throw new NoFileLoadedException();
        }
    }
}
