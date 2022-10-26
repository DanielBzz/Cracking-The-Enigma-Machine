package contestDtos;

import java.util.Set;

public class TeamDetailsContestDTO {

    private final ContestDetailsDTO contestDetails;
    private final Set<ActivePlayerDTO> competitorsTeams;
    private final Set<ActivePlayerDTO> teamAgents;


    public TeamDetailsContestDTO(ContestDetailsDTO contestDetails, Set<ActivePlayerDTO> competitorsTeams, Set<ActivePlayerDTO> teamAgents) {
        this.contestDetails = contestDetails;
        this.competitorsTeams = competitorsTeams;
        this.teamAgents = teamAgents;
    }

    public ContestDetailsDTO getContestDetails() {
        return contestDetails;
    }

    public Set<ActivePlayerDTO> getCompetitorsTeams() {
        return competitorsTeams;
    }

    public Set<ActivePlayerDTO> getTeamAgents() {
        return teamAgents;
    }
}
