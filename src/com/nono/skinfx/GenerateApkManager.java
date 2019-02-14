package com.nono.skinfx;

import com.github.promeg.pinyinhelper.Pinyin;
import com.nono.skinfx.controller.MainController;
import com.nono.skinfx.executor.ArchTaskExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GenerateApkManager {

    private String baseFolder;
    private MainController controller;
    private List<SkinApk> resourcesList = new ArrayList<SkinApk>();
    private static final String tempDeleteDir = "D:/tempDelete";

    File resourcesRoot;

    public GenerateApkManager(String baseFolder, MainController mainController) {
        this.baseFolder = baseFolder;
        this.controller = mainController;
    }

    public void generate() {
        loadParams();
        createAllApkFolder();
    }

    private void createAllApkFolder() {
        final File tempRootDir = new File(tempDeleteDir);
        for (final SkinApk skinApk : resourcesList) {
            ArchTaskExecutor.getInstance().executeOnDiskIO(new Runnable() {
                @Override
                public void run() {
                    createSingleApkFolder(tempRootDir, skinApk);
                }
            });
        }
    }

    private void createSingleApkFolder(File tempRootDir, SkinApk skinApk) {
        GenerateApk generateApk = new GenerateApk(tempRootDir, baseFolder, skinApk);
        generateApk.generate();
    }

    private void loadParams() {
        resourcesRoot = new File(baseFolder, "files\\resources");
        for (File file : resourcesRoot.listFiles()) {
            cacheToList(file);
        }
        System.out.println(resourcesList);
    }

    private void cacheToList(File file) {
        String fileName = file.getName();
        int index = fileName.indexOf("#");
        String name = fileName.substring(0, index);
        String pinName = Pinyin.toPinyin(name, "_").toLowerCase();
        System.out.println(pinName);

        String color = fileName.substring(index);
        SkinApk skinApk = new SkinApk();
        skinApk.setName(pinName);
        skinApk.setColor(color);
        skinApk.setCopyResourceFolder(file);

        resourcesList.add(skinApk);
    }
}
