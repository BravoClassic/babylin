package babylinapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class DeleteProductPageController {

    @FXML
    Button deletebtn;

    @FXML
    Button cancel;

    @FXML
    ComboBox productList;


    @FXML
    protected void delete() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
        PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.DELETE_QUERY_PRODUCTS_DELETE);
        preparedStatement.setString(0,(String) productList.getValue());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.getBoolean(0)){
            Controller.infoBox("Product deleted successfully!",null,"Success");
        }else {
            Controller.infoBox("Product not deleted successfully!",null,"Failed");
        }
    }

    @FXML
    private void back() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new products().start(stage);
    }

}
