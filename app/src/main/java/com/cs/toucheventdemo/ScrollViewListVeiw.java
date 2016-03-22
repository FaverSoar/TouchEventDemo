package com.cs.toucheventdemo;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by charmingsoft on 2016/3/16.
 */
public class ScrollViewListVeiw extends ViewGroup {
    private Scroller mScroller;
    private int mTouchSlop;
    private float  mXDown,mXMove,mXLastMove;
    /**
     * 界面可滚动的左边界
     */
    private int leftBorder;

    /**
     * 界面可滚动的右边界
     */
    private int rightBorder;
    public ScrollViewListVeiw(Context context) {
        this(context,null);
    }

    public ScrollViewListVeiw(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollViewListVeiw(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context));
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childcount = getChildCount();
        for (int i = 0; i < childcount; i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childcount = getChildCount();
        for (int i = 0; i < childcount; i++) {
            View view = getChildAt(i);
            view.layout(i*view.getMeasuredWidth() ,0,(i+1)*view.getMeasuredWidth(),view.getMeasuredHeight());
        }
        // 初始化左右边界值
        leftBorder = getChildAt(0).getLeft();
        rightBorder = getChildAt(getChildCount() - 1).getRight();
    }

    public static final String TAG = "ScrollViewListVeiw";
    public static final String TAG1 = "XY";
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG,"onInterceptTouchEvent---onInterceptTouchEvent");
        boolean isIntercept = false;
        switch (ev.getAction()){

            case MotionEvent.ACTION_DOWN:
                isIntercept = true;
                mXDown = ev.getRawX();
                mXLastMove = mXDown;
                Log.e(TAG,"onInterceptTouchEvent----ACTION_DOWN__a");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG,"onInterceptTouchEvent----ACTION_MOVE__b");
                mXMove = ev.getRawX();
                //取绝对值
                float diff = Math.abs(mXMove - mXDown);
                mXLastMove = mXMove;
                // 当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                if (diff > mTouchSlop) {
                    int parentY = (int)ev.getRawY();
                    View view = getChildAt(0);
                    Log.e(TAG1,"parentY:"+parentY+"||bottom:"+view.getBottom()+"||height:"+view.getMeasuredHeight());
                    if(parentY > view.getBottom()){
                        isIntercept = false;
                    }else{
                        isIntercept =  true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG,"onInterceptTouchEvent----ACTION_UP__c");
                break;
        }
        return isIntercept;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG,"onTouchEvent---onTouchEvent");
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    Log.e(TAG,"onTouchEvent----ACTION_DOWN__a");
                    View view = getChildAt(0);
                    if(event.getRawY() > view.getY()+view.getHeight()){
                        return false;
                    }else{
                        return true;
                    }

                case MotionEvent.ACTION_MOVE:
                    mXMove = event.getRawX();
                    int scrolledX = (int)(mXLastMove - mXMove);
                    if (getScrollX() + scrolledX < leftBorder) {
//                        scrollTo(leftBorder, 0);
                        return true;
                    } else if (getScrollX() + getWidth() + scrolledX > rightBorder) {
//                        scrollTo(rightBorder - getWidth(), 0);
                        return true;
                    }
                    Log.e(TAG1,"getScrollX()"+getScrollX());
                    scrollBy(scrolledX, 0);
                    mXLastMove = mXMove;
                    break;
                case MotionEvent.ACTION_UP:
//                    Log.e(TAG1,"getWidth()"+getWidth());
                    // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                    int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                    int dx = targetIndex * getWidth() - getScrollX();
                    // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
                    mScroller.startScroll(getScrollX(), 0, dx, 0);
                    invalidate();
                    break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
