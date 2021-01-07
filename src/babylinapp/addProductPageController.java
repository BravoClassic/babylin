package babylinapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class addProductPageController implements Initializable {
    @FXML
    private Button add;

    @FXML
    private Button back;

    @FXML
    private ComboBox<String> productList;

    @FXML
    private TextField addProductQuantity;

    @FXML
    private void increaseQuantity() {
        int val;
        try {
           val= Integer.parseInt(addProductQuantity.getText());
           if (productList.getValue()==null){
               Controller.infoBox("Select a product",null,"Error Select Product");
               return;
           }
           if (val<0) {
               Controller.infoBox("Enter a positive integer.", null, "Error Integer");
               return;
           }
        }catch (NumberFormatException r){
            Controller.showAlert(Alert.AlertType.ERROR,add.getScene().getWindow(),"Error",r.getMessage()+"\n"+"Enter a number");
            return;
        }

        try {
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.UPDATE_QUERY_PRODUCTS_QUANTITY);
            preparedStatement.setString(2, productList.getValue());
            preparedStatement.setInt(1, val);
            boolean resultSet = preparedStatement.execute();
            if (!resultSet) {
                Controller.infoBox("Added more " + productList.getValue(), null, "Success!");
            } else {
                Controller.infoBox("Could not add more of " + productList.getValue(), null, "Failed!");
            }
        } catch (SQLException e) {
           jdbcController.printSQLException(e);
        }
    }

    @FXML
    private void goBack() throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        new products().start(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_PRODUCTS_PRODUCT_NAME);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                productList.getItems().add(resultSet.getString("ProductName"));
            }


        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }
}
