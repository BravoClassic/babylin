package babylinapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddRawMaterialsPageController {

    @FXML
    protected Button addRawMaterialsBtn;

    @FXML
    protected Button cancel;

    @FXML
    protected Spinner addRawMaterialsQuantity;

    @FXML
    protected ComboBox addRawMaterialsComboBox;

    @FXML
    protected void addRawMaterials(){
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.UPDATE_QUERY_RAW_MATERIALS_QUANTITY);
            preparedStatement.setInt(1, (Integer) addRawMaterialsQuantity.getValue());
            preparedStatement.setString(2, (String) addRawMaterialsComboBox.getValue());

            boolean resultSet = preparedStatement.execute();

            if (resultSet){
                Controller.infoBox("Added "+addRawMaterialsQuantity.getValue()+" raw material has been added successfully!",null,"Success!");
            }else {
                Controller.infoBox("Added "+addRawMaterialsQuantity.getValue()+" raw material has not been added successfully!",null, "Failed!");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void  goBack() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new rawMaterials().start(stage);
    }
}
