package com.liu.easyoffice.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liu.easyoffice.Progressbar.LoadDialog;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.pojo.ApproveState;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.iwf.photopicker.widget.MultiPickResultView;

public class ApproveDecisonActivity extends AppCompatActivity {

    private MultiPickResultView photo_pick_desicion;
    private EditText et_appdecision_reason;
    private Button appdecision_submit;
    private ImageView my_title_img_left;  private String str;
    String approveId;
    List<String> photos=new ArrayList<>();
    List<String> rsphotos=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_decison);
        initView();
        initData();
        initEvernt();
    }
    private void initView() {
        photo_pick_desicion = ((MultiPickResultView) findViewById(R.id.photo_pick_desicion));
        et_appdecision_reason = ((EditText) findViewById(R.id.et_appdecision_reason));
        appdecision_submit = ((Button) findViewById(R.id.appdecision_submit));
        my_title_img_left = ((ImageView) findViewById(R.id.my_title_img_left));
    }

    private void initData() {
        Intent intent = getIntent();
//        intent1.putExtra("approveId", approveId);
//        intent1.putExtra("decsion",0);
     approveId = intent.getStringExtra("approveId");
        String decsion = intent.getStringExtra("decsion");
        if ("0".equals(decsion)) {
            et_appdecision_reason.setHint("请输入拒绝理由（非必填）");
            str="shenpirefuse";
        } else if ("1".equals(decsion)) {
            str="shenpitongyi";
            et_appdecision_reason.setHint("请输入同意理由（非必填）");
        }
        photo_pick_desicion.init(this, MultiPickResultView.ACTION_SELECT, null);

//onActivityResult里一行代码回调
       // photo_pick_desicion.onActivityResult(requestCode,resultCode,data);
    }
//approveId
    //userid
    //decisiontime
    //Imgurl
    //comment
    private void initEvernt() {
        my_title_img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        appdecision_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appdecision_submit.setClickable(false);
                LoadDialog.show(ApproveDecisonActivity.this, "努力加载中");

               RequestParams params=new RequestParams(Utils.XUTIL_URL+str);
                params.setConnectTimeout(4*1000);
              //  System.out.println("=============================="+Utils.XUTIL_URL+str);
                Gson gson=new Gson();
                if (photos!=null&&photos.size()>0){

                    for (int i = 0; i<photos.size(); i++){

                        Bitmap bitmap= BitmapFactory.decodeFile(photos.get(i));
                        String name=imgeUpload(bitmap);
                        Log.e("imgUploadBitmap", "onClick: "+name);
                       Log.e("name",name);
                        rsphotos.add(name);



                    }}
                ApproveState state=new ApproveState(rsphotos,Integer.parseInt(approveId.trim()),et_appdecision_reason.getText().toString(),new Timestamp(new Date().getTime()), MySharePreference.getCurrentUser(ApproveDecisonActivity.this));
                Type type = new TypeToken<ApproveState>() {
                }.getType();
               String statestr=gson.toJson(state,type);
                params.addBodyParameter("statejson",statestr);
                params.setConnectTimeout(4*1000);
                x.http().get(params, new Callback.CommonCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        System.out.println("result+++++++++++++++++++++" + result);
                        Integer code = Integer.parseInt(result.trim());
                        System.out.println("code+++++++++++++++++++++" + code);
                        Intent intent = new Intent("com.liu.easyoffice.approveinfoupdate");

                        Bundle bundle = new Bundle();
                        bundle.putString("approveId",approveId);
                        bundle.putString("code",code+"");
                        intent.putExtra("bundle1",bundle);


                        sendBroadcast(intent);
                        LoadDialog.dismiss(ApproveDecisonActivity.this);
                        finish();

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Intent intent = new Intent("com.liu.easyoffice.approveinfoupdate");

                        Bundle bundle = new Bundle();
                        bundle.putString("approveId",approveId);
                        bundle.putString("code",000+"");
                        intent.putExtra("bundle1",bundle);
                        LoadDialog.dismiss(ApproveDecisonActivity.this);
                        appdecision_submit.setClickable(true);
                        sendBroadcast(intent);
                        finish();
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
        photo_pick_desicion.onActivityResult(requestCode,resultCode,data);
      photos= photo_pick_desicion.getPhotos();
        Log.e("photos", "onActivityResult: "+photos.toString());
    }
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(date) + "_" + UUID.randomUUID() + ".png";
    }
    private String imgeUpload(Bitmap photo) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" +getPhotoFileName());

        RequestParams params = new RequestParams(Utils.XUTIL_URL+"upload");//upload 是你要访问的servlet
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
                ToastCommom.ToastShow(ApproveDecisonActivity.this, null, "图片上传失败");
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
