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
                    if (fileName.contains(".git") || fileName.contains("files") || fileName.equals("build")) {
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
        generateApk();
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
     * gradlew.bat clean assembleDebug
     *
     * java -version
     * openjdk version "1.8.0_152-release"
     * OpenJDK Runtime Environment (build 1.8.0_152-release-1136-b06)
     * OpenJDK 64-Bit Server VM (build 25.152-b06, mixed mode)
     *
     * 在gradle.properties文件增加两行
     * android.enableAapt2=false
     * org.gradle.java.home=D:\\Program Files\\android-studio-ide-173.4697961-windows\\android-studio\\jre
     *
     * 注意此处潜藏的深坑：
     * 1. Could not generate a proxy class for class com.android.build.gradle.tasks.BuildArtifactReportTask.
     * 把gradle运行的JDK的版本更换为和Android Studio的一致（当前运行的环境如Android Studio，IDEA）
     * 因为JavaFX已运行的JDK环境会被gradle脚本复用，需修改gradle.properties文件增加一行: org.gradle.java.home = D:\\Program Files\\android-studio-ide-173.4697961-windows\\android-studio\\jre
     *
     * 2. error: failed to create directory 'D:\xx\app\build\generated\source\r\debug\com.none.ss\skin'
     * gradle.properties文件增加一行：android.enableAapt2=false
     */
    private void generateApk() {
        StringBuffer generateApkCmd = new StringBuffer();
        System.setProperty("user.dir", skinApk.getProjectDir().getAbsolutePath());

        generateApkCmd.append("cmd /c ");
        generateApkCmd.append(skinApk.getProjectDir().getAbsolutePath() + "\\");
        generateApkCmd.append(Const.CMD_GENERATE_2);
        generateApkCmd.append(" -p");
        generateApkCmd.append(" " + skinApk.getProjectDir().getAbsolutePath());
        String cmd = generateApkCmd.toString();
        System.out.println(cmd);
        RunTimeHelper.executeAndPrintLines(cmd);
    }
}
