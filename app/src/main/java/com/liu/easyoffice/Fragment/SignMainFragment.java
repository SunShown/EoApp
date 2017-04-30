package com.liu.easyoffice.Fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.google.gson.Gson;
import com.liu.easyoffice.Activity.SignAddressAdjustment;
import com.liu.easyoffice.Activity.SignConfirmActivity;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.FileStore;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.pojo.SignInAndOffInfo;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2016/10/19.
 */

public class SignMainFragment extends Fragment implements LocationSource, AMapLocationListener, View.OnClickListener {

    private static final String TAG = "SignMainFragment";
    private com.amap.api.maps.AMap aMap;  //定义一个视图对象
    private UiSettings mUiSettings;   //定义一个UiSetting对象
    private LocationSource.OnLocationChangedListener mOnLocationChangedListener;
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationClientOption = null;
    private String city = null;
    private LatLonPoint mLatLonPoint = null;
    private RelativeLayout mSignInRelativeLayout;
    private View v;
    private String addressInfo;
    private TextView mCurrentTimeText;
    private TextView mCurrentBusText;
    private TextView mAddressTextview;
    private TextView mMainPageLatitudeTextview;
    private TextView mMainPageLongitudeTextview;
    private TextView mSnippetMainTextview;
    private static final int SIGNBUTTONRESULT_CODE = 2;
    private static final int SIGNBUTTOFFRESULT_CODE = 3;
    private RelativeLayout mSignOffRelativelayout;
    private SignInAndOffInfo mSignInAndOffInfo;
    private FileStore mFileStore = null;
    private Gson mGson = new Gson();
    private String signOffInfoGsonString;
    private int receiverTimeStamp;
    private String mainCurrentTime;
    private String mainCurrentBus;
    private Long employeeId;
    private Long companyId;
    private String dateString;
    private int buttonStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_sign_main_fragment, null);
        mSignInRelativeLayout = ((RelativeLayout) v.findViewById(R.id.signInRelativelayout));
        mSignOffRelativelayout = ((RelativeLayout) v.findViewById(R.id.signOffRelativelayout));
        initData();
        initView();
        initMap();
        myWidget();
        return v;
    }

//    private void initEvent() {
//        if (buttonStatus == 0) {
//            mSignInRelativeLayout.setVisibility(View.VISIBLE);
//            mSignOffRelativelayout.setVisibility(GONE);
//            mSignInRelativeLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.i(TAG, "没签到，没签退");
//                    Intent intent_sign = new Intent(getActivity().getApplicationContext(), SignConfirmActivity.class);
//                    intent_sign.putExtra("signInAddressInfo", mAddressTextview.getText().toString() + " " + mSnippetMainTextview.getText().toString());
//                    intent_sign.putExtra("signInLatitudeInfo", mMainPageLatitudeTextview.getText().toString());
//                    intent_sign.putExtra("signInLongitudeInfo", mMainPageLongitudeTextview.getText().toString());
//                    intent_sign.putExtra("button_flag", "signIn");
//                    startActivityForResult(intent_sign, SIGNBUTTONRESULT_CODE);
//                }
//            });
//        }
//        if (buttonStatus == 1) {      //已签到、已签退
//            mSignInRelativeLayout.setVisibility(View.VISIBLE);
//            mSignOffRelativelayout.setVisibility(GONE);
//            mSignInRelativeLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getActivity().getApplicationContext(), "今日已签到！", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//        if (buttonStatus == 2) {
//            mSignInRelativeLayout.setVisibility(GONE);
//            mSignOffRelativelayout.setVisibility(View.VISIBLE);
//            mSignOffRelativelayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.i(TAG, "已签到，未签退");
//                    Intent intent_sign_of = new Intent(getActivity().getApplicationContext(), SignConfirmActivity.class);
//                    intent_sign_of.putExtra("signOffAddressInfo", mAddressTextview.getText().toString() + " " + mSnippetMainTextview.getText().toString());
//                    intent_sign_of.putExtra("signOffLatitudeInfo", mMainPageLatitudeTextview.getText().toString());
//                    intent_sign_of.putExtra("signOffLongitudeInfo", mMainPageLongitudeTextview.getText().toString());
//                    intent_sign_of.putExtra("button_flag", "signOff");
//                    startActivityForResult(intent_sign_of, SIGNBUTTOFFRESULT_CODE);
//                }
//            });
//        }
//
//    }

    private void initData() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        mainCurrentTime = sdf.format(date);
        mainCurrentBus = MySharePreference.getCurrentCompany(getActivity()).getTcName();
        employeeId = MySharePreference.getCurrentUser(getActivity().getApplicationContext()).getId();
        companyId = MySharePreference.getCurrentCompany(getActivity().getApplicationContext()).getTcId();
        dateString = sdf2.format(date);

    }


    private void initView() {
        //设置当前时间
        mCurrentTimeText = ((TextView) v.findViewById(R.id.mainCurrentTimeText));
        mCurrentBusText = ((TextView) v.findViewById(R.id.currentBusTextview));
        mAddressTextview = ((TextView) v.findViewById(R.id.address_textview));
        mMainPageLatitudeTextview = ((TextView) v.findViewById(R.id.latitude_mainpage_textview));
        mMainPageLongitudeTextview = ((TextView) v.findViewById(R.id.longitude_mainpage_textview));
        mSnippetMainTextview = ((TextView) v.findViewById(R.id.snippet_main_textview));
        mCurrentTimeText.setText(mainCurrentTime);
        mCurrentBusText.setText(mainCurrentBus);
        mSignInRelativeLayout.setOnClickListener(this);
        mSignOffRelativelayout.setOnClickListener(this);


        //从网络拿一下数据，判断现在的按钮应该是签到还是签退,（根据员工Id、公司Id、当前日期）
        RequestParams querySignInAndOffInfoParams = new RequestParams(Utils.QUERY_SIGNiNANDOFFINFO);
        querySignInAndOffInfoParams.addBodyParameter("employeeId", employeeId + "");
        querySignInAndOffInfoParams.addBodyParameter("companyId", companyId + "");
        querySignInAndOffInfoParams.addBodyParameter("dateString", dateString);
        x.http().post(querySignInAndOffInfoParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //return 0;     //说明没签到，没签退
                //return 1;     // 说明今天已签到和签退
                //return 2;     // 说明已签到，但未签退
                buttonStatus = Integer.valueOf(result);
                //initEvent();
                if (buttonStatus == 0) {
                    mSignInRelativeLayout.setVisibility(View.VISIBLE);
                    mSignOffRelativelayout.setVisibility(View.GONE);
                } else if (buttonStatus == 1) {
                    mSignInRelativeLayout.setVisibility(View.VISIBLE);
                    mSignOffRelativelayout.setVisibility(View.GONE);
                } else {
                    mSignInRelativeLayout.setVisibility(View.GONE);
                    mSignOffRelativelayout.setVisibility(View.VISIBLE);
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

//        if (mSignInAndOffInfo != null) {
//            Log.i(TAG, "initView上班时间：" + mSignInAndOffInfo.getOnTimeString());
//            Log.i(TAG, "initView下班时间：" + mSignInAndOffInfo.getOffTimeString());
//            if (mSignInAndOffInfo.getOffTimeString() == null) {
//                Log.i(TAG, "显示签退按钮");
//                mSignInRelativeLayout.setVisibility(View.GONE);
//                mSignOffRelativelayout.setVisibility(View.VISIBLE);
//            } else {
//                Log.i(TAG, "显示签到按钮");
//                mSignInRelativeLayout.setVisibility(View.VISIBLE);
//                mSignOffRelativelayout.setVisibility(View.GONE);
//            }
//
//        }
    }


//    private void initEvent() {
    //签到按钮的监听事件
//        mSignInRelativeLayout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                //先进行判断和之前对象中的数据,如果当前日期的签到时间大于已经在服务器端的的当前签到时间，
//                // 则不进行签到(下边就不用执行了，来一个吐司提示已经签到)(需要参数 签到时间、签到日期、员工Id)
//
//
//                Date mDate = new Date();
//                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                String dateString = sdf2.format(mDate).split(" ")[0];
//                String timeString = sdf2.format(mDate).split(" ")[1];
//                final int signTimeStamp = 60 * Integer.valueOf(timeString.split(":")[0]) + Integer.valueOf(timeString.split(":")[1]);
//
//                Long testEmployeeId = MySharePreference.getCurrentUser(getActivity()).getId();
//                Long testCompanyId = MySharePreference.getCurrentCompany(getActivity()).getTcId();
//                RequestParams signOnTimeParams = new RequestParams(Utils.QUERY_SIGN_TIME);
//                Log.i(TAG, "日期：" + dateString);
//                Log.i(TAG, "员工Id：" + testEmployeeId);
//                signOnTimeParams.addBodyParameter("signDateString", dateString);
//                signOnTimeParams.addBodyParameter("signEmployeeId", testEmployeeId + "");
//                signOnTimeParams.addBodyParameter("signCompanyId", testCompanyId + "");
//
//                http().post(signOnTimeParams, new Callback.CommonCallback<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.i(TAG, result);
//                        if (!result.equals("null")) {
//                            receiverTimeStamp = 60 * Integer.valueOf(result.split(":")[0]) + Integer.valueOf(result.split(":")[1]);
//                            if (receiverTimeStamp < signTimeStamp) {   //签到时间戳只会大于接受的时间戳，否则result就为null
//                                Toast.makeText(getActivity().getApplicationContext(), "您今日已签到！", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Intent intent_sign = new Intent(getActivity().getApplicationContext(), SignConfirmActivity.class);
//                            intent_sign.putExtra("signInAddressInfo", mAddressTextview.getText().toString() + " " + mSnippetMainTextview.getText().toString());
//                            intent_sign.putExtra("signInLatitudeInfo", mMainPageLatitudeTextview.getText().toString());
//                            intent_sign.putExtra("signInLongitudeInfo", mMainPageLongitudeTextview.getText().toString());
//                            intent_sign.putExtra("button_flag", "signIn");
//                            Log.i(TAG, "执行跳转前：");
//                            startActivityForResult(intent_sign, SIGNBUTTONRESULT_CODE);
//                        }
//                        Log.i(TAG, result);
//                        Log.i(TAG, "查询出来的时间戳：" + receiverTimeStamp);
//                        Log.i(TAG, "签到时间戳：" + signTimeStamp);
//                    }
//
//                    @Override
//                    public void onError(Throwable ex, boolean isOnCallback) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(CancelledException cex) {
//
//                    }
//
//                    @Override
//                    public void onFinished() {
//
//                    }
//                });
//
//
//            }
//        });

    //签退按钮的监听事件
//        mSignOffRelativelayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent_sign_of = new Intent(getActivity().getApplicationContext(), SignConfirmActivity.class);
//                intent_sign_of.putExtra("signOffAddressInfo", mAddressTextview.getText().toString() + " " + mSnippetMainTextview.getText().toString());
//                intent_sign_of.putExtra("signOffLatitudeInfo", mMainPageLatitudeTextview.getText().toString());
//                intent_sign_of.putExtra("signOffLongitudeInfo", mMainPageLongitudeTextview.getText().toString());
//                intent_sign_of.putExtra("button_flag", "signOff");
//                startActivityForResult(intent_sign_of, SIGNBUTTOFFRESULT_CODE);
//
//            }
//        });
//    }

    //显示地图
    private void initMap() {
        if (aMap != null) {
            aMap.clear();
            aMap.setLocationSource(this);//设置定位监听(较重要)
            aMap.setMyLocationEnabled(true);  //可触发定位并显示定位层
        }
        //System.out.println(getChildFragmentManager().findFragmentById(R.id.map)+"==========");
        aMap = ((SupportMapFragment) (getChildFragmentManager().findFragmentById(R.id.map))).getMap();
        //对地图上的点击事件实施监听，点击后跳转到微调界面
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(com.amap.api.maps.model.LatLng latLng) {
                //Toast.makeText(getActivity().getApplicationContext(),"map监听成功",Toast.LENGTH_SHORT).show();
                Intent adjustIntent = new Intent(getActivity().getApplicationContext(), SignAddressAdjustment.class);
                startActivityForResult(adjustIntent, 1);
            }
        });
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        mUiSettings = aMap.getUiSettings();   //实例化UiSettings类

    }

    //对mapview添加一些控件
    private void myWidget() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_define_icon_64)));
        mUiSettings.setZoomControlsEnabled(false);   //设置了地图是否允许显示缩放按钮
        mUiSettings.setScaleControlsEnabled(true);  //设置地图默认的比例尺是否显示
        //mUiSettings.setCompassEnabled(true);   //设置地图默认的指南针是否显示
        mUiSettings.setMyLocationButtonEnabled(true);  //显示默认的定位按钮
        mUiSettings.setTiltGesturesEnabled(true);// 设置地图是否可以倾斜
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);//设置定位监听(较重要)
        aMap.setMyLocationEnabled(true);  //可触发定位并显示定位层
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mOnLocationChangedListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getActivity());
            mLocationClientOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClientOption.setNeedAddress(true);
            mLocationClientOption.setOnceLocation(true);
            mLocationClient.setLocationListener(this);
            //设置高精度定位模式
            mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置默认的定位间隔时间
            mLocationClientOption.setInterval(2000);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationClientOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        }

    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mOnLocationChangedListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //显示系统小蓝点  ??????可以这样用吗?在方法内部调用该方法本身
                mOnLocationChangedListener.onLocationChanged(aMapLocation);
                Log.i(TAG, aMapLocation.getProvince() + "," + aMapLocation.getCity() + ","
                        + aMapLocation.getAddress() + "," + aMapLocation.getStreet() + "," + aMapLocation.getAccuracy() + ","
                        + aMapLocation.getAoiName());
                addressInfo = aMapLocation.getAddress() + "," + aMapLocation.getStreet() + ","
                        + aMapLocation.getAoiName();
                //给地点信息控件textview设置文字
                mAddressTextview.setText(addressInfo);
                Double mLatitude = aMapLocation.getLatitude();
                Double mLongitude = aMapLocation.getLongitude();
                //给经纬度控件textview设置文字
                mMainPageLatitudeTextview.setText(mLatitude + "");   //维度
                mMainPageLongitudeTextview.setText(mLongitude + ""); //经度
                city = aMapLocation.getCity();
                mLatLonPoint = new LatLonPoint(mLatitude, mLongitude);
                //poi_search("");//开始周边搜索
            } else {
                String errText = "定位失败:" + aMapLocation.getErrorCode() + ":" + aMapLocation.getErrorInfo();
                Log.e(TAG, errText);
            }
        }
    }

    /**
     * startActivityForResult的回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String currentBusTitleString = data.getStringExtra("title");
            String currentBusSnippetString = data.getStringExtra("snippet");
            Double latitudeDouble = data.getDoubleExtra("latitude", 0);
            Double longitudeDouble = data.getDoubleExtra("longitude", 0);

            Long employeeIdLong = data.getLongExtra("employeeId", 0);  //签到签退数据
            Long companyIdLong = data.getLongExtra("companyId", 0);    //签到签退数据
            int signStatusInt = data.getIntExtra("signStatus", 0);     //签到数据
            String onTimeString = data.getStringExtra("onTime");      //签到数据

            String signDateString = data.getStringExtra("signDate");    //签到签退数据
            String offTimeString = data.getStringExtra("offTime");      //签退数据
            int signOffStatus = data.getIntExtra("signOffStatus", 0);  //签退数据状态
            int workTimes = data.getIntExtra("workTimes", 0);
            int lateTimes = data.getIntExtra("lateTimes", 0);
            int earlyOffTimes = data.getIntExtra("earlyOffTimes",0);

            switch (requestCode) {
                case 1:                         //回调微调
                    //使首页的地点信息更新至微调后的地点
                    mAddressTextview.setText(currentBusTitleString);
                    mSnippetMainTextview.setText(currentBusSnippetString);
                    mMainPageLatitudeTextview.setText(latitudeDouble + "");
                    mMainPageLongitudeTextview.setText(longitudeDouble + "");
                    break;
                case SIGNBUTTONRESULT_CODE:       //回调签到
                    //将传递过来的值封装成对象
                    mSignInAndOffInfo = new SignInAndOffInfo();
                    mSignInAndOffInfo.setDateString(signDateString);
                    mSignInAndOffInfo.setCompanyIdLong(companyIdLong);
                    mSignInAndOffInfo.setEmployeeIdLong(employeeIdLong);
                    mSignInAndOffInfo.setOnTimeString(onTimeString);
                    mSignInAndOffInfo.setSignStatusIdInt(signStatusInt);
                    mSignInAndOffInfo.setLateTimes(lateTimes);
                    //准备将签到信息传递到服务器
                    RequestParams signInParams = new RequestParams(Utils.INSERT_SIGNIN_INFO);
                    String signInfoGson = null;
                    try {
                        signInfoGson = URLEncoder.encode(mGson.toJson(mSignInAndOffInfo), "utf-8");
                        Log.i(TAG, "signInfoGson：" + signInfoGson);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    signInParams.addBodyParameter("signInfo", signInfoGson);
                    org.xutils.x.http().post(signInParams, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Log.i(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++");
                            Log.i(TAG, "签到结果吗：" + Integer.valueOf(result) + "");
                            Log.i(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++");
                            if (Integer.valueOf(result) > 0) {
                                mSignInRelativeLayout.setVisibility(GONE);
                                mSignOffRelativelayout.setVisibility(View.VISIBLE);   //已签到，但未签退
                                buttonStatus = 2;
                            }
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
                    break;
                case SIGNBUTTOFFRESULT_CODE:         //回调签退

                    mSignInAndOffInfo = new SignInAndOffInfo();
                    mSignInAndOffInfo.setWorkTimesLong(workTimes);
                    mSignInAndOffInfo.setCompanyIdLong(companyIdLong);
                    mSignInAndOffInfo.setEmployeeIdLong(employeeIdLong);
                    mSignInAndOffInfo.setOffTimeString(offTimeString);
                    mSignInAndOffInfo.setDateString(signDateString);
                    mSignInAndOffInfo.setSignOffStatusIdInt(signOffStatus);
                    mSignInAndOffInfo.setEarlyOffTimes(earlyOffTimes);

                    Log.i(TAG, "SignInAndOffInfo对象：" + mSignInAndOffInfo);
//                    mSignInRelativeLayout.setVisibility(View.VISIBLE);
//                    mSignOffRelativelayout.setVisibility(View.GONE);

                    //将签退数据插入到后台signinandoffinfo表中(要依据的是公司Id、员工Id、日期)
                    RequestParams signOffParams = new RequestParams(Utils.INSERT_SIGNOFF_INFO);
                    try {
                        signOffInfoGsonString = URLEncoder.encode(mGson.toJson(mSignInAndOffInfo), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    signOffParams.addBodyParameter("signOffInfo", signOffInfoGsonString);
                    org.xutils.x.http().post(signOffParams, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            int resultOffInt = Integer.valueOf(result);
                            Log.i(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++");
                            Log.i(TAG, "签退结果码：" + resultOffInt + "");
                            Log.i(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++");
                            if (resultOffInt > 0) {
                                mSignInRelativeLayout.setVisibility(View.VISIBLE);
                                mSignInRelativeLayout.setClickable(true);
                                mSignOffRelativelayout.setVisibility(GONE);     //已签到，已签退
                                buttonStatus = 1;
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
                    break;

            }
        }
    }

    @Override
    public void onClick(View v) {

        //return 0;     //说明没签到，没签退
        //return 1;     // 说明今天已签到和签退
        //return 2;     // 说明已签到，但未签退


        switch (v.getId()) {
            case R.id.signInRelativelayout:
                if (buttonStatus == 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "您今日已签到！", Toast.LENGTH_SHORT).show();
                } else if (buttonStatus == 0) {
                    Log.i(TAG, "没签到，没签退");
                    Intent intent_sign = new Intent(getActivity().getApplicationContext(), SignConfirmActivity.class);
                    intent_sign.putExtra("signInAddressInfo", mAddressTextview.getText().toString() + " " + mSnippetMainTextview.getText().toString());
                    intent_sign.putExtra("signInLatitudeInfo", mMainPageLatitudeTextview.getText().toString());
                    intent_sign.putExtra("signInLongitudeInfo", mMainPageLongitudeTextview.getText().toString());
                    intent_sign.putExtra("button_flag", "signIn");
                    startActivityForResult(intent_sign, SIGNBUTTONRESULT_CODE);
                }

                break;
            case R.id.signOffRelativelayout:
                Log.i(TAG, "已签到，未签退");
                Intent intent_sign_of = new Intent(getActivity().getApplicationContext(), SignConfirmActivity.class);
                intent_sign_of.putExtra("signOffAddressInfo", mAddressTextview.getText().toString() + " " + mSnippetMainTextview.getText().toString());
                intent_sign_of.putExtra("signOffLatitudeInfo", mMainPageLatitudeTextview.getText().toString());
                intent_sign_of.putExtra("signOffLongitudeInfo", mMainPageLongitudeTextview.getText().toString());
                intent_sign_of.putExtra("button_flag", "signOff");
                startActivityForResult(intent_sign_of, SIGNBUTTOFFRESULT_CODE);

                break;

        }
    }
}
