package com.notrace.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (onLayoutIdGenerated() < 0)
			throw new IllegalArgumentException(
					" ID for an XML layout resource can't not be 0 !");
		View view = inflater.inflate(onLayoutIdGenerated(), container, false);
		onViewCreated(view);
		return view;
	}

	/**
	 * Called when the fragment 's layout 's id is generated
	 */
	protected abstract int onLayoutIdGenerated();

	/**
	 * Called when the fragment view is fully created
	 */
	protected abstract void onViewCreated(View parentView);

	public final void replaceFragment(Fragment newFragment, int containerViewId) {
		FragmentTransaction ft = getFragmentTransaction();
		ft.replace(containerViewId, newFragment);
		ft.addToBackStack(null);// Add this transaction to the back stack
		ft.commit();
	}

	public final void addFragment(Fragment fragment, int containerId) {
		FragmentTransaction ft = getFragmentTransaction();
		ft.add(containerId, fragment);
		ft.commit();
	}

	public final void addFragment(int containerId, Fragment fragment, String tag) {
		FragmentTransaction ft = getFragmentTransaction();
		ft.add(containerId, fragment, tag);
		ft.commit();
	}

	public final FragmentTransaction getFragmentTransaction() {
		return getFragmentManager().beginTransaction();
	}

	/**
	 * look view from the fragment's whole parent view for the given id.
	 * 
	 * @param resId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T findView(int resId) {
		if (getView() == null)
			return null;
		View view = getView().findViewById(resId);
		if (view == null)
			throw new NullPointerException(
					"can't find any view associated with the resId");
		return (T) view;
	}
}
