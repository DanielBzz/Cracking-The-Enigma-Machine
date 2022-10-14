package components.subControllers;

import contestDtos.ActivePlayerDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class PlayerDetailsComponent extends Pane {
    @FXML
    private Label nameHeader;

    @FXML
    private Label amountHeader;

    @FXML
    private Label sizeHeader;

    @FXML
    private Label nameLabel;

    @FXML
    private Label amountLabel;

    @FXML
    private Label sizeLabel;

    public PlayerDetailsComponent(ActivePlayerDTO newPlayer, String type){
        this.nameLabel.setText(newPlayer.getName());
        this.amountLabel.setText(String.valueOf(newPlayer.getAmount()));
        this.sizeLabel.setText(String.valueOf(newPlayer.getSize()));
        if(type.equals("allies")){
            nameHeader.setText("Team: ");
            amountHeader.setText("Amount of Agents: ");
            sizeHeader.setText("Task Size: ");
        }
        else{
            nameHeader.setText("Agent: ");
            amountHeader.setText("Amount of Threads: ");
            sizeHeader.setText("Task Size in One Take: ");
        }
    }

}
