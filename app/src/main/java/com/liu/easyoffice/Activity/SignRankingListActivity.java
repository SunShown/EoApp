package com.liu.easyoffice.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liu.easyoffice.Fragment.CheckHardWorkingList;
import com.liu.easyoffice.Fragment.CheckLateList;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MySharePreference;

import java.util.Calendar;

public class SignRankingListActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignRankingListActivity";
    private Toolbar mToolbar;
    private Fragment mCheckHardWorkingListFragment;
    private Fragment mCheckLateListFragment;
    private Fragment[] checkRankingListFragments;
    private Button[] mRankingListSwitchButtons;
    private int oldIndex;
    private int newIndex;
    private int copyIndex;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mMonthAfter;
    private TextView mRankingListDateTextview;
    private TextView mCompanyNameTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_rankinglist);
        initToolbar();
        initData();
        initView();
    }

    private void initToolbar() {
        mToolbar = ((Toolbar) findViewById(R.id.toolbar));
        mToolbar.setTitle("考勤排行榜");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        mRankingListDateTextview = ((TextView) findViewById(R.id.rankingList_select_textview));
        mRankingListDateTextview.setOnClickListener(this);
        mCompanyNameTextview = ((TextView) findViewById(R.id.company_name_textview));
        String mainCurrentBusTextview = MySharePreference.getCurrentCompany(this).getTcName();
        mCompanyNameTextview.setText(mainCurrentBusTextview);
        if (mMonthAfter >= 10 && mDay >= 10) {
            mRankingListDateTextview.setText(mYear + "年" + mMonthAfter + "月" + mDay + "日");
        } else if (mMonthAfter < 10 && mDay >= 10) {
            mRankingListDateTextview.setText(mYear + "年0" + mMonthAfter + "月" + mDay + "日");
        } else if (mMonthAfter >= 10 && mDay < 10) {
            mRankingListDateTextview.setText(mYear + "年" + mMonthAfter + "月0" + mDay + "日");
        } else {
            mRankingListDateTextview.setText(mYear + "年0" + mMonthAfter + "月0" + mDay + "日");
        }
        getSupportFragmentManager().beginTransaction().add(R.id.inner_rankinglist_fragment_container, mCheckHardWorkingListFragment).commit();
        mRankingListSwitchButtons[0].setSelected(true);
    }

    private void initData() {
        Calendar mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mMonthAfter = mMonth + 1;
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mRankingListSwitchButtons = new Button[]{((Button) findViewById(R.id.button_hardworking_rankinglist)), ((Button) findViewById(R.id.button_late_rankinglist))};
        mRankingListSwitchButtons[0].setOnClickListener(this);
        mRankingListSwitchButtons[1].setOnClickListener(this);
        mCheckHardWorkingListFragment = new CheckHardWorkingList();
        mCheckLateListFragment = new CheckLateList();
        checkRankingListFragments = new Fragment[]{mCheckHardWorkingListFragment, mCheckLateListFragment};


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_hardworking_rankinglist:
                newIndex = 0;
                break;
            case R.id.button_late_rankinglist:
                newIndex = 1;
                break;
        }
        switchFragment();

    }

    private void switchFragment() {
        Log.i(TAG, "switchFragment()方法执行了！");
        FragmentTransaction mFragmentTransaction;
        //如果选择的项不是当前选中项，则替换；否则，不做操作
        if (oldIndex != newIndex) {
            mFragmentTransaction = getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.hide(checkRankingListFragments[oldIndex]);  //隐藏当前Fragment

            //如何选中项没有添加过，则添加
            if (!checkRankingListFragments[newIndex].isAdded()) {
                mFragmentTransaction.add(R.id.inner_rankinglist_fragment_container, checkRankingListFragments[newIndex]);
            }
            //显示当前项
            mFragmentTransaction.show(checkRankingListFragments[newIndex]).commit();
        }

        //之前选中的项取消
        mRankingListSwitchButtons[oldIndex].setSelected(false);

        //当前的选项被选中
        mRankingListSwitchButtons[newIndex].setSelected(true);

        //当前选项变为选中项
        oldIndex = newIndex;
    }
}
