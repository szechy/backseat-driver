package com.cszechy.backseat_driver;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.openxc.VehicleManager;

public class Home extends ActionBarActivity implements TextToSpeech.OnInitListener, ActionBar.TabListener {
>>>>>>> 27cccbfe158dca14d5e90d0b5ed9fc944a27bd08
	private ViewPager mPager;
	private Adapter_TabsPager mPagerAdapter;
	private ActionBar actionBar;
	public static FragmentManager fm;
	private TextToSpeech tts;
	int prevGear = 0;
	boolean shiftingUp = false, shiftingDown = false;
	
    private VehicleConnection mConnection = new VehicleConnection();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		managePageNavigation();
		tts = new TextToSpeech(this, this);
	}

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
	
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
            }
        } else { }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
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
		
        fm = getSupportFragmentManager();
		
		mPagerAdapter = new Adapter_TabsPager(fm);
		mPager.setAdapter(mPagerAdapter);
		mPager.setOffscreenPageLimit(3);
		
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() { 
		    public void onPageSelected(int position) { 
		    	actionBar.setSelectedNavigationItem(position);
		    	if (position==1)
		    		startRepeatingTask();
		    }
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
        stopRepeatingTask();
    }
    
    private final static int INTERVAL = 1000; //1000ms
    Handler mHandler = new Handler();

    Runnable mHandlerTask = new Runnable() {
         @Override 
         public void run() {
              Listen task = new Listen();
              task.execute();
              mHandler.postDelayed(mHandlerTask, INTERVAL);
         }
    };

    void startRepeatingTask() { mHandlerTask.run(); }
    void stopRepeatingTask() { mHandler.removeCallbacks(mHandlerTask); }
    
    private String prevSaying;
	
    private class Listen extends AsyncTask <Void, Boolean, int[]> {
		private String nextAction;
		private boolean accel, brake, clutch;
		private int shift = 0;
		
		@Override
		protected int[] doInBackground(Void...params) {
			CarDataPacket cardata = mConnection.getAllData();
			ShiftingLogic shifter = new ShiftingLogic(cardata, prevGear, shiftingUp, shiftingDown);
			shifter.printParams();
			shifter.determineAction();
			accel = shifter.getAccelerator();
			brake = shifter.getBrake();
			clutch = shifter.getClutch();
			shift = shifter.getShifter();
			nextAction = shifter.getNextDirection();
			if (!nextAction.equals(prevSaying)){
				tts.stop();
				tts.speak(nextAction, TextToSpeech.QUEUE_FLUSH, null);
			}
			prevGear = shift;
			shiftingDown = shifter.getShiftDown();
			shiftingUp = shifter.getShiftUp();
			boolean[] passAlong = {accel, brake, clutch};
			onProgressUpdate(passAlong);
			int[] shifterPlusOne = {shifter.getShifter(), 1};
			return shifterPlusOne;
		}
		
		public void calcRoute(View view) {
			((Fragment_Map)mPagerAdapter.getRegisteredFragment(2)).calcRoute();
		}

		protected void onProgressUpdate(boolean... progress) {
			Log.d("Accel, Brake, Clutch", "" + accel + ", " + brake + ", " + clutch);
			runOnUiThread(new Runnable() {
			     @Override
			     public void run() {
			    	((Fragment_Instructions)mPagerAdapter.getRegisteredFragment(1)).accel(accel);
					((Fragment_Instructions)mPagerAdapter.getRegisteredFragment(1)).brake(brake);
					((Fragment_Instructions)mPagerAdapter.getRegisteredFragment(1)).clutch(clutch);
			    }
			});
		}
		
		@Override
		protected void onPostExecute(final int[] shifterPlusOne) { 
			runOnUiThread(new Runnable() {
			     @Override
			     public void run() {
			((Fragment_Instructions) mPagerAdapter.getRegisteredFragment(1)).data(nextAction);
			((Fragment_Instructions) mPagerAdapter.getRegisteredFragment(1)).shifter(shifterPlusOne[0]);
			     }});
			/*if (results[0]) shiftImage.setImageResource(R.drawable.gearnuetral_lit);
			if (results[1]) shiftImage.setImageResource(R.drawable.gearfirst_lit);
			if (results[2]) shiftImage.setImageResource(R.drawable.gearsecond_lit);
			if (results[3]) shiftImage.setImageResource(R.drawable.gearthird_lit);
			if (results[4]) shiftImage.setImageResource(R.drawable.gearforth_lit);
			if (results[5]) shiftImage.setImageResource(R.drawable.gearfifth_lit);
			if (results[6]) shiftImage.setImageResource(R.drawable.gearsixth_lit);*/
		}
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
	}
}
