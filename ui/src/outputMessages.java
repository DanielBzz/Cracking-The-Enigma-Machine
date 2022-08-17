import java.nio.file.Path;
import java.sql.SQLData;
import java.text.MessageFormat;

public class outputMessages {

    public static String menu(){
        return null;
    }

    public static String getPathMsg(){

        return "Please enter path for your xml file:";
    }

    public static String invalidPathMsg(){

        return "The path you insert is not a valid path, please enter a new path:";
    }

    public static String machineSpecification(EngineInfoDTO engineInfo){

        StringBuilder msg = new StringBuilder();

        msg.append("Number of rotors(in use/optional): "  + engineInfo.getNumOfUsedRotors() + "/" + engineInfo.getNumOfOptionalRotors());
        msg.append(System.lineSeparator());
        msg.append("Number of reflectors: " + engineInfo.getNumOfOptionalReflectors());
        msg.append(System.lineSeparator());
        msg.append(MessageFormat.format("Amount of messages that encrypt in the machine: {0}", engineInfo.getNumOfEncryptedMsg()));
        msg.append(System.lineSeparator());
        msg.append(currentMachineSpecification(engineInfo.getMachineInfo()));

        return msg.toString();
    }

    private static String currentMachineSpecification(MachineInfoDTO machineInfo){

        StringBuilder msg = new StringBuilder();

        msg.append("<");
        for (int i = machineInfo.getRotorsInitPosition().size() - 1; i >= 0; --i) {
            msg.append(MessageFormat.format("{0}({1}),", machineInfo.getRotorsID().get(i), machineInfo.getNotchDistanceFromPositions().get(i)));
        }

        msg.replace(msg.length() - 1, msg.length(),">");
        msg.append("<");
        for (int i = machineInfo.getRotorsInitPosition().size() - 1; i >= 0; --i) {
            msg.append(machineInfo.getRotorsInitPosition().get(i));
        }

        msg.append(">");
        msg.append(MessageFormat.format("<{0}>", machineInfo.getReflectorID()));
        if(machineInfo.getPlugs().size() != 0 ){
            msg.append("<");
            for (Character key : machineInfo.getPlugs().keySet()){

                msg.append(MessageFormat.format("{0}|{1},", key, machineInfo.getPlugs().get(key)));
            }

            msg.replace(msg.length() - 1, msg.length(),">");
        }

        return msg.toString();
    }

    public static String getRotorsIdMsg(int numOfRotors){

        return "Please insert " + numOfRotors + " rotor's IDs you want to use in the machine(with commas between them):";
    }

    public static String getRotorsPositionMsg(int numOfRotors){
        return "Please enter sequence of " + numOfRotors + " initial positions from the ABC for the rotors(according to insertion order of the IDs):";
    }

    public static String invalidNumberOfInitialPositionsMsg(){
        return "number of initial positions not compatible to number of rotors";
    }

    public static String getReflectorIdMenuMsg(int numOfReflectors){

        StringBuilder msg = new StringBuilder();
        msg.append("Which reflector you want to have in the machine, please choose one of the following numbers:");

        for(int i = 1 ; i<= numOfReflectors ; i++){
            msg.append(System.lineSeparator());
            msg.append(i + ". " + EngineLogic.idEncoder(i));
        }

        return msg.toString();
    }

    public static String getPlugsMsg(){
        return "Please insert all the plugs you want in the system as a sequence string that each 2 character represent a plug:";
    }

    public static String invalidNumberOfRotorsMsg(){
        return "Number of rotors IDs not compatible with number of rotors you have in the machine";
    }

    public static String encryptedStringMsg(String encrypted){

        return "Your message after the encryption is: " + encrypted;
    }
}
