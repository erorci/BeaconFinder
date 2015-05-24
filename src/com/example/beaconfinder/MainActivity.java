package com.example.beaconfinder;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

public class MainActivity extends Activity {

	 BeaconManager beaconManager;

	  private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
	  private static final Region ALL_ESTIMOTE_BEACONS = new Region(ESTIMOTE_PROXIMITY_UUID, null, null, null);

	  final String TAG = "MainActivity";
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);

	    Log.e(TAG, "onCreate");

	    beaconManager = new BeaconManager(getApplicationContext());

	    if(beaconManager.isBluetoothEnabled())
	    {
	        Toast.makeText(getApplicationContext(), "BLE is enabled", Toast.LENGTH_LONG).show();
	    } else
	    {
	        Toast.makeText(getApplicationContext(), "BLE isn't enabled", Toast.LENGTH_LONG).show();
	    }

	      beaconManager.setRangingListener(new BeaconManager.RangingListener() {
	        @Override public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
	          Log.d(TAG, "Ranged beacons: " + beacons);
	          Toast.makeText(MainActivity.this,"Ranged beacons: " + beacons, Toast.LENGTH_LONG).show();
	        }           
	      });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu; this adds items to the action bar if it is present.
	    getMenuInflater().inflate(R.menu.main, menu);
	    return true;
	}

	@Override
	protected void onStart() {
	    // TODO Auto-generated method stub
	    super.onStart();
	    Log.e(TAG, "onStart");
	    beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
	        @Override public void onServiceReady() {
	          try {
	            beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
	            Toast.makeText(getApplicationContext(), "try start ranging", Toast.LENGTH_LONG).show();

	          } catch (RemoteException e) {
	            Log.e(TAG, "Cannot start ranging", e);
	            Toast.makeText(getApplicationContext(), "Cannot start ranging", Toast.LENGTH_LONG).show();
	          }
	        }
	      });
	}

	@Override
	protected void onStop() {
	    // TODO Auto-generated method stub
	    super.onStop();
	      // Should be invoked in #onStop.
	      try {
	        beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
	      } catch (RemoteException e) {
	        Log.e(TAG, "Cannot stop but it does not matter now", e);
	        Toast.makeText(getApplicationContext(), "Cannot stop but it does not matter now", Toast.LENGTH_LONG).show();
	      }


	}

	@Override
	protected void onDestroy() {
	    // TODO Auto-generated method stub
	    super.onDestroy();
	     // When no longer needed. Should be invoked in #onDestroy.
	      beaconManager.disconnect();
	}   }
