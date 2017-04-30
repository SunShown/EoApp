package com.liu.easyoffice.Adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hui on 2016/10/12.
 */

public class ViewHolder {
    SparseArray<View> mView;
    private View mConvertView;

    public ViewHolder(Context context, View convertview, ViewGroup parent, int layoutId) {
        mView = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent);
        mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context, View convertvie, ViewGroup parent, int layoutId) {
        if (convertvie == null) {
            return new ViewHolder(context, convertvie, parent, layoutId);
        }
        Log.e("tag", "get: "+convertvie.getTag() );
        return (ViewHolder) convertvie.getTag();
    }

    public <T extends View> T getView(int viewId) {
        View view = mView.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mView.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }
}
