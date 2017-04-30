package com.liu.easyoffice.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.liu.easyoffice.R;

/**
 * Created by Administrator on 2016/10/19.
 */

public class SignStatisticsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SignStatisticsFragment";
    private View v;

    private Fragment mTeamCheckFragment;
    private Fragment mPersonCheckFragment;
    private Fragment[] checkFragments;
    private Button[] checkButtons;
    int oldIndex;//用户看到的item
    int newIndex;//用户即将看到的item

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.activity_sign_statistics_fragment,null);
        Log.i(TAG,"测试统计fragment是否运行！");
        initData();
        initView();
        initEvent();
        return v;
    }

    private void initData() {
        mTeamCheckFragment = new TeamCheckFragment();
        mPersonCheckFragment = new PersonCheckFragment();
        checkFragments = new Fragment[]{mTeamCheckFragment,mPersonCheckFragment};
        checkButtons = new Button[]{((Button) v.findViewById(R.id.button_team)),((Button) v.findViewById(R.id.button_person))} ;

    }


    private void initView() {
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.inner_fragment_container,checkFragments[0]).commit();
        checkButtons[0].setSelected(true);
    }
    private void initEvent() {
        checkButtons[0].setOnClickListener(this);
        checkButtons[1].setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_team:
                newIndex=0;
                break;
            case R.id.button_person:
                newIndex=1;
                break;
        }
        switchFragment();
    }

    private void switchFragment() {
        FragmentTransaction mFragmentTransaction;
        //如果选择的项不是当前选中项，则替换；否则，不做操作
        if (newIndex != oldIndex) {
            mFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.hide(checkFragments[oldIndex]);    //隐藏当前fragment

            //如何选中项没有添加过,则添加
            if (!checkFragments[newIndex].isAdded()) {
                //添加fragment
                mFragmentTransaction.add(R.id.inner_fragment_container, checkFragments[newIndex]);
            }
            //显示当前项
            mFragmentTransaction.show(checkFragments[newIndex]).commit();
        }
        //之前选中的项被取消
        checkButtons[oldIndex].setSelected(false);

        //当前的选项被选中
        checkButtons[newIndex].setSelected(true);

        //当前选项变为选中项
        oldIndex = newIndex;
    }
}
