package com.liu.easyoffice.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liu.easyoffice.Progressbar.LoadDialog;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.ToastCommom;

public class AboutActivity extends AppCompatActivity {

    private RelativeLayout check;
    private Context mcontext;
    private TextView mToolTitle;
    private TextView mToolText;
    private ImageView mToolBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mcontext=this;
        mToolTitle = ((TextView) findViewById(R.id.mtoolbar_text));
        mToolTitle.setText("关于我们");
        mToolText = ((TextView) findViewById(R.id.mtoolbar_tv));
        mToolText.setVisibility(View.GONE);
        mToolBack = ((ImageView) findViewById(R.id.tool_back));
        mToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.this.finish();
            }
        });
        check = ((RelativeLayout) findViewById(R.id.about_check));
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadDialog.show(mcontext,"正在检查当前版本。。。");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LoadDialog.dismiss(mcontext);
                        ToastCommom.ToastShow(mcontext,null,"当前是最新版本哟！");
                    }
                },1500);
            }
        });
    }
}
