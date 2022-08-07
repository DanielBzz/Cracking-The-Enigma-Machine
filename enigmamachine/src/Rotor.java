import java.util.ArrayList;
import java.util.List;

public class Rotor implements Convertor<Integer> {

    private int id;
    private int notchPosition;
    private List<Conversion> rotorConversions = new ArrayList<>();

    public Rotor(int idValue, int notchValue){

        id = idValue;
        notchPosition = notchValue;
    }
    public int getId() {
        return id;
    }

    public int getNotchPosition() {
        return notchPosition;
    }

    public List<Conversion> getRotorConversions() {
        return rotorConversions;
    }

    public void setRotorConversions(List<Conversion> rotorConversions) {
        this.rotorConversions = rotorConversions;
    }

    @Override
    public Integer convert(Integer position) {

        return null;
    }
}
