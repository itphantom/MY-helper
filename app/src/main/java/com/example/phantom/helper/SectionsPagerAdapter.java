package com.example.phantom.helper;

//import com.paikyang.pproject.MainfrgActivity;
//import com.filenanumi.mapp.webview.WebViewMainFragment;
//import com.filenanumi.mapp.webview.WebViewDevFragment;
//import com.filenanumi.mapp.webview.WebViewTwtFragment;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
//import com.filenanumi.mapp.webview.*;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
	Context mContext;

	public SectionsPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		switch(position) {
		case 0:
			return new frgready(mContext);
		case 1:
			return new frgstart(mContext);
		}
		return null;
	}

	@Override
	public int getCount() {
		// Show 7 total pages.
		return 2;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			return mContext.getString(R.string.title_section1).toUpperCase();
		case 1:
			return mContext.getString(R.string.title_section2).toUpperCase();
		}
		return null;
	}

}
