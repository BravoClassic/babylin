package babylinapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class menuController implements Initializable {

    public Button appointment;
    @FXML
    private Label welcome;

    @FXML
    private Button settings;

    @FXML
    private Button productsButton;

    @FXML
    private  Button ordersButton;

    @FXML
    private Button rawMaterialsButton;

    @FXML
    private Button reportsButton;

    @FXML
    private Button logOutButton;


    public void displayOrders() throws IOException{
        Stage stage= (Stage) ordersButton.getScene().getWindow();
        new orders().start(stage);
    }

    public void displayStocks() throws IOException{
        Stage stage = (Stage) rawMaterialsButton.getScene().getWindow();
        new rawMaterials().start(stage);
    }

    public void displayReports() throws IOException{
        Stage stage = (Stage) reportsButton.getScene().getWindow();
        new reports().start(stage);
    }

    public void displayProduct() throws IOException {
        Stage stage = (Stage) productsButton.getScene().getWindow();
        new products().start(stage);
    }

    public void logOut() throws Exception {
        Stage stage=(Stage) logOutButton.getScene().getWindow();
        new Main().start(stage);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            Connection connection= DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
            if(jdbcController.userType.equals("Customer")) {
                PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_CUST_EMAIL);
                preparedStatement.setString(1,jdbcController.emailUniversal);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    welcome.setText("Welcome, " +resultSet.getString("userName")+"!");
                    reportsButton.setDisable(true);
                    reportsButton.setVisible(false);
                    rawMaterialsButton.setDisable(true);
                    rawMaterialsButton.setVisible(false);
                    productsButton.setDisable(true);
                    productsButton.setVisible(false);
                }
            }
            if(jdbcController.userType.equals("Employee")) {
                PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_EMAIL);
                preparedStatement.setString(1,jdbcController.emailUniversal);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    welcome.setText("Welcome, " +resultSet.getString("firstName")+"!");
                }
            }
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }


}
