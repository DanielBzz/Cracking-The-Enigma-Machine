package components.main;

import javafx.beans.property.SimpleStringProperty;

public interface FileLoadable {
    SimpleStringProperty selectedFileProperty();
    void setSelectedFile(String selectedFilePath);
}
