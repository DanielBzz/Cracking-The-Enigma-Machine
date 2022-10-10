package logic;

import scheme.generated.CTEBattlefield;

public class BattleField {

    private final String battleName;
    private final int numberOfAllies;
    private final DifficultyLevel level;

    public BattleField(String name, int allies , String level){

        battleName = name;
        numberOfAllies = allies;
        this.level = DifficultyLevel.valueOf(level.toUpperCase());
    }

    public static BattleField createBattleField(CTEBattlefield cteBattlefield){

        return new BattleField(cteBattlefield.getBattleName(),cteBattlefield.getAllies(),cteBattlefield.getLevel());
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
