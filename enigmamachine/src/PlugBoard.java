import java.util.HashMap;
import java.util.Map;

public class PlugBoard implements Convertor<Character>{

    Map<Character, Character> plugChars = new HashMap<>();

    public void init(){}

    @Override
    public Character convert(Character character) {
        return plugChars.get(character);
    }
}
