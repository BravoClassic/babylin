package babylinapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ordersController implements Initializable {

    @FXML
    protected ComboBox productList;

    @FXML
    protected Button addProduct;

    @FXML
    protected Button clear;

    @FXML
    protected  Button delete;

    @FXML
    protected  Button order;

    @FXML
    protected Button menu;

    @FXML
    protected Spinner quantity;

    @FXML
    protected TableView tableOrder;

    @FXML
    TableColumn<productClass, Integer> id = new TableColumn<>("ID");

    @FXML
    TableColumn<productClass, String> productName = new TableColumn<>("Product");

    @FXML
    TableColumn<productClass, Integer> quantityColumn = new TableColumn<>("Quantity");

    private ArrayList<Integer> quantityList = new ArrayList<>();

    ObservableList<productClass> productListTable = FXCollections.observableArrayList();

    ObservableList<productClass> orderList = FXCollections.observableArrayList();

    double total =0;
    
    private SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 0);

    public void addList(){
        id.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));
        productListTable.get(productList.getSelectionModel().getSelectedIndex()).setProductQuantity((Integer) quantity.getValue());
//        productListTable.get(productList.getSelectionModel().getSelectedIndex()).productPrice.intValue();
        orderList.add(productListTable.get(productList.getSelectionModel().getSelectedIndex()));
        tableOrder.getItems().add(productListTable.get(productList.getSelectionModel().getSelectedIndex()));
    }


    @FXML
    public void setQuantity() {
        valueFactory=new SpinnerValueFactory.IntegerSpinnerValueFactory(1,quantityList.get(productList.getSelectionModel().getSelectedIndex()),0);
        quantity.setValueFactory(valueFactory);
    }

    @FXML
    public void clear(){
        productList.setPromptText("Select a Product");
        quantity.decrement((Integer) quantity.getValue()-1);
    }

    @FXML
    public void goMenu() throws IOException {
        Stage stage = (Stage) menu.getScene().getWindow();
        new Menu().start(stage);
    }

    @FXML
    protected void placeOrder() {
        String p = "";

        if (orderList.size()>1)
             p="Products";
         else
             p="Product";

         if(orderList.size()>0) {
             total=0;
             for (int i = 0; i < orderList.size(); i++) {
                 total += orderList.get(i).getProductQuantity() * orderList.get(i).getProductPrice();
             }
             Controller.infoBox("Total Cost: GHC " + total, "Cost of " + p, "Order Number - 001");
         }
         else {
             Controller.showAlert(Alert.AlertType.ERROR,order.getScene().getWindow(),"Error | Empty Cart", "Please select a product(s) to purchase");
         }
//        System.out.println(orderList.get(0).getProductName());
//        System.out.println(orderList.get(0).getProductQuantity());
//        System.out.println(orderList.get(0).getProductPrice());
    }

    protected void placeOrderDB(Integer p, String n){
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.UPDATE_QUERY_PRODUCTS_QUANTITY_SUB);
            preparedStatement.setInt(1,p);
            preparedStatement.setString(2,n);
            Boolean resultSet = preparedStatement.execute();
            if (resultSet)
                Controller.showAlert(Alert.AlertType.INFORMATION,order.getScene().getWindow(),"Purchased Products","Thank for purchasing Bn Natural Foods products");
            else
                Controller.showAlert(Alert.AlertType.ERROR,order.getScene().getWindow(),"Error Purchasing Products","There was an error purchasing Bn Natural Foods products");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    protected void delete(){
        Integer rowInTable=tableOrder.getSelectionModel().getSelectedIndex();
        if (total>0)
            total-=orderList.get(tableOrder.getSelectionModel().getSelectedIndex()).getProductQuantity()*orderList.get(tableOrder.getSelectionModel().getSelectedIndex()).getProductPrice();

        tableOrder.getItems().remove(tableOrder.getItems().get(rowInTable));
        orderList.remove(orderList.get(rowInTable));
//        tableOrder.getItems().remove(rowInTable);
//        System.out.println(tableOrder.getItems().get(rowInTable));
//        System.out.println(orderList.get(tableOrder.getSelectionModel().getSelectedIndex()).getProductName());
//        System.out.println(orderList.get(tableOrder.getSelectionModel().getSelectedIndex()).getProductQuantity());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM babylinapp_products");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                productList.getItems().add(resultSet.getString("ProductName"));
                quantityList.add(Integer.parseInt(resultSet.getString("quantity")));
                productListTable.add((new productClass(
                        resultSet.getInt("productId"),
                        resultSet.getString("ProductName"),
                        resultSet.getInt("quantity"),
                        resultSet.getDouble("unitPrice"),
                        resultSet.getString("productDescription")
                )));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
