package babylinapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

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
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.*;

public class ordersController implements Initializable {

    @FXML
    protected AnchorPane orderWindow;

    @FXML
    protected HBox customer;

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
    private ComboBox<String> via;

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
    private TableColumn<productClass, Integer> id = new TableColumn<>("ID");

    @FXML
    private TableColumn<productClass, String> productName = new TableColumn<>("Product");

    @FXML
    TableColumn<productClass, Integer> quantityColumn = new TableColumn<>("Quantity");

    private ArrayList<Integer> quantityList = new ArrayList<>();

    private ObservableList<productClass> productListTable = FXCollections.observableArrayList();

    private ObservableList<productClass> orderList = FXCollections.observableArrayList();

    private ObservableList<String> userList = FXCollections.observableArrayList();

    private Double total =0.0;
    Date timeDateOrder;
    private String dtOrder;
    private int orderId;

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
            Connection connection=DriverManager.getConnection(jdbcController.url);
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
            preparedStatement.close();
            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
        System.out.println(userId);
        return userId;
    }

    @FXML
    private void placeOrder() {
        if (via.getValue()==null){
            Controller.infoBox("Error Select method to send receipt",null,"Error");
            return;
        }

         if(orderList.size()>0) {
             total=0.0;
             try{
                 int userId=getUserID();
                 //getUserID
                 if (userId != -1) {
                     timeDateOrder = Date.from(Instant.now());
                     SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                     ft.setTimeZone(TimeZone.getTimeZone("GMT"));
                     dtOrder=ft.format(timeDateOrder);//Formatted date
                     //Update babylinapp_orders first
                     Connection connection = DriverManager.getConnection(jdbcController.url);
                     PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO babylinapp_orders(OrderId,UserId,OrderAt) values(?,?,?)");
                     preparedStatement.setString(1, null);
                     preparedStatement.setInt(2, userId);
                     preparedStatement.setString(3, dtOrder);

                     boolean result = preparedStatement.execute();
                     if (!result)
                         System.out.println("Successful!");
                     else
                         System.out.println("Unsuccessful!");
                     preparedStatement.close();
                     connection.close();
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
    }



    protected void processOrder(){
        String p;

        if (orderList.size()>1)
            p="Products";
        else
            p="Product";
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT OrderId FROM babylinapp_orders WHERE OrderId=?");
            preparedStatement.setInt(1,returnOrderId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                    orderId = resultSet.getInt("OrderId");
            }
            preparedStatement.close();
            resultSet.close();
            connection.close();
            Controller.infoBox("Total Cost: GHC " + total, "Cost of " + p, "Order Number - BC"+orderId);
            TextInputDialog value = new TextInputDialog("");
            value.setHeaderText("Amount Given by Customer\n Total Cost:"+total);
            value.showAndWait();
            double change =total - NumberFormat.getInstance(Locale.getDefault()).parse(value.getEditor().getText()).doubleValue();
            Controller.infoBox("Change to be given: GHC"+change,null,"Change");
            createPDF("BC"+orderId,customerName.getSelectionModel().getSelectedItem());
        } catch (SQLException | ParseException e) {
            System.out.println(e);
        }
    }

    protected void storeRevenue(){
        try{
            Connection connection =DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO babylinapp_revenue values(?,?,?,?)");
            preparedStatement.setString(1,null);
            preparedStatement.setInt(2,returnOrderId());
            preparedStatement.setDouble(3,total);
            preparedStatement.setString(4, dtOrder);
            boolean result = preparedStatement.execute();
            if (!result) System.out.println("Worked");
            else System.out.println("Falied!");
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }

    protected void updateProductOrders(){
        orderId=returnOrderId();
        try{
            Connection connection = DriverManager.getConnection(jdbcController.url);
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
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }

    protected int returnOrderId(){
        int OrderId1=0;
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT max(OrderID) FROM babylinapp_orders ");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                    OrderId1=resultSet.getInt(1);
            }
            preparedStatement.close();
            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
        System.out.println(OrderId1);
        return OrderId1;
    }

    protected void placeOrderDB(Integer q, String n){//Update Product Quantity
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url);
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
            preparedStatement.close();
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
    }


    @FXML
    protected void addUser() {
        jdbcController jdbc = new jdbcController();
        String newUserEmail = email.getText();
        if(userNameFull.getText().equals("")||email.getText().equals("")||address.getText().equals("")||phone.getText().equals("")){
            Controller.infoBox("Empty field(s)! Enter some values!",null,"Error");
            return;
        }
        if (jdbc.checkEmail(newUserEmail,"Customer")) {
            try {
                Connection connection = DriverManager.getConnection(jdbcController.url);
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO babylinapp_users values(?,?,?,?,?,?,?)");
                preparedStatement.setString(1, null);
                preparedStatement.setString(2, userNameFull.getText());
                preparedStatement.setString(3, email.getText());
                preparedStatement.setString(4, jdbcController.getHash("123"));
                preparedStatement.setString(5, address.getText());
                preparedStatement.setString(6, phone.getText());
                preparedStatement.setString(7, null);

                boolean resultSet = preparedStatement.execute();
                if (!resultSet) {
                    customerName.getItems().add(userNameFull.getText());
                    Controller.infoBox("Added New User!", null, "New User");
                } else {
                    Controller.infoBox("Error did not add new user!", null, "Error New User");
                }
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                jdbcController.printSQLException(e);
            }
        }else{
            Controller.errBox("User registered already!",null,"Error!");
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
            PDImageXObject pdImage=PDImageXObject.createFromFile(System.getProperty("user.dir")+"\\src\\babylinapp\\assets\\Logo1000.jpg",document);
            pdPageContentStream.beginText();
            pdPageContentStream.setFont(PDType1Font.TIMES_ROMAN,25);
            pdPageContentStream.setLeading(14.5f);
            pdPageContentStream.newLineAtOffset(25, 725);
            pdPageContentStream.showText("Date: "+Date.from(Instant.now()));
            pdPageContentStream.newLine();
            pdPageContentStream.newLine();
            pdPageContentStream.newLine();
            pdPageContentStream.showText("Product Name | Unit Price | Quantity | Total Price ");
            pdPageContentStream.newLine();
            pdPageContentStream.newLine();
            pdPageContentStream.newLine();
            for (babylinapp.productClass productClass : orderList) {
                double value =productClass.getProductPrice()*productClass.getProductQuantity();
                String text1 = "" + productClass.getProductName() + " | " + productClass.getProductPrice()+ " | " +productClass.getProductQuantity()+ " | "+value ;
                pdPageContentStream.newLine();
                pdPageContentStream.newLine();
                pdPageContentStream.newLine();
                pdPageContentStream.showText(text1);
            }
            pdPageContentStream.newLine();
            pdPageContentStream.showText("------------------------------------------------");
            pdPageContentStream.newLine();
            pdPageContentStream.showText("Total:  "+"GHC"+total);
            pdPageContentStream.endText();
            pdPageContentStream.drawImage(pdImage, 400, 800);
            pdPageContentStream.close();
            document.save(file);
            document.close();
            if (via.getValue().equals("Email")) {
                sendReceipt(customerName.getSelectionModel().getSelectedItem());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    protected String getEmail(){
        String email="";
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM babylinapp_users WHERE userName=?");
            preparedStatement.setString(1,customerName.getSelectionModel().getSelectedItem());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                    email=resultSet.getString("email");
            }
            preparedStatement.close();
            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
        System.out.println(email);
        return email;
    }

    private void sendReceipt(String CustomerName){
        Dialog<String> dialog = new Dialog<>();
        PasswordField pwd = new PasswordField();
        pwd.setText("babylin@123");
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter password:"), pwd);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(content);
        System.out.println("Hello");
        dialog.showAndWait();
                send(pwd.getText(),CustomerName);
//                System.out.println("Hello");
    }

    private void send(String pass, String CustomerName){
        String from ="reply.babylin@gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls","true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.stmp.port","587");
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });
        Message message = prepareMessage(session,from,CustomerName);
        assert message != null;
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            jdbcController.printMessagingException(e);
        }
        Controller.infoBox("Sent receipt via Email!",null,"Purchase Successful!");

    }

    private Message prepareMessage(Session session, String from, String customerName) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(getEmail()));
            message.setSubject("Purchase Order:"+orderId+"-Babylin Consult");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Thank you for purchasing BN natural hair foods");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            String fileName="C:\\Users\\Gerald\\IdeaProjects\\babylin\\src\\babylinapp\\pdfs\\"+customerName+"-BC"+orderId+".pdf";
            DataSource source= new FileDataSource(fileName);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(source.getName());
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            return message;
        } catch (MessagingException e) {
            Controller.errBox(e.getMessage()+"\n"+e.getLocalizedMessage(),"Error email not sent!","Error!");
        }
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            try {
                Connection connection = DriverManager.getConnection(jdbcController.url);
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
                preparedStatement.close();
                resultSet.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(jdbcController.userType.equals("Employee")){
                    try{
                        Connection connection = DriverManager.getConnection(jdbcController.url);
                        PreparedStatement preparedStatement =connection.prepareStatement("SELECT * FROM babylinapp_users");
                        ResultSet resultSet =preparedStatement.executeQuery();
                        while (resultSet.next()){
                            userList.add(resultSet.getString("userName"));
                        }
                        preparedStatement.close();
                        resultSet.close();
                        connection.close();
                        customerName.getItems().addAll(userList);
                    } catch (SQLException e) {
                        jdbcController.printSQLException(e);
                    }
    }
        else if(jdbcController.userType.equals("Customer")){
            try {
                Connection connection = DriverManager.getConnection(jdbcController.url);
                PreparedStatement preparedStatement =connection.prepareStatement("SELECT * FROM babylinapp_users WHERE email=?");
                preparedStatement.setString(1,jdbcController.emailUniversal);
                ResultSet resultSet =preparedStatement.executeQuery();
                while (resultSet.next()){
                    userList.add(resultSet.getString("userName"));
                }
                preparedStatement.close();
                resultSet.close();
                connection.close();
                customerName.getItems().addAll(userList);
            } catch (SQLException e) {
                jdbcController.printSQLException(e);
            }
            customer.setVisible(false);
//            .setPrefWidth(450.00);
        }

    }
}
