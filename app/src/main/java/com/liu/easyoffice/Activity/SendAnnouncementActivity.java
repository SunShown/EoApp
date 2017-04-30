package com.liu.easyoffice.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.DateUtil;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.pojo.Announcement;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.iwf.photopicker.widget.MultiPickResultView;

public class SendAnnouncementActivity extends AppCompatActivity {
    public static final int ANNOUNCEMENT_SUCCESS = 1;
    public static final int ANNOUNCEMENT_FAILED = 2;
    private String author="";
    private Long id;
    private List<String> piclist=new ArrayList<>();
    private MultiPickResultView recyclerView;
    private EditText authorcontent;
    private TextView authorname;
    private TextView mToolTitle;
    private  TextView mToolText;
    private  ImageView mToolBack;
    private EditText mTitle;
    private Context mcontext;
    private File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" +getPhotoFileName());
    private List<String> picName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext=this;
        setContentView(R.layout.activity_send_announcement);
        initData();
    }

    private void initData() {
        mToolTitle = ((TextView) findViewById(R.id.mtoolbar_text));
        mToolTitle.setText("我的公告");
        mToolText = ((TextView) findViewById(R.id.mtoolbar_tv));
        mToolText.setText("完成");
        mToolBack = ((ImageView) findViewById(R.id.tool_back));
        mToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendAnnouncementActivity.this.finish();
            }
        });
        author=getIntent().getStringExtra("name");
        id=getIntent().getLongExtra("id",0L);
        authorcontent = ((EditText) findViewById(R.id.author_send));
        mTitle = ((EditText) findViewById(R.id.announcement_title_send));
        authorname = ((TextView) findViewById(R.id.author_dengmessage));
        authorname.setText(author);
        recyclerView = ((MultiPickResultView) findViewById(R.id.recycler_view_announcement));
        recyclerView.init(this,MultiPickResultView.ACTION_SELECT,null);
        mToolText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(mTitle.getText().toString())||mTitle.getText().toString().trim()==null){
                    ToastCommom.ToastShow(mcontext,null,"标题不能为空");
                    return;
                }
                if("".equals(authorcontent.getText().toString().trim())||authorcontent.getText().toString().trim()==null){
                    ToastCommom.ToastShow(mcontext,null,"内容不能为空");
                    return;
                }
                picName=new ArrayList<String>();
                for (int i=0;i<9;i++){
                    if(i<piclist.size()){
                        Bitmap bitmap= BitmapFactory.decodeFile(piclist.get(i));
                        picName.add(i,imgeUpload(bitmap));
                    }else{
                        picName.add(i,"");
                    }

                }
                final String time= DateUtil.dateToString(new Date());
                final Announcement announcement=new Announcement();
                announcement.setAtitle(mTitle.getText().toString().trim());
                announcement.setAuthor(author);
                announcement.setAuthorId(id.toString().trim());
                announcement.setAtime(time);
                announcement.setAcontent(authorcontent.getText().toString().trim());
                announcement.setImg_1(picName.get(0));
                announcement.setImg_2(picName.get(1));
                announcement.setImg_3(picName.get(2));
                announcement.setImg_4(picName.get(3));
                announcement.setImg_5(picName.get(4));
                announcement.setImg_6(picName.get(5));
                announcement.setImg_7(picName.get(6));
                announcement.setImg_8(picName.get(7));
                announcement.setImg_9(picName.get(8));
                SharedPreferences sp=getApplicationContext().getSharedPreferences("company",MODE_PRIVATE);
                Long tcId=sp.getLong("tcId",0L);
                Gson gson=new Gson();
                String announcementa=gson.toJson(announcement);
                RequestParams rp=new RequestParams(Utils.SEND_ANNOUNCEMENT);
                rp.addParameter("announcement",announcementa);
                rp.addParameter("tcId",tcId);
                x.http().get(rp, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String  result) {
                        System.out.println("返回结果"+result);
                        if("".equals(result)||result==null){
                            ToastCommom.ToastShow(mcontext,null,"发布失败");
                        }else{
                            ToastCommom.ToastShow(mcontext,null,"发布成功");
                            Gson gson=new Gson();
                            Announcement aa=new Announcement();
                            aa=gson.fromJson(result,Announcement.class);
                            Intent intent=new Intent();
                            intent.putExtra("announcement",aa);
                            System.out.println(aa+"Activityd的aa");
                            setResult(ANNOUNCEMENT_SUCCESS,intent);
                            SendAnnouncementActivity.this.finish();
                        }
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
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        recyclerView.onActivityResult(requestCode,resultCode,data);
        piclist=recyclerView.getPhotos();
    }
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(date) + "_" + UUID.randomUUID() + ".png";
    }
    private String imgeUpload(Bitmap photo) {
        RequestParams params = new RequestParams(Utils.XUTIL_URL + "upload");//upload 是你要访问的servlet
        String name = file.getName();
        file.delete();
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" +
                name);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        params.setMultipart(true);
        params.addBodyParameter("file", file);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("upload", "onSuccess: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("upload", "onError: " + ex);
                ToastCommom.ToastShow(mcontext, null, "图片上传失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
        return name;
    }
    }
