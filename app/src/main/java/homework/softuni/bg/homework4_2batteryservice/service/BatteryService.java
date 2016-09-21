package homework.softuni.bg.homework4_2batteryservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import homework.softuni.bg.homework4_2batteryservice.MainActivity;
import homework.softuni.bg.homework4_2batteryservice.listener.IOnBatteryChangedListener;

public class BatteryService extends Service {
  private IBinder binder = new BatteryServiceBinder();
  private IOnBatteryChangedListener callback;
  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  public void setServiceCallback(IOnBatteryChangedListener listener) {
        callback = listener;
  }

  public class BatteryServiceBinder extends Binder {
    public BatteryService getService() {
      return BatteryService.this;
    }
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    int level = intent.hasExtra(MainActivity.BATTERY_LEVEL) ? intent.getIntExtra(MainActivity.BATTERY_LEVEL, -1) :-1 ;
    if ((callback != null) && (level != -1)) {
      callback.updateBatteryPercentage(level);
    }
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }
}
