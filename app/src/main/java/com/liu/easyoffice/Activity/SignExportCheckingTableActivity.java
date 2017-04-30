package com.liu.easyoffice.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.easyoffice.R;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.Calendar;

public class SignExportCheckingTableActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private Button mWholeEmployeeButton;
    private Button mSingleEmployeeButton;
    private RelativeLayout mComployeeRelativelayout;
    private TextView mBeginTimeTextview;
    private TextView mEndTimeTextview;
    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_checking_table);
        initToolbar();
        initView();
        initData();
        initEvent();



    }


    private void initToolbar() {
        mToolbar = ((Toolbar) findViewById(R.id.toolbar));
        mToolbar.setTitle("导出考勤表");  //一定要放在下边语句之前
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        mWholeEmployeeButton = ((Button) findViewById(R.id.wholeButton));
        mSingleEmployeeButton = ((Button) findViewById(R.id.singlButton));
        mComployeeRelativelayout = ((RelativeLayout) findViewById(R.id.comployeeSelectRelativelayout));
        mBeginTimeTextview = ((TextView) findViewById(R.id.beginDateSelectTextview));
        mEndTimeTextview = ((TextView) findViewById(R.id.endDateSelectTextview));

        mComployeeRelativelayout.setVisibility(View.GONE);

    }

    private void initEvent() {
        mWholeEmployeeButton.setOnClickListener(this);
        mSingleEmployeeButton.setOnClickListener(this);
        mComployeeRelativelayout.setOnClickListener(this);
        mBeginTimeTextview.setOnClickListener(this);
        mEndTimeTextview.setOnClickListener(this);
        mWholeEmployeeButton.setSelected(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        mBeginTimeTextview.setText(acquireCurrentTime());
        mEndTimeTextview.setText(acquireCurrentTime());


    }

    private String acquireCurrentTime() {
        Calendar mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        String dateString = null;
        if (mMonth < 10 && mDay < 10) {
            dateString = mYear + "-0" + mMonth + "-0" + mDay;
        } else if (mMonth < 10 && mDay >= 10) {
            dateString = mYear + "-0" + mMonth + "-" + mDay;
        } else if (mMonth >= 10 && mDay < 10) {
            dateString = mYear + "-" + mMonth + "-0" + mDay;
        } else {
            dateString = mYear + "-" + mMonth + "-" + mDay;
        }
        return dateString;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wholeButton:
                mWholeEmployeeButton.setSelected(true);
                mSingleEmployeeButton.setSelected(false);
                mComployeeRelativelayout.setVisibility(View.GONE);
                break;
            case R.id.singlButton:
                mSingleEmployeeButton.setSelected(true);
                mWholeEmployeeButton.setSelected(false);
                mComployeeRelativelayout.setVisibility(View.VISIBLE);
                break;
            case R.id.comployeeSelectRelativelayout:
                Toast.makeText(getApplicationContext(), "展示员工列表", Toast.LENGTH_SHORT).show();
                break;
            case R.id.beginDateSelectTextview:
                String[] currentDates1 = acquireCurrentTime().split("-");
                new TimeSelector(this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        mBeginTimeTextview.setText(time);
                    }
                }, "1970-01-01", "2100-12-31").show_me(Integer.valueOf(currentDates1[0]), Integer.valueOf(currentDates1[1]), Integer.valueOf(currentDates1[2]));
                break;
            case R.id.endDateSelectTextview:
                String[] currentDates2 = acquireCurrentTime().split("-");
                new TimeSelector(this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        mEndTimeTextview.setText(time);
                    }
                }, "1970-01-01", "2100-12-31").show_me(Integer.valueOf(currentDates2[0]), Integer.valueOf(currentDates2[1]), Integer.valueOf(currentDates2[2]));
                break;
        }

    }
}
