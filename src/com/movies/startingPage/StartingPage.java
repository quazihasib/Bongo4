package com.movies.startingPage;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.movies.bongobd.R;
import com.movies.browseAll.ListViewAdapter;
import com.movies.categoryPage.CategoryLanding;
import com.movies.people.People;
import com.movies.singleMovie.RelatedContents;
import com.movies.singleMovie.SingleMoviePage;
import com.tab.ShareData;
import com.tab.UnCaughtException;

public class StartingPage extends Activity
{
	public ProgressDialog pDialog;
	public RequestQueue requestQueue;
	
	public static int value, movieCounter, loader;
	public static int padding , padding1;
	public static int bannerHeight = 0;
	public static int SingleMoviePageId = 0;
	public static int browseAll=0;
	
	public static int singleMoviePageReturn = 0;
	public static int browseAllPageReturn = 0;
	public static int movieSummaryPageReturn = 0;
	public static int actorPageReturn = 0;
	
	public static String ACTOR_ID = "";
	public static String rateType="";
	public static boolean peoplePageEnables, searchPageEnable;
	
	public LinearLayout llFullMovies, llMusicVideos, llPeople, llTvShows;
	public ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8; 
	
	public static StartingPage startInstance;
	public static String DEBUG_TAG = StartingPage.class.getSimpleName();
	public static String packageName;
	public static ArrayList<String> backs = new ArrayList<String>();
	
	public Handler h ;
	public Runnable runnable;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		
		//Handling application crashing
		Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(StartingPage.this));
		
		if(RelatedContents.relatedContentClicked==true)
		{
			Handler h = new Handler();
			h.postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					finish();
					startActivity(new Intent(startInstance, SingleMoviePage.class));
					overridePendingTransition( R.anim.animation1, R.anim.animation2 );
				}
			}, 2000);
		}
		else
		{
			setContentView(R.layout.starting_page);
		
		//setTheme(android.R.style.Theme_NoTitleBar_Fullscreen); 
		//setTheme(R.style.DialogAnimation);
		
		//Add animation on start
		LinearLayout layout = (LinearLayout) findViewById(R.id.startingLayout);
		AlphaAnimation animation = new AlphaAnimation(0.0f , 1.0f ) ;
		animation.setFillAfter(true);
		animation.setDuration(1200);
		layout.startAnimation(animation);
		
		startInstance = this;
		searchPageEnable = false;
		loader = 0;
		browseAll=0;
		SingleMoviePageId = 0;
		
		singleMoviePageReturn = 0;
		browseAllPageReturn = 0;
		movieSummaryPageReturn = 0;
		actorPageReturn = 0;
		rateType = "";
		
		Object o = startInstance;
        Package p = o.getClass().getPackage();
     
		String packages = p+""; 
		packages =  packages.replace("package", "").trim()+"."+StartingPage.class.getSimpleName();
		Log.d(DEBUG_TAG, packages);
		packageName = packages;
		backs.add(packageName);
		//Backs();
		
		ShareData.padding10 = ShareData.getScreenHeight(startInstance)/100;
		int width = ShareData.getScreenWidth(startInstance)/3;
		ShareData.headerHeight = ShareData.getScreenHeight(startInstance)/12;
		
		//singleMoviePageReturn=1 categoryLanding
		//singleMoviePageReturn=2 browseAll
		//PageReturn=3 SingleMoviePage
		//PageReturn=4 ActorProfile
		
		bannerHeight =  ShareData.getScreenWidth(startInstance)/2;
		
		padding = ShareData.getScreenWidth(startInstance)/17;
		padding1 = ShareData.getScreenWidth(startInstance)/26;

		ListViewAdapter.singleMovieId = "";
		SingleMoviePage.jsonData = null;
//		movieCounter=0;
		ACTOR_ID = null;
		
		iv1 = (ImageView)findViewById(R.id.ivimage_1);
		iv1.setPadding(padding, padding, padding, padding);
		
		iv2 = (ImageView)findViewById(R.id.ivimage_2);
		iv2.setPadding(padding, padding, padding, padding);
		
		iv3 = (ImageView)findViewById(R.id.ivimage_3);
		iv3.setPadding(padding, padding, padding, padding);
		
		iv4 = (ImageView)findViewById(R.id.ivimage_4);
		iv4.setPadding(padding, padding, padding, padding);
		
		iv5 = (ImageView)findViewById(R.id.ivtext_1);
		iv5.setPadding(padding+padding1, padding+padding1, padding+padding1, padding+padding1);
		
		iv6 = (ImageView)findViewById(R.id.ivtext_2);
		iv6.setPadding(padding+padding1, padding+padding1, padding+padding1, padding+padding1);
		
		iv7 = (ImageView)findViewById(R.id.ivtext_3);
		iv7.setPadding(padding+padding1, padding+padding1, padding+padding1, padding+padding1);
		
		iv8 = (ImageView)findViewById(R.id.ivtext_4);
		iv8.setPadding(padding+padding1, padding+padding1+ShareData.padding10*2, padding+padding1, padding+padding1+ShareData.padding10*2);
		
//		if(!(ShareData.getSavedString(this, "username").length()==0) && 
//				!(ShareData.getSavedString(this, "password").length()==0))
//		{
//			AutoLogin(startInstance, ShareData.getSavedString(this, "username"), ShareData.getSavedString(this, "password"));
//		}
		
		llFullMovies = (LinearLayout)findViewById(R.id.llFullMovies);
		llFullMovies.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				CategoryLanding.catagoryName = "movies";
				finish();
				startActivity(new Intent(getBaseContext(), CategoryLanding.class));
				overridePendingTransition( R.anim.animation1, R.anim.animation2 );
			}
		});
		
		llTvShows = (LinearLayout)findViewById(R.id.llTvShows);
		llTvShows.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				CategoryLanding.catagoryName = "tv";
				finish();
				startActivity(new Intent(getBaseContext(), CategoryLanding.class));
				overridePendingTransition( R.anim.animation1, R.anim.animation2 );
			}
		});
		
		llMusicVideos = (LinearLayout)findViewById(R.id.llMusicVideos);
		llMusicVideos.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				CategoryLanding.catagoryName = "music";
				finish();
				startActivity(new Intent(getBaseContext(), CategoryLanding.class));
				overridePendingTransition( R.anim.animation1, R.anim.animation2 );
			}
		});
		
//		myLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            @Override
//            public void onGlobalLayout() {
//                // TODO Auto-generated method stub
//                Rect r = new Rect();
//                parent.getWindowVisibleDisplayFrame(r);
//
//                int screenHeight = parent.getRootView().getHeight();
//                int heightDifference = screenHeight - (r.bottom - r.top);
//                Log.d("Keyboard Size", "Size: " + heightDifference);
//
//                //boolean visible = heightDiff > screenHeight / 3;
//            }
//        });
		
		
		llPeople = (LinearLayout)findViewById(R.id.llPeople);
		llPeople.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				finish();
				startActivity(new Intent(getBaseContext(), People.class));
				overridePendingTransition( R.anim.animation1, R.anim.animation2 );
			}
		});
//		showHashKey(this);
//		printKeyHash(startInstance);
		}
		
		//getWifiSpeed();
	}
	
	public void getWifiSpeed()
	{
		WifiManager mainWifiObj;
		mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		final WifiInfo wifiInfo = mainWifiObj.getConnectionInfo();
		
		
		h = new Handler();
//		h.postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				int speedMbps = wifiInfo.getLinkSpeed();
//				Log.d(DEBUG_TAG, "speed:"+speedMbps);
//			}
//		}, 1000);
		
		runnable = new Runnable()
		{
			@Override
			public void run() 
			{
			    /* my set of codes for repeated work */
			    //foobar();
				int speedMbps = wifiInfo.getLinkSpeed();
				Log.d(DEBUG_TAG, "speed:"+speedMbps);
			    h.postDelayed(this, 1000);          
			    // reschedule the handler
			}
		};
		h.postDelayed(runnable, 1000);
	}
	
	public static void Backs() 
	{
		// TODO Auto-generated method stub
		
		//backs.add(packageName);
	}

	public static void showHashKey(Context context) 
	{
	    try 
	    {
	        PackageInfo info = context.getPackageManager().getPackageInfo(
	                "com.movies.bongobd", PackageManager.GET_SIGNATURES); //Your package name here
	        for(Signature signature : info.signatures) 
	        {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	         }
	    } 
	    catch (NameNotFoundException e)
	    {
	    } 
	    catch (NoSuchAlgorithmException e) 
	    {
	    }
	}
	
	public static String printKeyHash(Activity context)
	{
		PackageInfo packageInfo;
		String key = null;
		try
		{
			//getting application package name, as defined in manifest
			String packageName = context.getApplicationContext().getPackageName();

			//Retriving package info
			packageInfo = context.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_SIGNATURES);
				
			Log.e("Package Name=", context.getApplicationContext().getPackageName());
			
			for (Signature signature : packageInfo.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				key = new String(Base64.encode(md.digest(), 0));
			
				// String key = new String(Base64.encodeBytes(md.digest()));
				Log.e("Key Hashsss=", key);
			}
		} catch (NameNotFoundException e1) {
			Log.e("Name not found", e1.toString());
		}
		catch (NoSuchAlgorithmException e) {
			Log.e("No such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("Exception", e.toString());
		}

		return key;
	}
	
	public void AutoLogin(final Activity instance, final String username2, final String password2) 
	{
		// TODO Auto-generated method stub
		showpDialog();

		// encrypt password
		String encryptedPassword = "";
		byte[] data;
		try 
		{
			data = password2.getBytes("UTF-8");
			encryptedPassword = Base64.encodeToString(data, Base64.DEFAULT);
			// Log.d(DEBUG_TAG, "base64;"+base64);
		}
		catch (UnsupportedEncodingException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String url = "http://site.bongobd.com/api/login.php?email=" + username2
				+ "&password=" + encryptedPassword;

		requestQueue = Volley.newRequestQueue(this);
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST, url,
				null, new Response.Listener<JSONObject>()
				{
					@SuppressLint("NewApi")
					@Override
					public void onResponse(JSONObject response) 
					{
						try
						{
							String jsError = response.getString("error");
							if(jsError.length()==0) 
							{
								JSONObject js = response.getJSONObject("data");

								@SuppressWarnings("unchecked")
								Iterator<String> iter = js.keys();
								while (iter.hasNext()) 
								{
									String key = iter.next();
									try 
									{
										JSONObject object = js.getJSONObject(""+ key);

										Toast.makeText(getApplicationContext(),
												"Login success !",Toast.LENGTH_LONG).show();
										
										//save id & password
										ShareData.saveSting(instance, "username", username2);
										ShareData.saveSting(instance, "password", password2);
										ShareData.saveSting(instance, "name", object.getString("display_name"));
										ShareData.saveSting(instance, "pic", object.getString("userprofile"));
										ShareData.saveSting(instance, "secret", object.getString("secret"));
										ShareData.saveSting(instance, "id", object.getString("id"));
								
									} 
									catch (JSONException e) 
									{
										// Something went wrong!
										Toast.makeText(getApplicationContext(),
												"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
									}
								}

							}
							else 
							{
//								Toast.makeText(getApplicationContext(),
//										"Error: " + jsError, Toast.LENGTH_LONG)
//										.show();
							}

						}
						catch (JSONException e)
						{
							e.printStackTrace();
//							Toast.makeText(getApplicationContext(),
//									"Error: " + e.getMessage(),
//									Toast.LENGTH_LONG).show();

						}
						// hide the progress dialog
						hidepDialog();
					}
				}, new Response.ErrorListener() 
				{

					@Override
					public void onErrorResponse(VolleyError error) 
					{
//						Toast.makeText(getApplicationContext(),
//								error.getMessage(), Toast.LENGTH_SHORT).show();
						// hide the progress dialog
						hidepDialog();
					}
				});

		// Adding request to request queue
		requestQueue.add(jsonObjReq);
	}
	
//	@Override
//	public void onSaveInstanceState(Bundle savedInstanceState) {
//	  super.onSaveInstanceState(savedInstanceState);
//	  // Save UI state changes to the savedInstanceState.
//	  // This bundle will be passed to onCreate if the process is
//	  // killed and restarted.
//	  String packageName = startInstance.getPackageName(); 
//	  packageName =  packageName.trim()+"."+StartingPage.class.getSimpleName();
//	  Log.d(DEBUG_TAG, packageName);
//	  savedInstanceState.putString("class", packageName);
//	  // etc.
//	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		if(h!=null)
		{
			h.removeCallbacks(runnable);
		}
	}
	
	public void showpDialog()
	{
		if(pDialog==null)
		{
			pDialog = ProgressDialog.show(startInstance, "", "");
			pDialog.setContentView(R.layout.custom_progress);
			pDialog.show();
			pDialog.setCancelable(false);
		}
	}

	public void hidepDialog() 
	{
		if(pDialog.isShowing())
		{
			pDialog.dismiss();
		}
	}
	
	public void onBackPressed() 
	{
		super.onBackPressed();
		
		finish();
		overridePendingTransition(R.anim.animation1, R.anim.animation2);
	}
}