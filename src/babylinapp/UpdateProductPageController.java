package babylinapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UpdateProductPageController implements Initializable {
    @FXML
    private TextField productName;
    @FXML
    private TextField productQuantity;
    @FXML
    private TextField productUnitPrice;
    @FXML
    private TextArea productDescription;
    @FXML
    private ComboBox<String> productList;
    @FXML
    private Button cancel;
    @FXML
    private Button update;


    @FXML
    private void update()  {
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.UPDATE_QUERY_PRODUCTS_ALL);
            preparedStatement.setString(1, productName.getText());
            preparedStatement.setDouble(2, Double.parseDouble(productUnitPrice.getText()));
            preparedStatement.setInt(3, Integer.parseInt(productQuantity.getText()));
            preparedStatement.setString(4, productDescription.getText());
            preparedStatement.setString(5, productList.getValue());
            preparedStatement.executeUpdate();
                Controller.infoBox("Product Updated!",null,"Success");
            preparedStatement.close();
            connection.close();
        }catch(SQLException e){
            Controller.errBox("Err Code:"+e.getErrorCode()+"\n Message:"+e.getMessage()+"\n"+e.getCause(),"Error with Update Function","Error");
            jdbcController.printSQLException(e);
        }
    }

    @FXML
    private void display(){

        try {
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_PRODUCTS_ADD);
            preparedStatement.setString(1,productList.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                productName.setText(productList.getValue());
                productQuantity.setText(Integer.toString(resultSet.getInt("quantity")));
                productUnitPrice.setText(Double.toString(resultSet.getDouble("unitPrice")));
                productDescription.setText(resultSet.getString("productDescription"));
            }
            preparedStatement.close();
            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @FXML
    private void goBack() throws IOException {
        productName.setText("");
        productQuantity.setText("");
        productUnitPrice.setText("");
        productDescription.setText("");
        Stage stage = (Stage) cancel.getScene().getWindow();
        new products().start(stage);
    }
    @FXML
    private void viewProduct() throws SQLException {

        Connection connection = DriverManager.getConnection(jdbcController.url);
        PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_PRODUCTS);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String result = resultSet.getString("productName");
//            Integer result1=resultSet.getInt("quantity");
//            Double result2= resultSet.getDouble("unitPrice");
//            String result3 = resultSet.getString("productDescription");
            productList.getItems().add(result);
        }
        preparedStatement.close();
        resultSet.close();
        connection.close();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            viewProduct();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
