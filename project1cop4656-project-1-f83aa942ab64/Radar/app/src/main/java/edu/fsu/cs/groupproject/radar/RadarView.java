package edu.fsu.cs.groupproject.radar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;


@SuppressLint("DrawAllocation")
public class RadarView extends View {

    Context mContext;
    boolean isSearching = false;
    Paint mPaint;
    Bitmap mScanBmp;
    int mOffsetArgs = 0;
    Bitmap mDefaultPointBmp;
    int mWidth, mHeight;
    int n =100;
    public static int m = 100;
    int mOutWidth;
    int mCx, mCy;
    int mOutsideRadius, mInsideRadius;

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public RadarView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }


    private void init(Context context) {
        mPaint = new Paint();
        this.mContext = context;
        this.mDefaultPointBmp = Bitmap.createBitmap(BitmapFactory
                .decodeResource(mContext.getResources(),
                        R.drawable.dot));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        if (mWidth == 0 || mHeight == 0) {
            final int minimumWidth = getSuggestedMinimumWidth();
            final int minimumHeight = getSuggestedMinimumHeight();
            mWidth = resolveMeasured(widthMeasureSpec, minimumWidth);
            mHeight = resolveMeasured(heightMeasureSpec, minimumHeight);
            mScanBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    mContext.getResources(), R.drawable.radar), mWidth
                    - mOutWidth, mWidth - mOutWidth, false);

            mCx = mWidth / 2;
            mCy = mHeight / 2;


            mOutWidth = mWidth / 10;


            mOutsideRadius = mWidth / 2;
            mInsideRadius = (mWidth - mOutWidth) / 4 / 2;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(0xff259b24);

        canvas.drawCircle(mCx, mCy, mOutsideRadius, mPaint);


        mPaint.setColor(0xff5677fc);
        canvas.drawCircle(mCx, mCy, mInsideRadius * 4, mPaint);


        mPaint.setStyle(Style.STROKE);
        mPaint.setColor(0xffe7e9fd);
        canvas.drawCircle(mCx, mCy, mInsideRadius * 3, mPaint);


        canvas.drawCircle(mCx, mCy, mInsideRadius * 2, mPaint);


        canvas.drawCircle(mCx, mCy, mInsideRadius * 1, mPaint);


        canvas.drawLine(mOutWidth / 2, mCy, mWidth - mOutWidth / 2, mCy, mPaint);
        canvas.drawLine(mCx, mHeight - mOutWidth / 2, mCx, mOutWidth / 2,
                mPaint);


        int startX, startY, endX, endY;
        double radian;

        radian = Math.toRadians((double) 45);
        startX = (int) (mCx + mInsideRadius * 4 * Math.cos(radian));
        startY = (int) (mCy + mInsideRadius * 4 * Math.sin(radian));

        radian = Math.toRadians((double) 45 + 180);
        endX = (int) (mCx + mInsideRadius * 4 * Math.cos(radian));
        endY = (int) (mCy + mInsideRadius * 4 * Math.sin(radian));
        canvas.drawLine(startX, startY, endX, endY, mPaint);


        radian = Math.toRadians((double) 135);
        startX = (int) (mCx + mInsideRadius * 4 * Math.cos(radian));
        startY = (int) (mCy + mInsideRadius * 4 * Math.sin(radian));

        radian = Math.toRadians((double) 135 + 180);
        endX = (int) (mCx + mInsideRadius * 4 * Math.cos(radian));
        endY = (int) (mCy + mInsideRadius * 4 * Math.sin(radian));
        canvas.drawLine(startX, startY, endX, endY, mPaint);


        int mx = mCx;
        int my =  (mOutsideRadius - mInsideRadius * 4 + mInsideRadius * 4 *  (m-n)/ m);
        canvas.drawBitmap(mDefaultPointBmp,
                mx,
                my, null);;
        ;
    }



    public void OnlocationChange(int n){
        this.n = n;
    }
    public double getN() {
        return n;

    }
    private int resolveMeasured(int measureSpec, int desired) {
        int result = 0;
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                result = desired;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(specSize, desired);
                break;
            case MeasureSpec.EXACTLY:
            default:
                result = specSize;
        }
        return result;
    }
}