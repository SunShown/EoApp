package com.liu.easyoffice.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liu.easyoffice.Adapter.CommonAdapter;
import com.liu.easyoffice.Adapter.ViewHolder;
import com.liu.easyoffice.ChatGroupUtils.CircularImageView;
import com.liu.easyoffice.Progressbar.LoadDialog;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.FileStore;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.pojo.ChatGroup;
import com.liu.easyoffice.pojo.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

public class ChatGroupActivity extends AppCompatActivity {

    private ListView chatGroupsLv;
    private CommonAdapter<ChatGroup> comAdaper;
    List<ChatGroup> chatGroups = new ArrayList<>();
    ChatGroupReceiver receiver = null;
    private ImageView ivNoDateNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("ChatGroup", "onCreate: ");
        setContentView(R.layout.chatgroups_show);
        initView();
        initReceiver();
        initDate();
    }

    /**
     * 注册广播
     */
    private void initReceiver() {
        receiver = new ChatGroupReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Utils.Receiver.CHANGE_TILE);
        registerReceiver(receiver, filter);
    }

    /**
     * 初始化控件
     */
    private void initDate() {
        LoadDialog loadDialog = new LoadDialog(this, false, "正在加载数据");
        loadDialog.show();
        connetToNet(MySharePreference.getCurrentUser(ChatGroupActivity.this).getUserId(), loadDialog);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        chatGroupsLv = ((ListView) findViewById(R.id.chatgroups_lv));
        ivNoDateNet = ((ImageView) findViewById(R.id.iv_no_date_net));//标识
    }

    private void initClick() {
        chatGroupsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RongIM.getInstance().startDiscussionChat(ChatGroupActivity.this, chatGroups.get(i).getGroupId(), chatGroups.get(i).getName());
                Toast.makeText(ChatGroupActivity.this, i + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 从网络获取最新数据
     *
     * @param userId
     */
    public void connetToNet(String userId, final LoadDialog dialog) {
        RequestParams params = new RequestParams(Utils.GET_CHAT_GROUP);
        params.addParameter("userId", userId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("intent", "onSuccess: " + result);
                Gson gson = new Gson();
                chatGroups.clear();
                List<ChatGroup> chats = gson.fromJson(result, new TypeToken<List<ChatGroup>>() {
                }.getType());
                if(chats.size()==0){
                    dialog.dismiss();
                    chatGroupsLv.setVisibility(View.GONE);
                    ivNoDateNet.setVisibility(View.VISIBLE);
                    ivNoDateNet.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.nodata));
                }else {
                    chatGroupsLv.setVisibility(View.VISIBLE);
                    ivNoDateNet.setVisibility(View.GONE);
                    FileStore fileStore = new FileStore(ChatGroupActivity.this, "chatGroups.txt");
                    final boolean isTrue = fileStore.saveList(chats);
                    Log.e("isTrue", "onSuccess: " + isTrue);
                    List<ChatGroup> chat = fileStore.readList();
                    Log.e("isTrue", "onSuccess: " + chat.get(0).getName());
                    chatGroups.addAll(chats);
                    if (comAdaper == null) {
                        comAdaper = new CommonAdapter<ChatGroup>(ChatGroupActivity.this, R.layout.chatgroup_item, chatGroups, null) {
                            @Override
                            public void convert(ViewHolder viewHolder, ChatGroup item, int posion) {
                                Log.e("chatgroup", "convert: " + item);
                                TextView chatName = viewHolder.getView(R.id.chatgroup_tv_name);
                                TextView chatNumber = viewHolder.getView(R.id.chatgroup_tv_count);
                                CircularImageView titleImg = viewHolder.getView(R.id.chatgroup_iv);
                                chatName.setText(item.getName());
                                chatNumber.setText("("+item.getUsers().size() + ")");
                                User[] userArray = (User[]) item.getUsers().toArray(new User[item.getUsers().size()]);
                                new DownImg(titleImg).execute(userArray);
                            }
                        };
                        chatGroupsLv.setAdapter(comAdaper);
                    } else {
                        Log.e("adapter", "onSuccess: " + " notify");
                        comAdaper.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                    initClick();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog.dismiss();
                ivNoDateNet.setVisibility(View.VISIBLE);
                chatGroupsLv.setVisibility(View.GONE);
                ivNoDateNet.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.nonet));
                Log.e("chatgroup", "onError: " + ex);
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
     * 取消注册
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    class ChatGroupReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Utils.Receiver.CHANGE_TILE)) {
                if (intent.getStringExtra("groupName") != null) {
                    initDate();
                }
            }
        }
    }

    class DownImg extends AsyncTask<User, Void, ArrayList<Bitmap>> {
        CircularImageView imgView;

        public DownImg(CircularImageView imgView) {
            this.imgView = imgView;
        }

        @Override
        protected ArrayList<Bitmap> doInBackground(User... params) {
            ArrayList<Bitmap> bimaps = new ArrayList<>();
            int size=params.length;
            if(size>5){
                size=5;
            }
            for (int i = 0; i < size; i++) {
                URL url = null;
                try {
                    Log.e("imgUrlBBBB", "doInBackground: "+params[i].getImgUrl());
                    url = new URL(Utils.IMG_URL+params[i].getImgUrl());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(8000);
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() == 200) {
                        InputStream inputStream = conn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bimaps.add(bitmap);
                        inputStream.close();
                        conn.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return bimaps;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmap) {
            imgView.setImageBitmaps(bitmap);
            super.onPostExecute(bitmap);
        }

    }
}
