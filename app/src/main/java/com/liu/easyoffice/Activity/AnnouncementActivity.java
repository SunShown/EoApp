package com.liu.easyoffice.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liu.easyoffice.Adapter.AnnouncementAdapter;
import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.DividerItemDecoration;
import com.liu.easyoffice.Utils.ToastCommom;
import com.liu.easyoffice.Utils.Utils;
import com.liu.easyoffice.pojo.Announcement;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementActivity extends AppCompatActivity {

    private TextView mToolTitle;
    private TextView mToolText;
    private ImageView mToolBack;
    public static final int REQUEST_SENDANNOUNCEMENT=1;
    private RecyclerView mRecycler;
    private List<Announcement> datalist=new ArrayList<>();
    private Context mcontext;
    private Long test=1L;
    private AnnouncementAdapter mAdapter;
    private Announcement a=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext=this;
        setContentView(R.layout.activity_announcement);
        initData();
        initView();
    }

    private void initData() {
        RequestParams rp=new RequestParams(Utils.GET_ALL_ANNOUNCEMENT);
        x.http().get(rp, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                Gson gson=new Gson();
                Type type1 = new TypeToken<List<Announcement>>() {}.getType();
                datalist.clear();
                List<Announcement> lists=gson.fromJson(result,type1);
                for(int i=lists.size()-1;i>=0;i--){
                    datalist.add(lists.get(i));
                }
//                datalist.addAll(lists);

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastCommom.ToastShow(mcontext,null,"服务器异常");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initView() {
        mToolTitle = ((TextView) findViewById(R.id.mtoolbar_text));
        mToolTitle.setText("公告");
        mToolText = ((TextView) findViewById(R.id.mtoolbar_tv));
        mToolText.setText("发公告");
        mToolBack = ((ImageView) findViewById(R.id.tool_back));
        mToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnnouncementActivity.this.finish();
            }
        });
        SharedPreferences sp=getApplicationContext().getSharedPreferences("user",MODE_PRIVATE);
        final String name=sp.getString("userName","用户不存在");
        final Long id=sp.getLong("id",0L);
        mToolText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AnnouncementActivity.this,SendAnnouncementActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("id",id);
                startActivityForResult(intent,REQUEST_SENDANNOUNCEMENT);
            }
        });
        mRecycler = ((RecyclerView) findViewById(R.id.announcement_recycler));
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new AnnouncementAdapter(AnnouncementActivity.this,datalist);
        mAdapter.setOnItemClickListener(new AnnouncementAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent=new Intent(AnnouncementActivity.this,AnnouncementShowActivity.class);
                intent.putExtra("showAnnouncement",datalist.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        mRecycler.setAdapter(mAdapter);
        mRecycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mRecycler.setItemAnimator(new DefaultItemAnimator());
    }
//    private class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.MyaViewHolder>{
//
//        @Override
//        public AnnouncementAdapter.MyaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            MyaViewHolder holder=new MyaViewHolder(LayoutInflater.from(AnnouncementActivity.this).inflate(R.layout.announcement_recycler_item,parent,false));
//            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(AnnouncementAdapter.MyaViewHolder holder, final int position) {
//            System.out.println(holder.mTitle+"hhhhhhhhhhhhhh");
//            if(datalist.get(position).getImg_1()==null||"".equals(datalist.get(position).getImg_1())){
//                holder.mPic.setImageResource(R.mipmap.defaltbackground);
//            }else{
//                xUtilsImageUtils.display(holder.mPic,datalist.get(position).getImg_1());
//            }
//            holder.mTitle.setText(datalist.get(position).getAtitle());
//            holder.mAuthottime.setText(datalist.get(position).getAuthor()+"  "+datalist.get(position).getAtime());
//        }
//        public void addData(Announcement announcement){
//            datalist.add(0,announcement);
//            notifyItemInserted(0);
//        }
//
//        @Override
//        public int getItemCount() {
//            return datalist.size();
//        }
//        class MyaViewHolder extends RecyclerView.ViewHolder {
//
////            RelativeLayout mRelative;
//            ImageView mPic;
//            TextView mTitle;
//            TextView mAuthottime;
//
//            public MyaViewHolder(View view)
//            {
//                super(view);
//                mPic = ((ImageView) view.findViewById(R.id.announcement_img));
//                mTitle = ((TextView) view.findViewById(R.id.announcement_title));
//                mAuthottime = ((TextView) view.findViewById(R.id.authorandtime));
//            }
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_SENDANNOUNCEMENT:
                switch(requestCode){
                    case SendAnnouncementActivity.ANNOUNCEMENT_SUCCESS:
                        a=new Announcement();
                        a=(Announcement) data.getSerializableExtra("announcement");
//                        a=(Announcement) getIntent().getSerializableExtra("announcement");
                        mAdapter.addData(a);
                        System.out.println(a+"Announcement");
                        break;
                }
                break;
        }
    }
}
