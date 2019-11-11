package com.cheweishi.android.config;

import android.app.ProgressDialog;

public class API {
	/** 新版基本路径 */
	public static final String CSH_BASE_URL = NetInterface.IMG_URL ;
//	public static final String CSH_BASE_URL = "http://192.168.1.111/";
	public static String CWS_BASE_URL = "http://125.86.116.228:81";
	public static String BASE_URL = "http://app.chcws.com:8080/ZL/";// 基础路径
	public static String CHEWEISHI_BASE_URL = "http://app.chcws.com:8080/XAI/";
	public static String HOME_IMAGE_URL = BASE_URL + "appDate/appGetAd.do";// 首页图片请求路径
	public static String NET_PHONE_CALL_URL = BASE_URL + "appDate/";// 网络电话请求路径
	public static String MESSAGE_LIST_URL = BASE_URL + "appDate/";// 消息提醒请求路径

	public static String CONFIRM_AN_ORDER = CHEWEISHI_BASE_URL
			+ "app/wash/appUserConfirmOrder.do";// 预约洗车历史订单确认
	public static String CONFIRM_AN_ORDER_cause = CHEWEISHI_BASE_URL
			+ "app/wash/appCarwashOrder.do";// 预约洗车历史订单原因说明
	public static String USER_SETTING_URL = BASE_URL
			+ "appDate/appReviseUser.do";
	public static String INFORMATION_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainCircle.do";
	public static String INFORMATION1_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainCircleInfo.do";
	public static String INFORMATION2_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainCircleDes.do";
	public static String USER_UP_SIGN = CHEWEISHI_BASE_URL
			+ "app/t_cws/appUpNote.do";
	public static String BALANCE_GET_URL = BASE_URL + "appDate/appGetAcount.do";
	public static String CITY_GET_URL = BASE_URL + "appDate/appGetCities.do";
	public static String returnSuccess = "200000";
	public static String returnDefault = "DEFAULT";
	public static String returnFail = "FAIL";
	public static String returnRelogin = "200001";

	public static String COMMIT_MODEL_URL = "http://114.215.109.210/carBSMList.do";
	public static String NEWS_GET_URL = "http://app.chcws.com:8080/ZL/appDate/appGetNews.do";
	public static String NET_PHONE = "http://app.chcws.com:8080/ZL/appDate/appCall.do?";
	public static String DRIVING_BEHAVIOR = "http://app.chcws.com:8080/XAI/app/t_cws/appGainDayDataDes.do";
	public static ProgressDialog progressDialog;
	public static String HELP_URL = "doc/2014471416577011.htm";
	public static String HISTORY_PHONE_URL = BASE_URL
			+ "appDate/appGetCalls.do?";
	public static String LOGOUT = CHEWEISHI_BASE_URL + "app/cws/appLogout.do";
	public static String REGIST_URL = CHEWEISHI_BASE_URL
			+ "app/cws/appRegist.do";
	public static String SIGN_URL = CHEWEISHI_BASE_URL + "app/cws/appSign.do";

	public static String USER_DETAIL_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainPerson.do";
	public static String LOGIN_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appLogin.do";
	public static String GET_CITY_URL = CHEWEISHI_BASE_URL
			+ "app/cws/appGainCity.do";
	public static String CHANGE_PERSON_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appUpPerson.do";

	public static String UPLOAD_IMG_URL = CHEWEISHI_BASE_URL
			+ "appUpload/uploadPhoto";
	public static String SAVE_IMG_URL = CHEWEISHI_BASE_URL
			+ "app/cws/appUpHead.do";
	public static String SeERVER_URL = CHEWEISHI_BASE_URL
			+ "app/service/appGainService.do";
	public static String SeERVER_DETAIL_URL = CHEWEISHI_BASE_URL
			+ "app/service/appGainServiceDetail.do";
	public static String SeERVER_AD_URL = CHEWEISHI_BASE_URL
			+ "app/cws/appGainAdvert.do";

	public static String SCORE_DETAIL_URL = CHEWEISHI_BASE_URL
			+ "app/cws/appGainScore";
	public static String GET_CITY_ID_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainCity.do";
	public static String LOGIN_MESSAGE_RELOGIN_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainLogin.do";
	public static String ACCEPT_MESSAGE = CHEWEISHI_BASE_URL
			+ "app/service/appGainIsPush.do";
	/** 验证app账号 */
	public static String LOGIN_IS_VALID = "http://app.chcws.com:8080/XAI/app/cws/appLoginIsValid.do";
	/**
	 * /** 我的车
	 */
	public static String CAR_DYNAMIC = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainGPSCars.do";
	/** 一键检测 */
	public static String KEY_DETECTION = "http://app.chcws.com:8080/XAI/app/cws/appGainCheck.do";

	// 足迹接口
	public static final String FOOTMARKURL = CHEWEISHI_BASE_URL
			+ "app/cws/appGainFootmark.do?";
	/** 获取推送消息列表 */
	public static final String PUSH_LIST_URL = CHEWEISHI_BASE_URL
			+ "app/service/appGainPush.do";

	public static final String PASSWORD_FORGET_URL = CHEWEISHI_BASE_URL
			+ "app/cws/appUpPsw.do";
	/** 获取实时车况数据 */
	public static final String REAL_TIME_CONDITION_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainOBD_re.do";

	/** 传递device_token */
	public static final String APP_UP_TOKEN_URL = CHEWEISHI_BASE_URL
			+ "app/cws/appUpToken.do";
	/** 修改保养状态提交 */
	public static final String MAINTENANCE_STATUS_CHANGES_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appUpMile.do";

	/** 修改年检状态提交 */
	public static final String INSPECTION_STATIC_CHANGES_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appUpInspect.do";
	public static final String SPEED_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainReportDes.do";
	public static final String OIL_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainReportDes.do";
	public static final String ONTHATDAY_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainReportDes.do";
	public static final String DRIVING_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainReportDes.do";
	/** 报告中驾驶里程接口 */
	public static final String DRIVING_MILE_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainReportDes.do";
	/**
	 * 报告-费用
	 */
	public static final String REPORT_FREE_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainReportDes.do";
	/**
	 * 报告-费用-添加
	 */
	public static final String REPORT_FREE_ADD_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appAddReportFee.do";
	/**
	 * 报告-费用-删除
	 */
	public static final String REPORT_FREE_DELETE_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appDelReportFee.do";
	/**
	 * 报告-费用-跟新
	 */
	public static final String REPORT_FREE_UPDATE_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appUpReportFee.do";

	/**
	 * 积分详情
	 */
	public static final String INTEGRALURL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainScore.do";
	/**
	 * 获取验证码
	 */
	public static final String GETCODE_URL = BASE_URL
			+ "appDate/appGetVdCode.do";
	public static final String FUWU_service = "http://app.chcws.com:8080/XAI/app/service/appGainGas.do";
	/**
	 * 获取消息
	 */
	public static final String MESSAGE_URL = "http://app.chcws.com:8080/XAI/app/t_cws/appGainPush.do";

	/**
	 * 获取费用详情
	 */
	public static final String MyMoney_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainFee.do";
	/**
	 * 获取是否购买过保险的信息接口
	 */
	public static final String MyMoney_Insurance_URL = CHEWEISHI_BASE_URL
			+ "app/up/appGainPolicy.do";
	public static final String INSURANCE_BUY_URL = CHEWEISHI_BASE_URL
			+ "app/up/appBuyPolicy.do";
	public static final String PROTOCAL_Insurance_URL = CHEWEISHI_BASE_URL
			+ "app/up/appGainPolicyRule.do";
	/**
	 * 选择绑定费用类型
	 */
	public static final String CHOOSE_FEED = "http://app.chcws.com:8080/XAI/app/t_cws/appChooseFeed.do";
	/**
	 * 签到详情
	 */
	public static final String SIGNIN_DETAIL = "http://app.chcws.com:8080/XAI/app/cws/appGainSign.do";

	/**
	 * 下载图片url
	 */
	public static final String DOWN_IMAGE_URL = "http://app.chcws.com:8080/XAI/appDownLoad/downLoadPhoto?path=";

	/**
	 * 加油站
	 */
	public static final String GASSTATIONLIST_URL = CHEWEISHI_BASE_URL
			+ "app/service/appGainGas.do";

	/***
	 * 找车位
	 */

	public static final String FINDCAR_URL = CHEWEISHI_BASE_URL
			+ "app/service/appGainPark.do";
	public static final String FINDCARPORT_URL = CHEWEISHI_BASE_URL
			+ "app/service/appGainInterest.do";

	// public static final String FINDCARPORT_URL = CHEWEISHI_BASE_URL
	// + "app/service/appGainPark.do";

	/**
	 * 找车位列表
	 */
	public static final String FINDCARPORT_LIST_URL = CHEWEISHI_BASE_URL
			+ "app/service/appGainPark.do";

	/**
	 * 车位详情
	 */
	public static final String FINDCARPORT_DETAILES_URL = CHEWEISHI_BASE_URL
			+ "app/service/appGainParkInfo.do";

	/**
	 * SOS-list
	 */
	public static final String SOS_LIST_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appGainWarn.do";
	/**
	 * SOS-delete
	 */
	public static final String SOS_DELETE_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appDelSos.do";
	/**
	 * SOS-add
	 */
	public static final String SOS_ADD_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appUpSos.do";
	/**
	 * SOS-edit
	 */
	public static final String SOS_EDIT_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appEditSos.do";
	/**
	 * SOS-status
	 */
	public static final String SOS_STATUS_URL = CHEWEISHI_BASE_URL
			+ "app/t_cws/appUpWarn.do";

	public static final String FOOTMARK_URL = CHEWEISHI_BASE_URL
			+ "app/cws/appGainFootmark.do";
	/**
	 * 洗车店-检查预定
	 */
	public static final String WASHCAR_CHECKORDER = CHEWEISHI_BASE_URL
			+ "app/service/appCheckOrder.do";

	/**
	 * 获取预约洗车订单详情
	 */
	public static final String WASHCAR_ORDER_DETAIL = CHEWEISHI_BASE_URL
			+ "app/service/appGainOrder.do";

	/**
	 * 洗车店-服务据点列表(已弃用)
	 */
	public static final String WASHCAR_LIST = CHEWEISHI_BASE_URL
			+ "app/service/appGainCarwash.do";
	/**
	 * 洗车店-服务据点列表
	 */
	public static final String WASHCHAR_LIST_DATA = CHEWEISHI_BASE_URL
			+ "app/service/appGainWash.do";

	/**
	 * 洗车店-预约洗车店
	 */
	public static final String WASHCAR_ORDER_SHOP = CHEWEISHI_BASE_URL
			+ "app/service/appAddOrder.do";

	/**
	 * 洗车店-取消预约
	 */
	public static final String WASHCAR_ORDER_CANCEL = CHEWEISHI_BASE_URL
			+ "app/service/appCancelOrder.do";
	/**
	 * 支付宝支付获取订单
	 */
	public static final String GET_NO = CHEWEISHI_BASE_URL
			+ "app/pgy/appGainPgyOrder.do";

	/**
	 * 微信支付获取订单
	 */
	public static final String WEIXIN_PAY_NO = "http://app.chcws.com:8080/XAI/app/pgy/appGainWechatOrder.do";

	/**
	 * 洗车点历史记录
	 */
	public static final String WASHCAR_HISTORY = CHEWEISHI_BASE_URL
			+ "app/up/appGainWashList.do";
	/**
	 * 洗车点历史记录-详情
	 */
	public static final String WASHCAR_HISTORY_DETAILS = CHEWEISHI_BASE_URL
			+ "app/up/appGainWash.do";
	public static final String GET_ACCOUNT = CHEWEISHI_BASE_URL
			+ "app/up/appGainAccount.do";

	/*
	 * ==========================================================================
	 * ==
	 * ========================================================================
	 * =====
	 */

	/** 注册 */
	public static final String CSH_REGISTER_URL = CSH_BASE_URL
			+ "app/auth/register";

	/** 登录 */
	public static final String CSH_LOGIN_URL = CSH_BASE_URL + "app/auth/login";

	/** 退出 */
	public static final String CSH_LOGOUT_URL = CSH_BASE_URL
			+ "app/auth/logout";

	/** 获取验证码 */
	public static final String CSH_CODE_URL = CSH_BASE_URL
			+ "ucpaas/pin/smscode";

	/** 修改密码 */
	public static final String CSH_UPDATE_PASSWORD_URL = CSH_BASE_URL
			+ "app/auth/updatePassowrd";

	/** 获取个人信息 */
	public static final String CSH_QUERY_USER_INFO_URL = CSH_BASE_URL
			+ "app/user/queryUserInfo";

	/** 修改个人信息 */
	public static final String CSH_UPDATE_USER_INFO_URL = CSH_BASE_URL
			+ "app/user/upUserInfo";

	/** 修改个人签名 */
	public static final String CSH_UPDATE_USER_SIGN_URL = CSH_BASE_URL
			+ "app/user/updateSignature";

	/** 修改昵称 */
	public static final String CSH_UPDATE_USER_NICK_URL = CSH_BASE_URL
			+ "app/user/upnickName";
	/** 意见反馈 */
	public static final String CSH_RETUR_ORDER_URL = CSH_BASE_URL
			+ "app/feedback/insertFeedback";

	/** 获取充值 */
	public static final String CSH_PING_CHARGE_URL = CSH_BASE_URL
			+ "app/pay/recharge";

	/** 获取订单支付 */
	public static final String CSH_ORDER_CHARGE_URL = CSH_BASE_URL
			+ "app/pay/queryCharge";

	/** 消息中心详情 */
	public static final String CSH_MESSAGE_DETAILS_URL = CSH_BASE_URL
			+ "app/messageInfo/findMessageInfo";

	/**
	 * 车辆管理列表
	 */
	public static String CAR_MANAGER_URL = CSH_BASE_URL
			+ "app/vehicle/queryVehicle";
	/**
	 * 绑定车辆
	 */
	public static String ADD_CAR_URL = CSH_BASE_URL
			+ "app/vehicle/insertVehicle";
	/**
	 * 编辑信息
	 */
	public static final String CAR_DETAIL_EDIT_URL = CSH_BASE_URL
			+ "app/vehicle/updateVehicle";

	/** 查询钱包信息 */
	public static final String CSH_QUERY_WALLET_URL = CSH_BASE_URL
			+ "app/wallet/queryWallet";

	/** 获取洗车列表接口 */
	public static final String CSH_WASHING_LIST_URL = CSH_BASE_URL
			+ "app/store/findStoreByWashingList";

	/** 主页面数据接口 */
	public static final String CSH_MAIN_DATA_URL = CSH_BASE_URL
			+ "app/loadData/index";
	/**
	 * 获取车品牌、车型、车系
	 */
	public static String GET_MODEL_URL = CSH_BASE_URL + "app/brand/queryBrand";
	/**
	 * 绑定设备
	 */
	public static String ADD_DEVICE_URL = CSH_BASE_URL
			+ "app/vehicle/bindVehicle";
	/** 获取图片基础路径 */
	public static String CSH_GET_IMG_BASE_URL = NetInterface.IMG_URL ;
	/** 商家详情接口 */
	public static final String CSH_WASHCARDETAILS_URL = CSH_BASE_URL
			+ "app/store/findStoreInfo";

	/** 一键检测 */
	public static final String CSH_CAR_DETECTION_URL = CSH_BASE_URL
			+ "app/vehicle/queryCheck";
	/**
	 * 报告主界面
	 */
	public static final String REPORT_MAIN = CSH_BASE_URL
			+ "app/vehicle/queryReport";
	/**
	 * 设置默认车辆
	 */
	public static String SET_DEDAULT_CAR_URL = CSH_BASE_URL
			+ "app/vehicle/updateDefault";
	/** 车动态 */
	public static String CAR_DYNAMIC_URL = CSH_BASE_URL
			+ "app/vehicle/queryLocation";
	/** 意见反馈 */
	public static String ORDER_DEAL = CSH_BASE_URL
			+ "app/vehicle/queryLocation";

	/**
	 * 预约保养列表
	 */
	public static String CSH_MAINTAIN_LIST_URL = CSH_BASE_URL
			+ "app/store/findMaintenance";

	/**
	 * 汽车美容列表
	 */
	public static String CSH_BEAUTY_LIST_URL = CSH_BASE_URL
			+ "app/store/findCosmetology";
	/**
	 * 生成订单
	 */
	public static String CSH_MAKE_ORDER_URL = CSH_BASE_URL
			+ "app/order/insertOrder";
	/**
	 * 订单详情
	 */
	public static String ORDER_DETAIL_URL = CSH_BASE_URL
			+ "app/order/findOrderDetails";
	/**
	 * 我的订单列表
	 */
	public static String CSH_MYORDER_LIST_URL = CSH_BASE_URL
			+ "app/order/findOrderList";
	/**
	 * 我的订单列表删除
	 */
	public static String CSH_MYORDER_LIST_DELETE_URL = CSH_BASE_URL
			+ "app/order/findOrderList";
	/**
	 * 取消订单
	 */
	public static String CANCEL_ORDER_URL = CSH_BASE_URL
			+ "app/order/cancelOrder";

	/** 红包详情 */
	public static String CSH_RED_PACKETS_DETAILS_URL = CSH_BASE_URL
			+ "app/wallet/redDetails";

	/** 验证码重新登录 */
	public static String CSH_VALIDATE_LOGIN_URL = CSH_BASE_URL
			+ "app/auth/loginAuth";

	/** 在线支付成功获取支付订单信息 */
	public static String CSH_GET_PAY_SUCCESS_ORDER_URL = CSH_BASE_URL
			+ "app/order/paymentResult";

	/**
	 * 获取消息列表
	 */
	public static String CSH_MESSAGE_LIST_URL = CSH_BASE_URL
			+ "app/msg/queryMsg";

	/**
	 * 获取余额明细列表
	 */
	public static String CSH_REST_OF_MONEY_LIST_URL = CSH_BASE_URL
			+ "app/walletRecord/WalletRecord";
	
	/**
	 * 获取网络电话
	 */
	public static String CSH_WEB_PHONE_URL = CSH_BASE_URL
			+ "app/mobile/queryMobile";
}
