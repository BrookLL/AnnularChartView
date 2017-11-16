package com.riverlet.ringview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by
 * Author:riverlet.liu
 * DATE:17/4/28.
 * Time:上午9:25
 */

public class AnnularChartView extends View {

    /**
     * 默认颜色
     */
    private static final int[] DEFAULT_COLOR = new int[]{0xff82B8FF, 0xffFF7F78, 0xffFFAE72, 0xff74D1B1, 0xffC38AFC};
    /**
     * 圆环半径，以内环算
     */
    private int innerRadius;
    /**
     * 圆环厚度
     */
    private int ringWidth;
    /**
     * 画笔数组
     */
    private Paint[] paints;
    /**
     * 画笔数组对应的颜色
     */
    private int[] colors = DEFAULT_COLOR;
    /**
     * 圆环圆心x坐标
     */
    private int centerX;
    /**
     * 圆环圆心y坐标
     */
    private int centerY;
    /**
     * 圆环范围
     */
    private RectF oval;
    /**
     * 每个数据对应的角度
     */
    private int[] angles;
    /**
     * 数据
     */
    private float[] datas;
    /**
     * 动画用的进度
     */
    private float progress;
    /**
     * 动画
     */
    private ObjectAnimator animator;

    public AnnularChartView(Context context) {
        this(context, null);
    }

    public AnnularChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnnularChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAnimator();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initRectF(getMeasuredWidth(), getMeasuredHeight());
    }

    /**
     * 计算圆环的范围
     *
     * @param w
     * @param h
     */
    private void initRectF(int w, int h) {
        if (w == 0 && h == 0) {
            return;
        }
        centerX = (int) ((float) w / 2);
        centerY = (int) ((float) h / 2);
        innerRadius = (int) ((float) w / 2 / 89 * 64);
        ringWidth = (int) ((float) w / 2 / 89 * 25);
        oval = new RectF(centerX - innerRadius, centerY - innerRadius, centerX + innerRadius, centerY + innerRadius);
    }

    /**
     * 初始化paint
     */
    private void initPaints() {
        if (datas == null) {
            angles = null;
        } else {
            float total = 0;
            for (float data : datas) {
                total += data;
            }
            if (total <= 0) {
                angles = null;
            } else {
                angles = new int[datas.length];
                int sumAngles = 0;
                for (int i = 0; i < datas.length; i++) {
                    float angle;
                    if (i == datas.length - 1) {
                        angles[i] = 360 - sumAngles;
                        Log.v("setData", angles[i] + "");
                    } else {
                        angle = datas[i] / total * 360;
                        if (angle < 1) {
                            angles[i] = 1;
                        } else {
                            angles[i] = Math.round(angle);
                        }
                        sumAngles += angles[i];
                        Log.v("setData", angles[i] + "");
                    }
                }
            }
        }
        if (angles != null) {
            //用于定义的圆弧的形状和大小的界限
            paints = new Paint[angles.length];
            for (int i = 0; i < angles.length; i++) {
                Paint paint = new Paint();
                paint.setColor(colors[i % colors.length]);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(25);
                paint.setAntiAlias(true);
                paints[i] = paint;
            }
        }
        animStart();
    }

    private void initAnimator() {
        progress = 0;
        animator = ObjectAnimator.ofFloat(this, "progress", 0f, 1.0f);
        animator.setDuration(800);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (oval == null) {
            initRectF(getWidth(), getHeight());
        }
        int lastAngle = 0;
        int nums = angles == null ? 0 : angles.length;
        if (nums > 0) {
            for (int i = 0; i < nums; i++) {
                if (i > 0) {
                    lastAngle = (int) (lastAngle + angles[i - 1] * progress);
                }
                paints[i].setStrokeWidth(ringWidth);
                if (angles[i] > 0) {
                    canvas.drawArc(oval, 270 + lastAngle, (angles[i] + 1) * progress, false, paints[i]);
                }
            }
        } else {
            Paint paint = new Paint();
            paint.setColor(0xffa0a0a0);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(ringWidth);
            paint.setAntiAlias(true);
            canvas.drawArc(oval, 270, 360 * progress + 1, false, paint);
        }
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }


    /**
     * 开始动画
     */
    public void animStart() {
        if (animator.isStarted()) {
            animator.cancel();
        }
        animator.start();
    }

    public void setData(float[] datas) {
        this.datas = datas;
        initPaints();
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public void setAnimator(ObjectAnimator animator) {
        this.animator = animator;
    }
}
