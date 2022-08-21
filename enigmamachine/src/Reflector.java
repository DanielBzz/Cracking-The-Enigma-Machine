import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Reflector implements Convertor<Integer>, Serializable {

    private final String id;
    private final Map<Integer, Integer> reflectorConversions;

    public Reflector(String id, Map<Integer,Integer> reflectorMap){

        this.id = id;
        reflectorConversions = reflectorMap;
    }

    public String getId() {

        return id;
    }

    @Override
    public Integer convert(Integer position) {

        return reflectorConversions.get(position);
    }
}
