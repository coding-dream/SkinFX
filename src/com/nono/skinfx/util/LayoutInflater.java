package com.nono.skinfx.util;

import java.net.URL;

import javafx.fxml.FXMLLoader;

/**
 * Created by wl on 2019/2/14.
 */
public class LayoutInflater {

	public static <T>T inflate(String viewName,Class<T> clazz){
		try {
			String filePath = "layout/" + viewName + ".fxml";
			String url = LayoutInflater.class.getClassLoader().getResource(filePath).toExternalForm();
			T t = FXMLLoader.load(new URL(url));
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}