package components;

import constants.Constants;
import contestDtos.ActivePlayerDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import util.tableHolderInterfaces.ActiveTableHolder;
import util.RefresherController;
import util.tableHolderInterfaces.AgentTableHolder;
import util.tableHolderInterfaces.TeamTableHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ActivePlayerListController extends RefresherController {

    private ActiveTableHolder parentController;
    @FXML private TableView<ActivePlayerDTO> usersTable;
    BooleanProperty chooseable = new SimpleBooleanProperty(false);

    @FXML
    public void initialize(){
        usersTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("name"));
        usersTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("amount"));
        usersTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("size"));
        usersTable.getSelectionModel().cellSelectionEnabledProperty().bind(chooseable);
    }

    public void setTableHolder(ActiveTableHolder holder){
        if(holder instanceof TeamTableHolder){
            usersTable.getColumns().get(0).setText("Allie name");
            usersTable.getColumns().get(1).setText("Amount of agents");
        } else if (holder instanceof AgentTableHolder) {
            usersTable.getColumns().get(0).setText("Agent name");
            usersTable.getColumns().get(1).setText("Amount of threads");
        }

        parentController = holder;
    }

    public void setChooseable(boolean b){
        chooseable.set(b);
    }

    public void addTeam(ActivePlayerDTO newTeam){

        usersTable.getItems().add(newTeam);
    }

    public void clearComponent(){

        if(usersTable !=null){
            usersTable.getItems().clear();
        }
        stopListRefresher();
    }


    public String getSelectedAlliesName(){

        return usersTable.getSelectionModel().getSelectedItem() != null ?
                usersTable.getSelectionModel().getSelectedItem().getName() :
                null;
    }

    @Override
    public void updateList(String jsonUserList) {

        List<ActivePlayerDTO> usersList = Arrays.asList(Constants.GSON_INSTANCE.fromJson(jsonUserList,ActivePlayerDTO[].class));
        Optional<ActivePlayerDTO> chosenAllie = null;

        if (chooseable.get()) {
            String nameOfChosen = getSelectedAlliesName();
            chosenAllie = nameOfChosen != null ?
                    usersList.stream().filter(allie -> allie.getName().equals(nameOfChosen)).findFirst()
                    : Optional.empty();

        }

        Platform.runLater(()->{
            clearComponent();
            usersList.forEach(this::addTeam);
        });

        usersTable.setItems(FXCollections.observableList(usersList));
        if(chooseable.get() && chosenAllie != null && chosenAllie.isPresent()){
            usersTable.getSelectionModel().select(chosenAllie.get());
        }
    }
}
