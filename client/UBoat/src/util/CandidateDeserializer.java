package util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import contestDtos.CandidateDataDTO;

import java.lang.reflect.Type;

public class CandidateDeserializer implements JsonDeserializer<CandidateDataDTO> {
    @Override
    public CandidateDataDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String decryptedMessage = json.getAsJsonObject().get("decryptedMessage").getAsString();
        String foundersName = json.getAsJsonObject().get("foundersName").getAsString();
        String configuration = json.getAsJsonObject().get("configuration").getAsString();
        return new CandidateDataDTO(decryptedMessage, foundersName, configuration);
    }
}
