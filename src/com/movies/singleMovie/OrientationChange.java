package com.movies.singleMovie;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.tab.ShareData;

public class OrientationChange 
{
	static OrientationEventListener mOrientationListener;
	public static String DEBUG_TAG = OrientationChange.class.getSimpleName();
	public static boolean _isFullScreen;
	public static int counts=0, counts1=0;
	public static void onRotate(final Activity instance)
	{
	    mOrientationListener = new OrientationEventListener(instance,
	            SensorManager.SENSOR_DELAY_NORMAL) 
	    { 
	            @Override
	            public void onOrientationChanged(int orientation) 
	            {
	                //Log.d(DEBUG_TAG,"Orientation changed to " + orientation);
	                int orientations = instance.getResources().getConfiguration().orientation; 
	     	        if(Configuration.ORIENTATION_LANDSCAPE == orientations) 
	     	        { 
	     	             //Do SomeThing; // Landscape
	     	        	 Log.d(DEBUG_TAG, "LANDSCAPE");
	     	        	 
	     	        	 _isFullScreen = true;
	     	        	 counts1=0;
	     	        	 counts++;
						 if(counts==1)
						 {
							 SingleMoviePage.singleMovieMainLayout.removeView(SingleMoviePage.headerLayout);
//							 if(SingleMoviePage.episodeEnable==true)
//							 {
//								 SingleMoviePage.layoutMain.removeView(AddEpisodes.mainLayoutEpsiode);
//							 }
							
							 SingleMoviePage.layoutMain.removeView(SingleMoviePage.singleMovieDetails);
							 SingleMoviePage.layoutMain.removeView(SingleMoviePage.relatedLayout);
							
							 
							 SingleMoviePage.relatedWebView.getLayoutParams().height =  (int) (ShareData.getScreenHeight(instance)-ShareData.getScreenHeight(instance)/15);
							 try 
							 {
								 if(SingleMoviePage.webView!=null)
								 {
									 SingleMoviePage.webView.getLayoutParams().height =  (int) (ShareData.getScreenHeight(instance)-ShareData.getScreenHeight(instance)/15);
								 }
								 HTML5WebView.mContentView.getLayoutParams().width = (ShareData.getScreenWidth(SingleMoviePage.singleMovieInstance));
							 }
							 catch (Exception e) 
							 {
								// TODO Auto-generated catch block
								e.printStackTrace();
							 }
							
							 //SingleMoviePage. mPlayerView.setPlayerViewDimensions(SingleMoviePage.vc.width,  (int) (ShareData.getScreenHeight(instance)-ShareData.getScreenHeight(instance)/15));
							 
//							 RelativeLayout.LayoutParams btnFullScreenParams = new RelativeLayout.LayoutParams(width/9, width/10);
//								btnFullScreenParams.setMargins(0, 0, 0, 0);
//								btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//								btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//								btnFullScreen.setLayoutParams(btnFullScreenParams);
//								btnFullScreen.setPadding(0, 0, 0, 0);
						 }
						 
						 Log.d(DEBUG_TAG, "rotate::"+rotate(instance));
						 if(rotate(instance)==3)
						 {
							 instance.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
						 }
	     	        }  
	     	        else  if(Configuration.ORIENTATION_PORTRAIT == orientations) 
	     	        {
	     	        	counts=0;
	     	        	counts1++;
	     	        	_isFullScreen = false;
						 if(counts1==1)
						 {
							 if(SingleMoviePage.singleMovieMainLayout.getChildAt(0) != SingleMoviePage.headerLayout)
							 {
								 LinearLayout.LayoutParams headerAParams = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
								 SingleMoviePage.headerLayout.setLayoutParams(headerAParams);
								 SingleMoviePage.singleMovieMainLayout.addView(SingleMoviePage.headerLayout,0);
							 }
//							 if(AddEpisodes.mainLayoutEpsiode==null)
//							 {
//								 SingleMoviePage.layoutMain.addView(AddEpisodes.mainLayoutEpsiode);
//							 }
							 
							 if(SingleMoviePage.layoutMain.getChildAt(2)!=SingleMoviePage.singleMovieDetails)
							 {
								 SingleMoviePage.layoutMain.removeView(SingleMoviePage.singleMovieDetails);
								 SingleMoviePage.layoutMain.addView(SingleMoviePage.singleMovieDetails,2);
							 }
							 if(SingleMoviePage.layoutMain.getChildAt(3)!=SingleMoviePage.relatedLayout)
							 {
								 SingleMoviePage.layoutMain.removeView(SingleMoviePage.relatedLayout);
								 SingleMoviePage.layoutMain.addView(SingleMoviePage.relatedLayout,3);
							 }
							
							 
							 SingleMoviePage.relatedWebView.getLayoutParams().height = SingleMoviePage.height/2;
							 try 
							 {
								if(SingleMoviePage.webView!=null)
								 {
									 SingleMoviePage.webView.getLayoutParams().height =  SingleMoviePage.height/2;
								 }
							 } 
							 catch (Exception e)
							 {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						     //SingleMoviePage.mPlayerView.setPlayerViewDimensions(SingleMoviePage.vc.width , SingleMoviePage.height/2);
//							 
//							 RelativeLayout.LayoutParams btnFullScreenParams = new RelativeLayout.LayoutParams(height/20, height/20);
//							 	btnFullScreenParams.setMargins(ShareData.padding10*3, 0, 0, 0); 
//								btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//								btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//								btnFullScreen.setLayoutParams(btnFullScreenParams);
//								btnFullScreen.setPadding(0, ShareData.padding10, 0, 0);
						 }
	     	        	Log.d(DEBUG_TAG, "POTRAIT");
	     	       } 
	            }
	        };

	       if (mOrientationListener.canDetectOrientation() == true)
	       {
	           Log.v(DEBUG_TAG, "Can detect orientation");
	           mOrientationListener.enable();
	       }
	       else 
	       {
	           Log.v(DEBUG_TAG, "Cannot detect orientation");
	           mOrientationListener.disable();
	       }
	}
	
	public static int rotate(final Activity instance)
	{
		int rotation = instance.getWindowManager().getDefaultDisplay().getRotation();
		return rotation;
	}
	
	public static void onToggleFullScreen(final Activity instance)
	{
		Log.d(DEBUG_TAG, " screen::"+_isFullScreen);
//		Log.d(DEBUG_TAG, "rotate::"+rotate());
		
		if(_isFullScreen == false)
		{
			Log.d(DEBUG_TAG, "Go Landscape");
			_isFullScreen = true;
			SingleMoviePage.singleMovieMainLayout.removeView(SingleMoviePage.headerLayout);
			
			SingleMoviePage.layoutMain.removeView(SingleMoviePage.singleMovieDetails);
			SingleMoviePage.layoutMain.removeView(SingleMoviePage.relatedLayout);
			
			instance.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
			
			SingleMoviePage.relatedWebView.getLayoutParams().height =  (int) (ShareData.getScreenHeight(instance)-ShareData.getScreenHeight(instance)/15);
			try 
			{
				if(SingleMoviePage.webView!=null)
				{
					SingleMoviePage.webView.getLayoutParams().height =  (int) (ShareData.getScreenHeight(instance)-ShareData.getScreenHeight(instance)/15);
				}
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//SingleMoviePage.mPlayerView.setPlayerViewDimensions(SingleMoviePage.vc.width, (int) (ShareData.getScreenHeight(instance)-ShareData.getScreenHeight(instance)/15));
			
//			mPlayerView.getLayoutParams().height = getScreenHeight(instance)-200;
//			mPlayerView.setPlayerViewDimensions( getScreenWidth(instance) , getScreenHeight(instance)-200 );
//			} 
		}
		else if(_isFullScreen == true) 
		{
			 if(rotate(instance)==1 || rotate(instance)==3)
			 {
				 _isFullScreen = false;
				 Log.d(DEBUG_TAG, "Go Portrait");
				 instance.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				 //potrait
				 if(SingleMoviePage.singleMovieMainLayout.getChildAt(0) != SingleMoviePage.headerLayout)
				 {
					 LinearLayout.LayoutParams headerAParams = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					 SingleMoviePage.headerLayout.setLayoutParams(headerAParams);
					 SingleMoviePage.singleMovieMainLayout.addView(SingleMoviePage.headerLayout,0);
				 }
				 if(SingleMoviePage.layoutMain.getChildAt(1)!=SingleMoviePage.singleMovieDetails)
				 { 
					 SingleMoviePage.layoutMain.addView(SingleMoviePage.singleMovieDetails,1);
				 }
				 if(SingleMoviePage.layoutMain.getChildAt(2)!=SingleMoviePage.relatedLayout)
				 {
					 SingleMoviePage.layoutMain.addView(SingleMoviePage.relatedLayout,2);
				 }
				
				// instance.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				 
				 SingleMoviePage.relatedWebView.getLayoutParams().height = SingleMoviePage.height/2;
				 try
				 {
					if(SingleMoviePage.webView!=null)
					 {
						 SingleMoviePage.webView.getLayoutParams().height =  SingleMoviePage.height/2;
					 }
				 } 
				 catch (Exception e) 
				 {
					// TODO Auto-generated catch block
					e.printStackTrace();
				 }
				 //SingleMoviePage.mPlayerView.setPlayerViewDimensions(SingleMoviePage.vc.width, SingleMoviePage.height/2);
//			
//				 RelativeLayout.LayoutParams btnFullScreenParams = new RelativeLayout.LayoutParams(height/20, height/20);
//				 	btnFullScreenParams.setMargins(ShareData.padding10*3, 0, 0, 0); 
//					btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//					btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//					btnFullScreen.setLayoutParams(btnFullScreenParams);
//					btnFullScreen.setPadding(0, ShareData.padding10, 0, 0);
			 }
			
//			mPlayerView.getLayoutParams().height = getScreenHeight(instance)/3;
//			mPlayerView.setPlayerViewDimensions( getScreenWidth(instance) , getScreenHeight(instance)/3 );
//			}
			Handler handlerTimer = new Handler();
			handlerTimer.postDelayed(new Runnable(){
			        public void run() {
			          // do something   
			        	Log.d(DEBUG_TAG, "Make it neutral sensor");
			        	instance.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			      }}, 1000);
		}
	}
}
