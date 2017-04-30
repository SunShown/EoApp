package com.liu.easyoffice.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by zyc on 2016/10/10.
 */

public abstract class CommonAdapter<T> extends BaseAdapter {

    List<T> data;
    Context context;
    int layout;

    public CommonAdapter(List<T> data, Context context, int layout) {
        this.data = data;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.getViewHolder(context, layout, parent, convertView);

        setcontant(viewHolder, data.get(position), position);
        return viewHolder.getConvertView();
    }

    public abstract void setcontant(ViewHolder viewHolder, T t, int position);
}

