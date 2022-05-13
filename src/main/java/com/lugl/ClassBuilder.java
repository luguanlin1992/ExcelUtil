package com.lugl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * @author luguanlin
 * 2022/5/11 14:45
 */
public class ClassBuilder implements JavaFastString {
    private String className;
    private String desc = "";
    public List<String[]> fields = new ArrayList<>();
    private Set<String> imps = new HashSet<>();
    private Set<String> jdkImps = new HashSet<>();
    public List<FieldInfo> fieldInfos = new ArrayList<>();
    public FieldInfo rootField;
    private JsonObject jsonObject = new JsonObject();

    public ClassBuilder(String className) {
        this.className = className;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void addField(String name, String type, String desc, String belong) {
        fields.add(new String[]{name, type, desc, belong});
    }

    public void addImp(String imp) {
        imps.add(imp);
    }

    public void addJdkImps(String imp) {
        jdkImps.add(imp);
    }

    public static String className(String filename) {
        String temp = filename.split("\\.")[0];
        String[] names = temp.split("_");
        String name = names.length == 1 ? names[0] : names[1];
        return name + "Bean";
    }

    public boolean build() {
        initFieldInfos();
        StringBuilder stringBuilder = new StringBuilder();
        String body = buildBody();
        stringBuilder.append(buildHead())
                .append(body)
                .append("}")
                .append(ENTER);
        createClass(stringBuilder);
        return true;
    }

    public void initFieldInfos() {
        fields.stream().filter(strings -> strings[3].equals("")).forEach(strings -> {
            FieldInfo fieldInfo = new FieldInfo(strings);
            fieldInfos.add(fieldInfo);
        });
        fieldInfos.forEach(fieldInfo -> fieldInfos.forEach(temp -> fieldInfo.getFriends().add(temp.getName())));
        while (!complete()) {
            fields.stream().filter(strings -> !fieldInfosContainsField(strings[0])).forEach(strings -> {
                FieldInfo fieldInfo = new FieldInfo(strings);
                FieldInfo belongTo = getFieldInfo(fieldInfo.getBelongTo());
                if (belongTo != null) {
                    fieldInfos.add(fieldInfo);
                    belongTo.getBelongs().add(fieldInfo.getName());
                    belongTo.getBelongs().forEach(name -> {
                        FieldInfo friend = getFieldInfo(name);
                        friend.setFriends(belongTo.getBelongs());
                    });
                }
            });
        }
        rootField = fieldInfos.get(0);
        for (int i = 0; i < fieldInfos.size(); i++) {
            FieldInfo fieldInfo = fieldInfos.get(i);
            if (fieldInfo.getBelongTo().equals("") && fieldInfo.getBelongs().size() > 0) {
                rootField = fieldInfo;
                break;
            }
        }
    }

    private FieldInfo getNextNode(FieldInfo fieldInfo) {
        if (fieldInfo.getBelongs().size() == 0) {
            return null;
        }
        FieldInfo result = getFieldInfo(fieldInfo.getBelongs().get(0));
        for (int i = 0; i < fieldInfo.getBelongs().size(); i++) {
            FieldInfo info = getFieldInfo(fieldInfo.getBelongs().get(i));
            if (info.getBelongs().size() > 0) {
                result = info;
            }
        }
        return result;
    }

    public FieldInfo getFieldInfo(String name) {
        return fieldInfos.stream().filter(fieldInfo -> fieldInfo.getName().equals(name)).findAny().orElse(null);
    }

    private boolean complete() {
        for (int i = 0; i < fields.size(); i++) {
            if (!fieldInfosContainsField(fields.get(i)[0])) {
                return false;
            }
        }
        return true;
    }

    private boolean fieldInfosContainsField(String name) {
        for (int i = 0; i < fieldInfos.size(); i++) {
            if (fieldInfos.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public String buildHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(GlobalVar.packageName).append(";").append(ENTER);
        sb.append(ENTER);
        sb.append(buildImport());
        sb.append("/**").append(ENTER);
        sb.append(" * ").append(desc).append(ENTER);
        sb.append(" */").append(ENTER);
        sb.append("public class ").append(className).append(" {").append(ENTER);
        return sb.toString();
    }

    public String buildImport() {
        StringBuilder sb = new StringBuilder();
        if (!imps.isEmpty()) {
            List<String> list = new ArrayList<>(imps);
            list.sort(String::compareToIgnoreCase);
            list.forEach(s -> sb.append(s).append(ENTER));
            sb.append(ENTER);
        }
        if (!jdkImps.isEmpty()) {
            List<String> list = new ArrayList<>(jdkImps);
            list.sort(String::compareToIgnoreCase);
            list.forEach(s -> sb.append(s).append(ENTER));
            sb.append(ENTER);
        }
        return sb.toString();
    }

    public String buildBody() {
        StringBuilder sb = buildField(rootField, "");
        return sb.toString();
    }

    public StringBuilder buildField(FieldInfo fieldInfo, String tabs) {
        StringBuilder fieldSb = new StringBuilder();
        StringBuilder classSb = new StringBuilder();
        StringBuilder methodSb = new StringBuilder();
        if (!fieldInfo.equals(rootField)) {
            classSb.append(tabs).append("public class ").append(className(fieldInfo.getName())).append("{").append(ENTER);
        }
        for (int i = 0; i < fieldInfo.getFriends().size(); i++) {
            buildField(getFieldInfo(fieldInfo.getFriends().get(i)), fieldSb, methodSb, tabs);
        }

        FieldInfo nextNode = getNextNode(fieldInfo);
        if (nextNode != null) {
            FieldInfo temp = new FieldInfo(nextNode.getName(), "map:" + nextNode.getType() + ":" + nextNode.getName(), nextNode.getDesc(), "");
            buildField(temp, fieldSb, methodSb, tabs);
            classSb.append(fieldSb).append(ENTER).append(methodSb);
            classSb.append(buildField(nextNode, tabs + TAB));
        } else {
            classSb.append(fieldSb).append(ENTER).append(methodSb);
        }
        if (!fieldInfo.equals(rootField)) {
            classSb.append(tabs).append("}").append(ENTER);
        }
        return classSb;
    }

    public void buildField(FieldInfo fieldInfo, StringBuilder fieldSb, StringBuilder methodSb, String tabs) {
        String typeName = fieldInfo.getType();
        String[] types = typeName.split(":");
        String type = types[0];
        String name = fieldInfo.getName();
        String desc = fieldInfo.getDesc();
        fieldSb.append(tabs).append(TAB).append("// ").append(desc).append(ENTER);
        switch (type) {
            case "int":
                break;
            case "long":
                break;
            case "bool":
                type = "boolean";
                break;
            case "str":
                type = "String";
                break;
            case "map":
                addJdkImps("import java.util.HashMap;");
                type = "HashMap<" + getInnerType(types[1]) + ", " + getInnerType(types[2]) + ">";
                break;
            case "arr":
                addJdkImps("import java.util.ArrayList;");
                type = "ArrayList<" + getInnerType(types[1]) + ">";
                break;
            default:
                type = type + "Bean";
//                    addImp("import " + GlobalVar.packageName + "." + type + ";");
                break;
        }
        methodSb.append(buildMethod(type, name, tabs));
        fieldSb.append(tabs).append(TAB).append("private ").append(type).append(" ").append(name).append(";").append(ENTER);
    }

    private String getInnerType(String s) {
        switch (s) {
            case "int":
                return "Integer";
            case "long":
                return "Long";
            case "str":
                return "String";
            default:
                return s + "Bean";
        }
    }

    public String buildMethod(String type, String name, String tabs) {
        StringBuilder sb = new StringBuilder();
        String upperFirstCharOnKey = JavaFastString.upperFirstChar(name);
        String getString = type.equals("boolean") ? "is" : "get";
        sb.append(tabs).append(TAB).append("/** ").append(getFieldInfo(name).getDesc()).append(" */").append(ENTER);
        sb.append(tabs).append(TAB).append("public " + type + " " + getString + upperFirstCharOnKey + "() {").append(ENTER);
        sb.append(tabs).append(TAB).append(TAB).append("return this." + name + ";").append(ENTER);
        sb.append(tabs).append(TAB).append("}").append(ENTER).append(ENTER);
        sb.append(tabs).append(TAB).append("public void set" + upperFirstCharOnKey + "(" + type + " " + name + ") {").append(ENTER);
        sb.append(tabs).append(TAB).append(TAB).append("this." + name + " = " + name + ";").append(ENTER);
        sb.append(tabs).append(TAB).append("}").append(ENTER).append(ENTER);
        return sb.toString();
    }

    public void addData(Map<String, String> map) {
        addData(jsonObject, map, rootField);
    }

    public void addData(JsonObject jsonObject, Map<String, String> map, FieldInfo fieldInfo) {
        if (!fieldInfo.equals(rootField)) {
            if (!jsonObject.has(fieldInfo.getName())) {
                jsonObject.add(fieldInfo.getName(), new JsonObject());
            }
            jsonObject = jsonObject.getAsJsonObject(fieldInfo.getName());
        }
        if (!jsonObject.has(map.get(fieldInfo.getName()))) {
            jsonObject.add(map.get(fieldInfo.getName()), new JsonObject());
        }
        JsonObject asJsonObject = jsonObject.getAsJsonObject(map.get(fieldInfo.getName()));
        fieldInfo.getFriends().forEach(friend -> {
            asJsonObject.addProperty(friend, map.get(friend));
        });
        FieldInfo nextNode = getNextNode(fieldInfo);
        if (nextNode != null) {
            addData(asJsonObject, map, nextNode);
        }
    }

    public void createJson() {
        String filePath = GlobalVar.jsonPath;
        File file = new File(filePath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filePath + "/" + className.replace("Bean", "") + ".json"), "UTF-8")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            osw.write(gson.toJson(jsonObject));
            osw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createClass(StringBuilder classContent) {
        String curPath = GlobalVar.packageName.replace(".", "/");
        String filePath = GlobalVar.javaPath + "/" + curPath;
        File file = new File(filePath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filePath + "/" + className + ".java"), "UTF-8")) {
            osw.write(classContent.toString());
            classContent.delete(0, classContent.length());
            osw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
