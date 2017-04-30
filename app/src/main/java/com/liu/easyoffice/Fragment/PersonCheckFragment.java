package com.liu.easyoffice.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liu.easyoffice.MyView.CustomStripProgressbarDataView;
import com.liu.easyoffice.MyView.CustomStripProgressbarView;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MyCommonPersonSyncTask;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.pojo.User;

import org.feezu.liuli.timeselector.TimeSelector_year_month;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */

public class PersonCheckFragment extends Fragment {
    private static final String TAG = "PersonCheckFragment";
    private View v;
    private TextView mPersonCheckingDateTextview;
    private MyCommonPersonSyncTask myCommonPersonSyncTask;
    private int mYear;
    private int mMonth;
    private TextView mNameTextView;
    private String nameString;
    private int normalCount;
    private int lateCount;
    private int earlyOffCount;
    private int askforleaveCount;
    private int onBusinessCount;
    private int absentCount;
    private int aveTime;
    private Long employeeId;
    private Long companyId;
    private String yearString;
    private String monthString;

    private CustomStripProgressbarView mStripProgressBarNormal;
    private CustomStripProgressbarView mStripProgressBarOnBusiness;
    private CustomStripProgressbarView mStripProgressBarAskforleave;
    private CustomStripProgressbarView mStripProgressBarAbsenteeism;
    private CustomStripProgressbarView mStripProgressBarLate;
    private CustomStripProgressbarView mStripProgressBarLeaveEarly;
    private CustomStripProgressbarDataView mStripProgressBarDataNormal;
    private CustomStripProgressbarDataView mStripProgressBarDataLeaveEarly;
    private CustomStripProgressbarDataView mStripProgressBarDataLate;
    private CustomStripProgressbarDataView mStripProgressBarDataAbsenteeism;
    private CustomStripProgressbarDataView mStripProgressBarDataAskforleave;
    private CustomStripProgressbarDataView mStripProgressBarDataOnBusiness;
    private TextView mAvgHoursTextview;
    private ImageView mPortImageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_sign_person_check, null);
        initData();
        initView();
        updateView();
        initEvent();
        return v;
    }


    private void initData() {
        nameString = MySharePreference.getCurrentUser(getActivity()).getUserName();
        employeeId = MySharePreference.getCurrentUser(getActivity().getApplicationContext()).getId();
        Log.i(TAG,"员工Id"+employeeId);
        companyId = MySharePreference.getCurrentCompany(getActivity().getApplicationContext()).getTcId();
    }

    private void initView() {
        mPortImageView = ((ImageView) v.findViewById(R.id.person_info_portrait_image));
        mStripProgressBarNormal = ((CustomStripProgressbarView) v.findViewById(R.id.customStripProgressbarViewNormal));
        mStripProgressBarLeaveEarly = ((CustomStripProgressbarView) v.findViewById(R.id.customStripProgressbarViewLeaveEarly));
        mStripProgressBarLate = ((CustomStripProgressbarView) v.findViewById(R.id.customStripProgressbarViewLate));
        mStripProgressBarAbsenteeism = ((CustomStripProgressbarView) v.findViewById(R.id.customStripProgressbarViewAbsenteeism));
        mStripProgressBarAskforleave = ((CustomStripProgressbarView) v.findViewById(R.id.customStripProgressbarViewAskforleave));
        mStripProgressBarOnBusiness = ((CustomStripProgressbarView) v.findViewById(R.id.customStripProgressbarViewOnBusiness));
        mStripProgressBarDataNormal = ((CustomStripProgressbarDataView) v.findViewById(R.id.customStripProgressbarDataViewNormal));
        mStripProgressBarDataLeaveEarly = ((CustomStripProgressbarDataView) v.findViewById(R.id.customStripProgressbarDataViewLeaveEarly));
        mStripProgressBarDataLate = ((CustomStripProgressbarDataView) v.findViewById(R.id.customStripProgressbarDataViewLate));
        mStripProgressBarDataAbsenteeism = ((CustomStripProgressbarDataView) v.findViewById(R.id.customStripProgressbarDataViewAbsenteeism));
        mStripProgressBarDataAskforleave = ((CustomStripProgressbarDataView) v.findViewById(R.id.customStripProgressbarDataViewAskforleave));
        mStripProgressBarDataOnBusiness = ((CustomStripProgressbarDataView) v.findViewById(R.id.customStripProgressbarDataViewOnBusiness));
        mAvgHoursTextview = ((TextView) v.findViewById(R.id.check_person_avg_hours_count_textview));
        mNameTextView = ((TextView) v.findViewById(R.id.sign_name_check));
        mPersonCheckingDateTextview = ((TextView) v.findViewById(R.id.person_check_select_date_textview));
        mNameTextView.setText(nameString);
        mPersonCheckingDateTextview.setText(acquireCurrentTime());
        RequestParams queryImageParams = new RequestParams(Utils.QUERY_PERSON_IMAGE);
        queryImageParams.addBodyParameter("employeeId",employeeId+"");
        x.http().post(queryImageParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson mGson = new Gson();
                try {
                    String urlString = URLDecoder.decode(result,"utf-8");
                    User userGson = mGson.fromJson(urlString, User.class);
                    xUtilsImageUtils.display(mPortImageView,userGson.getImgUrl(),true);
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

    private void updateView() {
        normalCount = 0;
        lateCount = 0;
        earlyOffCount = 0;
        askforleaveCount = 0;
        onBusinessCount = 0;
        absentCount = 0;
        //1.获取当前控件的年月
        yearString = mPersonCheckingDateTextview.getText().toString().substring(0,4);
        monthString = mPersonCheckingDateTextview.getText().toString().substring(5,7);
        //2.查询出该月份、该员工的平均工时(根据其empId)
        //3.分别查询出各种情况下的数量(表signinandoffinfo)
        RequestParams queryPersonCheckingParams = new RequestParams(Utils.QUERY_PERSON_CHECKING_INFO);
        queryPersonCheckingParams.addBodyParameter("yearString",yearString);
        queryPersonCheckingParams.addBodyParameter("monthString",monthString);
        queryPersonCheckingParams.addBodyParameter("companyId",companyId+"");
        queryPersonCheckingParams.addBodyParameter("employeeId",employeeId+"");
        x.http().post(queryPersonCheckingParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson mGson = new Gson();
                Type type = new TypeToken<List<Integer>>() {}.getType();
                List<Integer> statusIntList = mGson.fromJson(result,type);
                Log.i(TAG,"传递过来的statusIntList："+statusIntList);
                Log.i(TAG,"normalCount"+statusIntList.get(0));
                Log.i(TAG,"earlyOffCount"+statusIntList.get(1));
                Log.i(TAG,"lateCount"+statusIntList.get(2));
                Log.i(TAG,"askforleaveCount"+statusIntList.get(3));
                Log.i(TAG,"onBusinessCount"+statusIntList.get(4));
                Log.i(TAG,"aveTime"+statusIntList.get(5));
                normalCount = statusIntList.get(0);
                earlyOffCount = statusIntList.get(1);
                lateCount = statusIntList.get(2);
                askforleaveCount = statusIntList.get(3);
                onBusinessCount = statusIntList.get(4);
                aveTime = statusIntList.get(5);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAvgHoursTextview.setText(aveTime+"");
                                myCommonPersonSyncTask = new MyCommonPersonSyncTask(mStripProgressBarNormal,
                                        mStripProgressBarDataNormal,
                                        mStripProgressBarLeaveEarly,
                                        mStripProgressBarDataLeaveEarly,
                                        mStripProgressBarLate,
                                        mStripProgressBarDataLate,
                                        mStripProgressBarAskforleave,
                                        mStripProgressBarDataAskforleave,
                                        mStripProgressBarOnBusiness,
                                        mStripProgressBarDataOnBusiness,
                                        mStripProgressBarAbsenteeism,
                                        mStripProgressBarDataAbsenteeism);
                                //参数为传递过去的进度总值，doInBackground(Object[] params)方法的参数一一对应
                                myCommonPersonSyncTask.execute(normalCount, earlyOffCount, lateCount,
                                        askforleaveCount,onBusinessCount,absentCount);

                            }
                        });
                    }
                },40);


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


    private void initEvent() {
        mPersonCheckingDateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimeSelector_year_month(getActivity(), new TimeSelector_year_month.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        String[] timeString = time.split("-");
                        mPersonCheckingDateTextview.setText(timeString[0] + "年" + timeString[1] + "月");
                        updateView();
                    }
                }, "1970-01-01", "2100-12-31").show();
            }
        });

    }

    private String acquireCurrentTime() {
        Calendar mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        String dateString = null;
        if (mMonth < 10) {
            dateString = mYear + "年0" + mMonth + "月";
        } else {
            dateString = mYear + "年" + mMonth + "月";
        }
        return dateString;
    }
}
