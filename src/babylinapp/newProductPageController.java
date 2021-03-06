package babylinapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class newProductPageController {
    //Product new Page Controller(newProductPage.fxml)

    @FXML
    private TextField newProductNameProductPage;//Product Name

    @FXML
    private TextField newProductPriceProductPage;//Product Price

    @FXML
    private TextField newProductQuantityProductPage;//Product Quantity

    @FXML
    private TextArea newProductDescProductPage; //Product Description

    @FXML
    private Button cancel;

    private boolean check() throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(jdbcController.url);
                PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_PRODUCTS_ADD)
        ) {
            preparedStatement.setString(1, newProductNameProductPage.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        }
    }

    @FXML
    protected void newProduct() throws SQLException {
        if(newProductNameProductPage.getText().equals("")||newProductPriceProductPage.getText().equals("")||newProductQuantityProductPage.getText().equals("")||newProductDescProductPage.getText().equals("")){
            Controller.infoBox("Empty field. Enter a value",null,"Error");
            return;
        }
        boolean checked = check();
        if (!checked) {
            try (
                    Connection connection = DriverManager.getConnection(jdbcController.url);
                    PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.INSERT_QUERY_PRODUCTS)
            ) {
                preparedStatement.setString(1, null);
                preparedStatement.setString(2, newProductNameProductPage.getText());
                preparedStatement.setString(3, newProductPriceProductPage.getText());
                preparedStatement.setString(4, newProductQuantityProductPage.getText());
                preparedStatement.setString(5, newProductDescProductPage.getText());

                boolean resultSet = preparedStatement.execute();
                if (!resultSet) {
                    Controller.infoBox("Added new Product " + newProductNameProductPage.getText(), null, "Success");
                } else {
                    Controller.infoBox("Could not add new product " + newProductNameProductPage.getText() + ".", null, "Failed");
                }
            }catch (SQLException e){
                jdbcController.printSQLException(e);
            }
        } else {
            Controller.infoBox("Product exits already!", null, "Failed");
        }
    }

    @FXML
    private void goBack() throws IOException {
        Stage stage= (Stage) cancel.getScene().getWindow();
        new products().start(stage);
    }
}
