package com.cheweishi.android.config;

/**
 * Created by Tanck on 2016/3/20.
 */
public class NetInterface {

    //        public static final String BASE_URL = "http://120.27.92.247:10001";// 生产服务器路径
    public static final String BASE_URL = "http://120.27.27.99:10001";// 测试服务器路径
//    public static final String BASE_URL = "http://10.50.40.56:8081";// 基础路径

    public static final String IMG_BASE_URL = BASE_URL;// 基础路径

    public static final String IMG_PROJECT_NAME = "/csh-interface";

    public static final String IMG_URL = NetInterface.IMG_BASE_URL + NetInterface.IMG_PROJECT_NAME;

    public static final String TEMP_URL = "/csh-interface/endUser/";// 中间路径

    public static final String TEMP_HOME_URL = "/csh-interface/tenantInfo/";//租户相关的

    public static final String TEMP_CAR_URL = "/csh-interface/vehicle/";// 车辆管理

    public static final String TEMP_ADV_URL = "/csh-interface/advertisement/"; // 广告

    public static final String TEMP_USER_BALANCE = "/csh-interface/balance/";// 钱包

    public static final String TEMP_ORDER = "/csh-interface/carService/";// 用户订单

    public static final String TEMP_MESSAGE = "/csh-interface/message/";//消息中心

    public static final String TEMP_FEEDBACK = "/csh-interface/feedback/";// 反馈

    public static final String TEMP_OBD = "/csh-interface/obd/"; // 检测obd相关

    public static final String TEMP_EVALUATE = "/csh-interface/tenantEvaluate/";// 评价相关

    public static final String TEMP_JPUSH = "/csh-interface/jpush/";//极光推送

    public static final String TEMP_RECORD = "/csh-interface/illegalRecord/";//违章记录

    public static final String TEMP_SEARCH = "/csh-interface/aroundSearch/";//停车位和加油站

    public static final String TEMP_COUPON = "/csh-interface/coupon/";//优惠券相关

    public static final String TEMP_DUIBA = "/csh-interface/estore/duiba/";//兑吧相关

    public static final String TEMP_NEWS = "/csh-interface/news/";//新闻相关

    public static final String TEMP_SHOP = "/csh-interface/estore/product/";//电商相关

    public static final String TEMP_SHOP_CART = "/csh-interface/estore/cart/";//购物车相关

    public static final String TEMP_SHOP_ADD = "/csh-interface/estore/receiverAddress/";//收货地址

    public static final String TEMP_ORDER_CREATE = "/csh-interface/estore/order/";//创建订单

    public static final String HEADER_ALL = BASE_URL + TEMP_URL;

    public static final String RESPONSE_SUCCESS = "0000"; // success

    public static final String RESPONSE_TOKEN = "0004"; // ERROR

    public static final String RESPONSE_ERROR = "1000"; // ERROR

    public static final String SUFFIX = ".jhtml";//后缀


    /**********************
     * 接口
     *********************/
    public static final String USER_LOGIN = "login"; // 登录

    public static final String SMS_TOKEN = "getSmsToken"; // 验证码接口/忘记密码

    public static final String RESET_PASSWORD = "resetPwd"; // 修改密码

    public static final String REGISTER = "reg"; // 注册

    public static final String EDIT_USER_INFO = "editUserInfo";//昵称修改

    public static final String USER_PHOTO = "editUserPhoto";//修改用户头型

    public static final String LIST = "list";// 列表

    public static final String HOME_ADV = "getAdvImage"; // 首页广告

    public static final String GET_TENANT_INFO = "getTenantById"; //获取租户信息

    public static final String BALANCE = "myWallet";// 钱包

    public static final String USER_ORDER = "purchaseList";//我的订单

    public static final String MESSAGE_CENTER = "getMsgList"; // 消息中心

    public static final String MAINTAIN = "getTenantByUser";// 保养和美容

    public static final String QUERY_CAR = "getVehicleBrandByCode";// 查询车辆

    public static final String QUERY_CAR_TWO = "getVehicleLineByBrand";//查询车辆二级

    public static final String QUERY_CAR_THERE = "getVehicleBrandDetailByLine";// 三级

    public static final String ADD = "add";// 添加反馈.商品

    public static final String LOGOUT = "logout";//注销

    public static final String SET_READ_MSG = "readMessage";//设置为已读

    public static final String BIND_DEVICE = "bindDevice";//绑定设备

    public static final String SET_DEFAULT_DEVICE = "setDefault";//设置默认设备

    public static final String WALLET_RECORD = "walletRecordByType";//明细

    public static final String EDIT = "edit";// 编辑

    public static final String ORDER_DETIAL = "recordDetail";// 订单详情

    public static final String SUBSCRIBE = "subscribeService";// 预约

    public static final String BUY_SERVICE = "payService";//购买服务

    public static final String UPDATE_PAY_STATUS = "updatePayStatus";//更新购买状态

    public static final String CHARGE_PAY = "chargeIn";//充值到钱包的交易凭证

    public static final String PAY_CALLBACK = "walletCharge";//充值成功后回调

    public static final String DETECTION = "oneKeyDetection";//车辆检测报告

    public static final String CAR_SCAN = "vehicleScan";//车辆扫描

    public static final String DELETE_MSG = "deleteMsgs";//删除消息

    public static final String DYNAMIC = "vehicleTrends";// 车辆动态

    public static final String BIND_QR = "bindTenant";//绑定

    public static final String RATE = "doRate";//用户对商户打分

    public static final String SET_ID = "setRegId";//设置极光ID

    public static final String ILLEGAL_RECORD = "getIllegalRecords";//违章记录

    public static final String UPDATE_CACHE = "updateLoginCacheInfo";//更新登录缓存

    public static final String SEARCH = "keyWordSearch";//停车位+加油站

    public static final String GETLISTCOUPON = "availableCoupon";//获取活动的优惠券

    public static final String MYCOUPON = "myCoupon";//我的优惠券列表

    public static final String GETCOUPON = "getCoupon";//领取优惠券

    public static final String PAY_COUPON = "getCouponForPay";//支付时可用优惠券列表

    public static final String GET_DEVICE_PRICE = "purDevicePage";//获取设备价钱

    public static final String BUY_DEVICE = "purDeviceCharge";// 购买设备

    public static final String GETSERVICEBYID = "getServiceById";// 获取保养,目前只有保养

    public static final String WASHCARCOUPON = "myWashingCoupon";//洗车劵

    public static final String GET_DEVICES_LIST = "getAvailableDevice"; // 获取可用设备列表

    public static final String GET_DUIBA_LOGIN_URL = "getLoginUrl";//获取兑吧登陆url

    public static final String GET_NEWS_TYPES = "getNewsType";//获取新闻列表类型

    public static final String GET_NEWS_LIST = "getNewsList";//获取新闻列表内容

    public static final String READ_NEWS = "readNewsDetail";//阅读新闻

    public static final String DO_COMMENT = "doComment";//评论新闻

    public static final String DO_LIKE = "doLikeClick";//用户点赞

    public static final String GET_SHOP_CATEGORY = "getCategory";//获取商品分类

    public static final String GET_PAY_COUPON_RED = "getCouponAndGiftForPay";//获取支付时可用优惠券及可用红包及可用洗车券

    public static final String GET_PRODUCT_INFO = "details";//商品详情

    public static final String DELETE = "delete";//删除商品

    public static final String ADD_AND_LESS = "oprNum";//增减商品

    public static final String GET_DEFAULT_ADDRESS = "getDefault";//获取默认地址

    public static final String GET_ADDRESS = "getArea";//获取地区

    public static final String ADD_OR_EDIT = "addOrEdit";//编辑或者添加收货地址

    public static final String CREATE = "create";//创建订单

    public static final String PAY = "pay";//支付订单

    public static final String OPT = "operation";//取消订单或者确认收货

    public static final String REVIEWLIST = "reviewList";//评论列表

    public static final String RETURN = "applyReturns";//退货

    public static final String RETURNLIST = "getReturnsBill";//退货列表

    public static final String INSURANCE = "http://app.car1615.com/app/insurance/page/788bf80951bb4b6f867ae5d281ea6807"; // 购买保险

}
