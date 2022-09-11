package components.body.machine;

import components.Reflector;
import components.body.details.ReflectorParent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Pair;

import java.util.Map;

public class ReflectorController {

    @FXML
    private Label reflectorId;
    private Reflector currentReflector;

    @FXML
    private ListView<Pair<Integer,Integer>> reflectorList;
    private ReflectorParent parentController;

    public void setParentController(ReflectorParent parent){

        parentController = parent;

    }

    public void setCurrentReflector(Reflector reflector) {
        currentReflector = reflector;

        reflectorId.setText(currentReflector.getId());

        Map<Integer,Integer> reflectMap = currentReflector.getReflectorConversions();

        for (Integer key : reflectMap.keySet()) {

            reflectorList.getItems().add(new Pair<>(key,reflectMap.get(key)));
        }
    }
}