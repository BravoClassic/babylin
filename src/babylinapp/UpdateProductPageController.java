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
    public TextField productName;
    public TextField productQuantity;
    public TextField productUnitPrice;
    public TextArea productDescription;
    @FXML
    protected ComboBox<String> productList;

    @FXML
    protected Button cancel;

    @FXML
    protected Button update;

//    private SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 0);


    @FXML
    protected void update() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
        PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.UPDATE_QUERY_PRODUCTS_ALL);
        preparedStatement.setString(1,null);
        preparedStatement.setString(2,productName.getText());
        preparedStatement.setInt(3,Integer.parseInt(productUnitPrice.getText()));
        preparedStatement.setInt(4,Integer.parseInt(productQuantity.getText()));
        preparedStatement.setString(5,productDescription.getText());
        preparedStatement.setString(6,productList.getValue());
        int resultSet = preparedStatement.executeUpdate();

        if (resultSet==1){
            Controller.infoBox("Successfully updated "+productList.getValue(),null,"Success");
        }else {
            Controller.infoBox("Failed to update "+productList.getValue(),null,"Failed!");
        }
    }

    @FXML
    protected void display(){

        try {
            Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_PRODUCTS_ADD);
            preparedStatement.setString(1,productList.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                productName.setText(productList.getValue());
                productQuantity.setText(Integer.toString(resultSet.getInt("quantity")));
                productUnitPrice.setText(Integer.toString(resultSet.getInt("unitPrice")));
                productDescription.setText(resultSet.getString("productDescription"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @FXML
    protected void goBack() throws IOException {
        productName.setText("");
        productQuantity.setText("");
        productUnitPrice.setText("");
        productDescription.setText("");
        Stage stage = (Stage) cancel.getScene().getWindow();
        new products().start(stage);
    }
    @FXML
    protected void viewProduct() throws SQLException {

        Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
        PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_PRODUCTS);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String result = resultSet.getString("productName");
//            Integer result1=resultSet.getInt("quantity");
//            Double result2= resultSet.getDouble("unitPrice");
//            String result3 = resultSet.getString("productDescription");
            productList.getItems().add(result);
        }
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
