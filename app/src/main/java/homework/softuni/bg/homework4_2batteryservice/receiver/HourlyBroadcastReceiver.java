package homework.softuni.bg.homework4_2batteryservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import homework.softuni.bg.homework4_2batteryservice.MainActivity;
import homework.softuni.bg.homework4_2batteryservice.service.BatteryService;

/**
 * Created by Milko on 21.9.2016 г..
 */

public class HourlyBroadcastReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    Intent service = new Intent(context, BatteryService.class);
    if (intent.hasExtra(MainActivity.BATTERY_LEVEL)) {
      service.putExtra(MainActivity.BATTERY_LEVEL, intent.getIntExtra(MainActivity.BATTERY_LEVEL, -1));
    }
    context.startService(service);
  }
}
