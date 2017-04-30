package com.liu.easyoffice.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liu.easyoffice.Fragment.SignMainFragment;
import com.liu.easyoffice.Fragment.SignSettingFragment;
import com.liu.easyoffice.Fragment.SignStatisticsFragment;
import com.liu.easyoffice.R;

public class SignMainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainpageFragment";
    Fragment[] fragments;
    private Toolbar mToolbar;
    SignMainFragment mSignMainFragment;//主页
    SignStatisticsFragment mSignStatisticsFragment;//统计
    SignSettingFragment mSignSettingFragment;//设置
    //按钮的数组，一开始第一个按钮被选中
    Button[] tabs;
    int oldIndex;//用户看到的item
    int newIndex;//用户即将看到的item
    private TextView mExportDataTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_main);
        //初始化Toolbar的一些相关数据
        initToolbarData();
        //对Toolbar的相关控件进行监听
        initToolbarEvent();
        //初始化控件并进行相关监听
        initDataView();
        //if的作用是保存状态,防止横屏时,重新加载
        if (savedInstanceState == null) {
            //界面初始显示第一个fragment;添加第一个fragment,并且缓存第二个Fragment界面
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragments[0]).commit();
            //初始时按钮一被选中
            tabs[0].setSelected(true);
        }
    }

    //初始化Fragment，和相应的Button，并且进行一些监听
    private void initDataView() {
        //初始化Fragment
        mSignMainFragment = new SignMainFragment();
        mSignStatisticsFragment = new SignStatisticsFragment();
        mSignSettingFragment = new SignSettingFragment();

        //所有Fragment数组
        fragments = new Fragment[]{mSignMainFragment, mSignStatisticsFragment, mSignSettingFragment};

        //设置按钮的数组
        tabs = new Button[3];
        tabs[0] = (Button) findViewById(R.id.button1);//签到主页的button
        tabs[1] = (Button) findViewById(R.id.button2);//签到统计button
        tabs[2] = (Button) findViewById(R.id.button3);//签到设置button
        tabs[0].setOnClickListener(this);
        tabs[1].setOnClickListener(this);
        tabs[2].setOnClickListener(this);
    }

    //对Toolbar控件初始化和设置一些监听
    private void initToolbarData() {
        mToolbar = ((Toolbar) findViewById(R.id.toolbar));
        mToolbar.setTitle("签到");  //一定要放在下边语句之前
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //mToolbar.setNavigationIcon(R.mipmap.back);
        mExportDataTextview = ((TextView) mToolbar.findViewById(R.id.exportData));
        mExportDataTextview.setVisibility(View.GONE);

    }

    //对Toolbar控件进行监听
    private void initToolbarEvent() {
        //对Toolbar中的返回按钮添加监听事件
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        //导出考勤报表
        mExportDataTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exportCheckingTableIntent = new Intent(getApplicationContext(),SignExportCheckingTableActivity.class);
                startActivity(exportCheckingTableIntent);
            }
        });

    }

    //程序异常退出时,调用
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //程序恢复时,调用
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    //对三个按钮进行控制
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                initToolbarSignIn();
                newIndex = 0;  //选中第一项
                break;
            case R.id.button2:   //选中第二项
                initToolbarSignStatistics();
                newIndex = 1;
                break;
            case R.id.button3:    //选中第三项
                initToolbarSignSetting();
                newIndex = 2;
                break;
        }
        switchFragment();
    }

    //在签到界面的Fragment中对Toolbar进行定制
    private void initToolbarSignIn() {
        mToolbar.setTitle("签到");
        mExportDataTextview.setVisibility(View.GONE);
    }
    //在统计界面的Fragment中对Toolbar进行定制
    private void initToolbarSignStatistics() {
        mToolbar.setTitle("统计");
        mExportDataTextview = ((TextView) mToolbar.findViewById(R.id.exportData));
        mExportDataTextview.setVisibility(View.VISIBLE);
        mExportDataTextview.setText("导出考勤报表");
    }

    //在设置界面的Fragment中对Toolbar进行定制
    private void initToolbarSignSetting() {
        mExportDataTextview.setVisibility(View.GONE);
        mToolbar.setTitle("设置");
    }

    //抽出切换Fragment的方法
    private void switchFragment() {
        FragmentTransaction mFragmentTransaction;
        //如果选择的项不是当前选中项，则替换；否则，不做操作
        if (newIndex != oldIndex) {
            mFragmentTransaction = getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.hide(fragments[oldIndex]);    //隐藏当前fragment

            //如何选中项没有添加过,则添加
            if (!fragments[newIndex].isAdded()) {
                //添加fragment
                mFragmentTransaction.add(R.id.fragment_container, fragments[newIndex]);
            }
            //显示当前项
            mFragmentTransaction.show(fragments[newIndex]).commit();
        }
        //之前选中的项被取消
        tabs[oldIndex].setSelected(false);

        //当前的选项被选中
        tabs[newIndex].setSelected(true);

        //当前选项变为选中项
        oldIndex = newIndex;
    }
}
