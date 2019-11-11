package com.cheweishi.android.utils.mapUtils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.PoiDetailShareURLOption;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.cheweishi.android.utils.StringUtil;

public class MapSearchUtil {
    /**
     * 路线规划-开车
     */
    public static final int ROUTEPLAN_DRIVE = 1001;
    /**
     * 路线规划-步行
     */
    public static final int ROUTEPLAN_WALK = 1002;
    /**
     * 路线规划-公交
     */
    public static final int ROUTEPLAN_TRANSIT = 1003;
    /**
     * 分享链接-位置
     */
    public static final int SHAREURL_LOCATION = 1004;
    /**
     * 分享链接-兴趣点详情
     */
    public static final int SHAREURL_POIDETAIL = 1005;

    /* 路线规划 */
    private RoutePlanSearch routePlanSearch;

    /* 地理位置编译 */
    private GeoCoder geoCoder;

    /* 兴趣点搜索 */
    private PoiSearch poiSearch;

    /* 建议搜索 */
    private SuggestionSearch suggestionSearch;

    /* 链接分享搜索 */
    private ShareUrlSearch shareUrlSearch;

    /**
     * 路线规划
     *
     * @param planStyle                    规划方式
     * @param sLatLon
     * @param eLatLon
     * @param onGetRoutePlanResultListener
     */
    public void startRoutePlan(int planStyle, LatLng sLatLon, LatLng eLatLon,
                               OnGetRoutePlanResultListener onGetRoutePlanResultListener) {
        if (routePlanSearch == null) {
            routePlanSearch = RoutePlanSearch.newInstance();
        }
        routePlanSearch
                .setOnGetRoutePlanResultListener(onGetRoutePlanResultListener);
        PlanNode sNode = PlanNode.withLocation(sLatLon);
        PlanNode eNode = PlanNode.withLocation(eLatLon);

        switch (planStyle) {
            case ROUTEPLAN_TRANSIT:
                routePlanSearch.transitSearch(new TransitRoutePlanOption().from(
                        sNode).to(eNode));
                break;
            case ROUTEPLAN_DRIVE:
                routePlanSearch.drivingSearch(new DrivingRoutePlanOption().from(
                        sNode).to(eNode));
                break;
            case ROUTEPLAN_WALK:
                routePlanSearch.walkingSearch(new WalkingRoutePlanOption().from(
                        sNode).to(eNode));
                break;

            default:
        }

    }

    /**
     * 开始geocode编码
     *
     * @param city
     * @param address
     * @param onGetGeoCoderResultListener
     */
    public void startGeoCode(String city, String address,
                             OnGetGeoCoderResultListener onGetGeoCoderResultListener) {
        initGeoCoder(onGetGeoCoderResultListener);

        geoCoder.geocode(new GeoCodeOption().city(city).address(address));
    }

    /**
     * 开始geocoder反编译
     *
     * @param latLng
     * @param onGetGeoCoderResultListener
     */
    public void startReverseGeoCode(LatLng latLng, OnGetGeoCoderResultListener onGetGeoCoderResultListener) {
        initGeoCoder(onGetGeoCoderResultListener);

        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }

    /**
     * 初始化geocoder并设置监听
     *
     * @param onGetGeoCoderResultListener
     */
    private void initGeoCoder(OnGetGeoCoderResultListener onGetGeoCoderResultListener) {
        if (geoCoder == null) {
            geoCoder = GeoCoder.newInstance();
            geoCoder.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);
        }
    }

    /**
     * 简单poi搜索
     *
     * @param city
     * @param onGetPoiSearchResultListener
     */
    public void startPoiSearch(String city,
                               OnGetPoiSearchResultListener onGetPoiSearchResultListener) {
        if (poiSearch == null) {
            poiSearch = PoiSearch.newInstance();
            poiSearch
                    .setOnGetPoiSearchResultListener(onGetPoiSearchResultListener);
        }

        poiSearch
                .searchInCity(new PoiCitySearchOption().city(city).keyword(""));

    }

    /**
     * 建议点搜索
     *
     * @param city
     * @param keyWord
     * @param onGetSuggestionResultListener
     */
    public void startSuggetSearch(String city, String keyWord,
                                  OnGetSuggestionResultListener onGetSuggestionResultListener) {
        if (suggestionSearch == null) {
            suggestionSearch = SuggestionSearch.newInstance();
            suggestionSearch
                    .setOnGetSuggestionResultListener(onGetSuggestionResultListener);
        }
        if (!StringUtil.isEmpty(keyWord) && !StringUtil.isEmpty(city)) {
            suggestionSearch.requestSuggestion(new SuggestionSearchOption()
                    .keyword(keyWord).city(city));
        }

    }

    /**
     * @param type
     * @param latLng
     * @param poiUid
     * @param onGetShareUrlResultListener
     */
    public void startShareUrlSearch(int type, LatLng latLng, String poiUid,
                                    OnGetShareUrlResultListener onGetShareUrlResultListener) {
        if (shareUrlSearch == null) {
            shareUrlSearch = ShareUrlSearch.newInstance();
            shareUrlSearch
                    .setOnGetShareUrlResultListener(onGetShareUrlResultListener);
        }
        if (type == SHAREURL_LOCATION) {
            shareUrlSearch.requestLocationShareUrl(new LocationShareURLOption()
                    .location(latLng));
            return;
        }
        shareUrlSearch.requestPoiDetailShareUrl(new PoiDetailShareURLOption()
                .poiUid(poiUid));
    }

    /**
     * 释放变量
     */
    public void onDestory() {
        if (routePlanSearch != null) {
            routePlanSearch.destroy();
            routePlanSearch = null;
        }

        if (geoCoder != null) {
            geoCoder.destroy();
            geoCoder = null;
        }
        if (poiSearch != null) {
            poiSearch.destroy();
            poiSearch = null;
        }

        if (suggestionSearch != null) {
            suggestionSearch.destroy();
            suggestionSearch = null;
        }
        if (shareUrlSearch != null) {
            shareUrlSearch.destroy();
            shareUrlSearch = null;
        }
    }
}
