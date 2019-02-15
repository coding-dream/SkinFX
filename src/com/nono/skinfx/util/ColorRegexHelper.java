package com.nono.skinfx.util;

import com.nono.skinfx.ColorReplace;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wl on 2019/2/15.
 */
public class ColorRegexHelper {

    private static String baseRegexString = "<color name\\s*=\\s*\"#key#\">(.+)</color>";
    private static Pattern pattern = Pattern.compile(baseRegexString);

    public static List<ColorReplace> findAllColors(String colorPath) {
        List<ColorReplace> colorReplaces = new ArrayList<ColorReplace>();
        try {
            File file = new File(colorPath);
            String html = FileUtils.readFileToString(file);
            Matcher matcher = pattern.matcher(html);
            while (matcher.find()) {
                String name = matcher.group(1);
                String value = matcher.group(2);
                ColorReplace colorReplace = new ColorReplace();
                colorReplace.setOriginName(name);
                colorReplace.setOriginValue(value);

                colorReplaces.add(colorReplace);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return colorReplaces;
    }
}
