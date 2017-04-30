package com.liu.easyoffice.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.liu.easyoffice.Progressbar.LoadDialog;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.widget.ClearWriteEditText;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class ModifyPwdActivity extends AppCompatActivity {

    private ClearWriteEditText newPwd;
    private ClearWriteEditText confirm_newPwd;
    private Button submit;
    private Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        mcontext=this;
        init();
    }

    private void init() {
        newPwd = ((ClearWriteEditText) findViewById(R.id.new_password));
        confirm_newPwd = ((ClearWriteEditText) findViewById(R.id.confirm_new_password));
        submit= ((Button) findViewById(R.id.modify_password_submit));
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadDialog.show(mcontext,"正在修改。。。");
                if(!isPassword(newPwd.getText().toString().trim())){
                    System.out.println("hahahaha");
                    newPwd.setShakeAnimation();
                    ToastCommom.ToastShow(mcontext,null,"密码需要大于6位，并仅允许字母及数字组合，且必须含一位以上大写字母，亲！！！");
                    LoadDialog.dismiss(mcontext);
                    return;
                }
                if(!(newPwd.getText().toString().trim().equals(confirm_newPwd.getText().toString().trim()))){
                    confirm_newPwd.setShakeAnimation();
                    ToastCommom.ToastShow(mcontext,null,"两次密码不一致！！");
                    LoadDialog.dismiss(mcontext);
                    return;
                }
                SharedPreferences sp=getApplicationContext().getSharedPreferences("user",MODE_PRIVATE);
                String telnum=sp.getString("userId",null);
                RequestParams rp=new RequestParams(Utils.NEWPASSWORD);
                rp.addParameter("telnum",telnum);
                rp.addParameter("password",newPwd.getText().toString().trim());
                x.http().get(rp, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if((!("".equals(result)))&&result!=null){
                            LoadDialog.dismiss(mcontext);
                            ToastCommom.ToastShow(mcontext,null,"修改密码成功！！！");
                            ModifyPwdActivity.this.finish();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        LoadDialog.dismiss(mcontext);
                        ToastCommom.ToastShow(mcontext,null,"网络访问失败");
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
    public static  boolean isPassword(String password){
        String pwd="^(?=.*?[A-Z])[a-zA-Z0-9]{7,}$";
        if(TextUtils.isEmpty(password)){
            return false;
        }else {
            return password.matches(pwd);
        }
    }
}
