package com.lugl.data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 */
public class MyTestBean {
    // id
    private int id;
    // 名字
    private String name;
    // 年龄
    private int age;
    // 地址列表
    private ArrayList<String> addr;
    // 属性map
    private HashMap<String, String> prot;
    // 测试类
    private TestBean test;
    // 测试类列表
    private ArrayList<TestBean> testList;
    // 测试类map
    private HashMap<Integer, TestBean> testprot;
    // testboolean
    private boolean tb;

    /** id */
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /** 名字 */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** 年龄 */
    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /** 地址列表 */
    public ArrayList<String> getAddr() {
        return this.addr;
    }

    public void setAddr(ArrayList<String> addr) {
        this.addr = addr;
    }

    /** 属性map */
    public HashMap<String, String> getProt() {
        return this.prot;
    }

    public void setProt(HashMap<String, String> prot) {
        this.prot = prot;
    }

    /** 测试类 */
    public TestBean getTest() {
        return this.test;
    }

    public void setTest(TestBean test) {
        this.test = test;
    }

    /** 测试类列表 */
    public ArrayList<TestBean> getTestList() {
        return this.testList;
    }

    public void setTestList(ArrayList<TestBean> testList) {
        this.testList = testList;
    }

    /** 测试类map */
    public HashMap<Integer, TestBean> getTestprot() {
        return this.testprot;
    }

    public void setTestprot(HashMap<Integer, TestBean> testprot) {
        this.testprot = testprot;
    }

    /** testboolean */
    public boolean isTb() {
        return this.tb;
    }

    public void setTb(boolean tb) {
        this.tb = tb;
    }

}
