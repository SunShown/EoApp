package com.liu.easyoffice.MyView;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liu.easyoffice.R;


/**
 * Created by zyc on 2016/10/17.
 */

public class MySearchBar extends LinearLayout {


    private EditText query;
    private ImageButton search_clear;
    private TextView tv_search;
    private TextView tv_cancel;
    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public MySearchBar(Context context) {
        super(context);
    }
    public void setQuerytext(String text){
        query.setHint(text);



    }

    public MySearchBar(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_searchbar,this,true);
        query = ((EditText) view.findViewById(R.id.query));
        search_clear = ((ImageButton) view.findViewById(R.id.search_clear));
        tv_search = ((TextView) view.findViewById(R.id.tv_search));
        tv_cancel = ((TextView) view.findViewById(R.id.tv_cancel));
        final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        search_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
            }
        });
        tv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.clearFocus();
                tv_cancel.setVisibility(GONE);
                tv_search.setVisibility(VISIBLE);

                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
////接受软键盘输入的编辑文本或其它视图
//                inputMethodManager
//                        .showSoftInput(submitBt,InputMethodManager.SHOW_FORCED);
            }
        });
        query.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               if(s.length()==0) {
                    //没有输入内容，不显示按钮
                    search_clear.setVisibility(View.INVISIBLE);//设置不显示
                   tv_search.setVisibility(GONE);
                   tv_cancel.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    //输入框内容：
                    tv_cancel.setVisibility(GONE);
                    tv_search.setVisibility(VISIBLE);
                    search_clear.setVisibility(View.VISIBLE);//设置显示
                } else {
                    //没有输入内容，不显示按钮
                    search_clear.setVisibility(View.INVISIBLE);//设置不显示
                    tv_search.setVisibility(GONE);
                    tv_cancel.setVisibility(VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public MySearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public  void settv_searchOnClickListener(@Nullable OnClickListener l){
        tv_search.setOnClickListener(l);


    }
    public String getqueryText(){


        return query.getText().toString().trim();
    }
}
