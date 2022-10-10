import com.google.gson.Gson;
import machineDtos.MachineInfoDTO;

import java.util.ArrayList;
import java.util.List;

public class JsonChecks {
    public static void main(String[] args) {
        List<Integer> ids = new ArrayList<>();
        List<Character> positions = new ArrayList<>();
        String reflector = "V";
        for (int i = 0; i < 5; i++) {
            ids.add(i);
            positions.add('A');
        }
        MachineInfoDTO machineInfoDTO = new MachineInfoDTO(ids, ids, positions, reflector, null);

        System.out.println(new Gson().toJson(machineInfoDTO));
        System.out.println();
    }
}
