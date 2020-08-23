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
    public TextField rawMaterialsUnitPrice;
    public TextField rawMaterialsQuantity;
    public TextArea rawMaterialsDescription;
    public TextField rawMaterialsName;
    public TextField rawMaterialId;

    @FXML
    private ComboBox<String> rawMaterialsList;

    @FXML
    protected Button cancel;

    @FXML
    protected Button update;

    @FXML
    protected void update() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
        PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.UPDATE_QUERY_RAW);
        preparedStatement.setInt(1,Integer.parseInt(rawMaterialId.getText()));
        preparedStatement.setString(2,rawMaterialsName.getText());
        preparedStatement.setDouble(3,Double.parseDouble(rawMaterialsUnitPrice.getText()));
        preparedStatement.setInt(4,Integer.parseInt(rawMaterialsQuantity.getText()));
        preparedStatement.setString(5,rawMaterialsDescription.getText());
        preparedStatement.setString(6,rawMaterialsList.getValue());
        int resultSet = preparedStatement.executeUpdate();

        if (resultSet==1){
            Controller.infoBox("Successfully updated "+rawMaterialsList.getValue(),null,"Success");
        }else {
            Controller.infoBox("Failed to update "+rawMaterialsList.getValue(),null,"Failed!");
        }
    }

    @FXML
    protected void viewRawMaterial(){
        try{
            Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
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
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new rawMaterials().start(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
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
