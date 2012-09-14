package com.tacticalautomata.androidclient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

public class AutomataActivity extends Activity {

	public boolean isOnline() {
		try {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void clearLocalData(){
		SharedPreferences mSettings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
		mSettings.edit().clear();
		mSettings.edit().commit();
	}
}
