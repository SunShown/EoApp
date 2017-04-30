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

public class CustomStripProgressbarView extends View {
    private int backColor;  //图中粉色
    private int progressColor;  //图中绿色
    private int textColor;  //图中红色

    private Paint paintBack;  //粉色对应的画笔
    private Paint paintProgress;  //绿色对应的画笔
    private Paint paintText;  //红色对应的画笔

    private int progress;
    private int num;
    private float averageWidth;  //平均宽度
    private float strokeWidth;  //红色的宽度
    private float textSizeProgress;  //中间进度百分比的字符串的字体


    public CustomStripProgressbarView(Context context) {
        this(context, null);
    }

    public CustomStripProgressbarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomStripProgressbarView(Context context, AttributeSet attrs, int defStyleAttr) {  //构造方法不用说了吧
        super(context, attrs, defStyleAttr);
        TypedArray arr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomprogressBarView, defStyleAttr, 0);  //获取自定义的属性值组
        backColor = arr.getColor(R.styleable.CustomprogressBarView_backColor, Color.WHITE);  //获取自定义的属性backColor,默认为白色
        progressColor = arr.getColor(R.styleable.CustomprogressBarView_progressColor, Color.RED);  //获取自定义的属性progressColor,默认为红色
        textSizeProgress = arr.getDimension(R.styleable.CustomprogressBarView_textSizeProgress, 14);
        textColor = arr.getColor(R.styleable.CustomprogressBarView_strokeColor, getResources().getColor(R.color.grey_text_address));  //获取自定义的属性,外框的颜色即图中的红色框
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

        //设置灰色框的画笔属性
        paintBack.setColor(backColor);
        paintBack.setDither(true);  //防抖动
        paintBack.setStyle(Paint.Style.FILL);
        canvas.drawRect(strokeWidth, strokeWidth, width - strokeWidth, height - strokeWidth, paintBack);//画粉色矩形框，参数同上

        //计算绿色自定义进度条的宽度以及显示宽度
        float contentWidth = width - strokeWidth * 2;  //绿色进度框的纵向宽度
        averageWidth = contentWidth / num;  //绿色进度框的横向宽度
        //绿色进度框的画笔属性
        paintProgress.setColor(progressColor);
        paintProgress.setDither(true);
        canvas.drawRect(strokeWidth, strokeWidth, (float) progress*averageWidth+strokeWidth, height - strokeWidth, paintProgress);
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
