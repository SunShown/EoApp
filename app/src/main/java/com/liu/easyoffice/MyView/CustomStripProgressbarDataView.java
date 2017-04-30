package com.liu.easyoffice.MyView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.liu.easyoffice.R;


/**
 * Created by Administrator on 2016/10/25.
 */

public class CustomStripProgressbarDataView extends View {
    //private int backColor;  //图中粉色
    private int progressColor;  //图中绿色
    private int textColor;  //图中红色

    private Paint paintBack;  //粉色对应的画笔
    private Paint paintProgress;  //绿色对应的画笔
    private Paint paintText;  //红色对应的画笔

    private int progress = 0;
    private int num = 100;
    private float averageWidth;  //平均宽度
    private float strokeWidth;  //红色的宽度
    private float textSizeProgress;  //中间进度百分比的字符串的字体




    public CustomStripProgressbarDataView(Context context) {
        this(context, null);
    }

    public CustomStripProgressbarDataView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomStripProgressbarDataView(Context context, AttributeSet attrs, int defStyleAttr) {  //构造方法不用说了吧
        super(context, attrs, defStyleAttr);
        TypedArray arr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomprogressBarView, defStyleAttr, 0);  //获取自定义的属性值组
       // backColor = arr.getColor(R.styleable.CustomprogressBarView_backColor, Color.WHITE);  //获取自定义的属性backColor,默认为白色
        progressColor = arr.getColor(R.styleable.CustomprogressBarView_progressColor, Color.RED);  //获取自定义的属性progressColor,默认为红色
        textSizeProgress = arr.getDimension(R.styleable.CustomprogressBarView_textSizeProgress, 14);
        textColor = arr.getColor(R.styleable.CustomprogressBarView_strokeColor, getResources().getColor(R.color.green));
        strokeWidth = arr.getDimensionPixelSize(R.styleable.CustomprogressBarView_strokeWidth, 1);  //获取自定义的设置线条框宽度的属性，默认为1dp
        arr.recycle();  //用完后记得recycle();
        paintBack = new Paint();
        paintProgress = new Paint();
        paintText = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {  //重写onDraw来画进度条
        int width = getWidth();  //获取自定义的View的宽度
        int height = getHeight();  //获取自定义的View的高度


        //画进度TextView
        paintText.setColor(textColor);
        paintText.setTextSize(textSizeProgress);
        paintText.setDither(true);
        paintText.setAntiAlias(true);  //消除锯齿
        paintText.setStyle(Paint.Style.FILL);
        canvas.drawText(progress+"",width-25*strokeWidth,height-strokeWidth,paintText);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();  //刷新界面
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
