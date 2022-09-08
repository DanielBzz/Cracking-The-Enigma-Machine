package components.body.machine;

import components.body.details.ReflectorParent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ReflectorController {

    @FXML
    private Label reflectorId;

    @FXML
    private ListView<?> reflectorList;
    private ReflectorParent parentController;

    public void setParentController(ReflectorParent parent){

        parentController = parent;
    }

}