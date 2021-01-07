package babylinapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.Instant;

public class NewRawMaterialsPageController {

    @FXML
    private Button newBtnRawMaterials;

    @FXML
    private TextField newRawMaterialNamePage;

    @FXML
    private TextField newRawMaterialsPricePage;

    @FXML
    private TextField newRawMaterialsQuantityPage;

    @FXML
    private TextArea newRawMaterialsDescPage;

    @FXML
    private Button cancel;

    private ObservableList<Integer> rawMaterialsPrices = FXCollections.observableArrayList();


    private boolean check()  {
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_PRODUCTS_ADD);
            preparedStatement.setString(1, newRawMaterialNamePage.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        }catch (SQLException e){
            jdbcController.printSQLException(e);
        }
        return false;
    }


    @FXML
    private void newRawMaterials() {
        if (newRawMaterialNamePage.getText().equals("")||newRawMaterialsPricePage.getText().equals("")||newRawMaterialsQuantityPage.getText().equals("")||newRawMaterialsDescPage.getText().equals("")){
            Controller.infoBox("Empty fields!",null,"Error");
            return;
        }
        boolean checked = check();
        if (!checked) {
            try {
                Connection connection = DriverManager.getConnection(jdbcController.url);
                PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.INSERT_QUERY_STOCK);
                preparedStatement.setString(1, null);
                preparedStatement.setString(2, newRawMaterialNamePage.getText());
                preparedStatement.setDouble(3, Double.parseDouble(newRawMaterialsPricePage.getText()));
                preparedStatement.setInt(4, Integer.parseInt(newRawMaterialsQuantityPage.getText()));
                preparedStatement.setString(5, newRawMaterialsDescPage.getText());

                boolean resultSet = preparedStatement.execute();

                if (!resultSet) {
                    Controller.infoBox("New raw material has been added successfully!", null, "Success!");
                    insertCost();
                } else {
                    Controller.infoBox("New raw materials has not been added successfully!", null, "Failed!");
                }
            } catch (SQLException e) {
                jdbcController.printSQLException(e);
            }
        }else {
            Controller.infoBox("Product Exists already!",null,"Error");
        }
    }

    private void insertCost(){
        try{
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO babylinapp_cost VALUES(?,?,?)");
            preparedStatement.setString(1, null);
            preparedStatement.setDouble(2,-(Double.parseDouble(newRawMaterialsQuantityPage.getText())*Double.parseDouble(newRawMaterialsPricePage.getText())));
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
    private void goBack() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new rawMaterials().start(stage);
    }
}
