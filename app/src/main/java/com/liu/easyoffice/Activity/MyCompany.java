package com.liu.easyoffice.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.DateUtil;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.pojo.Company;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class MyCompany extends AppCompatActivity {
    private TextView mToolTitle;
    private TextView mToolText;
    private ImageView mToolBack;
    private TextView name;
    private TextView type;
    private TextView time;
    private TextView area;
    private Context context;
    private Company company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_company);
        context=this;
        initView();
        initData();
    }

    private void initData() {
        SharedPreferences sp=context.getSharedPreferences("company", MODE_PRIVATE);
        Long id=sp.getLong("tcId",-1);
        if(id==-1||id==0){
            ToastCommom.ToastShow(context,null,"你还不属于任何公司哦！！");
            MyCompany.this.finish();
            return;
        }
        RequestParams rp=new RequestParams(Utils.MY_COMPANY);
        rp.addParameter("id",id);
        System.out.println(id+"这是tcId");
        x.http().get(rp, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                company=gson.fromJson(result,Company.class);
                System.out.println(company.getTctype());
                name.setText(company.getTcName());
                type.setText(company.getTctype());
                time.setText(DateUtil.dateToString(company.getCreateTime()));
                area.setText(company.getTcarea());
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

    private void initView() {
        mToolTitle = ((TextView) findViewById(R.id.mtoolbar_text));
        mToolTitle.setText("我的公司");
        mToolText = ((TextView) findViewById(R.id.mtoolbar_tv));
        mToolText.setVisibility(View.GONE);
        mToolBack = ((ImageView) findViewById(R.id.tool_back));
        mToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCompany.this.finish();
            }
        });
        name = ((TextView) findViewById(R.id.my_company_name));
        type = ((TextView) findViewById(R.id.my_company_type));
        time = ((TextView) findViewById(R.id.my_company_time));
        area = ((TextView) findViewById(R.id.my_company_area));
    }
}
