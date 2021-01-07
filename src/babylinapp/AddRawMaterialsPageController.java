package babylinapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.util.ResourceBundle;

public class AddRawMaterialsPageController implements Initializable {

    @FXML
    private Button addRawMaterialsBtn;

    @FXML
    private Button cancel;

    @FXML
    private Spinner<Integer> addRawMaterialsQuantity;

    @FXML
    private ComboBox<String> addRawMaterialsComboBox;

    private ObservableList<Double> rawMaterialsPrices = FXCollections.observableArrayList();



    @FXML
    private void addRawMaterials(){
        if (addRawMaterialsQuantity.getValue() == null||addRawMaterialsComboBox.getValue().equals("")){
            Controller.infoBox("Empty fields! Enter some values!",null,"Error");
            return;
        }
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.UPDATE_QUERY_RAW_MATERIALS_QUANTITY);
            preparedStatement.setInt(1, addRawMaterialsQuantity.getValue());
            preparedStatement.setString(2, addRawMaterialsComboBox.getValue());

            boolean resultSet = preparedStatement.execute();

            if (!resultSet){
                Controller.infoBox("Added "+addRawMaterialsQuantity.getValue()+" raw material has been added successfully!",null,"Success!");
                insertCost();
            }else {
                Controller.infoBox("Added "+addRawMaterialsQuantity.getValue()+" raw material has not been added successfully!",null, "Failed!");
            }
        }catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }

    private void insertCost(){
        try{
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO babylinapp_cost VALUES(?,?,?)");
            preparedStatement.setString(1, null);
            preparedStatement.setDouble(2,-(rawMaterialsPrices.get(addRawMaterialsComboBox.getSelectionModel().getSelectedIndex())*addRawMaterialsQuantity.getValue()));
            preparedStatement.setDate(3, (Date) Date.from(Instant.now()));

            boolean res = preparedStatement.execute();
            if (!res)
                System.out.println("Worked");
            else
                System.out.println("Not working");

        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }

    @FXML
    private void  goBack() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new rawMaterials().start(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addRawMaterialsQuantity.setValueFactory((new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100)));
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_RAW);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                addRawMaterialsComboBox.getItems().add(resultSet.getString("rawMaterialName"));
                rawMaterialsPrices.add(resultSet.getDouble("unitPrice"));
            }
        }catch (SQLException e) {
            jdbcController.printSQLException(e);
        }

    }
}

