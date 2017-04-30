package com.liu.easyoffice.Activity.ManagerPart;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liu.easyoffice.Adapter.CommonAdapter;
import com.liu.easyoffice.Adapter.ViewHolder;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.pojo.Group;
import com.liu.easyoffice.pojo.GroupMember;
import com.liu.easyoffice.pojo.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepLeaderActivity extends AppCompatActivity {

    private ListView memberLv;
    private Group currentGroup;
    private Context mContext;
    private LinearLayout nodateLayout;
    private List<User> users = new ArrayList<>();
    private CommonAdapter<User> adapter;
    private LinearLayout nonetLayout;
    private Map<String, User> userMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dep_leader);
        initView();
        initDate();
        connectToServer();
        listItemClick();
    }

    private void initView() {
        mContext = this;
        userMap = new HashMap<>();
        memberLv = ((ListView) findViewById(R.id.dep_leader_lv));
        nodateLayout = ((LinearLayout) findViewById(R.id.dep_leader_no_date));
        nonetLayout = ((LinearLayout) findViewById(R.id.dep_leader_no_net));
    }

    private void initDate() {
        currentGroup = (Group) getIntent().getSerializableExtra("currentGroup");
        adapter = new CommonAdapter<User>(mContext, users, R.layout.show_member_item) {
            @Override
            public void convert(ViewHolder viewHolder, User item, int posion) {
                TextView tv = viewHolder.getView(R.id.show_member_item_tv_id);
                ImageView ivTitle = viewHolder.getView(R.id.show_member_iv);
                TextView userNameTv = viewHolder.getView(R.id.show_member_item_tv_name);
                tv.setText(item.getId() + "");
                userNameTv.setText(item.getUserName());
                userMap.put(item.getId() + "", item);
                x.image().bind(ivTitle, item.getImgUrl());

            }
        };
        memberLv.setAdapter(adapter);
    }

    /**
     * 从服务器获取当前租的成员
     */
    private void connectToServer() {
        AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(R.drawable.load_date);
        final ImageView iv = (ImageView) findViewById(R.id.dep_leader_load);
        iv.setBackgroundDrawable(drawable);
        drawable.start();
        iv.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(Utils.GET_GROUP_MEMBER);
        params.addParameter("companyId", MySharePreference.getCurrentCompany(mContext).getTcId());
        params.addParameter("currentGId", currentGroup.getTgId());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                iv.setVisibility(View.GONE);
                Gson gson = new Gson();
                GroupMember groupMember = gson.fromJson(result, GroupMember.class);
                if (groupMember.getUsers().size() != 0) {//如果有数据
                    nodateLayout.setVisibility(View.GONE);
                    users.clear();
                    users.addAll(groupMember.getUsers());

                    adapter.notifyDataSetChanged();
                } else {
                    nodateLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("DepLeader", "onError: " + ex);
                iv.setVisibility(View.GONE);
                nonetLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void listItemClick() {
        memberLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvId = (TextView) view.findViewById(R.id.show_member_item_tv_id);
                User user = userMap.get(tvId.getText().toString());
                Intent intent = new Intent();
                intent.putExtra("leader", user);
                DepLeaderActivity.this.setResult(3, intent);
                DepLeaderActivity.this.finish();
            }
        });
    }
}
