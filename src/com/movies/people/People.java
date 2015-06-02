package com.movies.people;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.movies.bongobd.ImageLoader;
import com.movies.bongobd.R;
import com.movies.categoryPage.CategoryLanding;
import com.movies.startingPage.StartingPage;
import com.tab.AddMenu;
import com.tab.Header;
import com.tab.ShareData;
import com.tab.SwipeGestureDetector;
import com.tab.UnCaughtException;


public class People extends Activity 
{
	public static int counter1=0, artistImageCounter = 0;
	public static int errorCheck=0;
	public static int carasolLoopCounter=0, urlLoopCounter=0;
	public static int loop, count;
	public static int widthScreen;
	public static int categoryID;
	public static boolean startSliders, flag;
	public static String[] images, names, ids, artistNames, artistRoles;
	public static String[][] peopleIds, peopleNames;
	public static int[] keys;
	
	public static LinearLayout mainLayout, mainLayoutArtist, peopleFlipperLayer;
	public static ListView listview;
	
	public static RequestQueue requestQueue;
    public static ProgressDialog pDialog;
	public static ImageLoader imageLoader;

	public static People peopleInstance;
	public static String DEBUG_TAG = People.class.getSimpleName();
	
	public String catagoryName;
	public boolean role;
	public static ImageView[] ivL, ivR;
	public static HorizontalScrollView[] scrollView ;
	public static HorizontalScrollView artistScroll;
	public static int[] maxScroll;
	public static RelativeLayout peopleRelative;
	public ScrollView scrollPeople;
	public static ImageView artistImageIvL, artistImageIvR;
	
	public static ViewFlipper peopleViewFlipper;
	public final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());
	
	public Header h;
	public static boolean banner;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.people);
		
		peopleInstance = this;
		
		//Handling application crashing
		Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(People.this));
				
		h = AddMenu.HeaderFunction(peopleInstance);
		AddMenu.pageNumber = 2;
		
		StartingPage.actorPageReturn = 3;
		StartingPage.browseAllPageReturn = 2;
		
		CategoryLanding.catagoryName = "people";
		
		mainLayout = (LinearLayout)findViewById(R.id.mainLayoutPeople);
		scrollPeople = (ScrollView)findViewById(R.id.scrollPeople);
		mainLayoutArtist = (LinearLayout)findViewById(R.id.mainLayoutArtist);
		artistScroll = (HorizontalScrollView) findViewById(R.id.mainLayoutArtistHS);
		peopleRelative = (RelativeLayout)findViewById(R.id.mainPeopleRelative);
		
		
		startSliders = false;
		StartingPage.browseAll = 2;
		role = true;
		categoryID=0;
		PeopleAddLayout.imgCounter=0;
		PeopleCarasol.maxScroll=0;
		
		h.tv1.setBackgroundColor(Color.parseColor("#FF0000"));
		h.tv2.setBackgroundColor(Color.parseColor("#FF0000"));
		h.tv3.setBackgroundColor(Color.parseColor("#FF0000"));
		h.tv4.setBackgroundColor(Color.parseColor("#B40404"));
		
		if(ShareData.isNetworkAvailable(peopleInstance)==true)
		{
			mainLayout.removeAllViews();
			carasolLoopCounter = 0;
			urlLoopCounter = 0;
			errorCheck = 0;
			count =0;
			counter1=0;
			banner = false;
			
			peopleIds=new String[1000][1000];
			peopleNames=new String[1000][1000];
			
			flag=false;
			peopleFlipperLayer = (LinearLayout) findViewById(R.id.peopleFlipperLayer);
			LayoutParams params = peopleFlipperLayer.getLayoutParams();
			params.height = StartingPage.bannerHeight;
        
			widthScreen = ShareData.getScreenWidth(peopleInstance);
			imageLoader = new ImageLoader(this);
			
			peopleViewFlipper = (ViewFlipper)findViewById(R.id.peopleViewFlipperLayout);
			peopleViewFlipper.getParent().requestDisallowInterceptTouchEvent(true);
			scrollPeople.setOnTouchListener(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) 
				{
					// TODO Auto-generated method stub
					if( arg1.getY()<StartingPage.bannerHeight)
					{
						Log.d(DEBUG_TAG, "slide banner");
						detector.onTouchEvent(arg1);
					}
					
					return false;
				}
			});
//			peopleFlipperLayer.setOnTouchListener(new OnTouchListener()
//			{
//				@Override
//				public boolean onTouch(final View view, final MotionEvent event)
//				{
//					detector.onTouchEvent(event);
//					return true;
//				}
//			});
			
			makeJsonObjectRequestForBanner("http://bongobd.com/api/people_banner.php?role=2&landing="+role, true);
			Log.d(DEBUG_TAG, "main url1:"+"http://bongobd.com/api/people_banner.php?role=2&landing="+role);
		}
		else
		{
			Toast.makeText(getBaseContext(), "No Internet", Toast.LENGTH_SHORT).show();
		}
		
	}

	public static void makeJsonObjectRequestForBanner(String urlJsonObj, final boolean layoutFlag)
	{
		Log.d(DEBUG_TAG, "main banner:"+urlJsonObj);
		images=null;
		artistNames = null;
		ids = null;
		names = null;
		artistImageCounter = 0;
		banner = true;
		
		requestQueue = Volley.newRequestQueue(peopleInstance);
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				urlJsonObj, null, new Response.Listener<JSONObject>()
				{
					@Override
					public void onResponse(JSONObject response) 
					{
						//Log.d(DEBUG_TAG, response.toString());
						try 
						{
							JSONArray json = response.getJSONArray("banner");
							images = new String[json.length()+1];
							names = new String[json.length()+1];
							for (int i=0; i<json.length(); i++)
							{
							    JSONObject js = json.getJSONObject(i);
							    String image_url = js.getString("slider_thumb_image");
							    image_url = "http://bongobd.com/wp-content/themes/bongobd/images/artistimage/thumb/"+image_url;
							    images[i] = image_url;
							    //Log.d(DEBUG_TAG, "image_url:" + images[i]);
							    
							    names[i] = js.getString("name");
							    Log.d(DEBUG_TAG, "names:" + names[i]);
							}
							
							JSONArray json2 = response.getJSONArray("sliders");
							ids = new String[json2.length()+1]; 
							artistNames = new String[json2.length()+1]; 
							for (int i=0; i<json2.length(); i++)
							{
							    JSONObject js2 = json2.getJSONObject(i);
							    //Log.d(DEBUG_TAG, "js2:" + js2);
							    ids[i] = js2.getString("id");
							    //Log.d(DEBUG_TAG, "ids:" + ids[i]);
							    
							    artistNames[i] = js2.getString("name");
							    //Log.d(DEBUG_TAG, "artistNames:" + artistNames[i]);
							}
							
							//Artist roles
							JSONObject json3 = response.getJSONObject("roles");
							artistRoles = new String[json3.length()+3];
							keys = new int[json3.length()+1];
							Iterator<String> iter = json3.keys();
							while (iter.hasNext()) 
							{ 
								String key = iter.next();
								try 
								{
									Object value = json3.get(key);
									artistImageCounter++;
									artistRoles[artistImageCounter] = ""+value.toString().trim();
									keys[artistImageCounter] = Integer.parseInt(key);
									//Log.d(DEBUG_TAG, "artistRoles:" + artistRoles[artistImageCounter]+" KeY:"+keys[artistImageCounter]);  
								} 
								catch (JSONException e) 
								{
									// Something went wrong!
								}
							}

						}
						catch (JSONException e) 
						{
							errorCheck = 1;
							e.printStackTrace();
							Toast.makeText(peopleInstance,
							"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
							//dismiss progress dialog
							//pDialog.dismiss();
						}
						hidepDialog(layoutFlag);
						maxScroll = new int[ids.length];
						scrollView = new HorizontalScrollView[ids.length];
						ivL = new ImageView[ids.length];
						ivR = new ImageView[ids.length];
					}
				}, 
				new Response.ErrorListener()
				{

					@Override
					public void onErrorResponse(VolleyError error) 
					{
						errorCheck = 1;
						VolleyLog.d(DEBUG_TAG, "Error: " + error.getMessage());
						//Toast.makeText(getApplicationContext(),
								//error.getMessage(), Toast.LENGTH_SHORT).show();
						// hide the progress dialog
						hidepDialog(layoutFlag);
					}
				});

		// Adding request to request queue
		requestQueue.add(jsonObjReq);
	}
	
	@SuppressLint("NewApi")
	public static void hidepDialog(boolean layoutFlags)
	{
		counter1=0;
		banner = false;
		//peopleFlipperLayer.removeAllViewsInLayout();
		peopleViewFlipper.removeAllViews();		
		//Log.d(DEBUG_TAG, "images.length-1:" + (images.length-1));
		if(images!=null)
		{
			for(int i = 0; i < images.length-1; i++)
			{
				try 
				{
					RelativeLayout peopleBannerRelativeLayout = new RelativeLayout(peopleInstance);
					RelativeLayout.LayoutParams peopleBannerRelativeLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, StartingPage.bannerHeight);
				
					//people banner name
					TextView peopleTvNameLayout = new TextView(peopleInstance);
					RelativeLayout.LayoutParams peopleTvNameLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					peopleTvNameLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					peopleTvNameLayout.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)People.peopleInstance.getResources().getDimension(R.dimen.text_size1));
					peopleTvNameLayout.setTextColor(Color.WHITE);
					peopleTvNameLayoutParams.setMargins(ShareData.padding10, 0, 0, ShareData.padding10);
					try
					{
						if(names[i]!=null)
						{
							peopleTvNameLayout.setText(""+names[i]);
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					peopleTvNameLayout.setId(44); 
				

					//people banner rank
					TextView peopleTvRankLayout = new TextView(peopleInstance);
					RelativeLayout.LayoutParams peopleTvRankLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					peopleTvRankLayout.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)People.peopleInstance.getResources().getDimension(R.dimen.text_size1));
					peopleTvRankLayout.setTextColor(Color.WHITE);
					peopleTvRankLayout.setPadding(ShareData.padding10*2, ShareData.padding10/2, ShareData.padding10*2, ShareData.padding10/2);
					peopleTvRankLayout.setBackgroundResource(R.drawable.border_people);
					
					peopleTvRankLayoutParams.setMargins(ShareData.padding10, 0, 0, ShareData.padding10/2);
					try
					{
						peopleTvRankLayout.setText("#"+(i+1));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					peopleTvRankLayoutParams.addRule(RelativeLayout.ABOVE, peopleTvNameLayout.getId());
				
					//people banner images
					ImageView imageView = new ImageView(peopleInstance);
					RelativeLayout.LayoutParams imageViewLayoutParams = new RelativeLayout.LayoutParams(widthScreen, StartingPage.bannerHeight);
					try 
					{
						if(images[i]!=null)
						{
							imageLoader.DisplayImage(images[i], imageView);
						}
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}
					imageView.setScaleType(ScaleType.FIT_XY);
				
					//add people images, names and ranks to the view
					peopleBannerRelativeLayout.addView(imageView, imageViewLayoutParams);
					peopleBannerRelativeLayout.addView(peopleTvNameLayout, peopleTvNameLayoutParams);
					peopleBannerRelativeLayout.addView(peopleTvRankLayout, peopleTvRankLayoutParams);
				
					peopleViewFlipper.addView(peopleBannerRelativeLayout, peopleBannerRelativeLayoutParams);
					peopleViewFlipper.setDisplayedChild(0);
				
//				peopleViewFlipper.setAutoStart(true);
//				peopleViewFlipper.setFlipInterval(4000);
//		        if(!peopleViewFlipper.isFlipping())
//		        {
//		        	peopleViewFlipper.startFlipping();
//		        }
//		        peopleViewFlipper.setInAnimation(peopleInstance, R.anim.slide_in_from_right);
//		    	peopleViewFlipper.setOutAnimation(peopleInstance, R.anim.slide_out_to_left);
			}
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}		 
		//Log.d("DEBUG_TAG", "layout flag:" + flag);  
		if(layoutFlags==true)
		{
			if(ids!=null && artistRoles!=null)
			{
				loop = ids.length;
				count = 0;
			
				PeopleCarasol.addArtistLayout();
			}
			
		}
		AddMenu.clickable=true;
	}
	
	@Override
	public void onStop() 
	{
		super.onStop();
		
		Log.d(DEBUG_TAG, "App Stopped");
		
		counter1=0;
		artistImageCounter = 0;
		errorCheck=0;
		carasolLoopCounter=0;
		urlLoopCounter=0;
		loop=0; 
		count=0;
		//catagoryID=0;
	    startSliders=false;
	    flag=false;
		images=null; 
		ids=null; 
		artistNames=null;
		artistRoles=null;
		
		AddMenu.clickable=true;
		//PeopleCarasol
//		if(requestQueue!=null)
//		{
//			requestQueue.cancelAll(new RequestQueue.RequestFilter() {
//
//				@Override
//				public boolean apply(Request<?> request) {
//					// TODO Auto-generated method stub
//					return true;
//				}
//		    });
//		}
//		requestQueue=null;
		if(PeopleCarasol.requestQueue1!=null)
		{
			PeopleCarasol.requestQueue1.cancelAll(new RequestQueue.RequestFilter()
			{
				@Override
				public boolean apply(Request<?> request)
				{
					// TODO Auto-generated method stub
					return true;
				}
		    });
		}
		PeopleCarasol.requestQueue1 = null;
		People.images=null;
		
		catagoryName=null;
		role=false;
		
		Slider.peopleHandler.removeCallbacks(Slider.peopleRunnable);
	}	
	
	public void onStart()
	{
		super.onStart();
		//Log.d(DEBUG_TAG, "on start app:"+catagoryName);
		
		//scroll to people section of the header on start
		final Handler handler = new Handler();
		final Runnable r = new Runnable() 
		{
			public void run() 
			{
				h.hsv.scrollTo(ShareData.getScreenWidth(peopleInstance)+StartingPage.padding*2,  h.hsv.getBottom());
				Log.d(DEBUG_TAG, "on start header scroller on");
				//handler.postDelayed(this, 1000);
			}
		};
		handler.postDelayed(r, 1000);
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		Log.d(DEBUG_TAG, "App Destroyed");
	}	
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		People.this.finish();
		
		startActivity(new Intent(getBaseContext(), StartingPage.class));
		overridePendingTransition( R.anim.animation1, R.anim.animation2 );
	}
}