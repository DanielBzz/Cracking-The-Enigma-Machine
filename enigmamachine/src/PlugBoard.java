import java.util.HashMap;
import java.util.Map;

public class PlugBoard implements Convertor<Character>{

    Map<Character, Character> plugChars = new HashMap<>();

    public void init(){}

    public void add(Character key,Character value){

        plugChars.put(key, value);
        plugChars.put(value, key);
    }

    @Override
    public Character convert(Character character) {

        return plugChars.get(character);
    }
}
