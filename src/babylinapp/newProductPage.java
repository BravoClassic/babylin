package babylinapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class newProductPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("newProductPage.fxml"));
        primaryStage.setTitle("Babylin Consult | New Product");
        primaryStage.setScene(new Scene(root, 300, 700));
        primaryStage.show();
    }
}
