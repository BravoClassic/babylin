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
    private Button deletebtn;

    @FXML
    private Button cancel;

    @FXML
    private ComboBox<String> productList;


    @FXML
    private void delete()  {
        if (productList.getValue().equals("")){
            Controller.infoBox("Select a product to delete!",null,"Error");
        }
        try{
            int selectedIndexCombo = productList.getSelectionModel().getSelectedIndex();
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.DELETE_QUERY_PRODUCTS_DELETE);
            preparedStatement.setString(1, productList.getValue());
            boolean resultSet = preparedStatement.execute();
            if (resultSet) {
                Controller.infoBox("Product not deleted successfully!",null,"Failed");
            } else {
                Controller.infoBox("Product deleted successfully!",null,"Success");
                productList.getItems().remove(selectedIndexCombo);
            }
            }catch (SQLException e){
                jdbcController.printSQLException(e);
            }
    }

    @FXML
    public void back() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new products().start(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try (
                Connection connection = DriverManager.getConnection(jdbcController.url);
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
