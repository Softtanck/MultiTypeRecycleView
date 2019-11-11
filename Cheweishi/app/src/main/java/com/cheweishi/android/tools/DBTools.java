package com.cheweishi.android.tools;

import java.util.List;

import android.content.Context;

import com.cheweishi.android.activity.BaseActivity;
import com.cheweishi.android.entity.LoginResponse;
import com.cheweishi.android.utils.StringUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

/**
 * 数据库工具类
 *
 * @author 大森
 */
public class DBTools {
    public static DBTools dbTools;
    public static DbUtils db;

    /**
     * 获取数据库
     *
     * @param mContext
     * @return
     */
    public static DBTools getInstance(Context mContext) {
        if (StringUtil.isEmpty(dbTools)) {
            dbTools = new DBTools();
        }
        if (StringUtil.isEmpty(db)) {
            db = DbUtils.create(mContext);
        }

        return dbTools;
    }

    /**
     * 数据库保存对象
     *
     * @param object
     */
    public void save(Object object) {
        try {
            db.save(object);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除表
     *
     * @param object
     */
    public void delete(Class<?> object) {
        try {
            db.dropTable(object);
//			db.deleteAll(object);
            if (object == LoginResponse.class) {
                BaseActivity.loginResponse = null;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过类型查找
     *
     * @param object
     * @return
     */
    public List<?> findAll(Class<?> object) {
        try {
            return db.findAll(object);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过查找key = value的对象
     *
     * @param object
     * @param key
     * @param value
     * @return
     */
    public Object findFirst(Class<?> object, String key, String value) {
        try {
            return db.findFirst(Selector.from(object).where(key, "=", value));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过id删除对象
     *
     * @param object
     * @param idValue
     */
    public void deleteById(Class<?> object, String idValue) {
        try {
            db.deleteById(object, idValue);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public static void destory() {

        db = null;
        dbTools = null;
    }

}
