package com.lugl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luguanlin
 * 2022/5/12 10:08
 */
public class FieldInfo {
    private String name;
    private String type;
    private String desc;
    private String belongTo;
    private List<String> belongs = new ArrayList<>();
    private List<String> friends = new ArrayList<>();

    public FieldInfo(String name, String type, String desc, String belongTo) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.belongTo = belongTo;
    }

    public FieldInfo(String[] strings) {
        if (strings.length != 4) {
            System.exit(1);
        }
        this.name = strings[0];
        this.type = strings[1];
        this.desc = strings[2];
        this.belongTo = strings[3];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    public List<String> getBelongs() {
        return belongs;
    }

    public void setBelongs(List<String> belongs) {
        this.belongs = belongs;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }
}
