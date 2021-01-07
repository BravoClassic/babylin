package babylinapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class productsController implements Initializable {

    @FXML
    private TableColumn<String,Integer> productsQuantity;
    @FXML
    private TableView<productClass> productsFinishTable;

    @FXML
    private Label productLabelName;

    @FXML
    private Label productPrice;

    @FXML
    private Label productQuantity;

    @FXML
    private TextArea productDesc;

    @FXML
    private Button back;

    @FXML
    private Button add;

    @FXML
    private Button newBtn;

    @FXML
    private Button delete;

    @FXML
    private Button update;

    private ObservableList<productClass> productList = FXCollections.observableArrayList();

    @FXML
    ComboBox<String> comboProductName;

    @FXML
    private void addProduct() throws IOException {
        Stage stage = (Stage) add.getScene().getWindow();
        new addProductPage().start(stage);
    }

    @FXML
    private void newProduct() throws IOException {
        Stage stage = (Stage) newBtn.getScene().getWindow();
        new newProductPage().start(stage);
    }

    @FXML
    private void deleteProduct() throws IOException{
        Stage stage = (Stage) delete.getScene().getWindow();
        new deleteProductPage().start(stage);
    }

    @FXML
    private void updateProduct() throws IOException {
        Stage stage = (Stage) update.getScene().getWindow();
        new updateProductPage().start(stage);
    }

    @FXML
    private void goBack() throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        new Menu().start(stage);
    }

    @FXML
    private void viewProduct() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcController.url);
        PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_PRODUCTS);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Integer result0 = resultSet.getInt("productId");
            String result = resultSet.getString("productName");
            Integer result1=resultSet.getInt("quantity");
            Double result2= resultSet.getDouble("unitPrice");
            String result3 = resultSet.getString("productDescription");
            productList.add(new productClass(result0,result, result1, result2, result3));
            comboProductName.getItems().add(resultSet.getString("productName"));
        }
        connection.close();

    }

    @FXML
    private void displayProduct(){
        productLabelName.setText("Name: "+productList.get(comboProductName.getSelectionModel().getSelectedIndex()).getProductName());
        productPrice.setText("Unit Price: "+productList.get(comboProductName.getSelectionModel().getSelectedIndex()).getProductPrice());
        productQuantity.setText("Quantity Available: "+productList.get(comboProductName.getSelectionModel().getSelectedIndex()).getProductQuantity());
        productDesc.setText(productList.get(comboProductName.getSelectionModel().getSelectedIndex()).getProductDesc());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            viewProduct();
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }
    

}