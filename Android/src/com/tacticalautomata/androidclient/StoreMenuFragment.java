package com.tacticalautomata.androidclient;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class StoreMenuFragment extends Fragment {

	// containing Activity must implement this interface
	public interface StoreMenuEventsCallback {
		public void onCategorySelected(String category);
	}

	// callback for notifying container of events
	private StoreMenuEventsCallback mContainerCallback;

	// key for saving state
	private static final String KEY_CURRENT_ACTIVATED = "KEY_CURRENT_ACTIVATED";

	// the id of our loader
	private static final int LOADER_ID = 0;

	// This is the Adapter being used to display the list's data.
	private SimpleCursorAdapter mAdapter;

	// track the currently activated item
	private int mCurrentActivePosition = ListView.INVALID_POSITION;

	// default constructor
	public StoreMenuFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			// check that the containing activity implements our callback
			mContainerCallback = (StoreMenuEventsCallback) activity;
		} catch (ClassCastException e) {
			activity.finish();
			throw new ClassCastException(activity.toString() + " must implement StoreMenuEventsCallback");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// restore any saved state
		if (savedInstanceState != null && savedInstanceState.containsKey(KEY_CURRENT_ACTIVATED)) {
			mCurrentActivePosition = savedInstanceState.getInt(KEY_CURRENT_ACTIVATED, ListView.INVALID_POSITION);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.store_menu, container, false);

		Button boostersButton = (Button) v.findViewById(R.id.store_menu_boosters);
		boostersButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mContainerCallback.onCategorySelected(StoreItem.TYPE_BOOSTERS);
			}
		});
		Button robotsButton = (Button) v.findViewById(R.id.store_menu_robots);
		robotsButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mContainerCallback.onCategorySelected(StoreItem.TYPE_ROBOTS);
			}
		});
		Button decksButton = (Button) v.findViewById(R.id.store_menu_decks);
		decksButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mContainerCallback.onCategorySelected(StoreItem.TYPE_DECKS);
			}
		});

		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_CURRENT_ACTIVATED, mCurrentActivePosition);
	}
}