package com.liu.easyoffice.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liu.easyoffice.Activity.ManagerPart.ManagerTeam;
import com.liu.easyoffice.Activity.ManagerPart.TelContactsActivity;
import com.liu.easyoffice.Adapter.CommonAdapter;
import com.liu.easyoffice.Adapter.ViewHolder;
import com.liu.easyoffice.MyView.MyListView;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.pojo.Group;
import com.liu.easyoffice.pojo.GroupMember;
import com.liu.easyoffice.pojo.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class ShowMemberActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView groupLv;
    private ListView memberLv;
    private TextView currentName;
    private LinearLayout editLayout;
    boolean isFirst = true;
    boolean flag = false;
    private TextView setPart;
    private TextView addChildPart;
    private TextView addMember;
    private LinearLayout namesLayout;
    private Context mContext;
    private CommonAdapter<Group> groupAdapter;
    private CommonAdapter<User> userAdapter;
    private List<Group> groups = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private Group currentGroup;
    private Group spGroup;
    private LinearLayout bottomLayout;
    private final static int REQUEST_INPUT_NUMBER=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_member);
        initView();
        initDate();
        itemClick();
        addView(null);
        connectToGetGroup(currentGroup.getTgId());
    }

    private void initView() {
        namesLayout = ((LinearLayout) findViewById(R.id.show_member_add_view_layout));
        groupLv = ((MyListView) findViewById(R.id.show_member_lv_group));
        memberLv = ((ListView) findViewById(R.id.show_member_lv_member));
        setPart = ((TextView) findViewById(R.id.show_member_edit_tv_set));
        addChildPart = ((TextView) findViewById(R.id.show_member_edit_tv_add_child));
        addMember = ((TextView) findViewById(R.id.show_member_edit_tv_add_member));
        bottomLayout = ((LinearLayout) findViewById(R.id.show_member_edit_bottom));
    }
    private void initDate() {
        mContext = this;
        spGroup=MySharePreference.getCurrentCroup(mContext);
        flag=getIntent().getBooleanExtra("isEdit",false);
        if(getIntent().getSerializableExtra("currentGroup")!=null){
            currentGroup= (Group) getIntent().getSerializableExtra("currentGroup");
        }else {
            currentGroup=spGroup;
        }
        if(!flag){
            bottomLayout.setVisibility(View.GONE);
        }
        groupAdapter = new CommonAdapter<Group>(mContext, groups, R.layout.show_dep_item) {
            @Override
            public void convert(ViewHolder viewHolder, final Group item, int posion) {
                TextView groupName = viewHolder.getView(R.id.show_dep_item_gname);
                final RelativeLayout iconLayout = viewHolder.getView(R.id.show_dep_item_rlt_icon);
                RelativeLayout rightLayout = viewHolder.getView(R.id.show_dep_item_rlt);//点击左边布局
                groupName.setText(item.getTgName());
                iconLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentGroup=item;
                        connectToGetGroup(item.getTgId());
                        setInAnimation(groupLv);
                        setInAnimation(memberLv);
                        addView(item);
                    }
                });
                //点击右面将group信息传到前一个页面
                rightLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.e("alldep", "onClick: "+item.getParentTgId());
                        Intent intent = new Intent();
                        intent.putExtra("parentGroup", item);
//                        AllDepActivity.this.setResult(2,intent);
//                        AllDepActivity.this.finish();
                    }
                });
            }
        };
        userAdapter = new CommonAdapter<User>(mContext, users, R.layout.show_member_item) {
            @Override
            public void convert(ViewHolder viewHolder, final User item, int posion) {
                ImageView editIv = viewHolder.getView(R.id.show_member_item_iv_edit);
                TextView name = viewHolder.getView(R.id.show_member_item_tv_name);
                final TextView userId = viewHolder.getView(R.id.show_member_tv_tel);
                ImageView imgUrl = viewHolder.getView(R.id.show_member_iv);
                if (flag) {
                    editIv.setVisibility(View.VISIBLE);
                }
                name.setText(item.getUserName());
                xUtilsImageUtils.display(imgUrl,item.getImgUrl(),true);
                viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(flag){
                            Log.e("listViewItem", "onItemClick: "+currentGroup );
                            Intent intent=new Intent(ShowMemberActivity.this,ManagerTeam.class);
                            intent.putExtra("isMember",true);//传个标志位过去，判断是否是修改员工信息
                            intent.putExtra("currentGroup", currentGroup);
                            intent.putExtra("user",item);
                            startActivityForResult(intent,4);
                        }else {
                            Intent intent=new Intent(ShowMemberActivity.this,ContactMsgActivity.class);
                            intent.putExtra("userId",item.getUserId()+"");
                            startActivity(intent);
                        }
                    }
                });

            }
        };
        groupLv.setAdapter(groupAdapter);
        memberLv.setAdapter(userAdapter);
    }

    /**
     * 显示控件
     */
    private void showView(){
        findViewById(R.id.bottom_view).setVisibility(View.VISIBLE);
        setPart.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏控件
     */
    private void hideView(){
        findViewById(R.id.bottom_view).setVisibility(View.GONE);
        setPart.setVisibility(View.GONE);
    }
    /**
     * 动态添加标题的级联
     * 并添加监听事件
     *
     * @param group
     */
    public void addView(Group group) {
        final View view = LayoutInflater.from(this).inflate(R.layout.dynamic_add_group_title, null);
        TextView gidTv = ((TextView) view.findViewById(R.id.all_dep_gid));
        final TextView parentGidTv=((TextView) view.findViewById(R.id.all_dep_parentGid));
        final TextView gNameTv = (TextView) view.findViewById(R.id.dynamic_add_group_tv_tgName);
        ImageView iv = (ImageView) view.findViewById(R.id.dynamic_add_group_iv);
        TextView count = ((TextView) view.findViewById(R.id.count));
        if (namesLayout.getChildCount() == 0) {//如果没有任何内容
            gNameTv.setText(currentGroup.getTgName());
            iv.setVisibility(View.GONE);
            gidTv.setText(currentGroup.getTgId()+"");
        } else {
            gidTv.setText(group.getTgId() + "");
            gNameTv.setText(group.getTgName());
            parentGidTv.setText(group.getParentTgId()+"");
        }
        namesLayout.addView(view);
        count.setText(namesLayout.getChildCount() + "");
        if (namesLayout.getChildCount() > 1) {//设置之前的控件颜色为蓝色，并且可以点击
            View viewchild = namesLayout.getChildAt(namesLayout.getChildCount() - 2);
            TextView tv = ((TextView) viewchild.findViewById(R.id.dynamic_add_group_tv_tgName));
            tv.setTextColor(getResources().getColor(R.color.de_blue));
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView idTv = ((TextView) v.findViewById(R.id.all_dep_gid));
                long id = Long.parseLong(idTv.getText().toString());
                TextView tv = (TextView) v.findViewById(R.id.count);

                if (Integer.parseInt(tv.getText().toString()) != namesLayout.getChildCount()) {
                    Log.e("click", "addView: " + "false");
                    setOutAnimation(groupLv);
                    setOutAnimation(memberLv);
                    TextView name = (TextView) v.findViewById(R.id.dynamic_add_group_tv_tgName);
                    connectToGetGroup(id);//再次去获取数据
                    currentGroup.setTgId(id);
                    currentGroup.setTgName(gNameTv.getText().toString());
                    if(parentGidTv.getText().toString()!=""){
                        currentGroup.setParentTgId(Long.parseLong(parentGidTv.getText().toString()));
                    }
                    name.setTextColor(getResources().getColor(R.color.ldeep_grey));
                    int start = Integer.parseInt(tv.getText().toString());
                    int count = namesLayout.getChildCount() - start;
                    deleteView(start, count);
                }
            }
        });
    }

    public void connectToGetGroup(long currentGId) {
//        groupLv.setVisibility(View.GONE);
//        AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(R.drawable.load_date);
//        final ImageView iv = (ImageView) findViewById(R.id.all_dep_iv);
//        iv.setBackgroundDrawable(drawable);
//        drawable.start();
//        iv.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(Utils.GET_GROUP_MEMBER);
        params.addParameter("companyId", MySharePreference.getCurrentCompany(mContext).getTcId());
        params.addParameter("currentGId", currentGId);
        if(currentGId==spGroup.getTgId()){
            hideView();
        }else {
            showView();
        }
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                GroupMember groupMember = gson.fromJson(result, GroupMember.class);
                List<Group> groupsNet = groupMember.getGroups();
                List<User> userNet = groupMember.getUsers();
                groups.clear();
                groups.addAll(groupsNet);
                users.clear();
                users.addAll(userNet);
                Log.e("groupSize", "size: " + groups.size());
//                iv.setVisibility(View.GONE);
//                if (groups.size() == 0) {
//                    nodateLayout.setVisibility(View.VISIBLE);//没有数据
//                } else {
//                    nodateLayout.setVisibility(View.GONE);
//                    showDeplvFirst.setVisibility(View.VISIBLE);
//                }
                groupAdapter.notifyDataSetChanged();
                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("AllDep", "onError: " + ex);
//                iv.setVisibility(View.GONE);
//                nonetLayout.setVisibility(View.VISIBLE);//没网
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
     * 删除标题级联
     *
     * @param
     */
    public void deleteView(int start, int end) {
        namesLayout.removeViews(start, end);

    }
    //设置从右向左动画
    private void setInAnimation(View view) {
        Animation animRightIn = AnimationUtils.loadAnimation(this, R.anim.listview_right_in);
        view.setAnimation(animRightIn);
    }
    //设置从左向右动画
    private void setOutAnimation(View view) {
        Animation animRight = AnimationUtils.loadAnimation(this, R.anim.listview_left_in);
        view.setAnimation(animRight);
    }
    /**
     * group点击与member点击
     */
    private void itemClick() {
        setPart.setOnClickListener(this);//编辑部门
        addChildPart.setOnClickListener(this);//添加子部门
        addMember.setOnClickListener(this);//添加员工
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.show_member_edit_tv_add_member:
                initAlertDialog(v);
                break;
            case R.id.show_member_edit_tv_add_child:
                intent.setClass(ShowMemberActivity.this, ManagerTeam.class);
                intent.putExtra("currentGroup", currentGroup);
                startActivityForResult(intent,1);
                break;
            case R.id.show_member_edit_tv_set:
                intent.setClass(ShowMemberActivity.this, ManagerTeam.class);
                intent.putExtra("edit", true);//标志位判断是否是编辑部门还是添加部门
                intent.putExtra("currentGroup", currentGroup);
                startActivityForResult(intent,2);
                break;
        }
    }
    /**
     * 点击添加子员工进行提示
     *
     * @param v 点击的按钮
     */
    public void initAlertDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.addmember_dialog, null);
        dialog.setView(view);
        dialog.show();
//        dialog.getWindow().setLayout(900,480);//设置大小
        final TextView inputTel = ((TextView) view.findViewById(R.id.addmember_dialog_tv_input));
        TextView contacts = ((TextView) view.findViewById(R.id.addmember_dialog_tv_contacts));
        final Intent intent = new Intent();
        //跳转到手动输入手机号
        inputTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(ShowMemberActivity.this, SearchMember.class);
                intent.putExtra("currentGId", currentGroup.getTgId());//传递当前id
                Log.e("currentGroupIdddddd", "onClick: "+currentGroup.getTgId());
                intent.putExtra("addnum","2");
//                startActivity(intent);
                startActivityForResult(intent,REQUEST_INPUT_NUMBER);
                dialog.dismiss();
            }
        });
        //跳转到手机通讯录
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(ShowMemberActivity.this, TelContactsActivity.class);
                intent.putExtra("currentGId", currentGroup.getTgId());//传递当前id
                startActivityForResult(intent,5);
                dialog.dismiss();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1:
                connectToGetGroup(currentGroup.getTgId());//更新数据添加子布局
                break;
            case 2:
                String groupName=data.getStringExtra("groupName");
                View view=namesLayout.getChildAt(namesLayout.getChildCount()-1);
                TextView tv= (TextView) view.findViewById(R.id.dynamic_add_group_tv_tgName);
                tv.setText(groupName);
                connectToGetGroup(currentGroup.getTgId());//更新更新当前组的信息
                break;
            case 3://删除部门
                connectToGetGroup(currentGroup.getParentTgId());
//                namesLayout.removeViewAt(namesLayout.getChildCount());
                namesLayout.getChildAt(namesLayout.getChildCount()-2).performClick();//通过代码实现按钮的点击
                break;
            case 4://更该员工信息
                connectToGetGroup(currentGroup.getTgId());
                break;
            case 5://从手机联系人读取信息
                connectToGetGroup(currentGroup.getTgId());
                break;
            case REQUEST_INPUT_NUMBER:
                Log.e("inputnumber", "onActivityResult: " );
                connectToGetGroup(currentGroup.getTgId());
        }
    }

    /**
     * 点击item成员事件
     */
    private void setOnMemberClick(final User user){
        memberLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
