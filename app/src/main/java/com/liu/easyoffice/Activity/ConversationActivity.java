package com.liu.easyoffice.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.ActivityCollector;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.pojo.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * 会话界面
 * Created by hui on 2016/9/22.
 */

public class ConversationActivity extends FragmentActivity {
    private TextView chatTitle;
    private ImageView chatGroupIv;
    private String targetId=null;
    private final String DISCUSSION="discussion";
    private final String PRIVATE="private";
    MyReceiver receiver=null;
    String title=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        ActivityCollector.addActivity(this);
        init();
        initReceiver();
        initMsg(getIntent());
        initClick();
    }

    /**
     * 控件初始化
     */
    private void init(){
        chatTitle = ((TextView) findViewById(R.id.chat_title));//用户名/讨论组名
        chatGroupIv = ((ImageView) findViewById(R.id.chat_title_iv));//用户头像
    }
    //注册广播
    private void initReceiver(){
        receiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(Utils.Receiver.CHANGE_TILE);
        registerReceiver(receiver,filter);
    }
    /**
     * 控件信息初始化
     */
    private void initMsg(Intent intent){
        //设置页面title
        Uri uri=intent.getData();
        targetId=uri.getQueryParameter("targetId").toString();
        title=uri.getQueryParameter("title").toString();//用户名
        Log.e("liufeixuan", "initMsg: "+targetId);
        Log.e("targetid", "tel: "+title );
        String chatType=uri.getLastPathSegment();
        Log.e("type", "initMsg: "+chatType );
        if(chatType.equals(DISCUSSION)){
            chatGroupIv.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.group_iv));
        }else if(chatType.equals(PRIVATE)){
//            RongIM.getInstance().refreshUserInfoCache(new UserInfo(tel,title,null));
            chatGroupIv.setVisibility(View.GONE);
        }
        chatTitle.setText(title);

    }
    public void initClick(){
        clickPersonMsg();
        clickTitleIvMsg();
    }
    /**
     * 点击头像查询用户信息
     */
    public void clickPersonMsg(){
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                Intent intent=new Intent(getApplicationContext(),ContactMsgActivity.class);
                intent.putExtra("userId",userInfo.getUserId());
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });
    }
    public void clickTitleIvMsg(){
        chatGroupIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ConversationActivity.this,GroupMsgActivity.class);
                intent.putExtra("chatGroupId",targetId);
                startActivity(intent);
            }
        });
    }

    /**
     * 取消注册
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver!=null){
            unregisterReceiver(receiver);
        }
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("start", "onResume: " );
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(Utils.Receiver.CHANGE_TILE)){
                String groupName=intent.getStringExtra("groupName");
                chatTitle.setText(groupName);
            }
        }
    }

}
