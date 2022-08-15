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

    public static String encryptedStringMsg(String encrypted){

        return "Your message after the encryption is: " + encrypted;
    }
}
