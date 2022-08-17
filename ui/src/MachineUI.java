import com.sun.org.glassfish.gmbal.NameValue;
import exceptions.MultipleMappingException;
import exceptions.NoFileLoadedException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MachineUI implements EnigmaMachineUI{

    private final EnigmaSystemEngine enigmaSystem = new EnigmaEngine();
    private final Scanner scanner= new Scanner(System.in);


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
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void displayingMachineSpecification(){       // 2

        EngineInfoDTO machineSpecification;

        try {
            machineSpecification = enigmaSystem.displayingMachineSpecification();
            System.out.print(outputMessages.machineSpecification(machineSpecification));
        }catch (NoFileLoadedException e){
            System.out.println(e.getMessage());
        }
    }

    public void manualInitialCodeConfiguration(){        // 3

        EngineInfoDTO machineSpecification = enigmaSystem.displayingMachineSpecification();

        if(enigmaSystem instanceof EnigmaEngine && !((EnigmaEngine) enigmaSystem).isEngineInitialized()){
            throw new NoFileLoadedException();
        }

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

    public void automaticInitialCodeConfiguration() {      // 4

        try {
            enigmaSystem.automaticMachineInit();
        }catch (NoFileLoadedException e){
            System.out.println(e.getMessage());
        }
    }

    public void encryptInput(){        // 5

        System.out.println(outputMessages.getPathMsg());
        String msgToEncrypt = scanner.nextLine();

        try{
            String encryptedMsg = enigmaSystem.encryptString(msgToEncrypt);
            System.out.println(outputMessages.encryptedStringMsg(encryptedMsg));

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void resetCurrentCode(){         // 6

        try{
            enigmaSystem.resetTheMachine();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void getHistoryAndStatistics(){
        enigmaSystem.getHistoryAndStatistics();

        try {
            HistoryAndStatisticDTO details = enigmaSystem.getHistoryAndStatistics();
            // made the message;System.out.print(outputMessages.machineSpecification(machineSpecification));
        }catch (NoFileLoadedException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void exit() {

    }
}
