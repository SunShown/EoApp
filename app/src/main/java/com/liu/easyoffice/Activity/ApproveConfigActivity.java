package com.liu.easyoffice.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liu.easyoffice.Adapter.ViewHolder;
import com.liu.easyoffice.MyView.NoScrollGridView;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.CommonAdapter;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.pojo.ApproveRule;
import com.liu.easyoffice.pojo.ApproveType;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

public class ApproveConfigActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView appconfig_back;
    private ExpandableListView ex_appcon;
    private TextView tv_appcon_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_config);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        appconfig_back = ((TextView) findViewById(R.id.appconfig_back));
        ex_appcon = ((ExpandableListView) findViewById(R.id.ex_appcon));
        tv_appcon_save = ((TextView) findViewById(R.id.tv_appcon_save));
    }

    private void initData() {
        getData();
    }

    private void getData() {
        RequestParams params=new RequestParams(Utils.XUTIL_URL+"getrules");
        params.addParameter("companyId", MySharePreference.getCurrentCompany(ApproveConfigActivity.this).getTcId());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                Type type=new TypeToken<List<ApproveType>>(){}.getType();
                List<ApproveType> types=gson.fromJson(result,type);
             //   ex_appcon.setAdapter( );

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
        appconfig_back.setOnClickListener(this);
        tv_appcon_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id. tv_appcon_save:
                break;
            case R.id.appconfig_back:
                finish();
                break;
        }
    }
private  class  AppConadapter extends BaseExpandableListAdapter{

    private Context context;
    List<ApproveType> approvetypes;
    List<Integer> icon;

    public AppConadapter(Context context, List<ApproveType> approvetypes) {
        this.context = context;
        this.approvetypes = approvetypes;
        for (ApproveType approvetype:approvetypes) {
            icon.add(approvetype.tyypeId);
        }
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }
    @Override
    public int getGroupCount() {
        return approvetypes.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return approvetypes.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return approvetypes.get(groupPosition).rules.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return  groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
       ApproveType approveType= approvetypes.get(groupPosition);
        ViewHolder viewHolder=ViewHolder.get(context,convertView,null,R.layout.appconfig_father_item);
     ImageView iv_appcon_fa=viewHolder.getView(R.id.iv_appcon_fa);
        TextView tv_appcon_fa=viewHolder.getView(R.id.tv_appcon_fa);
        ImageView iv_appcon_fa_jiantou=viewHolder.getView(R.id.iv_appcon_fa_jiantou);
        tv_appcon_fa.setText(approveType.tyypeName);
     int typeid= icon.get( groupPosition);
        switch (typeid){
            case 1:
                iv_appcon_fa.setBackgroundResource(R.mipmap.icon_approve_qingjia);
                break;
            case 2:
                iv_appcon_fa.setBackgroundResource(R.mipmap.icon_approve_trip);
                break;
            case 3:
                iv_appcon_fa.setBackgroundResource(R.mipmap.icon_approve_baoxiao);
                break;
            case 4:
                iv_appcon_fa.setBackgroundResource(R.mipmap.icon_approve_buqian);
                break;


        }

if (isExpanded){
    //RotateAnimation ro = new RotateAnimation(0,180);
    //RotateAnimation ro = new RotateAnimation(0,180,iv.getWidth()/2,iv.getHeight()/2);
    RotateAnimation  ro = new RotateAnimation(0,90, Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);

    ro.setDuration(1000);  //每次时间
    ro.setRepeatCount(1);  //重复次数
    ro.setRepeatMode(Animation.REVERSE); //重复的模式：RESTART：从头再来   REVERSE:反向

    ro.setFillAfter(true); // 保持动画的最终状态

    iv_appcon_fa_jiantou.startAnimation(ro);

}else{
    RotateAnimation  ro1 = new RotateAnimation(0,-90, Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);

    ro1.setDuration(1000);  //每次时间
    ro1.setRepeatCount(1);  //重复次数
    ro1.setRepeatMode(Animation.REVERSE); //重复的模式：RESTART：从头再来   REVERSE:反向

    ro1.setFillAfter(true); // 保持动画的最终状态

    iv_appcon_fa_jiantou.startAnimation(ro1);
}






        return viewHolder.getConvertView();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        int typeid= icon.get( groupPosition);
       ApproveRule  rule=null;

     if(typeid==4){
         rule=approvetypes.get(groupPosition).rules.get(childPosition);
         ViewHolder viewHolder=ViewHolder.get(context,convertView,null,R.layout.appcon_item_son);
         RelativeLayout ll_appcon_son=viewHolder.getView(R.id.ll_appcon_son);
         ll_appcon_son.setVisibility(View.GONE);
         NoScrollGridView gv_appcon_gd=viewHolder.getView(R.id.gv_appcon_gd);
//         gv_appcon_gd.setAdapter(new CommonAdapter<ApproveRule>() {
//             @Override
//             public void setcontant(com.liu.easyoffice.Utils.ViewHolder viewHolder, ApproveRule approveRule, int position) {
//
//             }
//         });
         return viewHolder.getConvertView();
     }
       if (childPosition==0){
           rule=approvetypes.get(groupPosition).rules.get(childPosition-1);
           ViewHolder viewHolder=ViewHolder.get(context,convertView,null,R.layout.appcon_son_first_item);
           ImageView appcon_fa_addmem=viewHolder.getView(R.id.appcon_fa_addmem);
           appcon_fa_addmem.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

               }
           });
           return viewHolder.getConvertView();
       }else{
           ViewHolder viewHolder=ViewHolder.get(context,convertView,null,R.layout.appcon_item_son);

TextView tv_app_con_danwei=viewHolder.getView(R.id.tv_app_con_danwei);
           ImageView iv_appcon_jianhao=viewHolder.getView(R.id.iv_appcon_jianhao);
           NoScrollGridView gv_appcon_gd=viewHolder.getView(R.id.gv_appcon_gd);
           if (childPosition==1){ iv_appcon_jianhao.setVisibility(View.GONE);}else {
               iv_appcon_jianhao.setVisibility(View.VISIBLE);
           }
           iv_appcon_jianhao.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

               }
           });
           switch (typeid){
               case 1:
                   tv_app_con_danwei.setText("天时");
                   break;
               case 2:
                   tv_app_con_danwei.setText("天时");
                   break;
               case 3:
                   tv_app_con_danwei.setText("元时");
                   break;



           }
        //   List<User> users=new A
           for (int i = 0; i <rule.shunxulist.length ; i++) {

           }
//           gv_appcon_gd.setAdapter(new CommonAdapter<ApproveRule>() {
//               @Override
//               public void setcontant(com.liu.easyoffice.Utils.ViewHolder viewHolder, ApproveRule approveRule, int position) {
//
//               }
//           });

           return null;

       }




    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }
}

}
