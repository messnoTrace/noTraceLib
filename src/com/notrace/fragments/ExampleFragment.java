package com.notrace.fragments;

import com.notrace.R;

import android.os.Bundle;
import android.view.View;

public class ExampleFragment extends LazyFragment {
	private static final String ARG_POSITION = "position";
	private int position = 1;
	private boolean isPrepared;

	@Override
	protected int onLayoutIdGenerated() {
		return R.layout.activity_main;
	}

	public static ExampleFragment newInstance(int position) {
		ExampleFragment f = new ExampleFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parseArguments();
	}

	private void parseArguments() {
		position = getArguments().getInt(ARG_POSITION);
	}

	@Override
	protected void lazyLoad() {
		isPrepared = true;
		if (!isPrepared || !isVisible) {
			return;
		}

	}

	@Override
	protected void onViewCreated(View parentView) {

		lazyLoad();
	}
}
