package com.tacticalautomata.androidclient;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StoreItemsAdapter extends ArrayAdapter<StoreItem> {

	private ArrayList<StoreItem> mItems;

	public Context mContext;

	public StoreItemsAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		mContext = context;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public StoreItem getItem(int position) {
		// TODO Auto-generated method stub
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mItems.get(position).getId();
	}

	public void setItems(ArrayList<StoreItem> scores) {
		mItems = scores;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StoreItem item = mItems.get(position);

		View view = convertView;

		if (view == null) {
			LayoutInflater viewInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = viewInflator.inflate(R.layout.product_list_row, null);
		}

		// manipulate view all up in here
		TextView rankText = (TextView) view.findViewById(R.id.product_name);
		rankText.setText(item.getName());

		TextView nameText = (TextView) view.findViewById(R.id.product_detail);
		nameText.setText(item.getDescription());

		return view;
	}
}