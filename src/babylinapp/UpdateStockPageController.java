package babylinapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class UpdateStockPageController {

    @FXML
    protected Button cancel;

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new stocks().start(stage);
    }
}
