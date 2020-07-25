package babylinapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

    @FXML
    protected void update() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
        PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.UPDATE_QUERY_STOCK);
        boolean resultSet = preparedStatement.execute();

        if (resultSet){
            Controller.infoBox("Successfully updated "+productList.getValue(),null,"Success");
        }else {
            Controller.infoBox("Failed to update "+productList.getValue(),null,"Failed!");
        }
    }


    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new stocks().start(stage);
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
