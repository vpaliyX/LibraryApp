package com.vpaliy.mvp.view.view;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.lang.reflect.Field;

import android.annotation.SuppressLint;

public class LockableSlider extends ViewPager {

    private boolean lockSwiping=false;

    public LockableSlider(Context context) {
        this(context,null);
    }

    public LockableSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        postInitViewPager();
    }


    private ScrollController mScroller = null;

    private void postInitViewPager() {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new ScrollController(getContext(),
                    (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }

    public void unLockSwiping() {
        lockSwiping=false;
    }

    //supposed to be called every time when a change of the data has occurred
    public void setPosition(int index) {
        try {
            Field item = ViewPager.class.getDeclaredField("mCurItem");
            item.setAccessible(true);
            item.setInt(this,index);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev) && !lockSwiping;
        } catch (IllegalArgumentException e) {
            //uncomment if you really want to see these errors
            //e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event) && !lockSwiping;
    }


    private static  class ScrollController extends Scroller {

        private double mScrollFactor = 1;

        public ScrollController(Context context) {
            super(context);
        }

        ScrollController(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @SuppressLint("NewApi")
        public ScrollController(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }


        void setScrollDurationFactor(double scrollFactor) {
            mScrollFactor = scrollFactor;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, (int) (duration * mScrollFactor));
        }
    }

}