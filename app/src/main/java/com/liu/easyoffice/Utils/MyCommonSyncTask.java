package com.liu.easyoffice.Utils;

import android.os.AsyncTask;

import com.liu.easyoffice.MyView.CustomStripProgressbarDataView;
import com.liu.easyoffice.MyView.CustomStripProgressbarView;
import com.liu.easyoffice.MyView.RoundProgressBar;


/**
 * Created by Administrator on 2016/11/6.
 */

public class MyCommonSyncTask extends AsyncTask {


    private int mRoundProgress;
    private int mTotalProgress;
    private int normalProgress;
    private int earlyProgress;
    private int lateProgress;
    private int askforleaveProgress;
    private int onBusinessProgress;
    private int absentProgress;
    private RoundProgressBar mRoundProgressBar;
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
    private static final String TAG = "MyCommonSyncTask";


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

    public MyCommonSyncTask(RoundProgressBar roundProgressBar,
                            CustomStripProgressbarView normalStrip,
                            CustomStripProgressbarView earlyOffStrip,
                            CustomStripProgressbarView lateStrip,
                            CustomStripProgressbarDataView normalDataStrip,
                            CustomStripProgressbarDataView earlyOffDataStrip,
                            CustomStripProgressbarDataView lateDataStrip,
                            CustomStripProgressbarView askforleaveStrip,
                            CustomStripProgressbarDataView askforleaveDataStrip,
                            CustomStripProgressbarView onbusinessStrip,
                            CustomStripProgressbarDataView onBusinessDataStrip,
                            CustomStripProgressbarView absentStrip,
                            CustomStripProgressbarDataView absentDataStrip
    ) {

        mRoundProgressBar = roundProgressBar;
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
        while (mRoundProgress < (Integer) params[0] || normalProgress < (Integer) params[2] || earlyProgress < (Integer) params[3] || lateProgress < (Integer) params[4]
                || askforleaveProgress < (Integer) params[5] || onBusinessProgress < (Integer) params[6] || absentProgress < (Integer) params[7]) {
            try {
                Thread.sleep(100);
                if (mRoundProgress < (Integer) params[0]) {
                    mRoundProgress++;
                }
                if (normalProgress < (Integer) params[2]) {
                    normalProgress++;
                }
                if (earlyProgress < (Integer) params[3]) {
                    earlyProgress++;
                }
                if (lateProgress < (Integer) params[4]) {
                    lateProgress++;
                }
                if (askforleaveProgress < (Integer) params[5]) {
                    askforleaveProgress++;
                }
                if (onBusinessProgress < (Integer) params[6]) {
                    onBusinessProgress++;
                }
                if (absentProgress < (Integer) params[7]) {
                    absentProgress++;
                }
                //参数和下边的onProgressUpdate(Object[] values)方法中的值一一对应
                publishProgress(mRoundProgress, params[1], normalProgress, earlyProgress,
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
        mRoundProgressBar.setProgress(0);
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
        mRoundProgressBar.setMax((Integer) values[1]);
        mNormalStrip.setNum((Integer) values[1]);
        mEarlyOffStrip.setNum((Integer) values[1]);
        mLateStrip.setNum((Integer) values[1]);
        mAskforleaveStrip.setNum((Integer) values[1]);
        mOnbusinessStrip.setNum((Integer) values[1]);
        mAbsentStrip.setNum((Integer) values[1]);
        mRoundProgressBar.setProgress((Integer) values[0]);
        mRoundProgressBar.setPeopleCount((Integer) values[1]);  //分母
        mNormalStrip.setProgress((Integer) values[2]);
        mNormalDataStrip.setProgress((Integer) values[2]);
        mEarlyOffStrip.setProgress((Integer) values[3]);
        mEarlyOffDataStrip.setProgress((Integer) values[3]);
        mLateStrip.setProgress((Integer) values[4]);
        mLateDataStrip.setProgress((Integer) values[4]);
        mAskforleaveStrip.setProgress((Integer) values[5]);
        mAskforleaveDataStrip.setProgress((Integer) values[5]);
        mOnbusinessStrip.setProgress((Integer) values[6]);
        mOnBusinessDataStrip.setProgress((Integer) values[6]);
        mAbsentStrip.setProgress((Integer) values[7]);
        mAbsentDataStrip.setProgress((Integer) values[7]);

    }


}
