package com.liu.easyoffice.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by hui on 2016/10/12.
 */

public abstract class CommonAdapter<T>  extends BaseAdapter{
    protected Context context;
    protected List<T> list;
    protected ViewGroup viewGroup;
    protected  int layoutId;
    public CommonAdapter(Context context, List<T> list, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
    }

    public CommonAdapter(Context context, int layoutId, List<T> list,ViewGroup viewGroup) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.viewGroup=viewGroup;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=getHolder(view,this.viewGroup);
        convert(viewHolder,list.get(i),i);
        return viewHolder.getConvertView();
    }
    public abstract void convert(ViewHolder viewHolder,T item,int posion);
    private ViewHolder getHolder(View converView,ViewGroup parent){
        return ViewHolder.get(context,converView,parent,layoutId);
    }
}
