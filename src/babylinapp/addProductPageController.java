package babylinapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class addProductPageController implements Initializable {
    @FXML
    Button add;

    @FXML
    Button back;

    @FXML
    ComboBox productList;

    @FXML
    javafx.scene.control.TextField addProductQuantity;


    public void increaseQuantity() {
        int val=0;
        try {
           val= Integer.parseInt(addProductQuantity.getText());
        }catch (NumberFormatException r){
            Controller.showAlert(Alert.AlertType.ERROR,add.getScene().getWindow(),"Error",r.getMessage()+"\n"+"Enter a number");
            return;
        }

        try {
            Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.UPDATE_QUERY_PRODUCTS_QUANTITY);
            preparedStatement.setString(2, (String) productList.getValue());
            preparedStatement.setInt(1, val);
            boolean resultSet = preparedStatement.execute();
//        System.out.println(resultSet);
            if (!resultSet) {
                Controller.infoBox("Added more " + productList.getValue(), null, "Success!");
            } else {
                Controller.infoBox("Could not add more of " + productList.getValue(), null, "Failed!");
            }
        } catch (SQLException e) {
           jdbcController.printSQLException(e);
           return;
        }
    }

    @FXML
    private void goBack(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        new products().start(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_PRODUCTS_PRODUCT_NAME);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                productList.getItems().add(resultSet.getString("ProductName"));
            }


        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }


        try {
            Objects.requireNonNull(connection).close();
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }
}
