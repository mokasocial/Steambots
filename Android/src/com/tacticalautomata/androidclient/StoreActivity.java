package com.tacticalautomata.androidclient;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tacticalautomata.androidclient.ProductDetailFragment.ProductDetailEventsCallback;
import com.tacticalautomata.androidclient.StoreDetailFragment.StoreDetailEventsCallback;
import com.tacticalautomata.androidclient.StoreMenuFragment.StoreMenuEventsCallback;

public class StoreActivity extends AutomataActivity implements ProductDetailEventsCallback, StoreMenuEventsCallback, StoreDetailEventsCallback {

	private Context mContext;

	// keys for adding fragments to this Activity
	private static final String STORE_DETAIL_TAG = "STORE_DETAIL";
	private static final String PRODUCT_DETAIL_TAG = "PRODUCT_DETAIL";

	// for animation shite
	private static final String ROTATION_AXIS_PROP = "rotationY";
	private static final int ROTATION_HALF_DURATION = 250;
	private ProgressDialog mProgressDialog;
	public SharedPreferences mSettings;
	private String mErrorText;

	private ArrayList<StoreItem> mAllItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store);

		mContext = this;

		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setCancelable(true);

		mSettings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
		mAllItems = new ArrayList<StoreItem>();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		StoreDetailFragment frag = new StoreDetailFragment();
		ft.add(R.id.store_detail_container, frag, STORE_DETAIL_TAG);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.commit();

		new AsyncFetchStore().execute();
	}

	public void onCategorySelected(final String category) {
		// check if the StoreDetailFragment has been added
		FragmentManager fm = getFragmentManager();
		StoreDetailFragment frag = (StoreDetailFragment) fm.findFragmentByTag(STORE_DETAIL_TAG);
		final boolean detailFragPresent = (frag != null);

		if (detailFragPresent) {
			Log.d("StoreActivity", "Detail frag present, reloading");

			if (frag.mCurrentCategory != null && frag.mCurrentCategory.equals(category)) {
				// clicked on the currently selected category
				return;
			}

			ObjectAnimator anim = ObjectAnimator.ofFloat(frag.getView(), ROTATION_AXIS_PROP, 0, 90).setDuration(ROTATION_HALF_DURATION);
			anim.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					StoreDetailFragment detailFrag = (StoreDetailFragment) getFragmentManager().findFragmentByTag(STORE_DETAIL_TAG);
					if (category != null) {
						detailFrag.loadCategory(category);
					} else {
						if (detailFragPresent) {
							detailFrag.clearActivation();
						}
						StoreMenuFragment list = (StoreMenuFragment) getFragmentManager().findFragmentById(R.id.shop_menu);
						// list.clearActivation();
					}
					// rotate in the new fragment
					ObjectAnimator.ofFloat(detailFrag.getView(), ROTATION_AXIS_PROP, -90, 0).start();
				}
			});
			anim.start();
		} else {
			Log.d("StoreActivity", "No detail frag present");
			// add the Fragment to the container
			FragmentTransaction ft = fm.beginTransaction();
			frag = new StoreDetailFragment();
			ft.add(R.id.store_detail_container, frag, STORE_DETAIL_TAG);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			ft.commit();
			frag.loadCategory(category);
		}
	}

	public void onStoreItemSelected(final StoreItem storeItem) {
		// check if the ProductDetailFragment has been added
		FragmentManager fm = getFragmentManager();
		ProductDetailFragment frag = (ProductDetailFragment) fm.findFragmentByTag(PRODUCT_DETAIL_TAG);
		final boolean detailFragPresent = (frag != null);

		if (detailFragPresent) {
			Log.d("StoreActivity", "Product frag present, reloading");

			if (frag.mProductId != 0 && frag.mProductId == storeItem.getId()) {
				// clicked on the currently selected product
				return;
			}

			// animate the frag transition. We do this in 3 steps:
			// 1. Rotate out the current product
			// 2. Switch the data to the new product
			// 3. Rotate in the new product
			ObjectAnimator anim = ObjectAnimator.ofFloat(frag.getView(), ROTATION_AXIS_PROP, 0, 90).setDuration(ROTATION_HALF_DURATION);
			anim.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					ProductDetailFragment detailFrag = (ProductDetailFragment) getFragmentManager().findFragmentByTag(PRODUCT_DETAIL_TAG);
					if (storeItem != null) {
						detailFrag.loadProduct(storeItem);
					} else {
						// displaying a new product - clear the form
						// activation
						if (detailFragPresent) {
							detailFrag.clearActivation();
						}
						// StoreMenuFragment list = (StoreMenuFragment)
						// getFragmentManager().findFragmentById(R.id.shop_menu);
					}
					// rotate in the new layout
					ObjectAnimator.ofFloat(detailFrag.getView(), ROTATION_AXIS_PROP, -90, 0).start();
				}
			});
			anim.start();
		} else {
			Log.d("StoreActivity", "No product frag present");
			// add the Fragment to the container
			FragmentTransaction ft = fm.beginTransaction();
			frag = new ProductDetailFragment();
			ft.add(R.id.product_detail_container, frag, PRODUCT_DETAIL_TAG);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			ft.commit();
			frag.loadProduct(storeItem);
		}
	}

	// Do all the time intensive work here.
	private class AsyncFetchStore extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... args) {

			try {
				if (!isOnline()) {
					throw new Exception("There was a network failure, please be so gracious as to connect to the internet!");
				}

				String token = mSettings.getString(MainActivity.LOGIN_TOKEN, "");

				if (token.isEmpty()) {
					clearLocalData();
				} else {
					List<NameValuePair> params = new LinkedList<NameValuePair>();
					params.add(new BasicNameValuePair("token", token));
					String paramString = URLEncodedUtils.format(params, "utf-8");

					URL apiUrl = new URL(MainActivity.API_PROTOCOL, MainActivity.API_HOST, MainActivity.API_PATH + "get_store_items?" + paramString);
					HttpGet httpRequest = new HttpGet(apiUrl.toExternalForm());

					HttpClient httpclient = new DefaultHttpClient();
					HttpResponse response = httpclient.execute(httpRequest);

					if (response.getStatusLine().getStatusCode() != 200) {
						// errors come as a JSONObject
						JSONObject loginJSON = JSONFactory.getJSONObject(response);
						String loginError = loginJSON.optString("error_code");
						Log.d("Store error", loginError);
						mErrorText = loginError;
						return false;
					}

					// but successful responses come as JSONArrays
					JSONArray responseJSONArray = JSONFactory.getJSONArray(response);

					Log.d("store items json", responseJSONArray.toString());

					mAllItems = new ArrayList<StoreItem>();
					for (int i = 0; i < responseJSONArray.length(); i++) {
						// id name description image cost
						JSONObject thisJSONItem = responseJSONArray.getJSONObject(i);
						StoreItem thisStoreItem = new StoreItem();
						thisStoreItem.setId(thisJSONItem.getInt("id"));
						thisStoreItem.setName(thisJSONItem.getString("name"));
						thisStoreItem.setDescription(thisJSONItem.getString("description"));
						thisStoreItem.setImageCode(thisJSONItem.getString("image"));
						thisStoreItem.setCost(thisJSONItem.getInt("cost"));
						thisStoreItem.setProductType(thisJSONItem.getString("class"));

						mAllItems.add(thisStoreItem);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mProgressDialog.dismiss();
			if (result) {
				FragmentManager fm = getFragmentManager();
				StoreDetailFragment frag = (StoreDetailFragment) fm.findFragmentByTag(STORE_DETAIL_TAG);
				final boolean detailFragPresent = (frag != null);

				if (detailFragPresent) {
					Log.d("StoreActivity", "Detail frag present, reloading");
					frag.setItems(mAllItems);
				}
			} else {
				Toast toast = Toast.makeText(mContext, mErrorText, Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}

	public void onPurchaseButtonClicked(StoreItem item, int quantity) {
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("Completing sprocket transaction sequence");
		mProgressDialog.setCancelable(false);

		AsyncDoPurchase task = new AsyncDoPurchase();
		task.setParams(item, quantity);
		task.execute();
	}

	private class AsyncDoPurchase extends AsyncTask<Void, Void, Boolean> {

		private StoreItem purchaseItem;
		private int purchaseQty;

		public void setParams(StoreItem item, int quantity) {
			purchaseItem = item;
			purchaseQty = quantity;
		}

		@Override
		protected Boolean doInBackground(Void... args) {
			try {
				if (!isOnline()) {
					throw new Exception("There was a network failure, please kindly connect to the internet and try again!");
				}

				String token = mSettings.getString(MainActivity.LOGIN_TOKEN, "");

				if (token.isEmpty()) {
					clearLocalData();
				} else {
					List<NameValuePair> params = new LinkedList<NameValuePair>();
					params.add(new BasicNameValuePair("token", token));
					params.add(new BasicNameValuePair("store_item_id", String.valueOf(purchaseItem.getId())));
					String paramString = URLEncodedUtils.format(params, "utf-8");

					URL apiUrl = new URL(MainActivity.API_PROTOCOL, MainActivity.API_HOST, MainActivity.API_PATH + "buy_item?" + paramString);
					HttpGet httpRequest = new HttpGet(apiUrl.toExternalForm());
					HttpClient httpclient = new DefaultHttpClient();
					HttpResponse response = httpclient.execute(httpRequest);

					if (response.getStatusLine().getStatusCode() != 200) {
						// errors come as a JSONObject
						JSONObject loginJSON = JSONFactory.getJSONObject(response);
						String loginError = loginJSON.optString("error_code");
						Log.d("Store error", loginError);
						mErrorText = loginError;
						return false;
					}

					// but successful responses come as JSONArrays
					JSONArray responseJSONArray = JSONFactory.getJSONArray(response);

					Log.d("store items json", responseJSONArray.toString());

					mAllItems = new ArrayList<StoreItem>();
					for (int i = 0; i < responseJSONArray.length(); i++) {
						// id name description image cost
						JSONObject thisJSONItem = responseJSONArray.getJSONObject(i);
						StoreItem thisStoreItem = new StoreItem();
						thisStoreItem.setId(thisJSONItem.getInt("id"));
						thisStoreItem.setName(thisJSONItem.getString("name"));
						thisStoreItem.setDescription(thisJSONItem.getString("description"));
						thisStoreItem.setImageCode(thisJSONItem.getString("image"));
						thisStoreItem.setCost(thisJSONItem.getInt("cost"));
						thisStoreItem.setProductType(thisJSONItem.getString("class"));

						mAllItems.add(thisStoreItem);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return true;
		}
	}
}