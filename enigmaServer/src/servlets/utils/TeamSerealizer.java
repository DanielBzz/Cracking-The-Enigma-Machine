package servlets.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import logic.serverdata.Team;

import java.lang.reflect.Type;

public class TeamSerealizer implements JsonSerializer<Team> {
    @Override
    public JsonElement serialize(Team src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject outerObject = new JsonObject();
        outerObject.addProperty("team name", src.getTeamName());
        outerObject.addProperty("amount of agents", src.getNumOfAgents());
        outerObject.addProperty("task size", src.getTaskSize());

        return outerObject;
    }
}
