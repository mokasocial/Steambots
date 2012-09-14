package com.tacticalautomata.androidclient;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class StoreDetailFragment extends ListFragment {

	// containing Activity must implement this interface
	public interface StoreDetailEventsCallback {
		public void onStoreItemSelected(StoreItem storeItem);
	}

	// key for saving state
	private static final String KEY_CURRENT_ACTIVATED = "KEY_CURRENT_ACTIVATED";

	public String mCurrentCategory;

	// This is the Adapter being used to display the list's data.
	private StoreItemsAdapter mAdapter;

	// callback for notifying container of events
	private StoreDetailEventsCallback mContainerCallback;

	// track the currently activated item
	private int mCurrentActivePosition = ListView.INVALID_POSITION;

	// track if we need to set an item to activated once data is loaded
	private long mStoreItemToActivate = -1;

	private ArrayList<StoreItem> mItems;

	// default constructor
	public StoreDetailFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setEmptyText("Loading products...");

		// create an empty adapter, our Loader will retrieve the data
		// asynchronously

		Activity mActivity = getActivity();

		// create an empty adapter, our Loader will retrieve the data async
		mAdapter = new StoreItemsAdapter(mActivity, 0);
		mItems = new ArrayList<StoreItem>();
		mAdapter.setItems(mItems);

		// mAdapter = new StoreItemListAdapter(getActivity(),
		// R.layout.product_list_row);
		setListAdapter(mAdapter);

		// setup our list view
		final ListView productList = getListView();
		productList.setDivider(null);
		productList.setDividerHeight(2);
		productList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		// restore any saved state
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(KEY_CURRENT_ACTIVATED)) {
			mCurrentActivePosition = savedInstanceState.getInt(
					KEY_CURRENT_ACTIVATED, ListView.INVALID_POSITION);
		}

		String category = "null";
		mCurrentCategory = category;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			// check that the containing activity implements our callback
			mContainerCallback = (StoreDetailEventsCallback) activity;
		} catch (ClassCastException e) {
			activity.finish();
			throw new ClassCastException(activity.toString()
					+ " must implement StoreDetailEventsCallback");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_CURRENT_ACTIVATED, mCurrentActivePosition);
	}

	protected void setActivatedStoreItem(long id) {
		if (mAdapter != null) {
			// work out the position in the list of items with the given id
			final int N = mAdapter.getCount();
			for (int position = 0; position < N; position++) {
				if (mAdapter.getItemId(position) == id) {
					if (position != mCurrentActivePosition) {
						clearActivation();
						mCurrentActivePosition = position;
						View row = getListView().getChildAt(position);
						if (row != null) {
							row.setActivated(true);
						}
					}
					break;
				}
			}
		} else {
			// if we have not loaded our cursor yet then store the note id
			// for now & activate once loaded
			mStoreItemToActivate = id;
		}
	}

	/**
	 * Helper method to clear the list's activated state
	 */
	protected void clearActivation() {
		if (mCurrentActivePosition != ListView.INVALID_POSITION) {
			getListView().getChildAt(mCurrentActivePosition)
					.setActivated(false);
		}
		mCurrentActivePosition = ListView.INVALID_POSITION;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mCurrentActivePosition = position;
		mContainerCallback.onStoreItemSelected(mAdapter.getItem(position));
	}

	public void loadCategory(String category) {
		ArrayList<StoreItem> newItems = new ArrayList<StoreItem>();
		// Called by parent - load a new StoreDetailEventsCallback!
		for (StoreItem item : mItems) {
			if (item.getProductType().contentEquals(category)) {
				newItems.add(item);
			}
		}
		mAdapter.setItems(newItems);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// check if we need to set one of the (now loaded) products as activated
		if (mStoreItemToActivate > -1) {
			setActivatedStoreItem(mStoreItemToActivate);
			mStoreItemToActivate = -1;
		}
	}

	public void setItems(ArrayList<StoreItem> items) {
		this.mItems = items;
	}
}