import java.util.HashMap;
import java.util.Map;

public class Reflector implements Convertor<Integer>{

    private String id;
    private final Map<Integer, Integer> reflectorConversions = new HashMap<>();

    public void add(Integer key, Integer value){

        reflectorConversions.put(key,value);
        reflectorConversions.put(value,key);
    }

    @Override
    public Integer convert(Integer position) {

        return reflectorConversions.get(position);
    }
}
