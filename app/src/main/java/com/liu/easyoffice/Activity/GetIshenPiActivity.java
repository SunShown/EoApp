package com.liu.easyoffice.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.liu.easyoffice.Fragment.NewIShenpiFragment;
import com.liu.easyoffice.Fragment.OldIShenpiFragment;
import com.liu.easyoffice.R;

public class GetIshenPiActivity extends AppCompatActivity implements View.OnClickListener {
    Fragment newFragment;
    Fragment oldFragment;
    Fragment leftFragment;
    Fragment rightFragment;
    private RadioButton rb_ishenpi1;
    private RadioButton rb_ishenpi2;
    private RadioGroup rg_tab_shenpi;
    private TextView tv_shenpi_back;
    private int backflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_ishen_pi);

        Intent intent=getIntent();
        backflag=intent.getIntExtra("backflag",0);
        System.out.println("11111111111111111111111111111111111");
        rb_ishenpi1 = ((RadioButton) findViewById(R.id.rb_ishenpi1));
        rb_ishenpi2 = ((RadioButton) findViewById(R.id.rb_ishenpi2));
        rg_tab_shenpi = ((RadioGroup) findViewById(R.id.rg_tab_shenpi));
        tv_shenpi_back = ((TextView) findViewById(R.id.tv_shenpi_back));
        tv_shenpi_back.setOnClickListener(this);
        rg_tab_shenpi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_ishenpi1:
                        if (leftFragment == null) {
                            leftFragment = new NewIShenpiFragment();
                        }
                        newFragment = leftFragment;
                        switchFragment(newFragment);
                        break;
                    case R.id.rb_ishenpi2:
                        if (rightFragment == null) {
                            rightFragment = new OldIShenpiFragment();
                        }

                        newFragment = rightFragment;
                        switchFragment(newFragment);
                        break;

                }
            }
        });
        switch ( backflag){
            case 0:
                ((RadioButton) rg_tab_shenpi.getChildAt(0)).setChecked(true);
                break;
            case 1:
                ((RadioButton) rg_tab_shenpi.getChildAt(1)).setChecked(true);
                break;

        }



    }


    public void switchFragment(Fragment fragment) {
        // 设置一个默认值
        if (fragment == null) {
            fragment = leftFragment = new NewIShenpiFragment();

        }


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //http://www.cnblogs.com/hixin/p/4427276.html
        if (oldFragment != null && !oldFragment.isHidden() && oldFragment.isAdded() && oldFragment != newFragment) {

            ft.hide(oldFragment);
        }
        if (fragment != null && fragment.isAdded() && fragment.isHidden()) {
            ft.show(fragment);
        } else {
            ft.add(R.id.fy_Ishenpi, fragment);
        }

        oldFragment = fragment;

        ft.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_shenpi_back:
                Intent intent=new Intent(GetIshenPiActivity.this,ApplyAcitity.class);
                startActivity(intent);
                break;

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(GetIshenPiActivity.this,ApplyAcitity.class);
        startActivity(intent);
        finish();
    }
}
