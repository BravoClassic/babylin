package babylinapp;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.awt.*;
import java.sql.*;

public class addProductPageController {
    @FXML
    Button add;

    @FXML
    Button back;

    @FXML
    protected SimpleObjectProperty<ComboBox> productList = new SimpleObjectProperty<>(this, "productList");

    @FXML
    javafx.scene.control.TextField addProductQuantity;


    public void increaseQuantity() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
        PreparedStatement preparedStatement= connection.prepareStatement(jdbcController.UPDATE_QUERY_PRODUCTS_QUANTITY);
        preparedStatement.setString(0,(String) productList.get().getValue());
        preparedStatement.setInt(1, Integer.parseInt(addProductQuantity.getText()));
        boolean resultSet = preparedStatement.execute();

        if (resultSet){
            Controller.infoBox("Added more "+ productList.get().getValue(),null, "Success!");
        }else {
            Controller.infoBox("Could not add more of "+  productList.get().getValue(), null, "Failed!");
        }

    }
}
