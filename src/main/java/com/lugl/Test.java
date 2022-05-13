package com.lugl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lugl.data.MyTBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luguanlin
 * 2022/5/11 11:43
 */
public class Test {
    public static void main(String[] args) {
        Map<Integer, MyTBean> map = new HashMap<>();
        MyTBean myTBean = new MyTBean();
        myTBean.setId(1);
        myTBean.setDescid("desc1");
        myTBean.setK2(new HashMap<>());
        myTBean.getK2().put(1, myTBean.new k2Bean());
        myTBean.getK2().get(1).setK2(1);
        myTBean.getK2().get(1).setDesc2("desc2");
        map.put(myTBean.getId(), myTBean);
        Gson gson = new Gson();
        String s = gson.toJson(map);
        System.out.println(s);
        JsonObject jsonObject = gson.fromJson(s, JsonObject.class);



    }
}
