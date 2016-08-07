package com.poisonh.waterprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


/**
 * 水波圆形view
 */
public class WaterProgressWithText extends View
{

    //圆画笔
    private Paint mCirclePaint;
    //评分画笔
    private Paint mScorePaint;
    //评分单位画笔
    private Paint mScoreUnitPaint;
    //提示文字画笔
    private Paint mTipsPaint;

    //水纹画笔
    private Paint mWaterPaint;

    //评分文字大小
    private int mScorePaintTextSize = 100;
    //评分单位文字大小
    private int mScoreUnitPaintTextSize = 60;
    //提示文字大小
    private int mTipsPaintTextSize = 80;

    //评分文字颜色
    private int mScoreTextColor;
    //评分单位文字颜色
    private int mScoreUnitTextColor;
    //提示文字颜色
    private int mTipsTextColor;
    //提示文字
    private String mStrTipsText;

    //当前评分
    private int mScore = 300;


    private static final int MSG_WAVE = 10;
    /**
     * 默认内环半径100dp
     */
    private int mRadius = dpToPx(100);

    /**
     * 外环的宽度
     */
    private int mStrokeWidth = dpToPx(6);

    private Paint mPaintInnerCircle;

    private Paint mPaintWater;

    /**
     * 当前进度，或者百分比
     */
    private int progress = 30;

    /**
     * 中心文字的范围大小
     */
    private Rect mRectProgress = new Rect();

    /**
     * 提示文字的范围大小
     */
    private Rect mRectTips = new Rect();

    private String mTipString;
    private int mBorderSize;

    /**
     * View中心点的坐标
     */
    private int mCenterXY;

    /**
     * 记录所有的波纹的点的位置
     */
    private Path mPath = new Path();

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            mOffsetX = mOffsetX + 3;
            if (mOffsetX > 10000)
            {
                mOffsetX = 0;
            }
            invalidate();
        }
    };

    /**
     * 每次重新绘制的时候，X轴的偏移量
     */
    private float mOffsetX = 0;

    /**
     * 波浪的高度
     */
    private double mWaveHeight = 8;

    private boolean isWaving = true;

    public WaterProgressWithText(Context context)
    {
        this(context, null);
    }

    public WaterProgressWithText(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public WaterProgressWithText(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
        init();
    }

    /**
     * 初始化资源
     */
    private void init()
    {
        mCirclePaint = new Paint();
        //设置画笔颜色
        mCirclePaint.setColor(Color.YELLOW);
        mCirclePaint.setStrokeWidth(8);
        // 设置是否使用抗锯齿功能，会消耗较大资源，绘制图形速度会变慢。
        mCirclePaint.setAntiAlias(true);
        //设置空心
        mCirclePaint.setStyle(Paint.Style.STROKE);


        mScorePaint = new Paint();
        mScorePaint.setStyle(Paint.Style.FILL);
        mScorePaint.setTextSize(mScorePaintTextSize);
        mScorePaint.setAntiAlias(true);
        mScorePaint.setColor(mScoreTextColor);


        mScoreUnitPaint = new Paint();
        mScoreUnitPaint.setStyle(Paint.Style.FILL);
        mScoreUnitPaint.setTextSize(mScoreUnitPaintTextSize);
        mScoreUnitPaint.setAntiAlias(true);
        mScoreUnitPaint.setColor(mScoreUnitTextColor);

        mTipsPaint = new Paint();
        mTipsPaint.setStyle(Paint.Style.FILL);
        mTipsPaint.setTextSize(mTipsPaintTextSize);
        mTipsPaint.setAntiAlias(true);
        mTipsPaint.setColor(mTipsTextColor);

        mWaterPaint = new Paint();
        mWaterPaint.setAntiAlias(true);
        mWaterPaint.setColor(getResources().getColor(R.color.mycolor));
        mWaterPaint.setStyle(Paint.Style.FILL);
    }

    private void initView(Context cxt, AttributeSet attrs)
    {
        TypedArray ta = cxt.obtainStyledAttributes(attrs, R.styleable.WaveViewStyle);
        int waveColor = ta.getColor(R.styleable.WaveViewStyle_waveColor, 0x9045A348);
        int innerBgColor = ta.getColor(R.styleable.WaveViewStyle_innerBgColor, 0x0045A348);
        int outBgColor = ta.getColor(R.styleable.WaveViewStyle_innerBgColor, 0xFF4CAF50);
        int mainTextColor = ta.getColor(R.styleable.WaveViewStyle_mainTextSize, 0xFFFFFFFF);
        int mainTextSize = ta.getDimensionPixelOffset(R.styleable.WaveViewStyle_mainTextSize, spToPx(40));

        mScorePaintTextSize = ta.getInteger(R.styleable.WaveViewStyle_score_txt_size, 10);
        mScoreUnitPaintTextSize = ta.getInteger(R.styleable.WaveViewStyle_score_unit_txt_size, 10);
        mTipsPaintTextSize = ta.getInteger(R.styleable.WaveViewStyle_tips_txt_size, 10);

        mScoreTextColor = ta.getColor(R.styleable.WaveViewStyle_score_txt_color, cxt.getResources().getColor(R.color.colorAccent));
        mScoreUnitTextColor = ta.getColor(R.styleable.WaveViewStyle_score_unit_txt_color, cxt.getResources().getColor(R.color.colorPrimary));
        mTipsTextColor = ta.getColor(R.styleable.WaveViewStyle_tips_txt_color, cxt.getResources().getColor(R.color.mycolor));
        mStrTipsText = ta.getString(R.styleable.WaveViewStyle_tips_txt);


        mTipString = ta.getString(R.styleable.WaveViewStyle_topInfoTextValue);
        ta.recycle();

        if (TextUtils.isEmpty(mTipString)) mTipString = cxt.getString(R.string.tip_info_value);
        mBorderSize = dpToPx(1);

        mPaintInnerCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintInnerCircle.setDither(true);
        mPaintInnerCircle.setColor(innerBgColor);
        mPaintInnerCircle.setStyle(Paint.Style.FILL);

        mPaintWater = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintWater.setDither(true);
        mPaintWater.setColor(waveColor);
        mPaintWater.setStyle(Paint.Style.FILL);
    }

    /**
     * 布局最好是一个正方形，绘制的区域将在屏幕的中心位置
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width, height;

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        } else
        {
            width = mRadius * 2 + mStrokeWidth * 2;
        }

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        } else
        {
            height = mRadius * 2 + mStrokeWidth * 2;
        }

        int size = Math.min(width, height);
        setMeasuredDimension(size, size);

        mCenterXY = size / 2;

        if (mCenterXY <= mRadius + mStrokeWidth)
        {
            mRadius = mCenterXY - mStrokeWidth;
        }
    }

    public void setWaveColor(int color)
    {
        mPaintWater.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {


        //圆半径
        int radius = getWidth() / 2 - 10;

        canvas.drawCircle(mCenterXY, mCenterXY, radius, mCirclePaint);

        //通过评分画笔，获取到评分文字高度
        float ScoreHeight = mScorePaint.descent() + mScorePaint.ascent();

        //绘制当前评分
        canvas.drawText(getmScore(), (getWidth() - mScorePaint.measureText(getmScore())) / 2, (getHeight() - ScoreHeight) / 2 + ScoreHeight, mScorePaint);

        //绘制当前评分单位
        canvas.drawText("分", radius + mScorePaint.measureText(getmScore()) / 2 + mScoreUnitPaint.measureText("分") / 2, (getHeight() - ScoreHeight) / 2 + ScoreHeight, mScoreUnitPaint);

        //通过评分画笔，获取到评分文字高度
        float TipsHeight = mTipsPaint.descent() + mTipsPaint.ascent();

        //绘制提示文字
        canvas.drawText(mStrTipsText, (getWidth() - mTipsPaint.measureText(mStrTipsText)) / 2, getHeight() / 2 - TipsHeight, mTipsPaint);

        //        super.onDraw(canvas);

        //        //绘制内圆
        canvas.drawCircle(mCenterXY, mCenterXY, mRadius, mPaintInnerCircle);

        //绘制波浪
        createWavePath(progress);
        canvas.drawPath(mPath, mPaintWater);

        if (isWaving)
        {
            mHandler.sendEmptyMessageDelayed(MSG_WAVE, 10);
        }
    }

    /**
     * 创建波浪，主要是通过path来创建
     */
    private void createWavePath(int progress)
    {
        mPath.reset();
        float absY;
        float sweepAngle;
        float startAngle;
        float absX;
        if (progress >= 50)
        {
            absY = mRadius * 2 * (progress * 1.0f / 100);
            float angle = (float) (Math.asin((absY - mRadius) * 1.0f / mRadius) * 180 / Math.PI);
            absX = (float) (mRadius * Math.cos(angle * Math.PI / 180));
            sweepAngle = angle * 2 + 180;
            startAngle = -angle;
        } else
        {
            absY = mRadius * 2 * (progress * 1.0f / 100);
            float angle = (float) (Math.acos((mRadius - absY) * 1.0f / mRadius) * 180 / Math.PI);
            absX = (float) (mRadius * Math.sin(angle * Math.PI / 180));
            sweepAngle = angle * 2;
            startAngle = 90 - angle;
        }

        int startX = (int) (mRadius - absX) + mStrokeWidth;

        float x, y;
        for (int i = 0; i < absX * 2; i++)
        {
            x = i + startX;
            y = (float) (mWaveHeight * Math.sin((i * 1.5f + mOffsetX) / mRadius * Math.PI)) + (mRadius * 2
                    - absY) + mStrokeWidth;
            if (i == 0)
            {
                mPath.moveTo(x, y);
            } else
            {
                mPath.quadTo(x, y, x + 1, y);
            }
        }
        RectF rectF = new RectF(mStrokeWidth, mStrokeWidth, mRadius * 2 + mStrokeWidth,
                mRadius * 2 + mStrokeWidth);
        mPath.arcTo(rectF, startAngle, sweepAngle);
        mPath.close();
    }

    /**
     * 设置进度
     */
    public void setProgress(int progress)
    {
        this.progress = progress;
        invalidate();
    }

    public void setTipInfoText(String str)
    {
        mTipString = str;
    }

    /**
     * 开始动画
     */
    public void startWave()
    {
        if (!isWaving && mHandler != null)
        {
            mHandler.sendEmptyMessage(MSG_WAVE);
        }
        isWaving = true;
    }

    /**
     * 关闭动画
     */
    public void stopWave()
    {
        if (mHandler != null)
        {
            mHandler.removeMessages(MSG_WAVE);
        }
        isWaving = false;
    }

    private int dpToPx(int dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private int spToPx(int sp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }

    /**
     * 设置提示字体大小
     *
     * @param mTipsPaintTextSize
     */
    public void setmTipsPaintTextSize(int mTipsPaintTextSize)
    {
        this.mTipsPaintTextSize = mTipsPaintTextSize;
        invalidate();
    }


    /**
     * 设置单位字体大小
     *
     * @param mScoreUnitPaintTextSize
     */
    public void setmScoreUnitPaintTextSize(int mScoreUnitPaintTextSize)
    {
        this.mScoreUnitPaintTextSize = mScoreUnitPaintTextSize;
        invalidate();
    }

    /**
     * 设置分数字体大小
     *
     * @param mScorePaintTextSize
     */

    public void setmScorePaintTextSize(int mScorePaintTextSize)
    {
        this.mScorePaintTextSize = mScorePaintTextSize;
        invalidate();
    }

    /**
     * 设置当前评分
     *
     * @param mScore
     */
    public void setmScore(int mScore)
    {

        this.mScore = mScore;
        invalidate();

    }

    /**
     * 获取当前评分
     *
     * @return
     */
    private String getmScore()
    {
        if (mScore < 300)
        {
            return 300 + "";
        }
        if (mScore > 900)
        {
            return 900 + "";
        } else
        {
            return mScore + "";
        }
    }

}
