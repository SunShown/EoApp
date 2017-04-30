package com.liu.easyoffice.Activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liu.easyoffice.Adapter.AllGroupExpandListAdapter;
import com.liu.easyoffice.Progressbar.LoadDialog;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.ActivityCollector;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.pojo.ChatGroup;
import com.liu.easyoffice.pojo.Company;
import com.liu.easyoffice.pojo.Group;
import com.liu.easyoffice.pojo.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by hui on 2016/9/23.
 */

public class CreateGroup extends Activity {
    String imgURl;
    ExpandableListView contactsLv;
    SimpleAdapter adapter;
    List<Map<String, Object>> list = new ArrayList<>();
    List<Group> groups = new ArrayList<>();
    LinearLayout parent;
    Map<String, View> views = new HashMap<>();
    List<String> chatUserIds = new ArrayList<>();//添加讨论则成员的id
    Map<String, String> chatUserNames = new HashMap<>();//添加讨论组成员的姓名
    private Button btnConfirm;
    String chatGroupId = null;
    String chatGroupName = null;
    AllGroupExpandListAdapter expandListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_contacts);
        ActivityCollector.addActivity(this);
        initView();
    }

    private void initView() {
        contactsLv = ((ExpandableListView) findViewById(R.id.choose_contacts_lv));
        parent = (LinearLayout) findViewById(R.id.dynamic_add_contact);//显示添加创建讨论组的父布局
        btnConfirm = ((Button) findViewById(R.id.dynamic_add_btn_confirm));//确定按钮

        initMsg();
        confirmChat();
    }

    private void initMsg() {
        LoadDialog.show(this, "正在加载");
        Company company = MySharePreference.getCurrentCompany(this);
        RequestParams params = new RequestParams(Utils.GET_ALL_GROUPS);
        if (getIntent().getStringExtra("chatGroupId") != null && !getIntent().getStringExtra("chatGroupId").trim().equals("")) {
            chatGroupId = getIntent().getStringExtra("chatGroupId");
            chatGroupName = getIntent().getStringExtra("chatGroupName");
            params.addParameter("chatGroupId", chatGroupId);
        }
        params.addParameter("companyId", company.getTcId());
        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LoadDialog.dismiss(CreateGroup.this);
                Gson gson = new Gson();
                Log.e("result", result);
                groups.clear();
                groups.addAll((Collection<? extends Group>) gson.fromJson(result, new TypeToken<List<Group>>() {
                }.getType()));
                Log.e("group", groups.size() + "");
                expandListAdapter = new AllGroupExpandListAdapter(getApplicationContext(), groups, new AllGroupExpandListAdapter.OnCheckBoxClickLinstener() {
                    @Override
                    public void handleClick(int childPosition, int groupPosition) {
                        User user = groups.get(groupPosition).getUsers().get(childPosition);
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getUserId(),user.getUserName(),Uri.parse(Utils.UPLOAD_IMG+user.getImgUrl())));
                        user.toggle();
                        expandListAdapter.notifyDataSetChanged();
                        if (user.isChecked()) {
                            chatUserIds.add(user.getUserId());
                            chatUserNames.put(user.getUserId(), user.getUserName());
                            Log.e("names", chatUserNames.get(user.getUserId()));
                            addView(user.getUserName(), user.getImgUrl(), user.getUserId());
                        } else {
                            removeView(user.getUserId());
                            chatUserIds.remove(user.getUserId());
                            chatUserNames.remove(user.getUserId());
                        }
                    }
                });
                contactsLv.setAdapter(expandListAdapter);
                itemClic();
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

    /**
     * 点击联系人
     */
    private void itemClic() {
        contactsLv.setGroupIndicator(null);
        contactsLv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            private TextView userId;
            private TextView imgUrl;
            private TextView userName;
            private CheckBox checkItem;

            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                checkItem = ((CheckBox) view.findViewById(R.id.choose_contacts_item_cb));
                userName = ((TextView) view.findViewById(R.id.choose_contacts_item_name));
                imgUrl = ((TextView) view.findViewById(R.id.choose_contacts_item_imgUrl));
                userId = ((TextView) view.findViewById(R.id.choose_contacts_item_id));
                String name = userName.getText().toString();
                String img = imgUrl.getText().toString();
                String id = userId.getText().toString();
                groups.get(i).getUsers().get(i1).toggle();
                expandListAdapter.notifyDataSetChanged();
                if (checkItem.isChecked()) {
                    removeView(id);
                    chatUserIds.remove(id);
                    chatUserNames.remove(id);
                } else {
                    chatUserIds.add(id);
                    chatUserNames.put(id, name);
                    Log.e("names", chatUserNames.get(id));
                    addView(name, img, id);
                }
                return true;
            }
        });

    }

    /**
     * 动态添加控件
     */
    private void addView(String userName, String imgURl, String userId) {
        View view = LayoutInflater.from(this).inflate(R.layout.dynamic_add_contact_item, null);
        ImageView iv = (ImageView) view.findViewById(R.id.itemUser_iv);
        TextView name = (TextView) view.findViewById(R.id.itemUser_tv_name);
        xUtilsImageUtils.display(iv,imgURl,true);
        name.setText(userName);
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(userId,userName, Uri.parse(Utils.UPLOAD_IMG+imgURl)));
        parent.addView(view);
        views.put(userId, view);
    }

    /**
     * 删除控件
     *
     * @param userId
     */
    private void removeView(String userId) {
        Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();
        View view = views.get(userId);
        views.remove(view);
        parent.removeView(view);
    }

    /**
     * 参加讨论组的成员id
     */
    private void confirmChat() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chatGroupId != null) {
                    RongIM.getInstance().addMemberToDiscussion(chatGroupId, chatUserIds, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            List<User> users = new ArrayList<User>();
                            User user = null;
                            for (int i = 0; i < chatUserIds.size(); i++) {
                                user = new User();
                                user.setUserId(chatUserIds.get(i));
                                users.add(user);
                                RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getUserId()+"",user.getUserName(), Uri.parse(Utils.UPLOAD_IMG+user.getImgUrl())));
                            }
                            LoadDialog.show(CreateGroup.this, "正在添加");
                            connectToNet(chatGroupId, mapToString(), users);
                            ActivityCollector.finishAll();
                            RongIM.getInstance().startDiscussionChat(CreateGroup.this, chatGroupId, chatGroupName);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                } else {
                    if (chatUserIds.size() > 1) {
                        RongIM.getInstance().createDiscussion(mapToString(), chatUserIds, new RongIMClient.CreateDiscussionCallback() {
                            @Override
                            public void onSuccess(String s) {
                                Log.e("chat", "onSuccess: " + s);
                                chatUserIds.add(MySharePreference.getCurrentUser(CreateGroup.this).getUserId());
                                List<User> users = new ArrayList<User>();
                                User user = null;
                                for (int i = 0; i < chatUserIds.size(); i++) {
                                    user = new User();
                                    user.setUserId(chatUserIds.get(i));
                                    users.add(user);
                                }
                                connectToNet(s, mapToString(), users);
                                Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                                CreateGroup.this.finish();
                            }
                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(CreateGroup.this, "请选择至少两位联系人", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private String mapToString() {
        StringBuilder userNames = new StringBuilder();
        for (int i = 0; i < chatUserNames.size() - 1; i++) {
            userNames.append(chatUserNames.get(chatUserIds.get(i)) + "、");
        }
        userNames.append(chatUserNames.get(chatUserIds.get(chatUserNames.size() - 1)));
        userNames.append("、" + MySharePreference.getCurrentUser(this).getUserName());
        return userNames.toString();
    }

    public void connectToNet(final String groupId, String groupName, List<User> users) {
        RequestParams params = new RequestParams(Utils.CREATE_CHAT_GROUP_ID);
        Timestamp currentTime = new Timestamp(new Date().getTime());
        ChatGroup chatGroup = new ChatGroup(groupId, groupName, users, currentTime);
        Gson gson = new Gson();
        String chatMessage = gson.toJson(chatGroup);
        if (chatGroupId != null) {
            params.addBodyParameter("flag", "0");//表示是添加成员
        }
        params.addBodyParameter("chatMessage", chatMessage);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("chat", "onSuccess: " + result);
//                RongIM.getInstance().startDiscussionChat(CreateGroup.this,groupId,mapToString());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
