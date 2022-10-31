package components;

import constants.Constants;
import contestDtos.ActivePlayerDTO;
import contestDtos.ContestDetailsDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import util.RefresherController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AlliesListController extends RefresherController {

    @FXML private TableView<ActivePlayerDTO> alliesTable;
    BooleanProperty chooseable = new SimpleBooleanProperty(false);

    @FXML
    public void initialize(){
        alliesTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("name"));
        alliesTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("amount"));
        alliesTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("size"));
        alliesTable.editableProperty().bind(chooseable);
    }

    public void setChooseable(boolean b){
        chooseable.set(b);
    }

    public void addTeam(ActivePlayerDTO newTeam){

        alliesTable.getItems().add(newTeam);
    }

    public void clearComponent(){

        if(alliesTable!=null){
            alliesTable.getItems().clear();
        }
        stopListRefresher();
    }


    public String getSelectedAlliesName(){

        return alliesTable.getSelectionModel().getSelectedItem() != null ?
                alliesTable.getSelectionModel().getSelectedItem().getName() :
                null;
    }

    @Override
    public void updateList(String jsonUserList) {

//        List<ActivePlayerDTO> usersList = Arrays.asList(Constants.GSON_INSTANCE.fromJson(jsonUserList,ActivePlayerDTO[].class));
//        Optional<ActivePlayerDTO> chosenAllie = null;
//
//        if (chooseable.get()) {
//            String nameOfChosen = getSelectedAlliesName();
//            chosenAllie = nameOfChosen != null ?
//                    usersList.stream().filter(allie -> allie.getName().equals(nameOfChosen)).findFirst()
//                    : Optional.empty();
//
//        }
//
////        Platform.runLater(()->{
////            clearComponent();
////            usersList.forEach(this::addTeam);
////        });
//
//        alliesTable.setItems(FXCollections.observableList(usersList));
//        if(/*chooseable.get() && */chosenAllie != null && chosenAllie.isPresent()){
//            alliesTable.getSelectionModel().select(chosenAllie.get());
//        }

        List<ActivePlayerDTO> usersList = Arrays.asList(Constants.GSON_INSTANCE.fromJson(jsonUserList,ActivePlayerDTO[].class));
        String nameOfChosen = getSelectedAlliesName();
        Optional<ActivePlayerDTO> chosenAlly = nameOfChosen != null ?
                usersList.stream().filter(ally -> ally.getName().equals(nameOfChosen)).findFirst()
                : Optional.empty();

        alliesTable.setItems(FXCollections.observableList(usersList));
        if(nameOfChosen!= null && chosenAlly.isPresent()){
            alliesTable.getSelectionModel().select(chosenAlly.get());
        }

    }
}
