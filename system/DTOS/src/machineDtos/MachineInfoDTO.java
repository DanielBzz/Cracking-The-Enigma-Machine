package machineDtos;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MachineInfoDTO implements EnigmaMachineDTO, Serializable {

    private List<Integer> rotorsID;
    private List<Integer> notchDistanceFromPositions;
    private List<Character> rotorsInitPosition;
    private String reflectorID;
    private Map<Character,Character> plugs;

    public MachineInfoDTO(){
    }

    public MachineInfoDTO(List<Integer> rotorsID,List<Integer> notchDistance,List<Character> rotorsInitPosition, String reflectorID, Map<Character, Character> plugs) {
        this.rotorsID = rotorsID;
        this.rotorsInitPosition = rotorsInitPosition;
        this.reflectorID = reflectorID;
        this.plugs = plugs;
        notchDistanceFromPositions = notchDistance;
    }

    public List<Integer> getRotorsID() {

        return rotorsID;
    }

    public List<Character> getRotorsInitPosition() {

        return rotorsInitPosition;
    }

    public String getReflectorID() {

        return reflectorID;
    }

    public Map<Character, Character> getPlugs() {

        return plugs;
    }

    public List<Integer> getNotchDistanceFromPositions() {

        return notchDistanceFromPositions;
    }
}
