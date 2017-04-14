package com.cdd.circlepercentview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/4/15 0015 00:58.
 * From url: http://www.jb51.net/article/108273.htm
 */

public class CirclePercentView extends View {
    private int mCircleColor;
    private int mArcColor;
    private int mArcWidth;
    private int mPercentTextColor;
    private int mPercentTextSize;
    private int mRadius;
    private Paint mCirclePaint;
    private Paint mArcPaint;
    private Paint mPercentTextPaint;
    private RectF mArcRectF;
    private Rect mTextBound;
    private float mCurPercent;
    private OnClickListener mOnClickListener;

    public CirclePercentView(Context context) {
        this(context, null);
    }

    public CirclePercentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs, defStyleAttr);
        initPaint();

        //在构造方法中
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(CirclePercentView.this);
                }
            }
        });
    }

    /**
     * 计算自定义View的宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measuredDimension(widthMeasureSpec), measuredDimension(heightMeasureSpec));
    }

    private int measuredDimension(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);


        if (specMode == MeasureSpec.EXACTLY) { //精确地，代表宽高为定值或者match_parent时
            result = specSize;
        } else {
            result = 2 * mRadius;
            if (specMode == MeasureSpec.AT_MOST) {//最大地，代表宽高为wrap_content时
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    /**
     * 绘制圆、圆弧和百分比文本
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画圆
        canvas.drawCircle(getWidth() / 2,
                getHeight() / 2,
                mRadius,
                mCirclePaint);

        //画圆弧
        mArcRectF.set(getWidth() / 2 - mRadius + mArcWidth / 2,
                getHeight() / 2 - mRadius + mArcWidth / 2,
                getWidth() / 2 + mRadius - mArcWidth / 2,
                getHeight() / 2 + mRadius - mArcWidth / 2);
        canvas.drawArc(mArcRectF,
                270,
                360 * mCurPercent / 100,
                false,
                mArcPaint);

        String text = mCurPercent + "%";
        //计算文本的宽高
        mPercentTextPaint.getTextBounds(text,
                0,
                String.valueOf(text).length(),
                mTextBound
        );
        //画百分比文字
        canvas.drawText(text,
                getWidth() / 2 - mTextBound.width() / 2,
                getHeight() / 2 + mTextBound.height() / 2,
                mPercentTextPaint
        );

    }

    private void initPaint() {
        //画圆
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mCircleColor);

        //画圆弧
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcWidth);
        mArcPaint.setColor(mArcColor);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);//使圆弧两端圆滑

        //画笔显示进度文本
        mPercentTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPercentTextPaint.setColor(mPercentTextColor);
        mPercentTextPaint.setStyle(Paint.Style.STROKE);
        mPercentTextPaint.setTextSize(mPercentTextSize);

        //圆弧的外界矩形
        mArcRectF = new RectF();
        //文本的范围矩形
        mTextBound = new Rect();

    }

    /**
     * 获取自定义属性
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView, defStyleAttr, 0);
        mCircleColor = ta.getColor(R.styleable.CirclePercentView_circleBackground, 0xff8e29fa);
        mArcColor = ta.getColor(R.styleable.CirclePercentView_arcColor, 0xffffee00);
        mArcWidth = ta.getDimensionPixelSize(R.styleable.CirclePercentView_arcWidth, DensityUtils.dp2px(context, 16));
        mPercentTextColor = ta.getColor(R.styleable.CirclePercentView_arcColor, 0xffffee00);
        mPercentTextSize = ta.getDimensionPixelSize(R.styleable.CirclePercentView_percentTextSize, DensityUtils.sp2px(context, 16));
        mRadius = ta.getDimensionPixelSize(R.styleable.CirclePercentView_radius, DensityUtils.dp2px(context, 100));
        ta.recycle();
    }

    /**
     * 设置当前百分比进度
     *
     * @param curPercent
     */
    public void setCurPercent(float curPercent) {
        ValueAnimator anim = ValueAnimator.ofFloat(mCurPercent, curPercent);
        //动画时长有百分比大小决定
        anim.setDuration((long) (Math.abs(mCurPercent - curPercent) * 20));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                //四舍五入保留到小数点后两位
                mCurPercent = (float) (Math.round(value * 10)) / 10;


                //重绘,重走onDraw()
                invalidate();
            }
        });
        anim.start();
    }

    public void setOnClickCirclePercentViewListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

}
