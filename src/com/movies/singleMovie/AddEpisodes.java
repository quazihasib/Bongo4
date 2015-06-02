package com.movies.singleMovie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.movies.bongobd.R;
import com.movies.browseAll.ListViewAdapter;
import com.tab.ShareData;

public class AddEpisodes 
{
	public static RequestQueue requestQueue;
	public static TextView[] episodeTv;
	public static int maxScroll = 0, length = 0;
	public static String episodeTag, episodeId;
	public static ImageView episodeImageIvL, episodeImageIvR;
	public static HorizontalScrollView episodeHz;
	public static LinearLayout mainLayoutEpsiode;
	public static RelativeLayout rLayout;
	public static String DEBUG_TAG = AddEpisodes.class.getSimpleName();
	public static String[] episodeUrl;
	
    public void makeRequestForEpisodes(String k)
    {
    	new HttpAsyncTask().execute(k);
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
            Log.d("InputStream", e.getLocalizedMessage());
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
        ConnectivityManager connMgr = (ConnectivityManager) SingleMoviePage.singleMovieInstance.getSystemService(Activity.CONNECTIVITY_SERVICE);
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
            //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            //tv1.setText(result);
            try
			{
            	JSONObject response = new JSONObject(result);
				
            	JSONArray episodes = response.getJSONArray("episodes");
				//Log.d(DEBUG_TAG, "episodes: "+episodes);
				
				length = episodes.length();
				episodeUrl = new String[episodes.length()+1];
				
				for(int i=0; i<episodes.length(); i++)
				{
				    JSONObject js = episodes.getJSONObject(i);
				    String episode_content = js.getString("episode_content");
				    episodeUrl[i] = episode_content;
				    //episodeUrl[i] = episodeUrl[i].replaceAll("\\[", "").replaceAll("\\]","");
				    Log.d(DEBUG_TAG, "episodeUrl:" + episodeUrl[i]);
				}
				
				AddEpisodeLayout(SingleMoviePage.singleMovieInstance, length);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
//				Toast.makeText(instance,"Error: " + e.getMessage(),
//						Toast.LENGTH_LONG).show();
			}
				
		}
    }
    
//	public static void makeJsonObjectRequestForEpisodes(String urlJsonObj, final Activity instance) 
//	{
//		requestQueue = Volley.newRequestQueue(instance);
//		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
//				urlJsonObj, null, new Response.Listener<JSONObject>()
//				{
//					@Override
//					public void onResponse(JSONObject response) 
//					{
//						//Log.d(DEBUG_TAG, response.toString());
//						try 
//						{
//							JSONArray episodes = response.getJSONArray("episodes");
//							//Log.d(DEBUG_TAG, "episodes: "+episodes);
//							
//							length = episodes.length();
//							episodeUrl = new String[episodes.length()+1];
//							
//							for(int i=0; i<episodes.length(); i++)
//							{
//							    JSONObject js = episodes.getJSONObject(i);
//							    String episode_content = js.getString("episode_content");
//							    episodeUrl[i] = episode_content;
//							    episodeUrl[i] = episodeUrl[i].replaceAll("\\[", "").replaceAll("\\]","");
//							    Log.d(DEBUG_TAG, "episodeUrl:" + episodeUrl[i]);
//							}
//							
//							addArtistLayout(instance, length);
//						}
//						catch (JSONException e)
//						{
//							e.printStackTrace();
////							Toast.makeText(getApplicationContext(),
////									"Error: " + e.getMessage(),
////									Toast.LENGTH_LONG).show();
//						}
//					}
//				}, new Response.ErrorListener() 
//				{
//					@Override
//					public void onErrorResponse(VolleyError error)
//					{
//						VolleyLog.d(DEBUG_TAG, "Error: " + error.getMessage());
////						Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
//					}
//				});
//		//addArtistLayout(instance, length);
//		// Adding request to request queue
//		requestQueue.add(jsonObjReq);
//	}
	
	public static void AddEpisodeLayout(final Activity instance, final int number)
	{
		mainLayoutEpsiode = (LinearLayout)instance.findViewById(R.id.mainLayoutEpisode);
		rLayout = (RelativeLayout)instance.findViewById(R.id.mainEpisodeRelative);
		episodeHz = (HorizontalScrollView)instance.findViewById(R.id.mainLayoutEpisodeHS);
		
		Log.d(DEBUG_TAG, "episodeUrl.length:"+number);
		episodeTv = new TextView[number];
		for(int t=1; t<number; t++)
		{
//			if(People.artistRoles[t]!=null)
//			{
				episodeTv[t] = new TextView(instance);
				episodeTv[t].setTextColor(Color.BLACK);
				if(t!=1)
				{
					episodeTv[t].setBackgroundColor(Color.GRAY);
				}
				else if(t==1)
				{
					episodeTv[1].setBackgroundColor(Color.parseColor("#B40404"));
				}
					
				episodeTv[t].setGravity(Gravity.CENTER);
				episodeTv[t].setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)instance.getResources().getDimension(R.dimen.text_size1));
				try 
				{
					if(episodeUrl[t-1]!=null)
					{
						episodeTv[t].setTag(episodeUrl[t-1]);
					}
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				episodeTv[t].setId(t);
				int width = ShareData.getScreenWidth(instance)/3;
				int height = width/3;
				LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(width, height);
				titleParams.setMargins(0, ShareData.padding10, 0, 0);
				//episodeTv[t].setText(""+People.artistRoles[t]);
				episodeTv[t].setText("Episode "+t);
				
				LinearLayout ll = new LinearLayout(instance);
				LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				
				//************
				View v11  = new View(instance);
				v11.setBackgroundColor(Color.WHITE);
				LinearLayout.LayoutParams v11Params = new LinearLayout.LayoutParams(ShareData.padding10/4,height);
				v11Params.setMargins(0, ShareData.padding10, 0, 0);
				mainLayoutEpsiode.addView(ll,llParams);
				
				ll.addView(episodeTv[t], titleParams);
				ll.addView(v11, v11Params);
				
				//Click the episode number to play it
				episodeTv[t].setOnClickListener(new View.OnClickListener() 
				{
					@Override
					public void onClick(View arg0) 
					{
						// TODO Auto-generated method stub
						
						episodeTv[arg0.getId()].setBackgroundColor(Color.parseColor("#B40404"));
						for(int p=1; p<number; p++)
						{
							if(p!=arg0.getId())
							{
								episodeTv[p].setBackgroundColor(Color.GRAY);
							}
						}
						
						episodeTag = ""+arg0.getTag();
						episodeId = ""+arg0.getId();
						Log.d(DEBUG_TAG, "id pressed:"+episodeTag+" id:"+episodeId);
//						instance.finish();
//						instance.startActivity(new Intent(instance, SingleMoviePage.class));
						
						//stop the player before playing
						try 
						{
							if(SingleMoviePage.webView!=null)
							{
								SingleMoviePage.webView.stopLoading();
							}
						} 
						catch (Exception e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						String[] array = episodeTag.split("=");
						int len = array.length;
						String movieID = array[len-1].trim();
						
					    String data1 = "<!doctype html>";
						data1 += "<html style=\"height:"+100+"%\">";
						data1 += "<head>";
						data1 += "</head>";
						data1 += "<body style=\"height:"+100+"%\">"; 
						
						data1 += "<script src=\"http://cdnapi.kaltura.com/p/1868701/sp/186870100/embedIframeJs/uiconf_id/29771892/partner_id/1868701\"></script>";
						data1 += "<div id=\"kaltura_player_1432820407\" style=\"width: "+ShareData.getScreenWidth(SingleMoviePage.singleMovieInstance)+"; height: "+100+"%;\" itemprop=\"video\" itemscope itemtype=\"http://schema.org/VideoObject\">";
						data1 += "<script>";
						data1 += "kWidget.embed({\"targetId\": \"kaltura_player_1432820407\",\"wid\": \"_1868701\",\"uiconf_id\": 29771892,\"flashvars\": {\"akamaiHD\": {\"loadingPolicy\": \"preInitialize\",\"asyncInit\": \"true\"},\"streamerType\": \"hdnetwork\",\"IframeCustomPluginCss1\" : 'http://www.bongobd.com/customSkin.css?v=5'},\"cache_st\": 1432848959,\"entry_id\": \""+movieID+"\"});";
						data1 += "</script>"; 
						
						data1 += "</body>";
						data1 += "</html>";
						

//				        if(savedInstanceState != null) 
//				        {
//				        	webView.restoreState(savedInstanceState);
//				        } 
//				        else 
//				        {    
				            //mWebView.loadUrl("http://www.google.com");
//				        }
				          
						//play the selected episode
				        try
				        {
							if(SingleMoviePage.webView!=null)
							{
								 SingleMoviePage.webView.loadDataWithBaseURL("http://localhost/", data1, "text/html", "utf-8", null);
							}
						} 
				        catch (Exception e)
				        {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		}
		
		ViewTreeObserver vto = episodeHz.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() 
		{
		    @Override
		    public void onGlobalLayout() 
		    {
		    	episodeHz.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		    	maxScroll = episodeHz.getChildAt(0)
		                .getMeasuredWidth()-instance.getWindowManager().getDefaultDisplay().getWidth();
		     
		    	Log.d(DEBUG_TAG, "artistScroll:"+maxScroll);

		    }
		});
		
		int width = (ShareData.getScreenWidth(instance)/3)/4;
		int height = (ShareData.getScreenWidth(instance)/3)/4;
		
		episodeImageIvL = new ImageView(instance);
		RelativeLayout.LayoutParams rIvlParams = new RelativeLayout.LayoutParams(width, height);
		episodeImageIvL.setImageResource(R.drawable.sidel1);
		rIvlParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rIvlParams.setMargins(0, height-(int)(height/1.5), 0, 0);
		episodeImageIvL .setVisibility(View.INVISIBLE);
		
		episodeImageIvR = new ImageView(instance);
		RelativeLayout.LayoutParams rIvrParams = new RelativeLayout.LayoutParams(width, height);
		episodeImageIvR.setImageResource(R.drawable.sider1);
		rIvrParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rIvrParams.setMargins(0,  height-(int)(height/1.5), 0, 0);
		
		
		rLayout.addView(episodeImageIvL, rIvlParams);
		rLayout.addView(episodeImageIvR, rIvrParams);
		
//		episodeImageIvL.setVisibility(View.INVISIBLE);
//		episodeImageIvR.setVisibility(View.INVISIBLE);
		
		episodeHz.setOnTouchListener(new OnTouchListener() 
		{           
		    @Override
		    public boolean onTouch(View v, MotionEvent event)
		    {
		    	if(episodeHz.getScrollX()==0) 
		    	{
		    		//Log.d(DEBUG_TAG, "scroller:"+v.getId()+" hide left");
		    		episodeImageIvL.setVisibility(View.INVISIBLE);
		    		if(episodeImageIvR.getVisibility()==View.INVISIBLE)
		    		{
		    			episodeImageIvR.setVisibility(View.VISIBLE);
		    		}
		    	}
		    	else if(episodeHz.getScrollX() >= maxScroll )
		    	{
		    		//Log.d(DEBUG_TAG, "scroller:"+v.getId()+" hide right");
		    		episodeImageIvR.setVisibility(View.INVISIBLE);
		    		if(episodeImageIvL.getVisibility()==View.INVISIBLE)
		    		{
		    			episodeImageIvL.setVisibility(View.VISIBLE);
		    		}
		    	}
		    	else
		    	{
		    		if(episodeImageIvL.getVisibility()==View.INVISIBLE)
		    		{
		    			episodeImageIvL.setVisibility(View.VISIBLE);
		    		}
		    		
		    		if(episodeImageIvR.getVisibility()==View.INVISIBLE)
		    		{
		    			episodeImageIvR.setVisibility(View.VISIBLE);
		    		}
		    	}
			return false;
		    }
		});
		
		Handler handlerTimer = new Handler();
		handlerTimer.postDelayed(new Runnable()
		{
	        public void run()
	        {
	        	RelatedContents k = new RelatedContents();
	    		k.makeRequestForRelatedContents("http://bongobd.com/api/related_contents.php?content_id="+ListViewAdapter.singleMovieId+"&pager=3");
	    		//RelatedContents.makeJsonObjectRequestForRelatedContents("http://stage.bongobd.com/api/related_contents.php?content_id="+ListViewAdapter.singleMovieId+"&pager=3", singleMovieInstance);
	    		Log.d(DEBUG_TAG, "http://bongobd.com/api/related_contents.php?content_id="+ListViewAdapter.singleMovieId+"&pager=3");	
	        }
	    }, 1000);
		
	
//		if(episodeTag!=null)
//		{
//			episodeTv[Integer.parseInt(episodeId)].setBackgroundColor(Color.BLUE);
//			final Handler handler = new Handler();
//			final Runnable r = new Runnable() 
//			{
//				public void run() 
//				{
//					AddEpisodes.episodeHz.scrollTo((ShareData.getScreenWidth(instance)/3)*(Integer.parseInt(episodeId)), AddEpisodes.episodeHz.getBottom());
//					Log.d(DEBUG_TAG, "on start header scroller on");
//					//handler.postDelayed(this, 1000);
//				}
//			};
//			handler.postDelayed(r, 1000);
//		}
	}
}
