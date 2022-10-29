package logic.datamanager;

import contestDtos.CandidateDataDTO;

import java.util.ArrayList;
import java.util.List;

public class CandidatesManager {
    private final List<CandidateDataDTO> candidates = new ArrayList<>();

    public synchronized void addNewCandidates(List<CandidateDataDTO> candidates){

        this.candidates.addAll(candidates);
    }

    public synchronized List<CandidateDataDTO> getNewCandidates(int version){

        if(version < 0 || version > candidates.size()){
            version = 0;
        }

        return candidates.subList(version, candidates.size());
    }

    public void clear(){
       candidates.clear();
    }

    public int getSize(){
        return candidates.size();
    }
}
