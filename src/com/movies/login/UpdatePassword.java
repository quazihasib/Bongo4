package com.movies.login;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.movies.bongobd.R;
import com.movies.categoryPage.CategoryLanding;
import com.movies.startingPage.StartingPage;
import com.tab.AddMenu;
import com.tab.ShareData;
import com.tab.UnCaughtException;

public class UpdatePassword extends Activity
{
	public EditText oldpasset, newpasset, confirmpasset;
	public Button updateBtn;
	public RequestQueue requestQueue;
	public UpdatePassword updatePasswordInstance;
	public ImageView crossButtonUpdatePassword;
	public LinearLayout wholeUpdatePasswordScreenLayout;
	public LinearLayout updatePasswordHeader;
	
	public static String DEBUG_TAG = UpdatePassword.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_update_password);

		updatePasswordInstance = this;
		
		//Handling application crashing
		Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(UpdatePassword.this));

		wholeUpdatePasswordScreenLayout = (LinearLayout)findViewById(R.id.wholeUpdatePasswordScreenLayout);
		wholeUpdatePasswordScreenLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				ShareData.hideKeyboard(updatePasswordInstance);
			}
		});
		
		updatePasswordHeader = (LinearLayout)findViewById(R.id.updatePasswordHeader);
		LinearLayout.LayoutParams updatePasswordHeaderParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ShareData.headerHeight);
		updatePasswordHeader.setLayoutParams(updatePasswordHeaderParams);
		
		TextView tvTopUpdatePassword = (TextView)findViewById(R.id.llTopUpdatePassword);
		tvTopUpdatePassword.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)updatePasswordInstance.getResources().getDimension(R.dimen.text_size1));
		
		TextView tvOldPass = (TextView)findViewById(R.id.tvOldPass);
		tvOldPass.setTypeface(ShareData.RobotoFont(updatePasswordInstance));
		tvOldPass.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)updatePasswordInstance.getResources().getDimension(R.dimen.text_size1));
		
		TextView tvNewPass = (TextView)findViewById(R.id.tvNewPass);
		tvNewPass.setTypeface(ShareData.RobotoFont(updatePasswordInstance));
		tvNewPass.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)updatePasswordInstance.getResources().getDimension(R.dimen.text_size1));
		
		TextView tvConfirmPass = (TextView)findViewById(R.id.tvConfirmPass);
		tvConfirmPass.setTypeface(ShareData.RobotoFont(updatePasswordInstance));
		tvConfirmPass.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)updatePasswordInstance.getResources().getDimension(R.dimen.text_size1));

		Updatepassword();
		crossButtonUpdatePassword();
	}

	public void crossButtonUpdatePassword()
	{
		crossButtonUpdatePassword = (ImageView)findViewById(R.id.crossButtonUpdatePassword);
		crossButtonUpdatePassword.setPadding(-ShareData.padding10*1, ShareData.padding10*3, 0, ShareData.padding10*3);
		crossButtonUpdatePassword.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				finish();
				startActivity(new Intent(getBaseContext(), UserProfile.class));
				overridePendingTransition(R.anim.animation1, R.anim.animation2);
			}
		});
	}

	
	private void Updatepassword()
	{
		// TODO Auto-generated method stub
		oldpasset = (EditText) findViewById(R.id.oldpass);
		oldpasset.setTypeface(ShareData.RobotoFont(updatePasswordInstance));
		oldpasset.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)updatePasswordInstance.getResources().getDimension(R.dimen.text_size2));
		
		newpasset = (EditText) findViewById(R.id.newpass);
		newpasset.setTypeface(ShareData.RobotoFont(updatePasswordInstance));
		newpasset.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)updatePasswordInstance.getResources().getDimension(R.dimen.text_size2));
		
		confirmpasset = (EditText) findViewById(R.id.confirmnewpass);
		confirmpasset.setTypeface(ShareData.RobotoFont(updatePasswordInstance));
		confirmpasset.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)updatePasswordInstance.getResources().getDimension(R.dimen.text_size2));
		
		updateBtn = (Button) findViewById(R.id.updatepassbtn);
		updateBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)updatePasswordInstance.getResources().getDimension(R.dimen.text_size2));
		updateBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				updateBtn.setEnabled(false);
				updateBtn.setBackgroundColor(Color.rgb(208, 95, 95));
				
				Validation(oldpasset.getText().toString().trim(), newpasset.getText()
						.toString().trim(), confirmpasset.getText().toString().trim());
			}

		});
	}

	@SuppressLint("NewApi")
	private void Validation(String oldp, String newp, String cnewp) 
	{
		// TODO Auto-generated method stub
		if (oldp.length()==0 || newp.length()==0 || cnewp.length()==0) 
		{
			updateBtn.setEnabled(true);
			updateBtn.setBackgroundColor(Color.rgb(208, 0, 0));
			
			Toast.makeText(UpdatePassword.this,"Please provide all informations", Toast.LENGTH_SHORT).show();
		} 
		else if(!(oldp.length()==0) && !(newp.length()==0) && !(cnewp.length()==0))
		{
			if(!newp.equals(cnewp)) 
			{
				updateBtn.setEnabled(true);
				updateBtn.setBackgroundColor(Color.rgb(208, 0, 0));
				
				Toast.makeText(UpdatePassword.this,"New password and confirm password did not matched!",Toast.LENGTH_SHORT).show();
			} 
			else
			{
				SubmitToService(oldp, newp);
			}
		}
	}

	private void SubmitToService(String oldp, final String newp)
	{
		// TODO Auto-generated method stub
		// encrypt password
		String encoldpass = "";
		String encnewpass = "";

		Log.d(DEBUG_TAG, "Old Password:"+oldp);
		byte[] data, data2;
		try 
		{
			data = oldp.getBytes("UTF-8");
			encoldpass = Base64.encodeToString(data, Base64.DEFAULT);

			data2 = newp.getBytes("UTF-8");
			encnewpass = Base64.encodeToString(data2, Base64.DEFAULT);

			// Log.d(DEBUG_TAG, "base64;"+base64);
		}
		catch (UnsupportedEncodingException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String url = "http://bongobd.com/api/change_pass.php?user_id="
				+ ShareData.getSavedString(updatePasswordInstance, "id").trim() + "&old_pass=" + encoldpass.trim() + "&new_pass="
				+ encnewpass.trim() + "&secret=" + ShareData.getSavedString(updatePasswordInstance, "secret").trim();

		Log.d(DEBUG_TAG, "url:"+url);
		requestQueue = Volley.newRequestQueue(this);

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, url,
				null, new Response.Listener<JSONObject>() 
				{
					@SuppressLint("NewApi")
					@Override
					public void onResponse(JSONObject response)
					{
						try 
						{
							Log.d(DEBUG_TAG, "Update Password Responce:"+response);
							String jsError = response.getString("error");
							Log.d(DEBUG_TAG, "jsError:"+jsError);
							
							if(jsError.length()==0)
							{
								String message = response.getString("message");
								Log.d(DEBUG_TAG, "message:"+message);
								
								updateBtn.setEnabled(true);
								updateBtn.setBackgroundColor(Color.rgb(208, 0, 0));
								
								Log.d(DEBUG_TAG, "new pass:"+newp);
								ShareData.saveSting(updatePasswordInstance, "password", newp);

								Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
								
								nextStep();	
							} 
							else 
							{
								updateBtn.setEnabled(true);
								updateBtn.setBackgroundColor(Color.rgb(208, 0, 0));
								
								//Toast.makeText(getApplicationContext(),"Error: " + jsError, Toast.LENGTH_LONG).show();
								Toast.makeText(getApplicationContext(),"The user is not logged in.", Toast.LENGTH_LONG).show();
							}

						} 
						catch(JSONException e) 
						{
							updateBtn.setEnabled(true);
							updateBtn.setBackgroundColor(Color.rgb(208, 0, 0));
							
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									"Error: " + e.getMessage(),
									Toast.LENGTH_LONG).show();
						}
					}
				}, new Response.ErrorListener() 
				{

					@Override
					public void onErrorResponse(VolleyError error) 
					{
						updateBtn.setEnabled(true);
						updateBtn.setBackgroundColor(Color.rgb(208, 0, 0));
						
						Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
					}
				});

		// Adding request to request queue
		requestQueue.add(jsonObjReq);
	}
	
	
	public void nextStep()
	{
		finish();
		startActivity(new Intent(getBaseContext(), UserProfile.class));
		overridePendingTransition( R.anim.animation1, R.anim.animation2 );
	}
	
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		UpdatePassword.this.finish();
		
		startActivity(new Intent(getBaseContext(), UserProfile.class));
		overridePendingTransition( R.anim.animation1, R.anim.animation2 );
	}
}
