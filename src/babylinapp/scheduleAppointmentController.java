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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class scheduleAppointmentController implements Initializable {

    @FXML
    private Button back1;
    @FXML
    private Tab tabScheduledAppointment;
    @FXML
    private Tab tabScheduledAppointments;
    @FXML
    private TabPane tabPane;
    @FXML
    private Label user;
    @FXML
    private TableColumn<appointmentClass,String> timeColumn;
    @FXML
    private TableColumn<appointmentClass, Date> dateColumn;
    @FXML
    private TableColumn<appointmentClass, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<appointmentClass, Integer> userIdColumn;
    @FXML
    private Label id;
    @FXML
    private Button schedule;
    @FXML
    private Button back;
    @FXML
    private ComboBox<String> timeBox;
    public int userID;
    @FXML
    private DatePicker dateSchedule;
    @FXML
    private TableView<appointmentClass> appointmentTable;
    private ObservableList<appointmentClass> appointmentListTable = FXCollections.observableArrayList();



    private boolean checkAppointment(){
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM babylinapp_appointments WHERE userId=? and DateBooked=? ");
            preparedStatement.setInt(1, userID);
            preparedStatement.setString(2, String.valueOf(dateSchedule.getValue()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getInt(1) == 1) {
                    Controller.infoBox("Change Date! Date already booked", null, "Error");
                    return true;
                }
            }
            preparedStatement.close();
            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            Controller.errBox(e.getMessage()+"\n"+e.getLocalizedMessage()+"\n","Error with generating reports","Error");
        }

        try {
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM babylinapp_appointments WHERE DateBooked=? and Time=?");
            preparedStatement.setString(1, String.valueOf(dateSchedule.getValue()));
            preparedStatement.setString(2,timeBox.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getInt(1) == 1) {
                    Controller.infoBox("Change Time! Time already booked", null, "Error");
                    return true;
                }
            }
            preparedStatement.close();
            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
        return false;
    }

    @FXML
    private void book() {
        if (timeBox.getValue().equals("")) {
            Controller.infoBox("Select a date!", null, "Date Error");
            return;
        }
        if(dateSchedule.getValue() == null){
            Controller.infoBox("Select a date!", null, "Date Error");
            return;
        }
        if(Date.from(Instant.now()).compareTo(Date.from(dateSchedule.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())) > 0) {
            Controller.showAlert(Alert.AlertType.ERROR,schedule.getScene().getWindow(),"Error Date!","Select a today or a after.");
            return;
        }
        boolean appoint = checkAppointment();
        if (appoint){
//            System.out.println("Hey there!");
            return;
        }

        try {
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO babylinapp_appointments VALUES(?,?,?,?)");
            preparedStatement.setString(1, null);
            preparedStatement.setInt(2, userID);
            preparedStatement.setString(3, String.valueOf(dateSchedule.getValue()));
            preparedStatement.setString(4,timeBox.getValue());
            boolean resultSet = preparedStatement.execute();
            preparedStatement.close();
            connection.close();
            if (resultSet) {
                Controller.infoBox("Appointment not booked!",null,"Error");
            } else {
                Controller.infoBox("Appointment booked!",null,"Success");
            }

        } catch (SQLException e) {
            Controller.errBox(e.getMessage()+"\n"+e.getLocalizedMessage()+"\n","Error with generating reports","Error");
        }

        try {
            appointmentTable.getItems().clear();
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM babylinapp_appointments WHERE userId = ?");
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                java.util.Date dateResult=  new SimpleDateFormat("yyyy-MM-dd").parse(resultSet.getString(3));
                appointmentListTable.add(new appointmentClass(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        dateResult,
                        resultSet.getString(4)
                ));
                appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
                userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
                timeColumn.setCellValueFactory(new PropertyValueFactory<>("t"));
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
                appointmentTable.setItems(appointmentListTable);

            }
            preparedStatement.close();
            resultSet.close();
            connection.close();
        } catch (SQLException | ParseException e) {
            Controller.errBox(e.getMessage()+"\n"+e.getLocalizedMessage()+"\n","Error with generating reports","Error");
        }
    }
    @FXML
    private void deleteAppointment(){
        if (appointmentTable.getSelectionModel().getFocusedIndex() == -1){
            Controller.infoBox("Select an appointment to delete.",null,"Error");
        }
        int deleteIndex=appointmentTable.getSelectionModel().getSelectedItem().getAppointmentId();
        try{
            Connection connection = DriverManager.getConnection(jdbcController.url);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM babylinapp_appointments WHERE appointmentId = ?");
            preparedStatement.setInt(1,deleteIndex);
            boolean res = preparedStatement.execute();
            if (res){
                System.out.println("not working");
                Controller.infoBox("Not Deleted",null,"Delete Appointment");
            }else {
                System.out.println("Working");
                Controller.infoBox("Deleted",null,"Delete Appointment");
                appointmentTable.getItems().remove(appointmentTable.getSelectionModel().getSelectedIndex());
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            Controller.errBox(e.getMessage()+"\n"+e.getLocalizedMessage()+"\n","Error with generating reports","Error");
        }
    }


    @FXML
    private void goBack() throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        new Menu().start(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (jdbcController.userType.equals("Customer")) {
            try {
                Connection connection = DriverManager.getConnection(jdbcController.url);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM babylinapp_users WHERE email = ?");
                preparedStatement.setString(1, jdbcController.emailUniversal);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    user.setText("Name: " + resultSet.getString(2));
                    id.setText("Id: " + resultSet.getString(1));
                    userID=resultSet.getInt(1);
                }
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                Controller.errBox(e.getMessage()+"\n"+e.getLocalizedMessage()+"\n","Error with generating reports","Error");
            }

            try{
                Connection connection = DriverManager.getConnection(jdbcController.url);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM babylinapp_appointments WHERE userId = ?");
                preparedStatement.setInt(1, userID);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    java.util.Date dateResult= new SimpleDateFormat("yyyy-MM-dd").parse(resultSet.getString(3));
                    appointmentListTable.add(new appointmentClass(
                           resultSet.getInt(1),
                           resultSet.getInt(2),
                          dateResult,
                           resultSet.getString(4)
                   ));

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("t"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        appointmentTable.setItems(appointmentListTable);
                }
                preparedStatement.close();
                resultSet.close();
                connection.close();
            } catch (SQLException | ParseException e) {
                Controller.errBox(e.getMessage()+"\n"+e.getLocalizedMessage()+"\n","Error with generating reports","Error");
            }
        } else if (jdbcController.userType.equals("Employee")) {
            tabScheduledAppointment.setDisable(true);
            try{
                Connection connection = DriverManager.getConnection(jdbcController.url);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM babylinapp_appointments");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    java.util.Date dateResult= new SimpleDateFormat("yyyy-MM-dd").parse(resultSet.getString(3));
                    appointmentListTable.add(new appointmentClass(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        dateResult,
                        resultSet.getString(4)
                ));

                appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
                userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
                timeColumn.setCellValueFactory(new PropertyValueFactory<>("t"));
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
                appointmentTable.setItems(appointmentListTable);
                }
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException | ParseException e) {
                Controller.errBox(e.getMessage()+"\n"+e.getLocalizedMessage()+"\n","Error with generating reports","Error");
            }
        }
    }
}

