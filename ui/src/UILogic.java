import sun.plugin.net.protocol.jar.CachedJarURLConnection;

import java.lang.reflect.Array;
import java.nio.file.LinkPermission;
import java.util.*;
import java.util.stream.Collectors;

public class UILogic {

    public static List<Integer> getIntListFromString(String s){

        List<Integer> intList = new ArrayList<>();

        if(s.lastIndexOf(",") == s.length() -1){
            throw new NumberFormatException("\"\"");
        }

        List<String> numberStrings = Arrays.asList(s.split(","));
        numberStrings.forEach(numStr->intList.add(0,Integer.parseInt(numStr)));

        return intList;
    }

    public static String getReflectorIdInput(Scanner scanner, int numOfReflectorsInSystem){

        System.out.println(outputMessages.getReflectorIdMenuMsg(numOfReflectorsInSystem));
        String input = scanner.nextLine();
        int reflectorIdChoice = UILogic.parseStringToIntInRange(input, 1 , numOfReflectorsInSystem);

        return EngineLogic.idEncoder(reflectorIdChoice).toString();
    }

    public static int parseStringToIntInRange(String input, int min, int max){

        int number = Integer.parseInt(input);

        if(number < min || number > max){
            throw new Error("Input number is out of range");
        }

        return number;
    }

    public static Map<Character, Character> parseStringToPlugs(Scanner scanner){

        System.out.println(outputMessages.getPlugsMsg());
        String input = scanner.nextLine();

        if(input.length() % 2 != 0){
            throw new Error("there is odd number of chars, it means that one of the characters not have pair");
        }

        Map<Character, Character> plugPairs = new HashMap<>();
        for(int i=0 ; i<input.length(); i+=2){
            plugPairs.put(input.charAt(i),input.charAt(i+1));
        }

        return plugPairs;
    }

    public static List<Integer> getRotorsIDsInput(Scanner scanner, int numberOfIds, int maxId) {

        System.out.println(outputMessages.getRotorsIdMsg(numberOfIds));
        String input = scanner.nextLine();
        List<Integer> IDs = UILogic.getIntListFromString(input);

        if(IDs.size() != numberOfIds){
            System.out.println(outputMessages.invalidNumberOfRotorsMsg());
        }

        if(IDs.stream().anyMatch(id-> id<1 || id > maxId)){
            System.out.println("some id is out of the range...");
        }

        return IDs;
    }

    public static List<Character> getRotorsInitialPositionsInput(Scanner scanner, int numberOfRotors){

        System.out.println(outputMessages.getRotorsPositionMsg(numberOfRotors));
        String input = scanner.nextLine();
        if(input.length() != numberOfRotors){
            System.out.println(outputMessages.invalidNumberOfInitialPositionsMsg());
        }

        return input.chars().mapToObj(c->(char)c).collect(Collectors.toList());
    }

}
