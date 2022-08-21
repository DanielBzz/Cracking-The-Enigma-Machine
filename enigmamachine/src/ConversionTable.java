import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConversionTable implements Serializable {

    private final List<Character> rightValues = new ArrayList<>();
    private final List<Character> leftValues = new ArrayList<>();

    public Character getRightCharacter(int position){

        return rightValues.get(position);
    }

    public Character getLeftCharacter(int position){

        return leftValues.get(position);
    }

    public int getCharIndexInRight(char character){

        return rightValues.indexOf(character);
    }

    public int getCharIndexInLeft(char character){

        return leftValues.indexOf(character);
    }

    public void add(Character right , Character left){

        rightValues.add(right);
        leftValues.add(left);
    }
}
