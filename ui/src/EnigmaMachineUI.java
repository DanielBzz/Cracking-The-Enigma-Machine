import exceptions.NoFileLoadedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EnigmaMachineUI {

    EnigmaSystemEngine enigmaSystem = new EnigmaEngine();
    Scanner scanner= new Scanner(System.in);

    public void getXmlFile(){       // 1

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
        enigmaSystem.automaticMachineInit();

        try {
            machineSpecification = enigmaSystem.displayingMachineSpecification();
            System.out.print(outputMessages.machineSpecification(machineSpecification));
        }catch (NoFileLoadedException e){
            System.out.println(e.getMessage());
        }
    }

    public void manualInitial(){        // 3

        EngineInfoDTO machineSpecification = enigmaSystem.displayingMachineSpecification();

        try{
            List<Integer> rotorsIDs = UILogic.getRotorsIDsInput(scanner,machineSpecification.getNumOfUsedRotors(), machineSpecification.getNumOfOptionalRotors());
            //  need to throw exception instead of return boolean

            List<Character> rotorsInitialPositions = UILogic.getRotorsInitialPositionsInput(scanner, machineSpecification.getNumOfUsedRotors());
            // check if all the positions belong to the ABC;

            String reflectorId = UILogic.getReflectorIdInput(scanner, machineSpecification.getNumOfOptionalReflectors());

            Map<Character,Character> plugs =UILogic.parseStringToPlugs(scanner);

            MachineInfoDTO args = new MachineInfoDTO(rotorsIDs, null, rotorsInitialPositions,reflectorId, plugs);
            enigmaSystem.manualMachineInit(args);

        }catch (NumberFormatException e){
            System.out.println(e.getMessage() + " is not a valid ID");
        }catch (Error e){
            System.out.println(e.getMessage());
        }

        // add all the exceptions it should have...
    }

    public void automaticInitial() {      // 4

        try {
            enigmaSystem.automaticMachineInit();
        }catch (NoFileLoadedException e){
            System.out.println(e.getMessage());
        }
    }

    public void encryptString(){        // 5

        System.out.println(outputMessages.getPathMsg());
        String msgToEncrypt = scanner.nextLine();

        try{
            String encryptedMsg = enigmaSystem.encryptString(msgToEncrypt);
            System.out.println(outputMessages.encryptedStringMsg(encryptedMsg));

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void resetMachine(){         // 6

        try{
            enigmaSystem.resetTheMachine();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
