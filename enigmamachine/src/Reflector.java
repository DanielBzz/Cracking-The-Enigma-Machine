import java.util.HashMap;
import java.util.Map;

public class Reflector implements Convertor<Integer>{

    String id;
    Map<Integer,Integer> reflectorConversions = new HashMap<>();

    @Override
    public Integer convert(Integer position) {

        return reflectorConversions.get(position);
    }
}
