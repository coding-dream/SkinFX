package com.nono.skinfx;

/**
 * Created by wl on 2019/2/14.
 */
public class ColorReplace {
    private String originName;
    private String originValue;
    private String replaceValue;

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getOriginValue() {
        return originValue;
    }

    public void setOriginValue(String originValue) {
        this.originValue = originValue;
    }

    public String getReplaceValue() {
        return replaceValue;
    }

    public void setReplaceValue(String replaceValue) {
        this.replaceValue = replaceValue;
    }

    @Override
    public String toString() {
        return "ColorReplace{" +
                "originName='" + originName + '\'' +
                ", originValue='" + originValue + '\'' +
                ", replaceValue='" + replaceValue + '\'' +
                '}';
    }
}
