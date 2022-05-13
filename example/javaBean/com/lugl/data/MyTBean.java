package com.lugl.data;

import java.util.HashMap;

/**
 * trydesc
 */
public class MyTBean {
    // id
    private int id;
    // testid
    private String descid;
    // 第二级
    private HashMap<Integer, k2Bean> k2;

    /** id */
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /** testid */
    public String getDescid() {
        return this.descid;
    }

    public void setDescid(String descid) {
        this.descid = descid;
    }

    /** 第二级 */
    public HashMap<Integer, k2Bean> getK2() {
        return this.k2;
    }

    public void setK2(HashMap<Integer, k2Bean> k2) {
        this.k2 = k2;
    }

    public class k2Bean{
        // 第二级
        private int k2;
        // 第二级desc
        private String desc2;
        // 第三级
        private HashMap<Long, k3Bean> k3;

        /** 第二级 */
        public int getK2() {
            return this.k2;
        }

        public void setK2(int k2) {
            this.k2 = k2;
        }

        /** 第二级desc */
        public String getDesc2() {
            return this.desc2;
        }

        public void setDesc2(String desc2) {
            this.desc2 = desc2;
        }

        /** 第三级 */
        public HashMap<Long, k3Bean> getK3() {
            return this.k3;
        }

        public void setK3(HashMap<Long, k3Bean> k3) {
            this.k3 = k3;
        }

        public class k3Bean{
            // 第三级
            private long k3;
            // 第三级desc
            private String desc3;
            // 结果
            private int result;
            // shuoming
            private String desc;

            /** 第三级 */
            public long getK3() {
                return this.k3;
            }

            public void setK3(long k3) {
                this.k3 = k3;
            }

            /** 第三级desc */
            public String getDesc3() {
                return this.desc3;
            }

            public void setDesc3(String desc3) {
                this.desc3 = desc3;
            }

            /** 结果 */
            public int getResult() {
                return this.result;
            }

            public void setResult(int result) {
                this.result = result;
            }

            /** shuoming */
            public String getDesc() {
                return this.desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

        }
    }
}
