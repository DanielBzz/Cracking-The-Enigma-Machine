package thirdParty.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import machineDtos.EnigmaMachineDTO;
import machineDtos.MachineInfoDTO;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class MachineInfoDTOSerializer implements JsonSerializer<MachineInfoDTO> {
    private void serializeList(List<Object> objects, String objectName, JsonObject outerObject){
        for (int i = 0; i < objects.size(); i++) {
            if(objectName.equals("rotor")){
                outerObject.addProperty(objectName + i, (int)objects.get(i));
            } else if (objectName.equals("position")) {
                outerObject.addProperty(objectName + i, (char)objects.get(i));
            }
        }
    }

    @Override
    public JsonElement serialize(MachineInfoDTO src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject outerObject = new JsonObject();
        serializeList(Collections.singletonList(src.getRotorsID()), "rotor", outerObject);
        serializeList(Collections.singletonList(src.getRotorsInitPosition()), "position", outerObject);
        outerObject.addProperty("reflector", src.getReflectorID());
        return outerObject;
    }
}
