package components.body.details;

import components.Rotor;
import components.body.machine.DynamicMachineComponentFactory;
import components.body.machine.RotorController;
import components.body.main.BodyController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.EngineLogic;
import machineDtos.EngineInfoDTO;
import machineDtos.MachineInfoDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CodeCalibrationController implements RotorParent {

    BodyController parentController;
    @FXML private Button randomButton;
    @FXML private Button manualSetButton;
    @FXML private HBox rotorsPane;
    @FXML private VBox reflectorPane;
    @FXML private ListView<String> plugsPane;
    @FXML private ChoiceBox<Character> leftPlugChoiceBox;
    @FXML private Button connectPlugsButton;
    @FXML private ChoiceBox<Character> rightPlugChoiceBox;
    private final ToggleGroup availableReflectorsGroup = new ToggleGroup();
    private final List<RotorController> rotorsChosen = new ArrayList<>();
    private SimpleBooleanProperty isCodeConfigurationSet = new SimpleBooleanProperty(false);
    private SimpleObjectProperty<MachineInfoDTO> machineInfoProperty = new SimpleObjectProperty<>();

    public SimpleObjectProperty<MachineInfoDTO> getMachineInfoProperty() {
        return machineInfoProperty;

    }

    public SimpleBooleanProperty getIsCodeConfigurationSetProperty(){

        return isCodeConfigurationSet;
    }

    public void setParentController(BodyController controller){
        parentController = controller;
    }

    public void initialComponent(EngineInfoDTO engineDetails){

        isCodeConfigurationSet.set(false);
        setReflectorPane(engineDetails.getNumOfOptionalReflectors());
        setRotorsPane(engineDetails.getEngineComponentsInfo().getOptionalRotors(),engineDetails.getNumOfUsedRotors());
        setPlugsPane(engineDetails.getEngineComponentsInfo().getABC());
    }

    private void setRotorsPane(List<Rotor> optionalRotors, int numberOfUsedRotors){

        for(int i=0 ; i < numberOfUsedRotors; i++){

                RotorController newController = DynamicMachineComponentFactory.createRotorOnPane(rotorsPane,this);
                newController.setRotorIdChoiceBox(IntStream.range(1,optionalRotors.size() + 1).boxed().collect(Collectors.toList()));
                rotorsChosen.add(newController);
        }
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

    private void setReflectorPane(int numOfOptionalReflectors){

        for(int i=0 ; i < numOfOptionalReflectors;i++) {

            RadioButton newRadioButton = new RadioButton();
            newRadioButton.setText(EngineLogic.idEncoder(i+1).name());
            availableReflectorsGroup.getToggles().add(newRadioButton);
            reflectorPane.getChildren().add(newRadioButton);
        }
    }

    @FXML
    public void manualSetCodeActionListener(ActionEvent event) {

        if(availableReflectorsGroup.getSelectedToggle() != null &&
                rotorsChosen.stream().allMatch(rotorController -> rotorController.getChosenRotorId() != null))
        {
            String reflectorId = ((RadioButton)availableReflectorsGroup.getSelectedToggle()).getText();
            List<Integer> rotorsIDs = new ArrayList<>();
            List<Character> rotorsInitialPositions = new ArrayList<>();
            rotorsChosen.forEach(rotor -> {
                        rotorsIDs.add(rotor.getChosenRotorId());
                        rotorsInitialPositions.add(rotor.getChosenInitialPosition());
                    });

            isCodeConfigurationSet.setValue(true);
            machineInfoProperty.set( new MachineInfoDTO(rotorsIDs, null, rotorsInitialPositions, reflectorId, setPlugsMap()));
        }
    }

    @FXML
    public void randomCodeActionListener(ActionEvent event) {

        parentController.randomCodeClicked();
        isCodeConfigurationSet.set(true);
    }

    private void setPlugsPane(List<Character> ABC) {

        ObservableList<Character> observableList = FXCollections.observableArrayList(ABC);
        leftPlugChoiceBox.setItems(observableList);
        rightPlugChoiceBox.itemsProperty().bind(leftPlugChoiceBox.itemsProperty());
    }

    @FXML
    public void connectPlugsButtonActionListener(ActionEvent event) {

        if(rightPlugChoiceBox.getValue() != null && leftPlugChoiceBox.getValue()!= null && leftPlugChoiceBox.getValue() != rightPlugChoiceBox.getValue())
        {
            plugsPane.getItems().add(leftPlugChoiceBox.getValue() + connectPlugsButton.getText() + rightPlugChoiceBox.getValue());
            rightPlugChoiceBox.getItems().remove(rightPlugChoiceBox.getValue());
            rightPlugChoiceBox.getItems().remove(leftPlugChoiceBox.getValue());
            leftPlugChoiceBox.setValue(null);
            rightPlugChoiceBox.setValue(null);
        }
    }

    @FXML
    public void onMouseClickedPlug(MouseEvent event) {

        if(event.getClickCount() == 2 && plugsPane.getSelectionModel().getSelectedItems().size() != 0){

            String chars = plugsPane.getSelectionModel().getSelectedItem().replaceAll(connectPlugsButton.getText(),"");
            rightPlugChoiceBox.getItems().add(chars.charAt(0));
            rightPlugChoiceBox.getItems().add(chars.charAt(1));
            plugsPane.getItems().remove(plugsPane.getSelectionModel().getSelectedIndex());
        }
    }

    private Map<Character,Character> setPlugsMap(){

        Map<Character,Character> plugs = new HashMap<>();
        List<String> pairs = new ArrayList<>();

        plugsPane.getItems().forEach(str ->pairs.add(str.replaceAll(connectPlugsButton.getText(),"")));
        pairs.forEach(pair-> plugs.put(pair.charAt(0),pair.charAt(1)));

        return plugs;
    }
}
