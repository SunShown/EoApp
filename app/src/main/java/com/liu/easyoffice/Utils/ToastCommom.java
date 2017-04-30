package com.liu.easyoffice.Utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.easyoffice.Application.MyApplication;
import com.liu.easyoffice.R;


/**
 * Created by candy on 2016/9/27.
 */

public class ToastCommom {
    private static ToastCommom toastCommom;

    private static  Toast toast;

    private ToastCommom(){
    }

    public static ToastCommom createToastConfig(){
        if (toastCommom==null) {
            toastCommom = new ToastCommom();
        }
        return toastCommom;
    }

    /**
     * 显示Toast
     * @param context
     * @param root
     * @param tvString
     */

    public static void ToastShow(Context context, ViewGroup root, String tvString){
        MyApplication application=MyApplication.getInstance();

        View layout = LayoutInflater.from(context).inflate(R.layout.toast,root);
        TextView text = (TextView) layout.findViewById(R.id.text);
//        ImageView mImageView = (ImageView) appconfig_father_item.findViewById(R.id.iv);
//        mImageView.setBackgroundResource(R.drawable.dz_after);
        text.setText(tvString);
        toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM,0,application.getPhoneHeight()/5);
//        toast.setMargin(application.getPhoneWidth()/2,application.getPhoneHeight()/4*3);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
