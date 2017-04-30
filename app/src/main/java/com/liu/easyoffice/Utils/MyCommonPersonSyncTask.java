package com.liu.easyoffice.Utils;

import android.os.AsyncTask;

import com.liu.easyoffice.MyView.CustomStripProgressbarDataView;
import com.liu.easyoffice.MyView.CustomStripProgressbarView;


/**
 * Created by Administrator on 2016/11/6.
 */

public class MyCommonPersonSyncTask extends AsyncTask {


    private int normalProgress;
    private int earlyProgress;
    private int lateProgress;
    private int askforleaveProgress;
    private int onBusinessProgress;
    private int absentProgress;
    private CustomStripProgressbarView mNormalStrip;
    private CustomStripProgressbarView mEarlyOffStrip;
    private CustomStripProgressbarView mLateStrip;
    private CustomStripProgressbarView mAskforleaveStrip;
    private CustomStripProgressbarView mOnbusinessStrip;
    private CustomStripProgressbarView mAbsentStrip;

    private CustomStripProgressbarDataView mNormalDataStrip;
    private CustomStripProgressbarDataView mEarlyOffDataStrip;
    private CustomStripProgressbarDataView mLateDataStrip;
    private CustomStripProgressbarDataView mAskforleaveDataStrip;
    private CustomStripProgressbarDataView mOnBusinessDataStrip;
    private CustomStripProgressbarDataView mAbsentDataStrip;
    private static final String TAG = "MyCommonPersonSyncTask";


    //doInBackground方法和onPostExecute的参数必须对应，这两个参数在AsyncTask声明的泛型参数列表中指定，
    // 第一个为 doInBackground接受的参数;
    // 第二个为显示进度的参数;
    // 第三个为doInBackground返回和onPostExecute传入的参 数。


    // * AsyncTask是抽象类.AsyncTask定义了三种泛型类型 Params，Progress和Result。
    // * Params 启动任务执行的输入参数，比如HTTP请求的URL。
    // * Progress 后台任务执行的百分比。
    // * Result 后台执行任务最终返回的结果，比如String,Integer等。

    //AsyncTask<>的参数类型由用户设定，这里设为三个String
    //第一个String代表输入到任务的参数类型，也即是doInBackground()的参数类型
    //第二个String代表处理过程中的参数类型，也就是doInBackground()执行过程中的产出参数类型，通过publishProgress()发消息
    //传递给onProgressUpdate()一般用来更新界面
    //第三个String代表任务结束的产出类型，也就是doInBackground()的返回值类型，和onPostExecute()的参数类型

    //AsyncTask的三个泛型参数说明（三个参数可以是任何类型）
    //第一个参数：传入doInBackground()方法的参数类型
    //第二个参数：传入onProgressUpdate()方法的参数类型
    //第三个参数：传入onPostExecute()方法的参数类型，也是doInBackground()方法返回的类型

    public MyCommonPersonSyncTask(
            CustomStripProgressbarView normalStrip,
            CustomStripProgressbarDataView normalDataStrip,
            CustomStripProgressbarView earlyOffStrip,
            CustomStripProgressbarDataView earlyOffDataStrip,
            CustomStripProgressbarView lateStrip,
            CustomStripProgressbarDataView lateDataStrip,
            CustomStripProgressbarView askforleaveStrip,
            CustomStripProgressbarDataView askforleaveDataStrip,
            CustomStripProgressbarView onbusinessStrip,
            CustomStripProgressbarDataView onBusinessDataStrip,
            CustomStripProgressbarView absentStrip,
            CustomStripProgressbarDataView absentDataStrip
    ) {

        mNormalStrip = normalStrip;
        mEarlyOffStrip = earlyOffStrip;
        mLateStrip = lateStrip;
        mNormalDataStrip = normalDataStrip;
        mEarlyOffDataStrip = earlyOffDataStrip;
        mLateDataStrip = lateDataStrip;
        mAskforleaveStrip = askforleaveStrip;
        mAskforleaveDataStrip = askforleaveDataStrip;
        mOnbusinessStrip = onbusinessStrip;
        mOnBusinessDataStrip = onBusinessDataStrip;
        mAbsentStrip = absentStrip;
        mAbsentDataStrip = absentDataStrip;
    }


    /**
     * 这边的参数和前边TeamCheckFragment中execute()方法传过来的按顺序对齐
     *
     * @param params
     * @return
     */
    @Override
    protected Object doInBackground(Object[] params) {
        //注意记住UI线程只有一个
        while (normalProgress < (Integer) params[0] || earlyProgress < (Integer) params[1] || lateProgress < (Integer) params[2]
                || askforleaveProgress < (Integer) params[3] || onBusinessProgress < (Integer) params[4] || absentProgress < (Integer) params[5]) {
            try {
                Thread.sleep(100);

                if (normalProgress < (Integer) params[0]) {
                    normalProgress++;
                }
                if (earlyProgress < (Integer) params[1]) {
                    earlyProgress++;
                }
                if (lateProgress < (Integer) params[2]) {
                    lateProgress++;
                }
                if (askforleaveProgress < (Integer) params[3]) {
                    askforleaveProgress++;
                }
                if (onBusinessProgress < (Integer) params[4]) {
                    onBusinessProgress++;
                }
                if (absentProgress < (Integer) params[5]) {
                    absentProgress++;
                }
                //参数和下边的onProgressUpdate(Object[] values)方法中的值一一对应
                publishProgress( normalProgress, earlyProgress,
                        lateProgress, askforleaveProgress, onBusinessProgress, absentProgress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mNormalStrip.setProgress(0);
        mNormalDataStrip.setProgress(0);
        mEarlyOffStrip.setProgress(0);
        mEarlyOffDataStrip.setProgress(0);
        mLateStrip.setProgress(0);
        mLateDataStrip.setProgress(0);
        mAskforleaveStrip.setProgress(0);
        mAskforleaveDataStrip.setProgress(0);
        mOnbusinessStrip.setProgress(0);
        mOnBusinessDataStrip.setProgress(0);
        mAbsentStrip.setProgress(0);
        mAbsentDataStrip.setProgress(0);
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        mNormalStrip.setNum(31);
        mEarlyOffStrip.setNum(31);
        mLateStrip.setNum(31);
        mAskforleaveStrip.setNum(31);
        mOnbusinessStrip.setNum(31);
        mAbsentStrip.setNum(31);
        mNormalStrip.setProgress((Integer) values[0]);
        mNormalDataStrip.setProgress((Integer) values[0]);
        mEarlyOffStrip.setProgress((Integer) values[1]);
        mEarlyOffDataStrip.setProgress((Integer) values[1]);
        mLateStrip.setProgress((Integer) values[2]);
        mLateDataStrip.setProgress((Integer) values[2]);
        mAskforleaveStrip.setProgress((Integer) values[3]);
        mAskforleaveDataStrip.setProgress((Integer) values[3]);
        mOnbusinessStrip.setProgress((Integer) values[4]);
        mOnBusinessDataStrip.setProgress((Integer) values[4]);
        mAbsentStrip.setProgress((Integer) values[5]);
        mAbsentDataStrip.setProgress((Integer) values[5]);

    }


}
