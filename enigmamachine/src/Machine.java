import javafx.util.Pair;
import sun.plugin.net.protocol.jar.CachedJarURLConnection;

import java.util.ArrayList;
import java.util.List;

public class Machine implements EnigmaMachine {

    private List<Rotor> rotors = new ArrayList<>();
    private List<Integer> rotorsPosition = new ArrayList<>();
    private List<Character> ABC = new ArrayList<>();
    private Reflector reflector;
    private PlugBoard plugBoard;

    public Machine(Reflector reflector, Rotor rotor1, Rotor rotor2, int rotor1P, int rotor2P, List<Character> abc, PlugBoard o) {

        this.reflector = reflector;
        rotors.add(rotor1);
        rotors.add(rotor2);
        rotorsPosition.add(rotor1P);
        rotorsPosition.add(rotor2P);
        ABC = abc;
        plugBoard = o;
    }

    @Override
    public Character encryption(Character value) {

        Character newChar = plugBoard.convert(value);
        int position = newChar == null ? ABC.indexOf(value) : ABC.indexOf(newChar);

        rotateRotors();
        for (int i = 0; i < rotors.size(); i++) {   // maybe change it to lambda exp?
            position = rotors.get(i).convert((position + rotorsPosition.get(i))% ABC.size());
            position = (ABC.size() + position - rotorsPosition.get(i)) % ABC.size();
        }

        position = reflector.convert(position);
        for (int i = rotors.size() - 1; i >= 0; i--) {
            position = rotors.get(i).convert((position + rotorsPosition.get(i)) % ABC.size());
            position = (ABC.size() + position - rotorsPosition.get(i)) % ABC.size();
        }

        newChar = ABC.get(position);

        return  plugBoard.convert(newChar) == null ? newChar : plugBoard.convert(newChar);
    }

    @Override
    public void rotateRotors(){

        int index=0;

        do{
            setNewPositionForRotor(index);
            ++index;
        }
        while(rotors.size() > index && rotors.get(index - 1).getNotchPosition() - 1 == rotorsPosition.get(index - 1));
    }       // FIX THE NOTCH TO BE ALREADY WITH THE -1 (COUNT START FROM 0)

    private void setNewPositionForRotor(int index){

        int  newValue = rotorsPosition.get(index) + 1;

        if (ABC.size() == newValue){
            newValue = 0;
        }

        rotorsPosition.set(index, newValue);
    }
}
