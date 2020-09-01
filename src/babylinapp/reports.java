package babylinapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class reports extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("reports.fxml"));
        primaryStage.setTitle("Babylin Consult | Reports");
        primaryStage.setScene(new Scene(root, 400, 850));
        primaryStage.show();
    }
}
