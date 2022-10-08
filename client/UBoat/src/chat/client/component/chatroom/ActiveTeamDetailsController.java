package chat.client.component.chatroom;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class ActiveTeamDetailsController extends Node {
    @FXML
    private Label teamName;

    @FXML
    private Label numberOfAgents;

    @FXML
    private Label taskSize;

    public ActiveTeamDetailsController(String teamName, int amountOfAgents, int taskSize){
        this.teamName.setText(teamName);
        this.numberOfAgents.setText(String.valueOf(amountOfAgents));
        this.taskSize.setText(String.valueOf(taskSize));
    }

    @Override
    protected NGNode impl_createPeer() {
        return null;
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        return null;
    }

    @Override
    protected boolean impl_computeContains(double localX, double localY) {
        return false;
    }

    @Override
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        return null;
    }
}
