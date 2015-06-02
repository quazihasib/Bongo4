package com.movies.people;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.movies.bongobd.R;
import com.tab.ShareData;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class PeopleCarasol  
{
	public static RequestQueue requestQueue1;
	public static String[] images ;
	public static int imageCounter=0;
	public static String DEBUG_TAG = PeopleCarasol.class.getSimpleName();
	public static int maxScroll = 0;
	
	public static TextView[] artsistTv;
	
	public static void addArtistLayout()
	{
		artsistTv = new TextView[People.artistRoles.length+1];
		for(int t=0; t<People.artistRoles.length; t++)
		{
			if(People.artistRoles[t]!=null)
			{
				artsistTv[t] = new TextView(People.peopleInstance);
				artsistTv[t].setTextColor(Color.BLACK);
				artsistTv[t].setBackgroundColor(Color.GRAY);
				artsistTv[t].setGravity(Gravity.CENTER);
				artsistTv[t].setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)People.peopleInstance.getResources().getDimension(R.dimen.text_size1));
				artsistTv[t].setTag(People.keys[t]);
				int width = ShareData.getScreenWidth(People.peopleInstance)/3;
				int height = width/3;
				LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(width, height);
				titleParams.setMargins(0, ShareData.padding10, 0, 0);
				artsistTv[t].setText(""+People.artistRoles[t]);

				LinearLayout ll = new LinearLayout(People.peopleInstance);
				LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				
				//************
				View v11  = new View(People.peopleInstance);
				v11.setBackgroundColor(Color.WHITE);
				LinearLayout.LayoutParams v11Params = new LinearLayout.LayoutParams(ShareData.padding10/4,height);
				v11Params.setMargins(0, ShareData.padding10, 0, 0);
				People.mainLayoutArtist.addView(ll,llParams);
				
				ll.addView(artsistTv[t], titleParams);
				ll.addView(v11, v11Params);
				
				
				artsistTv[t].setOnClickListener(new View.OnClickListener() 
				{
					@Override
					public void onClick(View arg0) 
					{
						// TODO Auto-generated method stub
						if(People.banner == false)
						{
							String id = ""+arg0.getTag();
//							int a = Integer.parseInt(id);
//							a = a-1;
							Log.d(DEBUG_TAG, "id pressed:"+id);
							getArtistData(id);
						}
						else
						{
							Toast.makeText(People.peopleInstance, "Loading..", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		}
		
		ViewTreeObserver vto = People.artistScroll.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() 
		{
		    @Override
		    public void onGlobalLayout() 
		    {
		    	People.artistScroll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		    	maxScroll = People.artistScroll.getChildAt(0)
		                .getMeasuredWidth()-People.peopleInstance.getWindowManager().getDefaultDisplay().getWidth();
		     
		    	Log.d(DEBUG_TAG, "artistScroll:"+maxScroll);

		    }
		});
		
		int width = (ShareData.getScreenWidth(People.peopleInstance)/3)/4;
		int height = (ShareData.getScreenWidth(People.peopleInstance)/3)/4;
		
		People.artistImageIvL = new ImageView(People.peopleInstance);
		RelativeLayout.LayoutParams rIvlParams = new RelativeLayout.LayoutParams(width, height);
		People.artistImageIvL.setImageResource(R.drawable.sidel1);
		rIvlParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rIvlParams.setMargins(0, height-(int)(height/1.5), 0, 0);
		People.artistImageIvL .setVisibility(View.INVISIBLE);
		
		People.artistImageIvR = new ImageView(People.peopleInstance);
		RelativeLayout.LayoutParams rIvrParams = new RelativeLayout.LayoutParams(width, height);
		People.artistImageIvR.setImageResource(R.drawable.sider1);
		rIvrParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rIvrParams.setMargins(0,  height-(int)(height/1.5), 0, 0);
		
		
		People.peopleRelative.addView(People.artistImageIvL, rIvlParams);
		People.peopleRelative.addView(People.artistImageIvR, rIvrParams);
		
		People.artistScroll.setOnTouchListener(new OnTouchListener() 
		{           
		    @Override
		    public boolean onTouch(View v, MotionEvent event)
		    {
		    	if(People.artistScroll.getScrollX()==0) 
		    	{
		    		//Log.d(DEBUG_TAG, "scroller:"+v.getId()+" hide left");
		    		People.artistImageIvL.setVisibility(View.INVISIBLE);
		    		if(People.artistImageIvR.getVisibility()==View.INVISIBLE)
		    		{
		    			People.artistImageIvR.setVisibility(View.VISIBLE);
		    		}
		    	}
		    	else if(People.artistScroll.getScrollX() >= maxScroll )
		    	{
		    		//Log.d(DEBUG_TAG, "scroller:"+v.getId()+" hide right");
		    		People.artistImageIvR.setVisibility(View.INVISIBLE);
		    		if(People.artistImageIvL.getVisibility()==View.INVISIBLE)
		    		{
		    			People.artistImageIvL.setVisibility(View.VISIBLE);
		    		}
		    	}
		    	else
		    	{
		    		if(People.artistImageIvL.getVisibility()==View.INVISIBLE)
		    		{
		    			People.artistImageIvL.setVisibility(View.VISIBLE);
		    		}
		    		
		    		if(People.artistImageIvR.getVisibility()==View.INVISIBLE)
		    		{
		    			People.artistImageIvR.setVisibility(View.VISIBLE);
		    		}
		    	}
			return false;
		    }
		});
		
		//call for carasols 
		makeJsonObjectRequestForCarasols("http://bongobd.com/api/people_sliders.php?slider_id="+People.ids[People.count], People.loop);
	}
	
	public static void getArtistData(String id)
	{
		People.makeJsonObjectRequestForBanner("http://bongobd.com/api/people_banner.php?role="+id+"&landing="+"false", false);
	}
	
	
	public static void makeJsonObjectRequestForCarasols(String urlJsonObj, final int b)
	{
		Log.d(DEBUG_TAG, "urlJsonObj:"+urlJsonObj);
		
		images = null;
		People.carasolLoopCounter++;
		Log.d("Carasol", "carasolLoopCounter:"+People.carasolLoopCounter);
		
		requestQueue1 = Volley.newRequestQueue(People.peopleInstance);
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				urlJsonObj, null, new Response.Listener<JSONObject>()
				{
					@Override
					public void onResponse(JSONObject response) 
					{
						//Log.d(DEBUG_TAG, response.toString());
						try
						{
							JSONArray jsonArtist = response.getJSONArray("artist");
							
							Log.d(DEBUG_TAG, "jsonArtist.length()+1:"+jsonArtist.length()+1);
							images = new String[jsonArtist.length()+1];
//							People.peopleIds = new String[jsonArtist.length()+1][jsonArtist.length()+1];
//							People.peopleNames = new String[jsonArtist.length()+1][jsonArtist.length()+1];
//							
							imageCounter=0;
							for (int i=0; i<jsonArtist.length(); i++)
							{
							    JSONObject js = jsonArtist.getJSONObject(i);
							    String image_url = js.getString("slider_thumb_image");
							    image_url = "http://bongobd.com/wp-content/themes/bongobd/images/artistimage/thumb/"+image_url;
							    //Log.d(DEBUG_TAG, "image_url:" + image_url);
							    images[i] = image_url;
							    
							    imageCounter++;
							    
							    People.peopleIds[People.carasolLoopCounter][imageCounter] = js.getString("id");
							    //Log.d(DEBUG_TAG, "IDDDDD:" + People.peopleIds[People.carasolLoopCounter][imageCounter]+" carasolLoopCounter:"+People.carasolLoopCounter+" imageCounter:"+imageCounter);
							    
							    People.peopleNames[People.carasolLoopCounter][imageCounter] = js.getString("name");
							    if(People.peopleNames[People.carasolLoopCounter][imageCounter].length()>8)
							    {
							    	People.peopleNames[People.carasolLoopCounter][imageCounter] = People.peopleNames[People.carasolLoopCounter][imageCounter].substring(0, 7)+"..";
							    }
							    
							    //Log.d(DEBUG_TAG, "imageCounter:" +imageCounter);
							    //Log.d(DEBUG_TAG, "peopleIds:" + People.peopleIds[People.carasolLoopCounter][i+1]);
							}
							hidepDialog1(b);
						} 
						catch (JSONException e) 
						{
							People.errorCheck = 1;
							e.printStackTrace();
							hidepDialog1(b);
						}
						
					}
				}, 
				new Response.ErrorListener()
				{

					@Override
					public void onErrorResponse(VolleyError error) 
					{
						People.errorCheck = 1;
						VolleyLog.d("DEBUG_TAG", "Error: 2" + error.getMessage());
						hidepDialog1(b);
					}
				});

		// Adding request to request queue
		requestQueue1.add(jsonObjReq);
	}

//	public static void showpDialog1() 
//	{
//		if(!pDialog.isShowing()) 
//		{
//			pDialog.show();
//		}
//	}

	public static void hidepDialog1(int b)
	{
		PeopleAddLayout.addLayouts(b);
	}
}
