package com.liu.easyoffice.Activity;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.DateUtil;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.pojo.Company;
import com.liu.easyoffice.pojo.Group;
import com.liu.easyoffice.pojo.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by hui on 2016/9/22.
 */

public class ContactMsgActivity extends Activity {
    private ImageView contactImg;
    private TextView contactName;
    private RelativeLayout startChat;
    String targetId=null;
    String userName=null;

    String imgUrl=null;

    boolean isUserCard=false;
    private TextView user_card_chat;
    private RelativeLayout chat;
    private TextView telnum;
    private TextView birth;
    private TextView area;
    private RelativeLayout begin;
    private ImageView sendmessage;
    private ImageView telphone;
    private Context mcontext;
    private TextView departure_tv;
    private TextView position_tv;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_msg);
        mcontext=this;
        initView();
        initMsg();
        initClick();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        mBack = ((ImageView) findViewById(R.id.contactmsgback));
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactMsgActivity.this.finish();
            }
        });
        contactImg = ((ImageView) findViewById(R.id.user_card_head));
        contactName = ((TextView) findViewById(R.id.user_card_name));
        startChat = ((RelativeLayout) findViewById(R.id.stat_chat));
        user_card_chat = ((TextView) findViewById(R.id.user_card_chat));
        chat = ((RelativeLayout) findViewById(R.id.stat_chat));
        telnum = ((TextView) findViewById(R.id.user_card_tel_tv));
        birth = ((TextView) findViewById(R.id.user_card_birth_tv));
        area = ((TextView) findViewById(R.id.user_card_area_tv));
        begin = ((RelativeLayout) findViewById(R.id.person_center_ll_header));
        sendmessage = ((ImageView) findViewById(R.id.user_card_message));
        telphone = ((ImageView) findViewById(R.id.user_card_phone));
        departure_tv = ((TextView) findViewById(R.id.user_card_departure_tv));
        position_tv = ((TextView) findViewById(R.id.user_card_position_tv));
    }
    /**
     * 初始化控件信息
     */
    private void initMsg(){
        targetId=getIntent().getStringExtra("userId");
        isUserCard=getIntent().getBooleanExtra("userCard",false);
        if(isUserCard){
            chat.setVisibility(View.GONE);
            sendmessage.setVisibility(View.GONE);
            telphone.setVisibility(View.GONE);
        }
        Log.e("con","user=="+targetId);
        //获取公司信息
        RequestParams compangy_params=new RequestParams(Utils.GET_GROUPS);
        Company c= MySharePreference.getCurrentCompany(mcontext);
        compangy_params.addParameter("companyId",c.getTcId());
        compangy_params.addParameter("tel",targetId);
        x.http().get(compangy_params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("con", "getGroup"+result );
                String departures="";
                Gson gson=new Gson();
                Type type=new TypeToken<List<Group>>(){}.getType();
                List<Group> departure=gson.fromJson(result,type);
                departures=departure.get(0).getTgName();
                departure_tv.setText(departures);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("con", "onError:getGroup"+ex );
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        //获取个人信息
        RequestParams params=new RequestParams(Utils.CONTACT_CARD);
        params.addParameter("userId",targetId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("con",result);
                Gson gson=new Gson();
                User user=gson.fromJson(result, User.class);
                System.out.println(user);
                userName=user.getUserName();
                imgUrl=user.getImgUrl();
                contactName.setText(userName);
                xUtilsImageUtils.display(contactImg,imgUrl,true);
                initUser();
                telnum.setText(user.getUserId());
                String tdate= DateUtil.dateToString(user.getBirthday());
                birth.setText(tdate);
                area.setText(user.getAddress());
                if("".equals(user.getUserPosition())||user.getUserPosition()==null){
                    position_tv.setText("暂无");
                }else{
                    position_tv.setText(user.getUserPosition());
                }
                if("".equals(user.getAddress())||user.getAddress()==null){
                    area.setText("暂无");
                }else{
                    area.setText(user.getAddress());
                }
                if(user.getSex()==1){
                    begin.setBackgroundColor(getResources().getColor(R.color.woman_bg_color));
                    Drawable nav_up=getResources().getDrawable(R.mipmap.user_card_women);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    contactName.setCompoundDrawables(null, null, nav_up, null);
                }else if(user.getSex()==0){
                    begin.setBackgroundColor(getResources().getColor(R.color.toolbar_blue));
                    Drawable nav=getResources().getDrawable(R.mipmap.user_card_man);
                    nav.setBounds(0, 0, nav.getMinimumWidth(), nav.getMinimumHeight());
                    contactName.setCompoundDrawables(null, null, nav, null);
                }else{
                    begin.setBackgroundColor(getResources().getColor(R.color.toolbar_blue));
                    Drawable nav1=getResources().getDrawable(R.mipmap.user_card_unkown);
                    nav1.setBounds(0, 0, nav1.getMinimumWidth(), nav1.getMinimumHeight());
                    contactName.setCompoundDrawables(null, null, nav1, null);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("con",ex.getMessage());
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
     * 点击事件
     */
    private void initClick(){
        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("target", "onClick: "+targetId );
                RongIM.getInstance().startPrivateChat(ContactMsgActivity.this,targetId,userName);
            }
        });
        //打电话
        telphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("Android.intent.action.CALL");
                intent.setData(Uri.parse("tel:"+ telnum.getText().toString().trim()));
                startActivity(intent);
            }
        });
        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(telnum.getText().toString().trim());
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + telnum.getText().toString().trim()));
                startActivity(sendIntent);
            }
        });
    }

    /**
     * 初始化融云
     */
    private void initUser(){
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(targetId,userName, Uri.parse(Utils.UPLOAD_IMG+imgUrl)));
        Log.e("userTitle", "initUser: "+Utils.UPLOAD_IMG+imgUrl );
    }
}
