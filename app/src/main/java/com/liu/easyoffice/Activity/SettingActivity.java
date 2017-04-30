package com.liu.easyoffice.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MySharePreference;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout modify;
    private RelativeLayout message;
    private RelativeLayout about;
    private RelativeLayout exit;
    private TextView v_title_tv;
    private TextView mToolTitle;
    private TextView mToolText;
    private ImageView mToolBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();
    }

    private void init() {
        mToolTitle = ((TextView) findViewById(R.id.mtoolbar_text));
        mToolTitle.setText("设置");
        mToolText = ((TextView) findViewById(R.id.mtoolbar_tv));
        mToolText.setVisibility(View.GONE);
        mToolBack = ((ImageView) findViewById(R.id.tool_back));
        mToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });
        modify = ((RelativeLayout) findViewById(R.id.modify_password));
        exit = ((RelativeLayout) findViewById(R.id.exit));
        modify.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        View v_title= LayoutInflater.from(getApplicationContext()).inflate(R.layout.alertdialog_title,null);
        v_title_tv = ((TextView) v_title.findViewById(R.id.txtPatient));
        v_title_tv.setText("退出");
        switch (v.getId()){
            case R.id.modify_password:
                Intent intent1=new Intent(SettingActivity.this,ModifyPwdActivity.class);
                startActivity(intent1);
                break;
            case R.id.exit:
                new AlertDialog.Builder(this)
                        .setCustomTitle(v_title)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(SettingActivity.this,Login2Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//清空栈
                                startActivity(intent);
                                MySharePreference.clearSharePreference(SettingActivity.this);
                                finish();
                                Log.e("isFirst", "退出程序 "+MySharePreference.isFirst(SettingActivity.this));
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }

    }
}
