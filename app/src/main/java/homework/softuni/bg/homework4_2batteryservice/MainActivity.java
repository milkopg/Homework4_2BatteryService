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

import org.w3c.dom.Text;

import homework.softuni.bg.homework4_2batteryservice.listener.IOnBatteryChangedListener;
import homework.softuni.bg.homework4_2batteryservice.receiver.BatteryBroadcastReceiver;
import homework.softuni.bg.homework4_2batteryservice.service.BatteryService;

public class MainActivity extends AppCompatActivity implements IOnBatteryChangedListener, View.OnClickListener {
  public final  String TAG = getClass().getSimpleName();
  public static final String BATTERY_LEVEL = "battery_level";

  private int percentLastCheck = 0;
  private ServiceConnection connection;
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
            percentLastCheck -= percent;
            mTextViewShowPercentage.setText("Battery level " + percent + " , baterry went down in the last hour: " + Math.abs(percentLastCheck - percent));
          }

        }
      }
  }

  @Override
  public void onClick(View view) {
    if (!isMyServiceRunning(BatteryService.class)) {
      Intent intent = new Intent(this, BatteryService.class);
      bindService(intent, connection, Context.BIND_AUTO_CREATE);
      startService(intent);
    }

//    if (!isMyServiceRunning(BatteryService.class)) {
//      mButtonStartService.setEnabled(true);
//      mButtonStartService.setOnClickListener(this);
//    } else {
//      mButtonStartService.setEnabled(false);
//      mButtonStartService.setOnClickListener(null);
//    }
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mButtonStartService = (Button) findViewById(R.id.buttonStartService);
    mButtonStartService.setOnClickListener(this);
    mTextViewShowPercentage = (TextView) findViewById(R.id.textViewBatteryPercentage);


    connection = new ServiceConnection() {
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

//    BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
//    int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

//    Intent intentForBroadcast = new Intent();
//    intentForBroadcast.setAction("android.intent.action.BATTERY_CHANGED");
//    intentForBroadcast.putExtra(MainActivity.BATTERY_LEVEL, batLevel);


    IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    Intent batteryStatus = registerReceiver(null, ifilter);
    BatteryBroadcastReceiver receiver = new BatteryBroadcastReceiver();
    this.registerReceiver(receiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    //this.registerReceiver(this.batteryInfoReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


//    sendBroadcast(intentForBroadcast);
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

  private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {

      int  health= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
      int  icon_small= intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL,0);
      int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
      int  plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
//      boolean  present= intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
//      int  scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
//      int  status= intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
//      String  technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
//      int  temperature= intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
//      int  voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);



      mTextViewShowPercentage.setText(
              "Health: "+health+"\n"+
                      "Icon Small:"+icon_small+"\n"+
                      "Level: "+level+"\n"+
                      "Plugged: "+plugged+"\n"
                      /*"Present: "+present+"\n"+
                      "Scale: "+scale+"\n"+
                      "Status: "+status+"\n"+
                      "Technology: "+technology+"\n"+
                      "Temperature: "+temperature+"\n"+
                      "Voltage: "+voltage+"\n"*/);
      //imageBatteryState.setImageResource(icon_small);
    }
  };

  @Override
  protected void onStop() {
    unregisterReceiver(receiver);
    super.onStop();
  }
}
