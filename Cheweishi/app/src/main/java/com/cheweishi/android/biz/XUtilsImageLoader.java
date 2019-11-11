package com.cheweishi.android.biz;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.cheweishi.android.R;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.utils.LogHelper;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 图片助手
 *
 * @author 健
 */
public class XUtilsImageLoader {
    private static XUtilsImageLoader mImageLoader;
//    private static Picasso picasso;

    /**
     * @param context
     * @param resid   0为默认图片
     */
    private XUtilsImageLoader(Context context, int resid) {
//        picasso = Picasso.with(context);
//        picasso.setDebugging(true);
    }

    private XUtilsImageLoader(Context context) {
    }
//
//
//    public static XUtilsImageLoader getInstance(Context con, int id) {
//        if (null == mImageLoader) {
//            synchronized (XUtilsImageLoader.class) {
//                if (null == mImageLoader)
//                    mImageLoader = new XUtilsImageLoader(con, id);
//            }
//        }
//        return mImageLoader;
//    }


    /**
     * @param context
     * @return
     */
    public static void getxUtilsImageLoader(Context context, int resid, ImageView imageView, String uri) {
        if (null == uri) {
            imageView.setImageResource(resid);
            return;
        }
        Picasso.with(context).load(NetInterface.IMG_URL + uri)
                .placeholder(resid)
                .error(resid)
                .config(Bitmap.Config.RGB_565)
                .into(imageView);
    }

    /**
     * @param context
     * @return
     */
    public static void getxUtilsImageLoaderNoData(Context context, int resid, ImageView imageView, String uri) {
        if (null == uri) {
            return;
        }
        Picasso.with(context).load(uri)
                .placeholder(resid)
                .error(resid)
                .config(Bitmap.Config.RGB_565)
                .into(imageView);
    }

    /**
     * @param context
     * @return
     */
    public static void getxUtilsImageLoader(Context context, int resid, int errorid, ImageView imageView, String uri) {
        if (null == uri) {
            imageView.setImageResource(errorid);
            return;
        }
        Picasso.with(context).load(NetInterface.IMG_URL + uri)
                .placeholder(resid)
                .error(errorid)
                .config(Bitmap.Config.RGB_565)
                .into(imageView);
    }


    /**
     * @param context
     * @return
     */
    public static void getHomeAdvImg(Context context, int resid, ImageView imageView, String uri) {
        if (null == uri) {
            imageView.setImageResource(resid);
            return;
        }
        Picasso.with(context).load(NetInterface.IMG_URL + uri).error(resid).config(Bitmap.Config.RGB_565).fit().tag(context).into(imageView);
    }

//    public static void getShopImg(Context context, int resid, ImageView imageView, String uri) {
//        if (null == uri) {
//            imageView.setImageResource(resid);
//            return;
//        }
//        Glide.with(context)
//                .load(NetInterface.IMG_URL + uri)
//                .error(resid)
//                .crossFade()
//                .into(imageView);
//    }

}
