package com.liu.easyoffice.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liu.easyoffice.Progressbar.LoadDialog;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.ActivityCollector;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.pojo.ChatGroup;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class SetChatGroupName extends AppCompatActivity {

    private Button confirm;
    private EditText et_groupName;
    private String groupId;
    private String groupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_chat_group_name);
        ActivityCollector.addActivity(this);
        groupName=getIntent().getStringExtra("groupName");
        groupId=getIntent().getStringExtra("groupId");
        initView();
        initClick();
    }

    private void initView() {
        confirm = ((Button) findViewById(R.id.tv_confirm));
        et_groupName = ((EditText) findViewById(R.id.et_confirm_name));
        et_groupName.setText(groupName);
    }

    private void initClick() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name=et_groupName.getText().toString();
                if(!isEmpty(name)){
                    RongIM.getInstance().setDiscussionName(groupId, name, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            LoadDialog.show(SetChatGroupName.this,"正在更新数据...");
                            updateGroupNameToNet(name);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            ToastCommom.ToastShow(SetChatGroupName.this,null,"更新失败");
                        }
                    });

                }
            }
        });
    }

    private boolean isEmpty(String name) {
        if (name == null || name.trim().equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SetChatGroupName.this);
            //设置对话框图标，可以使用自己的图片，Android本身也提供了一些图标供我们使用
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            //设置对话框内的文本
            builder.setMessage("讨论组名称不能为空");
            //设置确定按钮，并给按钮设置一个点击侦听，注意这个OnClickListener使用的是DialogInterface类里的一个内部接口
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    et_groupName.setText("");
                }
            });
            //使用builder创建出对话框对象
            AlertDialog dialog = builder.create();
            //显示对话框
            dialog.show();
            return true;
        }
        return false;
    }
    private void updateGroupNameToNet(final String groupName){
        RequestParams  params=new RequestParams(Utils.CREATE_CHAT_GROUP_ID);
        ChatGroup chatGroup=new ChatGroup();
        chatGroup.setGroupId(groupId);
        chatGroup.setName(groupName);
        Gson gson=new Gson();
        String chatMessage=gson.toJson(chatGroup);
        params.addParameter("chatMessage",chatMessage);
        params.addParameter("changeName","0");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("200")){
                    Intent intent=new Intent();
                    intent.putExtra("groupName",groupName);
                    intent.setAction(Utils.Receiver.CHANGE_TILE);
                    sendBroadcast(intent);
                    RongIM.getInstance().startDiscussionChat(SetChatGroupName.this,groupId,groupName);
                    LoadDialog.dismiss(SetChatGroupName.this);
                    Toast.makeText(SetChatGroupName.this,"修改成功", Toast.LENGTH_SHORT).show();
                    ActivityCollector.finishAll();
                }else {
                    Toast.makeText(SetChatGroupName.this, "失败", Toast.LENGTH_SHORT).show();
                }
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
