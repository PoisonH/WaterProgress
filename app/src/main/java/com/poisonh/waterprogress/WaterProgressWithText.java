package com.poisonh.waterprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 带文字的圆形进度
 * Created by PoisonH on 2016/8/4.
 */
public class WaterProgressWithText extends View
{
    private RectF rectF;
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

    public WaterProgressWithText(Context context)
    {
        this(context, null);
    }

    public WaterProgressWithText(Context context, AttributeSet atts)
    {
        this(context, atts, 0);
    }

    public WaterProgressWithText(Context context, AttributeSet atts, int style)
    {
        super(context, atts, style);
        initAttr(context, atts);
        init();
    }

    private void initAttr(Context context, AttributeSet atts)
    {
        TypedArray mTa = context.obtainStyledAttributes(atts, R.styleable.WaterProgressWithText);
        mScorePaintTextSize = mTa.getInteger(R.styleable.WaterProgressWithText_score_txt_size, 10);
        mScoreUnitPaintTextSize = mTa.getInteger(R.styleable.WaterProgressWithText_score_unit_txt_size, 10);
        mTipsPaintTextSize = mTa.getInteger(R.styleable.WaterProgressWithText_tips_txt_size, 10);

        mScoreTextColor = mTa.getColor(R.styleable.WaterProgressWithText_score_txt_color, context.getResources().getColor(R.color.colorAccent));
        mScoreUnitTextColor = mTa.getColor(R.styleable.WaterProgressWithText_score_unit_txt_color, context.getResources().getColor(R.color.colorPrimary));
        mTipsTextColor = mTa.getColor(R.styleable.WaterProgressWithText_tips_txt_color, context.getResources().getColor(R.color.mycolor));
        mStrTipsText = mTa.getString(R.styleable.WaterProgressWithText_tips_txt);


        mTa.recycle();
    }

    /**
     * 初始化资源
     */
    private void init()
    {
        rectF = new RectF();

        mCirclePaint = new Paint();
        //设置画笔颜色
        mCirclePaint.setColor(Color.BLUE);
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rectF.set(0, 0, MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        //圆半径
        int radius = getWidth() / 2 - 10;

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mCirclePaint);

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


//        float yHeight = 40 / (float) 100 * getHeight();
//        float angle = (float) (Math.acos((radius - yHeight) / radius) * 180 / Math.PI);
//        canvas.drawArc(rectF, 270 - angle, angle * 2, false, mWaterPaint);
//
//        float startAngle = 90 + angle;
//        float sweepAngle = 360 - angle * 2;
//        canvas.drawArc(rectF, sweepAngle, startAngle, false, mWaterPaint);

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
