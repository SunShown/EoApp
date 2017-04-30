package com.liu.easyoffice.Activity.ManagerPart;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liu.easyoffice.Adapter.CommonAdapter;
import com.liu.easyoffice.Adapter.ViewHolder;
import com.liu.easyoffice.Pinyin.PinyinComparator;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.FileStore;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.TelContacts;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.pojo.Group;
import com.liu.easyoffice.pojo.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TelContactsActivity extends AppCompatActivity {
    private List<User> users = new ArrayList<>();
    private ListView usersLv;
    private Context mContext;
    //
    private List<Boolean> btnposition = new ArrayList<>();//记录position位置
    private CommonAdapter adapter = null;
    private Long currentGid = null;
    private List<User> netUser = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel_contacts);
        initView();
        initDate();
        connectsFromNet(readContactsFromFile());
        saveContactsFromFilr();
    }

    private void initView() {
        usersLv = ((ListView) findViewById(R.id.tel_contacts_lv));
        mContext = this;
        currentGid = getIntent().getLongExtra("currentGId", -1);
        Log.e("currentId", "initView: " + currentGid);
    }

    private void initDate() {
        adapter = new CommonAdapter<User>(mContext, R.layout.telcontacts_item, users, null) {
            @Override
            public void convert(ViewHolder viewHolder, final User item, int posion) {
                Log.e("img", "convert: " + users.size());
                TextView userName = viewHolder.getView(R.id.tel_contacts_item_name);
                TextView userTel = viewHolder.getView(R.id.tel_contacts_item_number);
                ImageView iv = viewHolder.getView(R.id.tel_contacts_item_iv_title);
//                x.image().bind(iv, item.getImgUrl());
                xUtilsImageUtils.display(iv,item.getImgUrl(),true);
                TextView letter = viewHolder.getView(R.id.tel_contacts_item_letter);
                final TextView groupMsg = viewHolder.getView(R.id.tel_contacts_item_tv_groupmsg);
                final Button addBtn = viewHolder.getView(R.id.tel_contacts_item_btn_add);
                final Group group = item.getGroup();
                Log.e("groupTel", "convert: "+group.getTgId());
                final int pos = posion;
                if (group.getTgId() != null) {//该用户在当前公司.如果存在则显示部门，如果不存在则添加点击按钮
                    Log.e("groupTel", "visible");
                    groupMsg.setVisibility(View.VISIBLE);
                    btnposition.set(pos, false);
                    setVisible(pos, addBtn);
                    groupMsg.setText(group.getTgName());
                } else {
                    btnposition.set(pos, true);
                    setVisible(pos, addBtn);
//                    addBtn.setVisibility(View.VISIBLE);
                    groupMsg.setVisibility(View.GONE);
                    addBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnposition.set(pos, false);
                            group.setTgId(currentGid);
                            item.setGroup(group);
                            addMember(item, addBtn, groupMsg);
                        }
                    });
                }
                //隐藏字母开头 逻辑处理
                int section = getSectionForPosition(posion);
                if (posion == getPositionForSection(section)) {
                    letter.setVisibility(View.VISIBLE);
                    letter.setText(item.getSortLetters().charAt(0) + "");
                } else {
                    letter.setVisibility(View.GONE);
                }

                userName.setText(item.getUserName());
                userTel.setText(item.getUserId());
            }
        };
        usersLv.setAdapter(adapter);
    }


    public int getSectionForPosition(int position) {
        return users.get(position).getSortLetters().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < users.size(); i++) {
            String sortStr = users.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 添加成员
     *
     * @param user
     */
    public void addMember(final User user, final Button btn, final TextView tv) {
        RequestParams param = new RequestParams(Utils.MANAGER_GROUP);
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        param.addParameter("addMem", userJson);
        x.http().get(param, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("200")) {
                    ToastCommom.ToastShow(mContext, null, "添加成功");
                    btn.setVisibility(View.GONE);
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("已添加");
                    setResult(5);
                    finish();
//                    Intent intent=new Intent();
//                    intent.putExtra("currentId",currentGid);
//                    intent.putExtra("userName",user.getUserName());
//                    intent.putExtra("tel",user.getUserId());
//                    intent.putExtra("imgUrl",user.getImgUrl());
//                    intent.setAction(Utils.Receiver.ADD_MEMBER);//通知前一个更新页面
//                    sendBroadcast(intent);
                } else {
                    ToastCommom.ToastShow(mContext, null, "添加失败");
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

    public void connectsFromNet(final List<User> usersFromFile) {
        RequestParams params = new RequestParams(Utils.PHONE_CONTACTS);
        Gson gson = new Gson();
        String usersJson = gson.toJson(usersFromFile);
        params.addBodyParameter("contacts", usersJson);//本地联系人信息
        params.addBodyParameter("companyId", MySharePreference.getCurrentCompany(TelContactsActivity.this).getTcId() + "");//当前公司id
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("telex", "onSuccess: " + result);
                Gson gson = new Gson();
                users.clear();
                List<User> userFormNet = gson.fromJson(result, new TypeToken<List<User>>() {
                }.getType());
                for (User user : userFormNet) {
                    user.setSortLetters(TelContacts.getPinYinHeadChar(user.getUserName()));
                    users.add(user);
//                    if(user.getGroup())
                    btnposition.add(false);
                }
                Collections.sort(users, new PinyinComparator());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("telex", "onError: " + ex);
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
     * 从文件读取联系人并发送到服务器
     *
     * @return
     */
    public List<User> readContactsFromFile() {
        FileStore fileStore = new FileStore(this, "contacts.txt");
        List<User> users = fileStore.readList();
        return users;
    }

    public void saveContactsFromFilr() {
        List<User> users = TelContacts.getContactsFromPhone(getApplicationContext());
        FileStore fileStore = new FileStore(getApplicationContext(), "contacts.txt");
        fileStore.saveList(users);
    }

    public void setVisible(int position, Button btn) {
        boolean visible = btnposition.get(position);
        if (visible) {
            Log.e("visible", "setVisible: " + position);
            btn.setVisibility(View.VISIBLE);
        }else {
            btn.setVisibility(View.GONE);
        }

    }
}
