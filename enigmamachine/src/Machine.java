import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Machine implements EnigmaMachine {

    private List<Rotor> rotors = new ArrayList<>();
    private List<Integer> rotorsPosition = new ArrayList<>();
    private Reflector reflector;
    private PlugBoard plugBoard;

    public Machine(Reflector reflector) {

    }

    @Override
    public String encryption(String value) {

        rotateRotors();

        return null;
    }

    @Override
    public void rotateRotors(){

        int index=0;

        do{
            setNewPositionForRotor(index);
            ++index;
        }
        while(rotors.size() > index && rotors.get(index - 1).getNotchPosition() == rotorsPosition.get(index - 1));
    }

    private void setNewPositionForRotor(int index){

        int  newValue = rotorsPosition.get(index) + 1;

        if (rotors.get(index).getRotorConversions().size() == newValue){ // maybe to include size of ABC and change the rotor
            newValue = 0;
        }

        rotorsPosition.set(index, newValue);
    }
}
