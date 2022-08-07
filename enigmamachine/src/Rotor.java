import java.util.ArrayList;
import java.util.List;

public class Rotor implements Convertor<Integer> {

    private int id;
    private int notchPosition;
    private Conversion rotorConversions = new Conversion();
    private boolean rightToLeft = true;

    public Rotor(int idValue, int notchValue){

        id = idValue;
        notchPosition = notchValue;
    }

    public Conversion getRotorConversions() {
        return rotorConversions;
    }

    public int getId() {
        return id;
    }

    public int getNotchPosition() {
        return notchPosition;
    }

    /**
     * get an initial position of the rotor and the right location
     * and return the initial location that connect to the position we ask for
     */
    @Override
    public Integer convert(Integer position) {

        int nextPosition;

        if(rightToLeft){
            nextPosition = rotorConversions.getCharIndexInLeft(rotorConversions.getRightCharacter(position));
        }
        else {
            nextPosition = rotorConversions.getCharIndexInRight(rotorConversions.getLeftCharacter(position));
        }

        rightToLeft = !rightToLeft;

        return nextPosition;
    }
}
