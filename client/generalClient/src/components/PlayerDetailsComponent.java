package components;

import contestDtos.ActivePlayerDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import static constants.Constants.*;

public class PlayerDetailsComponent extends Pane {
    @FXML
    private Label nameHeader;

    @FXML
    private Label amountHeader;

    @FXML
    private Label intValHeader;

    @FXML
    private Label nameLabel;

    @FXML
    private Label amountLabel;

    @FXML
    private Label intValLabel;

    public PlayerDetailsComponent(ActivePlayerDTO newPlayer, String type){
        this.nameLabel.setText(newPlayer.getName());
        this.amountLabel.setText(String.valueOf(newPlayer.getAmount()));
        this.intValLabel.setText(String.valueOf(newPlayer.getSize()));
        if(type.equals(ALLIES_TYPE)){
            nameHeader.setText("Team: ");
            amountHeader.setText("Amount of Agents: ");
            intValHeader.setText("Task Size: ");
        } else if (type.equals(AGENT_TYPE)) {
            nameHeader.setText("Agent: ");
            amountHeader.setText("Amount of Threads: ");
            intValHeader.setText("Task Size in One Take: ");
        } else{
            nameHeader.setText("Agent: ");
            amountHeader.setText("Waiting Tasks: ");
            intValHeader.setText("Total Of Potential Candidates: ");
        }
    }

    public void updateAmountLabel(int newVal, int totalVal){
        amountLabel.setText(String.valueOf(newVal) + FROM_SEPARATOR + String.valueOf(totalVal));
    }

    public void increaseIntVal(){
        intValLabel.setText(String.valueOf(Integer.parseInt(intValLabel.getText()) + 1));
    }
}
