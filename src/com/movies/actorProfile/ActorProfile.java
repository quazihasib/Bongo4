package com.movies.actorProfile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html.ImageGetter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.movies.bongobd.ImageLoader;
import com.movies.bongobd.LeadingMarginSpan2;
import com.movies.bongobd.R;
import com.movies.browseAll.Movies;
import com.movies.categoryPage.CategoryLanding;
import com.movies.facebook.FacebookApp;
import com.movies.movieSummary.MovieSummary;
import com.movies.people.People;
import com.movies.startingPage.StartingPage;
import com.tab.AddMenu;
import com.tab.Header;
import com.tab.ShareData;
import com.tab.UnCaughtException;

public class ActorProfile extends Activity implements ImageGetter 
{
	public ProgressDialog pDialog;
	public RequestQueue requestQueue;

	public String castName;
	public String actorBio, actorImage;
	
	public TextView tvActorBio, tvCastName, tvCastRole, tvFeaturesIn, tvFilmography;
	public TextView hide, show;

	public ImageView ivActorImage, ivShareArtist;
	public ViewGroup.LayoutParams ivLayoutParams;
	
	public int screenheight, screenWidth;
	
	public static int castRoleCounter;
	public static String[] castRoles;
	
	public static ImageLoader actorImageLoader;
	public static ActorProfile actorProfileInstance;
	public static String castId;
	public static String DEBUG_TAG = ActorProfile.class.getSimpleName();
	
	public Header h;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.actor_profile_page);

		actorProfileInstance = this;
		h = AddMenu.HeaderFunction(actorProfileInstance);
		AddMenu.pageNumber = 6;
		StartingPage.singleMoviePageReturn = 3;
		
		//Handling application crashing
		Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(ActorProfile.this));
				
		
		screenheight = ShareData.getScreenHeight(actorProfileInstance);
		screenWidth = ShareData.getScreenWidth(actorProfileInstance);
		
		try
		{
			if(CategoryLanding.catagoryName!=null)
			{
				if(CategoryLanding.catagoryName.trim().equals("movies"))
				{
					h.tv1.setBackgroundColor(Color.parseColor("#B40404"));
					h.tv2.setBackgroundColor(Color.parseColor("#FF0000"));
					h.tv3.setBackgroundColor(Color.parseColor("#FF0000"));
					h.tv4.setBackgroundColor(Color.parseColor("#FF0000"));
				}
				else if(CategoryLanding.catagoryName.trim().equals("tv"))
				{
					h.tv1.setBackgroundColor(Color.parseColor("#FF0000"));
					h.tv2.setBackgroundColor(Color.parseColor("#B40404"));
					h.tv3.setBackgroundColor(Color.parseColor("#FF0000"));
					h.tv4.setBackgroundColor(Color.parseColor("#FF0000"));
				}
				else if(CategoryLanding.catagoryName.trim().equals("music"))
				{
					h.tv1.setBackgroundColor(Color.parseColor("#FF0000"));
					h.tv2.setBackgroundColor(Color.parseColor("#FF0000"));
					h.tv3.setBackgroundColor(Color.parseColor("#B40404"));
					h.tv4.setBackgroundColor(Color.parseColor("#FF0000"));
				}
			} 
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Intent i = getIntent();
//		castId = i.getStringExtra("artistId");
//		Log.d(DEBUG_TAG, "castId:" + castId); 
//		castName = i.getStringExtra("artistName");
//		Log.d(DEBUG_TAG, "castName:" + castName);
//		castRole = i.getStringExtra("artistRole");
		
		Log.d(DEBUG_TAG, "castId from Actor page:"+StartingPage.ACTOR_ID);
		Log.d(DEBUG_TAG, "castId from Movies:"+MovieSummary.singleArtistId);
//		if(StartingPage.peoplePageEnable == true)
//		{
//			castId = StartingPage.ACTOR_ID;
//			Log.d(DEBUG_TAG, "Movies.ACTOR_ID:"+StartingPage.ACTOR_ID);
//		}
//		else if(StartingPage.peoplePageEnable == false)
//		{
//			castId = MovieSummary.singleArtistId;
//		}
		
		if(StartingPage.browseAll == 2)
		{
			castId = StartingPage.ACTOR_ID;
			Log.d(DEBUG_TAG, "Movies.ACTOR_ID:"+StartingPage.ACTOR_ID);
		}
		else if(StartingPage.browseAll == 1 || StartingPage.browseAll == 3)
		{
			castId = MovieSummary.singleArtistId;
		}
		
		tvFilmography = (TextView) findViewById(R.id.tvFilmography);
//		tvFilmography.setTypeface(ShareData.RobotoFont(actorProfileInstance));
		tvFilmography.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)actorProfileInstance.getResources().getDimension(R.dimen.text_size1));
		
		tvFeaturesIn  = (TextView) findViewById(R.id.tvFeaturesIn);
//		tvFeaturesIn.setTypeface(ShareData.RobotoFont(actorProfileInstance));
		tvFeaturesIn.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)actorProfileInstance.getResources().getDimension(R.dimen.text_size1));
		
		ivActorImage = (ImageView) findViewById(R.id.icon);
		
		tvActorBio = (TextView) findViewById(R.id.message_view);
		tvActorBio.setTypeface(ShareData.RobotoFont(actorProfileInstance));
		tvActorBio.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)actorProfileInstance.getResources().getDimension(R.dimen.text_size2));
		
		tvCastName = (TextView) findViewById(R.id.tvCastName);
//		tvCastName.setTypeface(ShareData.RobotoFont(actorProfileInstance));
		tvCastName.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)actorProfileInstance.getResources().getDimension(R.dimen.text_size1));
		tvCastName.setTextColor(Color.RED);

		tvCastRole = (TextView) findViewById(R.id.tvCastRole);
//		tvCastRole.setTypeface(ShareData.RobotoFont(actorProfileInstance));
		tvCastRole.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)actorProfileInstance.getResources().getDimension(R.dimen.text_size2));
		tvCastRole.setTextColor(Color.BLACK);

		actorImageLoader = new ImageLoader(this);
		ivLayoutParams = ivActorImage.getLayoutParams();

		ivLayoutParams.width = screenWidth / 3-(ShareData.padding10*3);
		ivLayoutParams.height = screenWidth / 3+(ShareData.padding10*4);
		Log.d(DEBUG_TAG, "vc.width:" + ivLayoutParams.width);

		makeJsonObjectRequest("http://bongobd.com/api/artist.php?artist_id="+castId);
		Log.d(DEBUG_TAG, "http://bongobd.com/api/artist.php?artist_id="+castId);

		show = (TextView) findViewById(R.id.show);
		show.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)actorProfileInstance.getResources().getDimension(R.dimen.text_size2));
		show.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// System.out.println("Show button");
				show.setVisibility(View.INVISIBLE);
				hide.setVisibility(View.VISIBLE);
				tvActorBio.setMaxLines(Integer.MAX_VALUE);

			}
		});
		hide = (TextView) findViewById(R.id.hide);
		hide.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)actorProfileInstance.getResources().getDimension(R.dimen.text_size2));
		hide.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				System.out.println("Hide button");
				hide.setVisibility(View.INVISIBLE);
				show.setVisibility(View.VISIBLE);
				tvActorBio.setMaxLines(4);

			}
		});
		
		
		ivShareArtist = (ImageView)findViewById(R.id.ivShareArtist);
		RelativeLayout.LayoutParams ivShareArtistParams = new RelativeLayout.LayoutParams(ShareData.getScreenWidth(actorProfileInstance)/8, ShareData.getScreenWidth(actorProfileInstance)/22);
		ivShareArtistParams.setMargins(ShareData.padding10*2, 0, 0, 0);
		ivShareArtist.setLayoutParams(ivShareArtistParams);
		ivShareArtistParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		ivShareArtistParams.addRule(RelativeLayout.RIGHT_OF, R.id.icon);
		ivShareArtist.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				postToWall();
			}
		});
		
		int y = 5/0;
	}

	public void makeJsonObjectRequest(String urlJsonObj) 
	{
		castRoleCounter=-1;
		
		requestQueue = Volley.newRequestQueue(StartingPage.startInstance);
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				urlJsonObj, null, new Response.Listener<JSONObject>() 
				{
					@Override
					public void onResponse(JSONObject response)
					{
						Log.d(DEBUG_TAG, response.toString());

						try
						{
							JSONObject data = response.getJSONObject("data");
							Log.d(DEBUG_TAG, "data: " + data);

							actorBio = data.getString("bio");
							Log.d(DEBUG_TAG, "actorBiosss:" + actorBio);
							
							castName = data.getString("name");
							Log.d(DEBUG_TAG, "castName:" + castName);
							
							JSONObject j = data.getJSONObject("roles");
							Iterator<String> iter = j.keys();
							
							castRoles = new String[j.length()];
							Log.d("DEBUG_TAG", "j length: "+ j.length());
							
							while (iter.hasNext()) 
							{ 
								String key = iter.next();
								try 
								{
									Object value = j.get(key);
									//Log.d("DEBUG_TAG", "value:" + value);  
									JSONObject eachObject = j.getJSONObject(""+ key);

									castRoleCounter++;
										
									castRoles[castRoleCounter] = eachObject.getString("role_name");
									Log.d("DEBUG_TAG", "castRoles: "+ castRoles[castRoleCounter]);
								} 
								catch (JSONException e) 
								{
									// Something went wrong!
								}
							}
							
							actorImage = "http://bongobd.com/wp-content/themes/bongobd/images/artistimage/profile/"
									+ data.getString("profile_image");
							Log.d(DEBUG_TAG, "movieImage:" + actorImage);

						} 
						catch (JSONException e) 
						{
//							e.printStackTrace();
//							Toast.makeText(getApplicationContext(),
//									"Error: " + e.getMessage(),
//									Toast.LENGTH_LONG).show();
							Toast.makeText(getApplicationContext(),
									"No data.Try again.", Toast.LENGTH_SHORT).show();
						}
						hidepDialog();
					}
				},
				new Response.ErrorListener() 
				{
					@Override
					public void onErrorResponse(VolleyError error) 
					{
						VolleyLog.d(DEBUG_TAG, "Error: " + error.getMessage());
//						Toast.makeText(getApplicationContext(),
//								error.getMessage(), Toast.LENGTH_SHORT).show();
						
						Toast.makeText(getApplicationContext(),
								"No data.Try again.", Toast.LENGTH_SHORT).show();
						// hide the progress dialog
						hidepDialog();
					}
				});

		// Adding request to request queue
		requestQueue.add(jsonObjReq);
	}

	public void setSeeMoreText()
	{
		int width = screenWidth/3;
		int height = ivActorImage.getHeight();

		//Log.d(DEBUG_TAG, "width:" + width);
		//Log.d(DEBUG_TAG, "height:" + height);

		int leftMargin = width - ShareData.padding10;
		if(actorBio!=null)
		{
			if(actorBio.trim().length()==0 )
			{
				show.setVisibility(View.INVISIBLE);
				hide.setVisibility(View.INVISIBLE);
			}
			else
			{
				try
				{
					// icon.setImageDrawable(getResources().getDrawable(R.drawable.download));
					float textLineHeight = tvActorBio.getPaint().getTextSize();
					int lines = (int) Math.round(height / textLineHeight);
					actorBio = actorBio.replaceAll("(?<!\\\\)\\\\(?!\\\\)", "");
					SpannableString ss = new SpannableString(actorBio.toString().trim());
					ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(new LeadingMarginSpan2(lines, leftMargin), 0, ss.length(), 0);
					tvActorBio.setText(ss);
					tvActorBio.setMaxLines(4);
					
					if(tvActorBio.getLineCount()<=4)
					{
						show.setVisibility(View.INVISIBLE);
						hide.setVisibility(View.INVISIBLE);
					}
				} 
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(actorBio==null)
		{
			show.setVisibility(View.INVISIBLE);
			hide.setVisibility(View.INVISIBLE);
		}
			
	}

//	public void showpDialog() {
//		if (!pDialog.isShowing()) {
//			pDialog.show();
//		}
//	}

	public void hidepDialog()
	{
//		if (pDialog.isShowing()) {
//			pDialog.dismiss();
		
			setSeeMoreText();
			
			try
			{
				if(castRoles!=null)
				{
					StringBuilder builder = new StringBuilder();
					for (String s: castRoles) 
					{
					    builder.append(s);
					    builder.append(" | ");
					}
					
					String totalRoles = builder.substring(0, (builder.length()-2));
					// tvViews.setText(movieViews);
					actorImageLoader.DisplayImage(actorImage, ivActorImage);
					//Log.d(DEBUG_TAG, ":"+castName);
					//Log.d(DEBUG_TAG, "castRole:"+castRoles);
					tvCastName.setText(castName);
					tvCastRole.setText(totalRoles);
				}
			}
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			AddActorLayout.addLayout();
//		}
	}

	@Override
	public Drawable getDrawable(String source)
	{
		LevelListDrawable d = new LevelListDrawable();
		Drawable empty = getResources().getDrawable(R.drawable.ic);
		d.addLevel(0, 0, empty);
		d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
		new LoadImage().execute(source, d);

		return d;
	}

	class LoadImage extends AsyncTask<Object, Void, Bitmap> 
	{
		private LevelListDrawable mDrawable;
		@Override
		protected Bitmap doInBackground(Object... params)
		{
			String source = (String) params[0];
			mDrawable = (LevelListDrawable) params[1];
			Log.d("", "doInBackground " + source);
			try 
			{
				InputStream is = new URL(source).openStream();
				return BitmapFactory.decodeStream(is);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			} 
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap)
		{
			Log.d("", "onPostExecute drawable " + mDrawable);
			Log.d("", "onPostExecute bitmap " + bitmap);
			if(bitmap != null) 
			{
				BitmapDrawable d = new BitmapDrawable(bitmap);
				mDrawable.addLevel(1, 1, d);
				mDrawable.setBounds(0, 0, ShareData.padding10*2,  ShareData.padding10*5);
				mDrawable.setLevel(1);
				// d.setGravity(Gravity.TOP);
				// i don't know yet a better way to refresh TextView
				// mTv.invalidate() doesn't work as expected
				CharSequence t = tvActorBio.getText();
				tvActorBio.setText(t);
			}
		}
	}
	
	public void onStop() 
	{
		super.onStop();
		
		Log.d(DEBUG_TAG, "App Stopped");
		//CatagoryCarasol
		if(requestQueue!=null)
		{
			requestQueue.cancelAll(new RequestQueue.RequestFilter() 
			{
				@Override
				public boolean apply(Request<?> request)
				{
					// TODO Auto-generated method stub
					return true;
				}
		    });
		}
	}	
	
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		
		//ListViewAdapter.singleMovieId = "";
		//SingleMoviePage.jsonData = null;
		ActorProfile.this.finish();
		if(StartingPage.actorPageReturn  == 1)
		{
			 startActivity(new Intent(getBaseContext(), MovieSummary.class));
			 overridePendingTransition( R.anim.animation1, R.anim.animation2 );
		} 
		else if(StartingPage.actorPageReturn  == 2)
		{
			 startActivity(new Intent(getBaseContext(), Movies.class));
			 overridePendingTransition( R.anim.animation1, R.anim.animation2 );
		} 
		else if(StartingPage.actorPageReturn  == 3)
		{
			 startActivity(new Intent(getBaseContext(), People.class));
			 overridePendingTransition( R.anim.animation1, R.anim.animation2 );
		} 
	}
	
	
	 //Function to post to facebook wall
  	@SuppressWarnings("deprecation")
  	public void postToWall()
  	{
  		
  		 Bundle parameters = new Bundle();
  		    parameters.putString("name", "BongoBD app");
  		    parameters.putString("caption", "BongoBD caption.");
  		    parameters.putString("description", "BongoBD description.");
  		    parameters.putString("link", "https://play.google.com/store/apps/details?id=com.ck.batterymonitor");
  		    //parameters.putString("picture", "link to the picture");
  		    parameters.putString("display", "page");
  		    
 		// post on user's wall.
  		FacebookApp.facebook.dialog(this, "feed", parameters, new DialogListener()
  		{
  			@Override
  			public void onFacebookError(FacebookError e)
  			{
  				
  			}
  			@Override
  			public void onError(DialogError e) 
  			{
  				
  			}
  			@Override
  			public void onComplete(Bundle values)
  			{
  				
  			}
  			@Override
  			public void onCancel() 
  			{
  				
  			}
  		});
  	}
}
