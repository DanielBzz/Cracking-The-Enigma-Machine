package util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import contestDtos.ActivePlayerDTO;
import logic.serverdata.Team;

import java.lang.reflect.Type;

public class ActivePlayerDTODeserializer implements JsonDeserializer<ActivePlayerDTO> {
    @Override
    public ActivePlayerDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String teamName = json.getAsJsonObject().get("team name").getAsString();
        int amountOfAgents = json.getAsJsonObject().get("amount of agents").getAsInt();
        int taskSize = json.getAsJsonObject().get("task size").getAsInt();
        return new ActivePlayerDTO(teamName, amountOfAgents, taskSize);
    }
}
