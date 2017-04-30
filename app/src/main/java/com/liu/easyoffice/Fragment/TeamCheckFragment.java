package com.liu.easyoffice.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liu.easyoffice.Activity.SignRankingListActivity;
import com.liu.easyoffice.MyView.CustomStripProgressbarDataView;
import com.liu.easyoffice.MyView.CustomStripProgressbarView;
import com.liu.easyoffice.MyView.RoundProgressBar;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MyCommonSyncTask;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.pojo.User;

import org.feezu.liuli.timeselector.TimeSelector;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

/**
 * Created by Administrator on 2016/10/21.
 */

public class TeamCheckFragment extends Fragment implements View.OnClickListener {
    private View v;
    private RelativeLayout mWholeRankingListRelativelayout;
    private TextView mSelectDateTextview;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mMonthAfter;
    private RoundProgressBar mRoundProgressBar;

    private static final String TAG = "TeamCheckFragment";
    private CustomStripProgressbarView mStripProgressBarNormal;
    private CustomStripProgressbarView mStripProgressBarOnBusiness;
    private CustomStripProgressbarView mStripProgressBarAskforleave;
    private CustomStripProgressbarView mStripProgressBarAbsenteeism;
    private CustomStripProgressbarView mStripProgressBarLate;
    private CustomStripProgressbarView mStripProgressBarLeaveEarly;
    private TextView mMonthHardworkingCheckTextview;
    private RelativeLayout mLeftDateRelativelayout;
    private RelativeLayout mRightDateRelativelayout;
    private CustomStripProgressbarDataView mStripProgressBarDataNormal;
    private CustomStripProgressbarDataView mStripProgressBarDataLeaveEarly;
    private CustomStripProgressbarDataView mStripProgressBarDataLate;
    private CustomStripProgressbarDataView mStripProgressBarDataAbsenteeism;
    private CustomStripProgressbarDataView mStripProgressBarDataAskforleave;
    private CustomStripProgressbarDataView mStripProgressBarDataOnBusiness;
    private Timer mTimer;
    private Long companyId;
    private TextView mCheckLevle1Textview;
    private TextView mCheckLevle2Textview;
    private TextView mCheckLevle3Textview;
    private xUtilsImageUtils xUtilsImage = new xUtilsImageUtils();
    private ImageView mImageView1;
    private ImageView mImageView2;
    private ImageView mImageView3;
    private MyCommonSyncTask mMyCommonSyncTask;
    private int totalCount;
    private int checkingCount;
    private int normalCount;
    private int lateCount;
    private int earlyOffCount;
    private int askforleaveCount;
    private int onBusinessCount;
    private int absentCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_sign_team_check, null);
        companyId = MySharePreference.getCurrentCompany(getActivity()).getTcId();  //获得公司Id
        initData();
        initView();
        updateView();
        return v;
    }

    private void initData() {
        Calendar mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mMonthAfter = mMonth + 1;
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

    }

    private void initView() {
        mWholeRankingListRelativelayout = ((RelativeLayout) v.findViewById(R.id.month_hardworking_check_relativelayout));
        mSelectDateTextview = ((TextView) v.findViewById(R.id.selected_date));
        mLeftDateRelativelayout = ((RelativeLayout) v.findViewById(R.id.select_date_left_point_relativelayout));
        mRightDateRelativelayout = ((RelativeLayout) v.findViewById(R.id.select_date_right_point_relativelayout));
        mRoundProgressBar = ((RoundProgressBar) v.findViewById(R.id.roundProgressBar));
        mStripProgressBarNormal = ((CustomStripProgressbarView) v.findViewById(R.id.customStripProgressbarViewNormal));
        mStripProgressBarLeaveEarly = ((CustomStripProgressbarView) v.findViewById(R.id.customStripProgressbarViewLeaveEarly));
        mStripProgressBarLate = ((CustomStripProgressbarView) v.findViewById(R.id.customStripProgressbarViewLate));
        mStripProgressBarAbsenteeism = ((CustomStripProgressbarView) v.findViewById(R.id.customStripProgressbarViewAbsenteeism));
        mStripProgressBarAskforleave = ((CustomStripProgressbarView) v.findViewById(R.id.customStripProgressbarViewAskforleave));
        mStripProgressBarOnBusiness = ((CustomStripProgressbarView) v.findViewById(R.id.customStripProgressbarViewOnBusiness));
        mMonthHardworkingCheckTextview = ((TextView) v.findViewById(R.id.month_hardworking_check_textview));
        mStripProgressBarDataNormal = ((CustomStripProgressbarDataView) v.findViewById(R.id.customStripProgressbarDataViewNormal));
        mStripProgressBarDataLeaveEarly = ((CustomStripProgressbarDataView) v.findViewById(R.id.customStripProgressbarDataViewLeaveEarly));
        mStripProgressBarDataLate = ((CustomStripProgressbarDataView) v.findViewById(R.id.customStripProgressbarDataViewLate));
        mStripProgressBarDataAbsenteeism = ((CustomStripProgressbarDataView) v.findViewById(R.id.customStripProgressbarDataViewAbsenteeism));
        mStripProgressBarDataAskforleave = ((CustomStripProgressbarDataView) v.findViewById(R.id.customStripProgressbarDataViewAskforleave));
        mStripProgressBarDataOnBusiness = ((CustomStripProgressbarDataView) v.findViewById(R.id.customStripProgressbarDataViewOnBusiness));
        mCheckLevle1Textview = ((TextView) v.findViewById(R.id.check_level1_textview));
        mCheckLevle2Textview = ((TextView) v.findViewById(R.id.check_level2_textview));
        mCheckLevle3Textview = ((TextView) v.findViewById(R.id.check_level3_textview));
        mImageView1 = ((ImageView) v.findViewById(R.id.check_portrait1_image));
        mImageView2 = ((ImageView) v.findViewById(R.id.check_portrait2_image));
        mImageView3 = ((ImageView) v.findViewById(R.id.check_portrait3_image));
        mMonthHardworkingCheckTextview.setText(mMonthAfter + "月排行榜");  //设置显示排行榜的月份
        //设定日期
        if (mMonthAfter >= 10 && mDay >= 10) {
            mSelectDateTextview.setText(mYear + "-" + mMonthAfter + "-" + mDay);
        } else if (mMonthAfter < 10 && mDay >= 10) {
            mSelectDateTextview.setText(mYear + "-0" + mMonthAfter + "-" + mDay);
        } else if (mMonthAfter >= 10 && mDay < 10) {
            mSelectDateTextview.setText(mYear + "-" + mMonthAfter + "-0" + mDay);
        } else {
            mSelectDateTextview.setText(mYear + "-0" + mMonthAfter + "-0" + mDay);
        }
        //根据该月份(mMonthAfter)和员工号来计算总共的工作时间，将第一到第三名排出来，其余的排行，排到下一个activity的fragment中
        RequestParams queryWorkTimesParams = new RequestParams(Utils.QUERY_WORK_TIMES);
        queryWorkTimesParams.addBodyParameter("currentMonth", mMonthAfter + "");
        queryWorkTimesParams.addBodyParameter("companyId", companyId + "");

        x.http().post(queryWorkTimesParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "result的值:" + result);
                Gson mGson = new Gson();
                Type type = new TypeToken<List<User>>() {}.getType();
                List<User> userList = mGson.fromJson(URLDecoder.decode(result), type);
                mCheckLevle1Textview.setText(userList.get(0).getUserName());
                mCheckLevle2Textview.setText(userList.get(1).getUserName());
                mCheckLevle3Textview.setText(userList.get(2).getUserName());
                xUtilsImage.display(mImageView1, userList.get(0).getImgUrl(),true);
                xUtilsImage.display(mImageView2, userList.get(1).getImgUrl(),true);
                xUtilsImage.display(mImageView3, userList.get(2).getImgUrl(),true);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i(TAG, "查询前三的员工Id的异常：" + ex + "");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        mWholeRankingListRelativelayout.setOnClickListener(this);
        mSelectDateTextview.setOnClickListener(this);
        mLeftDateRelativelayout.setOnClickListener(this);
        mRightDateRelativelayout.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.month_hardworking_check_relativelayout:
                Intent intent = new Intent(getActivity(), SignRankingListActivity.class);
                startActivity(intent);
                break;
            case R.id.selected_date:
                String dateFromTextview = mSelectDateTextview.getText().toString();
                String[] dateFromTextviews = dateFromTextview.split("-");
                int mYearFromInt = Integer.valueOf(dateFromTextviews[0]);
                int mMonthFromInt = Integer.valueOf(dateFromTextviews[1]);
                int mDayFromInt = Integer.valueOf(dateFromTextviews[2]);
                Log.i("hehe_lgy", "TeamCheckFragment:" + mYearFromInt + "-" + mMonthFromInt + "-" + mDayFromInt);
                new TimeSelector(getActivity(), new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        mSelectDateTextview.setText(time);
                        updateView();
                    }
                }, "1970-01-01", "2100-12-31").show_me(mYearFromInt, mMonthFromInt, mDayFromInt);


                break;
            case R.id.select_date_left_point_relativelayout:
                //获取当前mSelectDateTextview上的日期值
                String dateString = mSelectDateTextview.getText().toString().trim();
                String[] dateStrings = dateString.split("-");
                String currentYear = dateStrings[0];
                String currentMonth = dateStrings[1];
                String currentDay = dateStrings[2];
                Calendar currentCalendar = Calendar.getInstance();
                currentCalendar.set(Calendar.YEAR, Integer.valueOf(currentYear));
                currentCalendar.set(Calendar.MONTH, Integer.valueOf(currentMonth) - 1);
                currentCalendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(currentDay));
                currentCalendar.add(Calendar.DAY_OF_MONTH, -1);
                int afterYear = currentCalendar.get(Calendar.YEAR);
                int afterMonth = currentCalendar.get(Calendar.MONTH) + 1;
                int afterDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
                if (afterMonth >= 10 && afterDay >= 10) {
                    mSelectDateTextview.setText(afterYear + "-" + afterMonth + "-" + afterDay);
                } else if (afterMonth < 10 && afterDay >= 10) {
                    mSelectDateTextview.setText(afterYear + "-0" + afterMonth + "-" + afterDay);
                } else if (afterMonth >= 10 && afterDay < 10) {
                    mSelectDateTextview.setText(afterYear + "-" + afterMonth + "-0" + afterDay);
                } else {
                    mSelectDateTextview.setText(afterYear + "-0" + afterMonth + "-0" + afterDay);
                }
                updateView();
                break;
            case R.id.select_date_right_point_relativelayout:
                //获取当前mSelectDateTextview上的日期值
                String dateStringUp = mSelectDateTextview.getText().toString().trim();
                String[] dateStringsUp = dateStringUp.split("-");
                String currentYearUp = dateStringsUp[0];
                String currentMonthUp = dateStringsUp[1];
                String currentDayUp = dateStringsUp[2];
                Calendar currentCalendarUp = Calendar.getInstance();
                currentCalendarUp.set(Calendar.YEAR, Integer.valueOf(currentYearUp));
                currentCalendarUp.set(Calendar.MONTH, Integer.valueOf(currentMonthUp) - 1);
                currentCalendarUp.set(Calendar.DAY_OF_MONTH, Integer.valueOf(currentDayUp));
                currentCalendarUp.add(Calendar.DAY_OF_MONTH, 1);
                int afterYearUp = currentCalendarUp.get(Calendar.YEAR);
                int afterMonthUp = currentCalendarUp.get(Calendar.MONTH) + 1;
                int afterDayUp = currentCalendarUp.get(Calendar.DAY_OF_MONTH);
                if (afterMonthUp >= 10 && afterDayUp >= 10) {
                    mSelectDateTextview.setText(afterYearUp + "-" + afterMonthUp + "-" + afterDayUp);
                } else if (afterMonthUp < 10 && afterDayUp >= 10) {
                    mSelectDateTextview.setText(afterYearUp + "-0" + afterMonthUp + "-" + afterDayUp);
                } else if (afterMonthUp >= 10 && afterDayUp < 10) {
                    mSelectDateTextview.setText(afterYearUp + "-" + afterMonthUp + "-0" + afterDayUp);
                } else {
                    mSelectDateTextview.setText(afterYearUp + "-0" + afterMonthUp + "-0" + afterDayUp);
                }
                updateView();
                break;
        }
    }


    private void updateView() {

        normalCount = 0;
        lateCount = 0;
        checkingCount = 0;
        earlyOffCount = 0;
        askforleaveCount=0;
        onBusinessCount=0;
        absentCount = 0;


        //一.获取当前selected_date控件上的日期
        String dateString = mSelectDateTextview.getText().toString();
        //二.拿取当前日期的本公司员工的考勤情况(7中情况下的人数)
        //1.获取该公司下的所有员工数，这里先写死10个人
        //
        //三.将步骤2中拿取的数据，添加到timer和handler中用来更新界面

        //四.根据公司ID和日期拿取signinandoffinfo表中的各种状态下的员工数量，用来更新各自的Progress的数值
        RequestParams queryStatusParams = new RequestParams(Utils.QUERY_SIGN_STATUS);
        queryStatusParams.addBodyParameter("companyId", companyId + "");
        queryStatusParams.addBodyParameter("dateString", dateString);
        Log.i(TAG, "===========================================");
        Log.i(TAG, "公司Id：" + companyId);
        Log.i(TAG, "当前日期：" + dateString);
        Log.i(TAG, "===========================================");
        x.http().post(queryStatusParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, result + "");
                //注意总体考勤情况中的分子为签到正常和迟到的(normalCount+lateCount)，分母为公司总人数totalCount
                totalCount = Integer.valueOf(result.split(", ")[0].substring(1));  //分母
                normalCount = Integer.valueOf(result.split(", ")[1]);
                lateCount = Integer.valueOf(result.split(", ")[2]);
                checkingCount = normalCount + lateCount;  //总体考勤情况的分子
                earlyOffCount = Integer.valueOf(result.split(", ")[3]);
                askforleaveCount = Integer.valueOf(result.split(", ")[4]);
                onBusinessCount = Integer.valueOf(result.split(", ")[5].substring(0,1));
                absentCount = totalCount-normalCount-lateCount-askforleaveCount-onBusinessCount;


                Log.i(TAG, "===========================================");
                Log.i(TAG, "totalCount:" + totalCount);
                Log.i(TAG, "normalCount:" + normalCount);
                Log.i(TAG, "lateCount:" + lateCount);
                Log.i(TAG, "earlyOffCount:" + earlyOffCount);
                Log.i(TAG, "askforleaveCount:" + askforleaveCount);
                Log.i(TAG, "onBusinessCount:" + onBusinessCount);
                Log.i(TAG, "onBusinessCount:" + absentCount);
                Log.i(TAG, "===========================================");


                //延迟加载动画
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        TeamCheckFragment.this.getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                //参数为自定义或本身的进度条
//                                mMyCommonSyncTask = new MyCommonSyncTask(mRoundProgressBar, mStripProgressBarNormal
//                                        , mStripProgressBarLeaveEarly, mStripProgressBarLate, mStripProgressBarDataNormal, mStripProgressBarDataLeaveEarly,
//                                        mStripProgressBarDataLate);
//                                //参数为传递过去的进度总值，doInBackground(Object[] params)方法的参数一一对应
//                                mMyCommonSyncTask.execute(checkingCount, totalCount, normalCount, earlyOffCount, lateCount);
//                            }
//                        });
//                    }
//                }, 0);

                //参数为自定义或本身的进度条
                mMyCommonSyncTask = new MyCommonSyncTask(mRoundProgressBar, mStripProgressBarNormal
                        , mStripProgressBarLeaveEarly, mStripProgressBarLate, mStripProgressBarDataNormal, mStripProgressBarDataLeaveEarly,
                        mStripProgressBarDataLate,mStripProgressBarAskforleave,mStripProgressBarDataAskforleave,mStripProgressBarOnBusiness,
                        mStripProgressBarDataOnBusiness,mStripProgressBarAbsenteeism,mStripProgressBarDataAbsenteeism);
                //参数为传递过去的进度总值，doInBackground(Object[] params)方法的参数一一对应
                mMyCommonSyncTask.execute(checkingCount, totalCount, normalCount, earlyOffCount, lateCount,
                        askforleaveCount,onBusinessCount,absentCount);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i(TAG, ex + "");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


        //注释掉的不清楚

//        mRoundProgressBar.setPeopleCount(100);    //设置圆弧里面的分母的大小，就是公司总人数
//        final Handler mHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (msg.what == 0x121) {
//                    mRoundProgress += 1;
//                    mRoundProgressBar.setProgress(mRoundProgress);
//                }
//                if (msg.what == 0x122) {
//                    mStripNormalProgress += 1;
//                    mStripProgressBarDataNormal.setProgress(mStripNormalProgress);
//                    mStripProgressBarNormal.setProgress(mStripNormalProgress);
//                }
//                if (msg.what == 0x123) {
//                    mStripLeaveEarlyProgress += 1;
//                    mStripProgressBarLeaveEarly.setProgress(mStripLeaveEarlyProgress);
//                    mStripProgressBarDataLeaveEarly.setProgress(mStripLeaveEarlyProgress);
//                }
//                if (msg.what == 0x124) {
//                    mStripLateProgress += 1;
//                    mStripProgressBarLate.setProgress(mStripLateProgress);
//                    mStripProgressBarDataLate.setProgress(mStripLateProgress);
//                }
//                if (msg.what == 0x125) {
//                    mStripAbsenteeismProgress += 1;
//                    mStripProgressBarAbsenteeism.setProgress(mStripAbsenteeismProgress);
//                    mStripProgressBarDataAbsenteeism.setProgress(mStripAbsenteeismProgress);
//                }
//                if (msg.what == 0x126) {
//                    mStripAskforleaveProgress += 1;
//                    mStripProgressBarAskforleave.setProgress(mStripAskforleaveProgress);
//                    mStripProgressBarDataAskforleave.setProgress(mStripAskforleaveProgress);
//                }
//                if (msg.what == 0x127) {
//                    mStripOnBussinessProgress += 1;
//                    mStripProgressBarOnBusiness.setProgress(mStripOnBussinessProgress);
//                    mStripProgressBarDataOnBusiness.setProgress(mStripOnBussinessProgress);
//                }
//            }
//        };
//
//        mTimer = new Timer();
//        mTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Message mRoundMessage = new Message();
//                Message mNormalMessage = new Message();
//                Message mLeaveEarlgyMessage = new Message();
//                Message mLateMessage = new Message();
//                Message mAbsentMessage = new Message();
//                Message mAskforleaveMessage = new Message();
//                Message mOnBusinessMessage = new Message();
//                mRoundMessage.what = 0x121;
//                if (mRoundProgress < 100) {
//                    //定时项消息队列发送消息队列，以供外边的HandlerMessage处理器处理
//                    mHandler.sendMessage(mRoundMessage);
//                }
//                mNormalMessage.what = 0x122;
//                if (mStripNormalProgress < 50) {
//                    mHandler.sendMessage(mNormalMessage);
//                }
//                mLeaveEarlgyMessage.what = 0x123;
//                if (mStripLeaveEarlyProgress < 30) {
//                    mHandler.sendMessage(mLeaveEarlgyMessage);
//                }
//                mLateMessage.what = 0x124;
//                if (mStripLateProgress < 60) {
//                    mHandler.sendMessage(mLateMessage);
//                }
//                mAbsentMessage.what = 0x125;
//                if (mStripAbsenteeismProgress < 70) {
//                    mHandler.sendMessage(mAbsentMessage);
//                }
//                mAskforleaveMessage.what = 0x126;
//                if (mStripAskforleaveProgress < 20) {
//                    mHandler.sendMessage(mAskforleaveMessage);
//                }
//                mOnBusinessMessage.what = 0x127;
//                if (mStripOnBussinessProgress < 40) {
//                    mHandler.sendMessage(mOnBusinessMessage);
//                }
//            }
//        }, 0, 25);


    }   //动画更新结束


}
