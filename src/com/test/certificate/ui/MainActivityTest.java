package com.test.certificate.ui;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.test.certificate.R;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private static Activity mActivity;
	private TextView mTv;
	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		setActivityInitialTouchMode(false);
	    mActivity = getActivity();
	    mTv = (TextView)mActivity.findViewById(R.id.textView);
	}

	public void testPreConditions() {
	    assertEquals(mTv.getText().toString(),"Hey!");
	  }
	
}
