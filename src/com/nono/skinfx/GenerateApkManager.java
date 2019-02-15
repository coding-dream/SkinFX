package com.nono.skinfx;

import com.github.promeg.pinyinhelper.Pinyin;
import com.nono.skinfx.bean.SkinApk;
import com.nono.skinfx.controller.MainController;
import com.nono.skinfx.executor.ArchTaskExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wl on 2019/2/15.
 */
public class GenerateApkManager {

    private String baseFolder;
    private String dstFolder;

    private List<SkinApk> resourcesList = new ArrayList<SkinApk>();
    private static final String tempDeleteDir = "D:/tempDelete";

    private Callback callback;

    public interface Callback {
        void done();
        void message(String log);
    }

    File resourcesRoot;

    public GenerateApkManager(String baseFolder, String dstFolder) {
        this.baseFolder = baseFolder;
        this.dstFolder = dstFolder;
    }

    public void generate(Callback callback) {
        this.callback = callback;
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
        generateApk.generate(callback);
    }

    private void loadParams() {
        resourcesRoot = new File(baseFolder, "files\\resources");
        for (File file : resourcesRoot.listFiles()) {
            cacheToList(file);
        }
        GlobalValueManager.getInstance().setTaskCount(resourcesList.size());
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
        skinApk.setDstFile(new File(dstFolder, name + ".skin"));
        skinApk.setCopyResourceFolder(file);
        if (name.equals("圣诞")) {
            skinApk.setFlag(SkinApk.FLAG_IMAGE);
        }

        callback.message(skinApk.toString());
        resourcesList.add(skinApk);
    }
}
