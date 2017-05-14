package com.liu.easyoffice.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.liu.easyoffice.Adapter.CommonAdapter;
import com.liu.easyoffice.Adapter.ViewHolder;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.pojo.Group;
import com.liu.easyoffice.pojo.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hui on 2016/9/24.
 */

public class SearchMember extends Activity {
    private EditText etUserId;
    private ListView memLv;
    private CommonAdapter adapter;
    private Context mContext;
    private List<User> users=new ArrayList<>();
    private Long currentGid;
    private String addnum;
    public static final int REQUEST_SUCCESS = 1;
    public static final int REQUEST_FAILED = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        initView();
        initDate();
        searchClick();
    }
    /**
     * 初始化控件
     */
    private void initView(){
        etUserId = ((EditText) findViewById(R.id.add_friend_et_number));
        memLv = ((ListView) findViewById(R.id.add_friend_lv));
        listItemClick();
    }
    private void initDate(){
        mContext=this;
        currentGid=getIntent().getLongExtra("currentGId",-1);
        Log.e("currentGroupIddd2", "onClick: "+currentGid);
        addnum=getIntent().getStringExtra("addnum").trim();
        adapter=new CommonAdapter<User>(mContext, R.layout.telcontacts_item, users, null) {
            @Override
            public void convert(ViewHolder viewHolder, final User item, int posion) {
                TextView userName = viewHolder.getView(R.id.tel_contacts_item_name);
                TextView userTel = viewHolder.getView(R.id.tel_contacts_item_number);
                ImageView iv=viewHolder.getView(R.id.tel_contacts_item_iv_title);
//                x.image().bind(iv,item.getImgUrl());
                xUtilsImageUtils.display(iv,item.getImgUrl(),true);
                TextView letter = viewHolder.getView(R.id.tel_contacts_item_letter);
                letter.setVisibility(View.GONE);
                final TextView groupMsg=viewHolder.getView(R.id.tel_contacts_item_tv_groupmsg);
                final Button addBtn=viewHolder.getView(R.id.tel_contacts_item_btn_add);
                final Group group=item.getGroup();
                if(group.getTgId()!=null){//该用户在当前公司.如果存在则显示部门，如果不存在则添加点击按钮
                    groupMsg.setVisibility(View.VISIBLE);
                    addBtn.setVisibility(View.GONE);
                    groupMsg.setText(group.getTgName());
                }else {
                    addBtn.setVisibility(View.VISIBLE);
                    groupMsg.setVisibility(View.GONE);
                    addBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if("1".equals(addnum)){
                                addMemberToCreate(item,addBtn,groupMsg);
                            }else{
                                item.setGroup(group);
                                group.setTgId(currentGid);
                                addMember(item,addBtn,groupMsg);
                                setResult(100);
                            }
                            SearchMember.this.finish();

                        }
                    });
                }
                userName.setText(item.getUserName());
                userTel.setText(item.getUserId());
            }
        };
        memLv.setAdapter(adapter);
    }
    /**
     * 搜索事件触发
     */
    public void searchClick(){
        etUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("change", "onTextChanged: " );
               searchFormServe(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public void searchFormServe(String userId){
        RequestParams params=new RequestParams(Utils.SEARCH_FRIEND_URL);
        params.addParameter("friendId",userId);
        params.addParameter("companyId",MySharePreference.getCurrentCompany(mContext).getTcId());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                users.clear();
                if(!result.equals("nothing")){
                    Log.e("friedId", "onSuccess: "+result);
                    Gson gson=new Gson();
                    List<User> uses=gson.fromJson(result,new TypeToken<List<User>>(){}.getType());
                    users.addAll(uses);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("error", "onError: "+ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
    public void listItemClick(){

    }
    public void addMemberToCreate(final User user, final Button btn, final TextView tv){
        btn.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
        tv.setText("已添加");
        Intent intent=new Intent();
        intent.putExtra("mId",user.getId());
        intent.putExtra("mtel",user.getUserId());
        intent.putExtra("mImg",user.getImgUrl());
        intent.putExtra("mName",user.getUserName());
        if(user.getId()==0||"".equals(user.getId())){
            setResult(REQUEST_FAILED,intent);

        }else{
            setResult(REQUEST_SUCCESS,intent);
        }

    }
    public void addMember(final User user, final Button btn, final TextView tv){
        RequestParams param=new RequestParams(Utils.MANAGER_GROUP);
        Gson gson= new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
        String userJson=gson.toJson(user);
        param.addParameter("addMem",userJson);
        x.http().get(param, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("200")){
                    ToastCommom.ToastShow(mContext,null,"添加成功");
                    btn.setVisibility(View.GONE);
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("已添加");
                    Intent intent=new Intent();
                    intent.putExtra("currentId",currentGid);
                    intent.putExtra("userName",user.getUserName());
                    intent.putExtra("tel",user.getUserId());
                    intent.putExtra("imgUrl",user.getImgUrl());
                    intent.setAction(Utils.Receiver.ADD_MEMBER);//通知前一个更新页面
                    sendBroadcast(intent);
                }else {
                    ToastCommom.ToastShow(mContext,null,"添加失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("search", "onError: "+ex );
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
