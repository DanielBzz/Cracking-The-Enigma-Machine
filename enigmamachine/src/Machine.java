import javafx.util.Pair;
import sun.plugin.net.protocol.jar.CachedJarURLConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Machine implements EnigmaMachine {

    private final List<Rotor> rotors;
    private final List<Integer> rotorsPositions;
    private final List<Character> ABC;
    private final Reflector reflector;
    private final PlugBoard plugBoard;

    public Machine(List<Rotor> rotorsList, List<Integer> rotorsPositions, Reflector reflector,  String abc, PlugBoard plugBoard) {

        this.reflector = reflector;
        rotors = rotorsList;
        this.rotorsPositions = rotorsPositions;
        ABC = abc.chars().mapToObj((c)->(char)c).collect(Collectors.toList());
        this.plugBoard = plugBoard;
    }

    public List<Integer> getRotorsId(){

        List<Integer> rotorsId = new ArrayList<>();
        rotors.forEach(rotor-> rotorsId.add(rotor.getId()));

        return rotorsId;
    }

    public String getReflectorId(){
        return reflector.getId();
    }

    public List<Character> getRotorsPositions(){

        List<Character> positions = new ArrayList<>();
        rotorsPositions.forEach(position->positions.add(ABC.get(position)));

        return positions;
    }

    public Map<Character,Character> getPlugs(){
        return plugBoard.getPlugChars();
    }

    @Override
    public Character encryption(Character value) {

        Character newChar = plugBoard.convert(value);
        int position = newChar == null ? ABC.indexOf(value) : ABC.indexOf(newChar);

        rotateRotors();
        for (int i = 0; i < rotors.size(); i++) {   // maybe change it to lambda exp?
            position = rotors.get(i).convert((position + rotorsPositions.get(i))% ABC.size());
            position = (ABC.size() + position - rotorsPositions.get(i)) % ABC.size();
        }

        position = reflector.convert(position);
        for (int i = rotors.size() - 1; i >= 0; i--) {
            position = rotors.get(i).convert((position + rotorsPositions.get(i)) % ABC.size());
            position = (ABC.size() + position - rotorsPositions.get(i)) % ABC.size();
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
        while(rotors.size() > index && rotors.get(index - 1).getNotchPosition() == rotorsPositions.get(index - 1));
    }

    private void setNewPositionForRotor(int index){

        int  newValue = rotorsPositions.get(index) + 1;

        if (ABC.size() == newValue){
            newValue = 0;
        }

        rotorsPositions.set(index, newValue);
    }

    public void setInitPositionForRotor(int index, int position){

        rotorsPositions.set(index, position);
    }

    public int getNotchDistanceFromPosition(int index){


        return (ABC.size() + rotors.get(index).getNotchPosition() - rotorsPositions.get(index)) % ABC.size();
    }

    public List<Integer> getNotchDistanceFromPositions(){

        List<Integer> notchDistanceFromPositions = new ArrayList<>();

        for(int i = 0 ; i < rotors.size() ; ++i) {
            notchDistanceFromPositions.add(getNotchDistanceFromPosition(i));
        }

        return notchDistanceFromPositions;
    }
}
