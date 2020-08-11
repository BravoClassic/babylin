package babylinapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewRawMaterialsPageController {

    @FXML
    protected Button newBtnRawMaterials;

    @FXML
    protected TextField newRawMaterialNamePage;

    @FXML
    protected TextField newRawMaterialsPricePage;

    @FXML
    protected TextField newRawMaterialsQuantityPage;

    @FXML
    protected TextArea newRawMaterialsDescPage;

    @FXML
    protected Button cancel;

    public void newRawMaterials() {
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.INSERT_QUERY_STOCK);
            preparedStatement.setString(1,newRawMaterialNamePage.getText());
            preparedStatement.setString(2,newRawMaterialsPricePage.getText());
            preparedStatement.setString(3,newRawMaterialsQuantityPage.getText());
            preparedStatement.setString(4,newRawMaterialsDescPage.getText());

            boolean resultSet = preparedStatement.execute();

            if (resultSet){
                Controller.infoBox("New raw material has been added successfully!",null,"Success!");
            }else {
                Controller.infoBox("New raw materials has not been added successfully!",null, "Failed!");
            }
        }catch (SQLException e) {
           jdbcController.printSQLException(e);
        }

    }

    public void goBack() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new rawMaterials().start(stage);
    }
}
