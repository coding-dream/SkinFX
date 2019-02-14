package com.nono.skinfx;

import com.nono.skinfx.util.cmd.RunTimeHelper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

public class GenerateApk {

    private String baseFolder;
    private File tempRootDir;
    private SkinApk skinApk;

    private String resFolder = "\\app\\src\\main\\res\\drawable-xhdpi\\";
    private String colorPath = "\\app\\src\\main\\res\\values\\colors.xml";

    private static final String regexReplace = "";

    public GenerateApk(File tempRootDir, String baseFolder, SkinApk skinApk) {
        this.baseFolder = baseFolder;
        this.tempRootDir = tempRootDir;
        this.skinApk = skinApk;
        File projectDir = new File(tempRootDir, skinApk.name);
        this.skinApk.setProjectDir(projectDir);
    }

    private void resetBaseFiles() {
        try {
            FileUtils.copyDirectory(new File(baseFolder), skinApk.projectDir, new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    String fileName = pathname.getName();
                    if (fileName.contains(".git") || fileName.contains("files")) {
                        return false;
                    }
                    return true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generate() {
        resetBaseFiles();
        resetResFiles();
        resetColor();
        // generateApk();
    }

    private void resetColor() {
        File colorXml = new File(skinApk.getProjectDir() + colorPath);
        // 正则替换
    }

    private void resetResFiles() {
        File copyResourceFolder = skinApk.getCopyResourceFolder();
        try {
            FileUtils.copyDirectory(copyResourceFolder, new File(skinApk.getProjectDir() + resFolder));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * gradlew.bat clean
     * gradlew.bat assembleDebug
     */
    private void generateApk() {
        StringBuffer generateApkCmd = new StringBuffer();
        generateApkCmd.append("cmd /c ");
        generateApkCmd.append(skinApk.getProjectDir().getAbsolutePath() + "\\");
        generateApkCmd.append(Const.CMD_GENERATE_2);
        generateApkCmd.append(" " + skinApk.getProjectDir().getAbsolutePath());
        String cmd = generateApkCmd.toString();
        System.out.println(cmd);
        RunTimeHelper.executeAndPrintLines(cmd);
    }
}
