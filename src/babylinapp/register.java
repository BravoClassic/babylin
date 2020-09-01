package babylinapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class register extends Application {


    public static void main(String[] args) {
        launch(args);
    }


            @Override
            public void start(Stage stage) throws Exception {
                Parent root= FXMLLoader.load(getClass().getResource("register.fxml"));
                stage.setTitle("Babylin Consult | Register");
                stage.setMaximized(true);
                Scene scene = new Scene(root, 1080, 720);
                scene.getStylesheets().add(getClass().getResource("assets/styles.css").toExternalForm());
                stage.setScene(scene);
                stage.show();
            }
}
