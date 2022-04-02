package frontend;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.canvas.*;




public class AppLauncher extends Application {

    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage stage) throws Exception {
    stage.setTitle("panatalla principal");
    Button button=new Button();
    button.setText("encrypt");
    }
}


