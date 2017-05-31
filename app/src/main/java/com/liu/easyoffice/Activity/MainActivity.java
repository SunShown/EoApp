package com.liu.easyoffice.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.easyoffice.Adapter.MyPagerAdapter;
import com.liu.easyoffice.MyView.MyViewPager;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.pojo.User;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_UPDATE_USER = 1;//设置用户信息
    private Toolbar toolBar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView slideLv;
    private MyViewPager viewPager;
    private RadioGroup bottomRg;
    private ImageView userImg;
    private TextView userName;
    private RelativeLayout drawer_left_top;
    private Context mcontext;
    //从preference读取
    String Token;
    String userId;
    String name;
    String portraitUri;
    String position;
    int sex;
    boolean isUserCard=true;
    private ImageView userSex;
    private TextView userPosition;
    private RelativeLayout create_company;
    private RelativeLayout create_departure;
    private RelativeLayout team;
    private RelativeLayout document;
    private RelativeLayout enjoy;
    private RelativeLayout usercard;
    private RelativeLayout communicate;
    private TextView setting;
    private long firstTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        msgInit();

        initDrawerClickListener();
//       设置点击左侧菜单侧滑按钮，弹出侧滑菜单
        toolBar.setTitle("易工作");
        if (toolBar != null) {
            setSupportActionBar(toolBar);
        }
        toolBar.setNavigationIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolBar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

//        viewpager+radiobtn 实现页面切换
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);//初始界面
        bottomRg.check(R.id.rbtn_msg);
        bottomRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_msg:
                        viewPager.setCurrentItem(0, false);//false为禁止动画
                        break;
                    case R.id.rbtn_work:
                        viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rbtn_contactor:
                        viewPager.setCurrentItem(2, false);
                }
            }
        });
        viewPager.setOnPageChangeListener(new MyViewPager.OnPageChangeListener() {//滑动页面切换底部按钮
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomRg.check(R.id.rbtn_msg);
                        break;
                    case 1:
                        bottomRg.check(R.id.rbtn_work);
                        break;
                    case 2:
                        bottomRg.check(R.id.rbtn_contactor);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initDrawerClickListener() {
        drawer_left_top.setOnClickListener(this);
        usercard.setOnClickListener(this);
        setting.setOnClickListener(this);
        create_company.setOnClickListener(this);
        team.setOnClickListener(this);
        communicate.setOnClickListener(this);
        enjoy.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void init() {

        toolBar = ((Toolbar) findViewById(R.id.tl_custom));
        mDrawerLayout = ((DrawerLayout) findViewById(R.id.drawer));
        slideLv = ((ListView) findViewById(R.id.slide_lv));
        userImg = ((ImageView) findViewById(R.id.head));//用户头像
        userName = ((TextView)findViewById(R.id.name));//用户姓名
        userSex = ((ImageView) findViewById(R.id.sex));//用户性别
        userPosition = ((TextView) findViewById(R.id.position));//用户职位
        viewPager = ((MyViewPager) findViewById(R.id.body_view_pager));//viewPager控件
        bottomRg = ((RadioGroup) findViewById(R.id.bottom_rg));//底部radioGroup
        drawer_left_top = ((RelativeLayout) findViewById(R.id.drawer_left_top));

        //列表信息初始化
        create_company = ((RelativeLayout) findViewById(R.id.drawer_left_list_company));//创建公司
        create_departure = ((RelativeLayout) findViewById(R.id.drawer_left_list_create));//创建部门
        team = ((RelativeLayout) findViewById(R.id.drawer_left_list_team));//我的团队
        document = ((RelativeLayout) findViewById(R.id.drawer_left_list_document));//我的文件
        enjoy = ((RelativeLayout) findViewById(R.id.drawer_left_list_enjoy));//分享给朋友
        usercard = ((RelativeLayout) findViewById(R.id.drawer_left_list_usercard));//我的名片
        communicate = ((RelativeLayout) findViewById(R.id.drawer_left_list_communicate));//联系我们
        setting = ((TextView) findViewById(R.id.drawer_left_list_setting));//设置
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.drawer_left_list_enjoy:
                Intent intentadvertise=new Intent(MainActivity.this,AdviseForReturn.class);
                startActivity(intentadvertise);
                break;
            case R.id.drawer_left_list_communicate:
                Intent intentabout=new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intentabout);
                break;
            case R.id.drawer_left_list_team:
                Intent intentmy=new Intent(MainActivity.this,MyCompany.class);
                startActivity(intentmy);
                break;
            //修改个人信息
            case R.id.drawer_left_top:
                Intent intent=new Intent(MainActivity.this,PersonalInformationActivity.class);
                startActivityForResult(intent,REQUEST_UPDATE_USER);
                break;
            case R.id.drawer_left_list_company:
                Log.e("MyCompany", "onClick: "+MySharePreference.getCurrentCompany(getApplicationContext()).getTcId());
                if(MySharePreference.getCurrentCompany(getApplicationContext()).getTcId()==-1L){//没有公司
                    Intent intentCompany=new Intent(MainActivity.this,CreateCompany.class);
                    startActivity(intentCompany);
                }else {
                    ToastCommom.ToastShow(getApplicationContext(),null,"你当前已所属于公司，不能创建新公司");
                }
                break;
            //我的名片
            case R.id.drawer_left_list_usercard:
                Intent intent2=new Intent();
                intent2.putExtra("userId",userId);
                intent2.putExtra("userCard",isUserCard);
                intent2.setClass(MainActivity.this,ContactMsgActivity.class);
                startActivity(intent2);
                break;
            case R.id.drawer_left_list_setting:
                Intent intent3=new Intent();
                intent3.setClass(MainActivity.this,SettingActivity.class);
                startActivity(intent3);
                break;
        }
    }
    /**
     * 信息初始化 用户头像 用户姓名等
     */
    private void msgInit() {
        SharedPreferences preferences = getApplication().getSharedPreferences("user", MODE_PRIVATE);
        Token = preferences.getString("userToken", null);
        Log.e("liufeixuan", "msgInit: " +Token);
        userId = preferences.getString("userId", "0");
        name = preferences.getString("userName", "用户不存在");
        portraitUri = preferences.getString("imgUrl", "");
        sex=preferences.getInt("userSex",0);
        position=preferences.getString("userPosition","暂无");
        userName.setText(name);
        if(sex==1){
            userSex.setBackgroundResource(R.mipmap.sex_women);
        }
        userPosition.setText("职务:"+position);
        rongConnect();
        xUtilsImageUtils.display(userImg,portraitUri,true);
    }
    /**
     * 融云连接
     */
    private void rongConnect() {
        Log.e("rongCloud", "rongConnect: "+RongIM.getInstance());
        RongIM.connect(Token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.e("main", "tokenCorrect");
            }

            @Override
            public void onSuccess(String s) {
                Log.e("main", s);
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(userId, name, Uri.parse(Utils.UPLOAD_IMG+portraitUri)));//设置头像
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("main", errorCode.getMessage() + "");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=new Intent();
        switch (item.getItemId()){
            case R.id.menu_create_chat:
                intent.setClass(this,CreateGroup.class);
                startActivity(intent);
                break;

            case R.id.menu_create_app:
                intent.setClass(this,ApplyAcitity.class);
                startActivity(intent);
                break;
        }
        Log.e("menu",item.getTitle()+"");
        Toast.makeText(this,item.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_UPDATE_USER:
                if(data!=null){//防止返回按钮的使用

                    User user= (User) data.getSerializableExtra("user");
                    if(user.getImgUrl()!=null){
                        xUtilsImageUtils.display(userImg,user.getImgUrl(),true);
                        Log.e("img", "onActivityResult: "+user.getImgUrl());
                    }
                    userName.setText(user.getUserName());
                    if(user.getSex()==1){
                        userSex.setBackgroundResource(R.mipmap.sex_women);
                    }
                    if(user.getUserPosition()!=null){
                        userPosition.setText(user.getUserPosition());
                    }
                    break;
                }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();

        if (secondTime - firstTime > 1500) {
            Toast.makeText(this, "再点一次退出客户端", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean isDestroyed() {

        return super.isDestroyed();
    }
}
