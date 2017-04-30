package com.liu.easyoffice.Utils;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zyc on 2016/10/10.
 */

public class ViewHolder {
 private View convertView;
    private SparseArray<View> sparseArray;

    public View getConvertView() {
        return convertView;
    }

    public ViewHolder(Context context, int layout, ViewGroup parent) {
        this.convertView = LayoutInflater.from(context).inflate(layout,null);
        convertView.setTag(this);
        sparseArray=new SparseArray<View>();

    }

    public static ViewHolder getViewHolder(Context context, int layout, ViewGroup parent, View convertView){
ViewHolder viewHolder;
        if(convertView==null){
         viewHolder=new ViewHolder(context,layout,parent);

        }else{
            viewHolder= ((ViewHolder) convertView.getTag());

        }
return viewHolder;
    }

    public <T extends View> T getViewById(int resourceId){
        View v= sparseArray.get(resourceId);
        //没有找到view
        if(v==null){
            v= convertView.findViewById(resourceId);
            sparseArray.put(resourceId,v);
        }
        return (T)v;
    }


}
