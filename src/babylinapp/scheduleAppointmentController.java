package babylinapp;

import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.Timer;

public class scheduleAppointmentController {

    public Tab tabScheduledAppointment;
    public Tab tabScheduledAppointments;
    public TabPane tabPane;
    public Spinner<Timer> time;

//    private void initUI() {
//        VBox vbox = new VBox(20);
//        vbox.setStyle("-fx-padding: 10;");
//        Scene scene = new Scene(vbox, 400, 400);
//        stage.setScene(scene);
//        checkInDatePicker = new DatePicker();
//        checkOutDatePicker = new DatePicker();
//        checkInDatePicker.setValue(LocalDate.now());
//        final Callback<DatePicker, DateCell> dayCellFactory =
//                new Callback<DatePicker, DateCell>() {
//                    @Override
//                    public DateCell call(final DatePicker datePicker) {
//                        return new DateCell() {
//                            @Override
//                            public void updateItem(LocalDate item, boolean empty) {
//                                super.updateItem(item, empty);
//
//                                if (item.isBefore(
//                                        checkInDatePicker.getValue().plusDays(1))
//                                ) {
//                                    setDisable(true);
//                                    setStyle("-fx-background-color: #ffc0cb;");
//                                }
//                            }
//                        };
//                    }
//                };
//        checkOutDatePicker.setDayCellFactory(dayCellFactory);
//        checkOutDatePicker.setValue(checkInDatePicker.getValue().plusDays(1));
//        GridPane gridPane = new GridPane();
//        gridPane.setHgap(10);
//        gridPane.setVgap(10);
//        Label checkInlabel = new Label("Check-In Date:");
//        gridPane.add(checkInlabel, 0, 0);
//        GridPane.setHalignment(checkInlabel, HPos.LEFT);
//        gridPane.add(checkInDatePicker, 0, 1);
//        Label checkOutlabel = new Label("Check-Out Date:");
//        gridPane.add(checkOutlabel, 0, 2);
//        GridPane.setHalignment(checkOutlabel, HPos.LEFT);
//        gridPane.add(checkOutDatePicker, 0, 3);
//        vbox.getChildren().add(gridPane);
//    }
//}
}
