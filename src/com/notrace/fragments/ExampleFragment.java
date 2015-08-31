package com.notrace.fragments;

import com.notrace.R;

import android.os.Bundle;
import android.view.View;

public class ExampleFragment extends LazyFragment {
	private boolean isPrepared;

	@Override
	public void bindListener(View parentView) {
		
	}

	@Override
	public void initData() {
	       isPrepared = true;
	        if (!isPrepared || !isVisible) {
	            return;
	   }
	}

	@Override
	protected int onLayoutIdGenerated() {
		return R.layout.activity_main;
	}

	@Override
	protected void onViewCreated(View parentView) {
		
	}
}

	
