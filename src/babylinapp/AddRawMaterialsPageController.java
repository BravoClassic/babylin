package babylinapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AddRawMaterialsPageController implements Initializable {

    @FXML
    protected Button addRawMaterialsBtn;

    @FXML
    protected Button cancel;

    @FXML
    protected Spinner<Integer> addRawMaterialsQuantity;

    @FXML
    protected ComboBox<String> addRawMaterialsComboBox;


    @FXML
    protected void addRawMaterials(){
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.UPDATE_QUERY_RAW_MATERIALS_QUANTITY);
            preparedStatement.setInt(1, addRawMaterialsQuantity.getValue());
            preparedStatement.setString(2, addRawMaterialsComboBox.getValue());

            boolean resultSet = preparedStatement.execute();

            if (!resultSet){
                Controller.infoBox("Added "+addRawMaterialsQuantity.getValue()+" raw material has been added successfully!",null,"Success!");
            }else {
                Controller.infoBox("Added "+addRawMaterialsQuantity.getValue()+" raw material has not been added successfully!",null, "Failed!");
            }
        }catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }

    @FXML
    protected void  goBack() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new rawMaterials().start(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                addRawMaterialsComboBox.getItems().add(resultSet.getString("StockName"));
            }
        }catch (SQLException e) {
            jdbcController.printSQLException(e);
        }

    }
}

