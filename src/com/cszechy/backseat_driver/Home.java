package com.cszechy.backseat_driver;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class Home extends ActionBarActivity implements ActionBar.TabListener {
	private ViewPager mPager;
	private Adapter_TabsPager mPagerAdapter;
	private ActionBar actionBar;
	public static FragmentManager fm;

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
		if (id == R.id.action_settings) {
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

}
