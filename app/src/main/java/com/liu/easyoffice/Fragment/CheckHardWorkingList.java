package com.liu.easyoffice.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liu.easyoffice.Adapter.CommonAdapter;
import com.liu.easyoffice.Adapter.ViewHolder;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.pojo.HardworkingInfo;

import org.feezu.liuli.timeselector.TimeSelector;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by Administrator on 2016/10/22.
 */

public class CheckHardWorkingList extends Fragment {
    private static final String TAG = "CheckHardWorkingList";
    private View v;
    private ImageView mHardworkingImage;
    private TextView mHardworkingTextview;
    private ListView mHardworkingListview;
    private TextView mDateTextView;
    private String dateString;
    private String afterConvertDateString;
    private Long companyId;
    private int hardworkingTimes;
    private xUtilsImageUtils xUtilsImage = new xUtilsImageUtils();
    List<HardworkingInfo> gsonHardworkingInfoList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_sign_hardworkinglist, null);
        initView();
        initEvent();   //要先执行
        initData();
        Log.i(TAG, "测试CheckHardWorkingList:");
        return v;
    }


    private void initView() {
        mHardworkingImage = ((ImageView) v.findViewById(R.id.hardworking_image));
        mHardworkingTextview = ((TextView) v.findViewById(R.id.hardworking_textview));
        mHardworkingListview = ((ListView) v.findViewById(R.id.hardworkinglist_listview));
        mDateTextView = (TextView) getActivity().findViewById(R.id.rankingList_select_textview);
    }

    private void initEvent() {
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimeSelector(getActivity(), new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        String[] timeString = time.split("-");
                        mDateTextView.setText(timeString[0] + "年" + timeString[1] + "月" + timeString[2] + "日");
                        initData();
                        Log.i(TAG, "hardworking第一次执行！");
                    }
                }, "1970-01-01", "2100-12-31").show_me(Integer.valueOf(mDateTextView.getText().toString().substring(0, 4)), Integer.valueOf(mDateTextView.getText().toString().substring(5, 7)), Integer.valueOf(mDateTextView.getText().toString().substring(8, 10)));
            }
        });
        Log.i(TAG, "hardworking第二次执行！");
    }

    private void initData() {

        //一.根据companyId和日期所选 以公司员工的工作时长要大于上榜时长，并且降序排列
        //1.首先获取activity的日期
        dateString = mDateTextView.getText().toString();
        afterConvertDateString = dateString.substring(0, 4) + "-" + dateString.substring(5, 7) + "-" + dateString.substring(8, 10);
        companyId = MySharePreference.getCurrentCompany(getActivity()).getTcId();
        //2.首先在daoimpl层获取该公司设置的上榜时长；
        RequestParams queryHardworkingTimeParams = new RequestParams(Utils.QUERY_HARDWORKINGTIME);
        queryHardworkingTimeParams.addBodyParameter("companyId", companyId + "");
        x.http().post(queryHardworkingTimeParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                hardworkingTimes = 60 * Integer.valueOf(result);
                Log.i(TAG, "第一个success：" + hardworkingTimes + "");

                // 获取员工信时长也(姓名、所属部门、头像、工作时长)
                RequestParams checkingParams = new RequestParams(Utils.QUERY_HARDWORKINGCHECKING);
                checkingParams.addBodyParameter("finalDateString", afterConvertDateString);
                checkingParams.addBodyParameter("companyId", companyId + "");
                checkingParams.addBodyParameter("checkingTime", hardworkingTimes + "");
                Log.i(TAG, "第二个success：" + hardworkingTimes + "");
                x.http().post(checkingParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.i(TAG, "CheckHardWorkingList的result的值：" + result);
                        Gson mGson = new Gson();
                        String resultUrlInfo;

                        try {
                            resultUrlInfo = URLDecoder.decode(result, "utf-8");
                            Type type = new TypeToken<List<HardworkingInfo>>() {
                            }.getType();
                            gsonHardworkingInfoList = mGson.fromJson(resultUrlInfo, type);
                            Log.i(TAG, "gsonHardworkingInfoList信息：" + gsonHardworkingInfoList);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if (gsonHardworkingInfoList.size() > 0) {
                            //让该显示的显示出来
                            mHardworkingImage.setVisibility(View.GONE);
                            mHardworkingTextview.setVisibility(View.GONE);
                            mHardworkingListview.setVisibility(View.VISIBLE);
                            //填充ListView
                            HardworkingTestAdapter hardworkingTestAdapter = new HardworkingTestAdapter(getActivity().getApplicationContext(), gsonHardworkingInfoList, R.layout.activity_sign_hardworking_items);
                            hardworkingTestAdapter.notifyDataSetChanged();
                            mHardworkingListview.setAdapter(hardworkingTestAdapter);
                        } else {
                            Log.i(TAG, "CheckHardWorkingList中的引导页显示了，ListView隐藏了！");
                            mHardworkingImage.setVisibility(View.VISIBLE);
                            mHardworkingTextview.setVisibility(View.VISIBLE);
                            mHardworkingListview.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.i(TAG, "checkHardworkingList的链接错误2:" + ex);
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
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i(TAG, "checkHardworkingList的链接错误1:" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        //3.然后通过QueryWorkTimes的servlet联合获取list<User>传递过来
        //4.然后通过Map记录排名，防止乱序
    }

    private class HardworkingTestAdapter extends CommonAdapter<HardworkingInfo> {
        public HardworkingTestAdapter(Context context, List<HardworkingInfo> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, HardworkingInfo hardworkingInfo, int posion) {
            TextView nameTextview = viewHolder.getView(R.id.nameTextview);
            TextView departmentTextview = viewHolder.getView(R.id.department);
            TextView workTimeTextview = viewHolder.getView(R.id.workTimeTextview);
            ImageView portraitImage = viewHolder.getView(R.id.portraitImage);
            nameTextview.setText(hardworkingInfo.getUserName());
            departmentTextview.setText(hardworkingInfo.getDepartmentName());
            workTimeTextview.setText(hardworkingInfo.getWorkTimes() + "");
            xUtilsImage.display(portraitImage, hardworkingInfo.getImgUrl(), true);
        }
    }


}
