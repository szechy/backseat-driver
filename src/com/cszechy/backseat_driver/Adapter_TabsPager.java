package com.cszechy.backseat_driver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.ViewGroup;

public class Adapter_TabsPager extends FragmentPagerAdapter {
	private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
	private FragmentManager fm;
	
	public Adapter_TabsPager(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }
 
    @Override
    public Fragment getItem(int position) {
    	Bundle data = new Bundle();
		data.putInt("current_page", position+1);
        switch(position){
            case 0:
            	Fragment_Main frag1 = new Fragment_Main();
                frag1.setArguments(data);
                return frag1;
            case 1:
            	Fragment_Map frag2 = new Fragment_Map();
                frag2.setArguments(data);
                return frag2;
            case 2:
            	Fragment_Instructions frag3 = new Fragment_Instructions();
                frag3.setArguments(data);
                return frag3;
            	
        }
        return null;
    }
    
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
    
    public Fragment getActiveFragment(ViewPager container, int position) {
    	String name = makeFragmentName(container.getId(), position);
    	return  fm.findFragmentByTag(name);
	}

	private static String makeFragmentName(int viewId, int index) {
	    return "android:switcher:" + viewId + ":" + index;
	}
 
    @Override
    public int getCount() {
        return 3;
    }
}
