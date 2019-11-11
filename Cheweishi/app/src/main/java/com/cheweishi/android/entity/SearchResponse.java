package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 5/3/2016.
 */
public class SearchResponse extends BaseResponse {

    /**
     * msg : [{"name":"中国石油(科发中和加油站)","cityName":"成都市","location":{"lng":104.08835981364,"lat":30.547196200815},"address":"成都高新区中和镇新下街","distance":1448,"district":"双流县","additionalInformation":{"name":"中国石油(科发中和加油站)","telephone":"","address":"成都高新区中和镇新下街","price":"","tag":"加油站"},"type":"交通设施加油站中国石油"},{"name":"中国石油华阳中和加油站","cityName":"成都市","location":{"lng":104.0909695455,"lat":30.561074950003},"address":"近郊  中和镇新上街119号新上街仁和路口","distance":1653,"district":"双流县","additionalInformation":{"name":"中国石油(满福庭苑东南)","telephone":"","address":"近郊  中和镇新上街119号新上街仁和路口","price":"","tag":"加油站"},"type":"交通设施加油站中国石油"},{"name":"加油站","cityName":"成都市","location":{"lng":104.0532555436,"lat":30.542685714248},"address":"天府四街668号1栋1层1号","distance":2481,"district":"武侯区","additionalInformation":{"name":"加油站","telephone":"13408646422","address":"天府四街668号1栋1层1号","price":"","tag":"加油站"},"type":"交通设施加油站"},{"name":"延长壳牌加油站(中柏大道)","cityName":"成都市","location":{"lng":104.0975687756,"lat":30.542054179081},"address":"中和镇中柏路龙祥佳苑斜对面","distance":2504,"district":"双流县","type":"交通设施加油站"}]
     * page : null
     */

    private Object page;
    /**
     * name : 中国石油(科发中和加油站)
     * cityName : 成都市
     * location : {"lng":104.08835981364,"lat":30.547196200815}
     * address : 成都高新区中和镇新下街
     * distance : 1448
     * district : 双流县
     * additionalInformation : {"name":"中国石油(科发中和加油站)","telephone":"","address":"成都高新区中和镇新下街","price":"","tag":"加油站"}
     * type : 交通设施加油站中国石油
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
        private String name;
        private String cityName;
        /**
         * lng : 104.08835981364
         * lat : 30.547196200815
         */

        private LocationBean location;
        private String address;
        private int distance;
        private String district;
        /**
         * name : 中国石油(科发中和加油站)
         * telephone :
         * address : 成都高新区中和镇新下街
         * price :
         * tag : 加油站
         */

        private AdditionalInformationBean additionalInformation;
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public AdditionalInformationBean getAdditionalInformation() {
            return additionalInformation;
        }

        public void setAdditionalInformation(AdditionalInformationBean additionalInformation) {
            this.additionalInformation = additionalInformation;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static class LocationBean {
            private double lng;
            private double lat;

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }
        }

        public static class AdditionalInformationBean {
            private String name;
            private String telephone;
            private String address;
            private String price;
            private String tag;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }
        }
    }
}
