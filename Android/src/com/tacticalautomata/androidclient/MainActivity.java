package com.tacticalautomata.androidclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	// action to launch straight to a specific game
	public static final String ACTION_VIEW_GAME = "com.example.android.honeypad.ACTION_VIEW_GAME";

	// extra for the above action
	public static final String EXTRA_GAME_ID = "gameId";
	public static final String API_ROOT = "gameId";

	public static String API_PROTOCOL = "http";
	public static String API_HOST = "www.tacticalautomata.com";
	public static String API_PATH = "/api/public/";

	// key for adding a game to this Activity
	private static final String GAME_FLAG = "view_game";

	// settings indexes
	protected SharedPreferences settings = null;
	public static final String PREFS_NAME = "TacticalAutomataPrefs";
	public static final String LOGIN_TOKEN = "login_token";
	public static final String PLAYER_NAME = "player_name";

	private Context mContext;
	public SharedPreferences mSettings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;

		mSettings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
		setContentView(R.layout.main);

		if (mSettings.getString(MainActivity.LOGIN_TOKEN, null) != null) {
			// user is logged in, cool
		} else {
			startActivity(new Intent(mContext, LoginActivity.class));
			finish();
		}

		if (ACTION_VIEW_GAME.equals(getIntent().getAction())) {
			viewGame(getIntent());
		}

		initButtons();
	}

	private void initButtons() {
		
		Button loginButton = (Button) findViewById(R.id.button_login_signup);
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(mContext, LoginActivity.class));
			}
		});
		Button profileButton = (Button) findViewById(R.id.button_profile);
		profileButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(mContext, ProfileActivity.class));
			}
		});
		Button storeButton = (Button) findViewById(R.id.button_store);
		storeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(mContext, StoreActivity.class));
			}
		});
		Button decksButton = (Button) findViewById(R.id.button_decks);
		decksButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(mContext, DecksActivity.class));
			}
		});

		if (mSettings.getString(MainActivity.LOGIN_TOKEN, null) != null) {
			// user is logged in, cool
		} else {
			loginButton.setVisibility(View.GONE);
		}
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (ACTION_VIEW_GAME.equals(intent.getAction())) {
			viewGame(intent);
		}
	}

	private void viewGame(Intent intent) {
		// intent could have an id bundled with it, check here
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.splash_img:
			showGameCreate(null);
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void showGameCreate(Object object) {
		// TODO Auto-generated method stub
	}
}