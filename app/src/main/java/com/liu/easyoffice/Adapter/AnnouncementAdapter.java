package com.liu.easyoffice.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.pojo.Announcement;

import java.util.List;


public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.MyaViewHolder>{
    private List<Announcement> datalist;
    private Context mcontext;
    private LayoutInflater inflater;
    private OnItemClickListener mOnItemClickListener;
    public AnnouncementAdapter(Context context, List<Announcement> datas){
        this.mcontext=context;
        this.datalist=datas;
        inflater=LayoutInflater.from(mcontext);
        System.out.println("aha");
    }
    @Override
    public MyaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyaViewHolder holder=new MyaViewHolder(inflater.inflate(R.layout.announcement_recycler_item,parent,false));
        return holder;
    }
    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this. mOnItemClickListener=onItemClickListener;
    }
    @Override
    public void onBindViewHolder(MyaViewHolder holder, final int position) {
        if (datalist.get(position).getImg_1() == null || "".equals(datalist.get(position).getImg_1())) {
            holder.mPic.setImageResource(R.mipmap.backpic);
        } else {
            xUtilsImageUtils.display(holder.mPic, datalist.get(position).getImg_1());
        }
        holder.mTitle.setText(datalist.get(position).getAtitle());
        holder.mAuthottime.setText(datalist.get(position).getAuthor() + "  " + datalist.get(position).getAtime());
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
        }
    }
    public void addData(Announcement announcement){
        datalist.add(0,announcement);
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
    class MyaViewHolder extends RecyclerView.ViewHolder {

        ImageView mPic;
        TextView mTitle;
        TextView mAuthottime;

        public MyaViewHolder(View view)
        {
            super(view);
            mPic = ((ImageView) view.findViewById(R.id.announcement_img));
            mTitle = ((TextView) view.findViewById(R.id.announcement_title));
            mAuthottime = ((TextView) view.findViewById(R.id.authorandtime));
        }
    }
}