package com.liu.easyoffice.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liu.easyoffice.Activity.ManagerPart.TelContactsActivity;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.DividerItemDecoration;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.city.ScrollerNumberPicker;
import com.liu.easyoffice.pojo.Group;
import com.liu.easyoffice.pojo.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class CreateCompany extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecycler;
    private HomeAdapter mAdapter;
    private List<User> mDatas;
    private User user;
    private RelativeLayout create_company_add_member_re;
    private Context mcontext;
    private RelativeLayout create_company_area;
    private TextView create_company_area_tv;
    private static int num=1;
    private TextView create_company_person_num_tv;
    private Button create_company_submit;
    private EditText create_company_name_tv;
    private EditText create_company_type_title_tv;
    private TextView mtoolbar_text;
    private TextView mtoolbar_tv;
    private String name;
    private String portraitUri;
    private TextView v_title_tv;
    private Long companyId;
    private Long id;
    private ImageView mToolBack;
    public static final int REQUEST_TEL = 1;
    public static final int REQUEST_B = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_company);
        mcontext=this;
        mcontext=this;
        init();
        initData();
    }

    private void initData() {
        SharedPreferences preferences = getApplication().getSharedPreferences("user", MODE_PRIVATE);
        name = preferences.getString("userName", "用户不存在");
        portraitUri = preferences.getString("imgUrl", "");
        id=preferences.getLong("id",0);
        companyId= MySharePreference.getCurrentCompany(getApplicationContext()).getTcId();
        user=new User();
        user.setId(id);
        user.setUserName(name);
        user.setImgUrl(portraitUri);
        mDatas=new ArrayList<>();
        mDatas.add(user);
        System.out.println(mDatas);
    }

    private void init() {
        mtoolbar_text = ((TextView) findViewById(R.id.mtoolbar_text));
        mtoolbar_text.setText("创建公司");
        mtoolbar_tv = ((TextView) findViewById(R.id.mtoolbar_tv));
        mtoolbar_tv.setVisibility(View.GONE);
        mToolBack = ((ImageView) findViewById(R.id.tool_back));
        mToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCompany.this.finish();
            }
        });
        //获取个人信息

        mRecycler = ((RecyclerView) findViewById(R.id.create_company_recycler));
        create_company_submit = ((Button) findViewById(R.id.create_company_submit));
        create_company_name_tv = ((EditText) findViewById(R.id.create_company_name_tv));
        create_company_type_title_tv = ((EditText) findViewById(R.id.create_company_type_title_tv));
        create_company_person_num_tv = ((TextView) findViewById(R.id.create_company_person_num_tv));
        create_company_add_member_re = ((RelativeLayout) findViewById(R.id.create_company_add_member_re));
        create_company_area = ((RelativeLayout) findViewById(R.id.create_company_area));
        create_company_area_tv = ((TextView) findViewById(R.id.create_company_area_tv));
        create_company_area.setOnClickListener(this);
        create_company_submit.setOnClickListener(this);
        create_company_add_member_re.setOnClickListener(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter=new HomeAdapter());
        mRecycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void onClick(View v) {
        View v_title=LayoutInflater.from(getApplicationContext()).inflate(R.layout.alertdialog_title,null);
        v_title_tv = ((TextView) v_title.findViewById(R.id.txtPatient));
        switch (v.getId()){
            case R.id.create_company_add_member_re:
//                mAdapter.addData(1);
                v_title_tv.setText("添加成员");
                final String sexItems[]={"手动输入成员信息","从手机通讯录添加"};
                AlertDialog alertDialog;
                ListAdapter itemlist = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sexItems);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setCustomTitle(v_title);
                final Intent intent=new Intent();
                builder1.setAdapter(itemlist, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            intent.setClass(CreateCompany.this,SearchMember.class);
                            intent.putExtra("currentGid",companyId);//传递当前id
                            intent.putExtra("addnum","1");
                            startActivityForResult(intent,REQUEST_TEL);
                        }else if(which==1){
                            intent.setClass(CreateCompany.this, TelContactsActivity.class);
                            intent.putExtra("currentGid",companyId);//传递当前id
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                });
                alertDialog=builder1.show();
                break;
            case R.id.create_company_area:
                AlertDialog.Builder builder=new AlertDialog.Builder(CreateCompany.this);
                View view = LayoutInflater.from(CreateCompany.this).inflate(R.layout.addressdialog, null);
                builder.setView(view);
                LinearLayout addressdialog_linearlayout = (LinearLayout)view.findViewById(R.id.addressdialog_linearlayout);
                final ScrollerNumberPicker provincePicker = (ScrollerNumberPicker)view.findViewById(R.id.province);
                final ScrollerNumberPicker cityPicker = (ScrollerNumberPicker)view.findViewById(R.id.city);
                final ScrollerNumberPicker counyPicker = (ScrollerNumberPicker)view.findViewById(R.id.couny);
                final AlertDialog dialog = builder.show();
                addressdialog_linearlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        create_company_area_tv.setText(provincePicker.getSelectedText()+"-"+cityPicker.getSelectedText()+"-"+counyPicker.getSelectedText());
                        Log.i("kkkk",provincePicker.getSelectedText()+cityPicker.getSelectedText()+counyPicker.getSelectedText());
                        dialog.dismiss();

                    }
                });
                break;
            case R.id.create_company_submit:
                String c_name=create_company_name_tv.getText().toString().trim();
                String c_type=create_company_type_title_tv.getText().toString().trim();
                String c_area=create_company_area_tv.getText().toString().trim();
                RequestParams rp=new RequestParams(Utils.CREATE_COMPANY);
                rp.addParameter("name",c_name);
                rp.addParameter("type",c_type);
                rp.addParameter("userid",MySharePreference.getCurrentUser(CreateCompany.this).getId());
                rp.addParameter("area",c_area);
                Gson gson=new Gson();
                String member=gson.toJson(mDatas);
                System.out.println(mDatas);
                rp.addParameter("member",member);
                x.http().get(rp, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("s", "onSuccess: ");
                        Gson gsonGroup = new Gson();
                        Group groupGson = gsonGroup.fromJson(result, Group.class);
                        Log.e("groupGson", "onSuccess: " + groupGson);
                        MySharePreference.setCurrentGroup(mcontext, groupGson);
                        MySharePreference.setCurrentComapny(mcontext, groupGson.getCompany());
                        ToastCommom.ToastShow(mcontext,null,"创建成功");
//                        new ContactsFragment().initMsg();//更新
                        CreateCompany.this.finish();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_TEL:
                switch(resultCode){
                    case SearchMember.REQUEST_SUCCESS:
                        user=new User();
                        long a=data.getLongExtra("mId",0);
                        user.setId(data.getLongExtra("mId",0));
                        user.setUserName(data.getStringExtra("mName"));
                        user.setImgUrl(data.getStringExtra("mImg"));
                        for (int i=0;i<mDatas.size();i++){
                            if (a==mDatas.get(i).getId()){
                                ToastCommom.ToastShow(mcontext,null,"该成员已经添加过！！！");
                                return;
                            }
                        }
                        mAdapter.addData(mDatas.size());
                        System.out.println(mDatas.size());
                        ToastCommom.ToastShow(mcontext,null,"添加成功！！！");
                        break;
                    case SearchMember.REQUEST_FAILED:
                        ToastCommom.ToastShow(mcontext,null,"添加失败！！！");
                }
        }
    }

    private class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder=new MyViewHolder(LayoutInflater.from(CreateCompany.this).inflate(R.layout.recyclerview_item,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(HomeAdapter.MyViewHolder holder, final int position) {

//            xUtilsImageUtils.display(holder.recycle_ItemImage,R.mipmap.user_card_test_head,true);
            if(position==0){
                holder.create_company_del.setVisibility(View.GONE);
                holder.ItemText.setText(mDatas.get(0).getUserName());
                xUtilsImageUtils.display(holder.recycle_ItemImage,portraitUri,true);
            }else{
                holder.ItemText.setText(mDatas.get(position).getUserName());
                xUtilsImageUtils.display(holder.recycle_ItemImage,user.getImgUrl(),true);
                holder.create_company_del.setImageResource(R.drawable.create_conpany_delete_selector);
            }


            holder.create_company_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeData(position);
                    System.out.println(mDatas);
                    ToastCommom.ToastShow(mcontext,null,"删除了"+position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }
        public void addData(int position) {
            mDatas.add(position, user);
            create_company_person_num_tv.setText("团队成员（"+(++num)+"人）");
            notifyItemInserted(position);
            System.out.println(mDatas);
        }

        public void removeData(int position) {
            System.out.println(mDatas.size());
            mDatas.remove(position);
            System.out.println(mDatas.size());
            create_company_person_num_tv.setText("团队成员（"+(--num)+"人）");
            notifyItemRemoved(position);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView ItemText;
            ImageView recycle_ItemImage;
            ImageView create_company_del;
                public MyViewHolder(View view) {
                    super(view);
                    ItemText = (TextView) view.findViewById(R.id.ItemText);
                    recycle_ItemImage = (ImageView) view.findViewById(R.id.recycle_ItemImage);
                    create_company_del = (ImageView) view.findViewById(R.id.create_company_del);
                }
        }
    }

}
