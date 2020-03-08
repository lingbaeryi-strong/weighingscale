package com.weighingscale;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;


public class InstrumentView extends View {

    private int mWidth;
    private int mHeight;
    private TextPaint mTextPaint, mKgPaint;
    private Bitmap mZhenBitmap, mBgBitmap;
    private float progress = 0;
    private float weight = 0;
    private boolean needAnimator;
    private Matrix mMatrix;
    private String mStatus = "--";
    private String mData = "0";

    public InstrumentView(Context context) {
        this(context, null);
    }

    public InstrumentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InstrumentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);

        mKgPaint = new TextPaint();
        mKgPaint.setAntiAlias(true);
        mKgPaint.setColor(Color.WHITE);

        mZhenBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_zhen);//指针
        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_weight);//背景图

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        mWidth = MeasureSpec.getSize(widthMeasureSpec);
//        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = mBgBitmap.getWidth();
        mHeight = mBgBitmap.getHeight();

        mMatrix = new Matrix();
        mMatrix.postTranslate(mWidth / 2 - mZhenBitmap.getWidth() / 2, mHeight / 4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("progress", progress + "");
        drawInstrument(canvas);//背景图
        drawCalorie(canvas);//绘制中间的文字
        drawPointer(canvas);//指针

    }

    /*
     *
     * 圆弧中间文字
     * */
    private void drawCalorie(Canvas canvas) {
        mTextPaint.setTextSize(sp2px(15));
        int weightWidth = (int) mTextPaint.measureText("体重");
        canvas.drawText("体重", (mWidth - weightWidth) / 2, dp2px(40), mTextPaint);

        mTextPaint.setTextSize(sp2px(38));
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        int numberWidth = (int) mTextPaint.measureText(weight + "");

        mKgPaint.setTextSize(sp2px(12));
        int textWidth = (int) mTextPaint.measureText("kg");

        canvas.drawText(weight + "", (mWidth - numberWidth - textWidth) / 2 + dp2px(15), mHeight / 2 + dp2px(10), mTextPaint);
        canvas.drawText("kg", (mWidth - numberWidth - textWidth) / 2 + numberWidth + dp2px(20), mHeight / 2 + dp2px(10), mKgPaint);


        mTextPaint.setTextSize(sp2px(15));
        mTextPaint.setTypeface(Typeface.DEFAULT);
        canvas.drawText(mStatus, (mWidth - mTextPaint.measureText(mStatus)) / 2, dp2px(115), mTextPaint);
        canvas.drawText(mData, (mWidth - mTextPaint.measureText(mData)) / 2, dp2px(148), mTextPaint);

    }

    /*
     * 绘制指针
     * */
    private void drawPointer(Canvas canvas) {
        mMatrix.reset();
        mMatrix.postTranslate(mWidth / 2 - mZhenBitmap.getWidth() / 2, mHeight / 40);
        mMatrix.postRotate(230 + progress, mWidth / 2f, mHeight / 2f);
        canvas.drawBitmap(mZhenBitmap, mMatrix, null);
    }


    private void drawInstrument(Canvas canvas) {
        // 绘制背景
        canvas.drawBitmap(mBgBitmap, 0, 0, null);
    }

    public void setData(float hasEat, String status, String data, boolean needAnimators) {
        weight = progress = hasEat;
        needAnimator = needAnimators;
        mStatus = status;
        mData = data;

        if (needAnimator) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
            objectAnimator.setDuration(2000);
            objectAnimator.setInterpolator(new DecelerateInterpolator());
            objectAnimator.start();
        } else {
            setProgress(progress);
        }
    }

    private void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }

    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}