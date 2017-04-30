package com.liu.easyoffice.MyView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.TextView;
import android.widget.TextView;

import com.liu.easyoffice.R;

/**
 * Created by zyc on 2016/10/27.
 */

public class QuedingDialog  extends Dialog {

    public QuedingDialog(Context context) {
        super(context);


    }

    public QuedingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected QuedingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public static class Builder {
        private Context context;
    
        private String message;
        private String positiveTextViewText;
        private String negativeTextViewText;
        private View contentView;
        private DialogInterface.OnClickListener positiveTextViewClickListener;
        private DialogInterface.OnClickListener negativeTextViewClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

    
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
     

        /**
         * Set the Dialog title from String
         *
         * @param
         * @return
         */

    

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive TextView resource and it's listener
         *
         * @param positiveTextViewText
         * @return
         */
        public Builder setPositiveTextView(int positiveTextViewText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveTextViewText = (String) context
                    .getText(positiveTextViewText);
            this.positiveTextViewClickListener = listener;
            return this;
        }

        public Builder setPositiveTextView(String positiveTextViewText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveTextViewText = positiveTextViewText;
            this.positiveTextViewClickListener = listener;
            return this;
        }

        public Builder setNegativeTextView(int negativeTextViewText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeTextViewText = (String) context
                    .getText(negativeTextViewText);
            this.negativeTextViewClickListener = listener;
            return this;
        }

        public Builder setNegativeTextView(String negativeTextViewText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeTextViewText = negativeTextViewText;
            this.negativeTextViewClickListener = listener;
            return this;
        }

        public QuedingDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final QuedingDialog dialog = new QuedingDialog(context);
            View layout = inflater.inflate(R.layout.queding_dialog_lay, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
          
            // set the confirm TextView
            if (positiveTextViewText != null) {
                ((TextView) layout.findViewById(R.id.tv_chexiao_agree))
                        .setText(positiveTextViewText);
                if (positiveTextViewClickListener != null) {
                    ((TextView) layout.findViewById(R.id.tv_chexiao_agree))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveTextViewClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm TextView just set the visibility to GONE
                layout.findViewById(R.id.tv_chexiao_agree).setVisibility(
                        View.GONE);
            }
            // set the cancel TextView
            if (negativeTextViewText != null) {
                ((TextView) layout.findViewById(R.id.tv_chexiao_cancel))
                        .setText(negativeTextViewText);
                if (negativeTextViewClickListener != null) {
                    ((TextView) layout.findViewById(R.id.tv_chexiao_cancel))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeTextViewClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm TextView just set the visibility to GONE
                layout.findViewById(R.id.tv_chexiao_cancel).setVisibility(
                        View.GONE);
            }
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(R.id.tv_dia_che_title)).setText(message);
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}



