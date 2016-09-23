package homework.softuni.bg.homework4_2batteryservice.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

import java.util.Calendar;

import homework.softuni.bg.homework4_2batteryservice.MainActivity;
import homework.softuni.bg.homework4_2batteryservice.R;

import static android.content.Context.BATTERY_SERVICE;

/**
 * Created by Milko on 21.9.2016 г..
 */

public class BatteryBroadcastReceiver extends BroadcastReceiver{
  private static final long REPEAT_TIME =  1000 * 30 * 60; // 1hour
  @Override
  public void onReceive(Context context, Intent intent) {
    AlarmManager service = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    Intent i = new Intent(context, HourlyBroadcastReceiver.class);
    int level = intent.getIntExtra("level", -1);
    i.putExtra(MainActivity.BATTERY_LEVEL, level);
    Toast.makeText(context, "Battery percentage from Intent: " + level, Toast.LENGTH_SHORT).show();
    PendingIntent pending =  PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);


    Calendar cal = Calendar.getInstance();
    // start 5 seconds after boot completed
    cal.add(Calendar.SECOND, 5);
    // fetch every 1 hour
    // InexactRepeating allows Android to optimize the energy consumption
    service.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), REPEAT_TIME, pending);
  }
}
