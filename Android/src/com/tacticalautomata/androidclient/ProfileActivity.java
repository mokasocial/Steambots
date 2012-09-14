package com.tacticalautomata.androidclient;

import java.net.URL;
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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class ProfileActivity extends AutomataActivity {
	
	public Context mContext;
	private ProgressDialog mProgressDialog;
	public SharedPreferences mSettings;
	public String mErrorText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.profile);

		mContext = this;
		mSettings = getSharedPreferences(MainActivity.PREFS_NAME, 0);

		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage(getString(R.string.loading_profile));
		mProgressDialog.setCancelable(false);
		
		new AsyncFetchProfile().execute();
	}
	
	private class AsyncFetchProfile extends AsyncTask<Void, Void, Boolean> {

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
					String paramString = URLEncodedUtils.format(params, "utf-8");

					URL apiUrl = new URL(MainActivity.API_PROTOCOL, MainActivity.API_HOST, MainActivity.API_PATH + "profile?" + paramString);
					HttpGet httpRequest = new HttpGet(apiUrl.toExternalForm());
					HttpClient httpclient = new DefaultHttpClient();
					HttpResponse response = httpclient.execute(httpRequest);
					
					Log.d("Api Url", apiUrl.toExternalForm());

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

					Log.d("profile json", responseJSONArray.toString());

//					mAllItems = new ArrayList<StoreItem>();
//					for (int i = 0; i < responseJSONArray.length(); i++) {
//						// id name description image cost
//						JSONObject thisJSONItem = responseJSONArray.getJSONObject(i);
//						StoreItem thisStoreItem = new StoreItem();
//						thisStoreItem.setId(thisJSONItem.getInt("id"));
//						thisStoreItem.setName(thisJSONItem.getString("name"));
//						thisStoreItem.setDescription(thisJSONItem.getString("description"));
//						thisStoreItem.setImageCode(thisJSONItem.getString("image"));
//						thisStoreItem.setCost(thisJSONItem.getInt("cost"));
//						thisStoreItem.setProductType(thisJSONItem.getString("class"));
//
//						mAllItems.add(thisStoreItem);
//					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return true;
		}
	}
}