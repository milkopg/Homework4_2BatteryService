package homework.softuni.bg.homework4_2batteryservice;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import homework.softuni.bg.homework4_2batteryservice.listener.IOnBatteryChangedListener;
import homework.softuni.bg.homework4_2batteryservice.receiver.BatteryBroadcastReceiver;
import homework.softuni.bg.homework4_2batteryservice.service.BatteryService;

public class MainActivity extends AppCompatActivity implements IOnBatteryChangedListener, View.OnClickListener {
  public final  String TAG = getClass().getSimpleName();
  public static final String BATTERY_LEVEL = "battery_level";

  private int percentLastCheck = 0;
  private BatteryBroadcastReceiver receiver;

  private Button mButtonStartService;
  private TextView mTextViewShowPercentage;


  @Override
  public void updateBatteryPercentage(int percent) {
      synchronized (this) {
        if (percent != -1) {
          if (percentLastCheck == 0) {
            percentLastCheck = percent;
            mTextViewShowPercentage.setText("No data , service just started");
          } else {
            mTextViewShowPercentage.setText("Battery level " + percent + " , baterry went down in the last hour: " + (percentLastCheck - percent) + "%");
            percentLastCheck = percent;
          }

        }
      }
  }

  @Override
  public void onClick(View view) {
    if (!isMyServiceRunning(BatteryService.class)) {
      Intent intent = new Intent(this, BatteryService.class);
      bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
      startService(intent);
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mButtonStartService = (Button) findViewById(R.id.buttonStartService);
    mButtonStartService.setOnClickListener(this);
    mTextViewShowPercentage = (TextView) findViewById(R.id.textViewBatteryPercentage);
    enableOrDisableButton();

    IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    Intent batteryStatus = registerReceiver(null, ifilter);
    receiver = new BatteryBroadcastReceiver();
    this.registerReceiver(receiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
  }


  private boolean isMyServiceRunning(Class<? extends Service> serviceClass) {
    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected void onStop() {
    if (receiver != null)
      try {
        unregisterReceiver(receiver);
      } catch (IllegalArgumentException e) {
        Log.e(TAG, "Reveiver is not registered "  + e);
      }
    super.onStop();
  }

  @Override
  protected void onPause() {
    super.onPause();
    unbindService(mConnection);
  }

  @Override
  protected void onResume() {
    super.onResume();
    enableOrDisableButton();
    Intent intent= new Intent(this, BatteryService.class);
    bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    startService(intent);
  }

  ServiceConnection mConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
      BatteryService.BatteryServiceBinder serviceToOperate = (BatteryService.BatteryServiceBinder) service;
      serviceToOperate.getService().setServiceCallback(MainActivity.this);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
      Log.d(TAG, "OnServiceDisconnected: " + componentName);
    }
  };

  public void enableOrDisableButton() {
    if (isMyServiceRunning(BatteryService.class)) {
      mButtonStartService.setOnClickListener(this);
      mButtonStartService.setEnabled(true);
      mButtonStartService.callOnClick();
    } else {
      mButtonStartService.setOnClickListener(null);
      mButtonStartService.setEnabled(false);
    }
  }
}
