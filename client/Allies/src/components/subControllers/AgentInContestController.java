package components.subControllers;

import components.PlayerDetailsComponent;
import contestDtos.ActivePlayerDTO;

public class AgentInContestController extends PlayerDetailsComponent {
    private final int totalAmountOfTasks;
    private int unfinishedTasks;
    public AgentInContestController(ActivePlayerDTO newPlayer, String type) {
        super(newPlayer, type);
        totalAmountOfTasks = newPlayer.getAmount();
        unfinishedTasks = totalAmountOfTasks;
    }
    public void doneTask(){
        unfinishedTasks--;
    }

    public void updateUnfinishedTaskToScreen(){
        super.updateAmountLabel(unfinishedTasks, totalAmountOfTasks);
    }

    public void increaseAmountOfCandidatesToScreen(){
        super.increaseIntVal();
    }
}
