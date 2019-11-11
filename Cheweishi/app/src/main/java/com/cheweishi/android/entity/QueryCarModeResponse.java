package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 3/27/2016.
 */
public class QueryCarModeResponse extends BaseResponse {


    /**
     * page : null
     * msg : [{"id":7,"childLine":[{"id":273,"icon":null,"name":"奥迪A3"},{"id":272,"icon":null,"name":"奥迪A6L"},{"id":271,"icon":null,"name":"奥迪Q5"},{"id":270,"icon":null,"name":"奥迪A6"},{"id":276,"icon":null,"name":"奥迪A4L"},{"id":275,"icon":null,"name":"奥迪Q3"},{"id":274,"icon":null,"name":"奥迪A4"}],"name":"一汽-大众奥迪"},{"id":8,"childLine":[{"id":278,"icon":null,"name":"奥迪RS 7"},{"id":277,"icon":null,"name":"奥迪RS 5"}],"name":"奥迪RS"},{"id":9,"childLine":[{"id":297,"icon":null,"name":"奥迪TTS"},{"id":296,"icon":null,"name":"奥迪A5"},{"id":295,"icon":null,"name":"奥迪A7"},{"id":294,"icon":null,"name":"奥迪Q3(进口)"},{"id":293,"icon":null,"name":"奥迪Q7"},{"id":292,"icon":null,"name":"奥迪S3"},{"id":291,"icon":null,"name":"奥迪S6"},{"id":290,"icon":null,"name":"奥迪S8"},{"id":289,"icon":null,"name":"奥迪A1"},{"id":288,"icon":null,"name":"奥迪TT"},{"id":287,"icon":null,"name":"奥迪A4(进口)"},{"id":286,"icon":null,"name":"奥迪A6(进口)"},{"id":285,"icon":null,"name":"奥迪A8"},{"id":284,"icon":null,"name":"奥迪Q5(进口)"},{"id":283,"icon":null,"name":"奥迪R8"},{"id":282,"icon":null,"name":"奥迪S5"},{"id":281,"icon":null,"name":"奥迪S7"},{"id":280,"icon":null,"name":"奥迪SQ5"},{"id":279,"icon":null,"name":"奥迪A3(进口)"}],"name":"奥迪(进口)"}]
     */

    private Object page;
    /**
     * id : 7
     * childLine : [{"id":273,"icon":null,"name":"奥迪A3"},{"id":272,"icon":null,"name":"奥迪A6L"},{"id":271,"icon":null,"name":"奥迪Q5"},{"id":270,"icon":null,"name":"奥迪A6"},{"id":276,"icon":null,"name":"奥迪A4L"},{"id":275,"icon":null,"name":"奥迪Q3"},{"id":274,"icon":null,"name":"奥迪A4"}]
     * name : 一汽-大众奥迪
     */

    private List<MsgBean> msg;

    public Object getPage() {
        return page;
    }

    public void setPage(Object page) {
        this.page = page;
    }

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private int id;
        private String name;
        /**
         * id : 273
         * icon : null
         * name : 奥迪A3
         */

        private List<ChildLineBean> childLine;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ChildLineBean> getChildLine() {
            return childLine;
        }

        public void setChildLine(List<ChildLineBean> childLine) {
            this.childLine = childLine;
        }

        public static class ChildLineBean {
            private int id;
            private String icon;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
