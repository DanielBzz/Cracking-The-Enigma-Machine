package exceptions;

public class BattleFieldException extends Error{

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public BattleFieldException illegalName(){
        message = "Battle Field name is empty or illegal";
        return this;
    }

    public BattleFieldException illegalAlliesNumber(){
        message = "the number of allies should be at least 1";
        return this;
    }

    public BattleFieldException illegalLevel(){
        message = "illegal level difficulty";
        return this;
    }
}
