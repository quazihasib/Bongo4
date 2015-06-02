package com.movies.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.movies.actorProfile.ActorProfile;
import com.movies.bongobd.R;
import com.movies.browseAll.Movies;
import com.movies.categoryPage.CategoryLanding;
import com.movies.facebook.FacebookApp;
import com.movies.movieSummary.MovieSummary;
import com.movies.people.People;
import com.movies.singleMovie.SingleMoviePage;
import com.movies.subscribe.Subscribe;
import com.tab.AddMenu;
import com.tab.ShareData;
import com.tab.UnCaughtException;

public class LoginNew  extends Activity
{
	public Button btnLogin;
	public EditText userName, password;
	public ImageView ivLoginFb, ivLoginEmail, ivText, ivLogo;
	public TextView tvText, tvText1, tvSkip;
	public static LoginNew loginNewInstance;
	public ImageView crossButtonLogin, ivSignUpLoginNewLeft, ivSignUpLoginRight;
	public LinearLayout wholeLoginScreenLayout, loginHeader;
	
    public FacebookApp fb;
    
    public static String DEBUG_TAG = LoginNew.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.new_login);
		    
		loginNewInstance = this;
		
		//Handling application crashing
		Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(LoginNew.this));
		
		fb = new FacebookApp(loginNewInstance);
		
		//AddMenu.pageNumber = 8;
		
		ivText = (ImageView)findViewById(R.id.ivText);
		LinearLayout.LayoutParams ivTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ShareData.getScreenHeight(loginNewInstance)/12);
		ivTextParams.setMargins(ShareData.padding10*4, 0, ShareData.padding10*4, 0);
		ivText.setLayoutParams(ivTextParams);
		
		
		ivLogo = (ImageView)findViewById(R.id.ivLogo);
		LinearLayout.LayoutParams ivLogoParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ShareData.getScreenHeight(loginNewInstance)/4);
		ivLogoParams.setMargins(ShareData.padding10*2, 0, ShareData.padding10*2, 0);
		ivLogo.setLayoutParams(ivLogoParams);
//		ivLogo.setBackgroundColor(Color.BLACK);logo_login
		try 
		{
			ivLogo.setImageResource(R.drawable.bongobdlogo1);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ivLogo.setAdjustViewBounds(true);
		
//		tvText1 = (TextView)findViewById(R.id.tvText1);
//		tvText1.setTypeface(ShareData.RobotoFont(loginNewInstance));
//		tvText1.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)loginNewInstance.getResources().getDimension(R.dimen.text_size2));
//		tvText1.setText("Don't have an account?");
//		LinearLayout.LayoutParams tvText1Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ShareData.getScreenHeight(loginNewInstance)/12);
//		tvText1Params.setMargins(ShareData.padding10*4, ShareData.padding10*4, 0, 0);
//		tvText1.setLayoutParams(tvText1Params);
		
		
		addSkipButton();
		addFacebookLogin();
		addLoginEmail();
		addSignUpFree();
		
	}
	
	public void addSignUpFree() 
	{
		ivSignUpLoginNewLeft = (ImageView)findViewById(R.id.ivSignUpLoginLeft);
		LinearLayout.LayoutParams ivSignUpLoginNewLeftParams = new LinearLayout.LayoutParams(0, ShareData.getScreenHeight(loginNewInstance)/12 , 2);
		ivSignUpLoginNewLeftParams.setMargins(ShareData.padding10*5, 0, 0, 0);
		ivSignUpLoginNewLeftParams.gravity = Gravity.LEFT;
		ivSignUpLoginNewLeft.setLayoutParams(ivSignUpLoginNewLeftParams);
		ivSignUpLoginNewLeft.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//Toast.makeText(getBaseContext(), "tvSignUpLoginNew", Toast.LENGTH_SHORT).show();
//				finish();
//				startActivity(new Intent(getBaseContext(), SignUp.class));
//				overridePendingTransition(R.anim.animation1, R.anim.animation2);
			}
		});		
		
		ivSignUpLoginRight = (ImageView)findViewById(R.id.ivSignUpLoginRight);
		LinearLayout.LayoutParams ivSignUpLoginRightParams = new LinearLayout.LayoutParams(0, ShareData.getScreenHeight(loginNewInstance)/12, 1);
		ivSignUpLoginRightParams.setMargins(ShareData.padding10, 0, ShareData.padding10*5, 0);
		ivSignUpLoginNewLeftParams.gravity = Gravity.RIGHT;
		ivSignUpLoginRight.setLayoutParams(ivSignUpLoginRightParams);
		ivSignUpLoginRight.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//Toast.makeText(getBaseContext(), "tvSignUpLoginNew", Toast.LENGTH_SHORT).show();
				finish();
				startActivity(new Intent(getBaseContext(), LoginActivity.class));
				overridePendingTransition(R.anim.animation1, R.anim.animation2);
			}
		});		
	}

	public void addLoginEmail() 
	{
		ivLoginEmail = (ImageView)findViewById(R.id.ivLoginEmail);
		LinearLayout.LayoutParams ivLoginEmailParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ShareData.getScreenHeight(loginNewInstance)/12);
		ivLoginEmailParams.setMargins(ShareData.padding10*3, 0, ShareData.padding10*3, 0);
		ivLoginEmail.setLayoutParams(ivLoginEmailParams);
		ivLoginEmail.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//Toast.makeText(getBaseContext(), "Email", Toast.LENGTH_SHORT).show();
				finish();
				startActivity(new Intent(getBaseContext(), SignUp.class));
				overridePendingTransition(R.anim.animation1, R.anim.animation2);
			}
		});
	}

	public void addFacebookLogin()
	{
		ivLoginFb = (ImageView)findViewById(R.id.ivLoginFb);
		LinearLayout.LayoutParams ivLoginFbParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ShareData.getScreenHeight(loginNewInstance)/12);
		ivLoginFbParams.setMargins(ShareData.padding10*3, 0, ShareData.padding10*3, ShareData.padding10*3);
		ivLoginFb.setLayoutParams(ivLoginFbParams);
		ivLoginFb.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//Toast.makeText(getBaseContext(), "FB", Toast.LENGTH_SHORT).show();
				Log.d("LoginActivity", "FB get pro");
				fb.loginToFacebook();
			}
		});		
	}

	public void addSkipButton()
	{
		tvSkip = (TextView)findViewById(R.id.tvSkip);
		tvSkip.setTypeface(ShareData.RobotoFont(loginNewInstance));
		tvSkip.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)loginNewInstance.getResources().getDimension(R.dimen.text_size2));
		tvSkip.setText("Skip");
		tvSkip.setTypeface(null, Typeface.BOLD);
		LinearLayout.LayoutParams tvSkipNewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ShareData.getScreenHeight(loginNewInstance)/12);
		tvSkipNewParams.setMargins(0, ShareData.padding10*2, ShareData.padding10*2, 0);
		tvSkipNewParams.gravity = Gravity.RIGHT;
		tvSkip.setLayoutParams(tvSkipNewParams);
		tvSkip.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				finish();
				Log.d(DEBUG_TAG, "AddMenu.pageNumber:"+AddMenu.pageNumber);
				if(AddMenu.pageNumber == 1)
				{
					startActivity(new Intent(getBaseContext(), CategoryLanding.class));
				}
				else if(AddMenu.pageNumber == 2)
				{
					startActivity(new Intent(getBaseContext(), People.class));
				}
				else if(AddMenu.pageNumber == 3)
				{
					startActivity(new Intent(getBaseContext(), Subscribe.class));
				}
				else if(AddMenu.pageNumber == 4)
				{
					startActivity(new Intent(getBaseContext(), SingleMoviePage.class));
				}
				else if(AddMenu.pageNumber == 5)
				{
					startActivity(new Intent(getBaseContext(), Movies.class));
				}
				else if(AddMenu.pageNumber == 6)
				{
					startActivity(new Intent(getBaseContext(), ActorProfile.class));
				}
				else if(AddMenu.pageNumber == 7)
				{
					startActivity(new Intent(getBaseContext(), MovieSummary.class));
				}
				overridePendingTransition(R.anim.animation1, R.anim.animation2);
			}
		});
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		fb.facebook.authorizeCallback(requestCode, resultCode, data);
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		
		finish();
		if(AddMenu.pageNumber == 1)
		{
			startActivity(new Intent(getBaseContext(), CategoryLanding.class));
		}
		else if(AddMenu.pageNumber == 2)
		{
			startActivity(new Intent(getBaseContext(), People.class));
		}
		else if(AddMenu.pageNumber == 3)
		{
			startActivity(new Intent(getBaseContext(), Subscribe.class));
		}
		else if(AddMenu.pageNumber == 4)
		{
			startActivity(new Intent(getBaseContext(), SingleMoviePage.class));
		}
		else if(AddMenu.pageNumber == 5)
		{
			startActivity(new Intent(getBaseContext(), Movies.class));
		}
		else if(AddMenu.pageNumber == 6)
		{
			startActivity(new Intent(getBaseContext(), ActorProfile.class));
		}
		else if(AddMenu.pageNumber == 7)
		{
			startActivity(new Intent(getBaseContext(), MovieSummary.class));
		}
		overridePendingTransition( R.anim.animation1, R.anim.animation2 );
	}
}