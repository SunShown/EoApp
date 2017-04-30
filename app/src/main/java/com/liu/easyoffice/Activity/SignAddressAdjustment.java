package com.liu.easyoffice.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.liu.easyoffice.Adapter.CommonAdapter;
import com.liu.easyoffice.Adapter.ViewHolder;
import com.liu.easyoffice.R;

import java.util.List;

public class SignAddressAdjustment extends AppCompatActivity implements LocationSource, AMapLocationListener, PoiSearch.OnPoiSearchListener {

    private static final String TAG = "ShowMapActivity";
    private com.amap.api.maps.MapView mapView;
    private com.amap.api.maps.AMap aMap;  //定义一个视图对象
    private UiSettings mUiSettings;   //定义一个UiSetting对象
    private LocationSource.OnLocationChangedListener mOnLocationChangedListener;
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationClientOption = null;
    private String city = null;
    private LatLonPoint mLatLonPoint = null;
    private ListView mPoiItemListView;
    private Toolbar mToolbar;
    private TextView mExportDataTextview;
    private List<PoiItem> poiItems = null;
    private TextView mFirstParaTextview;
    private TextView mSecondParaTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_showmap_and_listview);
        mapView = (com.amap.api.maps.MapView) findViewById(R.id.map);
        mPoiItemListView = ((ListView) findViewById(R.id.poisearch_listview));
        mFirstParaTextview = ((TextView) findViewById(R.id.firstPara));
        mSecondParaTextview = ((TextView) findViewById(R.id.secondPara));
        mapView.onCreate(savedInstanceState);//必须要写
        initToolbar();
        initMap();
        initEvent();
    }

    private void initEvent() {
        mPoiItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiItem poiItemInfo = poiItems.get(position);
                if (poiItemInfo != null) {
                    Intent signAdjustAddressIntent = new Intent();
                    signAdjustAddressIntent.putExtra("title", poiItemInfo.getTitle());
                    signAdjustAddressIntent.putExtra("snippet", poiItemInfo.getSnippet());
                    // 设置结果，并进行传送，即使当前边两个没有Activity没有用到resultCode，也要写。但是可以
                    //不用写&&resultCode==3,,,,当前两个Activity要用到resultCode要用到，则要用到该方法
                    signAdjustAddressIntent.putExtra("latitude", poiItemInfo.getLatLonPoint().getLatitude());  //维度
                    signAdjustAddressIntent.putExtra("longitude", poiItemInfo.getLatLonPoint().getLongitude());  //经度
                    setResult(3, signAdjustAddressIntent);
                    finish();
                }
            }
        });
    }

    private void initToolbar() {
        mToolbar = ((Toolbar) findViewById(R.id.toolbar));
        mToolbar.setTitle("地点微调");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mExportDataTextview = ((TextView) mToolbar.findViewById(R.id.exportData));
        mExportDataTextview.setVisibility(View.GONE);
    }

    private void initMap() {
        if (aMap != null) {
            aMap.clear();
        }
        aMap = mapView.getMap();
        aMap.moveCamera(CameraUpdateFactory.zoomTo(20));
        mUiSettings = aMap.getUiSettings();   //实例化UiSettings类
        myWidget();

    }

    //对mapview添加一些控件
    private void myWidget() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_define_icon_64)));
        mUiSettings.setZoomControlsEnabled(false);   //设置了地图是否允许显示缩放按钮
        mUiSettings.setScaleControlsEnabled(false);  //设置地图默认的比例尺是否显示
        mUiSettings.setCompassEnabled(false);   //设置地图默认的指南针是否显示
        mUiSettings.setMyLocationButtonEnabled(true);  //显示默认的定位按钮
        mUiSettings.setTiltGesturesEnabled(true);// 设置地图是否可以倾斜
        aMap.setMyLocationStyle(myLocationStyle);
        //设置定位监听(较重要)
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);  //可触发定位并显示定位层
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    /**
     * 开始定位
     *
     * @param onLocationChangedListener
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mOnLocationChangedListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
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
     * 结束定位
     */
    @Override
    public void deactivate() {

    }

    /**
     * 定位成功后调用该方法
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mOnLocationChangedListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //显示系统小蓝点  ??????可以这样用吗?在方法内部调用该方法本身
                mOnLocationChangedListener.onLocationChanged(aMapLocation);
                Log.i(TAG, aMapLocation.getProvince() + "," + aMapLocation.getCity() + ","
                        + aMapLocation.getAddress() + "," + aMapLocation.getStreet() + "," + aMapLocation.getAccuracy() + ","
                        + aMapLocation.getAoiName());
                Double mLatitude = aMapLocation.getLatitude();
                Double mLongitude = aMapLocation.getLongitude();
                //aMapLocation.distanceTo();  不好用
                LatLng startLatLng = new LatLng(mLatitude, mLongitude);
                LatLng endLatLng = new LatLng(mLatitude + 2, mLongitude + 2);
                double distance = AMapUtils.calculateLineDistance(startLatLng, endLatLng);  //计算距离
                Log.i(TAG, distance + " ");
                city = aMapLocation.getCity();
                mLatLonPoint = new LatLonPoint(mLatitude, mLongitude);
                poi_search("");//开始周边搜索
            } else {
                String errText = "定位失败:" + aMapLocation.getErrorCode() + ":" + aMapLocation.getErrorInfo();
                Log.e(TAG, errText);
            }
        }
    }

    private void poi_search(String str) {
        //第一个参数表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，
        //POI搜索类型共分为以下20种：汽车服务|汽车销售|
        //汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
        //住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
        //金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
        //第三个参数表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        PoiSearch.Query mPoiSearchQuery = new PoiSearch.Query(str, "", city);
        mPoiSearchQuery.requireSubPois(true);
        mPoiSearchQuery.setPageSize(50);  // 设置每页最多返回多少条poiitem
        mPoiSearchQuery.setPageNum(0);  //设置查询页码
        PoiSearch poiSearch = new PoiSearch(getApplicationContext(), mPoiSearchQuery);
        poiSearch.setBound(new PoiSearch.SearchBound(mLatLonPoint, 300, true));
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        poiItems = poiResult.getPois();
        mPoiItemListView.setAdapter(new GaodeTestAdapter(getApplicationContext(), poiItems, R.layout.activity_sign_poisearch_items));

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    private class GaodeTestAdapter extends CommonAdapter<PoiItem> {


        public GaodeTestAdapter(Context context, List<PoiItem> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, PoiItem poiItem, int posion) {
            TextView firstTextview = viewHolder.getView(R.id.firstPara);
            TextView secondTextview = viewHolder.getView(R.id.secondPara);
//            TextView latitudeTextview = viewHolder.getView(R.id.latitudeTextview);
//            TextView longtitudeTextview = viewHolder.getView(R.id.longitudeTextview);
            firstTextview.setText(poiItem.getTitle());
            secondTextview.setText(poiItem.getSnippet());
//            latitudeTextview.setText(poiItem.getLatLonPoint().getLatitude()+"");
//            longtitudeTextview.setText(poiItem.getLatLonPoint().getLongitude()+"");
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


}
