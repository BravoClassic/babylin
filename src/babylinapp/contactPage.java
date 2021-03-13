package babylinapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class contactPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("contactPage.fxml"));
        primaryStage.setTitle("Babylin Consult | Contacts Page");
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setResizable(false);
        scene.getStylesheets().add(getClass().getResource("assets/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
