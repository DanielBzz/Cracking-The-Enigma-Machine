package components.main;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public abstract class FileLoadable {

    private final SimpleStringProperty selectedFileProperty = new SimpleStringProperty("-");
    private final SimpleBooleanProperty isGoodFileSelected = new SimpleBooleanProperty(false);

    public SimpleStringProperty selectedFileProperty() {

        return this.selectedFileProperty;
    }

    public SimpleBooleanProperty isGoodFileSelectedProperty() {

        return this.isGoodFileSelected;
    }

    public void setSelectedFile(String selectedFilePath){

        selectedFileProperty.set(selectedFilePath);
    }

    public void setIsGoodFileSelected(Boolean isGood) {

        isGoodFileSelected.set(isGood);
    }

    public abstract void initialFileSelectedEvents();

    public void initialFileLoadable(){

        selectedFileProperty.set("-");
        isGoodFileSelected.set(false);
    }
}
