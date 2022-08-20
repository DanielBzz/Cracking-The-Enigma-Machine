import java.util.HashMap;
import java.util.Map;

public class PlugBoard implements Convertor<Character>{

    private final Map<Character, Character> plugChars = new HashMap<>();

    public Map<Character, Character> getPlugChars() {

        Map<Character,Character> returnMap = new HashMap<>();

        for (Character C : plugChars.keySet()) {
            if(!(returnMap.containsKey(C) || returnMap.containsValue(C))){
                returnMap.put(C,plugChars.get(C));
            }
        }

        return returnMap;
    }

    public void add(Character key, Character value){

        plugChars.put(key, value);
        plugChars.put(value, key);
    }

    @Override
    public Character convert(Character character) {

        return plugChars.get(character);
    }
}
