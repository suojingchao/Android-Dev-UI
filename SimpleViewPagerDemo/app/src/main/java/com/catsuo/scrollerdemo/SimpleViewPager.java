package com.catsuo.scrollerdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by cat on 2018/5/10.
 */

public class SimpleViewPager extends ViewGroup {

    private String TAG = SimpleViewPager.class.getSimpleName();

    private Scroller mScroller = null;
    private int mTouchSlop = 0;

    private float mLastDownX = 0;
    private float mNowDownX = 0;
    private int mLeftProtector = 0;
    private int mRightProtector = 0;

    public SimpleViewPager(Context context) {
        super(context);
        init();
    }

    public SimpleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SimpleViewPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledPagingTouchSlop();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN :{
                mNowDownX = ev.getRawX();
                break;
            }
            case MotionEvent.ACTION_MOVE :{
                mLastDownX = mNowDownX;
                mNowDownX = ev.getRawX();
                if (Math.abs(mNowDownX - mLastDownX) > mTouchSlop) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                mLastDownX = mNowDownX;
                mNowDownX = event.getRawX();
                float xOffset = mLastDownX - mNowDownX;
                Log.i(TAG, "xOffset = " + xOffset);
                if (getScrollX() + xOffset <= mLeftProtector) {
                    scrollTo(mLeftProtector, 0);
                } else if (getScrollX() + xOffset + getWidth() >= mRightProtector) {
                    scrollTo(mRightProtector - getWidth(), 0);
                } else {
                    scrollBy((int) xOffset, 0);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                int scrolledX = getScrollX();
                int targetIndex = (scrolledX + getWidth() / 2) / getWidth();
                int dx = targetIndex * getWidth() - getScrollX();
                mScroller.startScroll(scrolledX, 0, dx, 0);
                invalidate();
                Log.i(TAG, "scrolledX = " + scrolledX + " dx = " + dx);
                break;
            }

        }
        return true;

    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        View child = null;
        for (int i = 0 ; i < childCount ; i++) {
            child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            View child = null;
            for (int i = 0 ; i < childCount ; i++) {
                child = getChildAt(i);
                child.layout(i * child.getMeasuredWidth(), 0, (i + 1) * child.getMeasuredWidth(), child.getMeasuredHeight());
            }
            mLeftProtector = getChildAt(0).getLeft();
            mRightProtector = getChildAt(childCount - 1).getRight();
            Log.i(TAG, "leftProtector=" + mLeftProtector + "  rightProtector=" + mRightProtector);
        }
    }
}
