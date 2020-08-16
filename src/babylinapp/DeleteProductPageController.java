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


public class DeleteProductPageController implements Initializable {

    @FXML
    Button deletebtn;

    @FXML
    Button cancel;

    @FXML
    ComboBox<String> productList;


    @FXML
    protected void delete() throws SQLException {
        int selectedIndexCombo = productList.getSelectionModel().getSelectedIndex();
        Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
        PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.DELETE_QUERY_PRODUCTS_DELETE);
        preparedStatement.setString(1, productList.getValue());
        boolean resultSet = preparedStatement.execute();
        if (resultSet) {
            Controller.infoBox("Product not deleted successfully!",null,"Failed");
        } else {
            Controller.infoBox("Product deleted successfully!",null,"Success");
            productList.getItems().remove(selectedIndexCombo);
        }
    }

    @FXML
    protected void back() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new products().start(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try (
                Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
                PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_PRODUCTS_PRODUCT_NAME)
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                productList.getItems().add(resultSet.getString("ProductName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
