package logic.datamanager;

import decryptionDtos.AgentAnswerDTO;

import java.util.ArrayList;
import java.util.List;

public class CandidatesManager {
    private final List<AgentAnswerDTO> candidates;

    public CandidatesManager(){
        candidates = new ArrayList<>();
    }

    public synchronized void addNewCandidates(List<AgentAnswerDTO> candidates){
        candidates.forEach(AgentAnswerDTO->candidates.add(AgentAnswerDTO));
    }

    public synchronized List<AgentAnswerDTO> getCandidates(){
        return candidates;
    }


}
