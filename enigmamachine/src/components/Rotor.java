package components;

import java.io.Serializable;

public class Rotor implements Convertor<Integer>, Serializable {

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

    public int getPositionOfChar(char c) {

        return rotorConversions.getCharIndexInRight(c);
    }

    public char getCharInPosition(int position) {

        return rotorConversions.getRightCharacter(position);
    }

    @Override
    public Integer convert(Integer position) {

        int nextPosition = rightToLeft ? rotorConversions.getCharIndexInLeft(rotorConversions.getRightCharacter(position)) :
                rotorConversions.getCharIndexInRight(rotorConversions.getLeftCharacter(position));

        rightToLeft = !rightToLeft;

        return nextPosition;
    }
}