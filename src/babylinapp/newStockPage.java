package babylinapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class newStockPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("newStockPage.fxml"));
        primaryStage.setTitle("Babylin Consult | Stock");
        primaryStage.setScene(new Scene(root, 400, 850));
        primaryStage.show();
    }
}
