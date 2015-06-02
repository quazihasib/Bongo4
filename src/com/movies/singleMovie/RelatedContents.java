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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.movies.bongobd.ImageLoader;
import com.movies.bongobd.R;
import com.movies.browseAll.ListViewAdapter;
import com.movies.startingPage.StartingPage;
import com.tab.ShareData;

public class RelatedContents 
{

	public static String[] relatedImgUrls;
	public static String[] relatedTitles;
	public static String[] relatedViews;
	public static String[] relatedIds;
	public static String[] relatedContentLength;
	public static String[] relatedCompanyName;
	public static int counter=0, seeMoreCounter, errorValue;
	public static boolean addLayout;
	
    public static ImageLoader imageLoader;
    public static RequestQueue requestQueue;
    public static boolean relatedContentClicked;
    public static LinearLayout relatedLayouts;
    
    public static String DEBUG_TAG = RelatedContents.class.getSimpleName();

    public void makeRequestForRelatedContents(String k)
    {
    	relatedContentClicked = false;
    	SingleMoviePage.singleMoviePageLoader = false;
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
        	errorValue=0;
    		counter=0;
    		relatedImgUrls=null;
    		relatedTitles = null;
    		relatedViews = null;
    		relatedIds = null;
    		relatedContentLength = null;
    		relatedCompanyName = null;
    		
            try 
			{
            	JSONObject response = new JSONObject(result);
            	JSONObject data = response.getJSONObject("data");
				Log.d(DEBUG_TAG, "data Related: "+data);
				
				relatedImgUrls = new String[data.length()+3];
				relatedTitles = new String[data.length()+3];
				relatedViews = new String[data.length()+3];
				relatedIds = new String[data.length()+3];
				relatedContentLength = new String[data.length()+3];
				relatedCompanyName = new String[data.length()+3];
				
				Iterator<String> iter = data.keys();
				while(iter.hasNext()) 
				{
				    counter++;
				    //Log.d(DEBUG_TAG, "number of items: "+counter); 
				    String key = iter.next();
				    try 
				    {
				        Object value = data.get(key);
				        //Log.d(DEBUG_TAG, "value:"+value ); 
				            
				        JSONObject eachObject = data.getJSONObject(""+ key);
				            
				        if(eachObject.has("id"))
				        {
				        	String id = eachObject.getString("id").trim();
				        	relatedIds[counter] = id;
				        	//Log.d(DEBUG_TAG, "id: "+id); 
				        }
				        
				        if(eachObject.has("content_length"))
				        {
				        	String content_length = eachObject.getString("content_length"); 
				        	content_length = ShareData.changeFormat(content_length);
				        	relatedContentLength[counter] = content_length;
				        }
				        
				        if(eachObject.has("content_title"))
				        {
				        	String content_title = eachObject.getString("content_title");
				        	relatedTitles[counter] = content_title;
				        	//Log.d(DEBUG_TAG, "content_title: "+content_title);
				        }
				        
				        if(eachObject.has("all_views"))
				        {
				        	String total_view = eachObject.getString("all_views");
				        	relatedViews[counter] = total_view;
				        	//Log.d(DEBUG_TAG, "entry_time: "+entry_time); 
				        }
				        
				        if(eachObject.has("genre"))
				        {
				        	String companyName = eachObject.getString("company_name");
				        	relatedCompanyName[counter]= companyName;
				        	//Log.d(DEBUG_TAG, "relatedCompanyName:"+companyName);
				        }
				        
				        if(eachObject.has("content_thumb"))
				        {
				        	String content_thumb = eachObject.getString("content_thumb");
				        	content_thumb= "http://bongobd.com/wp-content/themes/bongobd/" +
								"images/posterimage/thumb/"+content_thumb;
				        	relatedImgUrls[counter] = content_thumb;
				        	//Log.d(DEBUG_TAG, "img:"+relatedImgUrls[counter] +" "+counter);
				        }
				    } 
				    catch(JSONException e)
				    {
				         // Something went wrong!
				    }
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				errorValue=1;
//				Toast.makeText(instance,"Error: " + e.getMessage(),
//						Toast.LENGTH_LONG).show();
			}
			addRelatedLayout(SingleMoviePage.singleMovieInstance);
				
		}
    }
	
	public static void addRelatedLayout(final Activity instance)
	{
		imageLoader = new ImageLoader(instance);
		relatedLayouts = (LinearLayout)instance.findViewById(R.id.relatedLayout);
		//relatedLayouts.removeAllViews();
		
		if(addLayout==true)
		{
			TextView tv1 = new TextView(instance);
			LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			tv1.setTextColor(Color.RED);
			tv1.setText("Related Content");
			tv1.setTypeface(ShareData.RobotoFont(SingleMoviePage.singleMovieInstance));
			tv1Params.setMargins(ShareData.padding10, ShareData.padding10, 0, ShareData.padding10);
			tv1.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)instance.getResources().getDimension(R.dimen.text_size1));
			try 
			{
				if(relatedLayouts!=null)
				{
					relatedLayouts.addView(tv1, tv1Params);
				}
			}
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//String im = "http://bongobd.com/wp-content/themes/bongobd/images/posterimage/cover/size-250/Chorabali.jpg";
		int i;
		for(i=1; i<=counter; i++)
		{
			LinearLayout mainLayout1 = new LinearLayout(instance);
			mainLayout1.setBackgroundColor(Color.WHITE);
			LinearLayout.LayoutParams mainLayout1params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			mainLayout1.setOrientation(LinearLayout.VERTICAL);
			try 
			{
				if(relatedLayouts!=null)
				{
					relatedLayouts.addView(mainLayout1, mainLayout1params);
				}
			} 
			catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			RelativeLayout rl = new RelativeLayout(instance);
			RelativeLayout.LayoutParams rlParams= new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (SingleMoviePage.relatedHeight/3));
			rl.setLayoutParams(rlParams);
			
			LinearLayout extraLayout = new LinearLayout(instance);
			extraLayout.setBackgroundColor(Color.WHITE);
			LinearLayout.LayoutParams extraLayoutparams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ShareData.getScreenWidth(instance)/12);
			extraLayout.setOrientation(LinearLayout.VERTICAL);
			
			final ImageView im1 = new ImageView(instance);
			im1.setTag(i);
			im1.setId(1);
//			im1.setOnClickListener(new View.OnClickListener() 
//			{
//				@Override
//				public void onClick(View arg0) 
//				{
//					// TODO Auto-generated method stub
//					try 
//					{
////						Log.d(DEBUG_TAG, "clicked");
//						int id = Integer.parseInt(arg0.getTag().toString());
//						Log.d(DEBUG_TAG, "ids:"+relatedIds[id]);
//						
//						ListViewAdapter.singleMovieId = relatedIds[id];
//						SingleMoviePage.singleMovieInstance.finish();
//						SingleMoviePage.singleMovieInstance.startActivity(new Intent(SingleMoviePage.singleMovieInstance, SingleMoviePage.class));
//						SingleMoviePage.singleMovieInstance.overridePendingTransition( R.anim.animation1, R.anim.animation2 );
//					} 
//					catch (Exception e) 
//					{
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			});
			im1.setScaleType(ScaleType.FIT_XY);
			try 
			{
				imageLoader.DisplayImage(relatedImgUrls[i], im1);
			}
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LinearLayout.LayoutParams im1Params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (SingleMoviePage.relatedHeight/3));
			rl.addView(im1, im1Params);
			
			
			
			
			TextView tv5 = new TextView(instance);
			RelativeLayout.LayoutParams tv5Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			tv5Params.addRule(RelativeLayout.ALIGN_BOTTOM,1);
			tv5Params.addRule(RelativeLayout.ALIGN_RIGHT,1);
			tv5Params.setMargins(0, 0, ShareData.padding10, ShareData.padding10);
			tv5.setBackgroundColor(Color.parseColor("#99000000"));
			tv5.setTextColor(Color.WHITE);
			tv5.setTypeface(ShareData.RobotoFont(SingleMoviePage.singleMovieInstance));
			try 
			{
				tv5.setText(""+relatedContentLength[i]);
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tv5.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)instance.getResources().getDimension(R.dimen.text_size2));
			tv5.setLayoutParams(tv5Params);
			rl.addView(tv5);
			mainLayout1.addView(extraLayout, extraLayoutparams);
			mainLayout1.addView(rl);

			
			TextView tv2 = new TextView(instance);
			LinearLayout.LayoutParams tv2Params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			tv2.setTextColor(Color.BLACK);
			try 
			{
				tv2.setText(""+relatedTitles[i]);
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tv2Params.setMargins(ShareData.padding10, ShareData.padding10, 0, ShareData.padding10);
			tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)instance.getResources().getDimension(R.dimen.text_size1));
			tv2.setTypeface(ShareData.RobotoFont(SingleMoviePage.singleMovieInstance));
			mainLayout1.addView(tv2, tv2Params);
			
			//Each related movie layout 
			LinearLayout mainLayout2 = new LinearLayout(instance);
			mainLayout2.setBackgroundColor(Color.WHITE);
			LinearLayout.LayoutParams mainLayout2params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			mainLayout2params.setMargins(0, 0, 0, ShareData.padding10);
			mainLayout2.setOrientation(LinearLayout.HORIZONTAL);
			mainLayout1.addView(mainLayout2, mainLayout2params);
			mainLayout1.setOnClickListener(new View.OnClickListener() 
			{
				@Override
				public void onClick(View arg0) 
				{
					// TODO Auto-generated method stub
					if(SingleMoviePage.singleMoviePageLoader==true)
					{
						try 
						{
							Log.d(DEBUG_TAG, "clicked");
							int id = Integer.parseInt(im1.getTag().toString());
							//Log.d(DEBUG_TAG, "ids:"+relatedIds[id]);
						
							ListViewAdapter.singleMovieId = relatedIds[id];
						} 
						catch (Exception e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
						if(SingleMoviePage.SingleMovieActivityRunning==true)
						{
							instance.finish();
							Log.d(DEBUG_TAG, "Running....");
						}
						relatedContentClicked = true;
						instance.startActivity(new Intent(instance, StartingPage.class));
						instance.overridePendingTransition( R.anim.animation1, R.anim.animation2 );
					}
					else
					{
						Toast.makeText(SingleMoviePage.singleMovieInstance, ""+SingleMoviePage.SingleMovieWaitingText, Toast.LENGTH_SHORT).show();
					}
				}
			});
			
			TextView tv3 = new TextView(instance);
			LinearLayout.LayoutParams tv3Params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			tv3.setTextColor(Color.GRAY);
			try 
			{
				if(relatedCompanyName[i]!=null )
				{
					tv3.setText("By "+relatedCompanyName[i]);
				}
			} 
			catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			tv3.setTypeface(ShareData.RobotoFont(SingleMoviePage.singleMovieInstance));
			tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)instance.getResources().getDimension(R.dimen.text_size2));
			tv3Params.setMargins(ShareData.padding10, 0, 0, ShareData.padding10);
			mainLayout2.addView(tv3, tv3Params);
			
			TextView tv4 = new TextView(instance);
			LinearLayout.LayoutParams tv4Params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			tv4.setTextColor(Color.GRAY);
			try 
			{
				if(relatedViews[i]!=null)
				{
					if(!relatedViews[i].trim().equals("0"))
					{
						tv4.setText(" | "+relatedViews[i]+" views");
					}
				}
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)instance.getResources().getDimension(R.dimen.text_size2));
			tv4Params.setMargins(ShareData.padding10, 0, 0, ShareData.padding10);
			tv4.setTypeface(ShareData.RobotoFont(SingleMoviePage.singleMovieInstance));
			mainLayout2.addView(tv4, tv4Params);
			
			
			View v1 = new View(instance);
			v1.setBackgroundColor(Color.parseColor("#E6E6E6"));
			LinearLayout.LayoutParams v1Params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ShareData.padding10/2);
			try 
			{
				if(relatedLayouts!=null)
				{
					relatedLayouts.addView(v1, v1Params);
				}
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		final Button btnSeeMore = new Button(instance);
		btnSeeMore.setText("More");
		btnSeeMore.setTextColor(Color.RED);
		btnSeeMore.setTypeface(ShareData.RobotoFont(SingleMoviePage.singleMovieInstance));
		btnSeeMore.setBackgroundColor(Color.WHITE);
		btnSeeMore.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)instance.getResources().getDimension(R.dimen.text_size2));
		
		LinearLayout.LayoutParams btnSeeMoreParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		try 
		{
			if(relatedLayouts!=null)
			{
				relatedLayouts.addView(btnSeeMore, btnSeeMoreParams);
			}
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		btnSeeMore.setTypeface(null, Typeface.BOLD);
		
		if(errorValue==1)
		{
			relatedLayouts.removeView(btnSeeMore);
		}
		
		btnSeeMore.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				if(SingleMoviePage.singleMoviePageLoader==true)
				{
					Log.d(DEBUG_TAG, "see more");
					seeMoreCounter++;
					RelatedContents k = new RelatedContents();
					k.makeRequestForRelatedContents("http://bongobd.com/api/related_contents.php?content_id="+ListViewAdapter.singleMovieId+"&pager="+seeMoreCounter);
					Log.d(DEBUG_TAG, "Related Movie:http://bongobd.com/api/related_contents.php?content_id="+ListViewAdapter.singleMovieId+"&pager="+seeMoreCounter);
			
					addLayout = false;
					relatedLayouts.removeView(btnSeeMore);
				}
				else
				{
					Toast.makeText(SingleMoviePage.singleMovieInstance, ""+SingleMoviePage.SingleMovieWaitingText, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		Log.d(DEBUG_TAG, "SingleMoviePage Loading Ended");
		SingleMoviePage.singleMoviePageLoader = true;
		
		if(SingleMoviePage.webView==null)
		{
			Log.d(DEBUG_TAG, "webviewnullb");
		}
		else
		{
			Log.d(DEBUG_TAG, "webview not nullb");
		}
		
		//SingleMoviePage.layoutMain.invalidate();
		
//		SingleMoviePage.mPlayerView.addComponents
//		("https://cdnapisec.kaltura.com/html5/html5lib/v2.21/mwEmbedFrame.php/p/1868701/uiconf_id/29233222/entry_id/"+SingleMoviePage.movieID+"?wid=_1868701&iframeembed=true&playerId=kaltura_player_1428490836&entry_id="+SingleMoviePage.movieID, SingleMoviePage.singleMovieInstance );
//		Log.d(DEBUG_TAG, "movie url::"+"https://cdnapisec.kaltura.com/html5/html5lib/v2.21/mwEmbedFrame.php/p/1868701/uiconf_id/29233222/entry_id/"+SingleMoviePage.movieID+"?wid=_1868701&iframeembed=true&playerId=kaltura_player_1428490836&entry_id="+SingleMoviePage.movieID);
	}
}


