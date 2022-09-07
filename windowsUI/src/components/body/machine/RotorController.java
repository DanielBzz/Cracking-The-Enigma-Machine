package components.body.machine;

import components.Rotor;
import components.body.details.CodeCalibrationController;
import components.body.details.rotorParent;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.util.List;

public class RotorController {

    rotorParent parentController;

    @FXML
    private ChoiceBox<Integer> rotorIdChoiceBox;

    @FXML
    private Button previousButton;

    @FXML
    private Button currentButton;

    @FXML
    private Button nextButton;
    private Rotor currentRotor;

//    public RotorController(rotorParent a){
//
//        parentController =a;
//    }

    @FXML
    public void initialize(){}

    @FXML
    void currentButtonActionListener(ActionEvent event) {

    }

    @FXML
    void nextButtonActionListener(ActionEvent event) {

        previousButton.setText(String.valueOf(currentButton.getText()));
        currentButton.setText(String.valueOf(nextButton.getText()));
        nextButton.setText(String.valueOf(currentRotor.getCharInPosition(
                (currentRotor.getPositionOfChar(nextButton.getText().charAt(0)) + 1) % currentRotor.getConversionTableSize())));
    }

    @FXML
    void previousButtonActionListener(ActionEvent event) {

        nextButton.setText(String.valueOf(currentButton.getText()));
        currentButton.setText(String.valueOf(previousButton.getText()));
        previousButton.setText(String.valueOf(currentRotor.getCharInPosition(
                (currentRotor.getPositionOfChar(previousButton.getText().charAt(0)) - 1 + currentRotor.getConversionTableSize()) % currentRotor.getConversionTableSize())));

    }

    public void setParentController(rotorParent controller) {

        this.parentController = controller;
        rotorIdChoiceBox.valueProperty().addListener(((observable, oldValue, newValue) -> setCurrentRotor(this.parentController.getRotor(newValue))));
        if(this.parentController instanceof CodeCalibrationController){
            rotorIdChoiceBox.valueProperty().addListener(
                    (observable, oldValue, newValue) -> ((CodeCalibrationController)parentController).hideSelectedRotorIdFromOthersControllers(this,oldValue,newValue));
        }
    }

    public void setRotorIdChoiceBox(List<Integer> options){

        rotorIdChoiceBox.setItems(FXCollections.observableList(options));
    }

    public void addRotorIdValue(int id){
        rotorIdChoiceBox.getItems().add(id);
    }

    public void removeRotorIdValue(int id){
        rotorIdChoiceBox.getItems().removeAll(id);
    }

    public void setCurrentRotor(Rotor rotor){
        currentRotor = rotor;
        currentButton.setText(String.valueOf(currentRotor.getCharInPosition(0)));
        nextButton.setText(String.valueOf(currentRotor.getCharInPosition(1)));
        previousButton.setText(String.valueOf(currentRotor.getCharInPosition(currentRotor.getConversionTableSize() - 1)));
    }

    public Integer getChosenRotorId(){

        return rotorIdChoiceBox.getValue();
    }

    public Character getChosenInitialPosition(){

        return currentButton.getText() != null ? currentButton.getText().charAt(0) : null;
    }
}