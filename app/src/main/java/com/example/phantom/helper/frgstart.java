package com.example.phantom.helper;


import java.util.ArrayList;
import java.util.Arrays;



import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.*;
import android.widget.*;
import android.content.*;
//import com.filenanumi.mapp.Parse.*;

import android.net.*;

@SuppressLint("ValidFragment")
public class frgstart extends Fragment {
	Context mContext;
	private ArrayList<HashMap<String, String>> list_board = new ArrayList<HashMap<String, String>>();
    private SimpleAdapter sa;
    private ArrayList<String> contents;
    int firstload =0;
    private ListView lvn;

	public frgstart(Context context) {
		mContext = context;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.v("frg_start", "create");
		View view = inflater.inflate(R.layout.frg_start, null);

//		RSSParse rss = new RSSParse(contents);
//		rss.threadRssParse(getActivity(), sa, list_board, "http://www.filenanumi.com/rss");
		Button btn_pri = (Button) view.findViewById(R.id.btn2);
		btn_pri.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					((com.example.phantom.helper.MainActivity)getActivity()).setCurrentPagerItem(0);

				}
			});
		return view;
	}

    /*	
	 public void refresh(View view) {  
	 Toast.makeText(getActivity(), "Touched! ", Toast.LENGTH_SHORT).show();
	 }

	 */

}
