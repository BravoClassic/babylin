package babylinapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AddStockPageController {

    @FXML
    protected Button addStockBtn;

    @FXML
    protected TextField addStockQuantity;

    @FXML
    protected ComboBox addStockComboBox;

    @FXML
    protected void addStock(){
        System.out.println("Hello!");
    }

    @FXML
    protected void  cancel(){
        System.out.println("Hello!");
    }
}
