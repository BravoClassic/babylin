package babylinapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class DeleteRawMaterialsPageController implements Initializable {

    @FXML
    private ComboBox<String> rawMaterialCombo;

    @FXML
    private Button delete;

    @FXML
    private Button cancel;

    @FXML
    private void deleteStock(){
        if (rawMaterialCombo.getValue().equals("")){
            Controller.infoBox("Empty field!",null,"Error");
            return;
        }
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.DELETE_QUERY_RAW_DELETE);
            preparedStatement.setString(1,rawMaterialCombo.getValue());
            int resultSet = preparedStatement.executeUpdate();

            if (resultSet==1){
                rawMaterialCombo.getItems().remove(rawMaterialCombo.getSelectionModel().getSelectedIndex());
                Controller.infoBox("Raw material has been deleted successfully!",null,"Success!");
            }else {
                Controller.infoBox("Raw materials has not been deleted successfully!",null, "Failed!");
            }
        }catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }

    @FXML
    private void cancel() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new rawMaterials().start(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_RAW);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                rawMaterialCombo.getItems().add(resultSet.getString("rawMaterialName"));
            }
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }
}
