package com.cheweishi.android.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.view.View;

import com.cheweishi.android.R;
import com.cheweishi.android.utils.DateUtils;
import com.cheweishi.android.utils.StringUtil;

/**
 * 车报告View
 *
 * @author zhangq<br>
 *         只通过new的方式创建<br>
 *         com.cheweishi.android.widget.ReportCircleView
 */

public class ReportCircleView extends View {
    private float text_dp_value_normal = 16;// 大号字体
    private float text_dp_value_small = 14;// 小号字体
    private Paint paint = new Paint();// 主画笔
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 字体画笔
    private int colorBg;
    private int colorText1;
    private int colorText2;
    private int width;
    private int height;

    ValueAnimator mAnimator = ValueAnimator.ofInt(0, 1);
    Matrix[] mMatrix = new Matrix[5];
    private float rotateSpeed = 1;
    public float overDegree;// 上次滑动的位置状态
    private float runDegree;// 滑动的角度
    /**
     * 五个圆的初始角度
     */
    private int[] originalDegree = {0, 72, 108, 36, 36};

    private int radius;// 背景圆
    public int bitR;// 大圆
    public int bitHW;// 中心方块_harf_width
    public int bitHH;// 中心方块_harf_height
    public int bitTextW;// 下方文字宽
    public int bitTextH;// 下方文字高
    /**
     * 旋转是否停止
     */
    private boolean isRotateStop;
    /**
     * 是否是平移动作
     */
    private boolean isTranslate = true;
    /**
     * 数据是否传入
     */
    private boolean isMessage = false;
    /**
     * 平移比例
     */
    private float transOffset = 1.0f;
    /**
     * 虚线dp值
     */
    private float dashPathDip;

    // 暴露几个数值
    public float suduW;
    public float suduH;
    public float bailiW;
    public float bailiH;
    public float lichengW;
    public float lichengH;
    public float shijianW;
    public float shijianH;
    public float youhaoW;
    public float youhaoH;
    public float centerW;
    public float centerH;
    public float textW;
    public float textH;

    // bitmap的两个数组长度一定要相同
    private int[] bitmapIds = {R.drawable.report_youhao2x,
            R.drawable.report_bailiyouhao2x, R.drawable.report_sudu2x,
            R.drawable.report_shijian2x, R.drawable.report_zonglicheng2x,
            R.drawable.report_weiyong2x, R.drawable.home_baogao_chakan};
    private Bitmap[] bitmaps;
    /**
     *
     */
    private int[] circleNameRid = {R.string.report_day_oil,
            R.string.report_avg_oil, R.string.report_avg_speed,
            R.string.report_drive_time, R.string.report_day_mile,
            R.string.report_day_free, R.string.report_mile_no_enough,
            R.string.report_your_score, R.string.report_score_unit};
    private String[] circleName;

    private String[] danwei = {"L", "L/100km", "km/h", "", "km", "¥", ""};
    private String[] values = {"0", "0", "0", "0", "0", "0", ""};

    public ReportCircleView(Context context) {
        super(context);
        initData(context);
    }

    /**
     * @param context
     * @param offsetDegree 偏移量，应该是72的整数倍
     */
    public ReportCircleView(Context context, int offsetDegree) {
        super(context);
        overDegree = offsetDegree;
        initData(context);
    }

    private void initData(Context context) {
        int i = 0;
        int len = bitmapIds.length;
        bitmaps = new Bitmap[len];
        for (; i < len; i++) {
            bitmaps[i] = getBitmap(context, bitmapIds[i]);
            if (i < 5) {
                mMatrix[i] = new Matrix();
            }
        }
        Resources res = context.getResources();
        getStringNeeded(res);
        getColorNeeded(res);

        bitR = bitmaps[0].getWidth() / 2;
        bitHW = bitmaps[i - 2].getWidth() / 2;
        bitHH = bitmaps[i - 2].getHeight() / 2;

        bitTextW = bitmaps[i - 1].getWidth();
        bitTextH = bitmaps[i - 1].getHeight();

        dashPathDip = DateUtils.dip2Px(getContext(), 5f);
        setValues(values);
        isMessage = false;

        mAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                if (!isTranslate && isRotateStop) {
                    if (runDegree % 72 == 0) {
                        mAnimator.cancel();
                    } else if (runDegree % 72 > 36) {
                        runDegree += rotateSpeed;
                    } else {
                        runDegree -= rotateSpeed;
                    }
                } else if (isTranslate && isRotateStop) {
                    mAnimator.cancel();
                }

                // 没有停止的时候，根据runDegree的改变而转动
                invalidate();
            }
        });
        mAnimator.setDuration(3000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);

    }

    private void getColorNeeded(Resources res) {
        colorBg = res.getColor(R.color.reportcircle);
        colorText1 = res.getColor(R.color.black);
        colorText2 = res.getColor(R.color.orange_text_color);
    }

    /**
     * 获取需要的字符串
     */
    private void getStringNeeded(Resources res) {
        int strLen = circleNameRid.length;
        circleName = new String[strLen];

        for (int i = 0; i < strLen; i++) {
            circleName[i] = res.getString(circleNameRid[i]);
        }
    }

    /**
     * 获取需要的图片资源
     *
     * @param context
     * @param rid
     * @return
     */
    private Bitmap getBitmap(Context context, int rid) {
        return BitmapFactory.decodeResource(context.getResources(), rid);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(colorBg);

        width = getWidth();
        height = getHeight();
        radius = (int) ((width < height ? width : height) * 0.36 * transOffset);

        paint.setStrokeWidth(4);

        PathEffect effect = new DashPathEffect(new float[]{2 * dashPathDip,
                dashPathDip, 2 * dashPathDip, dashPathDip}, 1);
        paint.setPathEffect(effect);
        canvas.drawCircle(width / 2, height * 0.4f, radius, paint);

        paint.setPathEffect(null);
        // 速度
        suduW = (float) (width / 2 - radius
                * mathSin(originalDegree[0] - runDegree - overDegree) - bitR);
        suduH = (float) (height * 0.4f
                - mathCos(originalDegree[0] - runDegree - overDegree) * radius - bitR);
        drawItem(canvas, suduW, suduH, 0);

        // 平均油耗（百里油耗）
        bailiW = (float) (width / 2 + radius
                * mathSin(originalDegree[1] + runDegree + overDegree) - bitR);
        bailiH = (float) (height * 0.4f
                - mathCos(originalDegree[1] + runDegree + overDegree) * radius - bitR);
        drawItem(canvas, bailiW, bailiH, 1);

        // 里程
        lichengW = (float) (width / 2
                - mathSin(originalDegree[2] + runDegree + overDegree) * radius - bitR);
        lichengH = (float) (height * 0.4f + radius
                * mathCos(originalDegree[2] + runDegree + overDegree) - bitR);
        drawItem(canvas, lichengW, lichengH, 2);
        // 时间
        shijianW = (float) (width / 2
                + mathSin(originalDegree[3] - runDegree - overDegree) * radius - bitR);
        shijianH = (float) (height * 0.4f + radius
                * mathCos(originalDegree[3] - runDegree - overDegree) - bitR);
        drawItem(canvas, shijianW, shijianH, 3);
        // 油耗
        youhaoW = (float) (width / 2
                - mathSin(originalDegree[4] + runDegree + overDegree) * radius - bitR);
        youhaoH = (float) (height * 0.4f
                + mathCos(originalDegree[4] + runDegree + overDegree) * radius - bitR);
        drawItem(canvas, youhaoW, youhaoH, 4);
        // 中心
        centerW = width / 2 - bitHW;
        centerH = height * 0.4f - bitHH;
        canvas.drawBitmap(bitmaps[5], centerW, centerH, null);

        if (!isTranslate && !mAnimator.isRunning()) {
            overDegree += runDegree;
            overDegree = overDegree % 360;
            runDegree = 0;
        }

        if (mAnimator.isRunning()) {
            return;
        }
        textPaint.setTextSize(DateUtils.dip2Px(getContext(),
                text_dp_value_normal));
        textPaint.setColor(colorText1);
        textPaint.setTextAlign(Align.CENTER);
        FontMetrics metrics = textPaint.getFontMetrics();
        float textTop = metrics.top;
        float textButtom = metrics.bottom;

        float lineH = Math.abs(textTop - textButtom);

        canvas.drawText(circleName[0], suduW + bitR, suduH + bitR * 2 + lineH,
                textPaint);
        canvas.drawText(circleName[1], bailiW + bitR,
                bailiH + bitR * 2 + lineH, textPaint);
        canvas.drawText(circleName[2], lichengW + bitR, lichengH + bitR * 2
                + lineH, textPaint);
        canvas.drawText(circleName[3], shijianW + bitR, shijianH + bitR * 2
                + lineH, textPaint);
        canvas.drawText(circleName[4], youhaoW + bitR, youhaoH + bitR * 2
                + lineH, textPaint);
        canvas.drawText(circleName[5], centerW + bitHW, centerH + bitHH * 2
                + lineH, textPaint);

        textPaint.setColor(colorText2);
        canvas.drawText(values[0], suduW + bitR, suduH + bitR * 2 + lineH * 2,
                textPaint);
        canvas.drawText(values[1], bailiW + bitR,
                bailiH + bitR * 2 + lineH * 2, textPaint);
        canvas.drawText(values[2], lichengW + bitR, lichengH + bitR * 2 + lineH
                * 2, textPaint);
        if (null != values[3] && !"".equals(values[3])) {
            int temp = Integer.valueOf(values[3]) / 60;
            canvas.drawText(temp + "Min", shijianW + bitR, shijianH + bitR * 2 + lineH
                    * 2, textPaint);
        } else {
            canvas.drawText("0Min", shijianW + bitR, shijianH + bitR * 2 + lineH
                    * 2, textPaint);
        }

        canvas.drawText(values[4], youhaoW + bitR, youhaoH + bitR * 2 + lineH
                * 2, textPaint);
        canvas.drawText(values[5], centerW + bitHW, centerH + bitHH * 2 + lineH
                * 2, textPaint);

        if (isMessage) {
            double d;
            try {
                d = StringUtil.getDouble(values[4].split(danwei[4])[0]);
            } catch (Exception e) {
                return;
            }

            if (d <= 3) {
                textPaint.setTextSize(DateUtils.dip2Px(getContext(),
                        text_dp_value_small));
                textPaint.setColor(colorText1);
                canvas.drawText(circleName[6], width / 2,
                        (float) (height * 0.9), textPaint);
                return;
            }

            String s1 = circleName[7];
            String s2 = values[6] + circleName[8];
            float f1 = textPaint.measureText(s1);
            float f2 = textPaint.measureText(s2);

            canvas.drawText(s2, width / 2 + f1 / 2, (float) (height * 0.9),
                    textPaint);
            textPaint.setColor(colorText1);
            textPaint.setTextSize(DateUtils.dip2Px(getContext(),
                    text_dp_value_small));
            canvas.drawText(s1, width / 2 - f2 / 2, (float) (height * 0.9),
                    textPaint);

            textW = (width - bitmaps[6].getWidth()) / 2;
            textH = (float) (height * 0.9) + lineH / 2;
            canvas.drawBitmap(bitmaps[6], textW, textH, null);
        }

    }

    private void drawItem(Canvas canvas, float suduW, float suduH, int position) {
        mMatrix[position].setTranslate(-bitR, -bitR);
        // 小圆自旋转
        // mMatrix[position].postRotate(runDegree * 5);
        mMatrix[position].postTranslate(bitR + suduW, bitR + suduH);
        canvas.drawBitmap(bitmaps[position], mMatrix[position], null);
    }

    /**
     * length=6 外部对circle赋值
     *
     * @param values
     */
    public void setValues(String[] values) {
        for (int i = 0; i < danwei.length; i++) {
            if (i == 5) {
                this.values[i] = danwei[i] + values[i];
            } else {
                this.values[i] = values[i] + danwei[i];
            }
        }
        isMessage = true;
        invalidate();
    }

    /**
     * 计算sin值（度数）
     *
     * @param degree
     * @return
     */
    private double mathSin(float degree) {
        return Math.sin(Math.PI * degree / 180);
    }

    /**
     * 计算cos值（度数）
     *
     * @param degree
     * @return
     */
    private double mathCos(float degree) {
        return Math.cos(Math.PI * degree / 180);
    }

    /**
     * 开启动画
     */
    public void resume() {
        if (mAnimator.isRunning()) {
            return;
        }
        isRotateStop = false;
        mAnimator.start();
    }

    /**
     * 设置动画停止标志（当圆圈归位后停止动画）
     */
    public void pause() {
        isRotateStop = true;
    }

    /**
     * 实际转动角度（控制旋转）
     *
     * @param rotateSpeed
     */
    public void setRotateSpeed(int rotateSpeed) {
        this.runDegree = rotateSpeed;
    }

    /**
     * 标志是否是viewpager的平移，并做大圆缩小操作
     *
     * @param isTranslate
     */
    public void setIsTranslate(boolean isTranslate) {
        this.isTranslate = isTranslate;
    }

    /**
     * 大圆缩小比例，根据平移量而改变
     *
     * @param transOffset
     */
    public void setTransOffset(float transOffset) {
        this.transOffset = transOffset;
    }

}
