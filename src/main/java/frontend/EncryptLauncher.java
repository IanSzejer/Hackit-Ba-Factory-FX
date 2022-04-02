package frontend;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class EncryptLauncher extends Application {
    File selectedFile;
    Label selectedFileLabel=new Label("");
    public static void main(String[] args) {
        launch(args);
    }
    public void changeSelectedFile(File file) {
        selectedFile=file;
        selectedFileLabel.setText(file.getName());

    }


    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Encryptor");
        Button decryptButton=new Button();
        decryptButton.setLayoutX(135);
        decryptButton.setLayoutY(240);
        decryptButton.setText("decrypt");
        //encryptButton.setOnAction(decrypt);
        Button encryptButton=new Button();
        encryptButton.setLayoutX(75);
        encryptButton.setLayoutY(100);
        encryptButton.setText("encrypt");
        //encryptButton.setOnAction(encrypt);
        Button fileChooserButton = new Button("Select File");
        fileChooserButton.setLayoutX(50);
        fileChooserButton.setLayoutY(50);
        FileChooser fileChooser=new FileChooser();
        fileChooserButton.setOnAction(e -> {
            changeSelectedFile(fileChooser.showOpenDialog(stage));
        });
        Button directoryChooserButton=new Button("select directory");
        directoryChooserButton.setLayoutX(150);
        directoryChooserButton.setLayoutY(50);
        DirectoryChooser directoryChooser=new DirectoryChooser();
        directoryChooserButton.setOnAction(e -> {
            changeSelectedFile(directoryChooser.showDialog(stage));
        });
        selectedFileLabel.setLayoutX(140);
        selectedFileLabel.setLayoutY(20);
        Label text = new Label("Selected file:");
        text.setLayoutX(70);
        text.setLayoutY(20);
        TextField keyInput= new TextField();
        keyInput.setLayoutX(100);
        keyInput.setLayoutY(200);
        Label keyInputLabel = new Label("enter decrypt key");
        keyInputLabel.setLayoutX(0);
        keyInputLabel.setLayoutY(200);
        CheckBox seedCheck=new CheckBox();
        seedCheck.setText("I have my previous seed");
        Pane layout=new Pane(encryptButton,seedCheck,fileChooserButton,keyInput,keyInputLabel,decryptButton,directoryChooserButton,text,selectedFileLabel);
        Scene scene= new Scene(layout,350,350);
        stage.setScene(scene);
        stage.show();




    }
}


