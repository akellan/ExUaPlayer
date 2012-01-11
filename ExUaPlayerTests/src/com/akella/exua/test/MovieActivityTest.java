package com.akella.exua.test;

import com.akella.exua.ExMoviePage;
import com.akella.exua.MovieActivity;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

public class MovieActivityTest extends
		ActivityInstrumentationTestCase2<MovieActivity> {
	
	MovieActivity mActivity;

	public MovieActivityTest() {
		super("com.akella.exua", MovieActivity.class);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void setUp() throws Exception {
		
		
		
		ExMoviePage pData = new ExMoviePage();
		pData.Title = "Test Film / 2011";
		pData.Link = "/view/9412310?r=2,23775";
		
		Intent intent = new Intent();
		intent.putExtra(MovieActivity.extraName, pData);
		
		setActivityIntent(intent);
		
		mActivity = getActivity();
		
		super.setUp();
	}
	
	
	
	public void testStart()
	{
		assertNotNull(mActivity.moviePage.FlvFullLink);
	}
}
