package com.liu.easyoffice.MyView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liu.easyoffice.R;

/**
 * Created by hui on 2016/10/17.
 */

public class MyTitleBar extends RelativeLayout {

    private View titleBar;
    private ImageView leftImg;
    private ImageView rightImg;
    private TextView title;
    private Drawable leftRes;
    private Drawable rightRes;
    private int bg_color;
    private boolean isShowLeftRes;
    private boolean isShowRightRes;
    private CharSequence titleText;

    public MyTitleBar(Context context) {
        super(context);
    }

    public MyTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public MyTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void init(AttributeSet attrs) {
        titleBar = LayoutInflater.from(getContext()).inflate(R.layout.work_title, this, true);
        leftImg = ((ImageView) titleBar.findViewById(R.id.my_title_img_left));
        rightImg = ((ImageView) titleBar.findViewById(R.id.my_title_img_right));
        title = ((TextView) titleBar.findViewById(R.id.my_title_tv));

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.page_titleBar);
        try {
            leftRes = ta.getDrawable(R.styleable.page_titleBar_leftImagResource);
            rightRes = ta.getDrawable(R.styleable.page_titleBar_rightImagResource);
            isShowLeftRes = ta.getBoolean(R.styleable.page_titleBar_showLeftImag, true);
            isShowRightRes = ta.getBoolean(R.styleable.page_titleBar_showRightImag, true);
            bg_color = ta.getColor(R.styleable.page_titleBar_bg_color, getResources().getColor(R.color.Indigo_nav_color));
            titleText = ta.getText(R.styleable.page_titleBar_text);
            if (isShowLeftRes && leftRes != null) {
                leftImg.setImageDrawable(leftRes);
            }
            if (isShowRightRes && rightRes != null) {
                rightImg.setImageDrawable(rightRes);
            }
            if (titleText != null) {
                title.setText(titleText);
            }

            //setbgcolor(bg_color);

        } finally {
            ta.recycle();
        }
    }




    public void setLeftImg(@Nullable Drawable drawable) {
        if (isShowLeftRes && leftRes != null) {
            leftImg.setImageDrawable(leftRes);
        }
    }

    public void setLeftImgClick(@Nullable OnClickListener l) {
        leftImg.setOnClickListener(l);
    }

    public void setRightImg(@Nullable Drawable drawable) {
        if (isShowRightRes && rightRes != null) {
            rightImg.setImageDrawable(rightRes);
        }
    }

    public void setRightImgClick(@Nullable OnClickListener l) {
        rightImg.setOnClickListener(l);
    }

    public void setText(String text) {
        if (titleText != null) {
            title.setText(titleText);
            invalidate();
            requestLayout();
        }
    }
}
