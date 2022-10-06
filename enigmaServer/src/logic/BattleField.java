package logic;

public class BattleField {

    private final String battleName;
    private final int numberOfAllies;
    private final DifficultyLevel level;

    public BattleField(String name, int allies , String level){

        battleName = name;
        numberOfAllies = allies;
        this.level = DifficultyLevel.valueOf(level.toUpperCase());
    }


    public DifficultyLevel getLevel() {
        return level;
    }

    public int getNumberOfAllies() {
        return numberOfAllies;
    }

    public String getBattleName() {
        return battleName;
    }
}
