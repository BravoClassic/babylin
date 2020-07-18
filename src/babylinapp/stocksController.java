package babylinapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class stocksController implements Initializable {

    @FXML
    TableView<stocksClass> table;

    @FXML
    TableColumn<stocksClass, String> columnStocks;

    @FXML
    Button back;

    @FXML
    Button next;

    @FXML
    Button previous;

    @FXML
    Button newBtn;

    @FXML
    Button add;

    @FXML
    Button delete;

    @FXML
    Button update;

    ObservableList<stocksClass> stockList= FXCollections.observableArrayList();


    public void viewStock() throws SQLException {

        Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
        PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_STOCK);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String result = resultSet.getString("productName");
            Integer result1=resultSet.getInt("quantity");
            Double result2= resultSet.getDouble("unitPrice");
            String result3 = resultSet.getString("productDescription");
            stockList.add(new stocksClass(result, result1, result2, result3));
        }
        connection.close();

    }

    public void addStock() throws IOException {
        Stage stage = (Stage) add.getScene().getWindow();
        new newProductPage().start(stage);
    }

    public void deleteProduct() throws IOException{
        Stage stage = (Stage) delete.getScene().getWindow();
        new deleteProductPage().start(stage);
    }

    public void goBack() throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        new Menu().start(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            viewStock();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        columnStocks.setCellValueFactory(new PropertyValueFactory<>("stockName"));
        table.setItems(stockList);
//        System.out.println(table.getSelectionModel().getSelectedItem().getProductName());
    }
}
