package com.cheweishi.android.entity;
/**
 * 首页商家服务列表信息
 * @author mingdasen
 *
 */
public class MainSellerServiceInfoNative {
	private String id;
	private int isFPrice;//是否有优惠价 0没有 1有
	private String name;//服务名称
	private String fPrice;//优惠价
	private String price;//实际价格
	private String isRed;//是否支持红包支付
	private String describe;//服务描述
	private String cate_id_2;//服务类型 30是普通洗车
	
	
	public String getCate_id_2() {
		return cate_id_2;
	}
	public void setCate_id_2(String cate_id_2) {
		this.cate_id_2 = cate_id_2;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getIsRed() {
		return isRed;
	}
	public void setIsRed(String isRed) {
		this.isRed = isRed;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getIsFPrice() {
		return isFPrice;
	}
	public void setIsFPrice(int isFPrice) {
		this.isFPrice = isFPrice;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getfPrice() {
		return fPrice;
	}
	public void setfPrice(String fPrice) {
		this.fPrice = fPrice;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
}
