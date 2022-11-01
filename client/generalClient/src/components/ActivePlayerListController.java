package components;

import constants.Constants;
import contestDtos.ActivePlayerDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import util.RefresherController;
import util.tableHolderInterfaces.ActiveTableHolder;
import util.tableHolderInterfaces.AgentTableHolder;
import util.tableHolderInterfaces.Disconnectable;
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
        //usersTable.getSelectionModel().selectedIndexProperty.bind(chooseable);
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

        usersTable.getItems().clear();
    }


    public String getSelectedUserName(){

        return usersTable.getSelectionModel().getSelectedItem() != null ?
                usersTable.getSelectionModel().getSelectedItem().getName() :
                null;
    }

    @Override
    public void updateList(String jsonUserList) {

        if(jsonUserList == null && parentController instanceof Disconnectable){
            ((Disconnectable) parentController).disconnectFromContest();
            return;
        }

        List<ActivePlayerDTO> usersList = Arrays.asList(Constants.GSON_INSTANCE.fromJson(jsonUserList,ActivePlayerDTO[].class));
        String nameOfChosen = getSelectedUserName();
        System.out.println("in useer list update  : " +nameOfChosen);
        Optional<ActivePlayerDTO> chosenAllie = nameOfChosen != null ?
                    usersList.stream().filter(allie -> allie.getName().equals(nameOfChosen)).findFirst()
                    : Optional.empty();
        System.out.println("in useer list update  : " +chosenAllie.isPresent());

        clearComponent();
        usersList.forEach(this::addTeam);

        if(nameOfChosen != null && chosenAllie.isPresent()){
            usersTable.getSelectionModel().select(chosenAllie.get());
        }
    }
}
