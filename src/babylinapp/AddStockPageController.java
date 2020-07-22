package babylinapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddStockPageController {

    @FXML
    protected Button addStockBtn;

    @FXML
    protected Button cancel;

    @FXML
    protected TextField addStockQuantity;

    @FXML
    protected ComboBox addStockComboBox;

    @FXML
    protected void addStock(){
        System.out.println("Hello!");
    }

    @FXML
    protected void  goBack() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new stocks().start(stage);
    }
}
