package com.itheima.customview;

import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.customview.adapter.MyPagerAdapter;
import com.itheima.customview.adapter.MyPopupAdapter;
import com.itheima.customview.utils.AnimationUtils;
import com.itheima.customview.view.ToggleView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, AdapterView.OnItemClickListener {
    //优酷
    private RelativeLayout rl3;
    private RelativeLayout rl2;
    private RelativeLayout rl1;
    private boolean is1Display = true;
    private boolean is2Display = true;
    private boolean is3Display = true;
    //轮播图
    private ArrayList<ImageView> imageList;
    private int[] imageArray;
    private String[] descArray;
    private ViewPager viewPager;
    private LinearLayout llPointContainer;
    private TextView tvDesc;
    private int lastPosition=0;
    private boolean isRunning = false;

    //下拉选择框
    private EditText etInput;
    private ImageButton ibDropdown;
    private ListView listView;
    private PopupWindow popupWindow;
    private ArrayList<String> datas;
    //自定义按钮
    private ToggleView toggleView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initAdapter();
        new Thread(new Runnable() {
            @Override
            public void run() {
                isRunning = true;
                while(isRunning){
                    SystemClock.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        }
                    });
                }

            }
        }).start();
    }

    private void initData() {
        imageArray = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};
        descArray = new String[]{
                "巩俐不低俗，我就不能低俗",
                "扑树又回来啦！再唱经典老歌引万人大合唱",
                "揭秘北京电影如何升级",
                "乐视网TV版大派送",
                "热血屌丝的反杀"
        };
        imageList = new ArrayList<ImageView>();
        ImageView imageView;
        View pointView;
        LinearLayout.LayoutParams layoutParams;
        for(int i = 0; i<imageArray.length;i++){
            //添加图片
            imageView = new ImageView(this);
            imageView.setBackgroundResource(imageArray[i]);
            imageList.add(imageView);
            //添加白点
            pointView = new View(this);
            pointView.setBackgroundResource(R.drawable.point_bg);
            pointView.setEnabled(false);
            layoutParams = new LinearLayout.LayoutParams(5,5);
            layoutParams.leftMargin=10;
            llPointContainer.addView(pointView,layoutParams);

        }
    }

    private void initAdapter() {
        viewPager.setAdapter(new MyPagerAdapter(imageList));
        viewPager.setCurrentItem(5000000);
    }

    private void initView() {
        //优酷
        findViewById(R.id.ib_home).setOnClickListener(this);
        findViewById(R.id.ib_menu).setOnClickListener(this);
        rl1 = (RelativeLayout) findViewById(R.id.rl_level1);
        rl2 = (RelativeLayout) findViewById(R.id.rl_level2);
        rl3 = (RelativeLayout) findViewById(R.id.rl_level3);
        //轮播图
        llPointContainer = (LinearLayout) findViewById(R.id.ll_point_container);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOnPageChangeListener(this);
        //下拉选择
        etInput = (EditText) findViewById(R.id.et_input);
        ibDropdown = (ImageButton) findViewById(R.id.ib_dropdown);
        ibDropdown.setOnClickListener(this);
        //自定义按钮
        toggleView = (ToggleView) findViewById(R.id.toggleView);
        toggleView.setOnSwitchStateChangedListener(new ToggleView.OnSwitchStateChangedListener() {
            @Override
            public void onSwitchStateChanged(boolean state) {
                Toast.makeText(getApplicationContext(),"当前状态开么："+state,1).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //优酷
            case R.id.ib_home:
                if (AnimationUtils.runningAnimationCount > 0) {
                    return;
                }
                if (is3Display) {
                    AnimationUtils.rotateOut(rl3, 0);
                    is3Display = !is3Display;
                    AnimationUtils.rotateOut(rl2, 200);
                } else if (is2Display) {
                    AnimationUtils.rotateOut(rl2, 0);
                } else {
                    AnimationUtils.rotateIn(rl2, 0);
                }
                is2Display = !is2Display;
                break;
            case R.id.ib_menu:
                if (AnimationUtils.runningAnimationCount > 0) {
                    return;
                }
                if (is3Display) {
                    AnimationUtils.rotateOut(rl3, 0);
                } else {
                    AnimationUtils.rotateIn(rl3, 0);
                }
                is3Display = !is3Display;
                break;
            //下拉选择框
            case R.id.ib_dropdown:
                showPopupWindow();
                break;
        }
    }

    private void showPopupWindow() {
        //init listView
        initListView();
        //show popupWindow
        popupWindow = new PopupWindow(listView,etInput.getWidth(),300);
        //setOnClick
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        //show as dropdown
        popupWindow.showAsDropDown(etInput,0,0);
    }

    private void initListView() {
        listView = new ListView(this);
        listView.setDividerHeight(0);
        listView.setBackgroundResource(R.drawable.listview_background);
        datas = new ArrayList<String>();
        for(int i = 0; i<30; i++){
            datas.add(10000+i+"");
        }
        MyPopupAdapter adapter = new MyPopupAdapter(datas);
        adapter.setCallback(new MyPopupAdapter.Callback() {
            @Override
            public void doDismiss() {
                if(datas.size()==0&&null!=popupWindow){
                    popupWindow.dismiss();
                }
            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (is3Display) {
                AnimationUtils.rotateOut(rl3, 0);
                is3Display = !is3Display;
                AnimationUtils.rotateOut(rl2, 200);
                is2Display = !is2Display;
                AnimationUtils.rotateOut(rl1, 400);
            } else if (is2Display) {
                AnimationUtils.rotateOut(rl2, 0);
                is2Display = !is2Display;
                AnimationUtils.rotateOut(rl1, 200);
            } else if (is1Display) {
                AnimationUtils.rotateOut(rl1, 0);
            } else {
                AnimationUtils.rotateIn(rl1, 0);
                AnimationUtils.rotateIn(rl2, 200);
                is2Display = !is2Display;
                AnimationUtils.rotateIn(rl3, 400);
                is3Display = !is3Display;
            }
            is1Display = !is1Display;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tvDesc.setText(descArray[position % descArray.length]);
        int newPosition = position % imageList.size();
        llPointContainer.getChildAt(lastPosition).setEnabled(false);
        llPointContainer.getChildAt(newPosition).setEnabled(true);
        lastPosition = newPosition;

    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        etInput.setText(datas.get(position));
        popupWindow.dismiss();
    }
}
