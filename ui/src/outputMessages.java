import javafx.util.Pair;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class outputMessages {

    public static String getMenuMsg() {

        Class clazz = EnigmaMachineUI.class;
        StringBuilder menuMsg = new StringBuilder("Please choose one of the following numbers:");

        List<Method> methods =Arrays.stream(clazz.getDeclaredMethods()).collect(Collectors.toList());

        methods.sort((method1, method2)-> {
            return method1.getAnnotation(SortedMethod.class).value() - method2.getAnnotation(SortedMethod.class).value();
        });

        for (Method method : methods) {
            menuMsg.append(System.lineSeparator());
            menuMsg.append(method.getAnnotation(SortedMethod.class).value() + ". " + fixName(method.getName()));
        }

        return menuMsg.toString();
    }

    private static String fixName(String input){

        StringBuilder output = new StringBuilder();

        for(int i =0;i<input.length();++i ){
            if(Character.isUpperCase(input.charAt(i))){
                output.append(" ");
            }
            output.append(input.charAt(i));
        }

        return output.toString().toLowerCase();
    }

    public static String getPathMsg(){

        return "Please enter path for your xml file:";
    }

    public static String getSuccessfullLoadMsg(){
        return "file loaded successfully";
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

        if(engineInfo.getMachineInitialInfo()!= null) {
            msg.append(System.lineSeparator());
            msg.append(currentMachineSpecification(engineInfo.getMachineInitialInfo()));
        }

        return msg.toString();
    }

    public static String historyMsg(HistoryAndStatisticDTO info){

        StringBuilder msg = new StringBuilder();

        for (MachineInfoDTO machineInfo : info.getHistoryAndStat().keySet())
        {
            msg.append("for code configuration: ");
            msg.append(currentMachineSpecification(machineInfo));
            msg.append(System.lineSeparator());
            int i = 1;
            for (Pair<String, String> pair : info.getHistoryAndStat().get(machineInfo).keySet()) {

                msg.append(i + ". <" + pair.getKey() + "> --> <" + pair.getValue() + "> ");
                msg.append("(" + info.getHistoryAndStat().get(machineInfo).get(pair) + "nano seconds)");
                msg.append(System.lineSeparator());
                i++;
            }
        }

        if(info.getHistoryAndStat().keySet().size() == 0){
            msg.append("You haven't done any actions on the machine yet");
        }

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

    public static String duplicateIdOfRotorsMsg(){
        return "You insert the same rotor twice, you can use a rotor only once per initial";
    }

    public static String getStringMsg(){
        return "Please enter a message that you want to encrypt";
    }

    public static String encryptedStringMsg(String encrypted){
        return "Your message after the encryption is: " + encrypted;
    }

    public static String invalidPlugsInputMsg(){
        return "there is odd number of chars, it means that one of the characters not have pair";
    }

    public static String outOfRangeInputMsg(){
        return "Input number is out of range";
    }

    public static String resetCodeMsg(){
        return "code reset to the initial code configuration";
    }
}
