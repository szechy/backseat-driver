package com.cszechy.backseat_driver;

import com.openxc.VehicleManager;
import com.openxc.measurements.AcceleratorPedalPosition;
import com.openxc.measurements.EngineSpeed;
import com.openxc.measurements.FuelLevel;
import com.openxc.measurements.IgnitionStatus;
import com.openxc.measurements.ParkingBrakeStatus;
import com.openxc.measurements.TransmissionGearPosition;
import com.openxc.measurements.TurnSignalStatus;
import com.openxc.measurements.UnrecognizedMeasurementTypeException;
import com.openxc.measurements.VehicleSpeed;
import com.openxc.remote.VehicleServiceException;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Home extends ActionBarActivity implements ActionBar.TabListener {
	private ViewPager mPager;
	private Adapter_TabsPager mPagerAdapter;
	private ActionBar actionBar;
	
    private VehicleConnection mConnection = new VehicleConnection();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		managePageNavigation();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		mConnection.printOutDebugSensors();
		if (id == R.id.action_settings) {
			//mConnection.printOutDebugSensors();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void managePageNavigation() {
        mPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
        FragmentManager fm = getSupportFragmentManager();
		
		mPagerAdapter = new Adapter_TabsPager(fm);
		mPager.setAdapter(mPagerAdapter);
		mPager.setOffscreenPageLimit(3);
		
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() { 
		    public void onPageSelected(int position) { actionBar.setSelectedNavigationItem(position); }
		    public void onPageScrolled(int arg0, float arg1, int arg2) { }
		    public void onPageScrollStateChanged(int arg0) { }
		});
		
		//Create the tabs and fragments
		ActionBar.Tab tab1 = actionBar.newTab().setText(getResources().getString(R.string.tab1));
		actionBar.addTab(tab1.setTabListener(this));
		ActionBar.Tab tab2 = actionBar.newTab().setText(getResources().getString(R.string.tab2));
		actionBar.addTab(tab2.setTabListener(this));
		ActionBar.Tab tab3 = actionBar.newTab().setText(getResources().getString(R.string.tab3));
		actionBar.addTab(tab3.setTabListener(this));
	}
	
	public void onTabSelected(Tab tab, FragmentTransaction ft) { 
		supportInvalidateOptionsMenu(); 
		mPager.setCurrentItem(tab.getPosition()); 
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) { }

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) { }
	
    @Override
    public void onResume() {
        super.onResume();
        // When the activity starts up or returns from the background,
        // re-connect to the VehicleManager so we can receive updates.
        Log.i("HomeActivity", "onResume");
        if(mConnection.getVehicleManager() == null) {
            Intent intent = new Intent(this, VehicleManager.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
        Log.i("HomeActivity", "onPause");
        // When the activity goes into the background or exits, we want to make
        // sure to unbind from the service to avoid leaking memory
        if(mConnection.getVehicleManager() != null) {
            Log.i("HomeActivity", "Unbinding from Vehicle Manager");
            // Remember to remove your listeners, in typical Android
			// fashion.
			mConnection.removeAllListeners();
            unbindService(mConnection);
            mConnection.setVehicleManager(null);
        }
    }
    
 
}
