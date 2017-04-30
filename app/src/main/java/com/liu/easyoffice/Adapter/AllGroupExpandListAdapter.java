package com.liu.easyoffice.Adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.easyoffice.R;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.Utils.xUtilsImageUtils;
import com.liu.easyoffice.pojo.Group;
import com.liu.easyoffice.pojo.User;

import org.w3c.dom.Text;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hui on 2016/10/12.
 */

public class AllGroupExpandListAdapter extends BaseExpandableListAdapter {

    private List<Group> groups;
    private Context context;
    private OnCheckBoxClickLinstener monCheckBoxClickLinstener;
    private OnLoadImg mOnLoadImg;
    public AllGroupExpandListAdapter(Context context, List<Group> groups,OnCheckBoxClickLinstener monCheckBoxClickLinstener) {
        this.groups = groups;
        this.context = context;
        this.monCheckBoxClickLinstener=monCheckBoxClickLinstener;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return groups.get(i).getUsers().size();
    }

    @Override
    public Object getGroup(int i) {
        return groups.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return groups.get(i).getUsers().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=ViewHolder.get(context,view,null,R.layout.creat_group_item);
        TextView tcName = viewHolder.getView(R.id.create_group_item_tv);
        tcName.setText(groups.get(i).getTgName());
        ImageView iv = viewHolder.getView(R.id.create_group_item_iv);
        if (b) {
            iv.setBackgroundResource(R.mipmap.pulldown);
        } else {
            iv.setBackgroundResource(R.mipmap.enter);
        }
        return viewHolder.getConvertView();
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        //保存当前状态 在user里添加一个check属性（网上的方法，😂）
        User user=groups.get(i).getUsers().get(i1);
        if(user.getId()==MySharePreference.getCurrentUser(context).getId()){
            user.setExist(true);
        }
        Log.e("View", "getChildView: "+view);
        if (view== null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.choose_contacts_item, null);
        }
        TextView userName= (TextView) view.findViewById(R.id.choose_contacts_item_name);
        TextView url= (TextView) view.findViewById(R.id.choose_contacts_item_imgUrl);
        ImageView iv= (ImageView) view.findViewById(R.id.choose_contacts_item_iv);
        TextView tv= (TextView) view.findViewById(R.id.choose_contacts_item_id);
        CheckBox checkBox=(CheckBox)view.findViewById(R.id.choose_contacts_item_cb);
        Log.e("getChildViewUserId222",checkBox.isChecked()+"");
        ImageView checked= (ImageView) view.findViewById(R.id.choose_contacts_item_iv_checked);
        if(user.isExist()){
            checkBox.setVisibility(View.GONE);
            checked.setVisibility(View.VISIBLE);
        }else {
            checkBox.setVisibility(View.VISIBLE);
            checked.setVisibility(View.GONE);
            checkBox.setChecked(user.isChecked());
            checkBox.setOnClickListener(new Child_CheckBox_Click(i,i1));
        }
        userName.setText(user.getUserName());
        tv.setText(user.getUserId());
        url.setText(user.getImgUrl());
        xUtilsImageUtils.display(iv,user.getImgUrl(),true);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        if(groups.get(i).getUsers().get(i1).isExist()){
            return false;
        }
        return true;
    }

    class Child_CheckBox_Click implements View.OnClickListener {
        private int groupPosition;
        private int childPosition;

        Child_CheckBox_Click(int groupPosition, int childPosition) {
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
        }
        public void onClick(View v) {
           monCheckBoxClickLinstener.handleClick(childPosition,groupPosition);
        }
    }
    //自定义接口，主类趋实现，因为checkBox 需要点击与item点击不能冲突
    public interface OnCheckBoxClickLinstener{
        void handleClick(int childPosition,int groupPosition);
    }
    public interface OnLoadImg{
        void loadImg(int childPosition,int groupPosition);
    }

}
