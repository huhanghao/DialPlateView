package com.example.huhanghao.dialpplanttest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by huhanghao on 2017/6/6.
 */

public class DialplateView extends View {

    int MIN_VALUE = 0;
    int MAX_VALUE = 100;

    private int mViewWidth;
    private int mViewHeight;
    private int mArcRadius;                 // 圆弧的半径
    private int mDialRadius;                // 刻度的半径
    private int mPointRadius;               // 指针的半径
    private int mArcColor = Color.RED;
    private int mDialLength;                // 刻度的长度
    private int mPointDegree = 0;
    private int currentDegree = 0;

    public DialplateView(Context context) {
        this(context, null);
    }

    public DialplateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialplateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DialplateView);
        mArcColor = typedArray.getColor(R.styleable.DialplateView_arc_color, Color.BLUE);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        mViewWidth = getWidth() - paddingLeft - paddingRight;
        mViewHeight = getHeight() - paddingBottom - paddingTop;

        mArcRadius = mViewHeight;
        mDialRadius = mViewHeight / 8 * 5;
        mPointRadius = mViewHeight / 7;
        mDialLength = mViewHeight / 10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        init(canvas);

        // 绘制圆弧
        drawArc(canvas);

        // 绘制刻度
        drawDial(canvas);

        // 绘制指针
        drawPoint(canvas);

    }

    /**
     * 绘制指针.
     *
     * @param mCanvas
     */
    private void drawPoint(Canvas mCanvas) {
        mCanvas.save();
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.addCircle(0, 0, mPointRadius, Path.Direction.CW);

        path.moveTo(0, -mPointRadius);
        path.lineTo(-mArcRadius, 0);
        path.lineTo(0, mPointRadius);
        path.close();
        if(currentDegree < 0)
        {
            currentDegree = 0;
        }else if(currentDegree > 180){
            currentDegree = 180;
        }
        mCanvas.rotate(currentDegree, 0, 0);
        mCanvas.drawPath(path, mPaint);

        mCanvas.restore();
    }

    /**
     * 画刻度.
     *
     * @param mCanvas
     */
    private void drawDial(Canvas mCanvas) {

        mCanvas.save();
        Paint paintDegree = new Paint();
        paintDegree.setAntiAlias(true);
        paintDegree.setColor(Color.GRAY);
        for (int i = 0; i < 7; i++) {
            paintDegree.setStrokeWidth(3);
            mCanvas.drawLine(mDialRadius, 0, mDialRadius - mDialLength, 0, paintDegree);
            mCanvas.rotate(-180 / 6, 0, 0);
        }

        mCanvas.restore();
    }

    /**
     * 画圆弧.
     *
     * @param mCanvas
     */
    private void drawArc(Canvas mCanvas) {
        RectF bigOval = new RectF(-mArcRadius, -mArcRadius, mArcRadius, mArcRadius);
        RectF smallOval = new RectF(-mArcRadius / 3 * 2, -mArcRadius / 3 * 2, mArcRadius / 3 * 2, mArcRadius / 3 * 2);

        Paint mPathpPaint = new Paint();
        mPathpPaint.setAntiAlias(true);
        mPathpPaint.setColor(mArcColor);
        mPathpPaint.setStyle(Paint.Style.FILL);
        Path mPath = new Path();
        mPath.moveTo(-mArcRadius * 2 / 3, 0);
        mPath.lineTo(-mArcRadius, 0);
        mPath.addArc(bigOval, -180, 180);
        mPath.lineTo(mArcRadius * 2 / 3, 0);
        mPath.addArc(smallOval, 0, -180);
        mPath.setFillType(Path.FillType.WINDING);
        mCanvas.drawPath(mPath, mPathpPaint);
    }

    /**
     * 初始化画布
     */
    private void init(Canvas mCanvas) {
        mCanvas.translate(getWidth() / 2, getHeight() - mPointRadius - 10);
    }

    private void initAnimator() {
        ValueAnimator anim = ValueAnimator.ofInt(0, mPointDegree);
        anim.setDuration(10000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentDegree = (int) valueAnimator.getAnimatedValue();
                Log.d("hhh",""+currentDegree);
                invalidate();
            }
        });
        anim.start();
    }

    public void setmArcColor(int mColor){
        mArcColor = mColor;
        invalidate();
    }

    public void setPointDegree(int mDegree){
        mPointDegree = mDegree;
        initAnimator();
    }
}
