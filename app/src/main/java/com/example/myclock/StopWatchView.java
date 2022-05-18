package com.example.myclock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Administrator
 * @creat time 2022/5/16 17:45
 * description:
 **/
public class StopWatchView extends LinearLayout {
    public StopWatchView(Context context) {
        super(context);
    }

    public StopWatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StopWatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StopWatchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //TextView
        timeHour = findViewById(R.id.timeHour);
        timeMin = findViewById(R.id.timeMin);
        timeSec = findViewById(R.id.timeSec);
        timeMSec = findViewById(R.id.timeMSec);

        timeHour.setText("00");
        timeMin.setText("00");
        timeSec.setText("00");
        timeMSec.setText("00");


        //ListView
        lvStopWatchList = findViewById(R.id.lvStopWatchList);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        lvStopWatchList.setAdapter(adapter);

        //Button
        btnSWStart = findViewById(R.id.btnSWStart);
        //开始
        btnSWStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
                btnSWStart.setVisibility(View.GONE);
                btnSWPause.setVisibility(View.VISIBLE);
                btnSWStop.setVisibility(View.GONE);
                btnSWFlag.setVisibility(View.VISIBLE);
            }
        });


        btnSWPause = findViewById(R.id.btnSWPause);
        //暂停
        btnSWPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                stopTimer();
                btnSWStart.setVisibility(View.GONE);
                btnSWPause.setVisibility(View.GONE);
                btnSWResume.setVisibility(View.VISIBLE);
                btnSWStop.setVisibility(View.VISIBLE);
                btnSWFlag.setVisibility(View.GONE);
            }
        });


        btnSWResume = findViewById(R.id.btnSWResume);
        //继续
        btnSWResume.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startTimer();
                btnSWStart.setVisibility(View.GONE);
                btnSWPause.setVisibility(View.VISIBLE);
                btnSWResume.setVisibility(View.GONE);
                btnSWStop.setVisibility(View.GONE);
                btnSWFlag.setVisibility(View.VISIBLE);
            }
        });


        btnSWStop = findViewById(R.id.btnSWStop);
        //停止
        btnSWStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                stopTimer();
                adapter.clear();
                timeSec.setText("00");
                timeMSec.setText("00");
                timeHour.setText("00");
                timeMin.setText("00");
                btnSWStart.setVisibility(View.VISIBLE);
                btnSWPause.setVisibility(View.GONE);
                btnSWResume.setVisibility(View.GONE);
                btnSWStop.setVisibility(View.GONE);
                btnSWFlag.setVisibility(View.GONE);
            }
        });


        btnSWFlag = findViewById(R.id.btnSWFlag);
        //计时
        btnSWFlag.setOnClickListener(new OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View view) {
                adapter.insert(String.format("%02d : %02d : %02d : %02d",
                        allMSec / 1000 / 60 / 60,
                        allMSec / 1000 / 60 % 60,
                        allMSec / 1000 % 60,
                        allMSec % 1000 / 10),
                        0);
            }
        });
        //可见
        btnSWStart.setVisibility(View.VISIBLE);
        btnSWPause.setVisibility(View.GONE);
        btnSWResume.setVisibility(View.GONE);
        btnSWStop.setVisibility(View.GONE);
        btnSWFlag.setVisibility(View.GONE);


    }

    private void stopTimer() {
        if (timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
    }

    private void startTimer() {
        if (timerTask == null) {

            allMSec = Integer.parseInt(timeMSec.getText().toString()) +
                    Integer.parseInt(timeSec.getText().toString()) * 1000 +
                    Integer.parseInt(timeMin.getText().toString()) * 60 * 1000 +
                    Integer.parseInt(timeHour.getText().toString()) * 60 * 60 * 1000;

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    allMSec++;

                    handler.sendEmptyMessage(MSG_TIME_IS_TICK);

                }
            };
            timer.schedule(timerTask, 1, 1);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_TIME_IS_TICK:
                    timeMSec.setText(String.format("%02d",allMSec % 1000 / 10));
                    timeSec.setText(String.format("%02d",allMSec / 1000 % 60));
                    timeMin.setText(String.format("%02d",allMSec / 1000 / 60 % 60));
                    timeHour.setText(String.format("%02d",allMSec / 1000 / 60 / 60));
                    break;
            }
        }
    };



    private ListView lvStopWatchList;
    private TextView timeHour, timeMin, timeSec, timeMSec;
    private Button btnSWStart, btnSWPause, btnSWResume, btnSWStop, btnSWFlag;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private static final int MSG_TIME_IS_TICK = 0;
    private ArrayAdapter<String> adapter;
    private int allMSec;


}
