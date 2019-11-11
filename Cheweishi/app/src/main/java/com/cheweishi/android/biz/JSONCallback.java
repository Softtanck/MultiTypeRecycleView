package com.cheweishi.android.biz;


import java.io.File;

import com.lidroid.xutils.http.ResponseInfo;

/**
 * JSON数据和下载文件回调接口
 *
 * @author 胡健
 * @time 2015.6.15
 */
public interface JSONCallback {
    /**
     * json数据返回
     *
     * @param type
     * @param data
     */
    void receive(int type, String data);

    /**
     * 下载文件的返回
     *
     * @param type
     * @param arg0
     */
    void downFile(int type, ResponseInfo<File> arg0);


    /**
     * 新的Json数据
     *
     * @param TAG
     * @param data
     */
    void receive(String TAG, String data);


    /**
     * 新的Json数据
     *
     * @param data
     */
    void receive(String data);


    /**
     * 错误信息
     *
     * @param errorMsg
     */
    void error(String errorMsg);
}
