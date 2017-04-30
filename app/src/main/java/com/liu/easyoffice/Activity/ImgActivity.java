package com.liu.easyoffice.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.Utils;

import org.xutils.image.ImageOptions;
import org.xutils.x;

public class ImgActivity extends AppCompatActivity {

    private ImageView iv_main;
    private RelativeLayout rl_img_back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window=ImgActivity.this.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        setContentView(R.layout.activity_img);
        iniView();
        initData();
   initEvent();



    }

    private void initEvent() {
        rl_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void iniView() {
        iv_main = ((ImageView) findViewById(R.id.iv_main));
        rl_img_back = ((RelativeLayout) findViewById(R.id.rl_img_back));


        //img_main_myti.setLeftImgShow(true);
     //   img_main_myti.setLeftImg(getResources().getDrawable(R.mipmap.back));
       // img_main_myti.setBackgroundColor(Color.parseColor("#808080"));
    }
    private void initData() {
        Intent intent=getIntent();
        String imgurl=intent.getStringExtra("imgurl");
        iv_main.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true

                .setFailureDrawableId(R.mipmap.ic_launcher)
                .setLoadingDrawableId(R.mipmap.ic_launcher)
                .build();
        x.image().bind(iv_main, Utils.IMG_URL+imgurl, imageOptions);

    }

}
