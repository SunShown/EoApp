package com.liu.easyoffice.Fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.liu.easyoffice.Activity.ApproveinfoActivity;
import com.liu.easyoffice.Activity.MyApplyActivity;
import com.liu.easyoffice.MyView.MySearchBar;
import com.liu.easyoffice.Progressbar.LoadDialog;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.CommonAdapter;
import com.liu.easyoffice.Utils.DateUtil;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.Utils.ViewHolder;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.pojo.AllShenPiBean;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OldIShenpiFragment extends Fragment {

    CommonAdapter<AllShenPiBean> allshenpiadapter;
    private List<AllShenPiBean> data=new ArrayList<AllShenPiBean>();
    private int page = 1;  //当前页码
    private int size = 10; //每页显示10个
    private int count = 0; //当前页面有多少个
    String searchinfo;
    private PullToRefreshListView lv_shenpiold;
    private MySearchBar sb_sheniold;


    public OldIShenpiFragment() {
        // Required empty public constructor
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initEvent();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ishenpi_old,null);
        lv_shenpiold = ((PullToRefreshListView) view.findViewById(R.id.lv_shenpiold));
        sb_sheniold = ((MySearchBar) view.findViewById(R.id.sb_sheniold));
        lv_shenpiold .setMode(PullToRefreshBase.Mode.BOTH);
        lv_shenpiold .setScrollingWhileRefreshingEnabled(true);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//
//    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void initData(){
        LoadDialog.show(getActivity(),"努力加载中");

        sb_sheniold.setActivity(getActivity());
        sb_sheniold.setQuerytext("搜索标题");

        new Handler(new Handler.Callback(){
            @Override
            public boolean handleMessage(Message msg) {
                getData(true);
                return true;
            }
        }).sendEmptyMessageDelayed(0,200);

    }
    public void getData( final boolean direction){


        if (direction) {  //如果是上拉，那应该将page变为第一页
            page = 1;

        } else {
            page++; //如果是下拉，就让page加1

        }
        Log.i("ISHENPINEW","================update========");
        RequestParams params = new RequestParams(Utils.XUTIL_URL+"getallihaveshenpi");
        params.addQueryStringParameter("userid", MySharePreference.getCurrentUser(getActivity().getApplicationContext()).getId()+"");
        params.addQueryStringParameter("searchinfo",searchinfo);
        params.addQueryStringParameter("page", String.valueOf(page));
        params.setConnectTimeout(4*1000);
        params.addQueryStringParameter("size", size+"");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("ISHENPINEW",result);
                Gson gson = new Gson();
                Type type=new TypeToken<List<AllShenPiBean>>(){}.getType();

                List<AllShenPiBean> lists=new ArrayList<AllShenPiBean>();
                lists=gson.fromJson(result,type);
                if (direction) { //下拉刷新
                    if(data!=null){
                        data.clear();
                        Log.i("ISHENPINEW","================clear========");
                    }
                    data.addAll(lists);



                } else {//尾部加载更多
                    data.addAll(lists);

                }
                if(data.size()>0){
                    lv_shenpiold.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent Intent=new  Intent(getActivity(), ApproveinfoActivity.class);
                            Intent.putExtra("approveId",data.get(position-1).approveId);
                            Intent.putExtra("backflag",2);
                            startActivityForResult(Intent,101);

                        }
                    });}else{
                    lv_shenpiold.setOnItemClickListener(null);
                }
                Log.e("OldI",lists.size()+"");
                if (data.size()<1&&lists.size()<1){

                    String rsinfo="";
                    int imgRes=0;
                    if (searchinfo==null||"".equals(searchinfo.trim())){
                        imgRes=R.mipmap.fail;
                        rsinfo="没有我已审批申请";


                    }else{

                        imgRes=R.mipmap.search_icon;
                        rsinfo ="没有搜索到“"+searchinfo+"”相关结果";


                    }

                    Map<String ,Object> objectMap=new HashMap<String, Object>();
                    objectMap.put("iv_app_fail",imgRes);
                    objectMap.put("tv_app_fail",rsinfo);
                    List<Map<String ,Object>> fail=new ArrayList<Map<String, Object>>();
                    fail.add(objectMap);
                    lv_shenpiold.setAdapter(new SimpleAdapter(getActivity(),fail,R.layout.fail_load_head,new String[]{"iv_app_fail","tv_app_fail"},new int[]{R.id.iv_app_fail,R.id.tv_app_fail}));
                    LoadDialog.dismiss(getActivity());


                }else {
                    if (lists == null || lists.size() <= 0) {
                        lv_shenpiold.onRefreshComplete();
                        lv_shenpiold.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

                        Log.e("NewI", lists.size() + "");
                        LoadDialog.dismiss(getActivity());
                        return;
                    } else {
                        lv_shenpiold.setMode(PullToRefreshBase.Mode.BOTH);
                    }


                    if (allshenpiadapter == null) {

                        allshenpiadapter = new CommonAdapter<AllShenPiBean>(data, getActivity(), R.layout.ishenpi_item) {

                            @Override
                            public void setcontant(ViewHolder viewHolder, AllShenPiBean allShenPiBean, int position) {
                                // Log.i("ISHENPINEW","111111111111111111111111111");
                                ImageView lv_ishenpi_photo = ((ImageView) viewHolder.getViewById(R.id.lv_ishenpi_photo));
                                TextView tv_ishenpi_title = ((TextView) viewHolder.getViewById(R.id.tv_ishenpi_title));
                                TextView tv_ishenpi_is = ((TextView) viewHolder.getViewById(R.id.tv_ishenpi_is));
                                TextView tv_ishenpi_time = ((TextView) viewHolder.getViewById(R.id.tv_ishenpi_time));
                                xUtilsImageUtils.display(lv_ishenpi_photo, allShenPiBean.sender.getImgUrl(), true);


                                tv_ishenpi_title.setText(allShenPiBean.approveTitle);


                                tv_ishenpi_is.setText(allShenPiBean.state + "(" + allShenPiBean.decison + ")");
                                tv_ishenpi_is.setTextColor(Color.parseColor("#cccccc"));

                                tv_ishenpi_time.setText(DateUtil.stringToDate(allShenPiBean.newDate, new Date()));
                                Log.i("ISHENPINEW", DateUtil.dateToString(allShenPiBean.newDate));


                            }
                        };
                        lv_shenpiold.setAdapter(allshenpiadapter);
                        LoadDialog.dismiss(getActivity());


                    } else {
                        lv_shenpiold.setAdapter(allshenpiadapter);
                        allshenpiadapter.notifyDataSetChanged();
                        lv_shenpiold.onRefreshComplete();
                        LoadDialog.dismiss(getActivity());
                    }
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println(ex);
                String rsinfo="网络错误";
                int imgRes=R.mipmap.fail;
                lv_shenpiold.setOnItemClickListener(null);

                Map<String ,Object> objectMap=new HashMap<String, Object>();
                objectMap.put("iv_app_fail",imgRes);
                objectMap.put("tv_app_fail",rsinfo);
                List<Map<String ,Object>> fail=new ArrayList<Map<String, Object>>();
                fail.add(objectMap);
                lv_shenpiold.setAdapter(new SimpleAdapter(getActivity(),fail,R.layout.fail_load_head,new String[]{"iv_app_fail","tv_app_fail"},new int[]{R.id.iv_app_fail,R.id.tv_app_fail}));
                lv_shenpiold.onRefreshComplete();
                LoadDialog.dismiss(getActivity());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                lv_shenpiold.onRefreshComplete();
                LoadDialog.dismiss(getActivity());
            }

            @Override
            public void onFinished() {
                lv_shenpiold.onRefreshComplete();
                LoadDialog.dismiss(getActivity());
            }
        });



    }
    public  void initEvent(){
        sb_sheniold.settv_searchOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchinfo=sb_sheniold.getqueryText();
                LoadDialog.show(getActivity(),"努力加载中");

                getData(true);

            }
        });
        lv_shenpiold.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                searchinfo=sb_sheniold.getqueryText();
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        lv_shenpiold.onRefreshComplete();




                    }
                }, 5000);
                getData(refreshView.getScrollY() < 0);
            }
        });




    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        allshenpiadapter=null;

        getData(true);
    }
}
