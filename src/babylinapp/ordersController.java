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
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ordersController implements Initializable {

    @FXML
    protected TextField phone;

    @FXML
    protected TextField address;

    @FXML
    protected TextField email;

    @FXML
    protected ComboBox<String> customerName;

    @FXML
    protected TextField userNameLast;

    @FXML
    protected Button clearUserName;

    @FXML
    protected Button addUserName;

    @FXML
    protected TextField userNameFull;

    @FXML
    protected ComboBox<String> productList;

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
    protected Spinner<Integer> quantity;

    @FXML
    protected TableView<productClass> tableOrder;

    @FXML
    TableColumn<productClass, Integer> id = new TableColumn<>("ID");

    @FXML
    TableColumn<productClass, String> productName = new TableColumn<>("Product");

    @FXML
    TableColumn<productClass, Integer> quantityColumn = new TableColumn<>("Quantity");

    private ArrayList<Integer> quantityList = new ArrayList<>();

    ObservableList<productClass> productListTable = FXCollections.observableArrayList();

    ObservableList<productClass> orderList = FXCollections.observableArrayList();

    ObservableList<String> userList = FXCollections.observableArrayList();

    double total =0;
    Date timeDateOrder;
    String dtOrder;
    int orderId;

    public void addList() {
        id.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));
        productListTable.get(productList.getSelectionModel().getSelectedIndex()).setProductQuantity(quantity.getValue());
//        productListTable.get(productList.getSelectionModel().getSelectedIndex()).productPrice.intValue();
        if (orderList.contains(productListTable.get(productList.getSelectionModel().getSelectedIndex()))) {
            Controller.infoBox("Your order already contains this product. Delete it and add it again if you want to change the quantity.", null, "Add same product Error");
        } else {
            orderList.add(productListTable.get(productList.getSelectionModel().getSelectedIndex()));
            tableOrder.getItems().add(productListTable.get(productList.getSelectionModel().getSelectedIndex()));
        }
    }

    @FXML
    public void setQuantity() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, quantityList.get(productList.getSelectionModel().getSelectedIndex()), 0);
        quantity.setValueFactory(valueFactory);
    }

    @FXML
    public void clear(){
        productList.setPromptText("Select a Product");
        quantity.decrement(quantity.getValue() -1);
    }

    @FXML
    public void goMenu() throws IOException {
        Stage stage = (Stage) menu.getScene().getWindow();
        new Menu().start(stage);
    }


    @FXML
    protected void clearOrderSheet(){
        if(productListTable.isEmpty() && orderList.isEmpty()) {
            System.out.println("Both empty freaking table and list!");
        }
        else{
            tableOrder.getItems().clear();
            orderList.clear();
        }
    }

    @FXML
    protected int getUserID(){
        int userId=0;
        try{
            Connection connection=DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM babylinapp_users");
//            preparedStatement.setString(1,customerName.getSelectionModel().getSelectedItem());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String name=resultSet.getString("userName");
                System.out.println(name);
                if(name.equals(customerName.getValue())) {
                    userId = resultSet.getInt("userId");
                    break;
                }
                else {
//                    Controller.infoBox("No user found!", null, "Error");
                    System.out.println("Error!");
                    userId= -1;
                }
            }

        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
        System.out.println(userId);
        return userId;
    }

    @FXML
    protected void placeOrder() {
         if(orderList.size()>0) {
             total=0;
             try{
                 int userId=getUserID();
                 //getUserID
                 if (userId != -1) {
                     timeDateOrder = Date.from(Instant.now());
                     SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                     dtOrder=ft.format(timeDateOrder);//Formatted date
                     //Update babylinapp_orders first
                     Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
                     PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO babylinapp_orders(OrderId,UserId,OrderAt) values(?,?,?)");
                     preparedStatement.setString(1, null);
                     preparedStatement.setInt(2, userId);
                     preparedStatement.setString(3, dtOrder);

                     boolean result = preparedStatement.execute();
                     if (!result)
                         System.out.println("Successful!");
                     else
                         System.out.println("Unsuccessful!");
                 }
                 //Select last order form babylinapp_orders and update babylinapp_products
                 updateProductOrders();

                 //Update Product Quantity
                 for (babylinapp.productClass productClass : orderList) {
                     placeOrderDB(productClass.getProductQuantity(), productClass.getProductName());
                 }
             } catch (SQLException e) {
                 jdbcController.printSQLException(e);
             }
             for (babylinapp.productClass productClass : orderList) {
                 total += productClass.getProductQuantity() * productClass.getProductPrice();
             }
             //using babylinapp_revenue
             storeRevenue();
             processOrder();
         }
         else {
             Controller.showAlert(Alert.AlertType.ERROR,order.getScene().getWindow(),"Error | Empty Cart", "Please select a product(s) to purchase");
         }
//        System.out.println(orderList.get(0).getProductName());
//        System.out.println(orderList.get(0).getProductQuantity());
//        System.out.println(orderList.get(0).getProductPrice());
    }


    //Get orderID

    protected void processOrder(){
        String p;

        if (orderList.size()>1)
            p="Products";
        else
            p="Product";
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT OrderId FROM babylinapp_orders");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                if (resultSet.last())
                    orderId = resultSet.getInt("OrderId");
            }
            Controller.infoBox("Total Cost: GHC " + total, "Cost of " + p, "Order Number - "+orderId);
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }

    protected void storeRevenue(){
        try{
            Connection connection =DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO babylinapp_revenue values(?,?,?,?)");
            preparedStatement.setString(1,null);
            preparedStatement.setInt(2,returnOrderId());
            preparedStatement.setDouble(3,total);
            preparedStatement.setString(4, dtOrder);
            boolean result = preparedStatement.execute();
            if (!result) System.out.println("Worked");
            else System.out.println("Falied!");
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }

    protected void updateProductOrders(){
        int orderId1=returnOrderId();
        try{
            Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO babylinapp_product_orders values(?,?,?,?)");
            //noinspection ForLoopReplaceableByForEach
            for (int val=0; val<orderList.size();val++){
                preparedStatement.setString(1,null);
                preparedStatement.setInt(2,orderId1);
                preparedStatement.setInt(3,orderList.get(val).getProductID());
                preparedStatement.setInt(4,orderList.get(val).getProductQuantity());
                boolean resultSet = preparedStatement.execute();
                if (!resultSet){
                    System.out.println("Worked");
                }else {
                    System.out.println("Not worked");
                }
            }
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }

    protected int returnOrderId(){
        int OrderId1=0;
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM babylinapp_orders");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                if (resultSet.last())
                    OrderId1=resultSet.getInt(1);
            }
            connection.close();
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
        System.out.println("Here");
        System.out.println(OrderId1);
        return OrderId1;
    }

    protected void placeOrderDB(Integer q, String n){//Update Product Quantity
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.UPDATE_QUERY_PRODUCTS_QUANTITY_SUB);
            preparedStatement.setInt(1,q);//Product Quantity
            preparedStatement.setString(2,n);//Product Name
            boolean resultSet = preparedStatement.execute();
            if (!resultSet)
                System.out.println("Working");
//                Controller.showAlert(Alert.AlertType.INFORMATION,order.getScene().getWindow(),"Purchased Products","Thank for purchasing Bn Natural Foods products");
            else
            System.out.println("Not working");
//                Controller.showAlert(Alert.AlertType.ERROR,order.getScene().getWindow(),"Error Purchasing Products","There was an error purchasing Bn Natural Foods products");
            connection.close();
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }


    @FXML
    protected void delete(){
        int rowInTable=tableOrder.getSelectionModel().getSelectedIndex();
        if (total>0)
            total-=orderList.get(tableOrder.getSelectionModel().getSelectedIndex()).getProductQuantity()*orderList.get(tableOrder.getSelectionModel().getSelectedIndex()).getProductPrice();

        tableOrder.getItems().remove(tableOrder.getItems().get(rowInTable));
        orderList.remove(orderList.get(rowInTable));
//        tableOrder.getItems().remove(rowInTable);
//        System.out.println(tableOrder.getItems().get(rowInTable));
//        System.out.println(orderList.get(tableOrder.getSelectionModel().getSelectedIndex()).getProductName());
//        System.out.println(orderList.get(tableOrder.getSelectionModel().getSelectedIndex()).getProductQuantity());
    }


    @FXML
    protected void addUser() {
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO babylinapp_users values(?,?,?,?,?,?)");
            preparedStatement.setString(1,null);
            preparedStatement.setString(2, userNameFull.getText());
            preparedStatement.setString(3,userNameLast.getText());
            preparedStatement.setString(4,email.getText());
            preparedStatement.setString(5,address.getText());
            preparedStatement.setString(6,phone.getText());

            boolean resultSet = preparedStatement.execute();
            if(!resultSet){
                customerName.getItems().add(userNameFull.getText()+userNameLast.getText());
                Controller.infoBox("Added New User!",null,"New User");
            }else {
                Controller.infoBox("Error did not add new user!",null,"Error New User");
            }

        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }


    @FXML
    protected void clearUser(){
         userNameFull.clear();
         userNameLast.clear();
         email.clear();
         address.clear();
         phone.clear();
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
            preparedStatement =connection.prepareStatement("SELECT * FROM babylinapp_users");
            resultSet =preparedStatement.executeQuery();
            while (resultSet.next()){
                userList.add(resultSet.getString("userName"));
            }
            connection.close();
            customerName.getItems().addAll(userList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
