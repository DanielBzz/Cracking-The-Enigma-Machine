import java.util.HashMap;
import java.util.Map;

public class Reflector implements Convertor<Integer>{

    private final String id;
    private final Map<Integer, Integer> reflectorConversions;

    public Reflector(String id, Map<Integer,Integer> reflectorMap){

        this.id = id;
        reflectorConversions = reflectorMap;
    }

    public String getId() {
        return id;
    }

    public void add(Integer key, Integer value){

        reflectorConversions.put(key,value);

        reflectorConversions.put(value,key);
    }

    @Override
    public Integer convert(Integer position) {

        return reflectorConversions.get(position);
    }
}
