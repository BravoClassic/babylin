package babylinapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.*;

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



    protected void processOrder(){
        String p;

        if (orderList.size()>1)
            p="Products";
        else
            p="Product";
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT OrderId FROM babylinapp_orders WHERE OrderId=?");
            preparedStatement.setInt(1,returnOrderId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                    orderId = resultSet.getInt("OrderId");
            }
            Controller.infoBox("Total Cost: GHC " + total, "Cost of " + p, "Order Number - BC"+orderId);
            createPDF("BC"+orderId,customerName.getSelectionModel().getSelectedItem());
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
        orderId=returnOrderId();
        try{
            Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO babylinapp_product_orders values(?,?,?,?)");
            //noinspection ForLoopReplaceableByForEach
            for (int val=0; val<orderList.size();val++){
                preparedStatement.setString(1,null);
                preparedStatement.setInt(2,orderId);
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
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO babylinapp_users values(?,?,?,?,?)");
            preparedStatement.setString(1,null);
            preparedStatement.setString(2, userNameFull.getText());
            preparedStatement.setString(3,email.getText());
            preparedStatement.setString(4,address.getText());
            preparedStatement.setString(5,phone.getText());

            boolean resultSet = preparedStatement.execute();
            if(!resultSet){
                customerName.getItems().add(userNameFull.getText());
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

    public void createPDF(String pdfName, String customerName) {
        String path = System.getProperty("user.dir");
        String fileName=path+"\\src\\babylinapp\\pdfs\\"+customerName+"-"+pdfName+".pdf";
        System.out.println(fileName);
        PDDocument pdDocument = new PDDocument();
        PDPage pdPage = new PDPage();
        try {
            pdDocument.getDocumentInformation().setTitle("Babylin Consult Receipt - "+customerName);
            pdDocument.getDocumentInformation().setAuthor("Babylin Consult");
            pdDocument.getDocumentInformation().setCreator("Babylin Consult Stock Management App - PDFBox API");
            pdDocument.getDocumentInformation().setSubject("Receipt");
            Calendar date = new GregorianCalendar();
            date.setTime(Date.from(Instant.now()));
            pdDocument.getDocumentInformation().setCreationDate(date);
            pdDocument.getDocumentInformation().setModificationDate(date);
            pdDocument.getDocumentInformation().setKeywords("receipt, invoice");
            pdDocument.addPage(pdPage);
            pdDocument.save(fileName);
            System.out.println("Document Created!"+pdDocument.getNumberOfPages());
            pdDocument.close();
            insertContent(fileName);
        } catch (IOException e) {
            jdbcController.printIOExpection(e);
        }
    }
    protected void insertContent(String pathname){
        File file = new File(pathname);
        try {
            PDDocument document = PDDocument.load(file);
            System.out.println(document.getNumberOfPages());
            PDPage pdPage = document.getPages().get(0);
            PDPageContentStream pdPageContentStream = new PDPageContentStream(document, pdPage);
            pdPageContentStream.beginText();
            pdPageContentStream.setFont(PDType1Font.TIMES_ROMAN,20);
            pdPageContentStream.setLeading(14.5f);
            pdPageContentStream.newLineAtOffset(25, 725);
            pdPageContentStream.showText("Products Purchased");
            for (babylinapp.productClass productClass : orderList) {
                String text1 = "" + productClass.getProductName() + "-" + productClass.getProductPrice() * productClass.getProductQuantity();
                pdPageContentStream.newLine();
                pdPageContentStream.showText(text1);
            }
            pdPageContentStream.newLine();
            pdPageContentStream.showText("Total: "+total);
            pdPageContentStream.endText();
            pdPageContentStream.close();
            document.save(file);
            document.close();
            sendReceipt(getEmail(),customerName.getSelectionModel().getSelectedItem(),orderId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String getEmail(){
        String email="";
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url,jdbcController.user,jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM babylinapp_users WHERE userName=?");
            preparedStatement.setString(1,customerName.getSelectionModel().getSelectedItem());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                    email=resultSet.getString("email");
            }
            connection.close();
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
        System.out.println(email);
        return email;
    }

    protected void sendReceipt(String email,String CustomerName, Integer orderId) {
        String from ="babylinnaturalhairfoods@gmail.com";
        String host="localhost";
        Properties properties = System.getProperties();
        properties.setProperty("mail.stmp.host", host);
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(email));
            message.setSubject("Purchase Order:");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Your receipt bro");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            String fileName=CustomerName+"-BC"+orderId+".pdf";
            DataSource source= new FileDataSource(fileName);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport.send(message);
            Controller.infoBox("Sent receipt via Email!",null,"Purchase Successful!");
        } catch (MessagingException e) {
            Controller.infoBox("Receipt not sent!",null,"Error");
            jdbcController.printMessagingException(e);
        }
    }
        @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(jdbcController.userType.equals("Employee")){
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
            jdbcController.printSQLException(e);
        }
    }
        else if(jdbcController.userType.equals("Customer")){
            try {
                Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
                PreparedStatement preparedStatement =connection.prepareStatement("SELECT * FROM babylinapp_users WHERE email=?");
                preparedStatement.setString(1,jdbcController.emailUniversal);
                ResultSet resultSet =preparedStatement.executeQuery();
                while (resultSet.next()){
                    userList.add(resultSet.getString("userName"));
                }
                connection.close();
                customerName.getItems().addAll(userList);
            } catch (SQLException e) {
                jdbcController.printSQLException(e);
            }
        }

    }
}
