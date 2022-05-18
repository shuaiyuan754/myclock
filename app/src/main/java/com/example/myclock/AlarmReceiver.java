package com.example.myclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author Administrator
 * @creat time 2022/5/12 14:32
 * description:
 **/
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>闹钟执行>>>>>>>>>>>>>>>>>>>>>>>");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getBroadcast(context,getResultCode(),new Intent(context,AlarmReceiver.class),0));

        Intent i = new Intent(context,PlayAlarmActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
