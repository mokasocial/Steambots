package com.tacticalautomata.androidclient;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AutomataActivity {

	public Context mContext;
	private ProgressDialog mProgressDialog;
	private String mErrorText;
	public SharedPreferences mSettings;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = this;

		setContentView(R.layout.login);

		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setCancelable(true);

		mSettings = getSharedPreferences(MainActivity.PREFS_NAME, 0);

		initButtons();
	}

	public void initButtons() {
		// Login
		Button loginButton = (Button) findViewById(R.id.button_login);
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mProgressDialog.show();
				new AsyncLogin().execute();
			}
		});
		// Sign up
		Button signupButton = (Button) findViewById(R.id.button_signup);
		signupButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivityForResult(new Intent(mContext, SignupActivity.class), 1);
			}
		});
	}

	public boolean doUserLogin(String email, String password) {

		if (email.contentEquals("") || password.contentEquals("")) {
			mErrorText = "Please enter your email and password, or click the Sign Up button to create an account.";
			return false;
		}

		try {
			if (!isOnline()) {
				throw new Exception("There was a network failure, please be so gracious as to connect to the internet!");
			}

			List<NameValuePair> params = new LinkedList<NameValuePair>();
			params.add(new BasicNameValuePair("username", email));
			params.add(new BasicNameValuePair("password", password));
			String paramString = URLEncodedUtils.format(params, "utf-8");

			URL apiUrl = new URL(MainActivity.API_PROTOCOL, MainActivity.API_HOST, MainActivity.API_PATH + "user?" + paramString);
			HttpGet httpRequest = new HttpGet(apiUrl.toExternalForm());

			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(httpRequest);

			// if (response.getStatusLine().getStatusCode() != 200) {
			// Log.d("Login", response.getStatusLine().getStatusCode() + "");
			// Log.d("Login", response.getStatusLine().getReasonPhrase());
			// throw new
			// Exception("This username and password combination is, I'm sorry to say, invalid.");
			// }

			// check for json-level errors
			JSONObject loginJSON = JSONFactory.getJSONObject(response);
			String loginError = loginJSON.optString("error_code");
			if (!loginError.isEmpty()) {
				Log.d("Login error", loginError);
				mErrorText = loginError;
				return false;
			} else {
				// none? we're good then
				Log.d("Login", loginJSON.toString() + "");
				String token = loginJSON.getString("token");
				Editor editableSettings = mSettings.edit();
				editableSettings.putString(MainActivity.LOGIN_TOKEN, token);
				editableSettings.commit();
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
			mErrorText = "Host was not reachable, maybe the server is down?";
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			mErrorText = e.getClass().toString();
			return false;
		}
		// if we got here, we're good.
		return true;
	}

	// Do all the time intensive work here.
	private class AsyncLogin extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... args) {
			EditText email = (EditText) findViewById(R.id.email);
			EditText password = (EditText) findViewById(R.id.password);

			return doUserLogin(email.getText().toString(), password.getText().toString());
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mProgressDialog.dismiss();
			if (result) {
				finish();
				startActivity(new Intent(mContext, MainActivity.class));
			} else {
				Toast toast = Toast.makeText(mContext, mErrorText, Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}
}