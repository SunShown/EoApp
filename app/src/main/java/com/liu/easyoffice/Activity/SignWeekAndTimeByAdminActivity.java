package com.liu.easyoffice.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liu.easyoffice.Fragment.SignSettingFragment;
import com.liu.easyoffice.R;

import java.util.ArrayList;
import java.util.List;


public class SignWeekAndTimeByAdminActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "WeekAndTime";
    private static final int WEEKANDTIMERESULTCODE = 4;
    private Toolbar mToolbar;
    private RelativeLayout mRelativelayout1;
    private RelativeLayout mRelativelayout2;
    private RelativeLayout mRelativelayout3;
    private RelativeLayout mRelativelayout4;
    private RelativeLayout mRelativelayout5;
    private RelativeLayout mRelativelayout6;
    private RelativeLayout mRelativelayout7;
    private boolean flag1;
    private boolean flag2;
    private boolean flag3;
    private boolean flag4;
    private boolean flag5;
    private boolean flag6;
    private boolean flag7;
    private boolean lawHolidayBoolean;
    private boolean[] flags;
    private boolean autoToggleFlag;
    private ImageView[] mSelectImages;
    private ImageView mSelectImage1;
    private ImageView mSelectImage2;
    private ImageView mSelectImage3;
    private ImageView mSelectImage4;
    private ImageView mSelectImage5;
    private ImageView mSelectImage6;
    private ImageView mSelectImage7;
    private TextView mOnTimeTextview1;
    private TextView mOnTimeTextview2;
    private TextView mOnTimeTextview3;
    private TextView mOnTimeTextview4;
    private TextView mOnTimeTextview5;
    private TextView mOnTimeTextview6;
    private TextView mOnTimeTextview7;
    private TextView mOffTimeTextview1;
    private TextView mOffTimeTextview2;
    private TextView mOffTimeTextview3;
    private TextView mOffTimeTextview4;
    private TextView mOffTimeTextview5;
    private TextView mOffTimeTextview6;
    private TextView mOffTimeTextview7;
    private String[] onTimes;
    private String[] offTimes;
    private TextView[] mRestTextviews;
    private TextView mRestTextview1;
    private TextView mRestTextview2;
    private TextView mRestTextview3;
    private TextView mRestTextview4;
    private TextView mRestTextview5;
    private TextView mRestTextview6;
    private TextView mRestTextview7;
    private ImageView mImageAutoToggle;
    private RelativeLayout mAutoSelectRelativelayout;
    private String onTimeSetting = "00:00";
    private String offTimeSetting = "00:00";
    private TextView[] onTimeTextviews;
    private TextView[] offTimeTextviews;
    private Button mSaveButton;
    private String[] weekStrings;
    private String onWeekString = " ";
    private String offWeekString = " ";
    private TextView[] locationFlagTextviews;
    private TextView mLocationFlagTextview1;
    private TextView mLocationFlagTextview2;
    private TextView mLocationFlagTextview3;
    private TextView mLocationFlagTextview4;
    private TextView mLocationFlagTextview5;
    private TextView mLocationFlagTextview6;
    private TextView mLocationFlagTextview7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_date_and_time_by_admin);
        initData();
        initToolbar();
        initView();
        initEvent();
    }

    private void initData() {
        onTimes = new String[]{"06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00"};
        offTimes = new String[]{"12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00"};
        flags = new boolean[]{flag1, flag2, flag3, flag4, flag5, flag6, flag7};
        weekStrings = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};


    }


    private void initToolbar() {
        mToolbar = ((Toolbar) findViewById(R.id.toolbar));
        mToolbar.setTitle("考勤时间");  //一定要放在下边语句之前
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {

        mRelativelayout1 = ((RelativeLayout) findViewById(R.id.weekRelativelayout1));
        mRelativelayout2 = ((RelativeLayout) findViewById(R.id.weekRelativelayout2));
        mRelativelayout3 = ((RelativeLayout) findViewById(R.id.weekRelativelayout3));
        mRelativelayout4 = ((RelativeLayout) findViewById(R.id.weekRelativelayout4));
        mRelativelayout5 = ((RelativeLayout) findViewById(R.id.weekRelativelayout5));
        mRelativelayout6 = ((RelativeLayout) findViewById(R.id.weekRelativelayout6));
        mRelativelayout7 = ((RelativeLayout) findViewById(R.id.weekRelativelayout7));
        mSelectImage1 = ((ImageView) findViewById(R.id.selectImage1));
        mSelectImage2 = ((ImageView) findViewById(R.id.selectImage2));
        mSelectImage3 = ((ImageView) findViewById(R.id.selectImage3));
        mSelectImage4 = ((ImageView) findViewById(R.id.selectImage4));
        mSelectImage5 = ((ImageView) findViewById(R.id.selectImage5));
        mSelectImage6 = ((ImageView) findViewById(R.id.selectImage6));
        mSelectImage7 = ((ImageView) findViewById(R.id.selectImage7));
        mOnTimeTextview1 = ((TextView) findViewById(R.id.onTime1));
        mOnTimeTextview2 = ((TextView) findViewById(R.id.onTime2));
        mOnTimeTextview3 = ((TextView) findViewById(R.id.onTime3));
        mOnTimeTextview4 = ((TextView) findViewById(R.id.onTime4));
        mOnTimeTextview5 = ((TextView) findViewById(R.id.onTime5));
        mOnTimeTextview6 = ((TextView) findViewById(R.id.onTime6));
        mOnTimeTextview7 = ((TextView) findViewById(R.id.onTime7));
        mOffTimeTextview1 = ((TextView) findViewById(R.id.offTime1));
        mOffTimeTextview2 = ((TextView) findViewById(R.id.offTime2));
        mOffTimeTextview3 = ((TextView) findViewById(R.id.offTime3));
        mOffTimeTextview4 = ((TextView) findViewById(R.id.offTime4));
        mOffTimeTextview5 = ((TextView) findViewById(R.id.offTime5));
        mOffTimeTextview6 = ((TextView) findViewById(R.id.offTime6));
        mOffTimeTextview7 = ((TextView) findViewById(R.id.offTime7));
        mRestTextview1 = ((TextView) findViewById(R.id.rest1));
        mRestTextview2 = ((TextView) findViewById(R.id.rest2));
        mRestTextview3 = ((TextView) findViewById(R.id.rest3));
        mRestTextview4 = ((TextView) findViewById(R.id.rest4));
        mRestTextview5 = ((TextView) findViewById(R.id.rest5));
        mRestTextview6 = ((TextView) findViewById(R.id.rest6));
        mRestTextview7 = ((TextView) findViewById(R.id.rest7));
        mLocationFlagTextview1 = ((TextView) findViewById(R.id.location_flag1));
        mLocationFlagTextview2 = ((TextView) findViewById(R.id.location_flag2));
        mLocationFlagTextview3 = ((TextView) findViewById(R.id.location_flag3));
        mLocationFlagTextview4 = ((TextView) findViewById(R.id.location_flag4));
        mLocationFlagTextview5 = ((TextView) findViewById(R.id.location_flag5));
        mLocationFlagTextview6 = ((TextView) findViewById(R.id.location_flag6));
        mLocationFlagTextview7 = ((TextView) findViewById(R.id.location_flag7));

        mImageAutoToggle = ((ImageView) findViewById(R.id.autoToggle));
        mAutoSelectRelativelayout = ((RelativeLayout) findViewById(R.id.autoSelectRelativelayout));
        mSaveButton = ((Button) findViewById(R.id.DateAndTimeCommitButton));
        mRestTextviews = new TextView[]{mRestTextview1, mRestTextview2, mRestTextview3, mRestTextview4, mRestTextview5, mRestTextview6, mRestTextview7};
        mSelectImages = new ImageView[]{mSelectImage1, mSelectImage2, mSelectImage3, mSelectImage4, mSelectImage5, mSelectImage6, mSelectImage7};
        onTimeTextviews = new TextView[]{mOnTimeTextview1, mOnTimeTextview2, mOnTimeTextview3, mOnTimeTextview4, mOnTimeTextview5, mOnTimeTextview6, mOnTimeTextview7};
        offTimeTextviews = new TextView[]{mOffTimeTextview1, mOffTimeTextview2, mOffTimeTextview3, mOffTimeTextview4, mOffTimeTextview5, mOffTimeTextview6, mOffTimeTextview7};
        locationFlagTextviews = new TextView[]{mLocationFlagTextview1, mLocationFlagTextview2, mLocationFlagTextview3, mLocationFlagTextview4, mLocationFlagTextview5, mLocationFlagTextview6, mLocationFlagTextview7};

        mRelativelayout1.setOnClickListener(this);
        mRelativelayout2.setOnClickListener(this);
        mRelativelayout3.setOnClickListener(this);
        mRelativelayout4.setOnClickListener(this);
        mRelativelayout5.setOnClickListener(this);
        mRelativelayout6.setOnClickListener(this);
        mRelativelayout7.setOnClickListener(this);
        mOnTimeTextview1.setOnClickListener(this);
        mOnTimeTextview2.setOnClickListener(this);
        mOnTimeTextview3.setOnClickListener(this);
        mOnTimeTextview4.setOnClickListener(this);
        mOnTimeTextview5.setOnClickListener(this);
        mOnTimeTextview6.setOnClickListener(this);
        mOnTimeTextview7.setOnClickListener(this);
        mOffTimeTextview1.setOnClickListener(this);
        mOffTimeTextview2.setOnClickListener(this);
        mOffTimeTextview3.setOnClickListener(this);
        mOffTimeTextview4.setOnClickListener(this);
        mOffTimeTextview5.setOnClickListener(this);
        mOffTimeTextview6.setOnClickListener(this);
        mOffTimeTextview7.setOnClickListener(this);
        mAutoSelectRelativelayout.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        List<Integer> onWeeksLists = new ArrayList<>();
        Intent receiverIntent = getIntent();
        String onWeekInfoIntent = receiverIntent.getStringExtra("onWeekInfoIntent");
        String offWeekInfoIntent = receiverIntent.getStringExtra("offWeekInfoIntent");
        if (offWeekInfoIntent != null) {
            offWeekString = offWeekInfoIntent;
        } else {
            offWeekString = " ";
        }
        String onTimeInfoIntent = receiverIntent.getStringExtra("onTimeInfoIntent");
        if (onTimeInfoIntent != null) {
            onTimeSetting = onTimeInfoIntent;
        } else {
            onTimeSetting = "00:00";
        }
        String offTimeInfoIntent = receiverIntent.getStringExtra("offTimeInfoIntent");
        if (offTimeInfoIntent != null) {
            offTimeSetting = offTimeInfoIntent;
        } else {
            offTimeSetting = "00:00";
        }
        boolean isSelectLawHolidayInfoIntent = receiverIntent.getBooleanExtra("isAutoLawHolidayInfoIntent", false);
        autoToggleFlag = isSelectLawHolidayInfoIntent;


        if (onWeekInfoIntent != null) {
            String[] onWeekInfoIntentStrings = onWeekInfoIntent.split(" ");
            for (int i = 1; i < onWeekInfoIntentStrings.length; i++) {
                for (int j = 0; j < weekStrings.length; j++) {
                    if (onWeekInfoIntentStrings[i].equals(weekStrings[j])) {
                        onWeeksLists.add(j);
                    }
                }
            }

            for (int i = 0; i < onWeeksLists.size(); i++) {
                mSelectImages[onWeeksLists.get(i)].setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectedweek));
                flags[onWeeksLists.get(i)] = true;
                mRestTextviews[onWeeksLists.get(i)].setVisibility(View.GONE);
                onTimeTextviews[onWeeksLists.get(i)].setVisibility(View.VISIBLE);
                offTimeTextviews[onWeeksLists.get(i)].setVisibility(View.VISIBLE);
                locationFlagTextviews[onWeeksLists.get(i)].setVisibility(View.VISIBLE);
                onTimeTextviews[onWeeksLists.get(i)].setText(onTimeSetting);
                offTimeTextviews[onWeeksLists.get(i)].setText(offTimeSetting);
                if (autoToggleFlag == true) {
                    mImageAutoToggle.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectedweek));
                }

            }
        }
    }


    private void initEvent() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weekRelativelayout1:
                if (flags[0] == false) {
                    mSelectImage1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectedweek));
                    mRestTextviews[0].setVisibility(View.GONE);
                    onTimeTextviews[0].setVisibility(View.VISIBLE);
                    locationFlagTextviews[0].setVisibility(View.VISIBLE);
                    offTimeTextviews[0].setVisibility(View.VISIBLE);

                    onTimeTextviews[0].setText(onTimeSetting);
                    offTimeTextviews[0].setText(offTimeSetting);
                    flags[0] = true;
                } else {
                    onTimeTextviews[0].setText("00:00");
                    offTimeTextviews[0].setText("00:00");
                    mSelectImage1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectweek));
                    mRestTextviews[0].setVisibility(View.VISIBLE);
                    onTimeTextviews[0].setVisibility(View.GONE);
                    locationFlagTextviews[0].setVisibility(View.GONE);
                    offTimeTextviews[0].setVisibility(View.GONE);
                    flags[0] = false;
                }
                break;
            case R.id.weekRelativelayout2:
                if (flags[1] == false) {
                    mSelectImages[1].setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectedweek));
                    mRestTextviews[1].setVisibility(View.GONE);
                    onTimeTextviews[1].setVisibility(View.VISIBLE);
                    locationFlagTextviews[1].setVisibility(View.VISIBLE);
                    offTimeTextviews[1].setVisibility(View.VISIBLE);
                    onTimeTextviews[1].setText(onTimeSetting);
                    offTimeTextviews[1].setText(offTimeSetting);
                    flags[1] = true;
                } else {
                    onTimeTextviews[1].setText("00:00");
                    offTimeTextviews[1].setText("00:00");
                    mSelectImages[1].setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectweek));
                    mRestTextviews[1].setVisibility(View.VISIBLE);
                    onTimeTextviews[1].setVisibility(View.GONE);
                    locationFlagTextviews[1].setVisibility(View.GONE);
                    offTimeTextviews[1].setVisibility(View.GONE);
                    flags[1] = false;
                }
                break;
            case R.id.weekRelativelayout3:
                if (flags[2] == false) {
                    mSelectImage3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectedweek));
                    mRestTextview3.setVisibility(View.GONE);
                    mOnTimeTextview3.setVisibility(View.VISIBLE);
                    findViewById(R.id.location_flag3).setVisibility(View.VISIBLE);
                    mOffTimeTextview3.setVisibility(View.VISIBLE);
                    mOnTimeTextview3.setText(onTimeSetting);
                    mOffTimeTextview3.setText(offTimeSetting);

                    flags[2] = true;
                } else {
                    mOnTimeTextview3.setText("00:00");
                    mOffTimeTextview3.setText("00:00");
                    mSelectImage3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectweek));
                    mRestTextview3.setVisibility(View.VISIBLE);
                    mOnTimeTextview3.setVisibility(View.GONE);
                    findViewById(R.id.location_flag3).setVisibility(View.GONE);
                    mOffTimeTextview3.setVisibility(View.GONE);
                    flags[2] = false;
                }
                break;
            case R.id.weekRelativelayout4:
                if (flags[3] == false) {
                    mSelectImage4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectedweek));
                    mRestTextview4.setVisibility(View.GONE);
                    mOnTimeTextview4.setVisibility(View.VISIBLE);
                    findViewById(R.id.location_flag4).setVisibility(View.VISIBLE);
                    mOffTimeTextview4.setVisibility(View.VISIBLE);
                    mOnTimeTextview4.setText(onTimeSetting);
                    mOffTimeTextview4.setText(offTimeSetting);

                    flags[3] = true;
                } else {
                    mOnTimeTextview4.setText("00:00");
                    mOffTimeTextview4.setText("00:00");
                    mSelectImage4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectweek));
                    mRestTextview4.setVisibility(View.VISIBLE);
                    mOnTimeTextview4.setVisibility(View.GONE);
                    findViewById(R.id.location_flag4).setVisibility(View.GONE);
                    mOffTimeTextview4.setVisibility(View.GONE);
                    flags[3] = false;
                }
                break;
            case R.id.weekRelativelayout5:
                if (flags[4] == false) {
                    mSelectImage5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectedweek));
                    mRestTextview5.setVisibility(View.GONE);
                    mOnTimeTextview5.setVisibility(View.VISIBLE);
                    findViewById(R.id.location_flag5).setVisibility(View.VISIBLE);
                    mOffTimeTextview5.setVisibility(View.VISIBLE);
                    mOnTimeTextview5.setText(onTimeSetting);
                    mOffTimeTextview5.setText(offTimeSetting);

                    flags[4] = true;
                } else {
                    mOnTimeTextview5.setText("00:00");
                    mOffTimeTextview5.setText("00:00");
                    mSelectImage5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectweek));
                    mRestTextview5.setVisibility(View.VISIBLE);
                    mOnTimeTextview5.setVisibility(View.GONE);
                    findViewById(R.id.location_flag5).setVisibility(View.GONE);
                    mOffTimeTextview5.setVisibility(View.GONE);
                    flags[4] = false;
                }
                break;
            case R.id.weekRelativelayout6:
                if (flags[5] == false) {
                    mSelectImage6.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectedweek));
                    mRestTextview6.setVisibility(View.GONE);
                    mOnTimeTextview6.setVisibility(View.VISIBLE);
                    findViewById(R.id.location_flag6).setVisibility(View.VISIBLE);
                    mOffTimeTextview6.setVisibility(View.VISIBLE);
                    mOnTimeTextview6.setText(onTimeSetting);
                    mOffTimeTextview6.setText(offTimeSetting);

                    flags[5] = true;
                } else {
                    mOnTimeTextview6.setText("00:00");
                    mOffTimeTextview6.setText("00:00");
                    mSelectImage6.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectweek));
                    mRestTextview6.setVisibility(View.VISIBLE);
                    mOnTimeTextview6.setVisibility(View.GONE);
                    findViewById(R.id.location_flag6).setVisibility(View.GONE);
                    mOffTimeTextview6.setVisibility(View.GONE);
                    flags[5] = false;
                }
                break;
            case R.id.weekRelativelayout7:
                if (flags[6] == false) {
                    mSelectImage7.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectedweek));
                    mRestTextview7.setVisibility(View.GONE);
                    mOnTimeTextview7.setVisibility(View.VISIBLE);
                    locationFlagTextviews[6].setVisibility(View.VISIBLE);
                    mOffTimeTextview7.setVisibility(View.VISIBLE);
                    mOnTimeTextview7.setText(onTimeSetting);
                    mOffTimeTextview7.setText(offTimeSetting);

                    flags[6] = true;
                } else {
                    mOnTimeTextview7.setText("00:00");
                    mOffTimeTextview7.setText("00:00");
                    mSelectImage7.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectweek));
                    mRestTextview7.setVisibility(View.VISIBLE);
                    mOnTimeTextview7.setVisibility(View.GONE);
                    locationFlagTextviews[6].setVisibility(View.GONE);
                    mOffTimeTextview7.setVisibility(View.GONE);
                    flags[6] = false;
                }
                break;
            case R.id.onTime1:
                AlertDialog.Builder builderSelectOnTime1 = new AlertDialog.Builder(this)
                        .setItems(onTimes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mOnTimeTextview1.setText(onTimes[which]);
                                onTimeSetting = onTimes[which];
                                for (int i = 0; i < flags.length; i++) {
                                    if (flags[i]) {
                                        onTimeTextviews[i].setText(onTimes[which]);
                                    }
                                }

                            }
                        });
                builderSelectOnTime1.create().show();
                break;
            case R.id.onTime2:
                AlertDialog.Builder builderSelectOnTime2 = new AlertDialog.Builder(this)
                        .setItems(onTimes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mOnTimeTextview2.setText(onTimes[which]);
                                onTimeSetting = onTimes[which];
                                Log.i(TAG, flags.length + "");

                                for (int i = 0; i < flags.length; i++) {
                                    Log.i(TAG, "进入for语句");
                                    Log.i(TAG, "判断进入的各个flag的boolean类型：" + flags[i] + "");
                                    if (flags[i]) {
                                        Log.i(TAG, flags[i] + "");
                                        onTimeTextviews[i].setText(onTimes[which]);
                                    }
                                }
                            }
                        });
                builderSelectOnTime2.create().show();
                break;
            case R.id.onTime3:
                AlertDialog.Builder builderSelectOnTime3 = new AlertDialog.Builder(this)
                        .setItems(onTimes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mOnTimeTextview3.setText(onTimes[which]);
                                onTimeSetting = onTimes[which];
                                for (int i = 0; i < flags.length; i++) {
                                    if (flags[i]) {
                                        onTimeTextviews[i].setText(onTimes[which]);
                                    }
                                }
                            }
                        });
                builderSelectOnTime3.create().show();
                break;
            case R.id.onTime4:
                AlertDialog.Builder builderSelectOnTime4 = new AlertDialog.Builder(this)
                        .setItems(onTimes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mOnTimeTextview4.setText(onTimes[which]);
                                onTimeSetting = onTimes[which];
                                for (int i = 0; i < flags.length; i++) {
                                    if (flags[i]) {
                                        onTimeTextviews[i].setText(onTimes[which]);
                                    }
                                }
                            }
                        });
                builderSelectOnTime4.create().show();
                break;
            case R.id.onTime5:
                AlertDialog.Builder builderSelectOnTime5 = new AlertDialog.Builder(this)
                        .setItems(onTimes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mOnTimeTextview5.setText(onTimes[which]);
                                onTimeSetting = onTimes[which];
                                for (int i = 0; i < flags.length; i++) {
                                    if (flags[i]) {
                                        onTimeTextviews[i].setText(onTimes[which]);
                                    }
                                }
                            }
                        });
                builderSelectOnTime5.create().show();
                break;
            case R.id.onTime6:
                AlertDialog.Builder builderSelectOnTime6 = new AlertDialog.Builder(this)
                        .setItems(onTimes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mOnTimeTextview6.setText(onTimes[which]);
                                onTimeSetting = onTimes[which];

                                for (int i = 0; i < flags.length; i++) {
                                    if (flags[i]) {
                                        onTimeTextviews[i].setText(onTimes[which]);
                                    }
                                }
                            }
                        });
                builderSelectOnTime6.create().show();
                break;
            case R.id.onTime7:
                AlertDialog.Builder builderSelectOnTime7 = new AlertDialog.Builder(this)
                        .setItems(onTimes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mOnTimeTextview7.setText(onTimes[which]);
                                onTimeSetting = onTimes[which];
                                for (int i = 0; i < flags.length; i++) {
                                    if (flags[i]) {
                                        onTimeTextviews[i].setText(onTimes[which]);
                                    }
                                }
                            }
                        });
                builderSelectOnTime7.create().show();
                break;
            case R.id.offTime1:
                AlertDialog.Builder builderSelectOffTime1 = new AlertDialog.Builder(this)
                        .setItems(offTimes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mOffTimeTextview1.setText(offTimes[which]);
                                offTimeSetting = offTimes[which];
                                for (int i = 0; i < flags.length; i++) {
                                    if (flags[i]) {
                                        offTimeTextviews[i].setText(offTimes[which]);
                                    }
                                }
                            }
                        });
                builderSelectOffTime1.create().show();
                break;
            case R.id.offTime2:
                AlertDialog.Builder builderSelectOffTime2 = new AlertDialog.Builder(this)
                        .setItems(offTimes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mOffTimeTextview2.setText(offTimes[which]);
                                offTimeSetting = offTimes[which];
                                for (int i = 0; i < flags.length; i++) {
                                    if (flags[i]) {
                                        offTimeTextviews[i].setText(offTimes[which]);
                                    }
                                }
                            }
                        });
                builderSelectOffTime2.create().show();
                break;
            case R.id.offTime3:
                AlertDialog.Builder builderSelectOffTime3 = new AlertDialog.Builder(this)
                        .setItems(offTimes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mOffTimeTextview3.setText(offTimes[which]);
                                offTimeSetting = offTimes[which];
                                for (int i = 0; i < flags.length; i++) {
                                    if (flags[i]) {
                                        offTimeTextviews[i].setText(offTimes[which]);
                                    }
                                }
                            }
                        });
                builderSelectOffTime3.create().show();
                break;
            case R.id.offTime4:
                AlertDialog.Builder builderSelectOffTime4 = new AlertDialog.Builder(this)
                        .setItems(offTimes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mOffTimeTextview4.setText(offTimes[which]);
                                offTimeSetting = offTimes[which];
                                for (int i = 0; i < flags.length; i++) {
                                    if (flags[i]) {
                                        offTimeTextviews[i].setText(offTimes[which]);
                                    }
                                }
                            }
                        });
                builderSelectOffTime4.create().show();
                break;
            case R.id.offTime5:
                AlertDialog.Builder builderSelectOffTime5 = new AlertDialog.Builder(this)
                        .setItems(offTimes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mOffTimeTextview5.setText(offTimes[which]);
                                offTimeSetting = offTimes[which];
                                for (int i = 0; i < flags.length; i++) {
                                    if (flags[i]) {
                                        offTimeTextviews[i].setText(offTimes[which]);
                                    }
                                }
                            }
                        });
                builderSelectOffTime5.create().show();
                break;
            case R.id.offTime6:
                AlertDialog.Builder builderSelectOffTime6 = new AlertDialog.Builder(this)
                        .setItems(offTimes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mOffTimeTextview6.setText(offTimes[which]);
                                offTimeSetting = offTimes[which];
                                for (int i = 0; i < flags.length; i++) {
                                    if (flags[i]) {
                                        offTimeTextviews[i].setText(offTimes[which]);
                                    }
                                }
                            }
                        });
                builderSelectOffTime6.create().show();
                break;
            case R.id.offTime7:
                AlertDialog.Builder builderSelectOffTime7 = new AlertDialog.Builder(this)
                        .setItems(offTimes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mOffTimeTextview7.setText(offTimes[which]);
                                offTimeSetting = offTimes[which];
                                for (int i = 0; i < flags.length; i++) {
                                    if (flags[i]) {
                                        offTimeTextviews[i].setText(offTimes[which]);
                                    }
                                }
                            }
                        });
                builderSelectOffTime7.create().show();
                break;
            case R.id.autoSelectRelativelayout:
                if (autoToggleFlag == false) {
                    mImageAutoToggle.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectedweek));
                    autoToggleFlag = true;
                } else {
                    mImageAutoToggle.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectweek));
                    autoToggleFlag = false;
                }
                break;
            case R.id.DateAndTimeCommitButton:
                Intent weekInfoIntent = new Intent(getApplicationContext(), SignSettingFragment.class);
                //onWeekString = "";
                offWeekString = "";
                for (int i = 0; i < flags.length; i++) {
                    if (flags[i]) {
                        onWeekString += weekStrings[i] + " ";
                    } else {
                        offWeekString += weekStrings[i] + " ";
                    }
                }
                weekInfoIntent.putExtra("deliverOnString", onWeekString);
                weekInfoIntent.putExtra("deliverOffString", offWeekString);
                weekInfoIntent.putExtra("deliverOnTime", onTimeSetting.trim());
                Log.i("deliverOnTime", "传送方上班时间：" + onTimeSetting.trim());
                weekInfoIntent.putExtra("deliverOffTime", offTimeSetting.trim());
                Log.i("deliverOnTime", "传送方下班时间：" + offTimeSetting.trim());
                weekInfoIntent.putExtra("deliverAutoSelect", autoToggleFlag);
                setResult(WEEKANDTIMERESULTCODE, weekInfoIntent);
                finish();
                break;
        }

    }
}
