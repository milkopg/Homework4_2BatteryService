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

import static android.content.Context.BATTERY_SERVICE;

/**
 * Created by Milko on 21.9.2016 г..
 */

public class BatteryBroadcastReceiver extends BroadcastReceiver{
  private static final long REPEAT_TIME =  1000 * 60/* * 60*/;
  @Override
  public void onReceive(Context context, Intent intent) {
//    if (intent.getIntArrayExtra(MainActivity.BATTERY_LEVEL) != null) {
      //int level = intent.getIntExtra(MainActivity.BATTERY_LEVEL, 1);
      //int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//
//      //float batteryPct = level / (float)scale;
//      Toast.makeText(context, "Battery percentage: " + level, Toast.LENGTH_SHORT).show();
//    } else {
//      Toast.makeText(context, "Battery percentage: 0" , Toast.LENGTH_SHORT).show();
//    }

    String action = intent.getAction();

    AlarmManager service = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    Intent i = new Intent(context, HourlyBroadcastReceiver.class);
    int level = intent.getIntExtra("level", -1);
    i.putExtra(MainActivity.BATTERY_LEVEL, level);
    Toast.makeText(context, "Battery percentage from Intent: " + level, Toast.LENGTH_SHORT).show();
//    if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
//      int level = intent.getIntExtra("level", -1);
//      i.putExtra(MainActivity.BATTERY_LEVEL, level);
//      Toast.makeText(context, "Battery percentage from Intent: " + level, Toast.LENGTH_SHORT).show();
//    } else {
//      Toast.makeText(context, "Battery percentage: None" , Toast.LENGTH_SHORT).show();
//    }
    PendingIntent pending =  PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);


    Calendar cal = Calendar.getInstance();
    // start 30 seconds after boot completed
    cal.add(Calendar.SECOND, 30);
    // fetch every 30 seconds
    // InexactRepeating allows Android to optimize the energy consumption
    service.setInexactRepeating(AlarmManager.RTC_WAKEUP,  cal.getTimeInMillis(), REPEAT_TIME, pending);
  //  service.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), REPEAT_TIME, pending);


//    if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
//
//      int level = intent.getIntExtra("level", -1);
//      Toast.makeText(context, "Battery percentage: " + level, Toast.LENGTH_SHORT).show();
//    } else {
//      Toast.makeText(context, "Battery percentage: None" , Toast.LENGTH_SHORT).show();
//    }

//    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
//    Toast.makeText(context, "Battery percentage: " + level, Toast.LENGTH_SHORT).show();
  }
}
