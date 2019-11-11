//package com.cheweishi.android.tools;
//
//import android.content.Context;
//import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.onekeyshare.OnekeyShare;
//import cn.sharesdk.tencent.qq.QQ;
//import cn.sharesdk.wechat.favorite.WechatFavorite;
//
//public class ShareTools {
//	public static void showShare(Context context) {
//		ShareSDK.initSDK(context);
//		final OnekeyShare oks = new OnekeyShare();
//		oks.setTitle("ggod");
//		oks.setTitleUrl("http://mob.com");
//		oks.setText("text");
//		oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg");
//		oks.setUrl("http://www.mob.com");
//		oks.disableSSOWhenAuthorize();
//		oks.show(context);
//	}
//
//	public static void showShare(Context context, String title, String content,
//			String titleUrl, String imgUrl) {
//		OnekeyShare oks = new OnekeyShare();
//		oks.disableSSOWhenAuthorize();
//		oks.setTitle(title);
//		oks.addHiddenPlatform(QQ.NAME);
//		oks.addHiddenPlatform(WechatFavorite.NAME);
//		oks.setTitleUrl(titleUrl);
//		oks.setText(content);
//		oks.setDialogMode();
//		oks.setUrl(titleUrl);
//		oks.show(context);
//	}
//}
