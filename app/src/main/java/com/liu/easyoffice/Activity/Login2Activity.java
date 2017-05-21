package com.liu.easyoffice.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liu.easyoffice.JPush.MyJPush;
import com.liu.easyoffice.MyView.CountDownTimerUtils;
import com.liu.easyoffice.Progressbar.LoadDialog;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.FileStore;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.pojo.Company;
import com.liu.easyoffice.pojo.Group;
import com.liu.easyoffice.pojo.User;
import com.liu.easyoffice.widget.ClearWriteEditText;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.SocketTimeoutException;

import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.SystemConversationProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class Login2Activity extends AppCompatActivity implements View.OnClickListener {
    private ClearWriteEditText mUserName, mPassWord;
    // 登录
    private Button mButtonLogin;
    // 忘记密码 注册 左侧title  又侧title
    private TextView mRegister, mButtonRegist;
    // 大 logo 图片  背景图片
    private ImageView mImgBackgroud;

//    private String sUserName, sPassword, oldUserName, oldPassWord;

    //    private SharedPreferences sp;
    protected Context mcontext;
    private String psd, id;
    private LinearLayout regist_linear;
    private LinearLayout login_linear;
    private TextView btn_forget;
    private LinearLayout forget_linear;
    private LinearLayout new_psd_linear;
    private Button btn_confirm;
    private Button btn_regist;
    private ClearWriteEditText new_psd_et;
    private ClearWriteEditText new_psd_confirm_dt;
    private Button new_psd_enter;
    private String new_psd_psd, new_psd_check;
    private TextView forget_return;
    private TextView confirm_return;
    private TextView regist_return;
    private Button regist_check_sendmessage;
    private Button forget_sendmessage;
    private ClearWriteEditText regist_username;
    private ClearWriteEditText regist_password;
    private ClearWriteEditText regist_check;
    private ClearWriteEditText forget_username;
    private ClearWriteEditText forget_check;
    private String code = "";
    private String code1 = "";
    private String selectTel = "";
    public final static boolean aa = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mcontext = this;

        initView();
        checlkLogin();
    }

    private void checlkLogin() {
        mButtonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                id=mUserName.getText().toString().trim();
                psd=mPassWord.getText().toString().trim();
                requestForCom(id);
                if(TextUtils.isEmpty(id)){
                    ToastCommom.ToastShow(getApplicationContext(),null,"手机号不可以为空");
                    mUserName.setShakeAnimation();
                    return;
                } else if (TextUtils.isEmpty(psd)) {
                    ToastCommom.ToastShow(getApplicationContext(), null, "密码不可以为空");
                    mPassWord.setShakeAnimation();
                    return;
                }
                LoadDialog.show(mcontext, "正在登录。。。");

                final RequestParams requestParams = new RequestParams(Utils.LOGIN_URL);
                requestParams.addParameter(" ", "");
                requestParams.addParameter("userId", id);
                requestParams.addParameter("userPwd", psd);
                requestParams.setConnectTimeout(4000);
                x.http().get(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if ("0".equals(result)) {
                            ToastCommom.ToastShow(getApplicationContext(), null, "账号或者密码错误");
                            LoadDialog.dismiss(mcontext);
                            mPassWord.setShakeAnimation();
                            mUserName.setShakeAnimation();
                            return;
                        }
                        Gson gson = new Gson();
                        User user = gson.fromJson(result, User.class);
                        if (id.equals(user.getUserId().trim()) && psd.equals(user.getUserPwd())) {
                            SharedPreferences sp = getApplication().getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putLong("id", user.getId());
                            Log.e("id", "onSuccess: "+user.getId() );
                            editor.putString("userId", user.getUserId());
                            editor.putString("userName", user.getUserName());
                            editor.putString("userPassword", user.getUserId());
                            editor.putString("userToken", user.getUserToken());
                            editor.putString("imgUrl", user.getImgUrl());
                            editor.putString("userBirth", user.getBirthday().toString() + "");
                            editor.putString("userAddress", user.getAddress());
                            editor.putInt("userSex", user.getSex());
                            editor.putString("userPosition", user.getUserPosition());
                            editor.commit();
                            ToastCommom.ToastShow(getApplicationContext(), null, "欢迎使用" + user.getUserName());
                            MySharePreference.setNotFirst(mcontext);
                            MyJPush.setAlias(mcontext,user.getId()+"");//注册
                            Intent intent = new Intent(Login2Activity.this, MainActivity.class);
                            startActivity(intent);
                            LoadDialog.dismiss(mcontext);
                            Login2Activity.this.finish();
                        } else {
                            ToastCommom.ToastShow(getApplicationContext(), null, "账号或者密码错误");
                            LoadDialog.dismiss(mcontext);
                            mPassWord.setShakeAnimation();
                            mUserName.setShakeAnimation();
                            return;
                        }
                    }


                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e("login", "onError: " + ex);
//                        LoadDialog.dismiss(mcontext);
                        Log.e("error", "onError: " + ex);
                        if (ex instanceof SocketTimeoutException) {
                            LoadDialog.dismiss(mcontext);
                            ToastCommom.ToastShow(mcontext, null, "请检查网络");
                        }
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });
    }

    private void initView() {
        mUserName = (ClearWriteEditText) findViewById(R.id.app_username_et);
        mPassWord = (ClearWriteEditText) findViewById(R.id.app_password_et);
        mButtonLogin = (Button) findViewById(R.id.app_sign_in_bt);
        mRegister = (TextView) findViewById(R.id.de_login_register);
        mImgBackgroud = (ImageView) findViewById(R.id.de_img_backgroud);
        mButtonRegist = (TextView) findViewById(R.id.de_login_register);
        //界面
        regist_linear = ((LinearLayout) findViewById(R.id.regist_linear));
        login_linear = ((LinearLayout) findViewById(R.id.login_linear));
        forget_linear = ((LinearLayout) findViewById(R.id.forget_linear));
        new_psd_linear = ((LinearLayout) findViewById(R.id.new_psd_linear));
        //确认新密码界面空间的初始化
        new_psd_et = ((ClearWriteEditText) findViewById(R.id.new_psd_psd));
        new_psd_confirm_dt = ((ClearWriteEditText) findViewById(R.id.new_psd_confirm_psd));
        new_psd_enter = ((Button) findViewById(R.id.new_psd_enter));
        //按钮
        btn_forget = ((TextView) findViewById(R.id.de_login_forgot));
        btn_confirm = ((Button) findViewById(R.id.forget_submit));
        btn_regist = ((Button) findViewById(R.id.regist_submit));

        //返回
        forget_return = ((TextView) findViewById(R.id.forget_return));
        confirm_return = ((TextView) findViewById(R.id.confirm_return));
        regist_return = ((TextView) findViewById(R.id.regist_return));
        //发送验证码
        regist_check_sendmessage = ((Button) findViewById(R.id.regist_check_sendmessage));
        forget_sendmessage = ((Button) findViewById(R.id.forget_sendmessage));

        //注册验证码获取控件属性
        regist_username = ((ClearWriteEditText) findViewById(R.id.regist_app_username_et));
        regist_password = ((ClearWriteEditText) findViewById(R.id.regist_app_password_et));
        regist_check = ((ClearWriteEditText) findViewById(R.id.regist_check));
        //忘记密码获取控件属性
        forget_username = ((ClearWriteEditText) findViewById(R.id.forget_username));
        forget_check = ((ClearWriteEditText) findViewById(R.id.forget_check));
        //新密码界面获取控件
        regist_check_sendmessage.setOnClickListener(this);
        forget_sendmessage.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mUserName.setOnClickListener(this);
        mPassWord.setOnClickListener(this);
        mButtonRegist.setOnClickListener(this);
        btn_forget.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        btn_regist.setOnClickListener(this);
        new_psd_enter.setOnClickListener(this);
        forget_return.setOnClickListener(this);
        confirm_return.setOnClickListener(this);
        regist_return.setOnClickListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(Login2Activity.this, R.anim.translate_anim);
                mImgBackgroud.startAnimation(animation);
            }
        }, 200);
    }

    @Override
    public void onClick(View v) {
        final Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.ad_left_in);
        final Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.ad_right_out);
        final Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.ad_right_in);
        final Animation animation4 = AnimationUtils.loadAnimation(this, R.anim.ad_left_out);
        final Animation animation5 = AnimationUtils.loadAnimation(this, R.anim.pull_in);
        final Animation animation6 = AnimationUtils.loadAnimation(this, R.anim.pull_out);
        switch (v.getId()) {
            //忘记密码发送手机验证码
            case R.id.forget_sendmessage:
                //判断手机合法性
                if (!isMobile(forget_username.getText().toString().trim())) {
                    forget_username.setShakeAnimation();
                    ToastCommom.ToastShow(mcontext, null, "手机号不合法");
                    return;
                }
                final CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(forget_sendmessage, 60000, 1000);
                mCountDownTimerUtils.start();
                final RequestParams forget_rp = new RequestParams(Utils.REGIST_MESSAGE);
                forget_rp.addParameter("telnum", forget_username.getText().toString().trim());
                x.http().get(forget_rp, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        code1 = result;
                        selectTel = forget_username.getText().toString().trim();
                        System.out.println(code1);
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
                break;
            //注册发送短信验证码
            case R.id.regist_check_sendmessage:
                //判断手机合法性
                if (!isMobile(regist_username.getText().toString().trim())) {
                    regist_username.setShakeAnimation();
                    ToastCommom.ToastShow(mcontext, null, "手机号不合法");
                    return;
                }
                CountDownTimerUtils mCountDownTimerUtils1 = new CountDownTimerUtils(regist_check_sendmessage, 60000, 1000);
                mCountDownTimerUtils1.start();
                final RequestParams rp = new RequestParams(Utils.REGIST_MESSAGE);
                rp.addParameter("telnum", regist_username.getText().toString().trim());
                x.http().get(rp, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        code = result;
                        System.out.println(code);
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


                break;
            case R.id.regist_return:
                regist_linear.setAnimation(animation2);
                regist_linear.setVisibility(View.GONE);
                login_linear.setAnimation(animation1);
                login_linear.setVisibility(View.VISIBLE);
                mButtonRegist.setAnimation(animation5);
                mButtonRegist.setVisibility(View.VISIBLE);
                break;
            case R.id.confirm_return:
                new_psd_linear.setAnimation(animation2);
                new_psd_linear.setVisibility(View.GONE);
                forget_linear.setAnimation(animation1);
                forget_linear.setVisibility(View.VISIBLE);
                break;
            case R.id.forget_return:
                forget_linear.setAnimation(animation2);
                forget_linear.setVisibility(View.GONE);
                login_linear.setAnimation(animation1);
                login_linear.setVisibility(View.VISIBLE);
                mButtonRegist.setAnimation(animation5);
                mButtonRegist.setVisibility(View.VISIBLE);
                break;
            //注册跳转
            case R.id.de_login_register:
                login_linear.startAnimation(animation4);
                login_linear.setVisibility(View.GONE);
                regist_linear.setAnimation(animation3);
                regist_linear.setVisibility(View.VISIBLE);
                mButtonRegist.setAnimation(animation6);
                mButtonRegist.setVisibility(View.GONE);

                break;
            //注册提交
            case R.id.regist_submit:
                if (!isPassword(regist_password.getText().toString().trim())) {
                    regist_password.setShakeAnimation();
                    ToastCommom.ToastShow(mcontext, null, "密码需要大于6位，并仅允许字母及数字组合，且必须含一位以上大写字母，亲！！！");
                    return;
                }
                String number = regist_username.getText().toString().trim();
                RequestParams rp_isExist = new RequestParams(Utils.TELEXIST);
                rp_isExist.addParameter("telnum", number);
                x.http().get(rp_isExist, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result.equals("0")) {
                            if (regist_check.getText().toString().trim() == null || regist_check.getText().toString().trim().equals("")) {
                                ToastCommom.ToastShow(mcontext, null, "验证码不能为空！！！");
                                regist_check.setShakeAnimation();
                                return;
                            }
                            if (code.equals(regist_check.getText().toString().trim())) {
                                User user = new User();
                                user.setUserId(regist_username.getText().toString().trim());
                                user.setUserPwd(regist_password.getText().toString().trim());
                                Gson gson = new Gson();
                                String registJson = gson.toJson(user);
                                RequestParams rp1 = new RequestParams(Utils.REGIST);
                                System.out.println(Utils.REGIST);
                                rp1.addParameter("registJson", registJson);
                                System.out.println(rp1);
                                x.http().get(rp1, new Callback.CommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        System.out.println(result);
                                        if (result != null) {
                                            Gson gson2 = new Gson();
                                            User user2 = gson2.fromJson(result, User.class);
                                            ToastCommom.ToastShow(mcontext, null, "注册成功");
                                            SharedPreferences sp = getApplication().getSharedPreferences("user", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putString("userId", user2.getUserId());
                                            editor.putLong("id", user2.getId());
                                            editor.putString("userName", user2.getUserName());
                                            editor.putString("userPassword", user2.getUserPwd());
                                            editor.putString("userToken", user2.getUserToken());
                                            editor.putString("imgUrl", user2.getImgUrl());
                                            editor.commit();
                                            MySharePreference.setNotFirst(mcontext);
                                            MyJPush.setAlias(mcontext,user2.getId()+"");//注册
                                            ToastCommom.ToastShow(getApplicationContext(), null, "欢迎使用" + user2.getUserName());
                                            Intent intent = new Intent(Login2Activity.this, MainActivity.class);
                                            startActivity(intent);
                                            Login2Activity.this.finish();
                                        } else {
                                            ToastCommom.ToastShow(mcontext, null, "注册失败");
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable ex, boolean isOnCallback) {
                                        System.out.println("+++++++++++++++++++++" + ex.getMessage());
                                    }

                                    @Override
                                    public void onCancelled(CancelledException cex) {

                                    }

                                    @Override
                                    public void onFinished() {

                                    }
                                });
                            } else {
                                ToastCommom.ToastShow(mcontext, null, "验证码错误");
                            }
                        } else {
                            regist_username.setShakeAnimation();
                            ToastCommom.ToastShow(mcontext, null, "该号码已经注册过了,亲！！");
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
                break;
            //跳转忘记密码
            case R.id.de_login_forgot:
                login_linear.startAnimation(animation4);
                login_linear.setVisibility(View.GONE);
                forget_linear.startAnimation(animation3);
                forget_linear.setVisibility(View.VISIBLE);
                mButtonRegist.setAnimation(animation6);
                mButtonRegist.setVisibility(View.GONE);
                break;
            //忘记密码提交，跳转新密码
            case R.id.forget_submit:
                String telnumber = forget_username.getText().toString().trim();
                RequestParams rp_isExist_forget = new RequestParams(Utils.TELEXIST);
                rp_isExist_forget.addParameter("telnum", telnumber);
                x.http().get(rp_isExist_forget, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        System.out.println(result);
                        if (result.equals("0")) {
                            ToastCommom.ToastShow(mcontext, null, "您还不是我们的用户，请先注册，亲！！");
                            forget_username.setShakeAnimation();
                            return;
                        } else {
                            if (forget_check.getText().toString().trim() == null || forget_check.getText().toString().trim().equals("")) {
                                ToastCommom.ToastShow(mcontext, null, "验证码不能为空！！！");
                                System.out.println("hhahaha");
                                forget_check.setShakeAnimation();
                                return;
                            }
                            if (!(code1.equals(forget_check.getText().toString().trim()))) {
                                ToastCommom.ToastShow(mcontext, null, "验证码错误");
                                forget_check.setShakeAnimation();
                                return;
                            }
                            forget_linear.startAnimation(animation4);
                            forget_linear.setVisibility(View.GONE);
                            new_psd_linear.startAnimation(animation3);
                            new_psd_linear.setVisibility(View.VISIBLE);
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


                break;
            //新密码的输入界面
            case R.id.new_psd_enter:
                if (!isPassword(new_psd_et.getText().toString().trim())) {
                    new_psd_et.setShakeAnimation();
                    ToastCommom.ToastShow(mcontext, null, "密码需要大于6位，并仅允许字母及数字组合，且必须含一位以上大写字母，亲！！！");
                    return;
                }
                new_psd_psd = new_psd_et.getText().toString().trim();
                new_psd_check = new_psd_confirm_dt.getText().toString().trim();
                System.out.println(new_psd_psd + "--------------------------" + new_psd_check);
                if (!(new_psd_psd.equals(new_psd_check))) {
                    new_psd_confirm_dt.setShakeAnimation();
                    ToastCommom.ToastShow(getApplicationContext(), null, "两次密码不一致");
                    return;
                }
                LoadDialog.show(mcontext, "正在上传。。。");
                RequestParams newpassword = new RequestParams(Utils.NEWPASSWORD);
                newpassword.addParameter("telnum", selectTel);
                newpassword.addParameter("password", new_psd_psd);
                x.http().get(newpassword, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        System.out.println(result);
                        if (result == null) {
                            ToastCommom.ToastShow(mcontext, null, "修改失败！！！");
                            LoadDialog.dismiss(mcontext);
                        } else {
                            Gson gson = new Gson();
                            User user_forget = gson.fromJson(result, User.class);
                            SharedPreferences sp = getApplication().getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putLong("id", user_forget.getId());
                            editor.putString("userId", user_forget.getUserId());
                            editor.putString("userName", user_forget.getUserName());
                            editor.putString("userPassword", user_forget.getUserId());
                            editor.putString("userToken", user_forget.getUserToken());
                            editor.putString("imgUrl", user_forget.getImgUrl());
                            editor.putString("userBirth", user_forget.getBirthday().getTime() + "");
                            editor.putString("userAddress", user_forget.getAddress());
                            editor.putString("userSex", user_forget.getSex() + "");
                            editor.putString("userPosition", user_forget.getUserPosition());
                            editor.commit();
                            MySharePreference.setNotFirst(mcontext);
                            ToastCommom.ToastShow(mcontext, null, "修改成功！！！");
                            LoadDialog.dismiss(mcontext);
                            MyJPush.setAlias(mcontext,user_forget.getId()+"");//注册
                            Intent intent2 = new Intent(Login2Activity.this, MainActivity.class);
                            startActivity(intent2);
                            Login2Activity.this.finish();
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
                Intent intent2 = new Intent(Login2Activity.this, MainActivity.class);
                startActivity(intent2);


                break;
        }

    }

    //找到用户所在公司
    private void requestForCom(final String tel) {
        final RequestParams params = new RequestParams(Utils.GET_COMPANY);
        params.addParameter("tel", tel);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("company", "onSuccess: " + result);
                final Gson gson = new Gson();
                Company company = null;
                if (result.equals("null")) {
                    company = new Company();
                    Log.e("company", "暂无公司" );
                    company.setTcId(-1L);
                    company.setTcName("暂无公司");
                } else {
                    company = gson.fromJson(result, Company.class);
                    FileStore fileStore=new FileStore(mcontext,"company.txt");
                    boolean sucess=fileStore.saveObject(company);
                    Company fileCompany= (Company) fileStore.readObject();
                    Log.e("file", "onSuccess: "+fileCompany.getTcName());
                }
                SharedPreferences preferences = getApplication().getSharedPreferences("company", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong("tcId", company.getTcId());
                editor.putString("tcName", company.getTcName());
                editor.commit();
                Log.e("companyiiiii", "onSuccess: "+MySharePreference.getCurrentCompany(mcontext));
                /**
                 * 查询当前第一个组id
                 */
                RequestParams currentGId = new RequestParams(Utils.GET_GROUP_CURRENT_ID);
                currentGId.addParameter("companyId", company.getTcId());
                currentGId.addParameter("parentId", 0);
                x.http().get(currentGId, new CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("groupCUUUU", "onSuccess: "+result);
                        Gson gson1 = new Gson();
                        Group group = gson1.fromJson(result, Group.class);
                        FileStore store=new FileStore(mcontext,"group.txt");
                        store.saveObject(group);
                        Group storeGroup= (Group) store.readObject();
                        Log.e("storeGroup", "onSuccess: "+storeGroup.getTgName());
                        SharedPreferences preferences = getApplication().getSharedPreferences("group", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putLong("tgId", group.getTgId());
                        editor.putString("tgName",group.getTgName());
                        editor.putLong("parentGId",group.getParentTgId());
                        editor.commit();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e("company", "onError: " + ex);
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
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("company", "onError:"+ex );
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
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    public static boolean isPassword(String password) {
        String pwd = "^(?=.*?[A-Z])[a-zA-Z0-9]{7,}$";
        if (TextUtils.isEmpty(password)) {
            return false;
        } else {
            return password.matches(pwd);
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
//        moveTaskToBack(true);
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
        super.onBackPressed();
    }
}
