package babylinapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class contactPageController implements Initializable {

    @FXML
    private Label pwdLabel;
    @FXML
    private CheckBox pwdChange;
    @FXML
    private PasswordField pwd;
    @FXML
    private Button reset;
    @FXML
    private Button copyNum;
    @FXML
    private Button copyEmail;
    @FXML
    private Button update;
    @FXML
    private Button back;
    @FXML
    private TextField phone;
    @FXML
    private TextField address;
    @FXML
    private TextField email;
    @FXML
    private DatePicker dob;
    @FXML
    private TextField fullName;
    @FXML
    private ComboBox<String> comboContactName;
    @FXML
    private Integer userId;

    private final ObservableList<contactClass> contactList = FXCollections.observableArrayList();

    @FXML
    private void copyMail() {
        String t = email.getText();
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(t);
        clipboard.setContent(content);
        Controller.infoBox("Email copied!", null, "Copied!");
    }

    @FXML
    private void copyNumber() {
        String p = phone.getText();
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(p);
        clipboard.setContent(content);
        Controller.infoBox("Phone number copied!", null, "Copied!");

    }

    private void getContactInfo() {
        if (jdbcController.userType.equals("Employee")) {
            try {
                Connection connection = DriverManager.getConnection(jdbcController.url);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(jdbcController.SELECT_CLIENTS_DETAILS);
                while (resultSet.next()) {
                    Integer result0 = resultSet.getInt("userId");
                    String result = resultSet.getString("userName");
                    Date result1 = Date.valueOf(resultSet.getString("DOB"));
                    String result2 = resultSet.getString("email");
                    String result3 = resultSet.getString("address");
                    String result4 = resultSet.getString("phone");
                    contactList.add(new contactClass(result0, result, result1, result2, result3, result4));
                    comboContactName.getItems().add(resultSet.getString("userName"));
                }
                resultSet.close();
                statement.close();
                connection.close();
//                displayContact();
            } catch (SQLException e) {
                jdbcController.printSQLException(e);
            }
        } else if (jdbcController.userType.equals("Customer")) {
            try {
                Connection connection = DriverManager.getConnection(jdbcController.url);
                PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_CUST_QUERY_VALIDATE);
                preparedStatement.setString(1, jdbcController.emailUniversal);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    userId = resultSet.getInt("userId");
                    Integer result0 = resultSet.getInt("userId");
                    String result = resultSet.getString("userName");
                    Date result1 = Date.valueOf(resultSet.getString("DOB"));
                    String result2 = resultSet.getString("email");
                    String result3 = resultSet.getString("address");
                    String result4 = resultSet.getString("phone");
                    contactList.add(new contactClass(result0, result, result1, result2, result3, result4));
                }
                resultSet.close();
                preparedStatement.close();
                connection.close();
                displayContact();
            } catch (SQLException e) {
                jdbcController.printSQLException(e);
            }
        }
    }

    @FXML
    private void displayContact() {
        if (jdbcController.userType.equals("Employee")) {

            fullName.setText(contactList.get(comboContactName.getSelectionModel().getSelectedIndex()).getContactName());
            dob.setValue(LocalDate.parse(contactList.get(comboContactName.getSelectionModel().getSelectedIndex()).getDateBirth().toString()));
            email.setText(contactList.get(comboContactName.getSelectionModel().getSelectedIndex()).getContactEmail());
            address.setText(contactList.get(comboContactName.getSelectionModel().getSelectedIndex()).getContactAddress());
            phone.setText(contactList.get(comboContactName.getSelectionModel().getSelectedIndex()).getContactPhone());
        } else if (jdbcController.userType.equals("Customer")) {
            fullName.setText(contactList.get(0).getContactName());
            dob.setValue(LocalDate.parse(contactList.get(0).getDateBirth().toString()));
            email.setText(contactList.get(0).getContactEmail());
            address.setText(contactList.get(0).getContactAddress());
            phone.setText(contactList.get(0).getContactPhone());

        }
    }

    @FXML
    private void updateContact() throws IOException {
        if (fullName.getText().equals("")) {
            Controller.errBox("Enter Full Name!", null, "Error");
            return;
        }
        if (dob.getValue() == null) {
            Controller.errBox("Enter Date of Birth!", null, "Error");
            return;
        }
        if (email.getText().equals("")) {
            Controller.errBox("Enter Email!", null, "Error");
            return;
        }
        if (address.getText().equals("")) {
            Controller.errBox("Enter Address!", null, "Error");
            return;
        }
        if (phone.getText().equals("")) {
            Controller.errBox("Enter Phone Number!", null, "Error");
            return;
        }
        if (!pwdChange.isSelected()) {

            String name = fullName.getText().trim();
            LocalDate birthDate = dob.getValue();
            String emailAddress = email.getText().trim();
            String addressDetails = address.getText().trim();
            String number = phone.getText().trim();
//            System.out.println(birthDate);
            try {
                Connection connection = DriverManager.getConnection(jdbcController.url);
                PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.UPDATE_CUST);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, String.valueOf(birthDate));
                preparedStatement.setString(3, emailAddress);
                preparedStatement.setString(4, addressDetails);
                preparedStatement.setString(5, number);
                preparedStatement.setInt(6, userId);
                preparedStatement.executeUpdate();
                Controller.infoBox("Contact Updated!", null, "Success");
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                jdbcController.printSQLException(e);
            }
        }else if(pwdChange.isSelected()){
            if (pwd.getText().equals("")) {
                Controller.errBox("Enter password!", null, "Error");
                return;
            }
            String name = fullName.getText().trim();
            LocalDate birthDate = dob.getValue();
            String emailAddress = email.getText().trim();
            String addressDetails = address.getText().trim();
            String number = phone.getText().trim();
            String pass =pwd.getText();
            String hashedPwd = jdbcController.getHash(pass);
            try {
                Connection connection = DriverManager.getConnection(jdbcController.url);
                PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.UPDATE_CUST_PWD);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, String.valueOf(birthDate));
                preparedStatement.setString(3, emailAddress);
                preparedStatement.setString(4, addressDetails);
                preparedStatement.setString(5, number);
                preparedStatement.setString(6, hashedPwd);
                preparedStatement.setInt(7, userId);
                preparedStatement.execute();
                Controller.infoBox("Contact Updated and password!", null, "Success");
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                jdbcController.printSQLException(e);
            }

        }
        goMenu();
    }

    @FXML
    private void resetPwd(){
        String emailChange = email.getText();
        String hashedPwd = jdbcController.getHash("123");
        try{
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.UPDATE_CUST_PWD_CHANGE);
            preparedStatement.setString(1,hashedPwd);
            preparedStatement.setString(2,emailChange);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
            Controller.infoBox("Password reset!",null,"Password Reset");
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }

    @FXML
    public void goMenu() throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        new Menu().start(stage);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getContactInfo();
        if (jdbcController.userType.equals("Employee")) {
            update.setVisible(false);
            update.setDisable(true);

            pwdLabel.setVisible(false);

            fullName.setDisable(true);
            fullName.setEditable(false);

            dob.setDisable(true);
            dob.setEditable(false);

            email.setDisable(true);
            email.setEditable(false);

            address.setDisable(true);
            address.setEditable(false);

            phone.setDisable(true);
            phone.setEditable(false);

            reset.setDisable(false);
            reset.setVisible(true);

            pwd.setDisable(true);
            pwd.setVisible(false);

            pwdChange.setDisable(true);
            pwdChange.setVisible(false);

        } else if (jdbcController.userType.equals("Customer")) {
            comboContactName.setVisible(false);
            comboContactName.setDisable(true);

            pwdLabel.setVisible(true);

            fullName.setDisable(false);
            fullName.setEditable(true);

            dob.setDisable(false);
            dob.setEditable(true);

            email.setDisable(false);
            email.setEditable(true);

            address.setDisable(false);
            address.setEditable(true);

            phone.setDisable(false);
            phone.setEditable(true);

            copyEmail.setDisable(true);
            copyEmail.setVisible(false);

            copyNum.setDisable(true);
            copyNum.setVisible(false);

            reset.setDisable(true);
            reset.setVisible(false);

            pwd.setDisable(false);
            pwd.setVisible(true);
        }
    }
}

