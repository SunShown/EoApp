package com.liu.easyoffice.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liu.easyoffice.Activity.SignAddressAdjustment;
import com.liu.easyoffice.Activity.SignWeekAndTimeByAdminActivity;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.pojo.CompanySignSettingInfo;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/10/19.
 */

public class SignSettingFragment extends Fragment implements View.OnClickListener {
    private static final int CHECKINGTIMEREQUEST = 3;
    private static final String TAG = "WeekAndTime";
    private View v;
    private RelativeLayout mAddAddressByAdministratorRelativelayout;
    private TextView mFirstParaTextview;
    private TextView mSecondParaTextview;
    private RelativeLayout mSelectDistanceselectRelativelayout;
    private String[] distanceItems;
    private String[] timeSelectItems;
    private String[] hardworkingTimes;
    private TextView mDistanceTextview;
    private RelativeLayout mSelectTimeDurationRelativelayout;
    private TextView mElasticityTimeTextview;
    private RelativeLayout mLateTimeRelativelayout;
    private TextView mLateTimeTextview;
    private RelativeLayout mAheadTimeRelativelayout;
    private TextView mAheadTimeTextview;
    private RelativeLayout mOnRemindTimeRelativelayout;
    private TextView mOnRemindTimeTextview;
    private RelativeLayout mOffRemindTimeRelativelayout;
    private TextView mOffRemindTextview;
    private RelativeLayout mCheckingTimeRelativelayout;
    private RelativeLayout mHardworkingRelativelayout;
    private TextView mHardworkingTextview;
    private TextView mScheduleWeekTextview1;
    private TextView mScheduleWeekTextview2;
    private ImageView mIconImageview;
    private Button mCommitButton;
    private TextView mAddressTextview;
    private RelativeLayout mAddressDetailsRelativelayout;
    private View mStripView6;
    private ImageView mSelectImageview;


    private double latitudeDouble;  //维度
    private double longitudeDouble;  //经度
    private String addressTitleString;  //地点主信息
    private String addressSnippetString; //地点位置信息
    private String deliverOnString = "";  //上班周信息
    private String deliverOffString = "";  //休息周信息
    private String deliverOnTime;     //上班时间
    private String deliverOffTime;   //下班时间
    private Boolean deliverAutoSelect;  //是否自动排休
    private boolean isNextDayBegin;   //是否是按照明天开计算工作日的(考勤的)，默认为false，说明默认是从下周一开始的

    private long companyId;  //公司ID
    private CompanySignSettingInfo companySignSettingInfo = new CompanySignSettingInfo();
    private RelativeLayout mNextDayOrWeekRelativelayout;
    private ImageView mAutoNextDayImageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_sign_setting_fragment, null);
        initViewData();
        return v;
    }

    private void initViewData() {
        distanceItems = new String[]{"关闭", "50米", "100米", "150米", "200米", "250米", "300米", "350米", "400米", "450米", "500米"};
        hardworkingTimes = new String[]{"关闭", "6小时", "7小时", "8小时", "9小时", "10小时", "11小时", "12小时", "13小时", "14小时"};
        timeSelectItems = new String[121];
        timeSelectItems[0] = "关闭";
        for (int i = 1; i < 121; i++) {
            timeSelectItems[i] = i + "分钟";
        }
        companyId = MySharePreference.getCurrentCompany(getActivity().getApplicationContext()).getTcId();
        mAddAddressByAdministratorRelativelayout = ((RelativeLayout) v.findViewById(R.id.addaddress_relativelayout));
        mSelectDistanceselectRelativelayout = ((RelativeLayout) v.findViewById(R.id.select_distance_relativelayout));
        mDistanceTextview = ((TextView) v.findViewById(R.id.after_motify_distance_textview));
        mSelectTimeDurationRelativelayout = ((RelativeLayout) v.findViewById(R.id.setting_elasticity_time_relativelayout));
        mElasticityTimeTextview = ((TextView) v.findViewById(R.id.elasticity_time));
        mLateTimeRelativelayout = ((RelativeLayout) v.findViewById(R.id.setting_late_time_relativelayout));
        mLateTimeTextview = ((TextView) v.findViewById(R.id.late_time));
        mAheadTimeRelativelayout = ((RelativeLayout) v.findViewById(R.id.setting_ahead_time_relativelayout));
        mAheadTimeTextview = ((TextView) v.findViewById(R.id.ahead_time));
        mOnRemindTimeRelativelayout = ((RelativeLayout) v.findViewById(R.id.setting_on_remind_time_relativelayout));
        mOnRemindTimeTextview = ((TextView) v.findViewById(R.id.on_remind_time));
        mOffRemindTimeRelativelayout = ((RelativeLayout) v.findViewById(R.id.setting_off_remind_time_relativelayout));
        mOffRemindTextview = ((TextView) v.findViewById(R.id.off_remind_time));
        mCheckingTimeRelativelayout = ((RelativeLayout) v.findViewById(R.id.checking_time_relativelayout));
        mHardworkingRelativelayout = ((RelativeLayout) v.findViewById(R.id.hardworking_relativelayout));
        mHardworkingTextview = ((TextView) v.findViewById(R.id.hardworkingCheckingTextview));
        mFirstParaTextview = ((TextView) v.findViewById(R.id.firstPara));
        mSecondParaTextview = ((TextView) v.findViewById(R.id.secondPara));
        mScheduleWeekTextview1 = ((TextView) v.findViewById(R.id.schedule_weekly1));
        mScheduleWeekTextview2 = ((TextView) v.findViewById(R.id.schedule_weekly2));
        mCommitButton = ((Button) v.findViewById(R.id.commit));
        mAddressTextview = ((TextView) v.findViewById(R.id.addaddress_textview));
        mSelectImageview = ((ImageView) v.findViewById(R.id.addaddress_imageview));
        mAddressDetailsRelativelayout = ((RelativeLayout) v.findViewById(R.id.addAddress_details_relativelayout));
        mStripView6 = v.findViewById(R.id.strip_line6);
        mNextDayOrWeekRelativelayout = ((RelativeLayout) v.findViewById(R.id.nextDayorWeek_relativelayout));
        mAutoNextDayImageView = ((ImageView) v.findViewById(R.id.autoNextDayToggle));
        mAddAddressByAdministratorRelativelayout.setOnClickListener(this);
        mSelectDistanceselectRelativelayout.setOnClickListener(this);
        mSelectTimeDurationRelativelayout.setOnClickListener(this);
        mLateTimeRelativelayout.setOnClickListener(this);
        mAheadTimeRelativelayout.setOnClickListener(this);
        mOnRemindTimeRelativelayout.setOnClickListener(this);
        mOffRemindTimeRelativelayout.setOnClickListener(this);
        mCheckingTimeRelativelayout.setOnClickListener(this);
        mHardworkingRelativelayout.setOnClickListener(this);
        mCommitButton.setOnClickListener(this);
        mNextDayOrWeekRelativelayout.setOnClickListener(this);

        //从服务器上边拿取数据，显示在设置界面
        RequestParams showSettingInfoParams = new RequestParams(Utils.SHOW_SIGNSETTINGINFO);
        showSettingInfoParams.addBodyParameter("companyId", companyId + "");
        Log.i(TAG, companyId + "");
        x.http().post(showSettingInfoParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson mGson = new Gson();
                try {
                    if (result != null) {

                        String UrlGsonString = URLDecoder.decode(result, "utf-8");
                        CompanySignSettingInfo companySignSettingInfo = mGson.fromJson(UrlGsonString, CompanySignSettingInfo.class);
                        deliverAutoSelect = companySignSettingInfo.isAutoBreakByLaw();
                        isNextDayBegin = (!companySignSettingInfo.isNextDayBegin());
                        Log.i(TAG, "测试是否从网络对象中拿数据！"+isNextDayBegin);
                        deliverOnTime = companySignSettingInfo.getOnTimeString();
                        deliverOffTime = companySignSettingInfo.getOffTimeString();

                        deliverOnString = companySignSettingInfo.getOnWeekString();
                        //打印onWeekInfoLists
                        Log.i(TAG, "上班周信息：" + deliverOnString);
                        mScheduleWeekTextview1.setText(deliverOnString + " 上班 " + deliverOnTime + "-" + deliverOffTime);

                        deliverOffString = companySignSettingInfo.getOffWeekString();
                        //打印offWeekInfoLists
                        Log.i(TAG, "上班周信息：" + deliverOffString);
                        mScheduleWeekTextview2.setText(deliverOffString + " 休息" + (deliverAutoSelect ? " 法定节假日自动排休" : " "));
                        mSelectImageview.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.modifyaddress));
                        mAddressTextview.setText("修改办公地点");
                        mAddressDetailsRelativelayout.setVisibility(View.VISIBLE);
                        mStripView6.setVisibility(View.VISIBLE);
                        addressTitleString = companySignSettingInfo.getStringTitle();
                        mFirstParaTextview.setText(companySignSettingInfo.getStringTitle());
                        addressSnippetString = companySignSettingInfo.getStringSnippet();
                        mSecondParaTextview.setText(companySignSettingInfo.getStringSnippet());
                        mDistanceTextview.setText(companySignSettingInfo.getIntEffectiveRange() + "米");
                        mElasticityTimeTextview.setText(companySignSettingInfo.getIntElasticTime() + "分钟");
                        mAheadTimeTextview.setText(companySignSettingInfo.getIntEarliestTime() + "分钟");
                        mOnRemindTimeTextview.setText(companySignSettingInfo.getIntOnRemind() + "分钟");
                        mOffRemindTextview.setText(companySignSettingInfo.getIntOffRemind() + "分钟");
                        mHardworkingTextview.setText(companySignSettingInfo.getIntHardAveTime() + "小时");
                        latitudeDouble = companySignSettingInfo.getDoubleLatitude();
                        longitudeDouble = companySignSettingInfo.getDoubleLongitude();
                        if (isNextDayBegin) {
                            mAutoNextDayImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectedweek));
                        } else {
                            mAutoNextDayImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectweek));
                        }

                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addaddress_relativelayout:
                Intent addAddressIntent = new Intent(getActivity().getApplicationContext(), SignAddressAdjustment.class);
                startActivityForResult(addAddressIntent, 2);
                break;
            case R.id.select_distance_relativelayout:
                AlertDialog.Builder builderSelectDistance = new AlertDialog.Builder(getActivity())
                        .setItems(distanceItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDistanceTextview.setText(distanceItems[which]);
                            }
                        });
                builderSelectDistance.create().show();
                break;
            case R.id.setting_elasticity_time_relativelayout:
                AlertDialog.Builder builderSelectTimeDuration = new AlertDialog.Builder(getActivity())
                        .setItems(timeSelectItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mElasticityTimeTextview.setText(timeSelectItems[which]);
                            }
                        });
                builderSelectTimeDuration.create().show();
                break;
//            case R.id.setting_late_time_relativelayout:
//                AlertDialog.Builder builderLateTime = new AlertDialog.Builder(getActivity())
//                        .setItems(timeSelectItems, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                mLateTimeTextview.setText(timeSelectItems[which]);
//                            }
//                        });
//                builderLateTime.create().show();
//                break;
            case R.id.setting_ahead_time_relativelayout:
                AlertDialog.Builder builderAheadTime = new AlertDialog.Builder(getActivity())
                        .setItems(timeSelectItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAheadTimeTextview.setText(timeSelectItems[which]);
                            }
                        });
                builderAheadTime.create().show();
                break;
            case R.id.setting_on_remind_time_relativelayout:
                AlertDialog.Builder builderOnRemindTime = new AlertDialog.Builder(getActivity())
                        .setItems(timeSelectItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mOnRemindTimeTextview.setText(timeSelectItems[which]);
                            }
                        });
                builderOnRemindTime.create().show();
                break;
            case R.id.setting_off_remind_time_relativelayout:
                AlertDialog.Builder builderOffRemindTime = new AlertDialog.Builder(getActivity())
                        .setItems(timeSelectItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mOffRemindTextview.setText(timeSelectItems[which]);
                            }
                        });
                builderOffRemindTime.create().show();
                break;
            case R.id.checking_time_relativelayout:
                Intent mIntent = new Intent(getActivity(), SignWeekAndTimeByAdminActivity.class);
                mIntent.putExtra("onWeekInfoIntent", deliverOnString);
                Log.i("haha", "haha" + deliverOnString);
                mIntent.putExtra("offWeekInfoIntent", deliverOffString);
                Log.i("haha", "haha" + deliverOffString);
                mIntent.putExtra("onTimeInfoIntent", deliverOnTime);
                Log.i("haha", "haha" + deliverOnTime);
                mIntent.putExtra("offTimeInfoIntent", deliverOffTime);
                Log.i("haha", "haha" + deliverOffTime);
                mIntent.putExtra("isAutoLawHolidayInfoIntent", deliverAutoSelect);
                Log.i("haha", "haha" + deliverAutoSelect);

                startActivityForResult(mIntent, CHECKINGTIMEREQUEST);
                break;
            case R.id.hardworking_relativelayout:
                AlertDialog.Builder builderHardworkingTime = new AlertDialog.Builder(getActivity())
                        .setItems(hardworkingTimes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mHardworkingTextview.setText(hardworkingTimes[which]);
                            }
                        });
                builderHardworkingTime.create().show();
                break;
            case R.id.nextDayorWeek_relativelayout:
                if (isNextDayBegin==false) {
                    mAutoNextDayImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectedweek));
                    isNextDayBegin = true;
                } else {
                    mAutoNextDayImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selectweek));
                    isNextDayBegin = false;
                }
                break;
            case R.id.commit:
                mCommitButton.setClickable(false);
                Log.i(TAG, "====================================");
                Log.i(TAG, "纬度：" + latitudeDouble + "");
                Log.i(TAG, "经度：" + longitudeDouble + "");
                Log.i(TAG, "地点主标题：" + addressTitleString + "");
                Log.i(TAG, "地点副标题：" + addressSnippetString + "");
                Log.i(TAG, "上班周信息：" + deliverOnString + "");
                Log.i(TAG, "下班周信息：" + deliverOffString + "");
                Log.i(TAG, "上班时间：" + deliverOnTime + "");
                Log.i(TAG, "下班时间：" + deliverOffTime + "");
                Log.i(TAG, "是否自动排休：" + deliverAutoSelect + "");
                Log.i(TAG, "上班弹性时间：" + mElasticityTimeTextview.getText().toString() + "");
//                Log.i(TAG, "迟到及旷工时间：" + mLateTimeTextview.getText().toString() + "");
                Log.i(TAG, "最高提前时间：" + mAheadTimeTextview.getText().toString() + "");
                Log.i(TAG, "有效距离：" + mDistanceTextview.getText().toString() + "");
                Log.i(TAG, "上榜时间：" + mHardworkingTextview.getText().toString() + "");
                Log.i(TAG, "====================================");
                if (latitudeDouble != 0
                        && longitudeDouble != 0
                        && addressTitleString != null
                        && addressSnippetString != null
                        && deliverOnString != null
                        && deliverOffString != null
                        && deliverOnTime != "00:00"
                        && deliverOffTime != "00:00"
                        && deliverAutoSelect != null
                        && mElasticityTimeTextview.getText() != "关闭"
                        && mAheadTimeTextview.getText() != "关闭"
                        && mDistanceTextview.getText() != "关闭"
                        && mHardworkingTextview.getText() != "关闭"
                        ) {
                    commitData();

                } else {
                    mCommitButton.setClickable(true);
                    Toast.makeText(getActivity().getApplicationContext(), "您有未设置项,请继续设置", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void commitData() {

        //给companySignSettingInfo对象初始化
        companySignSettingInfo.setAutoBreakByLaw(deliverAutoSelect);  //是否自动排班
        companySignSettingInfo.setDoubleLatitude(latitudeDouble);  //纬度
        companySignSettingInfo.setDoubleLongitude(longitudeDouble);  //经度
        int mAheadTimeTextviewLength = mAheadTimeTextview.getText().toString().length();
        companySignSettingInfo.setIntEarliestTime(Integer.valueOf(mAheadTimeTextview.getText().toString().substring(0, mAheadTimeTextviewLength - 2)));
        int meffectiveRangeLength = mDistanceTextview.getText().toString().length();
        companySignSettingInfo.setIntEffectiveRange(Integer.valueOf(mDistanceTextview.getText().toString().substring(0, meffectiveRangeLength - 1)));
        companySignSettingInfo.setStringTitle(addressTitleString);
        companySignSettingInfo.setStringSnippet(addressSnippetString);
        int elasticityTime = mElasticityTimeTextview.getText().toString().length();
        companySignSettingInfo.setIntElasticTime(Integer.valueOf(mElasticityTimeTextview.getText().toString().substring(0, elasticityTime - 2)));
        int averOnTime = mHardworkingTextview.getText().toString().length();
        companySignSettingInfo.setIntHardAveTime(Integer.valueOf(mHardworkingTextview.getText().toString().substring(0, averOnTime - 2)));
        companySignSettingInfo.setOnTimeString(deliverOnTime);
        companySignSettingInfo.setOffTimeString(deliverOffTime);
        companySignSettingInfo.setOnWeekString(deliverOnString);
        companySignSettingInfo.setOffWeekString(deliverOffString);
        companySignSettingInfo.setNextDayBegin(isNextDayBegin);

        Log.i(TAG, "提交中的文件对象信息：" + companySignSettingInfo + "");
        Log.i(TAG, "公司Id" + companyId);


        RequestParams params = new RequestParams(Utils.SEND_SETTING_INFO);
        Gson gson = new Gson();
        String gsonString = null;
        try {
            gsonString = URLEncoder.encode(gson.toJson(companySignSettingInfo), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        params.addBodyParameter("companyId", companyId + "");  //post方式
        params.addParameter("companySignSettingInfo", gsonString);  //可以传对象过去，不用Gson  get方式(有大小限制的，相当于拼接在地址后边)
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mCommitButton.setClickable(true);
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mCommitButton.setClickable(true);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                mCommitButton.setClickable(true);

            }

            @Override
            public void onFinished() {
                mCommitButton.setClickable(true);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case 2:
                    addressTitleString = data.getStringExtra("title");
                    addressSnippetString = data.getStringExtra("snippet");
                    latitudeDouble = data.getDoubleExtra("latitude", 0);
                    longitudeDouble = data.getDoubleExtra("longitude", 0);
                    mFirstParaTextview.setText(addressTitleString);
                    mSecondParaTextview.setText(addressSnippetString);
                    mAddressDetailsRelativelayout.setVisibility(View.VISIBLE);
                    mStripView6.setVisibility(View.VISIBLE);
                    mAddressTextview.setText("修改办公地点");
                    mSelectImageview.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.modifyaddress));
                    break;
                case CHECKINGTIMEREQUEST:      //给返回的SignSettingFragment的schedule_weekly1、schedule_weekly2赋值
                    deliverOnString = data.getStringExtra("deliverOnString");
                    deliverOffString = data.getStringExtra("deliverOffString");
                    deliverOnTime = data.getStringExtra("deliverOnTime");
                    Log.i("deliverOnTime", "接收方上班时间：" + deliverOnTime);
                    deliverOffTime = data.getStringExtra("deliverOffTime");
                    Log.i("deliverOnTime", "接收方下班时间：" + deliverOffTime);
                    deliverAutoSelect = data.getBooleanExtra("deliverAutoSelect", false);
                    mScheduleWeekTextview1.setText(deliverOnString + " 上班 " + deliverOnTime + "-" + deliverOffTime);
                    mScheduleWeekTextview2.setText(deliverOffString + " 休息" + (deliverAutoSelect ? " 法定节假日自动排休" : " "));

            }
        }
    }
}
