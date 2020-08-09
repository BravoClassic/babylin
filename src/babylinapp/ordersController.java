package babylinapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
    protected  Button cancel;

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


    private SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 0);
//    public Object productClass;

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
    protected void placeOrder() {
        Controller.infoBox("Total Cost: GHC "+(orderList.get(0).getProductQuantity()*orderList.get(0).getProductPrice()), "Cost of "+orderList.get(0).getProductName(), "Order Number - 001");
        System.out.println(orderList.get(0).getProductName());
        System.out.println(orderList.get(0).getProductQuantity());
        System.out.println(orderList.get(0).getProductPrice());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
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
