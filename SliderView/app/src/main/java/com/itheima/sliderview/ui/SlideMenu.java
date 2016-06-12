package com.itheima.sliderview.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by yong on 2016/6/11.
 */
public class SlideMenu extends ViewGroup {

    private Scroller scroller;
    private View slideMenu;
    private View slideMain;
    private float downX;
    private float downY;
    private float moveX;
    private static final int STATE_MENU = 1;
    private static final int STATE_MAIN = 0;
    private int currentState = STATE_MAIN;

    public SlideMenu(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        scroller = new Scroller(context);
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        slideMenu = getChildAt(0);
        slideMenu.measure(slideMenu.getLayoutParams().width,heightMeasureSpec);

        slideMain = getChildAt(1);
        slideMain.measure(widthMeasureSpec,heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        slideMenu.layout(-slideMenu.getMeasuredWidth(),0,0,b);
        slideMain.layout(l,t,r,b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                // will offset
                float scrollX = downX - moveX;
                // offseted + offset
                float newPosition = getScrollX() + scrollX;
                //estimate
                if(newPosition<-slideMenu.getMeasuredWidth()){
                    scrollTo(-slideMenu.getMeasuredWidth(),0);
                }else if(newPosition>0){
                    scrollTo(0,0);
                }else{
                    scrollBy((int) scrollX,0);
                }
                downX = moveX;
                break;
            case MotionEvent.ACTION_UP:
                int menuCenterX = -slideMenu.getMeasuredWidth()/2;
                if(getScrollX()<menuCenterX){
                    currentState = STATE_MENU;
                    autoAdjust();
                }else{
                    currentState = STATE_MAIN;
                    autoAdjust();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void autoAdjust() {
        int startX = getScrollX();
        int dx = 0;
        if(currentState==STATE_MAIN){
            dx = 0-startX;
        }else{
            dx = -slideMenu.getMeasuredWidth()-startX;
        }
        //scroller.startScroll
        scroller.startScroll(startX,0,dx,0,Math.abs(dx*2));
        //invalidate()
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),0);
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float offsetX = Math.abs(ev.getX() - downX);
                float offsetY = Math.abs(ev.getY()-downY);
                if(offsetX>offsetY && offsetX>5){
                    return  true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
    public void switchState(){
        if(currentState == STATE_MAIN){
            drawOpen();
        }else{
            drawClose();
        }
    }

    private void drawClose() {
        currentState = STATE_MAIN;
        autoAdjust();
    }

    private void drawOpen() {
        currentState = STATE_MENU;
        autoAdjust();
    }
}
