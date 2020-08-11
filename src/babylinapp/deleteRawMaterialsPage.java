package babylinapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class deleteRawMaterialsPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("deleteRawMaterialsPage.fxml"));
        primaryStage.setTitle("Babylin Consult | Delete Raw Materials");
        primaryStage.setScene(new Scene(root, 400, 550));
        primaryStage.show();
    }
}
