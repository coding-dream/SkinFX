package com.nono.skinfx;

import com.nono.skinfx.util.LayoutInflater;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by wl on 2019/2/14.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = LayoutInflater.inflate("main", Parent.class);
        primaryStage.setTitle("皮肤批量生成工具");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
