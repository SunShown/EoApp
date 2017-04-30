package com.liu.easyoffice.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.BitmapToCircle;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.city.ScrollerNumberPicker;
import com.liu.easyoffice.pojo.User;

import org.feezu.liuli.timeselector.TimeSelector;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import io.rong.push.RongPushClient;
import io.rong.push.notification.PushNotificationMessage;

public class PersonalInformationActivity extends Activity implements View.OnClickListener {
    //////////////////拍照所需变量
    /////////////////////
    //////////
    private static final int PHOTO_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PHOTO_CLIP = 3;
    private static final int NAME_REQUEST = 4;
    private static final int SEX_REQUEST = 5;
    private File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+
            getPhotoFileName());


    ///////////
    private RelativeLayout birth;
    private RelativeLayout name;
    private TextView name_tv;
    private RelativeLayout sex;
    private TextView sex_tv;
    private RelativeLayout head_img;
    private TextView birthday_tv;
     private   String date;
    private int year, monthOfYear, dayOfMonth;
    private RelativeLayout area;
    private TextView area_tv;
    private TextView v_title_tv;
    private ImageView textView;
    private TextView imgUrlTv;
    private Context mcontext;
    private Button confirmBtn;
    private Bitmap photo=null;
    private TextView mToolTitle;
    private TextView mToolText;
    private ImageView mToolBack;
    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        mcontext=this;
        init();
        initEvent();
        Log.e("fileName", "onCreate: " + file.getName());
        initDate();
    }
    private void init() {
        mToolTitle = ((TextView) findViewById(R.id.mtoolbar_text));
        mToolTitle.setText("个人信息");

        mToolBack = ((ImageView) findViewById(R.id.tool_back));
        mToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalInformationActivity.this.finish();
            }
        });
        //生日
        currentUser = MySharePreference.getCurrentUser(mcontext);
        birth = ((RelativeLayout) findViewById(R.id.activity_personal_information_birth));
        birthday_tv = ((TextView) findViewById(R.id.activity_personal_information_birth_tv));

        //姓名
        name = ((RelativeLayout) findViewById(R.id.activity_personal_information_name));
        name_tv = ((TextView) findViewById(R.id.activity_personal_information_name_tv));

        //性别
        sex = ((RelativeLayout) findViewById(R.id.activity_personal_information_sex));
        sex_tv = ((TextView) findViewById(R.id.activity_personal_information_sex_tv));
        imgUrlTv = ((TextView) findViewById(R.id.personal_tv_imgUrl));//图片地址用textView展示

        //头像
        head_img = ((RelativeLayout) findViewById(R.id.activity_personal_information_head_img));
        textView = ((ImageView) findViewById(R.id.textView));
        //地区
        area = ((RelativeLayout) findViewById(R.id.activity_personal_information_area));
        area_tv = ((TextView) findViewById(R.id.activity_personal_information_area_tv));
        //确定按钮
        mToolText = ((TextView) findViewById(R.id.mtoolbar_tv));
        mToolText.setText("完成");

    }
    private void initEvent() {
        birth.setOnClickListener(this);
        name.setOnClickListener(this);
        sex.setOnClickListener(this);
        head_img.setOnClickListener(this);
        area.setOnClickListener(this);
        mToolText.setOnClickListener(this);
    }

    private void initDate() {
        xUtilsImageUtils.display(textView, currentUser.getImgUrl(), true);
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(currentUser.getUserId(),currentUser.getUserName(),Uri.parse(Utils.UPLOAD_IMG+currentUser.getImgUrl())));
        name_tv.setText(currentUser.getUserName());
        area_tv.setText(currentUser.getAddress());
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Log.e("birthday", "initDate: " + currentUser.getBirthday());
        birthday_tv.setText(sdf.format(currentUser.getBirthday()));
        if (currentUser.getSex() == 0) {
            sex_tv.setText("男");
        } else {
            sex_tv.setText("女");
        }
    }

    //////////////////////////////////////////////////
    //////////////////////
    //所有拍照选照片上传方法都在这
    ////////////////////////
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        System.out.println("============"+ UUID.randomUUID());
        return sdf.format(date)+"_"+UUID.randomUUID() + ".png";
    }
    private void getPicFromPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_REQUEST);
    }
    private void getPicCa() {
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent2,CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("++++++++++++++++data != null"+requestCode);
        switch (requestCode){
            case  PHOTO_REQUEST:
                System.out.println("++++++++++++++++data != null"+data != null);
                if (data != null) {
                    photoclip(data.getData());
                }

                break;
            case  CAMERA_REQUEST:
                System.out.println("++++++++++++++++++++++++++file.exists():"+file.exists());
                if (file.exists()) {
                    photoclip(Uri.fromFile(file));
                }


                break;
            case PHOTO_CLIP:
                Log.e("fileName", "onActivityResult: " + file.getName());
                System.out.println(data);
                if (data != null) {
                    imgUrlTv.setText(file.getName());
                    final Bundle extras = data.getExtras();
                    if (extras != null) {
                        photo = extras.getParcelable("data");
                        showImage(photo);
                    }
                    break;
                }
                break;




        }
    }

    private void sendImage(Intent data) {

        // Toast.makeText(getApplicationContext(),"sendImage",Toast.LENGTH_SHORT).show();

        if (data != null) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                final Bitmap photo = extras.getParcelable("data");


                RequestParams params = new RequestParams(Utils.XUTIL_URL+"upload");//upload 是你要访问的servlet

                //System.out.println(NetUtil.url+"upload");
                //System.out.println(file.length()+" ============ file length:upload");

                String name=file.getName();
                file.delete();
                file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+
                        name);
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

                    photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                    params.addBodyParameter("file",file);
//        params.addBodyParameter("file",file1);

                    x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
//RequestParams params1=
                            showImage(photo);

                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Toast.makeText(getApplicationContext(),"服务器错误",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                            //  Toast.makeText(getApplicationContext(),"服务器错误",Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFinished() {
                            //Toast.makeText(getApplicationContext(),"服务器错误",Toast.LENGTH_SHORT).show();

                        }
                    });

                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"压图出错",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }




            }
        }




    }

    private void showImage(Bitmap photo) {
        textView.setImageBitmap(BitmapToCircle.toRoundBitmap(photo));
    }

    private void photoclip(Uri data) {
        System.out.println("========================剪彩");
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CLIP);


    }

    @Override
    public void onClick(View v) {
        View v_title=LayoutInflater.from(getApplicationContext()).inflate(R.layout.alertdialog_title,null);
        v_title_tv = ((TextView) v_title.findViewById(R.id.txtPatient));
        switch(v.getId()){
            case R.id.activity_personal_information_birth:
                new TimeSelector(mcontext, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        birthday_tv.setText(time);
                    }
                }, "1970-01-01", "2100-12-31").show();
                break;
            case R.id.activity_personal_information_name:
                v_title_tv.setText("姓名");
                final EditText editText = new EditText(this);
                new AlertDialog.Builder(this)
                        .setCustomTitle(v_title)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(editText )
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String nameString=editText.getText().toString();
                                name_tv.setText(nameString);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.activity_personal_information_sex:
                v_title_tv.setText("性别");
                final String sexItems[]={"男","女"};
//                new AlertDialog.Builder(this)
//                        .setCustomTitle(v_title)
//                        .setIcon(R.mipmap.ic_launcher)
//                        .setItems(sexItems, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                sex_tv.setText(sexItems[which]);
//                                dialog.dismiss();
//                            }
//                        }).show();
                AlertDialog alertDialog;
                ListAdapter itemlist = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sexItems);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setCustomTitle(v_title);
                builder1.setAdapter(itemlist, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sex_tv.setText(sexItems[which]);
                        dialog.dismiss();
                    }
                });
                alertDialog=builder1.show();
                break;
            case R.id.activity_personal_information_head_img:
                v_title_tv.setText("头像");
                final String img_headItems[]={"拍照","从相册中选取"};
                new AlertDialog.Builder(this)
                        .setCustomTitle(v_title)
                        .setItems(img_headItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                switch (which){
                                    case 0:
                                        getPicCa();
                                        break;
                                    case 1:

                                        getPicFromPhoto();
                                        break;


                                }




                            }
                        }).show();
                break;
            case R.id.activity_personal_information_area:
                AlertDialog.Builder builder=new AlertDialog.Builder(PersonalInformationActivity.this);
                View view = LayoutInflater.from(PersonalInformationActivity.this).inflate(R.layout.addressdialog, null);
                builder.setView(view);
                LinearLayout addressdialog_linearlayout = (LinearLayout)view.findViewById(R.id.addressdialog_linearlayout);
                final ScrollerNumberPicker provincePicker = (ScrollerNumberPicker)view.findViewById(R.id.province);
                final ScrollerNumberPicker cityPicker = (ScrollerNumberPicker)view.findViewById(R.id.city);
                final ScrollerNumberPicker counyPicker = (ScrollerNumberPicker)view.findViewById(R.id.couny);
                final AlertDialog dialog = builder.show();
                addressdialog_linearlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        area_tv.setText(provincePicker.getSelectedText()+"-"+cityPicker.getSelectedText()+"-"+counyPicker.getSelectedText());
                        Log.i("kkkk",provincePicker.getSelectedText()+cityPicker.getSelectedText()+counyPicker.getSelectedText());
                        dialog.dismiss();
                        
                    }
                });
                break;
            case R.id.mtoolbar_tv://点击确定按钮
                User user = new User();
                user.setId(MySharePreference.getCurrentUser(mcontext).getId());
                if (name_tv.getText().toString().trim().equals("") || name_tv.getText().toString() == null) {
                    ToastCommom.ToastShow(mcontext, null, "用户名不能为空");
                } else {
                    user.setUserName(name_tv.getText().toString());
                    if (sex_tv.getText().toString().equals("女")) {
                        user.setSex(1);
                    }
                    if (imgUrlTv.getText().toString() != null && !imgUrlTv.getText().toString().trim().equals("")) {//存图片
                        user.setImgUrl(imgUrlTv.getText().toString());
                    }
                    String birthdayStr = birthday_tv.getText().toString();
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    format.setLenient(false);
                    try {
                        user.setBirthday(new Timestamp(format.parse(birthdayStr).getTime()));
                        Log.e("birthday", "onClick: "+format.parse(birthdayStr));
                        Log.e("birthday", "onClick: "+user.getBirthday() );
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (area_tv.getText().toString() != null) {
                        user.setAddress(area_tv.getText().toString());
                    }
                    connetUpdate(user, photo);
                }
        }
    }

    private void connetUpdate(final User user, Bitmap photo) {
        if (photo != null) {//如果不为空则上传服务器
            imgeUpload(photo);//上传服务器
        }
        Gson gson = new Gson();
        String userGson = gson.toJson(user);
        PushNotificationMessage message=new PushNotificationMessage();
        message.setPushTitle("通知");
        message.setTargetId("3");
        RongPushClient.sendNotification(mcontext,message);
//        RongIM.getInstance().sendMessage();
        RequestParams params = new RequestParams(Utils.UPDATE_USER);
        params.addBodyParameter("user", userGson);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("200")) {
                    Log.e("update", "onSuccess:修改成功 ");
                    ToastCommom.ToastShow(mcontext,null,"更新成功");
                    Intent intent=new Intent();
                    intent.putExtra("user",user);
                    setResult(1,intent);
                    finish();
                    MySharePreference.setCurrentUser(mcontext,user);//修改preference
                    Log.e("Myshare", "onSuccess: "+MySharePreference.getCurrentUser(mcontext) );
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("update", "onError: " + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }


    //上传图片
    private void imgeUpload(Bitmap photo) {
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
    }
}
