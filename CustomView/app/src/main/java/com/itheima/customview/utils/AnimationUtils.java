package com.itheima.customview.utils;

import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Created by yong on 2016/6/8.
 */
public class AnimationUtils {
    public static int runningAnimationCount = 0;

    public static void rotateOut(ViewGroup viewGroup, long delay) {
        for(int i =0; i<viewGroup.getChildCount();i++){
            viewGroup.getChildAt(i).setEnabled(false);
        }
        RotateAnimation ra = new RotateAnimation(0f, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
        ra.setDuration(500);
        ra.setFillAfter(true);
        ra.setStartOffset(delay);
        ra.setAnimationListener(new MyAnimationListener());
        viewGroup.startAnimation(ra);
    }

    public static void rotateIn(ViewGroup viewGroup, long delay){
        RotateAnimation ra = new RotateAnimation(-180f,0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,1.0f);
        ra.setDuration(500);
        ra.setFillAfter(true);
        ra.setStartOffset(delay);
        ra.setAnimationListener(new MyAnimationListener());
        viewGroup.startAnimation(ra);
        for(int i =0; i<viewGroup.getChildCount();i++){
            viewGroup.getChildAt(i).setEnabled(true);
        }
    }

    static class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            runningAnimationCount++;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            runningAnimationCount--;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
