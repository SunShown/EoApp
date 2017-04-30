package com.liu.easyoffice.MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class NoScrollListview extends ListView {
  
        public NoScrollListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}

	public NoScrollListview(Context context) {
		super(context);
		
	}

		public NoScrollListview(Context context, AttributeSet attrs) {
                super(context, attrs);  
        }  
          
        /** 
         * 设置不滚动 
         */  
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  
        {  
                int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                                MeasureSpec.AT_MOST);
                super.onMeasure(widthMeasureSpec, expandSpec);  
  
        }  
  
} 