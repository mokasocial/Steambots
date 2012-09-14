package com.tacticalautomata.androidclient;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends Activity {

	public Context mContext;
	private String mErrorText = null;
	private ProgressDialog mProgressDialog;
	public SharedPreferences mSettings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.signup);

		mContext = this;

		TextView terms = (TextView) findViewById(R.id.signup_terms);
		Spanned termsText = Html.fromHtml(getString(R.string.terms_agree));
		terms.setText(termsText);

		mSettings = getSharedPreferences(MainActivity.PREFS_NAME, 0);

		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage(getString(R.string.creating_account));
		mProgressDialog.setCancelable(false);

		initButtons();
	}

	public void initButtons() {
		// Signup
		Button signupButton = (Button) findViewById(R.id.button_signup);
		signupButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mProgressDialog.show();
				new AsyncSignup().execute();
			}
		});
	}

	public boolean doUserSignup() {

		EditText emailView = (EditText) findViewById(R.id.signup_email);
		EditText screennameView = (EditText) findViewById(R.id.signup_username);
		EditText passwordView = (EditText) findViewById(R.id.signup_password);
		EditText passwordAgainView = (EditText) findViewById(R.id.signup_password_confirm);
		CheckBox agreedTerms = (CheckBox) findViewById(R.id.signup_terms);

		// required fields
		if (emailView.getText().equals("") || screennameView.getText().equals("") || passwordView.getText().equals("") || passwordAgainView.getText().equals("")) {
			mErrorText = getString(R.string.fill_all_fields);
			return false;
		} else if (!passwordAgainView.getText().toString().equals(passwordView.getText().toString())) {
			// these have to match though
			mErrorText = getString(R.string.passwords_must_match);
			return false;
		} else if (!agreedTerms.isChecked()) {
			// gotta be checked
			mErrorText = getString(R.string.must_agree_terms);
			return false;
		}

		try {

			if (!isOnline()) {
				throw new Exception("Network Timeout");
			}
			URL apiUrl = new URL(MainActivity.API_PROTOCOL, MainActivity.API_HOST, MainActivity.API_PATH + "signup");
			Log.d("apiUrl", apiUrl.toString());

			HttpPost httpRequest = new HttpPost(apiUrl.toExternalForm());

			// attach data
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("username", screennameView.getText().toString()));
			nvps.add(new BasicNameValuePair("password", passwordView.getText().toString()));
			nvps.add(new BasicNameValuePair("email", emailView.getText().toString()));
			httpRequest.setEntity(new UrlEncodedFormEntity(nvps));
			httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");

			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(httpRequest);

			// check for request-level errors
			if (response.getStatusLine().getStatusCode() != 201) {
				Log.d("Signup", response.getStatusLine().getStatusCode() + "");
				Log.d("Signup", response.getStatusLine().getReasonPhrase());
				JSONObject errorJSON = JSONFactory.getJSONObject(response);
				mErrorText = errorJSON.toString();
				throw new BadSignupException();
			}
			
			// check for json-level errors
			JSONObject loginJSON = JSONFactory.getJSONObject(response);
			String loginError = loginJSON.optString("error_code");
			if (!loginError.isEmpty()) {
				Log.d("Login error", loginError);
				mErrorText = loginError;
				return false;
			} else {
				// none? we're good then
				String token = loginJSON.getString("token");
				Log.d("Signup token", token);
				Editor editableSettings = mSettings.edit();
				editableSettings.putString(MainActivity.LOGIN_TOKEN, token);
				editableSettings.commit();
				return true;
			}

		} catch (UnknownHostException e) {
			mErrorText = getString(R.string.trouble_connecting_to_server);
			e.printStackTrace();
			return false;
		} catch (BadSignupException e) {
			mErrorText = getString(R.string.bad_signup_attempt);
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			mErrorText = e.toString();
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Do all the time intensive work here.
	 */
	private class AsyncSignup extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... args) {
			return doUserSignup();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mProgressDialog.dismiss();
			if (result) {
				startActivity(new Intent(mContext, MainActivity.class));
				Intent intent = getIntent();
				setResult(RESULT_OK, intent);
				finish();
			} else {
				Toast toast;
				toast = Toast.makeText(mContext, mErrorText, Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}

	public boolean isOnline() {
		try {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		} catch (Exception e) {
			return false;
		}
	}

	class BadSignupException extends Exception {
		private static final long serialVersionUID = 1L;
	}
}
