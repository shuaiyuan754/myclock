package com.example.myclock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;

/**
 * @author Administrator
 * @creat time 2022/5/11 11:05
 * description:
 **/
@SuppressLint("HandlerLeak")
public class TimeView extends LinearLayout {

    private TextView tvTime;

    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //初始化完成之后
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvTime = findViewById(R.id.tvTime);
        tvTime.setText("hello");

        timeHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        if (visibility == View.VISIBLE){
            timeHandler.sendEmptyMessage(0);
        }else{
            timeHandler.removeMessages(0);
        }
    }

    @SuppressLint("DefaultLocale")
    private void refrushTime(){
        Calendar calendar = Calendar.getInstance();
        tvTime.setText(String.format("%02d:%02d:%02d",calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND)));

    }

    private Handler timeHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            refrushTime();

            if (getVisibility() == View.VISIBLE){
                timeHandler.sendEmptyMessageDelayed(0,1000);
            }
        }
    };


}
