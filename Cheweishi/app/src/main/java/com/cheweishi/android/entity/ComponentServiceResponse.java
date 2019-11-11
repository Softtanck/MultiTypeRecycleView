package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tangce on 6/1/2016.
 */
public class ComponentServiceResponse extends BaseResponse implements Serializable {

    /**
     * msg : [{"serviceItemName":"车身保养","itemParts":[{"isDefault":true,"price":20,"serviceItemPartName":"保养1","id":1},{"isDefault":true,"price":12,"serviceItemPartName":"保养2","id":2},{"isDefault":false,"price":5,"serviceItemPartName":"保养3","id":3}]},{"serviceItemName":"车窗保养","itemParts":[{"isDefault":true,"price":30,"serviceItemPartName":"保养4","id":4}]}]
     * page : null
     */

    private Object page;
    /**
     * serviceItemName : 车身保养
     * itemParts : [{"isDefault":true,"price":20,"serviceItemPartName":"保养1","id":1},{"isDefault":true,"price":12,"serviceItemPartName":"保养2","id":2},{"isDefault":false,"price":5,"serviceItemPartName":"保养3","id":3}]
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

    public static class MsgBean implements Serializable {
        private String serviceItemName;
        /**
         * isDefault : true
         * price : 20
         * serviceItemPartName : 保养1
         * id : 1
         */

        private List<ItemPartsBean> itemParts;

        public String getServiceItemName() {
            return serviceItemName;
        }

        public void setServiceItemName(String serviceItemName) {
            this.serviceItemName = serviceItemName;
        }

        public List<ItemPartsBean> getItemParts() {
            return itemParts;
        }

        public void setItemParts(List<ItemPartsBean> itemParts) {
            this.itemParts = itemParts;
        }

        public static class ItemPartsBean implements Serializable {
            private boolean isDefault;
            private String price;
            private String serviceItemPartName;
            private int id;

            public boolean isIsDefault() {
                return isDefault;
            }

            public void setIsDefault(boolean isDefault) {
                this.isDefault = isDefault;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getServiceItemPartName() {
                return serviceItemPartName;
            }

            public void setServiceItemPartName(String serviceItemPartName) {
                this.serviceItemPartName = serviceItemPartName;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
