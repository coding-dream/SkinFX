package com.nono.skinfx;

import java.io.File;

public class SkinApk {

    String name;
    String color;
    File copyResourceFolder;
    File projectDir;

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
}
