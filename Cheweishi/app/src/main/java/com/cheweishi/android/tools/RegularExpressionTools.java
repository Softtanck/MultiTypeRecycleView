package com.cheweishi.android.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionTools {
	/**
	 * 验证手机号码是否正确
	 * 
	 * @param str
	 * @return
	 */
	/**
	 * 验证手机号码是否正确
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String mobiles) {
		Pattern p = Pattern.compile("^(1[34578])\\d{9}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isRightPhone(String phoneNum) {
		Pattern p = Pattern.compile("^(1[34578])\\d{9}$");
		Matcher m = p.matcher(phoneNum);
		return m.matches();
	}

	public static boolean isCarPlate(String carPlate) {
		Pattern pattern = Pattern
				.compile("[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}");
		Matcher matcher = pattern.matcher(carPlate);
		return matcher.matches();
	}

	public static boolean isFacingExpression(String faceExpression) {
		String regu = "^([a-z]|[A-Z]|[0-9]|[\u2E80-\u9FFF]){3,}|@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?|[wap.]{4}|[www.]{4}|[blog.]{5}|[bbs.]{4}|[.com]{4}|[.cn]{3}|[.net]{4}|[.org]{4}|[http://]{7}|[ftp://]{6}$";
		boolean b = false;
		Pattern p = Pattern.compile(regu);
		Matcher m = p.matcher(faceExpression);
		b = m.matches();
		return b;
	}

	public static boolean isChinese(String chineseStr) {
		String regu = "[\u4e00-\u9fa5]";
		boolean b = false;
		Pattern p = Pattern.compile(regu);
		Matcher m = p.matcher(chineseStr);
		b = m.find();
		return b;
	}

	public static boolean isRightPhoen(String phone) {
		Pattern p = Pattern.compile("^0\\d{2,3}\\d{7,8}$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}
	
	public static boolean isAllChar(String str) {
		boolean flag = false;
        final String REGEX="^[a-z;]+$";
        final String REGEX1="^[0-9;]+$";
        final String REGEX2="^[a-z0-9;]+$";
        Matcher m=Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE).matcher(str);
        if(m.find()){
            flag = true;
        }else{
            m=Pattern.compile(REGEX1, Pattern.CASE_INSENSITIVE).matcher(str);
            if(m.find()){
            	flag = true;
            }else{
                m=Pattern.compile(REGEX2, Pattern.CASE_INSENSITIVE).matcher(str);
				flag = m.find();
            }
        }
        return flag;
	}
	
	/**
	 * 判断是否是url
	 * @param str
	 * @return
	 */
	public static boolean isUrl(String str){
		Pattern p = Pattern.compile("^(http|www|ftp|https|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$");
		Matcher m = p.matcher(str);
		return m.matches();
	}
}
