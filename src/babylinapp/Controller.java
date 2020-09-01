package babylinapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class Controller {
    @FXML
    protected ComboBox<String> u_type_register;
    //Login Input
    @FXML
    private TextField emailIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    //Register Input
    @FXML
    private TextField emailIdFieldRegister;

    @FXML
    private PasswordField passwordFieldRegister;

    @FXML
    private PasswordField passwordFieldRegister1;

    @FXML
    private TextField fnRegister;

    @FXML
    private  TextField lnRegister;

    @FXML
    private DatePicker DOB;

    @FXML
    protected ComboBox<String> u_type_login;

    @FXML
    private Button submitButtonRegister;

    @FXML
    public TextField address;

    @FXML
    public TextField number;

    @FXML
    private Label registerTxtBtn;

    @FXML
    private  Label signInTxtBtn;


    @FXML
    private void login() throws Exception {
        Stage stage;
        Window owner = submitButton.getScene().getWindow();
        if (emailIdField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your email id");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password");
            return;
        }
        if (u_type_login.getValue().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please select user type.");
            return;
        }

        String emailId = emailIdField.getText();
        String password = passwordField.getText();

        jdbcController jdbcDao = new jdbcController();
        boolean flag = jdbcDao.validate(emailId, password, u_type_login.getValue());

        if (!flag) {
            infoBox("Please enter correct Email and Password", null, "Failed");
        } else {
            infoBox("Login Successful!", null, "Success");
            stage = (Stage) submitButton.getScene().getWindow();
            Menu menu = new Menu();
            menu.start(stage);
        }
    }

    public void register() throws IOException {
        Stage stage;
        Window owner =submitButtonRegister.getScene().getWindow();
        String fn = fnRegister.getText();
        String ln = lnRegister.getText();
        String dob =""+DOB.getValue();
        String us_type=u_type_register.getValue();
        String addr=address.getText();
        String phone=number.getText();
        String email=emailIdFieldRegister.getText();
        String pwd=passwordFieldRegister.getText();
        String pwd1=passwordFieldRegister1.getText();
        //Checks if inputs are empty
        if(fn.isEmpty()){
            showAlert(Alert.AlertType.ERROR,owner,"FORM ERROR!","Please Enter Your First Name");
            return;
        }

        if (ln.isEmpty()){
            showAlert(Alert.AlertType.ERROR,owner,"FORM ERROR!","Please Enter Your Last Name");
            return;
        }

        if(dob.isEmpty()){
            showAlert(Alert.AlertType.ERROR,owner,"FORM ERROR!","Please Enter Your Date of Birth");
            return;
        }
        if (us_type.isEmpty()){
            showAlert(Alert.AlertType.ERROR,owner,"FORM ERROR!","Please Enter Your User Type");
            return;
        }
        if(email.isEmpty()){
            showAlert(Alert.AlertType.ERROR,owner,"FORM ERROR!", "Please Enter Your Email");
            return;
        }
        if(pwd.isEmpty()){
            showAlert(Alert.AlertType.ERROR,owner,"FORM ERROR!","Please Enter Your Password");
        }
        if(pwd1.isEmpty()){
            showAlert(Alert.AlertType.ERROR,owner,"FORM ERROR!","Plase Enter Your Password");
        }

        if(pwd.equals(pwd1))
            System.out.println(passwordFieldRegister.getText());
        else {
            showAlert(Alert.AlertType.ERROR, owner, "FORM ERROR!", "Please Enter Valid Password");
            return;
        }
        jdbcController jdbcController = new jdbcController();
        boolean flag=jdbcController.register(fn,dob,us_type,addr,phone,email,pwd,pwd1);

        if (!flag) {
            infoBox("Please there is an error", null, "Failed");
        } else {
            infoBox("Registration Successful!", null, "Success");
            stage = (Stage) submitButtonRegister.getScene().getWindow();
            Menu menu = new Menu();
            menu.start(stage);
        }
    }



    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public void switchRegister() throws Exception {
            Stage stage = (Stage) registerTxtBtn.getScene().getWindow();
            new register().start(stage);

    }

    public void switchSignIn() throws Exception {
        Stage stage = (Stage) signInTxtBtn.getScene().getWindow();
        new Main().start(stage);
    }

}
