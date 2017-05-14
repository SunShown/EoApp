package com.liu.easyoffice.Activity.ManagerPart;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liu.easyoffice.Progressbar.LoadDialog;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.pojo.Group;
import com.liu.easyoffice.pojo.GroupMember;
import com.liu.easyoffice.pojo.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.sql.Timestamp;

public class ManagerTeam extends AppCompatActivity implements View.OnClickListener {

    private Button confirmBtn;
    private EditText etName;
    private ImageView delIv;
    private Long parentId;//从上个页面传过来的的id
    private String parentName;//从上一个页面传过来的组名
    private TextView tvParentName;
    private EditText description;
    private TextView editDelete;
    private TextView chooseLeader;
    private boolean isEdit = false;//是否是编辑页面
    private RelativeLayout rltDescription;
    private RelativeLayout setLeaderLayout;
    private Group currentGroup;
    private final int REQUEST_PARENT_GROUP = 1;
    private final int REQUEST_CHOOSE_LEADER = 2;
    private GroupMember groupMember = null;
    private Long lastParentid;//最后要传到服务器的parentid
    private Long leaderId = 0L;//要传到服务器的主管id
    private TextView partNameTitle;
    private boolean isMember;//判断是否是编辑员工界面
    private RelativeLayout phoneLayout;
    private TextView departTitle;
    private User editUser;//编辑用户信息
    private TextView phoneEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_team);
        initView();
        initMsg();
        initClick();

    }

    private void initView() {
        groupMember = new GroupMember();
        confirmBtn = ((Button) findViewById(R.id.manager_team_confirm));
        etName = ((EditText) findViewById(R.id.manager_et_part_name));
        delIv = ((ImageView) findViewById(R.id.manager_iv_delte));
        tvParentName = ((TextView) findViewById(R.id.manager_tv_parentName));
        description = ((EditText) findViewById(R.id.manager_et_descript));
        editDelete = ((TextView) findViewById(R.id.manager_edit_depart_delete));
        chooseLeader = ((TextView) findViewById(R.id.manager_edit_depart_tv_leader));
        rltDescription = ((RelativeLayout) findViewById(R.id.manager_add_rlt_description));
        setLeaderLayout = ((RelativeLayout) findViewById(R.id.manager_edit_rlt_set_leader));
        partNameTitle = ((TextView) findViewById(R.id.manager_tv_part_name));//部门名称与成员名称
        phoneLayout = ((RelativeLayout) findViewById(R.id.edit_menmber_phone_layout));//手机号布局
        departTitle = ((TextView) findViewById(R.id.manager_tv_depart_title));
        phoneEt = ((TextView) findViewById(R.id.manager_tv_phone));
    }

    private void initMsg() {
        currentGroup = (Group) getIntent().getSerializableExtra("currentGroup");//获取从上一个页面传过来的值
        isMember = getIntent().getBooleanExtra("isMember", false);
        parentName = currentGroup.getTgName();
        Log.e("managerTeam", "parentName" + parentName);
        isEdit = getIntent().getBooleanExtra("edit", false);
        if (isMember) {//判断是否是编辑员工信息界面
            editUser = (User) getIntent().getSerializableExtra("user");
            Log.e("liufeixuan", "initMsg: "+editUser.getBirthday() );
            partNameTitle.setText("姓名");
            etName.setText(editUser.getUserName());
            phoneLayout.setVisibility(View.VISIBLE);
            phoneEt.setText(editUser.getUserId());
            departTitle.setText("设置部门");
            setLeaderLayout.setVisibility(View.GONE);
            rltDescription.setVisibility(View.GONE);
            editDelete.setText("删除员工");
            parentId = currentGroup.getTgId();
            tvParentName.setText(parentName);
        } else if (isEdit) {//如果是编辑页面
            parentId = currentGroup.getParentTgId();
            rltDescription.setVisibility(View.GONE);
            setLeaderLayout.setVisibility(View.VISIBLE);
            editDelete.setVisibility(View.VISIBLE);
            etName.setText(parentName);
            getParentGroupAndLeader();
        } else {//添加子部门
            parentId = currentGroup.getTgId();
            rltDescription.setVisibility(View.VISIBLE);
            setLeaderLayout.setVisibility(View.GONE);
            editDelete.setVisibility(View.GONE);
            tvParentName.setText(parentName);
        }
        lastParentid = parentId;//默认id
        if (etName.getText().toString().trim().equals("")) {
            delIv.setVisibility(View.GONE);
            confirmBtn.setClickable(false);
            confirmBtn.setBackgroundResource(R.drawable.btn_unclicked);
        }

        //监听编辑框是否有字
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!etName.getText().toString().trim().equals("")) {
                    delIv.setVisibility(View.VISIBLE);
                    confirmBtn.setClickable(true);
                    confirmBtn.setBackgroundResource(R.drawable.chat_group_btn_quit);
                } else {
                    delIv.setVisibility(View.GONE);
                    confirmBtn.setClickable(false);
                    confirmBtn.setBackgroundResource(R.drawable.btn_unclicked);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initClick() {
        delIv.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        chooseLeader.setOnClickListener(this);
        tvParentName.setOnClickListener(this);
        editDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Group group = new Group();
        group.setTgName(etName.getText().toString());
        if (!isEdit) {
            group.setParentTgId(lastParentid);
            group.setDescription(description.getText().toString());
            group.setTcId(MySharePreference.getCurrentCompany(ManagerTeam.this).getTcId());
        } else {
            Log.e("parentId", "onClick: " + lastParentid);
            group.setParentTgId(lastParentid);
            group.setTgLeaderId(leaderId);
            group.setTgId(currentGroup.getTgId());
        }
        if(isMember){
            if (editUser != null) {
                editUser.setUserName(etName.getText().toString());
                Group userGroup = new Group();
                userGroup.setTgId(lastParentid);
                editUser.setGroup(userGroup);
            }
        }
        Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.manager_iv_delte:
                etName.setText("");
                break;
            case R.id.manager_team_confirm:
                LoadDialog.show(ManagerTeam.this, "正在添加");

                connetToServe(group, isEdit, false, editUser);
                break;
            case R.id.manager_tv_parentName://选择父级部门
                intent.putExtra("currentGroup", currentGroup);//将当前的部门信息传到选择部门信息的界面(防止选择该部门或者子部门作为父部门)
                intent.setClass(ManagerTeam.this, AllDepActivity.class);
                startActivityForResult(intent, REQUEST_PARENT_GROUP);
                break;
            case R.id.manager_edit_depart_tv_leader://选择主管
                intent.setClass(ManagerTeam.this, DepLeaderActivity.class);
                intent.putExtra("currentGroup", currentGroup);//将当前的group传到另一个页面
                startActivityForResult(intent, REQUEST_CHOOSE_LEADER);
                break;
            case R.id.manager_edit_depart_delete:
//                intent.setClass(ManagerTeam.this,)
                Dialog dialog = new AlertDialog.Builder(ManagerTeam.this).
                        setTitle("确定要删除？").
                        setIcon(R.drawable.rc_ic_warning).
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                connetToServe(null, true, true, editUser);
                            }
                        }).
                        setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
        }
    }

    private void connetToServe(final Group group, final boolean isEdit, final boolean isDelete, User user) {
        RequestParams params = new RequestParams(Utils.MANAGER_GROUP);
        Gson gson = new Gson();
        String groupMsg = gson.toJson(group);
        if (isDelete) {
            if(isMember){

                Gson userGson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
//                user.setBirthday(Timestamp.valuseOf("1992-08-06"));
                Log.e("liufeixuan", "connetToServe: "+user.getBirthday());
                String userStr = userGson.toJson(user);
                params.addParameter("editMember", "0");
                params.addParameter("deleteMember","0");
                params.addParameter("user",userStr);
            }else {
                params.addParameter("delete", "0");
                params.addParameter("currentGid", currentGroup.getTgId());
                params.addParameter("targetGid", MySharePreference.getCurrentCroup(ManagerTeam.this).getTgId());
            }
        } else {
            params.addParameter("group", groupMsg);
            if (isMember) {
                params.addParameter("editMember", "0");
                Gson userGson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
                String userStr = userGson.toJson(user);
                params.addParameter("user", userStr);
            } else if (isEdit) {//是否是编辑页面
                params.addParameter("update", 0);
            } else {
                params.addParameter("addChild", "0");
            }
        }
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("Manager", "onSuccess: " + result);
                if (isDelete) {
                    if(isMember){
                        if(result.equals("200")){
                            ToastCommom.ToastShow(ManagerTeam.this, null, "删除成功");
                            setResult(4);
                            finish();
                        }else {
                            ToastCommom.ToastShow(ManagerTeam.this,null,"网络繁忙，请重试");
                        }
                    }else {
                        ToastCommom.ToastShow(ManagerTeam.this, null, "删除成功");
                        setResult(3);
                        finish();
                    }

                } else {
                    if (isEdit) {
                        if (result.equals("200")) {
                            Intent intent = new Intent();
                            intent.putExtra("groupName", etName.getText().toString());//将当前的组名传过去更新页面
                            setResult(2, intent);//更新成功
                            ToastCommom.ToastShow(ManagerTeam.this, null, "更新成功");
                            finish();
                        } else {
                            LoadDialog.dismiss(ManagerTeam.this);
                            ToastCommom.ToastShow(ManagerTeam.this, null, "更新失败");
                        }
                    }
                    if (isMember) {
                        if(result.equals("200")){
                            LoadDialog.dismiss(ManagerTeam.this);
                            ToastCommom.ToastShow(ManagerTeam.this, null, "添加成功");
                            setResult(4);
                            finish();
                        }
                    } else {
                        if (!result.equals("0")) {//插入成功
                            LoadDialog.dismiss(ManagerTeam.this);
                            Log.e("receiver", "onSuccess: " + result);
                            ToastCommom.ToastShow(ManagerTeam.this, null, "添加成功");
                            Intent intent = new Intent();
                            intent.putExtra("currentId", parentId);//只让这个布局的父界面添加
                            intent.putExtra("groupName", group.getTgName());
                            intent.putExtra("groupId", Long.parseLong(result));
                            setResult(1, intent);
                            finish();
                        } else {
                            LoadDialog.dismiss(ManagerTeam.this);
                            ToastCommom.ToastShow(ManagerTeam.this, null, "添加失败");
                        }
                    }
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(ManagerTeam.this);
                Log.e("ex", "onError: " + ex.getMessage());
                ToastCommom.ToastShow(ManagerTeam.this, null, "请检查网络");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getParentGroupAndLeader() {
        RequestParams params = new RequestParams(Utils.GET_PARENTGROUP_LEADER);
        Log.e("Manager", "getParentGroupAndLeader: " + currentGroup);
        params.addParameter("parentId", currentGroup.getParentTgId());
        params.addParameter("currentTgId", currentGroup.getTgId());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("magener", "onSuccess: " + result);
                Gson gson = new Gson();
                GroupMember groupMember = gson.fromJson(result, GroupMember.class);
                Log.e("mange", "grouipMember" + groupMember);
                if (groupMember.getUsers().size() != 0) {
                    User user = groupMember.getUsers().get(0);
                    chooseLeader.setText(user.getUserName());//设置leader姓名
                } else {
                    chooseLeader.setText("请选择一名主管");
                }
                if (groupMember.getGroups().size() != 0) {
                    Group group = groupMember.getGroups().get(0);
                    tvParentName.setText(group.getTgName());
                } else {
                    tvParentName.setText("该部门已是最高等级");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("manager", "onError: " + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 从上一个页面传值过来
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 2://如果是选择部门回传过来的
                Group group = (Group) data.getSerializableExtra("parentGroup");
                tvParentName.setText(group.getTgName());
                lastParentid = group.getTgId();//更新id
                Log.e("parentId", "传值 " + lastParentid);
                break;
            case 3://选择主管传过来的
                User user = (User) data.getSerializableExtra("leader");
                chooseLeader.setText(user.getUserName());
                leaderId = user.getId();//更新负责人id
                break;
        }
    }
}
