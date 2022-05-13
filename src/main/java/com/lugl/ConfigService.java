package com.lugl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lugl.data.MyTBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * @author luguanlin
 * 2022/5/12 17:12
 */
public class ConfigService {
    public static String path = "";
    private static final Map<String, String> FILE_PATH_MAP = new HashMap<>();
    private static final Map<String, Long> FILE_MODIFY_MAP = new HashMap<>();
    private static final Map<String, Map<Object, Object>> DATA_MAP = new HashMap<>();

    public static void main(String[] args) {
        init(System.getProperty("user.dir") + "/data");

        MyTBean jsonData = getJsonData(1, MyTBean.class);
        System.out.println(new Gson().toJson(jsonData));
        System.out.println(jsonData.getId());
        System.out.println(jsonData.getDescid());
        System.out.println(jsonData.getK2().get(1).getK3().get(3000000000L).getResult());
    }

    public static String className(String fileName) {
        String result = fileName.substring(0, 1).toUpperCase() + fileName.substring(1);
        result = GlobalVar.packageName + "." + result.replace(".json", "Bean");
        return result;
    }

    public static void init(String jsonPath) {
        path = jsonPath;
        FILE_PATH_MAP.clear();
        FILE_MODIFY_MAP.clear();
        DATA_MAP.clear();
        File rootFile = new File(path);
        Arrays.stream(Objects.requireNonNull(rootFile.listFiles((dir, name) -> name.endsWith(".json")))).forEach(file -> {
            FILE_PATH_MAP.put(className(file.getName()), file.getPath());
        });
        FILE_PATH_MAP.keySet().forEach(ConfigService::parseJsonTable);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getJsonData(int id, Class<? extends T> className) {
        Map<Object, Object> list = DATA_MAP.get(className.getName());
        if (list == null || !list.containsKey(id)) return null;
        return (T) list.get(id);
    }

    @SuppressWarnings("unchecked")
    public static <T> Map<Object, T> getJsonTable(Class<? extends T> className) {
        return (Map<Object, T>) DATA_MAP.get(className.getName());
    }

    private static void parseJsonTable(String key) {
        try {
            File file = new File(FILE_PATH_MAP.get(key));
            Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            packageJson(reader, key);
            FILE_MODIFY_MAP.put(key, file.lastModified());
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
//            log.error("key=" + key, e);
        }

    }

    private static void packageJson(Reader reader, String key) {
        try {
            Gson gson = new Gson();
            Class onwClass;
            try {
                onwClass = Class.forName(key);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            HashMap<Object, Object> list = new HashMap<>();
            JsonObject jsonobject = gson.fromJson(reader, JsonObject.class);
            Set var2 = jsonobject.entrySet();
            Object[] var3 = var2.toArray();
            for (Object aVar3 : var3) {
                Map.Entry tmp = (Map.Entry) aVar3;
                try {
                    Object item = gson.fromJson(tmp.getValue().toString(), onwClass);
                    list.put(Integer.parseInt((String) tmp.getKey()), item);
                } catch (Exception e) {
                    e.printStackTrace();
//                    log.error("load json data error,key = " + key + ",value = " + tmp.getValue().toString(), e);
                }
            }
            DATA_MAP.put(key, list);
        } catch (Throwable e) {
            e.printStackTrace();
//            log.error("load json data key:" + key, e);
        }
    }

    public static void checkDataModify() {
        try {
            if (FILE_PATH_MAP.isEmpty()) return;
            Iterator<Map.Entry<String, String>> iterator = FILE_PATH_MAP.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                String filePath = next.getValue();
                File file = new File(filePath);
                if (!file.exists()) continue;
                Long modifyTime = FILE_MODIFY_MAP.get(next.getKey());
                if (modifyTime == null) {
                    FILE_MODIFY_MAP.put(next.getKey(), file.lastModified());
                } else if (modifyTime != file.lastModified()) {
                    try (Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-8")) {
                        packageJson(reader, next.getKey());
//                        log.info("reload json data key=" + next.getKey() + "");
                    } catch (Exception e) {
//                        log.error("reload fail file name=" + resource.getFilename(), e);
                    }
                    FILE_MODIFY_MAP.put(next.getKey(), file.lastModified());
                }
            }
        } catch (Exception e) {
//            log.error("checkDataModify", e);
        }
    }
}
