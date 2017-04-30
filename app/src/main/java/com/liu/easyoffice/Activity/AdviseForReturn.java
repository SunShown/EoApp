package com.liu.easyoffice.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.easyoffice.Progressbar.LoadDialog;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.ToastCommom;

import java.util.List;

import me.iwf.photopicker.widget.MultiPickResultView;

public class AdviseForReturn extends AppCompatActivity {

    private  MultiPickResultView recyclerView;
    private List<String> piclist;
    private Button submit;
    private Context mcontext;
    private TextView mToolTitle;
    private TextView mToolText;
    private ImageView mToolBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advise_for_return);
        mcontext=this;
        mToolTitle = ((TextView) findViewById(R.id.mtoolbar_text));
        mToolTitle.setText("意见反馈");
        mToolText = ((TextView) findViewById(R.id.mtoolbar_tv));
        mToolText.setVisibility(View.GONE);
        mToolBack = ((ImageView) findViewById(R.id.tool_back));
        mToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdviseForReturn.this.finish();
            }
        });
        recyclerView = (MultiPickResultView) findViewById(R.id.recycler_advise);
        recyclerView.init(this,MultiPickResultView.ACTION_SELECT,null);
        submit = ((Button) findViewById(R.id.advise_submit));
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadDialog.show(mcontext,"正在提交。。。");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LoadDialog.dismiss(mcontext);
                        ToastCommom.ToastShow(mcontext,null,"感谢你的建议和配合！");
                        AdviseForReturn.this.finish();
                    }
                },1500);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        recyclerView.onActivityResult(requestCode,resultCode,data);
        piclist=recyclerView.getPhotos();
    }
}
