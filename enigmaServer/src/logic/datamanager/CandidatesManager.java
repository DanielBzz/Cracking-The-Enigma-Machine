package logic.datamanager;

import contestDtos.CandidateDataDTO;

import java.util.ArrayList;
import java.util.List;

public class CandidatesManager {
    private final List<CandidateDataDTO> candidates = new ArrayList<>();
    private int lastVersion = 0;

    public synchronized void addNewCandidates(List<CandidateDataDTO> candidates){

        candidates.forEach(candidate-> {
            lastVersion++;
            candidates.add(candidate);
        });
    }

    public synchronized List<CandidateDataDTO> getNewCandidates(int version){

        if(version < 0 || version > lastVersion){
            version = 0;
        }

        return candidates.subList(version, candidates.size());
    }

}
