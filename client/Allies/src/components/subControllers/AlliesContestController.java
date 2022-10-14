package components.subControllers;

import components.body.details.DecryptionManagerController;
import components.main.AlliesMainAppController;
import components.main.UBoatMainAppController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class AlliesContestController {
    private AlliesMainAppController parentController;
    @FXML
    private GridPane alliesDecryptionProgressAndCandidatesComponent;
    @FXML
    private DecryptionManagerController alliesDecryptionProgressAndCandidatesController;
    @FXML
    private BorderPane contestDataComponent;
    @FXML
    private ContestDetailsController contestDataController;

    public void setAlliesMainAppController(AlliesMainAppController alliesMainAppController) {
        this.parentController = alliesMainAppController;
    }
}
