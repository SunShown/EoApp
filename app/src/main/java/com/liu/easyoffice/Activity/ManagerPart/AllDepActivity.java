package com.liu.easyoffice.Activity.ManagerPart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liu.easyoffice.Adapter.CommonAdapter;
import com.liu.easyoffice.Adapter.ViewHolder;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.pojo.Group;
import com.liu.easyoffice.pojo.GroupMember;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class AllDepActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout namesLayout;
    private ListView showDeplvFirst;
    private CommonAdapter<Group> adapter = null;
    private Context mContext;
    private List<Group> groups = new ArrayList<>();
    private List<View> viewList = new ArrayList<>();
    private ListView showDeplvSec;
    private LinearLayout nodateLayout;
    private LinearLayout nonetLayout;
    private Group spGroup;
    private Group currentGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_dep);
        initView();
        initDate();
        connectToGetGroup(spGroup.getTgId());
    }

    private void initView() {

        Log.e("spGroup", "initView: "+spGroup);
        namesLayout = ((LinearLayout) findViewById(R.id.all_dep_lyt));//显示部门级联信息
        showDeplvFirst = ((ListView) findViewById(R.id.all_dep_lv));//显示当前所有部门
        nodateLayout = ((LinearLayout) findViewById(R.id.all_dep_nodate));
        nonetLayout = ((LinearLayout) findViewById(R.id.all_dep_nonet));
        addView(null);
    }

    private void initDate() {
        mContext = this;
        spGroup=MySharePreference.getCurrentCroup(mContext);
        currentGroup= (Group) getIntent().getSerializableExtra("currentGroup");
        Log.e("currentGroup", "groupName"+currentGroup.getTgName()+" tgId="+currentGroup.getTgId());
        adapter = new CommonAdapter<Group>(mContext, groups, R.layout.show_dep_item) {
            @Override
            public void convert(ViewHolder viewHolder, final Group item, int posion) {
                Log.e("currentGroup", "item "+item.getTgName()+" tgId="+item.getTgId() );
                TextView groupName = viewHolder.getView(R.id.show_dep_item_gname);
                View view=viewHolder.getView(R.id.show_dep_item_view);
                RelativeLayout layout=viewHolder.getView(R.id.show_dep_item_rlt_icon);
                RelativeLayout parentLayout=viewHolder.getView(R.id.show_dep_parent_layout);
                final RelativeLayout iconLayout = viewHolder.getView(R.id.show_dep_item_rlt_icon);
                RelativeLayout rightLayout=viewHolder.getView(R.id.show_dep_item_rlt);//点击左边布局
                groupName.setText(item.getTgName());
                if(item.getTgId().equals(currentGroup.getTgId())){
                    Log.e("current", "A组 " );
                    parentLayout.setBackgroundResource(R.color.grey_bg_choosed);
                    groupName.setTextColor(Color.WHITE);
                    parentLayout.setClickable(false);
                    view.setVisibility(View.GONE);
                    layout.setVisibility(View.GONE);
                }else {
                    parentLayout.setBackgroundResource(R.color.white);
                    groupName.setTextColor(Color.BLACK);
                    parentLayout.setClickable(true);
                    view.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.VISIBLE);
                }
                iconLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(item.getTgId().equals(currentGroup.getTgId())){
                            ToastCommom.ToastShow(mContext,null,"不能设置上级部门为该部门以下");
                        }else {
                            connectToGetGroup(item.getTgId());
                            setInAnimation(showDeplvFirst);
                            addView(item);
                        }
                    }
                });
                //点击右面将group信息传到前一个页面
                rightLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(item.getTgId().equals(currentGroup.getTgId())){
                            ToastCommom.ToastShow(mContext,null,"不能设置上级部门为该部门以下");
                        }else {
                            Intent intent=new Intent();
                            intent.putExtra("parentGroup",item);
                            AllDepActivity.this.setResult(2,intent);
                            AllDepActivity.this.finish();
                        }
                    }
                });
            }
        };
    }

    /**
     * 动态添加标题的级联
     * 并添加监听事件
     *
     * @param group
     */
    public void addView(Group group) {
        final View view = LayoutInflater.from(this).inflate(R.layout.dynamic_add_group_title, null);
        TextView gidTv = ((TextView) view.findViewById(R.id.all_dep_gid));
        TextView gNameTv = (TextView) view.findViewById(R.id.dynamic_add_group_tv_tgName);
        ImageView iv = (ImageView) view.findViewById(R.id.dynamic_add_group_iv);
        TextView count = ((TextView) view.findViewById(R.id.count));
        if (namesLayout.getChildCount() == 0) {//如果没有任何内容
            gNameTv.setText("首页");
            iv.setVisibility(View.GONE);
            gidTv.setText("0");
        } else {
            gidTv.setText(group.getTgId() + "");
            gNameTv.setText(group.getTgName());
        }
        namesLayout.addView(view);
        count.setText(namesLayout.getChildCount() + "");


        if (namesLayout.getChildCount() > 1) {//设置之前的控件颜色为蓝色，并且可以点击
            View viewchild = namesLayout.getChildAt(namesLayout.getChildCount() - 2);
            TextView tv = ((TextView) viewchild.findViewById(R.id.dynamic_add_group_tv_tgName));
            tv.setTextColor(getResources().getColor(R.color.de_blue));
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView idTv = ((TextView) v.findViewById(R.id.all_dep_gid));
                long id = Long.parseLong(idTv.getText().toString());
                TextView tv = (TextView) v.findViewById(R.id.count);

                if (Integer.parseInt(tv.getText().toString()) != namesLayout.getChildCount()) {
                    Log.e("click", "addView: " + "false");
                    setOutAnimation(showDeplvFirst);
                    TextView name = (TextView) v.findViewById(R.id.dynamic_add_group_tv_tgName);
                    connectToGetGroup(id);//再次去获取数据
                    adapter.notifyDataSetChanged();
                    name.setTextColor(getResources().getColor(R.color.ldeep_grey));
                    int start = Integer.parseInt(tv.getText().toString());
                    int count = namesLayout.getChildCount() - start;
                    deleteView(start, count);
                }
            }
        });
    }

    /**
     * 删除标题级联
     *
     * @param
     */
    public void deleteView(int start, int end) {
        namesLayout.removeViews(start, end);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    //设置从右向左动画
    private void setInAnimation(View view) {
        Animation animRightIn = AnimationUtils.loadAnimation(this, R.anim.listview_right_in);
        view.setAnimation(animRightIn);
    }

    //设置从左向右动画
    private void setOutAnimation(View view) {
        Animation animRight = AnimationUtils.loadAnimation(this, R.anim.listview_left_in);
        view.setAnimation(animRight);

    }

    /**
     * 从服务器获取当前下面的所由部门
     */
    public void connectToGetGroup(long currentGId) {
        showDeplvFirst.setVisibility(View.GONE);
        AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(R.drawable.load_date);
        final ImageView iv = (ImageView) findViewById(R.id.all_dep_iv);
        iv.setBackgroundDrawable(drawable);
        drawable.start();
        iv.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(Utils.GET_GROUP_MEMBER);
        params.addParameter("companyId", MySharePreference.getCurrentCompany(mContext).getTcId());
        params.addParameter("currentGId", currentGId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                GroupMember groupMember = gson.fromJson(result, GroupMember.class);
                List<Group> groupsNet = groupMember.getGroups();
                groups.clear();
                groups.addAll(groupsNet);
                Log.e("groupSize", "size: " + groups.size());
                iv.setVisibility(View.GONE);
                if (groups.size() == 0) {
                    nodateLayout.setVisibility(View.VISIBLE);//没有数据
                } else {
                    nodateLayout.setVisibility(View.GONE);
                    showDeplvFirst.setVisibility(View.VISIBLE);
                }
                showDeplvFirst.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("AllDep", "onError: " + ex);
                iv.setVisibility(View.GONE);
                nonetLayout.setVisibility(View.VISIBLE);//没网
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}

