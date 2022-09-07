package components.body.details;

import components.Rotor;
import components.body.machine.RotorController;
import components.body.main.BodyController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.EngineLogic;
import machineDtos.EngineInfoDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CodeCalibrationController implements rotorParent {

    BodyController parentController;
    @FXML
    private Button randomButton;
    @FXML
    private Button manualSetButton;
    @FXML
    private HBox rotorsPane;
    @FXML
    private VBox reflectorPane;
    private final ToggleGroup availableReflectorsGroup = new ToggleGroup();
    private final List<RotorController> rotorsChosen = new ArrayList<>();

    public void setParentController(BodyController controller){
        parentController = controller;
    }

    public void initialComponent(){

        EngineInfoDTO engineDetails = parentController.getEngineDetails();

        setReflectorPane(engineDetails.getNumOfOptionalReflectors());
        setRotorsPane(engineDetails.getEngineComponentsInfo().getOptionalRotors(),engineDetails.getNumOfUsedRotors());
        // setPlugs..
    }

    private void setRotorsPane(List<Rotor> optionalRotors, int numberOfUsedRotors){

        for(int i=0 ; i < numberOfUsedRotors; i++){

            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(RotorController.class.getResource("rotorComponent.fxml"));
                SplitPane newRotor = loader.load();
                RotorController newController = loader.getController();

                newController.setRotorIdChoiceBox(IntStream.range(1,optionalRotors.size() + 1).boxed().collect(Collectors.toList()));
                newController.setParentController(this);
                rotorsChosen.add(newController);
                rotorsPane.getChildren().add(newRotor);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setReflectorPane(int numOfOptionalReflectors){

        for(int i=0 ; i < numOfOptionalReflectors;i++) {

            RadioButton newRadioButton = new RadioButton();
            newRadioButton.setText(EngineLogic.idEncoder(i+1).name());
            availableReflectorsGroup.getToggles().add(newRadioButton);
            reflectorPane.getChildren().add(newRadioButton);
        }
    }

    @FXML
    void manualSetCodeActionListener(ActionEvent event) {

    }

    @FXML
    void randomCodeActionListener(ActionEvent event) {

    }

    @Override
    public Rotor getRotor(int id) {

        return parentController.getEngineDetails().getEngineComponentsInfo().getOptionalRotors().stream().
                filter(rotor -> rotor.getId() == id).findFirst().get();
    }

    public void hideSelectedRotorIdFromOthersControllers(RotorController e , Integer oldValue, Integer newValue){

        rotorsChosen.stream().filter(rotorController -> !rotorController.equals(e)).forEach(rotorController -> {
            if(oldValue != null) {
                rotorController.addRotorIdValue(oldValue);
            }
            rotorController.removeRotorIdValue(newValue);
        });

    }

}
