package com.liu.easyoffice.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.google.gson.Gson;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.pojo.QueryCompanySettingInfoBean;
import com.liu.easyoffice.pojo.SignInAndOffInfo;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.liu.easyoffice.pojo.SignConstant.EARLYOFF;
import static com.liu.easyoffice.pojo.SignConstant.LATE;
import static com.liu.easyoffice.pojo.SignConstant.NORMALOFF;
import static com.liu.easyoffice.pojo.SignConstant.NORMALON;
import static com.liu.easyoffice.pojo.SignConstant.OVERTIMESIGNIN;
import static com.liu.easyoffice.pojo.SignConstant.OVERTIMESIGNOFF;

/**
 * Created by Administrator on 2016/10/19.
 */
public class SignConfirmActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignConfirmActivity";
    private Toolbar mToolbar;
    private TextView mCurrentBusTextview;
    private TextView mCurrentTimeText;
    private Button mCommitButton;

    private String onDateString;
    private String onTimeString;
    private String offDateString;
    private String offTimeString;
    private long companyId;
    private String signInAddressInfoConfirm;
    private Double signInLatitudeDouble;
    private Double signInLongitudeDouble;
    private String signOffAddressInfoConfirm;
    private Double signOffLatitudeDouble;
    private Double signOffLongitudeDouble;
    private Double distanceDouble;
    private Gson mGson;
    private SignInAndOffInfo mSignInAndOffInfo;
    private List<String> workdayDateLists = new ArrayList<>();
    private boolean overtimeBoolean = true;    //用于判断签到日期是否在工作日内
    private boolean overtimeOffBoolean = true;    //用于判断签退日期是否在工作日内
    private Intent intent;
    private int signStatus;
    private int signOffStatus;
    private long employeeId;
    private static final int SIGNINRESULT_CODE = 1;
    private int receiverTimeStamp;
    private int earlyOffTimes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_confirm);
        intent = this.getIntent();
        initToolbar();
        initView();
        initData();
        initEvent();

    }


    private void initToolbar() {
        mToolbar = ((Toolbar) findViewById(R.id.toolbar));
        mCurrentBusTextview = ((TextView) findViewById(R.id.currentBus));
        mCurrentTimeText = ((TextView) findViewById(R.id.currentTimeText));
        //设置Toolbar
        if (intent.getStringExtra("button_flag").equals("signIn")) {
            mToolbar.setTitle("签到确认");
        }
        if (intent.getStringExtra("button_flag").equals("signOff")) {
            mToolbar.setTitle("签退确认");
        }
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
        mCommitButton = ((Button) findViewById(R.id.commit_button));
    }

    private void initData() {
        //接受前一个签到界面传递过来的签到地点

        //点击的是签到按钮
        if (intent.getStringExtra("button_flag").equals("signIn")) {
            Log.i(TAG, "点击的是签到按钮");
            signInAddressInfoConfirm = intent.getStringExtra("signInAddressInfo");   //获得传递过来的地点信息
            signInLatitudeDouble = Double.valueOf(intent.getStringExtra("signInLatitudeInfo"));   //获得前一个界面传递过来的纬度
            signInLongitudeDouble = Double.valueOf(intent.getStringExtra("signInLongitudeInfo"));  //获得前一个界面传递过来的经度
            mCurrentBusTextview.setText(signInAddressInfoConfirm);
        }
        //点击的是签退按钮
        if (intent.getStringExtra("button_flag").equals("signOff")) {
            Log.i(TAG, "点击的是签退按钮");
            signOffLatitudeDouble = Double.valueOf(intent.getStringExtra("signOffLatitudeInfo"));
            signOffLongitudeDouble = Double.valueOf(intent.getStringExtra("signOffLongitudeInfo"));
            signOffAddressInfoConfirm = intent.getStringExtra("signOffAddressInfo");
            mCurrentBusTextview.setText(signOffAddressInfoConfirm);
        }


        //设置签到时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentTime = sdf.format(date);
        mCurrentTimeText.setText(currentTime);
        onDateString = currentTime.split(" ")[0];     //获得签到日期
        onTimeString = currentTime.split(" ")[1];     //获得签到时间
        companyId = MySharePreference.getCurrentCompany(this).getTcId();  //获得公司Id
        employeeId = MySharePreference.getCurrentUser(this).getId();
        Log.i(TAG, "employeeId" + employeeId);

    }


    private void initEvent() {
        mCommitButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        //签到按钮
        //将按钮设置为不能点击,在onSuccess(),onError(),onCancelled(),onFinished()方法中设置为true
        mCommitButton.setClickable(false);
        if (intent.getStringExtra("button_flag").equals("signIn")) {
            //通过companyID调用服务器端的servlet,查看管理员设置的信息(日期、签到时间、经纬度信息、有效范围、弹性时间
            // 、旷工及迟到时间、最早打卡时间)
            RequestParams params = new RequestParams(Utils.QUERY_SETTING_INFO);
            params.addBodyParameter("signInfo", companyId + "");  //通过POST方式
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    mCommitButton.setClickable(true);
                    Intent mIntent = new Intent();
                    //通过之前客户端发送到服务器端的数据
                    mGson = new Gson();
                    QueryCompanySettingInfoBean mCompanySettingInfoBean = mGson.fromJson(result, QueryCompanySettingInfoBean.class);
                    Double companyLatitudeDouble = mCompanySettingInfoBean.getmDoubleLatitude();
                    Double companyLongitudeDouble = mCompanySettingInfoBean.getmDoubleLongitude();

                    //计算距离
                    LatLng companyLatLng = new LatLng(companyLatitudeDouble, companyLongitudeDouble);
                    LatLng employeeLatLng = new LatLng(signInLatitudeDouble, signInLongitudeDouble);
                    double distance = AMapUtils.calculateLineDistance(companyLatLng, employeeLatLng);  //计算距离
                    workdayDateLists = mCompanySettingInfoBean.getWorkDateLists();
                    int employeeTimestamp = Integer.valueOf(onTimeString.split(":")[0]) * 60 + Integer.valueOf(onTimeString.split(":")[1]);
                    int elasticityTimestamp = Integer.valueOf(mCompanySettingInfoBean.getOnTimeString().split(":")[0]) * 60 + Integer.valueOf(mCompanySettingInfoBean.getOnTimeString().split(":")[1]) + Integer.valueOf(mCompanySettingInfoBean.getmIntElasticTime());
                    int earlistTimestamp = Integer.valueOf(mCompanySettingInfoBean.getOnTimeString().split(":")[0]) * 60 + Integer.valueOf(mCompanySettingInfoBean.getOnTimeString().split(":")[1]) - Integer.valueOf(mCompanySettingInfoBean.getmIntEarliestTime());
                    boolean flag1 = employeeTimestamp >= earlistTimestamp;   //签到时间数大于最早时间数
                    boolean flag2 = employeeTimestamp <= elasticityTimestamp;  //签到时间数小于弹性时间数
                    boolean flag3 = distance <= mCompanySettingInfoBean.getmIntEffectiveRange(); //签到距离小于公司设置距离
                    Log.i(TAG, "签到时间数大于最早时间数" + flag1);
                    Log.i(TAG, "签到时间数小于弹性时间数" + flag2);
                    Log.i(TAG, "签到距离小于公司设置距离" + flag3);
                    //加班与不加班所要生成的数据：员工ID，公司Id，签到状态，日期，上班时间(还有下班时间，为统计做准备)，还有工作时长
                    for (int i = 0; i < workdayDateLists.size(); i++) {
                        Log.i(TAG, "工作日：" + workdayDateLists.get(i));
                        Log.i(TAG, "签到日期：" + onDateString);
                        //条件一：签到日期在工作日日期数据库中；条件二：签到时间的 (时*60+分)<(设定时*60+设定分+弹性分钟数)；条件三：距离相距小于设定距离范围;条件四：签到时间(时*60+分)>(设定时*60+设定分-最早分钟数)
                        if (workdayDateLists.get(i).equals(onDateString)) {
                            //正常签到
                            if (flag1 && flag2 && flag3) {
                                overtimeBoolean = false;
                                signStatus = NORMALON;   //1
                                mIntent.putExtra("onTime", onTimeString);
                                mIntent.putExtra("signStatus", signStatus);
                                mIntent.putExtra("signDate", onDateString);
                                mIntent.putExtra("employeeId", employeeId);
                                mIntent.putExtra("companyId", companyId);
                                mIntent.putExtra("lateTimes",0);


                                setResult(SIGNINRESULT_CODE, mIntent);
                                Toast.makeText(getApplicationContext(), "签到成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (flag1 && !flag2 && flag3) {  //迟到
                                overtimeBoolean = false;
                                signStatus = LATE;   //3
                                mIntent.putExtra("onTime", onTimeString);
                                mIntent.putExtra("signStatus", signStatus);
                                mIntent.putExtra("signDate", onDateString);
                                mIntent.putExtra("employeeId", employeeId);
                                mIntent.putExtra("companyId", companyId);
                                //计算迟到时间，并且传入：
                                mIntent.putExtra("lateTimes",(employeeTimestamp - elasticityTimestamp));  //迟到时间


                                setResult(SIGNINRESULT_CODE, mIntent);
                                Toast.makeText(getApplicationContext(), "迟到！", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (!flag1 && flag2 && flag3) {  //签到时间大于最早签到时间
                                overtimeBoolean = false;
                                Toast.makeText(getApplicationContext(), "签到时间早于最早签到时间！", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (flag1 && flag2 && !flag3) {   //签到位置偏差太大
                                overtimeBoolean = false;
                                Toast.makeText(getApplicationContext(), "签到位置偏差太大！", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {     //多项要求不符合
                                overtimeBoolean = false;
                                Toast.makeText(getApplicationContext(), "有多项条件不符合设置要求！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }
                    //生成加班签到数据，该日期不在管理员设置的日期范围内  //相比正常工作日签到，没有迟到等情况
                    if (overtimeBoolean == true) {
                        if (flag3) {
                            signStatus = OVERTIMESIGNIN;   //8
                            mIntent.putExtra("onTime", onTimeString);
                            mIntent.putExtra("signStatus", signStatus);
                            mIntent.putExtra("signDate", onDateString);
                            mIntent.putExtra("employeeId", employeeId);
                            mIntent.putExtra("companyId", companyId);
                            mIntent.putExtra("lateTimes",0);
                            setResult(SIGNINRESULT_CODE, mIntent);
                            Toast.makeText(getApplicationContext(), "加班签到成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "签到位置不在范围内！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

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
        //签退按钮
        if (intent.getStringExtra("button_flag").equals("signOff")) {
            //通过companyID调用服务器端的servlet,查看管理员设置的信息(日期、签到时间、经纬度信息、有效范围、弹性时间
            // 、旷工及迟到时间、最早打卡时间)
            RequestParams params = new RequestParams(Utils.QUERY_SETTING_INFO);
            params.addBodyParameter("signInfo", companyId + "");  //通过POST方式
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    mCommitButton.setClickable(true);
                    //通过之前客户端发送到服务器端的数据
                    mGson = new Gson();
                    QueryCompanySettingInfoBean mCompanySettingInfoBean = mGson.fromJson(result, QueryCompanySettingInfoBean.class);
                    Double companyLatitudeDouble = mCompanySettingInfoBean.getmDoubleLatitude();
                    Double companyLongitudeDouble = mCompanySettingInfoBean.getmDoubleLongitude();

                    //计算距离
                    LatLng companyLatLng = new LatLng(companyLatitudeDouble, companyLongitudeDouble);
                    LatLng employeeLatLng = new LatLng(signOffLatitudeDouble, signOffLongitudeDouble);
                    double distance = AMapUtils.calculateLineDistance(companyLatLng, employeeLatLng);  //计算距离
                    workdayDateLists = mCompanySettingInfoBean.getWorkDateLists();
                    final int employeeTimestamp = Integer.valueOf(onTimeString.split(":")[0]) * 60 + Integer.valueOf(onTimeString.split(":")[1]);
                    //下班时间
                    final int offTimestamp = Integer.valueOf(mCompanySettingInfoBean.getOffTimeString().split(":")[0]) * 60 + Integer.valueOf(mCompanySettingInfoBean.getOffTimeString().split(":")[1]);
                    final boolean flag1 = employeeTimestamp >= offTimestamp;   //签到时间数大于下班时间数
                    boolean flag2 = distance <= mCompanySettingInfoBean.getmIntEffectiveRange();
                    //加班与不加班所要生成的数据：员工ID，公司Id，签到状态，日期，上班时间(还有下班时间，为统计做准备)，还有工作时长
                    for (int i = 0; i < workdayDateLists.size(); i++) {
                        //条件一：签到日期在工作日日期数据库中；条件二：签到时间的 (时*60+分)<(设定时*60+设定分+弹性分钟数)；条件三：距离相距小于设定距离范围;条件四：签到时间(时*60+分)>(设定时*60+设定分-最早分钟数)
                        if (workdayDateLists.get(i).equals(onDateString)) {
                            overtimeOffBoolean = false;
                            if (flag2) {
                                //获取当天的签到时间，用来计算出工作时长
                                RequestParams querySignInTimeParams = new RequestParams(Utils.QUERY_SIGN_TIME);
                                querySignInTimeParams.addBodyParameter("signDateString", onDateString);
                                querySignInTimeParams.addBodyParameter("signEmployeeId", employeeId + "");
                                querySignInTimeParams.addBodyParameter("signCompanyId", companyId + "");
                                x.http().post(querySignInTimeParams, new CommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        //获取服务器端的先前的签到数据
                                        if (!result.equals("null")) {
                                            receiverTimeStamp = 60 * Integer.valueOf(result.split(":")[0]) + Integer.valueOf(result.split(":")[1]);
                                            if (flag1) {
                                                signOffStatus = NORMALOFF;  //正常下班
                                                Toast.makeText(getApplicationContext(), "签退成功！", Toast.LENGTH_SHORT).show();
                                            } else {
                                                earlyOffTimes = offTimestamp-employeeTimestamp;
                                                signOffStatus = EARLYOFF;  //早退
                                                Toast.makeText(getApplicationContext(), "早退了哦！", Toast.LENGTH_SHORT).show();
                                            }
                                            Intent mIntent = new Intent();
                                            mIntent.putExtra("offTime", onTimeString);
                                            mIntent.putExtra("signOffStatus", signOffStatus);
                                            mIntent.putExtra("signDate", onDateString);
                                            mIntent.putExtra("employeeId", employeeId);
                                            mIntent.putExtra("companyId", companyId);
                                            mIntent.putExtra("earlyOffTimes",earlyOffTimes);
                                            mIntent.putExtra("workTimes", employeeTimestamp - receiverTimeStamp);
                                            Log.i(TAG, "SignConfirmActivity工作时间workTimes:" + (employeeTimestamp - receiverTimeStamp));
                                            Log.i(TAG, "签退时间：" + onTimeString);
                                            Log.i(TAG, "签退状态：" + signOffStatus);
                                            Log.i(TAG, "签退日期：" + onDateString);
                                            Log.i(TAG, "签退员工号：" + employeeId);
                                            Log.i(TAG, "签退公司号：" + companyId);
                                            setResult(SIGNINRESULT_CODE, mIntent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "请重新签退！", Toast.LENGTH_SHORT).show();
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
                            } else {
                                Toast.makeText(getApplicationContext(), "注意签到范围哦！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    //生成加班签退数据     //相比工作日签退没有早退等情况，随时都可以走
                    if (overtimeOffBoolean) {
                        if (flag2) {
                            //先获取之前的签到时间
                            signOffStatus = OVERTIMESIGNOFF;
                            final Intent mIntent = new Intent();
                            mIntent.putExtra("offTime", onTimeString);
                            mIntent.putExtra("signOffStatus", signOffStatus);
                            mIntent.putExtra("signDate", onDateString);
                            mIntent.putExtra("employeeId", employeeId);
                            mIntent.putExtra("companyId", companyId);
                            //获取加班签到时间
                            //获取当天的签到时间，用来计算出工作时长
                            RequestParams querySignInTimeParams = new RequestParams(Utils.QUERY_SIGN_TIME);
                            querySignInTimeParams.addBodyParameter("signDateString", onDateString);
                            querySignInTimeParams.addBodyParameter("signEmployeeId", employeeId + "");
                            querySignInTimeParams.addBodyParameter("signCompanyId", companyId + "");
                            x.http().post(querySignInTimeParams, new CommonCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    if (!result.equals("null")) {
                                        receiverTimeStamp = 60 * Integer.valueOf(result.split(":")[0]) + Integer.valueOf(result.split(":")[1]);
                                        mIntent.putExtra("workTimes", employeeTimestamp - receiverTimeStamp);
                                        Log.i(TAG,"加班签退时候计算出来的workTimes:"+(employeeTimestamp - receiverTimeStamp));
                                        setResult(SIGNINRESULT_CODE, mIntent);
                                        Toast.makeText(getApplicationContext(), "加班签退成功！", Toast.LENGTH_SHORT).show();
                                        finish();
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
                        } else {
                            Toast.makeText(getApplicationContext(), "注意签到范围！", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }

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
    }
}
