package com.nono.skinfx.bean;

import java.io.File;

/**
 * Created by wl on 2019/2/14.
 */
public class SkinApk {

    public static final int FLAG_NOMAL = 1;
    public static final int FLAG_IMAGE = 2;

    private String name;
    private String color;
    private File copyResourceFolder;
    private File projectDir;
    private File dstFile;

    private int flag = FLAG_NOMAL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "SkinApk{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", copyResourceFolder=" + copyResourceFolder +
                '}';
    }

    public File getDstFile() {
        return dstFile;
    }

    public void setDstFile(File dstFile) {
        this.dstFile = dstFile;
    }

    public File getProjectDir() {
        return projectDir;
    }

    public void setProjectDir(File projectDir) {
        this.projectDir = projectDir;
    }

    public File getCopyResourceFolder() {
        return copyResourceFolder;
    }

    public void setCopyResourceFolder(File copyResourceFolder) {
        this.copyResourceFolder = copyResourceFolder;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
