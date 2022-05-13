package com.lugl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;

/**
 * @author luguanlin
 * 2022/5/11 11:23
 */
public class GlobalVar {
    public static String excelPath = "";
    public static String jsonPath = "";
    public static String javaPath = "";

    public static String packageName = "com.lugl.data";

    public static void initData() {
        JsonObject jsonObject;
        String path = System.getProperty("user.dir");
        excelPath = path;
        jsonPath = path + "/data";
        javaPath = path + "/java";
        File file = new File(path + "/config.txt");
        if (file.exists()) {
            try {
                Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
                Gson gson = new Gson();
                jsonObject = gson.fromJson(reader, JsonObject.class);
                if (jsonObject.has("excelRoot")) {
                    GlobalVar.excelPath = jsonObject.get("excelRoot").getAsString();
                }
                if (jsonObject.has("jsonRoot")) {
                    GlobalVar.jsonPath = jsonObject.get("jsonRoot").getAsString();
                }
                if (jsonObject.has("javaRoot")) {
                    GlobalVar.javaPath = jsonObject.get("javaRoot").getAsString();
                }
                if (jsonObject.has("packageName")) {
                    packageName = jsonObject.get("packageName").getAsString();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close() {
        JsonObject jsonObject = new JsonObject();
        String path = System.getProperty("user.dir");
        jsonObject.addProperty("excelRoot", excelPath);
        jsonObject.addProperty("jsonRoot", jsonPath);
        jsonObject.addProperty("javaRoot", javaPath);
        jsonObject.addProperty("packageName", packageName);
        File file = new File(path + "/config.txt");
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            osw.write(gson.toJson(jsonObject));
            osw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
