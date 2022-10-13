package components.main;

import javafx.beans.property.SimpleStringProperty;

public abstract class FileLoadable {

    private final SimpleStringProperty selectedFileProperty = new SimpleStringProperty("-");

    public SimpleStringProperty selectedFileProperty() {

        return this.selectedFileProperty;
    }

    public void setSelectedFile(String selectedFilePath){

        selectedFileProperty.set(selectedFilePath);
    }
}
