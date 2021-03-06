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


public class UpdateRawMaterialsController implements Initializable {
    @FXML
    private TextField rawMaterialsUnitPrice;
    @FXML
    private TextField rawMaterialsQuantity;
    @FXML
    private TextArea rawMaterialsDescription;
    @FXML
    private TextField rawMaterialsName;
    @FXML
    private TextField rawMaterialId;

    @FXML
    private ComboBox<String> rawMaterialsList;

    @FXML
    private Button cancel;

    @FXML
    private Button update;

    @FXML
    private void update() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcController.url);
        PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.UPDATE_QUERY_RAW);
        preparedStatement.setInt(1,Integer.parseInt(rawMaterialId.getText()));
        preparedStatement.setString(2,rawMaterialsName.getText());
        preparedStatement.setDouble(3,Double.parseDouble(rawMaterialsUnitPrice.getText()));
        preparedStatement.setInt(4,Integer.parseInt(rawMaterialsQuantity.getText()));
        preparedStatement.setString(5,rawMaterialsDescription.getText());
        preparedStatement.setString(6,rawMaterialsList.getValue());
        preparedStatement.executeUpdate();
            Controller.infoBox("Successfully updated "+rawMaterialsList.getValue(),null,"Success");

//        if (resultSet==1){
//        }else {
//            Controller.infoBox("Failed to update "+rawMaterialsList.getValue(),null,"Failed!");
//        }
    }

    @FXML
    private void viewRawMaterial(){
        try{
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_RAW_NAME);
            preparedStatement.setString(1,rawMaterialsList.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                rawMaterialId.setText(resultSet.getString(1));
                rawMaterialsName.setText(resultSet.getString(2));
                rawMaterialsUnitPrice.setText(resultSet.getString(3));
                rawMaterialsQuantity.setText(resultSet.getString(4));
                rawMaterialsDescription.setText(resultSet.getString(5));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }

    }

    @FXML
    private void goBack() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new rawMaterials().start(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_RAW);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                rawMaterialsList.getItems().add(resultSet.getString("rawMaterialName"));
            }
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }
    }
