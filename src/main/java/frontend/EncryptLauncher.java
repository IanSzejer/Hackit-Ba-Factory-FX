package frontend;
import backend.Encripting_tools.Encription;
import backend.Encripting_tools.EncriptionHandler;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class EncryptLauncher extends Application {
    File selectedFile=null;
    Label selectedFileLabel=new Label("no selected file");
    public static void main(String[] args) {
        launch(args);
    }
    public void changeSelectedFile(File file) {
        selectedFile=file;
        selectedFileLabel.setText("Selected file:"+file.getName());

    }


    @Override
    public void start(Stage stage) throws Exception {
        EncriptionHandler encriptionHandler=new EncriptionHandler();
        stage.setTitle("Encryptor");
        selectedFileLabel.setLayoutX(90);
        selectedFileLabel.setLayoutY(30);
        Button decryptButton=new Button();
        decryptButton.setLayoutX(135);
        decryptButton.setLayoutY(240);
        decryptButton.setText("decrypt");

        Button encryptButton=new Button();
        encryptButton.setLayoutX(135);
        encryptButton.setLayoutY(130);
        encryptButton.setText("encrypt");

        Button fileChooserButton = new Button("Select File");
        fileChooserButton.setLayoutX(110);
        fileChooserButton.setLayoutY(75);
        FileChooser fileChooser=new FileChooser();
        fileChooserButton.setOnAction(e -> {
            changeSelectedFile(fileChooser.showOpenDialog(stage));
        });
        Button directoryChooserButton=new Button("Select directory");
        directoryChooserButton.setLayoutX(210);
        directoryChooserButton.setLayoutY(75);
        DirectoryChooser directoryChooser=new DirectoryChooser();
        directoryChooserButton.setOnAction(e -> {
            changeSelectedFile(directoryChooser.showDialog(stage));
        });
        Label userSeed=new Label("");
        TextField codeInput= new TextField();
        codeInput.setLayoutX(100);
        codeInput.setLayoutY(200);
        Label decryptStatus=new Label("");
        decryptStatus.setLayoutX(100);
        decryptStatus.setLayoutY(300);
        Label codeInputLabel = new Label("enter code:");
        codeInputLabel.setLayoutX(0);
        codeInputLabel.setLayoutY(200);
        CheckBox seedCheck=new CheckBox();
        seedCheck.setText("I have my previous seed");
        seedCheck.setLayoutX(90);
        seedCheck.setLayoutY(110);
        decryptButton.setOnAction(actionEvent -> {
            if(selectedFile==null)
                decryptStatus.setText("no file selected");
            else if(encriptionHandler.decrypt(selectedFile,codeInput.getText()))
                decryptStatus.setText("Succesfully decrypted");
            else decryptStatus.setText("wrong code");
        });

        TextArea textArea = new TextArea("" );
        textArea.setEditable(false);
        textArea.setWrapText(true);
        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(textArea, 0, 0);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Seed");
        alert.getDialogPane().setContent(gridPane);
        encryptButton.setOnAction(actionEvent -> {
            if(selectedFile==null)
                userSeed.setText("no file selected");
            else if(seedCheck.isSelected())encriptionHandler.encrypt(selectedFile,false);
            else{
                textArea.setText("your seed is: " +encriptionHandler.encrypt(selectedFile,true)+" put this in te app and save it");

                alert.showAndWait();
                textArea.setText("");}

        });
        Pane layout=new Pane(encryptButton,seedCheck,fileChooserButton,codeInput,codeInputLabel,decryptButton,directoryChooserButton,selectedFileLabel,userSeed,decryptStatus);
        Scene scene= new Scene(layout,350,350);
        stage.setScene(scene);
        stage.hide();
        stage.show();




    }
}


