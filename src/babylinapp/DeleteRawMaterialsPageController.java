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
    protected ComboBox<String> rawMaterialCombo;

    @FXML
    protected Button delete;

    @FXML
    protected Button cancel;

    @FXML
    protected void deleteStock(){
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
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
    protected void cancel() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new Menu().start(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
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
