package babylinapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class productsController implements Initializable {

    @FXML
    Label productLabelName;

    @FXML
    Label productPrice;

    @FXML
    Label productQuantity;

    @FXML
    TextArea productDesc;

    @FXML
    Button back;

    @FXML
    Button add;

    @FXML
    Button newBtn;

    @FXML
    Button delete;

    @FXML
    Button update;

//    @FXML
//    TableView<productClass> table;
//
//    @FXML
//    TableColumn<productClass, String> column = new TableColumn<>("Product");
//
    ObservableList<productClass> productList = FXCollections.observableArrayList();

    @FXML
    ComboBox<String> comboProductName;

    @FXML
    protected void addProduct() throws IOException {
        Stage stage = (Stage) add.getScene().getWindow();
        new addProductPage().start(stage);
    }

    @FXML
    protected void newProduct() throws IOException{
        Stage stage = (Stage) newBtn.getScene().getWindow();
        new newProductPage().start(stage);
    }

    @FXML
    protected void deleteProduct() throws IOException{
        Stage stage = (Stage) delete.getScene().getWindow();
        new deleteProductPage().start(stage);
    }

    @FXML
    protected void updateProduct() throws IOException {
        Stage stage = (Stage) update.getScene().getWindow();
        new updateProductPage().start(stage);
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        new Menu().start(stage);
    }

    @FXML
    protected void viewProduct() throws SQLException {

        Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
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
    protected void displayProduct(){
        productLabelName.setText("Name: "+productList.get(comboProductName.getSelectionModel().getSelectedIndex()).getProductName());
        productPrice.setText("Unit Price: "+productList.get(comboProductName.getSelectionModel().getSelectedIndex()).getProductPrice());
        productQuantity.setText("Quantity Available: "+productList.get(comboProductName.getSelectionModel().getSelectedIndex()).getProductQuantity());
        productDesc.setText(productList.get(comboProductName.getSelectionModel().getSelectedIndex()).getProductDesc());
    }

    @FXML
    protected void next(){
        int size = comboProductName.getItems().size();
        int selectedIndex = comboProductName.getSelectionModel().getSelectedIndex();
        if (selectedIndex<size){
            productLabelName.setText("Name: "+productList.get(comboProductName.getSelectionModel().getSelectedIndex()+1).getProductName());
            productPrice.setText("Unit Price: "+productList.get(comboProductName.getSelectionModel().getSelectedIndex()+1).getProductPrice());
            productQuantity.setText("Quantity Available: "+productList.get(comboProductName.getSelectionModel().getSelectedIndex()+1).getProductQuantity());
            productDesc.setText(productList.get(comboProductName.getSelectionModel().getSelectedIndex()+1).getProductDesc());
        }
    }

    @FXML
    protected void previous(){
        int selectedIndex = comboProductName.getSelectionModel().getSelectedIndex();
        if (selectedIndex>0){
//            comboProductName.setValue();
            productLabelName.setText("Name: "+productList.get(comboProductName.getSelectionModel().getSelectedIndex()-1).getProductName());
            productPrice.setText("Unit Price: "+productList.get(comboProductName.getSelectionModel().getSelectedIndex()-1).getProductPrice());
            productQuantity.setText("Quantity Available: "+productList.get(comboProductName.getSelectionModel().getSelectedIndex()-1).getProductQuantity());
            productDesc.setText(productList.get(comboProductName.getSelectionModel().getSelectedIndex()-1).getProductDesc());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            viewProduct();
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
//        column.setCellValueFactory(new PropertyValueFactory<>("productName"));
//        table.setItems(productList);
    }
    

}