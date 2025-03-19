import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;

public class Starter extends Application {
    public static void main(String[] args) {
     launch();
    }
    @Override
    public void start(Stage stage) throws Exception {


        Parent root = FXMLLoader.load(getClass().getResource("view/LoginForm.fxml"));

        Scene scene = new Scene(root);
        scene.setFill(Color.BLUE);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("SmartLibraryX");
    }
}
