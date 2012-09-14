package com.tacticalautomata.androidclient;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProductDetailFragment extends Fragment {

	public int mProductId = 0;
	private ProductDetailEventsCallback mContainerCallback;
	private StoreItem mItem;
	private TextView mTitle;
	private TextView mDescription;
	private TextView mImageCode;
	private TextView mPrice;
	private TextView mType;
	private ImageView mImage;
	private Button mBuyButton;
	private LinearLayout mRobotLayout;
	private LinearLayout mDeckLayout;
	private LinearLayout mBoosterLayout;

	// containing Activity must implement this interface
	public interface ProductDetailEventsCallback {
		public void onPurchaseButtonClicked(StoreItem item, int quantity);
	}

	public ProductDetailFragment() {

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			// check that the containing activity implements our callback
			mContainerCallback = (ProductDetailEventsCallback) activity;
		} catch (ClassCastException e) {
			activity.finish();
			throw new ClassCastException(activity.toString() + " must implement ProductDetailEventsCallback");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup mRootView = (ViewGroup) inflater.inflate(R.layout.product_detail, container, false);

		mTitle = (TextView) mRootView.findViewById(R.id.product_name);
		mDescription = (TextView) mRootView.findViewById(R.id.product_detail);
		mImageCode = (TextView) mRootView.findViewById(R.id.product_image_code);
		mType = (TextView) mRootView.findViewById(R.id.product_class);
		mPrice = (TextView) mRootView.findViewById(R.id.product_price);
		mImage = (ImageView) mRootView.findViewById(R.id.product_image);
		mBuyButton = (Button) mRootView.findViewById(R.id.button_purchase);
		mRobotLayout = (LinearLayout) mRootView.findViewById(R.id.robot_linear_layout);
		if (mItem != null) {
			loadProduct(mItem);
		} else {
			// no product, do default stuff
			// mTitle.setText("Welcome to the steambots store!");
			// mDescription.setText("Choose a category and buy some stuff!");
		}

		return mRootView;
	}

	public void loadProduct(StoreItem storeItem) {
		mItem = storeItem;
		if (isAdded()) {
			populateFields();
		}
	}

	private void populateFields() {
		if (mItem != null) {
			int resId = getResources().getIdentifier(mItem.getImageCode(), "drawable", "com.tacticalautomata.androidclient");
			mTitle.setText(mItem.getName());
			mDescription.setText(mItem.getDescription());
			mImageCode.setText(mItem.getImageCode() + " " + resId);
			mPrice.setText("Cost: " + Integer.toString(mItem.getCost()) + " sprockets");
			mType.setText(mItem.getHumanProductType());
			mImage.setImageResource(resId);
			mBuyButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mContainerCallback.onPurchaseButtonClicked(mItem, 1);
				}
			});
			mBuyButton.setEnabled(true);

			// type specific
			if (mItem.getProductType().contentEquals(StoreItem.TYPE_ROBOTS)) {
				// show robot stats
				mRobotLayout.setVisibility(View.VISIBLE);
			} else if (mItem.getProductType().contentEquals(StoreItem.TYPE_BOOSTERS)) {
				mRobotLayout.setVisibility(View.GONE);
			} else if (mItem.getProductType().contentEquals(StoreItem.TYPE_DECKS)) {
				mRobotLayout.setVisibility(View.GONE);
			} else {
				Exception e = new Exception("Unknown product type");
				e.printStackTrace();
			}
		} else {
			mTitle.setText("Name");
			mDescription.setText("Desc");
			mImageCode.setText("Image");
			mPrice.setText("Cost");
			mType.setText("Type");
			mImage.setImageResource(R.drawable.card_electrical_bomb);
			mBuyButton.setEnabled(false);
		}
	}

	public void clearActivation() {
		// TODO Auto-generated method stub

	}
}