package com.liu.easyoffice.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.pojo.Announcement;

public class AnnouncementShowActivity extends AppCompatActivity {

    private TextView mToolTitle;
    private TextView mToolRight;
    private TextView title;
    private TextView author;
    private TextView content;
    private ImageView img2;
    private ImageView img1;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private ImageView img6;
    private ImageView img7;
    private ImageView img8;
    private ImageView img9;
    private ImageView mToolLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_show);
        initData();
    }

    private void initData() {
        mToolTitle = ((TextView) findViewById(R.id.mtoolbar_text));
        mToolTitle.setText("公告");
        mToolRight = ((TextView) findViewById(R.id.mtoolbar_tv));
        mToolRight.setVisibility(View.GONE);
        mToolLeft = ((ImageView) findViewById(R.id.tool_back));
        mToolLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnnouncementShowActivity.this.finish();
            }
        });
        Announcement a=(Announcement) getIntent().getSerializableExtra("showAnnouncement");
        title = ((TextView) findViewById(R.id.announcement_show_title));
        title.setText(a.getAtitle());
        author = ((TextView) findViewById(R.id.announcement_show_author));
        author.setText(a.getAuthor()+"  "+a.getAtime());
        content = ((TextView) findViewById(R.id.announcement_show_content));
        content.setText(a.getAcontent());
        img2 = ((ImageView) findViewById(R.id.img_iv2));
        img1 = ((ImageView) findViewById(R.id.img_iv1));
        img3 = ((ImageView) findViewById(R.id.img_iv3));
        img4 = ((ImageView) findViewById(R.id.img_iv4));
        img5 = ((ImageView) findViewById(R.id.img_iv5));
        img6 = ((ImageView) findViewById(R.id.img_iv6));
        img7 = ((ImageView) findViewById(R.id.img_iv7));
        img8 = ((ImageView) findViewById(R.id.img_iv8));
        img9 = ((ImageView) findViewById(R.id.img_iv9));
        if("".equals(a.getImg_1())||a.getImg_1()==null){
            img1.setImageResource(R.mipmap.backpic);
        }else{
            xUtilsImageUtils.display(img1,a.getImg_1());
        }
        xUtilsImageUtils.display(img2,a.getImg_2());
        xUtilsImageUtils.display(img3,a.getImg_3());
        xUtilsImageUtils.display(img4,a.getImg_4());
        xUtilsImageUtils.display(img5,a.getImg_5());
        xUtilsImageUtils.display(img6,a.getImg_6());
        xUtilsImageUtils.display(img7,a.getImg_7());
        xUtilsImageUtils.display(img8,a.getImg_8());
        xUtilsImageUtils.display(img9,a.getImg_9());

    }
}
