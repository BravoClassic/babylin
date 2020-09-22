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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ResourceBundle;

public class scheduleAppointmentController implements Initializable {

    public Tab tabScheduledAppointment;
    public Tab tabScheduledAppointments;
    public TabPane tabPane;
    public Label user;
    public TableColumn<appointmentClass,LocalTime> timeColumn;
    public TableColumn<appointmentClass, Date> dateColumn;
    public TableColumn<appointmentClass, Integer> appointmentIdColumn;
    public TableColumn<appointmentClass, Integer> userIdColumn;
    private LocalTime timeSchedule;
    private LocalDateTime dateTime;
    public Label id;
    public Button schedule;
    public Button back;
    public Spinner<Integer> hourTime;
    public Spinner<Integer> minuteTime;
    public int userID;
    public DatePicker dateSchedule;
    @FXML
    private TableView<appointmentClass> appointmentTable;
    private ObservableList<appointmentClass> appointmentListTable = FXCollections.observableArrayList();

    private SpinnerValueFactory<Integer> hourValue=new SpinnerValueFactory.IntegerSpinnerValueFactory(9,17,1) ;
    private SpinnerValueFactory<Integer> minuteValue=new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,1) ;

    @FXML
    private void book() {
        if (dateSchedule.getValue() == null) {
            Controller.infoBox("Select a date!", null, "Date Error");
            return;
        }

        timeSchedule = LocalTime.of(hourTime.getValue(), minuteTime.getValue(), 0);
        dateTime = LocalDateTime.of(dateSchedule.getValue(), timeSchedule);
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dt = ft.format(Date.from(dateTime.toInstant(ZoneOffset.UTC)));

        Controller.infoBox("Time is: " + dt, null, "Time");
        try {
            Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO babylinapp_appointments VALUES(?,?,?)");
            preparedStatement.setString(1, null);
            preparedStatement.setInt(2, userID);
            preparedStatement.setString(3, dt);
            boolean resultSet = preparedStatement.execute();
            if (resultSet) {
                System.out.println("not Working");
            } else {
                System.out.println("working");
            }
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }

        try {
            appointmentTable.getItems().clear();
            Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM babylinapp_appointments WHERE userId = ?");
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                appointmentListTable.add(new appointmentClass(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getDate(3),
                        resultSet.getTime(3).toLocalTime()
                ));

                appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
                userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
                timeColumn.setCellValueFactory(new PropertyValueFactory<>("t"));
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
                appointmentTable.setItems(appointmentListTable);

            }
        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }
    @FXML
    private void deleteAppointment(){
        int deleteIndex=appointmentTable.getSelectionModel().getSelectedItem().getAppointmentId();
        try{
            Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
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

        } catch (SQLException e) {
            jdbcController.printSQLException(e);
        }
    }


    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        new Menu().start(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hourTime.setValueFactory(hourValue);
        minuteTime.setValueFactory(minuteValue);
        if (jdbcController.userType.equals("Customer")) {
            try {
                Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM babylinapp_users WHERE email = ?");
                preparedStatement.setString(1, jdbcController.emailUniversal);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    user.setText("Name: " + resultSet.getString(2));
                    id.setText("Id: " + resultSet.getString(1));
                    userID=resultSet.getInt(1);
                }
            } catch (SQLException e) {
                jdbcController.printSQLException(e);
            }

            try{
                Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM babylinapp_appointments WHERE userId = ?");
                preparedStatement.setInt(1, userID);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                   appointmentListTable.add(new appointmentClass(
                           resultSet.getInt(1),
                           resultSet.getInt(2),
                           resultSet.getDate(3),
                           resultSet.getTime(3).toLocalTime()
                   ));

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("t"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        appointmentTable.setItems(appointmentListTable);
                }
            } catch (SQLException e) {
                jdbcController.printSQLException(e);
            }
        }
    }
}

