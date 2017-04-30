package com.liu.easyoffice.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liu.easyoffice.Progressbar.LoadDialog;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.DateUtil;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.pojo.ApproveColumn;
import com.liu.easyoffice.pojo.ApproveType;
import com.liu.easyoffice.pojo.Approveinfo;

import org.feezu.liuli.timeselector.TimeSelector;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.iwf.photopicker.widget.MultiPickResultView;

public class ApplymentActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButton;
    private RelativeLayout during;
    private RelativeLayout days;
    private RelativeLayout reason;
    private RelativeLayout pic;
    private RelativeLayout approvers;
    private RelativeLayout leave_type;
    private RelativeLayout travel_area;
    private RelativeLayout expense;
    private RelativeLayout options;
    private RelativeLayout subscribe;
    private TextView days_title;
    private EditText days_tv;
    private TextView reason_title;
    private EditText reason_tv;
    private TextView leave_type_tv;
    private TextView v_title_tv;
    private static  final int REQUEST_CODE_GALLERY=1001;
    private  MultiPickResultView recyclerView;
    private List<String> piclist=new ArrayList<>();//图片数组
    private List<String> rsphotos=new ArrayList<>();
    private RelativeLayout start_relative;
    private RelativeLayout end_relative;
    private TextView start_time_tv;
    private TextView end_time_tv;
    private Context mcontext;
    private EditText area_tv;
    private EditText expense_num_tv;

    private EditText expense_type_tv;
    private EditText rexpense_reason_tv;
    private TextView applyment_subscribe_tv;
    private Toolbar mBar;
    private TextView mtoolbar_tv;
    private TextView mtoolbar_text;
    private ImageView im_applyment_back;
    private Button applyment_submit;
    private Map<String, String> rsviews;
    private MultiPickResultView recycler_view;
    private String type;

//    private GridView approvers_grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyment);

        mcontext = this;
        init();
        initEvent();
        initApproment();//界面调整

//        initGrid();
    }


    private void init() {
        //初始化grid
//        approvers_grid = ((GridView) findViewById(R.id.add_approvers_grid));
        /*toolbar标题栏、以及button*/
        mBar = ((Toolbar) findViewById(R.id.mtoolbar));
        mtoolbar_text = ((TextView) findViewById(R.id.mtoolbar_text));
        mtoolbar_tv = ((TextView) findViewById(R.id.mtoolbar_tv));
        mButton = ((Button) findViewById(R.id.applyment_submit));
        //共供调用模块
        during = ((RelativeLayout) findViewById(R.id.applyment_time));//开始结束时间
        start_relative = ((RelativeLayout) findViewById(R.id.time_start_realative));//开始时间
        start_time_tv = ((TextView) findViewById(R.id.time_start_tv));
        end_relative = ((RelativeLayout) findViewById(R.id.time_end_realative));//结束时间
        end_time_tv = ((TextView) findViewById(R.id.time_end_tv));
        days = ((RelativeLayout) findViewById(R.id.applyment_days));//天数
        days_title = ((TextView) findViewById(R.id.days_title));
        days_tv = ((EditText) findViewById(R.id.days_tv));
        reason = ((RelativeLayout) findViewById(R.id.applyment_reason));//事由
        reason_title = ((TextView) findViewById(R.id.reason_title));
        reason_tv = ((EditText) findViewById(R.id.reason_tv));
        pic = ((RelativeLayout) findViewById(R.id.applyment_pic));//图片
//        approvers = ((RelativeLayout) findViewById(R.id.applyment_approvers));//审批人


        //请假
        leave_type = ((RelativeLayout) findViewById(R.id.applyment_leave_type));//请假类型
        leave_type_tv = ((TextView) findViewById(R.id.leave_type_tv));
        //出差
        travel_area = ((RelativeLayout) findViewById(R.id.applyment_travel_area));//出差地点
        area_tv = ((EditText) findViewById(R.id.travel_area_tv));
        //报销
        expense = ((RelativeLayout) findViewById(R.id.applyment_expense));//报销
        expense_num_tv = ((EditText) findViewById(R.id.expense_num_tv));//报销金额
        expense_type_tv = ((EditText) findViewById(R.id.expense_type_tv));//报销类型
        rexpense_reason_tv = ((EditText) findViewById(R.id.rexpense_reason_tv));//报销明细
        options = ((RelativeLayout) findViewById(R.id.applyment_options));//附件


        //补签
        subscribe = ((RelativeLayout) findViewById(R.id.applyment_subscribe));//补签
        applyment_subscribe_tv = ((TextView) findViewById(R.id.applyment_subscribe_tv));
        recyclerView = (MultiPickResultView) findViewById(R.id.recycler_view);
        recyclerView.init(this, MultiPickResultView.ACTION_SELECT, null);


        im_applyment_back = ((ImageView) findViewById(R.id.tool_back));
        applyment_submit = ((Button) findViewById(R.id.applyment_submit));
        recycler_view = ((MultiPickResultView) findViewById(R.id.recycler_view));


    }

    private void initEvent() {
        mBar.setOnClickListener(this);
        mButton.setOnClickListener(this);
        during.setOnClickListener(this);
        days.setOnClickListener(this);
        reason.setOnClickListener(this);
        pic.setOnClickListener(this);
//        approvers.setOnClickListener(this);
        leave_type.setOnClickListener(this);
        travel_area.setOnClickListener(this);
        expense.setOnClickListener(this);
        subscribe.setOnClickListener(this);
        start_relative.setOnClickListener(this);
        end_relative.setOnClickListener(this);
        im_applyment_back.setOnClickListener(this);
        applyment_submit.setOnClickListener(this);

    }

    private void initApproment() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type").trim();
//        请假的界面调整
        if ("请假".equals(type)) {
            mtoolbar_text.setText("我的请假");
            mtoolbar_tv.setVisibility(View.GONE);
            leave_type.setVisibility(View.VISIBLE);
            during.setVisibility(View.VISIBLE);
            days.setVisibility(View.VISIBLE);
            days_title.setText("请假天数");
            days_tv.setHint("请输入请假天数");
            reason.setVisibility(View.VISIBLE);
            reason_title.setText("请假事由");
            reason_tv.setHint("请输入请假事由");
            pic.setVisibility(View.VISIBLE);
//            approvers.setVisibility(View.VISIBLE);
            return;
        }
        if ("报销".equals(type)) {
            mtoolbar_text.setText("我的报销");
            mtoolbar_tv.setVisibility(View.GONE);
            expense.setVisibility(View.VISIBLE);
            pic.setVisibility(View.VISIBLE);
//            approvers.setVisibility(View.VISIBLE);
            return;
        }
        if ("出差".equals(type)) {
            mtoolbar_text.setText("我的出差");
            mtoolbar_tv.setVisibility(View.GONE);
            travel_area.setVisibility(View.VISIBLE);
            during.setVisibility(View.VISIBLE);
            days.setVisibility(View.VISIBLE);
            days_title.setText("出差天数");
            days_tv.setHint("请输入出差天数");
            reason.setVisibility(View.VISIBLE);
            reason_title.setText("出差事由");
            reason_tv.setHint("请输入出差事由");
            pic.setVisibility(View.VISIBLE);
//            approvers.setVisibility(View.VISIBLE);
            return;
        }
        if ("补签".equals(type)) {
            mtoolbar_text.setText("我的补签");
            mtoolbar_tv.setVisibility(View.GONE);
            subscribe.setVisibility(View.VISIBLE);
            reason.setVisibility(View.VISIBLE);
            reason_title.setText("补签备注");
            reason_tv.setHint("请输入补签事由");
            return;
        }
    }


    public void onClick(View v) {
        View v_title = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alertdialog_title, null);
        v_title_tv = ((TextView) v_title.findViewById(R.id.txtPatient));
        switch (v.getId()) {
            case R.id.applyment_leave_type:
                v_title_tv.setText("请假类型");
                AlertDialog dialog1;
                final String typeitems[] = {"事假", "病假", "年假", "调休", "婚嫁", "产假", "陪产假", "路途假", "其他"};
                ListAdapter typeList = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, typeitems);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请选择");
                builder.setAdapter(typeList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        leave_type_tv.setText(typeitems[which]);
                        dialog.dismiss();
                    }
                });
                dialog1 = builder.show();
                break;
            case R.id.time_start_realative:
                new TimeSelector(mcontext, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        start_time_tv.setText(time);
                    }
                }, "1970-01-01", "2100-12-31").show();
                break;
            case R.id.time_end_realative:
                new TimeSelector(mcontext, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        end_time_tv.setText(time);
                    }
                }, "1970-01-01", "2100-12-31").show();
                break;
            case R.id.applyment_subscribe:
                new TimeSelector(mcontext, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        applyment_subscribe_tv.setText(time);
                    }
                }, "1970-01-01", "2100-12-31").show();
                break;
            case R.id.tool_back:
                finish();
                break;
            case R.id.applyment_submit:
                applyment_submit.setClickable(false);
                LoadDialog.show(ApplymentActivity.this, "努力加载中");
                update();
                break;
        }
    }

    private void getAllData() {

        String duringstr=days_tv.getText().toString().trim();//天数
        String startstr=start_time_tv.getText().toString().trim();//开始时间
        String endstr=end_time_tv.getText().toString().trim();//结束时间
        String reasonstr=reason_tv.getText().toString().trim();//原因事由
        String leavetypestr=leave_type_tv.getText().toString().trim();//请假类型
        String areastr=area_tv.getText().toString().trim();//出差地点
        String expense_numstr=expense_num_tv.getText().toString().trim();//报销金额
        String expense_typestr=expense_type_tv.getText().toString().trim();//报销类型
        String rexpense_reasonstr=rexpense_reason_tv.getText().toString().trim();//报销明细
        String applyment_subscribestr=applyment_subscribe_tv.getText().toString().trim();//补签时间
        rsviews=new HashMap<String,String>();

        rsviews.put("days_tv",duringstr);

        rsviews.put("start_time_tv",startstr);
        rsviews.put("end_time_tv",endstr);
        rsviews.put("reason_tv",reasonstr);
        rsviews.put("leave_type_tv",leavetypestr);
        rsviews.put("area_tv",areastr);
        rsviews.put("expense_num_tv",expense_numstr);
        rsviews.put("expense_type_tv",expense_typestr);
        rsviews.put("rexpense_reason_tv",rexpense_reasonstr);
        rsviews.put("applyment_subscribe_tv",applyment_subscribestr);


    }

    //    private void initGrid() {
//        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
//        for (int i = 0; i < 8; i++) {
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("ItemImage", R.mipmap.user_card_test_head);// 添加图像资源的ID
//            map.put("ItemText", "神" + String.valueOf(i));// 按序号做ItemText
//            lstImageItem.add(map);
//        }
//        SimpleAdapter saImageItems = new SimpleAdapter(this, // 没什么解释
//                lstImageItem,// 数据来源
//                R.appconfig_father_item.approvers_list_item,// night_item的XML实现
//                // 动态数组与ImageItem对应的子项
//                new String[] { "ItemImage", "ItemText" },
//                // ImageItem的XML文件里面的一个ImageView,两个TextView ID
//                new int[] { R.id.ItemImage, R.id.ItemText });
//        // 添加并且显示
//        approvers_grid.setAdapter(saImageItems);
//    }
    private void update() {


        RequestParams params = new RequestParams(Utils.XUTIL_URL + "getcolumns");
        params.addBodyParameter("typename", type);
        params.setConnectTimeout(4*1000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                // System.out.println("=======type====="+result);
                Gson gson = new Gson();
                Type types = new TypeToken<List<ApproveColumn>>() {
                }.getType();
                List<ApproveColumn> columns = gson.fromJson(result, types);
                List<ApproveColumn> rscolumns = new ArrayList<ApproveColumn>();


                getAllData();

                RequestParams params1 = new RequestParams(Utils.XUTIL_URL + "addapprove");

                params1.addBodyParameter("companyId", MySharePreference.getCurrentCompany(ApplymentActivity.this).getTcId() + "");
                params1.setConnectTimeout(4*1000);
                Approveinfo info = new Approveinfo();
                ApproveType approveType = new ApproveType();
                approveType.tyypeName = type;

                approveType.tyypeId = columns.get(0).approvetypeid;
                info.approvetypeId = approveType.tyypeId;


                info.sender = MySharePreference.getCurrentUser(ApplymentActivity.this);
                for (ApproveColumn column : columns) {



                    column.data = rsviews.get(column.viewId);
                    if ("".equals(column.data.trim()) || "请选择(必填)".equals(column.data.trim())) {
                        ToastCommom.ToastShow(ApplymentActivity.this, null, column.approvecolumncnname + "不能为空");
                        return;
                    }

                    if(column.viewId.equals("start_time_tv")||column.viewId.equals("end_time_tv")){
                        Log.e("time",rsviews.get("start_time_tv")+rsviews.get("end_time_tv"));
                        if ((!rsviews.get("start_time_tv").trim().equals("请选择(必填)"))&&(!rsviews.get("end_time_tv").trim().equals("请选择(必填)"))){
                            Log.e("time","2");
                        if(DateUtil.stringToDate(rsviews.get("start_time_tv")).getTime()>DateUtil.stringToDate(rsviews.get("end_time_tv")).getTime()){
                            Log.e("time","3");
                            ToastCommom.ToastShow(ApplymentActivity.this,null,"开始时间必须小于结束时间");
                            return;
                        }}
                    }

                    if (column.viewId.equals("days_tv")){
                        if (Integer.parseInt(rsviews.get(column.viewId))<1){
                            ToastCommom.ToastShow(ApplymentActivity.this,null,"天数必须大于0");
                            return;
                        }
                    }

                    rscolumns.add(column);
                }
                System.out.println(columns);
                approveType.columns = rscolumns;
                info.type = approveType;

                for (int i = 0; i < piclist.size(); i++) {

                    Bitmap bitmap = BitmapFactory.decodeFile(piclist.get(i));
                    rsphotos.add(imgeUpload(bitmap));


                }

                info.imgurl = rsphotos;
                info.newTime = new Timestamp(new Date().getTime());
                info.sender = MySharePreference.getCurrentUser(ApplymentActivity.this);
                params1.addBodyParameter("approveinfo", gson.toJson(info, new TypeToken<Approveinfo>() {
                }.getType()));
                x.http().get(params1, new CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        System.out.println("result===============" + result);
                        final int approveId = Integer.parseInt(result.trim());
                        System.out.println("approveId===============" + approveId);
                        if (approveId > 0) {
                            ToastCommom.ToastShow(ApplymentActivity.this, null, "提交成功");

                            Intent intent = new Intent(ApplymentActivity.this, ApproveinfoActivity.class);
                            intent.putExtra("approveId", approveId);
                            startActivity(intent);

                            finish();


                        } else {


                            ToastCommom.ToastShow(ApplymentActivity.this, null, "提交失败");
                        }
                        LoadDialog.dismiss(ApplymentActivity.this);

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
//                        ToastCommom.ToastShow(ApplymentActivity.this,null,"网络错误");
                        Log.e("exception", "onError: " + ex);
                        applyment_submit.setClickable(true);
                        LoadDialog.dismiss(ApplymentActivity.this);

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {



                    }

                    @Override
                    public void onFinished() {


                    }
                });


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                applyment_submit.setClickable(true);
                LoadDialog.dismiss(ApplymentActivity.this);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        recyclerView.onActivityResult(requestCode, resultCode, data);
        piclist = recyclerView.getPhotos();
        System.out.println(piclist);
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(date) + "_" + UUID.randomUUID() + ".png";
    }

    private String imgeUpload(Bitmap photo) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" + getPhotoFileName());
        RequestParams params = new RequestParams(Utils.XUTIL_URL + "upload");//upload 是你要访问的servlet
        params.setConnectTimeout(4*1000);
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
                ToastCommom.ToastShow(ApplymentActivity.this, null, "图片上传失败");
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
