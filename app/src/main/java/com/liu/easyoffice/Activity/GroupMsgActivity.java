package com.liu.easyoffice.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liu.easyoffice.Adapter.CommonAdapter;
import com.liu.easyoffice.Adapter.ViewHolder;
import com.liu.easyoffice.MyView.MyGridView;
import com.liu.easyoffice.Progressbar.LoadDialog;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.ActivityCollector;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.pojo.ChatGroup;
import com.liu.easyoffice.pojo.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class GroupMsgActivity extends AppCompatActivity {

    private Button quitBtn;
    private TextView chatGroupName;
    private TextView createTime;
    private MyGridView memberGv;
    String chatGroupId;
    List<User> users=new ArrayList<>();
    private RelativeLayout nameRelative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatgroup_msg);
        ActivityCollector.addActivity(this);
        initView();
        initDate();
        initClick();
        LoadDialog.show(this,"正在加载");
        connectToServe();
    }

    public void initView() {
        quitBtn = ((Button) findViewById(R.id.chatgroup_btn_quit));
        chatGroupName = ((TextView) findViewById(R.id.chatgroup_msg_et_groupName));
        nameRelative = ((RelativeLayout) findViewById(R.id.chatgroup_rlt_groupName));
        createTime = ((TextView) findViewById(R.id.chatgroup_msg_tv_createTime));
        memberGv = ((MyGridView) findViewById(R.id.chatgroup_gv_members));
    }

    public void initDate() {

    }

    public void initClick() {
        //退组
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupMsgActivity.this);
//设置对话框图标，可以使用自己的图片，Android本身也提供了一些图标供我们使用
                builder.setIcon(android.R.drawable.ic_dialog_alert);
//设置对话框标题
                builder.setTitle("退出讨论组");
//设置对话框内的文本
                builder.setMessage("确定退出讨论组？");
//设置确定按钮，并给按钮设置一个点击侦听，注意这个OnClickListener使用的是DialogInterface类里的一个内部接口
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        quitChatGroup();
                    }
                });
//设置取消按钮
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
//使用builder创建出对话框对象
                AlertDialog dialog = builder.create();
//显示对话框
                dialog.show();
            }
        });
        //编辑讨论组名称跳转
        nameRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GroupMsgActivity.this,SetChatGroupName.class);
                intent.putExtra("groupId",chatGroupId);
                intent.putExtra("groupName",chatGroupName.getText().toString());
                startActivity(intent);
            }
        });
    }

    public void connectToServe() {
        chatGroupId= getIntent().getStringExtra("chatGroupId");
        Log.e("tag", "chat" + chatGroupId);
        final RequestParams params = new RequestParams(Utils.GET_CURRENT_CHATGROUP_USERS);
        params.addParameter("chatGroupId", chatGroupId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ChatGroup chatGroup = new Gson().fromJson(result, ChatGroup.class);
                chatGroupName.setText(chatGroup.getName());
                createTime.setText(chatGroup.getCreateTime() + "");
                users.clear();
                users.addAll(chatGroup.getUsers());
                users.add(new User());
                final int length = users.size();
                memberGv.setAdapter(new CommonAdapter<User>(GroupMsgActivity.this, R.layout.dynamic_add_contact_item, users, null) {
                    @Override
                    public void convert(ViewHolder viewHolder, User item, int posion) {
                        
                        ImageView iv = viewHolder.getView(R.id.itemUser_iv);
                        TextView userName = viewHolder.getView(R.id.itemUser_tv_name);
                        if (posion == length - 1) {
                            iv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.add_member));
                            userName.setText("添加好友");
                        } else {
                            RongIM.getInstance().refreshUserInfoCache(new UserInfo(item.getUserId(),item.getUserName(),Uri.parse(Utils.UPLOAD_IMG+item.getImgUrl())));
                            xUtilsImageUtils.display(iv,item.getImgUrl(),true);
                            userName.setText(item.getUserName());
                        }
                    }
                });
                LoadDialog.dismiss(GroupMsgActivity.this);
                memberGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == length - 1) {
                            Intent intent = new Intent(GroupMsgActivity.this, CreateGroup.class);
                            intent.putExtra("chatGroupId", chatGroupId);
                            intent.putExtra("chatGroupName",chatGroupName.getText().toString());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(GroupMsgActivity.this, ContactMsgActivity.class);
                            intent.putExtra("userId", users.get(position).getUserId());
                            startActivity(intent);
                        }
                    }
                });
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
     * 退讨论组，将讨论组id与当前成员id传到服务器
     */
    public void quitChatGroup(){
        Log.e("quit", "quitChatGroup: "+"quit" );
        final String chatGroupId= getIntent().getStringExtra("chatGroupId");
        final ChatGroup chatGroup=new ChatGroup();
        chatGroup.setGroupId(chatGroupId);
        User user=MySharePreference.getCurrentUser(this);
        List<User> users=new ArrayList<>();
        users.add(user);
        chatGroup.setUsers(users);
        RongIM.getInstance().quitDiscussion(chatGroupId, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Gson gson=new Gson();
                String json=gson.toJson(chatGroup);
                RequestParams params=new RequestParams(Utils.CREATE_CHAT_GROUP_ID);
                params.addParameter("chatMessage",json);
                params.addParameter("quit","0");
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if(result.equals("200")){
                            ToastCommom.ToastShow(GroupMsgActivity.this,null,"退出成功");
//                            RongIM.getInstance().refreshDiscussionCache(new Discussion(chatGroupId,chatGroupName.getText().toString()));

                            RongIM.getInstance().removeConversation(Conversation.ConversationType.DISCUSSION, chatGroupId, new RongIMClient.ResultCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    Toast.makeText(GroupMsgActivity.this, aBoolean+"", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    Toast.makeText(GroupMsgActivity.this, errorCode.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            finish();
                            Intent intent=new Intent(GroupMsgActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else {
                            ToastCommom.ToastShow(GroupMsgActivity.this,null,"退组失败");

                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        ToastCommom.ToastShow(GroupMsgActivity.this,null,"退出失败");
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
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
