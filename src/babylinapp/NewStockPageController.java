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

public class NewStockPageController {

    @FXML
    protected Button newBtnStock;

    @FXML
    protected TextField newStockNameProductPage;

    @FXML
    protected TextField newStockPriceStockPage;

    @FXML
    protected TextField newStockQuantityStockPage;

    @FXML
    protected TextArea newStockDescStockPage;

    @FXML
    protected Button cancel;

    public void newStock() {
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.INSERT_QUERY_STOCK);
            preparedStatement.setString(0,newStockNameProductPage.getText());
            preparedStatement.setString(1,newStockPriceStockPage.getText());
            preparedStatement.setString(2,newStockQuantityStockPage.getText());
            preparedStatement.setString(3,newStockDescStockPage.getText());

            boolean resultSet = preparedStatement.execute();

            if (resultSet){
                Controller.infoBox("New Stock has been added successfully!",null,"Success!");
            }else {
                Controller.infoBox("New Stock has not been added successfully!",null, "Failed!");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void goBack() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new stocks().start(stage);
    }
}
