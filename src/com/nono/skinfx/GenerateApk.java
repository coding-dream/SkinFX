package com.nono.skinfx;

import com.nono.skinfx.bean.SkinApk;
import com.nono.skinfx.util.SimpleDialog;
import com.nono.skinfx.util.cmd.RunTimeHelper;
import javafx.application.Platform;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

/**
 * Created by wl on 2019/2/14.
 */
public class GenerateApk {

    private String baseFolder;
    private SkinApk skinApk;

    private String resFolder = "\\app\\src\\main\\res\\drawable-xhdpi\\";
    private String colorPath = "\\app\\src\\main\\res\\values\\colors.xml";

    static String baseRegexString = "<color name\\s*=\\s*\"#key#\">(.+)</color>";

    private String color_theme_background_color = "color_theme_background_color";
    private String color_title_bar_bottom_separator = "color_title_bar_bottom_separator";
    private String color_home_title_bg = "color_home_title_bg";

    private String resultColorString;
    private GenerateApkManager.Callback callback;

    public GenerateApk(File tempRootDir, String baseFolder, SkinApk skinApk) {
        this.baseFolder = baseFolder;
        this.skinApk = skinApk;
        File projectDir = new File(tempRootDir, skinApk.getName());
        this.skinApk.setProjectDir(projectDir);
    }

    private void resetBaseFiles() {
        try {
            FileUtils.copyDirectory(new File(baseFolder), skinApk.getProjectDir(), new FileFilter() {
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

    public void generate(GenerateApkManager.Callback callback) {
        this.callback = callback;

        resetBaseFiles();
        resetResFiles();
        resetColor();
        generateApk();
    }

    private void resetColor() {
        try {
            String colorResPath = skinApk.getProjectDir() + colorPath;
            File originColorPath = new File(colorResPath);

            resultColorString = FileUtils.readFileToString(originColorPath);

            replaceNewValue(color_theme_background_color, skinApk.getColor());
            // todo 优化：策略模式
            if (skinApk.getFlag() == SkinApk.FLAG_NOMAL) {
                replaceNewValue(color_title_bar_bottom_separator, skinApk.getColor());
                replaceNewValue(color_home_title_bg, skinApk.getColor());
            } else {
                replaceNewValue(color_title_bar_bottom_separator, "#00000000");
                replaceNewValue(color_home_title_bg, "#00000000");
            }
            System.out.println(resultColorString);
            FileUtils.write(originColorPath, resultColorString);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        RunTimeHelper.executeAndPrintLines(new RunTimeHelper.Callback() {
            @Override
            public void done() {
                outputApkFile();

                GlobalValueManager.getInstance().decreaseTaskCount();
                if (GlobalValueManager.getInstance().getTaskCount() == 0) {
                    handleComplete();
                }
            }
        }, cmd);
    }

    private void handleComplete() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.done();
                }
                SimpleDialog simpleDialog = new SimpleDialog("任务完成！");
                simpleDialog.show("提示");
            }
        });
    }

    private void outputApkFile() {
        try {
            File outPutApk = new File(skinApk.getProjectDir(), "\\app\\build\\outputs\\apk\\debug\\app-debug.apk");
            FileUtils.moveFile(outPutApk, skinApk.getDstFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GenerateApk replaceNewValue(String key, String newValue){
        String regexReplace = baseRegexString.replace("#key#", key);
        String template = String.format("<color name=\"%s\">#value#</color>", key);
        System.out.println("regexReplace: " + regexReplace + " template: " + template);
        // 注意特殊字符的处理, 蛋疼的Java每次输出都会把类似\n等转为真正的换行到文本中.
        String newResult = template.replace("#value#", newValue).replace("\n","\\n");
        resultColorString = resultColorString.replaceFirst(regexReplace, newResult);
        return this;
    }
}
