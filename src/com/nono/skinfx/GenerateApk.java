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
 * 单独的Skin生成对象
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

    /**
     * 复制基础工程到skin目录
     */
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

    /**
     * 生成Apk
     * @param callback
     */
    public void generate(GenerateApkManager.Callback callback) {
        this.callback = callback;

        resetBaseFiles();
        resetResFiles();
        resetColor();
        generateApk();
    }

    /**
     * 重新设置新的skin的颜色值
     */
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
            FileUtils.write(originColorPath, resultColorString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制基础项目文件 -> 并创建新的skin临时项目(并发生成Apk)
     */
    private void resetResFiles() {
        File copyResourceFolder = skinApk.getCopyResourceFolder();
        try {
            FileUtils.copyDirectory(copyResourceFolder, new File(skinApk.getProjectDir() + resFolder));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成Apk文件，要特别注意其中的坑。
     *
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
                callback.message("命令处理: " + skinApk.getName() + " 创建完成！");
                outputApkFile();

                GlobalValueManager.getInstance().decreaseTaskCount();
                if (GlobalValueManager.getInstance().getTaskCount() == 0) {
                    handleComplete();
                }
            }
        }, cmd);
    }

    /**
     * 任务完成后的回调UI处理
     */
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

    /**
     * 输出Apk文件到指定目录中
     */
    private void outputApkFile() {
        try {
            File outPutApk = new File(skinApk.getProjectDir(), "\\app\\build\\outputs\\apk\\debug\\app-debug.apk");
            if (skinApk.getDstFile().exists()) {
                boolean flag = skinApk.getDstFile().delete();
                callback.message(skinApk.getName() + " 覆盖删除: " + flag);
            }

            FileUtils.moveFile(outPutApk, skinApk.getDstFile());

            // 删除临时目录
            File tempDir = skinApk.getProjectDir();
            FileUtils.forceDelete(tempDir);
            callback.message("forceDelete: " + tempDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 换肤替换新值
     * @param key
     * @param newValue
     * @return
     */
    private GenerateApk replaceNewValue(String key, String newValue){
        String regexReplace = baseRegexString.replace("#key#", key);
        String template = String.format("<color name=\"%s\">#value#</color>", key);
        // 注意特殊字符的处理, 蛋疼的Java每次输出都会把类似\n等转为真正的换行到文本中.
        String newResult = template.replace("#value#", newValue).replace("\n","\\n");
        resultColorString = resultColorString.replaceFirst(regexReplace, newResult);
        return this;
    }
}
