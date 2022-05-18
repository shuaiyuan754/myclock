package com.example.myclock;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Administrator
 * @creat time 2022/5/11 15:27
 * description:
 **/
public class AlarmView extends LinearLayout {


    public AlarmView(Context context) {
        super(context);
        init();
    }

    public AlarmView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlarmView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }


    private void init(){
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        lvAlarmList = findViewById(R.id.lvAlarmList);
        btnAddAlarm = findViewById(R.id.btnAddAlarm);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        lvAlarmList.setAdapter(adapter);
//        adapter.add(new AlarmData(System.currentTimeMillis()));
        readAlarmList();

        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAlarm();
            }
        });

        lvAlarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                new AlertDialog.Builder(getContext()).setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeAlarm(position);
                    }
                }).setNegativeButton("取消",null).setTitle("操作选项").show();
                return true;
            }
        });
    }

    public void addAlarm() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, i1);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                Calendar currentCalendar = Calendar.getInstance();

                if (calendar.getTimeInMillis() <= currentCalendar.getTimeInMillis()) {
                    calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
                }
                AlarmData alarmData = new AlarmData(calendar.getTimeInMillis());
                adapter.add(alarmData);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alarmData.getTime(),5*60*1000,
                        PendingIntent.getBroadcast(getContext(),alarmData.getId(),new Intent(getContext(),AlarmReceiver.class),0));
                saveAlarmList();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    public void saveAlarmList() {
        StringBuffer sb = new StringBuffer();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < adapter.getCount(); i++) {
            sb.append(adapter.getItem(i).getTime()).append(",");
        }
        if (sb.length() > 1){
            editor.putString(KET_ALARM_LIST, sb.toString().substring(0, sb.length() - 1));
            System.out.println(sb.toString().substring(0, sb.length() - 1));
        }else{
            editor.putString(KET_ALARM_LIST,null);
        }
        editor.commit();
    }

    public void readAlarmList(){
        String content = sharedPreferences.getString(KET_ALARM_LIST, null);
        if (content != null){
            String[] timeString = content.split(",");
            for (String string : timeString){
                adapter.add(new AlarmData(Long.parseLong(string)));
            }
        }
    }

    public void removeAlarm(int position){
        AlarmData alarmData = adapter.getItem(position);
        adapter.remove(alarmData);
        saveAlarmList();
        alarmManager.cancel(PendingIntent.getBroadcast(getContext(),alarmData.getId(),new Intent(getContext(),AlarmReceiver.class),0));
    }

    private ListView lvAlarmList;
    private Button btnAddAlarm;
    private ArrayAdapter<AlarmData> adapter;
    private final String KET_ALARM_LIST = "alarmList";
    private SharedPreferences sharedPreferences = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE);
    private AlarmManager alarmManager;


    private static class AlarmData {
        private long time = 0;
        private String timeLable = "";
        private Calendar date;

        @SuppressLint("DefaultLocale")
        public AlarmData(long time) {
            this.time = time;
            date = Calendar.getInstance();
            date.setTimeInMillis(time);
            timeLable = String.format("%02d月%02d日 %02d:%02d"
                    , date.get(Calendar.MONTH) + 1
                    , date.get(Calendar.DAY_OF_MONTH)
                    , date.get(Calendar.HOUR_OF_DAY)
                    , date.get(Calendar.MINUTE));
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getTimeLable() {
            return timeLable;
        }

        public void setTimeLable(String timeLable) {
            this.timeLable = timeLable;
        }

        @Override
        public String toString() {
            return getTimeLable();
        }

        public int getId(){
            return (int) getTime()/1000/60;
        }
    }

}











