package components.subControllers;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class ActiveTeamDetailsComponent extends Pane {
    @FXML
    private Label teamName;

    @FXML
    private Label numberOfAgents;

    @FXML
    private Label taskSize;

    public ActiveTeamDetailsComponent(String teamName, int amountOfAgents, int taskSize){
        this.teamName.setText(teamName);
        this.numberOfAgents.setText(String.valueOf(amountOfAgents));
        this.taskSize.setText(String.valueOf(taskSize));
    }

}
