package com.example.myclock;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Administrator
 * @creat time 2022/5/13 10:47
 * description:
 **/
public class TimerView extends LinearLayout {
    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                stopTimer();
                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.VISIBLE);
            }
        });
        btnResume = findViewById(R.id.btnResume);
        btnResume.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startTimer();
                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
            }
        });
        btnStop = findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                stopTimer();
                etHour.setText("");
                etMin.setText("");
                etSec.setText("");

                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.GONE);
                btnStop.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
            }
        });

        etHour = findViewById(R.id.etHour);
        etMin = findViewById(R.id.etMin);
        etSec = findViewById(R.id.etSec);

        //按键可视化
        btnStart.setVisibility(View.VISIBLE);
        btnStart.setEnabled(false);
        btnPause.setVisibility(View.GONE);
        btnResume.setVisibility(View.GONE);
        btnStop.setVisibility(View.GONE);

        //点击事件
        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startTimer();

                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.VISIBLE);
            }
        });

        //Hour格式设置
//        etHour.setText("00");
//        etHour.setHint("00");
        etHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    int c = Integer.parseInt(charSequence.toString());
                    if (c > 23) {
                        etHour.setText("23");
                    } else if (c < 0) {
                        etHour.setText("0");
                    }
                    checkToEnableStart();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //Min格式设置
//        etMin.setText("00");
        etMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    int c = Integer.parseInt(charSequence.toString());
                    if (c > 59) {
                        etMin.setText("59");
                    } else if (c < 0) {
                        etMin.setText("0");
                    }
                    checkToEnableStart();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //Sec格式设置
//        etSec.setText("00");
        etSec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    int c = Integer.parseInt(charSequence.toString());
                    if (c > 59) {
                        etSec.setText("59");
                    } else if (c < 0) {
                        etSec.setText("0");
                    }
                    checkToEnableStart();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void checkToEnableStart() {

        btnStart.setEnabled((!TextUtils.isEmpty(etHour.getText()) && Integer.parseInt(etHour.getText().toString()) > 0)
                || (!TextUtils.isEmpty(etMin.getText()) && Integer.parseInt(etMin.getText().toString()) > 0)
                || (!TextUtils.isEmpty(etSec.getText()) && Integer.parseInt(etSec.getText().toString()) > 0));
    }


    @SuppressLint("SetTextI18n")
    private void startTimer() {


        if (timerTask == null) {
            if (etHour.getText().length() == 0){
                etHour.setText("00");
            }
            if (etMin.getText().length() == 0){
                etMin.setText("00");
            }
            if (etSec.getText().length() == 0){
                etSec.setText("00");
            }

            timeCount = Integer.parseInt(etHour.getText().toString()) * 60 * 60
                    + Integer.parseInt(etMin.getText().toString()) * 60
                    + Integer.parseInt(etSec.getText().toString());
            timerTask = new TimerTask() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    timeCount--;
                    handler.sendEmptyMessage(MSG_TIME_IS_TICK);
                    if (timeCount <= 0) {
                        stopTimer();
                        handler.sendEmptyMessage(MSG_TIME_IS_UP);
                    }

                }
            };
            timer.schedule(timerTask, 1000, 1000);


        }
    }

    private void stopTimer() {

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_TIME_IS_TICK:
                    int hour = timeCount / 60 / 60;
                    int min = (timeCount / 60) % 60;
                    int sec = timeCount % 60;
                    Log.d(TAG, "handleMessage: " + hour + "|" + min + "|" + sec);
                    etHour.setText(String.format("%02d",hour));
                    etMin.setText(String.format("%02d",min));
                    etSec.setText(String.format("%02d",sec));
                    break;
                case MSG_TIME_IS_UP:
                    new AlertDialog.Builder(getContext()).setTitle("时间到").setNegativeButton("okk", null).show();
                    btnPause.setVisibility(View.GONE);
                    btnResume.setVisibility(View.GONE);
                    btnStop.setVisibility(View.GONE);
                    btnStart.setVisibility(View.VISIBLE);
                    etHour.setText("");
                    etMin.setText("");
                    etSec.setText("");
                    break;
                default:
                    break;
            }

        }
    };


    private static final String TAG = "TimerView";
    private Button btnStart, btnPause, btnResume, btnStop;
    private EditText etHour, etMin, etSec;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private int timeCount;
    private static final int MSG_TIME_IS_UP = 1;
    private static final int MSG_TIME_IS_TICK = 2;


}
