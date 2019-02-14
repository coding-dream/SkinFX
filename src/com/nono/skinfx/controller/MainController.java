package com.nono.skinfx.controller;

import com.nono.skinfx.GenerateApkManager;
import com.nono.skinfx.util.SimpleDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TextField et_base;
    @FXML
    private TextField et_dst;
    @FXML
    private TextArea tv_log;

    @FXML
    private Button btn_confirm;

    private StringBuffer logBuffer = new StringBuffer();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mockData();
    }

    public void generateApk(ActionEvent actionEvent) {
        String baseFolder = et_base.getText();
        String dstFolder = et_dst.getText();

        if (baseFolder.equals("") || dstFolder.equals("")) {
            SimpleDialog simpleDialog = new SimpleDialog("Sorry，输入内容不能为空！");
            simpleDialog.show("错误提示");
            return;
        }
        GenerateApkManager generateApkManager = new GenerateApkManager(baseFolder, this);
        generateApkManager.generate();
    }

    private void mockData() {
        String data = "D:\\Codes\\Work\\nn_android_skinpack";
        File originData = new File(data);
        et_base.setText(originData.getAbsolutePath());
        et_dst.setText(originData.getAbsolutePath() + "\\temp");
    }

    public void log(String log) {
        logBuffer.append(log).append("\r\n");
        tv_log.setText(logBuffer.toString());
    }
}
