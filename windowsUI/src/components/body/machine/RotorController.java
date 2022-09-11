package components.body.machine;

import components.Rotor;
import components.body.details.CodeCalibrationController;
import components.body.details.RotorParent;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;

import java.util.List;

public class RotorController {

    RotorParent parentController;

    @FXML
    private ChoiceBox<Integer> rotorIdChoiceBox;

    @FXML
    private Button previousButton;

    @FXML
    private Button currentButton;

    @FXML
    private Button nextButton;
    private Rotor currentRotor;

    @FXML
    public void initialize(){}

    @FXML
    void currentButtonActionListener(ActionEvent event) {

        if(!(this.parentController instanceof CodeCalibrationController)){

            new Alert(Alert.AlertType.INFORMATION, "should show all the rotor", ButtonType.OK).show();
        }
    }

    @FXML
    void nextButtonActionListener(ActionEvent event) {

        if(this.parentController instanceof CodeCalibrationController && !nextButton.getText().equals("")) {
            previousButton.setText(String.valueOf(currentButton.getText()));
            currentButton.setText(String.valueOf(nextButton.getText()));
            nextButton.setText(String.valueOf(currentRotor.getCharInPosition(
                    (currentRotor.getPositionOfChar(nextButton.getText().charAt(0)) + 1) % currentRotor.getConversionTableSize())));
        }
    }

    @FXML
    void previousButtonActionListener(ActionEvent event) {

        if(this.parentController instanceof CodeCalibrationController && !previousButton.getText().equals("")) {
            nextButton.setText(String.valueOf(currentButton.getText()));
            currentButton.setText(String.valueOf(previousButton.getText()));
            previousButton.setText(String.valueOf(currentRotor.getCharInPosition(
                    (currentRotor.getPositionOfChar(previousButton.getText().charAt(0)) - 1 + currentRotor.getConversionTableSize()) % currentRotor.getConversionTableSize())));
        }
    }

    public void setParentController(RotorParent controller) {

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

    public void setRotorIdChoiceBox(int rotorId){

        rotorIdChoiceBox.getItems().add(rotorId);
        rotorIdChoiceBox.setValue(rotorId);
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
