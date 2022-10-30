package components;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class DynamicComponent {
    public static void fitToPane(Node subComponent){
        AnchorPane.setTopAnchor(subComponent, 0.0);
        AnchorPane.setRightAnchor(subComponent, 0.0);
        AnchorPane.setLeftAnchor(subComponent, 0.0);
        AnchorPane.setBottomAnchor(subComponent, 0.0);
    }
}
