package babylinapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class updateStockPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("updateStockPage.fxml"));
        primaryStage.setTitle("Babylin Consult | Update Stock");
        primaryStage.setScene(new Scene(root, 400, 550));
        primaryStage.show();
    }
}
