package contestDtos;

import decryptionDtos.AgentAnswerDTO;

public class CandidateDataDTO {
    private final String decryptedMessage;
    private final String foundersName;
    private final String configuration;

    public CandidateDataDTO(String decryptedMessage, String foundersName, String configuration) {
        this.decryptedMessage = decryptedMessage;
        this.foundersName = foundersName;
        this.configuration = configuration;
    }

}
