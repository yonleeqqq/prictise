package com.itheima.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yong on 2016/6/9.
 */
public class ToggleView extends View {
    private Paint paint;
    private Bitmap background;
    private Bitmap button;
    private float currentX;
    private boolean mSwitchState = false;
    private boolean isTouchMode = false;
    public ToggleView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
    }

    public ToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        //read attrs
        String xmlns = "http://schemas.android.com/apk/res-auto";
        int backgroidRes = attrs.getAttributeResourceValue(xmlns,"switch_background",-1);
        int buttonRes = attrs.getAttributeResourceValue(xmlns,"switch_button",-1);
        boolean state = attrs.getAttributeBooleanValue(xmlns,"switch_state",false);

        setSwitchBackgroundResource(backgroidRes);
        setSwitchButtonResource(buttonRes);
        setSwitchState(state);
    }

    public ToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(background.getWidth(),background.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw background
        canvas.drawBitmap(background,0,0,paint);
        // draw button
        if(isTouchMode){
            float left = currentX - button.getWidth()/2.0f;
            int maxLeft = background.getWidth()- button.getWidth();
            if(left<0){
                left = 0;
            }else if(left >maxLeft){
                left = maxLeft;
            }
            canvas.drawBitmap(button,left,0,paint);
        }else{
            if(mSwitchState){
                canvas.drawBitmap(button,background.getWidth()-button.getWidth(),0,paint);
            }else{
                canvas.drawBitmap(button,0,0,paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //touchEvent
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isTouchMode = true;
                currentX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                isTouchMode = false;
                boolean state = event.getX()>background.getWidth()/2.0f;
                if(state!=mSwitchState&&onSwitchStateChangedListener!=null){
                    onSwitchStateChangedListener.onSwitchStateChanged(state);
                }
                mSwitchState = state;
                break;
            default:
                break;
        }

        //reDraw;
        invalidate();
        //consume touchevent
        return true;
    }

    public void setSwitchBackgroundResource(int res){
        background = BitmapFactory.decodeResource(getResources(),res);
    }
    public void setSwitchButtonResource(int res){
        button = BitmapFactory.decodeResource(getResources(),res);
    }
    public void setSwitchState(boolean switchState){
        this.mSwitchState = switchState;
    }

    public interface OnSwitchStateChangedListener{
        void onSwitchStateChanged(boolean state);
    }
    private OnSwitchStateChangedListener onSwitchStateChangedListener;

    public void setOnSwitchStateChangedListener(OnSwitchStateChangedListener onSwitchStateChangedListener) {
        this.onSwitchStateChangedListener = onSwitchStateChangedListener;
    }
}
