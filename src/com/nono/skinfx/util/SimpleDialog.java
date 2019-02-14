package com.nono.skinfx.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SimpleDialog {

    Parent root;
    private Stage stage;
    private Label label;
    private String content;

    public SimpleDialog(String content) {
        root = LayoutInflater.inflate("error", Parent.class);
        this.content = content;
        label = (Label) root.lookup("#lb_tip");
        label.setText(content);
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
    }

    public void show(String title){
        Scene scene = new Scene(root);
        stage.setTitle(title);
        // stage.getIcons().add(Constants.APP_LOGO);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();

    }

    public void close(){
        stage.close();
    }
}
