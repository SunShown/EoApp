package com.liu.easyoffice.Activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.liu.easyoffice.MyView.NoScrollGridView;
import com.liu.easyoffice.MyView.NoScrollListview;

import com.liu.easyoffice.MyView.QuedingDialog;
import com.liu.easyoffice.Progressbar.LoadDialog;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.CommonAdapter;
import com.liu.easyoffice.Utils.DateUtil;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.Utils.ViewHolder;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.pojo.ApproveColumn;
import com.liu.easyoffice.pojo.ApproveState;
import com.liu.easyoffice.pojo.ApproveType;
import com.liu.easyoffice.pojo.Approveinfo;
import com.liu.easyoffice.pojo.Group;
import com.liu.easyoffice.pojo.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApproveinfoActivity extends AppCompatActivity {
    CommonAdapter<ApproveState> approvestateAdapter;
    CommonAdapter<Map<String, String>> approveinfoAdapter;
    CommonAdapter<String> appinfo_img_adapter;
    private NoScrollListview rl_appinfo_down;
    ObjectAnimator statusani;
    private TextView tv_shenpiinfotitle;
    private ListView lv_approvesate;
    private ImageView iv_approveinfo_img;
    List<String> infoimgs = new ArrayList<String>();
    private NoScrollGridView gv_app_img;
    private Approveinfo approveinfo;
    private ApproveType type;
     private  int code;
    private List<ApproveState> allstates = new ArrayList<ApproveState>();
    private List<Map<String, String>> columns = new ArrayList<Map<String, String>>();
    private TextView tv_appinfo_name;
    private TextView tv_appinfo_state;
    View view1;
    int approveId;
    private RelativeLayout rl_appinfo_up;
    private RelativeLayout rl_appimgs;
    private ImageView iv_status;
    private RadioGroup rg_tab_shenpiinfo;
    private RadioButton rb_ishenpitongyi;
    private View v_line;
    private RadioButton rb_ishenpijujue;
    private View v_line1;
    private RadioButton rb_ishenpichexiao;
    private  int backflag;
    private TextView tv_shenpiinfo_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approveinfo);
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View view1 = inflater.inflate(R.appconfig_father_item.appconfig_father_item,null);
//        lv_test.addHeaderView(view1);
        initView();

        initData();


        initEvernt();


    }


    private void initView() {

        //toolbar审批标题
        tv_shenpiinfotitle = ((TextView) findViewById(R.id.tv_shenpiinfotitle));
        tv_shenpiinfo_back = ((TextView) findViewById(R.id.tv_shenpiinfo_back));
        //审批状态listview
        lv_approvesate = ((ListView) findViewById(R.id.lv_approvesate));
        LayoutInflater inflater = LayoutInflater.from(this);
        view1 = inflater.inflate(R.layout.apprroveinfo_main, null);
        rl_appimgs = ((RelativeLayout) view1.findViewById(R.id.rl_appimgs));

        //审批详细信息listview
        rl_appinfo_down = ((NoScrollListview) view1.findViewById(R.id.rl_appinfo_down));
        //审批发起人头像
        iv_approveinfo_img = ((ImageView) view1.findViewById(R.id.iv_approveinfo_img));
        rl_appinfo_up = ((RelativeLayout) view1.findViewById(R.id.rl_appinfo_up));
        //审批人名字
        tv_appinfo_name = ((TextView) view1.findViewById(R.id.tv_appinfo_name));
        gv_app_img = ((NoScrollGridView) view1.findViewById(R.id.gv_app_img));
        iv_status = ((ImageView) view1. findViewById(R.id.iv_status));
        //审批最新状态
        tv_appinfo_state = ((TextView) view1.findViewById(R.id.tv_appinfo_state));
        rg_tab_shenpiinfo = ((RadioGroup) findViewById(R.id.rg_tab_shenpiinfo));
        rb_ishenpitongyi = ((RadioButton) findViewById(R.id.rb_ishenpitongyi));
        v_line = ((View) findViewById(R.id.v_line));
        rb_ishenpijujue = ((RadioButton) findViewById(R.id.rb_ishenpijujue));
        v_line1 = ((View) findViewById(R.id.v_line1));
        rb_ishenpichexiao = ((RadioButton) findViewById(R.id.rb_ishenpichexiao));
//        findViewById(R.id.)
//        findViewById(R.id.)
//        findViewById(R.id.)
//        findViewById(R.id.)
//        findViewById(R.id.)
//        findViewById(R.id.)


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=null;
        switch (backflag){
            case 0:
            intent=new Intent(ApproveinfoActivity.this,MyApplyActivity.class);
                break;
            case 1:
                intent=new Intent(ApproveinfoActivity.this,GetIshenPiActivity.class);
                intent.putExtra("backflag",0);

                break;
            case 2:
                intent=new Intent(ApproveinfoActivity.this,GetIshenPiActivity.class);
                intent.putExtra("backflag",1);
                break;

        }
        startActivity(intent);

    }

    private void initData() {
        LoadDialog.show(ApproveinfoActivity.this,"努力加载中");
        Intent intent = getIntent();


        approveId = intent.getIntExtra("approveId", 0);
        backflag=intent.getIntExtra("backflag",0);
        Bundle bundle1 = intent.getBundleExtra("bundle2");

        if (bundle1!=null){

           approveId=Integer.parseInt(bundle1.getString("approveId"));
            System.out.println(approveId+"=============================approveIdrs");
           code=Integer.parseInt(bundle1.getString("code"));
            System.out.println( code+"===================================== coders");

        }


        Log.e("ApproveinfoActivity", "=============approveId" + approveId);
        getData();

//        for (int i = 0; i < 10; i++) {
//            allstates.add(new ApproveState(i));
//        }
//        lv_approvesate.setAdapter(new CommonAdapter<ApproveState>(allstates,getApplicationContext(),R.appconfig_father_item.approve_state_item) {
//            @Override
//            public void setcontant(ViewHolder viewHolder, ApproveState approveState, int position) {
//
//            }
//        });


    }

    private void getData() {
        //LoadDialog.show(ApproveinfoActivity.this, "努力加载中");
        if (approveId != 0) {
            LoadDialog.show(ApproveinfoActivity.this, "努力加载中");
            RequestParams params = new RequestParams(Utils.XUTIL_URL + "getapproveinfo");
            System.out.println(Utils.XUTIL_URL + "getapproveinfo");
            params.setConnectTimeout(4*1000);
            params.addQueryStringParameter("approveid", approveId + "");
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    // System.out.println(result);
                    Gson gson = new Gson();
                    Type type = new TypeToken<Approveinfo>() {
                    }.getType();
                    approveinfo = gson.fromJson(result, type);//
                    if ( approveinfo==null){
                        String rsinfo="网络错误";
                        int imgRes=R.mipmap.fail;


                        Map<String ,Object> objectMap=new HashMap<String, Object>();
                        objectMap.put("iv_app_fail",imgRes);
                        objectMap.put("tv_app_fail",rsinfo);
                        List<Map<String ,Object>> fail=new ArrayList<Map<String, Object>>();
                        fail.add(objectMap);
                        lv_approvesate.setAdapter(new SimpleAdapter(ApproveinfoActivity.this,fail,R.layout.fail_load_head,new String[]{"iv_app_fail","tv_app_fail"},new int[]{R.id.iv_app_fail,R.id.tv_app_fail}));


                        LoadDialog.dismiss(ApproveinfoActivity.this);
                        return;
                    }
//                SharedPreferences preferences = getApplication().getSharedPreferences("user", MODE_PRIVATE);
//                final String  id = preferences.getString("id","0");
                    final long id = MySharePreference.getCurrentUser(ApproveinfoActivity.this).getId();
                    tv_shenpiinfotitle.setText(approveinfo.approvetitle);

                    ApproveState now = null;
                    List<ApproveState> a = approveinfo.allstates;
                    for (ApproveState approveState : a) {
                        if (approveState.isnew) {
                            now = approveState;
                        }
                    }

                    System.out.println("=============================ApproveState:" + approveinfo.allstates.size());
                    if (allstates != null) {
                        allstates.clear();
                    }
                    allstates.addAll(a);

                    for (ApproveState allstate : allstates) {
                        System.out.println(allstate);
                    }
                    if (approvestateAdapter == null) {
                        approvestateAdapter = new CommonAdapter<ApproveState>(allstates, getApplicationContext(), R.layout.approve_state_item) {


                            @Override
                            public void setcontant(ViewHolder viewHolder, final ApproveState approveState, int position) {
                                ImageView iv_appstate_touxiang = ((ImageView) viewHolder.getViewById(R.id.iv_appstate_touxiang));
                                ImageView iv_appstate_state_img = ((ImageView) viewHolder.getViewById(R.id.iv_appstate_state_img));

                                RelativeLayout rlappstate_item_kuang = ((RelativeLayout) viewHolder.getViewById(R.id.rlappstate_item_kuang));
                                rlappstate_item_kuang.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //   Intent intent=new Intent(ApproveinfoActivity.this,);


                                    }
                                });
                            TextView tv_appstate_comment_title=    ((TextView) viewHolder.getViewById(R.id.tv_appstate_comment_title));
                                TextView tv_appstate_name = ((TextView) viewHolder.getViewById(R.id.tv_appstate_name));
                              TextView tv_appstate_comment_data=  ((TextView) viewHolder.getViewById(R.id.tv_appstate_comment_data));
                                TextView tv_appstate_state = ((TextView) viewHolder.getViewById(R.id.tv_appstate_state));
                               NoScrollGridView gv_state_img= ((NoScrollGridView) viewHolder.getViewById(R.id.gv_state_img));
                                TextView tv_appstate_time = ((TextView) viewHolder.getViewById(R.id.tv_appstate_time));
                                View app_state_line_down = ((View) viewHolder.getViewById(R.id.app_state_line_down));
                                xUtilsImageUtils.display(iv_appstate_touxiang,approveState.getter.getImgUrl(), true);
                                User getter = approveState.getter;
                            final List<String> stateimgs=new ArrayList<String>();
                                if (stateimgs != null) {
                                    stateimgs.clear();

                                }
                                final List<String> i = approveState.imgurl;
                                if (i==null||i.size() < 1) {
                                    gv_state_img.setVisibility(View.GONE);
                                } else {
                                    gv_state_img.setVisibility(View.VISIBLE);
                                    stateimgs.addAll(i);
                                    CommonAdapter<String> approvestateimg_adapter = new CommonAdapter<String>(stateimgs, getApplicationContext(), R.layout.appinfo_img_item) {
                                        @Override
                                        public void setcontant(ViewHolder viewHolder, String s, int position) {
                                            ImageView iv_appinfoite = ((ImageView) viewHolder.getViewById(R.id.iv_appinfoite));
                                            xUtilsImageUtils.display(iv_appinfoite,  s);
                                        }
                                    };
                                    gv_state_img.setAdapter(approvestateimg_adapter);
                                    gv_state_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent intent = new Intent(ApproveinfoActivity.this, ImgActivity.class);
                                            intent.putExtra("imgurl", i.get(position));
                                            startActivity(intent);
                                        }
                                    });
                                }
                                if(approveState.comment==null||"".equals(approveState.comment.trim())){
                                    tv_appstate_comment_data.setVisibility(View.GONE);tv_appstate_comment_title.setVisibility(View.GONE);
                                }else{
                                    tv_appstate_comment_data.setVisibility(View.VISIBLE);tv_appstate_comment_title.setVisibility(View.VISIBLE);
                                tv_appstate_comment_data.setText(approveState.comment);}
                                if (getter.getId() == id) {
                                    tv_appstate_name.setText("我");
                                } else {
                                    tv_appstate_name.setText(approveState.getter.getUserName());
                                }

                                      iv_appstate_touxiang.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent intent=new Intent(ApproveinfoActivity.this,ContactMsgActivity.class);
                                              intent.putExtra("userId",approveState.getter.getUserId());
                                              if (MySharePreference.getCurrentUser(ApproveinfoActivity.this).getId()==approveState.getter.getId()){
                                                  intent.putExtra("userCard",true);}
                                              startActivity(intent);

                                          }
                                      });
                                Timestamp time = approveState.decisiontime;
                                if (time != null) {
                                    tv_appstate_time.setText(DateUtil.dateToString(time, "yyyy.MM.dd HH:mm"));
                                } else {
                                    tv_appstate_time.setVisibility(View.GONE);
                                }
//                            if(allstates.get(position).approvestateId==approveState.approvestateId){
//                                app_state_line_down.setVisibility(View.GONE);}
                                //  app_state_line_down.setTag(position);((int) app_state_line_down.getTag())
                                if (position == allstates.size() - 1) {
                                    app_state_line_down.setVisibility(View.GONE);
                                } else {
                                    app_state_line_down.setVisibility(View.VISIBLE);
                                }
                                if (approveState.isself) {
                                    if (approveState.decision == 1) {
                                        tv_appstate_state.setText("发起申请");
                                        tv_appstate_state.setTextColor(getResources().getColor(R.color.approve_agree_green));
                                        iv_appstate_state_img.setBackgroundResource(R.mipmap.app_state_yes);
                                    } else if (approveState.decision == 0) {
                                        tv_appstate_state.setText("已撤销");
                                        tv_appstate_state.setTextColor(getResources().getColor(R.color.approve_notagree_gray));
                                        iv_appstate_state_img.setBackgroundResource(R.mipmap.app_state_no);


                                    }


                                } else {
                                    if (approveState.isnew) {
                                        if (approveState.decision == 2) {
                                            tv_appstate_state.setText("审批中");
                                            tv_appstate_state.setTextColor(getResources().getColor(R.color.approve_agree_green));
                                            iv_appstate_state_img.setBackgroundResource(R.mipmap.app_state_wait);
                                        } else if (approveState.decision == 0) {
                                            tv_appstate_state.setText("已拒绝");
                                            tv_appstate_state.setTextColor(getResources().getColor(R.color.approve_notagree_gray));
                                            iv_appstate_state_img.setBackgroundResource(R.mipmap.app_state_no);


                                        } else if (approveState.decision == 1) {
                                            tv_appstate_state.setText("已同意");
                                            iv_appstate_state_img.setBackgroundResource(R.mipmap.app_state_yes);
                                            tv_appstate_state.setTextColor(getResources().getColor(R.color.approve_agree_green));
                                        }
                                    } else {
                                        if (approveState.decision == 1) {
                                            tv_appstate_state.setText("已同意");
                                            iv_appstate_state_img.setBackgroundResource(R.mipmap.app_state_yes);
                                            tv_appstate_state.setTextColor(getResources().getColor(R.color.approve_agree_green));
                                        } else if (approveState.decision == 2) {
                                            iv_appstate_state_img.setBackgroundResource(R.mipmap.app_state_nomal);
                                            tv_appstate_state.setText("");
                                        }

                                    }

                                }


                            }
                        };
                        lv_approvesate.setAdapter(approvestateAdapter);
                    } else {
//                        Log.i("===","???????????????????");
//                        allstates.clear();
                        lv_approvesate.setAdapter(approvestateAdapter);
                        approvestateAdapter.notifyDataSetChanged();
                    }
                    User sender = approveinfo.sender;
                    xUtilsImageUtils.display(iv_approveinfo_img, sender.getImgUrl(), true);
                    tv_appinfo_name.setText(sender.getUserName());
                    int state = approveinfo.state;
                    int decsion = approveinfo.decison;
                    System.out.println(state + "state======================");
                    System.out.println((state == 0) + "flag==============");
                    if (state == 0) {
                        System.out.println("9999999999999");
                        rg_tab_shenpiinfo.setVisibility(View.VISIBLE);
                        System.out.println(id + "===================id");
                        System.out.println(now.getter.getId() + "===================now.getter.getId()");
                        System.out.println(approveinfo.sender.getId() + "===================approveinfo.sender.getId()");
                        if (now.getter.getId() == id) {
                            System.out.println("11111111111111");
                            rb_ishenpitongyi.setVisibility(View.VISIBLE);
                            v_line.setVisibility(View.VISIBLE);
                            rb_ishenpijujue.setVisibility(View.VISIBLE);
                            v_line1.setVisibility(View.GONE);
                            rb_ishenpichexiao.setVisibility(View.GONE);
                            if (approveinfo.sender.getId() == id) {
//                                System.out.println("22222222222222222");
//                                rb_ishenpijujue.setVisibility(View.GONE);
//                                rb_ishenpitongyi.setVisibility(View.GONE);
//                                v_line.setVisibility(View.GONE);
                                rb_ishenpichexiao.setVisibility(View.VISIBLE);
                                v_line1.setVisibility(View.VISIBLE);
                            }

                        } else if (approveinfo.sender.getId() == id) {
                            System.out.println("3333333333333333");
                            rb_ishenpijujue.setVisibility(View.GONE);
                            rb_ishenpitongyi.setVisibility(View.GONE);
                            v_line.setVisibility(View.GONE);
                            v_line1.setVisibility(View.GONE);
                            rb_ishenpichexiao.setVisibility(View.VISIBLE);

                        }
                    } else {
                        System.out.println("44444444444");
                        rb_ishenpitongyi.setVisibility(View.GONE);
                        v_line.setVisibility(View.GONE);
                        v_line1.setVisibility(View.GONE);
                        rb_ishenpichexiao.setVisibility(View.GONE);
                        rb_ishenpijujue.setVisibility(View.GONE);
                        rg_tab_shenpiinfo.setVisibility(View.GONE);
                    }
                    String str = "";
                    tv_appinfo_state.setTextColor(getResources().getColor(R.color.approve_agree_green));


                    if (state == 0) {

                        for (ApproveState st : a) {
                            if (st.isnew) {
                                User g = st.getter;
                                if (id == g.getId()) {
                                    tv_appinfo_state.setText("等待" + "我" + "审批");
                                } else {
                                    tv_appinfo_state.setText("等待" + g.getUserName() + "审批");
                                }
                            }
                        }


                    } else if (state == 1) {
                        tv_appinfo_state.setText("已完成");
                        switch (decsion) {
                            case 1:
                                break;
                            case 2:
                                break;
                        }


                    } else if (state == 2) {
                        tv_appinfo_state.setText("已撤销");
                    }


                    List<ApproveColumn> c = approveinfo.type.columns;
                    if (columns != null) {
                        columns.clear();

                    }
                    Map<String, String> map = new HashMap<String, String>();

                    map.put("审批编号", approveinfo.approveId + "");
                    columns.add(map);
                    // System.out.println(map);
                    map = new HashMap<String, String>();
                    Group group=approveinfo.senderGroup;
                    String groupName="";
                    if(group==null){groupName="";}else {groupName=group .getTgName();}
                    map.put("所在部门",groupName);
                    columns.add(map);
                    //   System.out.println("111====================columns"+columns);
                    // System.out.println(map);

                    for (ApproveColumn column : c) {
                        map = new HashMap<String, String>();
                        map.put(column.approvecolumncnname, column.data);
                        columns.add(map);
                        // System.out.println(map);
                    }
                    System.out.println("====================columns" + columns);
                    if (approveinfoAdapter == null) {
                        approveinfoAdapter = new CommonAdapter<Map<String, String>>(columns, getApplicationContext(), R.layout.approve_info_headitem) {

                            @Override
                            public void setcontant(ViewHolder viewHolder, Map<String, String> stringStringMap, int position) {
                                TextView tv_appinfo_column = ((TextView) viewHolder.getViewById(R.id.tv_appinfo_column));
                                TextView tv_appinfo_data = ((TextView) viewHolder.getViewById(R.id.tv_appinfo_data));
                                for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {

                                    tv_appinfo_column.setText(entry.getKey() + ":");
                                    tv_appinfo_data.setText(entry.getValue());


                                }


                            }
                        };
                        rl_appinfo_down.setAdapter(approveinfoAdapter);

                    } else {
                        rl_appinfo_down.setAdapter(approveinfoAdapter);
                        approveinfoAdapter.notifyDataSetChanged();
                    }


                    if (infoimgs != null) {
                        infoimgs.clear();

                    }
                    List<String> i = approveinfo.imgurl;
                    if (i.size() < 1) {
                        rl_appimgs.setVisibility(View.GONE);
                    } else {
                        rl_appimgs.setVisibility(View.VISIBLE);
                        infoimgs.addAll(i);
                        if (appinfo_img_adapter == null) {
                            appinfo_img_adapter = new CommonAdapter<String>(infoimgs, getApplicationContext(), R.layout.appinfo_img_item) {
                                @Override
                                public void setcontant(ViewHolder viewHolder, String s, int position) {
                                    ImageView iv_appinfoite = ((ImageView) viewHolder.getViewById(R.id.iv_appinfoite));
                                    xUtilsImageUtils.display(iv_appinfoite, s);
                                }
                            };

                            gv_app_img.setAdapter(appinfo_img_adapter);
                        } else {
                            gv_app_img.setAdapter(appinfo_img_adapter);
                            appinfo_img_adapter.notifyDataSetChanged();
                        }
                    }

          ScaleAnimation sa = new ScaleAnimation(2,1,2,1, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0);


               sa.setDuration(500);  //每次时间
         //  Animation sa = AnimationUtils.loadAnimation(ApproveinfoActivity.this, R.anim.status);
////
         sa.setFillAfter(true);
                    System.out.println( " iv_status"+iv_status);
                 // iv_status.startAnimation(sa);// 保持动画的最终状态

                    if ( approveinfo.state==0){
                  iv_status.setVisibility(View.VISIBLE);
                        iv_status.setBackgroundResource(R.mipmap.status_checking);
                        iv_status.startAnimation(sa);

                    }else if (approveinfo.state==1){
                        if (approveinfo.decison==1){
                          iv_status.setVisibility(View.VISIBLE);
                            iv_status.setBackgroundResource(R.mipmap.status_joined);
                           iv_status.startAnimation(sa);


                        }else if(approveinfo.decison==2){
                           iv_status.setVisibility(View.VISIBLE);
                            iv_status.setBackgroundResource(R.mipmap.status_refused);
                         iv_status.startAnimation(sa);

                        }
                    }else if(approveinfo.state==2){
                        iv_status.setVisibility(View.VISIBLE);
                        iv_status.setBackgroundResource(R.mipmap.status_chexiao);
                        iv_status.startAnimation(sa);
                    }else{
                        iv_status.setVisibility(View.INVISIBLE);
                    }

                    LoadDialog.dismiss(ApproveinfoActivity.this);
                    lv_approvesate.addHeaderView(view1);
                if (code!=0){
                    switch (code) {

                        case 110:
                            ToastCommom.ToastShow(getApplicationContext(), null, "提交失败");
                            break;
                        case 111:
                            ToastCommom.ToastShow(getApplicationContext(), null, "提交成功");
                            break;
                        case 112:
                            ToastCommom.ToastShow(getApplicationContext(), null, "审批已被撤销无法操作");
                            break;
                        case 113:
                            ToastCommom.ToastShow(getApplicationContext(), null, "审批已被通过无法撤销");
                            break;
                        case 114:
                            ToastCommom.ToastShow(getApplicationContext(), null, "审批已被拒绝无法撤销");
                            break;
                        case 115:
                            ToastCommom.ToastShow(getApplicationContext(), null, "未知错误");
                            break;
                        case 116:
                            ToastCommom.ToastShow(getApplicationContext(), null, "你没权限");
                            break;
                        case 120:
                            ToastCommom.ToastShow(getApplicationContext(), null, "提交失败");
                            break;
                        case 121:
                            ToastCommom.ToastShow(getApplicationContext(), null, "提交成功");
                            break;
                        case 122:
                            ToastCommom.ToastShow(getApplicationContext(), null, "审批已被撤销无法操作");
                            break;
                        case 123:
                            ToastCommom.ToastShow(getApplicationContext(), null, "审批已被通过无法撤销");
                            break;
                        case 124:
                            ToastCommom.ToastShow(getApplicationContext(), null, "审批已被拒绝无法撤销");
                            break;
                        case 125:
                            ToastCommom.ToastShow(getApplicationContext(), null, "未知错误");
                            break;
                        case 126:
                            ToastCommom.ToastShow(getApplicationContext(), null, "你没权限");
                            break;
                        case 100:
                            ToastCommom.ToastShow(getApplicationContext(), null, "撤销失败");
                            break;
                        case 101:
                            ToastCommom.ToastShow(getApplicationContext(), null, "撤销成功");
                            break;
                        case 102:
                            ToastCommom.ToastShow(getApplicationContext(), null, "已被撤销过");
                            break;
                        case 103:
                            ToastCommom.ToastShow(getApplicationContext(), null, "审批已被通过无法撤销");
                            break;
                        case 104:
                            ToastCommom.ToastShow(getApplicationContext(), null, "审批已被拒绝无法撤销");
                            break;
                        case 105:
                            ToastCommom.ToastShow(getApplicationContext(), null, "未知错误");
                            break;
                        case 106:
                            ToastCommom.ToastShow(getApplicationContext(), null, "你没权限");
                            break;
                        case 000:
                            ToastCommom.ToastShow(getApplicationContext(), null, "网络错误");
                            break;
                        default:
                            ToastCommom.ToastShow(getApplicationContext(), null, "未知错误");
                            break;

                    }}


//                rg_tab_shenpiinfo = ((RadioGroup) view1.findViewById(R.id.rg_tab_shenpiinfo));
//                rb_ishenpitongyi = ((RadioButton) view1.findViewById(R.id.rb_ishenpitongyi));
//                v_line = ((View) view1.findViewById(R.id.v_line));
//                rb_ishenpijujue = ((RadioButton) view1.findViewById(R.id.rb_ishenpijujue));
//                v_line1 = ((View) view1.findViewById(R.id.v_line1));
//                rb_ishenpichexiao = ((View) view1.findViewById(R.id.rb_ishenpichexiao));


                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    ToastCommom.ToastShow(ApproveinfoActivity.this,null,"出错了");
                    String rsinfo="网络错误";
                    int imgRes=R.mipmap.fail;
                    LoadDialog.dismiss(ApproveinfoActivity.this);

                    Map<String ,Object> objectMap=new HashMap<String, Object>();
                    objectMap.put("iv_app_fail",imgRes);
                    objectMap.put("tv_app_fail",rsinfo);
                    List<Map<String ,Object>> fail=new ArrayList<Map<String, Object>>();
                    fail.add(objectMap);
                    lv_approvesate.setAdapter(new SimpleAdapter(ApproveinfoActivity.this,fail,R.layout.fail_load_head,new String[]{"iv_app_fail","tv_app_fail"},new int[]{R.id.iv_app_fail,R.id.tv_app_fail}));


                    LoadDialog.dismiss(ApproveinfoActivity.this);
                    Log.e("ex", "onError: " + ex);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                    LoadDialog.dismiss(ApproveinfoActivity.this);


                }

                @Override
                public void onFinished() {

                    LoadDialog.dismiss(ApproveinfoActivity.this);

                }
            });
        }else {

        }
    }



    private void initEvernt() {
        rl_appinfo_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ApproveinfoActivity.this,ContactMsgActivity.class);
                intent.putExtra("userId",approveinfo.sender.getUserId());
                if (MySharePreference.getCurrentUser(ApproveinfoActivity.this).getId()==approveinfo.sender.getId()){
                intent.putExtra("userCard",true);}
                startActivity(intent);
            }
        });
        tv_shenpiinfo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                switch (backflag){
                    case 0:
                        intent=new Intent(ApproveinfoActivity.this,MyApplyActivity.class);
                        break;
                    case 1:
                        intent=new Intent(ApproveinfoActivity.this,GetIshenPiActivity.class);
                        intent.putExtra("backflag",0);

                        break;
                    case 2:
                        intent=new Intent(ApproveinfoActivity.this,GetIshenPiActivity.class);
                        intent.putExtra("backflag",1);
                        break;

                }
                startActivity(intent);
            }
        });
        final Gson gson = new Gson();
        rg_tab_shenpiinfo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_ishenpitongyi:

                        rb_ishenpitongyi.setChecked(false);
                        Intent intent=new Intent(ApproveinfoActivity.this,ApproveDecisonActivity.class);
                        intent.putExtra("approveId", approveId+"");
                        intent.putExtra("decsion",1+"");

                        startActivity(intent);
                        break;
                    case R.id.rb_ishenpichexiao:
                        QuedingDialog.Builder builder = new QuedingDialog.Builder(ApproveinfoActivity.this);
                        builder.setPositiveTextView("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                RequestParams params = new RequestParams(Utils.XUTIL_URL + "shenpicancel");
                                System.out.println(Utils.XUTIL_URL + "shenpicancel");
                                System.out.println(gson.toJson(new ApproveState(approveinfo.approveId, MySharePreference.getCurrentUser(ApproveinfoActivity.this), new Timestamp(new Date().getTime())), ApproveState.class));
                                String str = gson.toJson(new ApproveState(approveinfo.approveId, MySharePreference.getCurrentUser(ApproveinfoActivity.this), new Timestamp(new Date().getTime())), ApproveState.class);
                                System.out.println(str);
                                params.addParameter("statejson", str);
                                x.http().get(params, new Callback.CommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        System.out.println("result+++++++++++++++++++++" + result);
                                        Integer code = Integer.parseInt(result.trim());
                                        System.out.println("code+++++++++++++++++++++" + code);
                                        Intent intent1 = new Intent("com.liu.easyoffice.approveinfoupdate");
                                        Bundle bundle = new Bundle();
                                        bundle.putString("approveId",approveId+"");
                                        bundle.putString("code",code+"");
                                        intent1.putExtra("bundle1",bundle);
                                        sendBroadcast(intent1);
                                        finish();



                                    }

                                    @Override
                                    public void onError(Throwable ex, boolean isOnCallback) {

                                    }

                                    @Override
                                    public void onCancelled(CancelledException cex) {
                                        LoadDialog.dismiss(ApproveinfoActivity.this);

                                    }

                                    @Override
                                    public void onFinished() {


                                    }
                                });

                                dialog.dismiss();


                            }
                        });
                        builder.setNegativeTextView("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();


                        rb_ishenpichexiao.setChecked(false);
                        break;
                    case R.id.rb_ishenpijujue:
                        Intent intent1=new Intent(ApproveinfoActivity.this,ApproveDecisonActivity.class);
                        intent1.putExtra("approveId", approveId+"");
                        intent1.putExtra("decsion",0+"");

                       startActivity(intent1);
                        rb_ishenpichexiao.setChecked(false);
                        break;

                }
            }
        });
        gv_app_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ApproveinfoActivity.this, ImgActivity.class);
                intent.putExtra("imgurl", infoimgs.get(position));
                startActivity(intent);


            }
        });






    }


}

