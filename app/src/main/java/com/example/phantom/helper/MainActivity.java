package com.example.phantom.helper;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.BroadcastReceiver;
import android.graphics.*;
import android.view.*;
import android.content.*;
import android.widget.*;

// import com.filenanumi.mapp.RecycleUtils;
import android.app.*;


public class MainActivity extends AppCompatActivity
 {

	private Typeface mTypeface;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setCurrentItem(0);
	//	SharedPreferences pref = getSharedPreferences("prefDATE", MODE_PRIVATE);
	//	Integer See_nomore = pref.getInt("see_nomore", 0);
        /*
		if(See_nomore == 0)
		{
			startActivity(new Intent(this, TransparentActivity.class));  
		} else {}
        */
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
*/

	public void setCurrentPagerItem(int item) {
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setCurrentItem(item);
	}
	
	@Override
	protected void onDestroy() {
//		RecycleUtils.recursiveRecycle(getWindow().getDecorView());
		System.gc();
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/* back key destroy */
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				onBackPressed();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
			.setMessage("정말로 종료할까요?")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					//stopService(new Intent("com.filenanumi.hangukji.MusicService"));
					System.gc();
                    finish();
				}
			})
			.setNegativeButton("No", null)
			.show();
	}
}
	
