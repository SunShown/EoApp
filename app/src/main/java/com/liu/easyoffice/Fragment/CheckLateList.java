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
import com.liu.easyoffice.pojo.LateInfo;

import org.feezu.liuli.timeselector.TimeSelector;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/22.
 */

public class CheckLateList extends Fragment {
    private View v;
    private static final String TAG = "CheckLateList";
    private ImageView mLateImageview;
    private TextView mLateTextview;
    private ListView mLateListview;
    private TextView mDateTextView;
    private String dateString;
    private String afterConvertDateString;
    private Long companyId;
    private xUtilsImageUtils xUtilsImage = new xUtilsImageUtils();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_sign_latelist, null);
        initView();
        initEvent();
        initData();
        return v;
    }


    private void initView() {
        mLateImageview = ((ImageView) v.findViewById(R.id.hardworking_image));
        mLateTextview = ((TextView) v.findViewById(R.id.hardworking_textview));
        mLateListview = ((ListView) v.findViewById(R.id.hardworkinglist_listview));
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
                        Log.i(TAG, "late第一次执行！");
                    }
                }, "1970-01-01", "2100-12-31").show();
            }
        });
        Log.i(TAG, "late第二次执行！");
    }

    private void initData() {

        //一.根据companyId和日期所选 以公司员工的工作时长要大于上榜时长，并且降序排列
        //1.首先获取activity的日期
        dateString = mDateTextView.getText().toString();
        afterConvertDateString = dateString.substring(0, 4) + "-" + dateString.substring(5, 7) + "-" + dateString.substring(8, 10);
        companyId = MySharePreference.getCurrentCompany(getActivity()).getTcId();
        //2.首先在daoimpl层获取该公司设置的；(或者直接该更改之前的签到数据库，在signinandoffinfo数据库中添加（迟到时间、早退时间)字段)
        RequestParams queryHardworkingTimeParams = new RequestParams(Utils.QUERY_LATETIME);
        queryHardworkingTimeParams.addBodyParameter("companyId", companyId + "");
        queryHardworkingTimeParams.addBodyParameter("dateString", afterConvertDateString);
        x.http().post(queryHardworkingTimeParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "CheckLateList的result的值：" + result);

                    Gson mGson = new Gson();
                    String urlGsonString;
                    List<LateInfo> lateInfoList = new ArrayList<>();
                    Type type = new TypeToken<List<LateInfo>>() {
                    }.getType();
                    try {
                        urlGsonString = URLDecoder.decode(result, "utf-8");
                        lateInfoList = mGson.fromJson(urlGsonString, type);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (lateInfoList.size() > 0) {
                        mLateImageview.setVisibility(View.GONE);
                        mLateTextview.setVisibility(View.GONE);
                        mLateListview.setVisibility(View.VISIBLE);

                        Log.i(TAG, "客户端lateInfoList信息：" + lateInfoList);
                        LateAdapter lateAdapter = new LateAdapter(getActivity().getApplicationContext(), lateInfoList, R.layout.activity_sign_hardworking_items);
                        lateAdapter.notifyDataSetChanged();
                        mLateListview.setAdapter(lateAdapter);
                    }
                    else {
                        Log.i(TAG, "CheckLateList中的引导页显示了，ListView隐藏了！");
                        mLateImageview.setVisibility(View.VISIBLE);
                        mLateTextview.setVisibility(View.VISIBLE);
                        mLateListview.setVisibility(View.GONE);
                    }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i(TAG, "checkLateList的链接错误1:" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private class LateAdapter extends CommonAdapter<LateInfo> {
        public LateAdapter(Context context, List<LateInfo> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, LateInfo lateInfo, int posion) {
            TextView nameTextview = viewHolder.getView(R.id.nameTextview);
            TextView departmentTextview = viewHolder.getView(R.id.department);
            TextView workTimeTextview = viewHolder.getView(R.id.workTimeTextview);
            ImageView portraitImage = viewHolder.getView(R.id.portraitImage);
            nameTextview.setText(lateInfo.getUserName());
            departmentTextview.setText(lateInfo.getDepartmentName());
            workTimeTextview.setText(lateInfo.getLateTimes() + "");
            xUtilsImage.display(portraitImage, lateInfo.getImgUrl(), true);
        }
    }


}
