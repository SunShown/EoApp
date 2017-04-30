package com.liu.easyoffice.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.widget.PathTextView;

public class IndexActivity extends AppCompatActivity {
    private Context mContext;
    private PathTextView mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        mContext=this;
        mPath = ((PathTextView) findViewById(R.id.path_text));
        mPath.init("EO");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               isFirst();
            }
        },3000);

    }
    /**
     * 判断用户是不是第一次登录
     * @return
     */
    private  void isFirst(){
        SharedPreferences sp = getApplication().getSharedPreferences("user", MODE_PRIVATE);
        String name=sp.getString("userName","");
        Intent intent=new Intent();
        if(MySharePreference.isFirst(mContext)){
            intent.setClass(IndexActivity.this,LeaderActivity.class);
        }else {
            if ("".equals(name)||name==null){
                intent.setClass(IndexActivity.this,Login2Activity.class);
            }else{
                intent.setClass(IndexActivity.this,MainActivity.class);
            }
        }
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.activity_fade_in,R.anim.activity_fade_out);
    }
}
