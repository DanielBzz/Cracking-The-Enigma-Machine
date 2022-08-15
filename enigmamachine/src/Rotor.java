public class Rotor implements Convertor<Integer> {

    private final int id;
    private final int notchPosition;
    private final ConversionTable rotorConversions;
    private boolean rightToLeft = true;

    public Rotor(int idValue, int notchValue, ConversionTable conversionTable){

        id = idValue;
        notchPosition = notchValue;
        rotorConversions = conversionTable;
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
