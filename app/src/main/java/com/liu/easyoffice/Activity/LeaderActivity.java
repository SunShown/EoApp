package com.liu.easyoffice.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.liu.easyoffice.MyView.DepthPageTransformer;
import com.liu.easyoffice.MyView.ViewPager;
import com.liu.easyoffice.R;

import java.util.ArrayList;
import java.util.List;

public class LeaderActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager vp;
    private Button skip;
    int previoutsPosition_vp = 0;
    private Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader);
        vp = ((ViewPager) findViewById(R.id.vp));
        skip = ((Button) findViewById(R.id.btn_tiaoguo));
        enter = ((Button) findViewById(R.id.leader_enter));
        skip.setOnClickListener(this);
        enter.setOnClickListener(this);
        List<Integer> viewList=new ArrayList<>();
        viewList.add(0,R.mipmap.start_a);
        viewList.add(1,R.mipmap.start_b);
        viewList.add(2,R.mipmap.start_c);
        viewList.add(3,R.mipmap.start_d);
        MyPageAdapter myPageAdapter=new MyPageAdapter(viewList);
        final Animation animation5= AnimationUtils.loadAnimation(this, R.anim.pull_in);
        final Animation animation6=AnimationUtils.loadAnimation(this, R.anim.pull_out);
        vp.setAdapter(myPageAdapter);
        vp.setPageTransformer(true, new DepthPageTransformer());
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                previoutsPosition_vp=position;
                if(position==3){
                    skip.setVisibility(View.GONE);
                    skip.setAnimation(animation6);
                    enter.setVisibility(View.VISIBLE);
                    enter.setAnimation(animation5);
                }else {
                    skip.setVisibility(View.VISIBLE);
                    skip.setAnimation(animation5);
                    enter.setVisibility(View.GONE);
                    enter.setAnimation(animation6);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_tiaoguo:
                Intent intent=new Intent(LeaderActivity.this,Login2Activity.class);
                startActivity(intent);
                break;
            case R.id.leader_enter:
                Intent intent1=new Intent(LeaderActivity.this,Login2Activity.class);
                startActivity(intent1);
                break;
        }
    }

    private class MyPageAdapter extends PagerAdapter{
        List<Integer> imgsrc;
        public MyPageAdapter(List<Integer> imgsrc){
            this.imgsrc = imgsrc;
        }
        @Override
        public int getCount() {
            return imgsrc.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.vp_item,null);
            ImageView iv_vp_item= ((ImageView) view.findViewById(R.id.iv_vp_item));
            iv_vp_item.setImageResource(imgsrc.get(position));
            container.addView(view);
            return  view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
