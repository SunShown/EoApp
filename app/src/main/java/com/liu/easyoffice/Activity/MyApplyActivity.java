package com.liu.easyoffice.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.liu.easyoffice.MyView.MySearchBar;
import com.liu.easyoffice.Progressbar.LoadDialog;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.CommonAdapter;
import com.liu.easyoffice.Utils.DateUtil;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.Utils.ViewHolder;
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






/**
 * Created by hui on 2016/9/19.
 */
public class MyApplyActivity extends Activity implements View.OnClickListener {
    PullToRefreshListView myApplyLv;
    CommonAdapter<AllShenPiBean> allshenpiiadapter;
    private MySearchBar sb_ishen;
    private int[] icons={R.mipmap.icon_approve_qingjia,R.mipmap.icon_approve_trip,R.mipmap.icon_approve_baoxiao,R.mipmap.icon_approve_buqian};
    String searchinfo;
    private TextView tv_shenpii_back;
    private List<AllShenPiBean> data=new ArrayList<AllShenPiBean>();
    private int page = 1;  //当前页码
      private int size = 10; //每页显示10个
       private int count = 0; //当前页面有多少个
    Map<Integer,Map<String,String>> states;
    private RelativeLayout rl_fail_rs_apply;
    private ImageView iv_app_fail;
    private TextView tv_app_fail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_myapply_main);
        initView();
        initData();
        initEvent();



    }


    private void initView() {
        myApplyLv = ((PullToRefreshListView) findViewById(R.id.apply_myapply_lv));
        sb_ishen = ((MySearchBar) findViewById(R.id.sb_ishen));
        tv_shenpii_back = ((TextView) findViewById(R.id.tv_shenpii_back));
        myApplyLv.setMode(PullToRefreshBase.Mode.BOTH);
        myApplyLv.setScrollingWhileRefreshingEnabled(true);

        iv_app_fail = ((ImageView) findViewById(R.id.iv_app_fail));
        tv_app_fail = ((TextView) findViewById(R.id.tv_app_fail));

        myApplyLv.setVisibility(View.VISIBLE);


    }
    private  void initData(){
        LoadDialog.show(this,"努力加载中");
        sb_ishen.setQuerytext("搜索标题");sb_ishen.setActivity(this);
        new Handler (new Handler.Callback(){
                       @Override
                        public boolean handleMessage(Message msg) {
                         getData(true);
                               return true;
                           }
                   }).sendEmptyMessageDelayed(0,200);



    }
    private  void initEvent(){
        tv_shenpii_back.setOnClickListener(this);
        sb_ishen.settv_searchOnClickListener(this);
        myApplyLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                searchinfo=sb_ishen.getqueryText();
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        myApplyLv.onRefreshComplete();




                    }
                }, 5000);
                getData(refreshView.getScrollY() < 0);

            }
        });




    }

    public void getData(final boolean direction){

        if (direction) {  //如果是上拉，那应该将page变为第一页
                    page = 1;

                  } else {
                      page++; //如果是下拉，就让page加1

                 }

        Log.i("ISHENPINEW","================update========");
        RequestParams params = new RequestParams(Utils.XUTIL_URL+"getallshenpii");
        params.addQueryStringParameter("userid", MySharePreference.getCurrentUser(MyApplyActivity.this).getId()+"");
        params.addQueryStringParameter("searchinfo",searchinfo);
        params.addQueryStringParameter("page", String.valueOf(page));
        params.addQueryStringParameter("size", size+"");
        params.setConnectTimeout(4*1000);
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
                System.out.println("data.size()"+data.size());
                if(data.size()>0){
                    myApplyLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent Intent=new  Intent(getApplicationContext(), ApproveinfoActivity.class);
                            Intent.putExtra("approveId",data.get(position-1).approveId);
                            Intent.putExtra("backflag",0);
                            startActivityForResult(Intent,102);

                        }
                    });}else{
                    myApplyLv.setOnItemClickListener(null);
                }
                if (data.size()<1&&lists.size()<1){

                    String rsinfo="";
                    int imgRes=0;
                    if (searchinfo==null||"".equals(searchinfo.trim())){
                        imgRes=R.mipmap.fail;
                        rsinfo="没有我发起的审批";


                    }else{

                        imgRes=R.mipmap.search_icon;
                        rsinfo ="没有搜索到“"+searchinfo+"”相关结果";


                    }

                    Map<String ,Object> objectMap=new HashMap<String, Object>();
                    objectMap.put("iv_app_fail",imgRes);
                    objectMap.put("tv_app_fail",rsinfo);
                    List<Map<String ,Object>> fail=new ArrayList<Map<String, Object>>();
                    fail.add(objectMap);
                    myApplyLv.setAdapter(new SimpleAdapter(MyApplyActivity.this,fail,R.layout.fail_load_head,new String[]{"iv_app_fail","tv_app_fail"},new int[]{R.id.iv_app_fail,R.id.tv_app_fail}));
                    LoadDialog.dismiss(MyApplyActivity.this);


                }else{
                if(lists==null||lists.size()<=0){
                    myApplyLv.onRefreshComplete();
                    myApplyLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

                    Log.e("NewI",lists.size()+"");
                    LoadDialog.dismiss(getApplicationContext());

                    return;
                }else { myApplyLv.setMode(PullToRefreshBase.Mode.BOTH);}
                Log.e("ISHENPINEW", lists.size()+"====lists.size()lists.size()================================");



                Log.e("MyI",lists.size()+"");
                System.out.println("count=================="+count);
//                int z=0;
//                Map<String ,String> k;
//                for (AllShenPiBean   bean:data){
//                    k=new HashMap<String,String>();
//                  k.put("state",bean.state);
//                    k.put("decison",bean.decison);
//                    states.put(z,k);
//                    z++;
//                }
                System.out.println(data.size()+"data.size()"+result);




                if( allshenpiiadapter==null){

                    allshenpiiadapter=new CommonAdapter<AllShenPiBean>(data,getApplicationContext(),R.layout.ishenpi_item) {

                        @Override
                        public void setcontant(ViewHolder viewHolder, AllShenPiBean allShenPiBean, int position) {
                            // Log.i("ISHENPINEW","111111111111111111111111111");

                            ImageView lv_ishenpi_photo = ((ImageView) viewHolder.getViewById(R.id.lv_ishenpi_photo));
                            TextView tv_ishenpi_title=((TextView) viewHolder.getViewById(R.id.tv_ishenpi_title));
                            TextView tv_ishenpi_is= ((TextView) viewHolder.getViewById(R.id.tv_ishenpi_is));
                            TextView tv_ishenpi_time=((TextView) viewHolder.getViewById(R.id.tv_ishenpi_time));
                            //xUtilsImageUtils.display(lv_ishenpi_photo, allShenPiBean.sender.getImgUrl(),true);

                            lv_ishenpi_photo.setImageResource(icons[allShenPiBean.type-1]);
                            System.out.println("position==============="+position);
                            tv_ishenpi_title.setText(allShenPiBean.approveTitle);
//                            String state= states.get(position).get("state");
//                            System.out.println(state+"state=================");
//                            String decison= states.get(position).get("decison");
//                            System.out.println(decison+"decison=================");
//
//
                            String state= allShenPiBean.state;
                         System.out.println(state+"state=================");
                           String decison=allShenPiBean.decison;
                            if("审批中".equals(state)){
                           tv_ishenpi_is.setText("等待"+allShenPiBean.sender.getUserName()+"审批");
                                tv_ishenpi_is.setTextColor(Color.parseColor("#ff8227"));

                            }else {
                                tv_ishenpi_is.setTextColor(Color.parseColor("#cccccc"));
                                if("审批撤销".equals(state)){
                                    tv_ishenpi_is.setText("已撤回");
                                }else if("审批完成".equals(state)){
                                    tv_ishenpi_is.setText(state+"("+decison+")");

                                }

                            }


                            tv_ishenpi_time.setText(DateUtil.stringToDate(allShenPiBean.newDate,new Date()));
                            Log.i("ISHENPINEW",DateUtil.dateToString(allShenPiBean.newDate));







                        }
                    };
                    myApplyLv.setAdapter(allshenpiiadapter);
                    LoadDialog.dismiss(getApplicationContext());


                }else {
                    myApplyLv.setAdapter(allshenpiiadapter);
                   // allshenpiiadapter.notifyDataSetChanged();
                    myApplyLv.onRefreshComplete();
                    LoadDialog.dismiss(getApplicationContext());
                }}






            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println(ex);
                String rsinfo="网络错误";
                int imgRes=R.mipmap.fail;
                myApplyLv.setOnItemClickListener(null);

                Map<String ,Object> objectMap=new HashMap<String, Object>();
                objectMap.put("iv_app_fail",imgRes);
                objectMap.put("tv_app_fail",rsinfo);
                List<Map<String ,Object>> fail=new ArrayList<Map<String, Object>>();
                fail.add(objectMap);
                myApplyLv.setAdapter(new SimpleAdapter(MyApplyActivity.this,fail,R.layout.fail_load_head,new String[]{"iv_app_fail","tv_app_fail"},new int[]{R.id.iv_app_fail,R.id.tv_app_fail}));
                myApplyLv.onRefreshComplete();
                LoadDialog.dismiss(getApplicationContext());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                myApplyLv.onRefreshComplete();
                LoadDialog.dismiss(getApplicationContext());

            }

            @Override
            public void onFinished() {
                myApplyLv.onRefreshComplete();
                LoadDialog.dismiss(getApplicationContext());

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_shenpii_back:
                Intent intent=new Intent(MyApplyActivity.this,ApplyAcitity.class);
                startActivity(intent);
                break;
            case  R.id.tv_search:

                searchinfo=sb_ishen.getqueryText();
                data.clear();
                getData(true);
                break;

        }


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(MyApplyActivity.this,ApplyAcitity.class);
        startActivity(intent);

    }
}
