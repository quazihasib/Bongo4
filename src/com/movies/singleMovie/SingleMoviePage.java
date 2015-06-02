package com.movies.singleMovie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.kaltura.playersdk.PlayerViewController;
import com.kaltura.playersdk.events.OnToggleFullScreenListener;
import com.movies.actorProfile.ActorProfile;
import com.movies.bongobd.R;
import com.movies.browseAll.ListViewAdapter;
import com.movies.browseAll.Movies;
import com.movies.categoryPage.CategoryLanding;
import com.movies.movieSummary.MovieSummary;
import com.movies.startingPage.StartingPage;
import com.tab.AddMenu;
import com.tab.Header;
import com.tab.Search;
import com.tab.ShareData;
import com.tab.UnCaughtException;

public class SingleMoviePage extends Activity implements OnToggleFullScreenListener 
{
	public static String movieShortSummary;
	public static String movieCategory;
	public static String movieAvgRating;
	public static String jsonData;
	public static float movieRating;
	public static float userRateStatus;

	public static String movieReleaseDate;
	public String movieDuration;
	public static String movieGenre;
	public String movieContentDetails;
	public static String movieUrl;
	public String rate_status;
	public String position;
	public String formattedYear, formattedMonth, formattedDay;
	public String movieName;
	public String movieDirector;
	public String movieViews;
	public String movieImage;
	public String movieType;
	public static String movieSummary;
	public String detailsId, roleName, roleId, artistProfileImage, contentId, artistId, artistName;
	public String id;
	public static boolean episodeEnable;

	public static HTML5WebView webView;
	public static ViewGroup.LayoutParams vc, vc1;

	public TextView  tvMovieName, tvDirector, tvViews, tvViewsLabel, tvDirectorLabel,
			tvMovieCategory, tvPostedOn, tvShortSummary;
	public Button btnRate;

	public static int height, width, count;
	public static int relatedHeight;
	public int finalHeight, finalWidth;
	public int counter;
	
	public RequestQueue requestQueue;
	public ProgressDialog pDialog;

	public LinearLayout directorLayout, infoLayout;
	public JSONArray cast;
	public JSONObject data, additionalData, userData;
	
	public String[] relatedImgUrls;
	public String[] relatedTitles;
	public String[] relatedViews;
	public String[] relatedIds;
	public String[] relatedContentLength;
	public String[] relatedGenre;

	public static SingleMoviePage singleMovieInstance;
	public static String DEBUG_TAG = SingleMoviePage.class.getSimpleName();

	public static TextView tv;
	public static String userId;
	LinearLayout addTabs, tabContainer;
	TabResult tb;
	public Header h;
	public static LinearLayout singleMovieMainLayout, singleMovieDetails, layoutMain, relatedLayout;

	 
	public static com.tab.Header headerLayout;
	public ScrollView scrollLayout;
	public static RelativeLayout relatedWebView;
	public ImageView btnFullScreen;
	
	public String movieID;
	public String episodeCount;
	
	public HttpGetJson getJson;
	public static boolean SingleMovieActivityRunning;
	public Bundle savedInstanceStates;
	public static boolean singleMoviePageLoader;
	public static String SingleMovieWaitingText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		setContentView(R.layout.single_movie_page);

		Log.d(DEBUG_TAG, "SingleMoviePage Started");
		singleMovieInstance = this;
		
		//Handling application crashing
		Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(SingleMoviePage.this));
				
		savedInstanceStates = savedInstanceState;
		h = AddMenu.HeaderFunction(singleMovieInstance);
		jsonData = "";
		AddMenu.pageNumber = 4;
		movieID="";
		episodeEnable = false;
		SingleMovieActivityRunning = true;
		singleMoviePageLoader = false;
//		webView = null;
		SingleMovieWaitingText = "Please Wait...Loading..."; 
		
		//getJson = new HttpGetJson(singleMovieInstance);
		
		RelatedContents.addLayout=true;
		RelatedContents.seeMoreCounter=3;
		
		StartingPage.movieSummaryPageReturn = 1;
		addTabs = (LinearLayout)findViewById(R.id.tabs);
		tabContainer = (LinearLayout)findViewById(R.id.tabContainer);
		tabContainer.setVisibility(View.INVISIBLE);
		
		headerLayout = (com.tab.Header)findViewById(R.id.headers);
		singleMovieMainLayout = (LinearLayout)findViewById(R.id.singleMovieMainLayout);
		singleMovieDetails = (LinearLayout)findViewById(R.id.singleMovieDetails);
		layoutMain = (LinearLayout)findViewById(R.id.layoutMain);
		relatedLayout = (LinearLayout)findViewById(R.id.relatedLayout);
		relatedWebView = (RelativeLayout)findViewById(R.id.relatedWebView);
		
		scrollLayout = (ScrollView)findViewById(R.id.scroll);
		
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
		
		tb = new TabResult(singleMovieInstance);
		//Enable movie counter dialog
//		VideoCounter.checkVideoCounter(singleMovieInstance);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		height = displaymetrics.heightPixels;
		width = displaymetrics.widthPixels;

		relatedHeight = height;
		
		try 
		{
			if(webView!=null) 
			{
				Log.d(DEBUG_TAG, "WEBVIEW NOT NULL");
				webView = null;
			}
			
			if(webView==null) 
			{
				Log.d(DEBUG_TAG, "WEBVIEW NULL");
				webView = new HTML5WebView(this);
				webView.getSettings().setJavaScriptEnabled(true);
				webView.getSettings().setUseWideViewPort(true);
				webView.getSettings().setLoadWithOverviewMode(true);
				webView.getSettings().setPluginState(PluginState.ON);
				vc = webView.getLayoutParams();
				vc.height = height / 2;
				//Log.d("Screen Height:", "vc.height:" + vc.height);
				vc1 = relatedWebView.getLayoutParams();
				vc1.height = height/2;
				relatedWebView.setLayoutParams(vc1);
				webView.setLayoutParams(vc);
			}
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Intent i = getIntent();
		// id = ShareData.getSavedString(singleMovieInstance,
		// "ListViewAdapterMovieId");
		id = i.getStringExtra("movieId");

		//Log.d(DEBUG_TAG, "StartingPage.value:"+StartingPage.value);
		
		tvDirector = (TextView) findViewById(R.id.tvDirectorName);
		tvDirector.setTextColor(Color.GRAY);
		tvDirector.setTypeface(ShareData.RobotoFont(singleMovieInstance));
		tvDirector.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)singleMovieInstance.getResources().getDimension(R.dimen.text_size2));
		
		tvDirectorLabel = (TextView) findViewById(R.id.tvDirectorLabel);
		tvDirectorLabel.setTextColor(Color.GRAY);
		tvDirectorLabel.setTypeface(ShareData.RobotoFont(singleMovieInstance));
		tvDirectorLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)singleMovieInstance.getResources().getDimension(R.dimen.text_size2));
		
		tvViews = (TextView) findViewById(R.id.tvViews);
		tvViews.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)singleMovieInstance.getResources().getDimension(R.dimen.text_size2));
		tvViews.setTextColor(Color.GRAY);
		tvViews.setTypeface(ShareData.RobotoFont(singleMovieInstance));
		
		tvViewsLabel = (TextView) findViewById(R.id.tvViewsLabel);
		tvViewsLabel.setTextColor(Color.GRAY);
		tvViewsLabel.setTypeface(ShareData.RobotoFont(singleMovieInstance));
		tvViewsLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)singleMovieInstance.getResources().getDimension(R.dimen.text_size2));
		
		tvMovieName = (TextView) findViewById(R.id.tvMovieName);
		tvMovieName.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//		tvMovieName.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
		tvMovieName.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)singleMovieInstance.getResources().getDimension(R.dimen.text_size1));
		tvMovieName.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				if(singleMoviePageLoader == true)
				{
					finish();
					Intent i = new Intent(getBaseContext(), MovieSummary.class);
					i.putExtra("movieName", movieName);
					i.putExtra("movieImage", movieImage);
					i.putExtra("movieSummary", movieSummary);
					i.putExtra("movieDuration", movieDuration);
					i.putExtra("movieReleaseDate", movieReleaseDate);
					i.putExtra("movieGenre", movieGenre);

					jsonData = data.toString();
					i.putExtra("json", data.toString());
					// i.putExtra("roleName", roleName);
					// i.putExtra("artistName", artistName);

					// go to Movie Summary page
					startActivity(i);
					overridePendingTransition(R.anim.animation1, R.anim.animation2);
				}
				else
				{
					Toast.makeText(getBaseContext(), SingleMovieWaitingText, Toast.LENGTH_SHORT).show();
				}
			}
		});
			
		infoLayout = (LinearLayout) findViewById(R.id.infoLayout);
		directorLayout = (LinearLayout) infoLayout.findViewById(R.id.directorLayout);
		
		
		userId = ShareData.getSavedString(singleMovieInstance, "id");
//		userId="305";
//		ListViewAdapter.singleMovieId = "11";
		if(userId!=null && userId.trim().length()!=0)
		{
			new HttpAsyncTask().execute("http://bongobd.com/api/content.php?id="+ ListViewAdapter.singleMovieId+"&user_id="+userId);
			Log.d(DEBUG_TAG, "browse user signed in:http://bongobd.com/api/content.php?id="+ ListViewAdapter.singleMovieId+"&user_id="+userId);
		}
		else
		{
			new HttpAsyncTask().execute("http://bongobd.com/api/content.php?id="+ ListViewAdapter.singleMovieId);
			Log.d(DEBUG_TAG, "browse:http://bongobd.com/api/content.php?id="+ ListViewAdapter.singleMovieId);
		}
		
		OrientationChange.onRotate(singleMovieInstance);
		OrientationChange._isFullScreen = false;
		OrientationChange.counts=0;
		OrientationChange.counts1=0;
	 }
	
    public static String GET(String url)
    {
        InputStream inputStream = null;
        String result = "";
        try 
        {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
 
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } 
        catch (Exception e) 
        {
            //Log.d("InputStream", e.getLocalizedMessage());
        }
 
        return result;
    }
 
    private static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }
 
    public boolean isConnected()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                return true;
            else
                return false;  
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls) 
        {
 
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) 
        {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            counter=0;
    		relatedImgUrls=null;
    		relatedTitles = null;
    		relatedViews = null;
    		relatedIds = null;
    		relatedContentLength = null;
    		relatedGenre = null;
    		
            try 
			{
            	JSONObject response = new JSONObject(result);
				data = response.getJSONObject("data");
				//Log.d(DEBUG_TAG, "datas: "+data);
				if(userId.trim().length()!=0)
				{
					userData = data.getJSONObject("content_user_data");
					//Log.d(DEBUG_TAG, "userData:"+userData);
					rate_status = userData.getString("rate_status");
					if(!rate_status.trim().equals("null"))
					{
						userRateStatus = Integer.parseInt(rate_status);
						//Log.d(DEBUG_TAG, "userRateStatus:"+userRateStatus);
					}
				}
				else
				{
					//Log.d(DEBUG_TAG, "No User loged in");
				}
				
				additionalData = response.getJSONObject("additionalData");
				// Log.d(DEBUG_TAG, " additionalData:"+additionalData );
				if(additionalData!=null)
				{
					if(additionalData.has("iframe2"))
					{
						movieUrl = additionalData.getString("iframe2");
						//Log.d(DEBUG_TAG, " movieUrl:"+ movieUrl );
						String[] array = movieUrl.split("=");
						int len = array.length;
						movieID = array[len-1].trim();
						//Log.d(DEBUG_TAG, "movieID:"+ movieID );
					}
				}
				
				if(data.has("content_title"))
				{
					movieName = data.getString("content_title");
					//Log.d(DEBUG_TAG, "movieName:"+movieName );
				}
				
				if(data.has("genre"))
				{
					movieGenre = data.getString("genre");
					//Log.d(DEBUG_TAG, "movieGenre:"+movieGenre );
				
					movieImage = "http://bongobd.com/wp-content/themes/bongobd/images/posterimage/thumb/"
						+ data.getString("content_thumb");
					//Log.d(DEBUG_TAG, "movieImage:"+movieImage );
				}
				if(data.has("by"))
				{
					movieDirector = data.getString("by");
					// Log.d(DEBUG_TAG,
					// "movieDirector: "+movieDirector);
					// if there is no director then delete it
					if (movieDirector.length() == 0)
					{
						infoLayout.removeView(directorLayout);
					} 
					else
					{
						tvDirectorLabel.setText("By ");
						tvDirector.setText(movieDirector);
					}
				}
				if(data.has("total_view"))
				{
					movieViews = data.getString("total_view");
					if(movieViews != null)
					{
						tvViewsLabel.setText(" Views");
					}
				}
				// Log.d(DEBUG_TAG, "movieViews: "+movieViews);
				
				//Getting Episode Type
				if(data.has("content_type"))
				{
					movieType = data.getString("content_type");
					if(movieType != null)
					{
						if(movieType.trim().equals("Episode"))
						{
							episodeEnable = true;
						}
						else
						{
							episodeEnable = false;
						}
					}
					//Log.d(DEBUG_TAG, "movieType:"+movieType);
					//Log.d(DEBUG_TAG, "episodeEnable:"+episodeEnable);
				}
				if(data.has("category_name"))
				{
					movieCategory = data.getString("category_name");
				}
				// Log.d(DEBUG_TAG,
				// "movieCategory: "+movieCategory);

				//moviePostedOn = data.getString("entry_time");
				// Log.d(DEBUG_TAG,
				// "moviePostedOn: "+moviePostedOn);

				//Getting movie summary
				if(data.has("content_short_summary"))
				{
					movieShortSummary = data.getString("content_short_summary");
					// Log.d(DEBUG_TAG, "movieShortSummary: "+movieShortSummary);
					
					movieSummary = data.getString("content_summary");
					movieSummary = movieSummary.replaceAll("(?<!\\\\)\\\\(?!\\\\)", "");
					movieSummary = movieSummary.replaceAll("</i>", "");
					movieSummary = movieSummary.replaceAll("<i>", "");
					//Log.d(DEBUG_TAG, "movieSummary: "+movieSummary);
				}
				
				//Getting movie release date
				if(data.has("release_date"))
				{
					movieReleaseDate = data.getString("release_date");
					movieReleaseDate = ShareData.dateFix(movieReleaseDate);
					//Log.d(DEBUG_TAG, "movieReleaseDate: "+movieReleaseDate);
				}
				
				//Counting episode numbers
				if(data.has("episode_count"))
				{
					episodeCount = data.getString("episode_count");
					//Log.d(DEBUG_TAG, "episodeCount:"+episodeCount);
				}
				
				//Getting movie average rating
				if(data.has("avg_rating"))
				{
					movieAvgRating = data.getString("avg_rating");
					//Log.d(DEBUG_TAG, "movieAvgRating string value:"+movieAvgRating);
				
					if(movieAvgRating.equals("null"))
					{
						movieRating=0;
						//Log.d(DEBUG_TAG, "movieAvgRating null:");
						
					}
					else if(!movieAvgRating.equals("null"))
					{
						//Log.d(DEBUG_TAG, " not null movieAvgRating:");
						//Log.d(DEBUG_TAG, "movieAvgRating:"+movieAvgRating);
						movieRating = Float.parseFloat(movieAvgRating);
						//Log.d(DEBUG_TAG, "movieRating:"+movieRating);
					}
				}
				
				if(data.has("content_details")) 
				{
					movieContentDetails = data.getString("content_details");
					// Log.d(DEBUG_TAG,"movieContentDetails: "+movieContentDetails);

					//Parsing Movie Casts
					ArrayList<String> allNames = new ArrayList<String>();
					cast = data.getJSONArray("content_details");
					for (int i = 0; i < cast.length(); i++) 
					{
						JSONObject actor = cast.getJSONObject(i);
						detailsId = actor.getString("details_id");
						contentId = actor.getString("content_id");
						artistId = actor.getString("artist_id");
						artistName = actor.getString("artist_name");
						artistProfileImage = actor.getString("artist_profile_image");
						roleName = actor.getString("role_name");
						roleId = actor.getString("role_id");
					}
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				//Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
			}
            hidepDialog();
       }
    }
		
	public void hidepDialog() 
	{
		tvViews.setText(movieViews);
		tvMovieName.setText(movieName);
		
		tabContainer.setVisibility(View.VISIBLE);
		if(data==null)
		{
			tabContainer.setVisibility(View.INVISIBLE);
			Toast.makeText(getBaseContext(), "There is no Data.", Toast.LENGTH_SHORT).show();
		}
		
		
		tb.addTabs();
		tb.addListenerOnRatingBar();
		
		//if there is episodes, then add episode carasol, if not, then add only related contents
		if(episodeEnable==true)
		{
			//AddEpisodes.AddEpisodeLayout(singleMovieInstance, Integer.parseInt(episodeCount));
			AddEpisodes p = new AddEpisodes();
			p.makeRequestForEpisodes("http://bongobd.com/api/episodes.php?id="+ListViewAdapter.singleMovieId);
		}
		else if(episodeEnable==false)
		{
			RelatedContents k = new RelatedContents();
			k.makeRequestForRelatedContents("http://bongobd.com/api/related_contents.php?content_id="+ListViewAdapter.singleMovieId+"&pager=3");
			Log.d(DEBUG_TAG, "http://bongobd.com/api/related_contents.php?content_id="+ListViewAdapter.singleMovieId+"&pager=3");
		}

//        if(savedInstanceStates != null) 
//        {
//        	webView.restoreState(savedInstanceStates);
//        } 
//        else 
//        {    
            //mWebView.loadUrl("http://www.google.com");
			//Log.d(DEBUG_TAG, "WebView Not Null");
//        }
		
		//Load & Play Video
		LoadPlayVideo();
		
		//if search suggestion box opened, close it
		disableSuggestionBox();
		
//		if(episodeEnable==true)
//		{
//			AddEpisodes p = new AddEpisodes();
//			p.makeRequestForEpisodes("http://stage.bongobd.com/api/content_new.php?id="+ListViewAdapter.singleMovieId);
//		}
		//AddEpisodes.makeJsonObjectRequestForEpisodes("http://stage.bongobd.com/api/content_new.php?id=92", singleMovieInstance);
		//RelatedContents.seeMoreCounter=61;
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		Log.d(DEBUG_TAG, "App Pause");
		
		if(webView!=null)
		{
			webView.onPause();
		}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		Log.d(DEBUG_TAG, "App Resume");
		
		if(webView!=null)
		{
			webView.onResume();
		}
		
	}
	@Override
	public void onStop()
	{
		super.onStop();
		overridePendingTransition( R.anim.animation1, R.anim.animation2 );
		Log.d(DEBUG_TAG, "App Stopped");
		
		movieSummary = "";
		movieGenre = "";
		movieCategory = "";
		episodeEnable = false;
		SingleMovieActivityRunning = false;
//		try
//		{
//			if(webView!=null)
//			{
//				webView.stopLoading();
//				webView.destroy();
//				webView = null;
//			}
//		} 
//		catch (Exception e1) 
//		{
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		TabResult.counter=0;
		movieRating=0;
		userRateStatus=0;
		if (requestQueue != null)
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
	public void onDestroy() 
	{
		super.onDestroy();
		Log.d(DEBUG_TAG, "Destroyed");
		
		try
		{
			if(webView!=null)
			{
				webView.stopLoading();
				webView.destroy();
				webView = null;
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		data = null;
		
		try 
		{
			if(RelatedContents.relatedLayouts!=null)
			{
				RelatedContents.relatedLayouts.removeAllViews();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public void onBackPressed() 
	{
		super.onBackPressed();
		SingleMoviePage.this.finish();
		
		try 
		{
			if(webView!=null)
			{
				webView.stopLoading();
				webView.destroy();
				webView = null;
			}
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(StartingPage.singleMoviePageReturn == 1)
		{
			 startActivity(new Intent(getBaseContext(), CategoryLanding.class));
			 overridePendingTransition( R.anim.animation1, R.anim.animation2 );
		} 
		else if(StartingPage.singleMoviePageReturn == 2)
		{
		  	 startActivity(new Intent(getBaseContext(), Movies.class));
		  	 overridePendingTransition( R.anim.animation1, R.anim.animation2 );
		} 
		else if(StartingPage.singleMoviePageReturn == 3)
		{
		  	 startActivity(new Intent(getBaseContext(), ActorProfile.class));
		  	 overridePendingTransition( R.anim.animation1, R.anim.animation2 );
		} 
		
	}

	//On orientation change player change 
	@Override
	public void onToggleFullScreen() 
	{
		OrientationChange.onToggleFullScreen(singleMovieInstance);
	}
	

	public void LoadPlayVideo()
	{
		String data1 = "<!doctype html>";
		data1 += "<html style=\"height:"+100+"%\">";
		data1 += "<head>";
		data1 += "</head>";
		data1 += "<body style=\"height:"+100+"%\">"; 
			
		data1 += "<script src=\"http://cdnapi.kaltura.com/p/1868701/sp/186870100/embedIframeJs/uiconf_id/29771892/partner_id/1868701\"></script>";
		data1 += "<div id=\"kaltura_player_1432820407\" style=\"width: "+ShareData.getScreenWidth(singleMovieInstance)+"; height: "+100+"%;\" itemprop=\"video\" itemscope itemtype=\"http://schema.org/VideoObject\">";
		data1 += "<script>";
		data1 += "kWidget.embed({\"targetId\": \"kaltura_player_1432820407\",\"wid\": \"_1868701\",\"uiconf_id\": 29771892,\"flashvars\": {\"akamaiHD\": {\"loadingPolicy\": \"preInitialize\",\"asyncInit\": \"true\"},\"streamerType\": \"hdnetwork\",\"IframeCustomPluginCss1\" : 'http://www.bongobd.com/customSkin.css?v=5'},\"cache_st\": 1432848959,\"entry_id\": \""+movieID+"\"});";
		data1 += "</script>"; 
			
		data1 += "</body>";
		data1 += "</html>";
			
		//Load video to webview
		try 
		{
			if(webView!=null)
			{
				Log.d(DEBUG_TAG, "webview not nulla");
				webView.loadDataWithBaseURL("http://localhost/", data1, "text/html", "utf-8", null);
			}
			else if(webView==null)
			{
				Log.d(DEBUG_TAG, "webview nulla");
				webView = new HTML5WebView(this);
				webView.getSettings().setJavaScriptEnabled(true);
				webView.getSettings().setUseWideViewPort(true);
				webView.getSettings().setLoadWithOverviewMode(true);
				webView.getSettings().setPluginState(PluginState.ON);
				vc = webView.getLayoutParams();
				vc.height = height / 2;
				//Log.d("Screen Height:", "vc.height:" + vc.height);
				vc1 = relatedWebView.getLayoutParams();
				vc1.height = height/2;
				relatedWebView.setLayoutParams(vc1);
				webView.setLayoutParams(vc);
				
				webView.loadDataWithBaseURL("http://localhost/", data1, "text/html", "utf-8", null);
			}
		}
		catch (Exception e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Auto play movie-webview
		try 
		{
			if(webView!=null)
			{
				webView.setWebViewClient(new WebViewClient() 
				{
				   public void onPageFinished(WebView view, String url)
				   {
    			        // do your stuff here
	   				    runOnUiThread(new Runnable() 
						{
							public void run() 
							{ 
								//btnFullScreen.setVisibility(View.VISIBLE);
										
								Handler handlerTimer = new Handler();
								handlerTimer.postDelayed(new Runnable()
								{
							        public void run()
							        {
										//Log.d(DEBUG_TAG, "x:"+HTML5WebView.mContentView.getLayoutParams().width/2);
										//Log.d(DEBUG_TAG, "y:"+HTML5WebView.mContentView.getLayoutParams().height/2);
										final long uMillis = SystemClock.uptimeMillis();
										try 
										{
											if(SingleMovieActivityRunning==true)
											{
												if(webView!=null)
												{
													webView.dispatchTouchEvent(MotionEvent.obtain(uMillis, uMillis, MotionEvent.ACTION_DOWN, 100,100, 0)); 
													webView.dispatchTouchEvent(MotionEvent.obtain(uMillis, uMillis, MotionEvent.ACTION_UP, 100,100, 0));
												}
											}
										}
										catch (Exception e) 
										{
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
							      }}, 2000);
										
								  Log.d(DEBUG_TAG, "Play the video now");

								} 
							});
					    }
					});
				}
					
			} 
			catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	
	public void disableSuggestionBox()
	{
		//if suggestion box is opened, then close it
		scrollLayout.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1)
			{
				//Log.d(DEBUG_TAG, "Scroller Touched");
				try
				{
					if(Search.mpopup!=null && Search.mpopup.isShowing())
					{
								Search.mpopup.dismiss();
					}
				}
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		});
	}
}