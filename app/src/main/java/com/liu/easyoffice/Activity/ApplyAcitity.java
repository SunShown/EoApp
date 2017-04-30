package com.liu.easyoffice.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.pojo.Company;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hui on 2016/9/18.
 */
public class ApplyAcitity extends Activity implements AdapterView.OnItemClickListener {
    private GridView applyGv;
//    private int[] icons={R.mipmap.icon_approve_qingjia,R.mipmap.icon_approve_baoxiao,R.mipmap.icon_approve_trip,R.mipmap.icon_approve_buqian,R.mipmap.app_rule_config};
//private String[] names={"请假","报销","出差","补签","流程设置"};
    private int[] icons={R.mipmap.icon_approve_qingjia,R.mipmap.icon_approve_baoxiao,R.mipmap.icon_approve_trip};
    private String[] names={"请假","报销","出差"};
    private ImageView applyIvBack;
    private RadioButton rbtnMyApply;
    private RadioButton apply_rb_receive;

private  long compnyId;

    private ImageView mBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_main);
        init();
        initGridView();

    }

    @Override
    protected void onResume() {

        super.onResume();
        RequestParams params=new RequestParams(Utils.XUTIL_URL+"getcompany");
        params.addParameter("tel",MySharePreference.getCurrentUser(ApplyAcitity.this).getUserId());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
           Gson gson=new Gson();
                Type type=new TypeToken<Company>(){}.getType();
                Company company=gson.fromJson(result,type);
                compnyId=company.getTcId();
                Log.e("applycompnyId",compnyId+"");

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
       //compnyId=MySharePreference.getCurrentCompany(ApplyAcitity.this).getTcId();
        onclick();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.setClass(ApplyAcitity.this,MainActivity.class);
        startActivity(intent);
    }

    private void initGridView() {
        List<Map<String,Object>> grids=new ArrayList<>();
        for (int i = 0; i <icons.length ; i++) {
            Map<String,Object> item=new HashMap<>();
            item.put("grid_item_icon",icons[i]);
            item.put("grid_item_name",names[i]);
            grids.add(item);
        }
        SimpleAdapter gridAdapter=new SimpleAdapter(this,grids,R.layout.work_grid_item,new String[]{"grid_item_icon","grid_item_name"},new int[]{R.id.work_grid_item_iv,R.id.work_grid_item_tv});
        applyGv.setAdapter(gridAdapter);
        applyGv.setOnItemClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void init() {
        mBack = ((ImageView) findViewById(R.id.apply_back));
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ApplyAcitity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        applyGv = ((GridView) findViewById(R.id.apply_grid));
        rbtnMyApply = ((RadioButton) findViewById(R.id.apply_rb_myApply));
        apply_rb_receive = ((RadioButton) findViewById(R.id.apply_rb_receive));


        //审批空间的点击事件

    }

    /**
     * 各个空间点击事件
     */
    private void onclick(){
        rbtnMyApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (compnyId<1){
                    ToastCommom.ToastShow(ApplyAcitity.this,null,"您还没有加入公司，不能操作");
                }else{
                    Intent intent=new Intent(ApplyAcitity.this,MyApplyActivity.class);
                    startActivity(intent);}
            }
        });
        apply_rb_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (compnyId<1){
                    ToastCommom.ToastShow(ApplyAcitity.this,null,"您还没有加入公司，不能操作");
                }else{
                    Intent intent1=new Intent(ApplyAcitity.this,GetIshenPiActivity.class);
                    startActivity(intent1);}
            }
        });
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (compnyId<1){
            ToastCommom.ToastShow(ApplyAcitity.this,null,"您还没有加入公司，不能操作");
        }else{
        switch (position){
            case 0:
                Intent intent=new Intent(this,ApplymentActivity.class);
                intent.putExtra("type",names[position].trim());
                startActivity(intent);
                break;
            case 1:
                Intent intent1=new Intent(this,ApplymentActivity.class);
                intent1.putExtra("type",names[position].trim());
                startActivity(intent1);
                break;
            case 2:
                Intent intent2=new Intent(this,ApplymentActivity.class);
                intent2.putExtra("type",names[position].trim());
                startActivity(intent2);
                break;

//            case 4:
//                Intent intent4=new Intent(this,ApproveConfigActivity.class);
//
//                startActivity(intent4);
//                break;
        }}


    }

}
