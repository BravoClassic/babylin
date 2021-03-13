package babylinapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Pattern;

public class Controller {
    @FXML
    private BorderPane imageBorderPane;
    @FXML
    private AnchorPane main;
    @FXML
    private ComboBox<String> u_type_register;
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
    private TextField lnRegister;

    @FXML
    private DatePicker DOB;

    @FXML
    private ComboBox<String> u_type_login;

    @FXML
    private Button submitButtonRegister;

    @FXML
    private TextField address;

    @FXML
    private TextField number;

    @FXML
    private Label registerTxtBtn;

    @FXML
    private Label signInTxtBtn;


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
        if (u_type_login.getValue() == null) {
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

    @FXML
    private void register() throws IOException {
        Stage stage;
        Window owner = submitButtonRegister.getScene().getWindow();
        String fn = fnRegister.getText();
        String ln = lnRegister.getText();
        LocalDate dob = DOB.getValue();
        String us_type = u_type_register.getValue();
        String addr = address.getText();
        String phone = number.getText();
        String email = emailIdFieldRegister.getText();
        String pwd = passwordFieldRegister.getText();
        String pwd1 = passwordFieldRegister1.getText();
        Period period = Period.between(dob, LocalDate.now());
        //Checks if inputs are empty
        if (fnRegister.getText().equals("")) {
            showAlert(Alert.AlertType.ERROR, owner, "FORM ERROR!", "Please Enter Your First Name");
            return;
        }

        if (lnRegister.getText().equals("")) {
            showAlert(Alert.AlertType.ERROR, owner, "FORM ERROR!", "Please Enter Your Last Name");
            return;
        }

        if (DOB.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, owner, "FORM ERROR!", "Please Enter Your Date of Birth");
            return;
        }
        if (period.getYears() < 13) {
            showAlert(Alert.AlertType.ERROR, owner, "FORM ERROR!", "Must be 13years or above to sign up.");
            return;
        }
        if (Date.from(Instant.now()).compareTo((java.sql.Date.from(DOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()))) < 0) {
            showAlert(Alert.AlertType.ERROR, owner, "FORM ERROR!", "Enter the right date of birth");
            return;
        }
        if (u_type_register.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, owner, "FORM ERROR!", "Please Enter Your User Type");
            return;
        }
        if (emailIdFieldRegister.getText().equals("")) {
            showAlert(Alert.AlertType.ERROR, owner, "FORM ERROR!", "Please Enter Your Email");
            return;
        }
        if (passwordFieldRegister.getText().equals("")) {
            showAlert(Alert.AlertType.ERROR, owner, "FORM ERROR!", "Please Enter Your Password");
            return;
        }
        if (passwordFieldRegister1.getText().equals("")) {
            showAlert(Alert.AlertType.ERROR, owner, "FORM ERROR!", "Please Enter Your Password");
            return;
        }

        if (!validEmail(emailIdFieldRegister.getText())) {
            showAlert(Alert.AlertType.ERROR, owner, "FORM ERROR!", "Please Enter A Valid Email!");
            return;
        }

        if (pwd.equals(pwd1)) {
            System.out.println(passwordFieldRegister.getText());
            System.out.println(DOB.getValue().toString());
        } else {
            showAlert(Alert.AlertType.ERROR, owner, "FORM ERROR!", "Please Enter Valid Password");
            return;
        }
        jdbcController jdbcController = new jdbcController();
        boolean flag = jdbcController.register(fn, ln, dob, us_type, addr, phone, email, pwd, pwd1);

        if (!flag) {
            infoBox("User registered already", null, "Failed");
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

    public static boolean validEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static void errBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        TextArea txt = new TextArea();
        txt.setText(infoMessage);
        alert.getDialogPane().setExpandableContent(txt);
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
