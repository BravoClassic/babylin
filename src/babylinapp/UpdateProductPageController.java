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
        PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.UPDATE_QUERY_PRODUCTS_QUANTITY);
        preparedStatement.setString(1,productList.getValue());
        boolean resultSet = preparedStatement.execute();

        if (resultSet){
            Controller.infoBox("Successfully updated "+productList.getValue(),null,"Success");
        }else {
            Controller.infoBox("Failed to update "+productList.getValue(),null,"Failed!");
        }
    }

    @FXML
    protected void display(){
        String pN = productList.getValue();//Product Name
        int pQ=0; //Get Quantity
        int pP=0;//Get unit price
        String pD="";//Get product Description

        try {
            Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_PRODUCTS_ADD);
            preparedStatement.setString(1,pN);
            ResultSet resultSet = preparedStatement.executeQuery();
            pQ=resultSet.getInt("quantity");
            pP=resultSet.getInt("unitPrice");
            pD=resultSet.getString("productDescription");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        productName.setText(pN);
        productQuantity.setText(Integer.toString(pQ));
        productUnitPrice.setText(Integer.toString(pP));
        productDescription.setText(pD);
    }


    @FXML
    protected void goBack() throws IOException {
        productName.setText("");
        productQuantity.setText("");
        productUnitPrice.setText("");
        productDescription.setText("");
        Stage stage = (Stage) cancel.getScene().getWindow();
        new rawMaterials().start(stage);
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
