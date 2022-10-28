package contestDtos;

import javafx.util.Pair;

public class ContestDetailsDTO {
    private final String battleFieldName;
    private final String contestManagerName;
    private final boolean status;
    private final String level;
    private final Pair<Integer, Integer> teams;
    private final double taskSize;
    private final String encryptedMessage;

    public ContestDetailsDTO(String battleFieldName, String contestManagerName, boolean status, String level, Pair<Integer, Integer> teams, double taskSize, String encryptedMessage) {
        this.battleFieldName = battleFieldName;
        this.contestManagerName = contestManagerName;
        this.status = status;
        this.level = level;
        this.teams = teams;
        this.taskSize = taskSize;
        this.encryptedMessage = encryptedMessage;
    }

    public String getBattleFieldName() {
        return battleFieldName;
    }

    public String getContestManagerName() {
        return contestManagerName;
    }

    public boolean isStatus() {
        return status;
    }

    public String getLevel() {
        return level;
    }

    public Pair<Integer, Integer> getTeams() {
        return teams;
    }

    public double getTaskSize() {
        return taskSize;
    }

    public String getEncryptedMessage() {
        return encryptedMessage;
    }
}
