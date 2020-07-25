package babylinapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class UpdateStockPageController {
    public TextField stockUnitPrice;
    public TextField stockQuantity;
    public TextArea stockDescription;
    public TextField stockName;
    @FXML
    private ComboBox stockList;

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
            Controller.infoBox("Successfully updated "+stockList.getValue(),null,"Success");
        }else {
            Controller.infoBox("Failed to update "+stockList.getValue(),null,"Failed!");
        }
    }


    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        new stocks().start(stage);
    }
}
