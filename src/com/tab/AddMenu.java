package com.tab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.movies.actorProfile.ActorProfile;
import com.movies.bongobd.ImageLoader;
import com.movies.bongobd.R;
import com.movies.browseAll.Movies;
import com.movies.categoryPage.CategoryLanding;
import com.movies.facebook.FacebookApp;
import com.movies.login.LoginActivity;
import com.movies.login.LoginNew;
import com.movies.login.UserProfile;
import com.movies.movieSummary.MovieSummary;
import com.movies.people.People;
import com.movies.singleMovie.SingleMoviePage;
import com.movies.startingPage.StartingPage;
import com.movies.subscribe.Subscribe;
import com.navdrawer.SimpleSideDrawer;

public class AddMenu 
{
	public static boolean menuFlag;
	public static ImageLoader userImageLoader;
	public static ImageView menuButton, loginPic, settings, ivSearch, ivBongoIcon;
	public static TextView tvName;
	public static SimpleSideDrawer slide_me;
	public static boolean clickable;
	public static String searchQuery;
	
	public static int pageNumber;
	
	static int height;
	static int h1;
	public static Header header;
	public static Handler clickHandler;
	public static Runnable alternate;
	public static RequestQueue requestQueue;
	public static String DEBUG_TAG = AddMenu.class.getSimpleName();
	public static AutoCompleteTextView et;
	public static Bitmap bitmap;
	
	public static Header HeaderFunction(final Activity instance)
    {

    	AddMenu.addMenus(instance);
    	clickable=false;
    	//makeJsonObjectRequestForSearchQuery("http://stage.bongobd.com/api/autocomplete.php", instance);
		
        header = (Header) instance.findViewById(R.id.headers);
		header.initHeader(instance);
		
		header.bongoIcon.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				instance.finish();
				instance.startActivity(new Intent(instance.getBaseContext(), StartingPage.class));
				instance.overridePendingTransition(R.anim.animation1, R.anim.animation2);
			}
		});
		
		
		et = new AutoCompleteTextView(instance);
//		et.setHint("Search");
		et.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int)instance.getResources().getDimension(R.dimen.text_size2));
		et.setHintTextColor(Color.BLACK);
		et.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		et.setTextColor(Color.BLACK);
		try 
		{
			et.setBackgroundResource(R.drawable.border);
		} 
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		et.setBackgroundColor(Color.parseColor("#E6E6E6"));
		et.setSingleLine();
		et.requestFocus();
		et.setText(null);
		InputMethodManager inputManager = (InputMethodManager)instance.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.restartInput(et);
		
		//Cross image for shutting down the search
		try 
		{
			Drawable dr = instance.getResources().getDrawable(R.drawable.cross11);
			bitmap = ((BitmapDrawable) dr).getBitmap();
		} 
		catch (NotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Drawable d = new BitmapDrawable(instance.getResources(), Bitmap.createScaledBitmap(bitmap,  ShareData.getScreenHeight(instance)/50,  ShareData.getScreenHeight(instance)/50, true));
		et.setCompoundDrawablesWithIntrinsicBounds( null, null, d, null);
		et.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		et.setPadding(ShareData.padding10, 0, ShareData.padding10, 0);
		et.setOnTouchListener(new OnTouchListener() 
		{
	        @Override
	        public boolean onTouch(View v, MotionEvent event) 
	        {
	            final int DRAWABLE_LEFT = 0;
	            final int DRAWABLE_TOP = 1;
	            final int DRAWABLE_RIGHT = 2;
	            final int DRAWABLE_BOTTOM = 3;

	            if(event.getAction() == MotionEvent.ACTION_UP) 
	            {
	                if(event.getRawX() >= (et.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) 
	                {
	                    //dismiss pop up is it is showing
	                	if(Search.mpopup!=null && Search.mpopup.isShowing())
	            		{
	                		Search.mpopup.dismiss();
	            		}
	                	//hide keyboard and add the normal header
	                	ShareData.hideKeyboard1(instance, et);
	                	header.headerLayout.removeAllViews();
	        			header.headerLayout.addView(header.menuIcon, header.menuLayout);
	        			header.headerLayout.addView(header.bongoIcon, header.bongoLayout);
	        			header.headerLayout.addView(header.searchIcon, header.searchLayout);
	                 return true;
	                }
	            }
	            return false;
	        }
	    });
//		ShareData.hideKeyboard(instance);
//		ShareData.hideKeyboard1(instance, et);
		et.addTextChangedListener(new TextWatcher()
		{
		    public void beforeTextChanged(CharSequence s, int start, int count, int after)
		    {

		    }
		    public void onTextChanged(CharSequence s, int start, int before, int count)
		    {
		    	 Search.makeJsonObjectRequestForSearchQuery
		         ("http://stage.bongobd.com/api/autocomplete.php?key="+et.getText().toString().trim(), instance);
		    }
		    public void afterTextChanged(Editable s) 
		    {
		         //Log.d(DEBUG_TAG, "len:"+et.getText().toString().length());
		         //http://bongobd.com/api/menu_categories.php?menu=tv
		    }
		}); 
		 
		et.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) 
		    {
		        if (hasFocus) 
		        {
		            et .setHint("");
		        }
		    }
		});
		et.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
		   @Override
		   public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
		   {
			    if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || 
			    		(actionId == EditorInfo.IME_ACTION_SEARCH))
			    {
			    	actionSearch(instance);
	            } 
		        return false;
		    }
		});
		
		final LinearLayout.LayoutParams etLayout = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 9);
		etLayout.setMargins(ShareData.padding10*1, ShareData.padding10*1, -ShareData.padding10*1, ShareData.padding10*1);
		et.setLayoutParams(etLayout);
		
		header.searchIcon.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				//Dialog dialog1s = DialogSearch.dialogs(instance);
			 	//dialog1s.show();
				//http://bongobd.com/api/menu_categories.php?menu=tv
				ShareData.openKeyboard(instance, et);
				
				//search button pressed first time
				if(header.headerLayout.getChildCount()==3)
				{
					
//					header.searchLayout.setMargins(ShareData.padding10*2, ShareData.padding10*2, ShareData.padding10*2, ShareData.padding10*2);
//					header.searchLayout.setMargins(ShareData.padding10*4, ShareData.padding10*2, ShareData.padding10*12, ShareData.padding10*2);
					header.headerLayout.removeView(header.menuIcon);
					header.headerLayout.removeView(header.bongoIcon);
					header.headerLayout.addView(et, 0);
					
					
				}
				//after search query is written
				else if(header.headerLayout.getChildCount()==2)
				{
					searchQuery = et.getText().toString().trim();
					Log.d("AddMenu", "searchQuery:"+searchQuery);
					//If there is data
					if(searchQuery.length()!=0)
					{
						header.headerLayout.removeAllViews();
						header.headerLayout.addView(header.menuIcon, header.menuLayout);
						header.headerLayout.addView(header.bongoIcon, header.bongoLayout);
						header.headerLayout.addView(header.searchIcon, header.searchLayout);
						header.searchLayout.setMargins(ShareData.padding10*8, ShareData.padding10*2, ShareData.padding10*5, ShareData.padding10*2);
					
						StartingPage.browseAll = 3;
					
						instance.finish();
						instance.startActivity(new Intent(instance.getBaseContext(), Movies.class));
						instance.overridePendingTransition(R.anim.animation1, R.anim.animation2);
					}
					//If there is no data
					else
					{
						instance.finish();
						instance.startActivity(new Intent(instance.getBaseContext(), CategoryLanding.class));
						instance.overridePendingTransition(R.anim.animation1, R.anim.animation2);
					}
					
					ShareData.hideKeyboard1(instance, et);
				}
			}
		});
		
		
		header.menuIcon.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				slide_me.toggleLeftDrawer();
			}
		});
		
		new CountDownTimer(3000, 1000) 
		{
			public void onTick(long millisUntilFinished) 
		    {
//		    	header.tv1.setText("Wait for " + (millisUntilFinished / 1000) + " seconds");
		    }
		    public void onFinish() 
		    {
		    	header.tv1.setOnClickListener(new View.OnClickListener() 
		 		{ 
		    		@Override
		 			public void onClick(View arg0) 
		 			{
		 				// TODO Auto-generated method stub
		 				CategoryLanding.catagoryName = "movies";
		 				instance.finish();
		 				instance.startActivity(new Intent(instance.getBaseContext(), CategoryLanding.class));
		 				instance.overridePendingTransition(R.anim.animation1, R.anim.animation2);
		 				
		 				header.tv1.setBackgroundColor(Color.parseColor("#B40404"));
		    			header.tv2.setBackgroundColor(Color.parseColor("#FF0000"));
		    			header.tv3.setBackgroundColor(Color.parseColor("#FF0000"));
		    			header.tv4.setBackgroundColor(Color.parseColor("#FF0000"));
		 			}
		 		});
		    	
		    	header.tv2.setOnClickListener(new View.OnClickListener() 
				{
					@Override
					public void onClick(View arg0) 
					{
						// TODO Auto-generated method stub
						CategoryLanding.catagoryName = "tv";
						instance.finish();
						instance.startActivity(new Intent(instance.getBaseContext(), CategoryLanding.class));
						instance.overridePendingTransition(R.anim.animation1, R.anim.animation2);
						
						header.tv1.setBackgroundColor(Color.parseColor("#FF0000"));
		    			header.tv2.setBackgroundColor(Color.parseColor("#B40404"));
		    			header.tv3.setBackgroundColor(Color.parseColor("#FF0000"));
		    			header.tv4.setBackgroundColor(Color.parseColor("#FF0000"));
					}
				});
				
				header.tv3.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						// TODO Auto-generated method stub
						CategoryLanding.catagoryName = "music";
						instance.finish();
						instance.startActivity(new Intent(instance.getBaseContext(), CategoryLanding.class));
						instance.overridePendingTransition(R.anim.animation1, R.anim.animation2);
						
						header.tv1.setBackgroundColor(Color.parseColor("#FF0000"));
		    			header.tv2.setBackgroundColor(Color.parseColor("#FF0000"));
		    			header.tv3.setBackgroundColor(Color.parseColor("#B40404"));
		    			header.tv4.setBackgroundColor(Color.parseColor("#FF0000"));
					}
				});
		     }
		}.start();
		  
		
		header.tv4.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				instance.finish();
				instance.startActivity(new Intent(instance.getBaseContext(), People.class));
				instance.overridePendingTransition(R.anim.animation1, R.anim.animation2);
				
				header.tv1.setBackgroundColor(Color.parseColor("#FF0000"));
    			header.tv2.setBackgroundColor(Color.parseColor("#FF0000"));
    			header.tv3.setBackgroundColor(Color.parseColor("#FF0000"));
    			header.tv4.setBackgroundColor(Color.parseColor("#B40404"));
			}
		});

//		header.tv5.setOnClickListener(new View.OnClickListener() 
//		{
//			@Override
//			public void onClick(View arg0) 
//			{
//				// TODO Auto-generated method stub
//				
//				instance.finish();
//				instance.startActivity(new Intent(instance.getBaseContext(), Subscribe.class));
//				instance.overridePendingTransition(R.anim.animation1, R.anim.animation2);
//			}
//		});
		
		
		header.hsv.setOnTouchListener(new OnTouchListener()
		{           
		    @Override
		    public boolean onTouch(View v, MotionEvent event) 
		    {
		    	//Log.d(DEBUG_TAG, "scroller:"+header.hsv.getScrollX());
		    	if(header.hsv.getScrollX()==0) 
		    	{
		    		header.sideLeft.setVisibility(View.INVISIBLE);
		    		if(header.sideRight.getVisibility()==View.INVISIBLE)
		    		{
		    			header.sideRight.setVisibility(View.VISIBLE);
		    		}
		    	}
		    	else if(header.hsv.getScrollX() >= Header.mainMaxScroll)
		    	{
		    		//Log.d(header.DEBUG_TAG, "scroller:"+v.getId()+" hide right");
		    		
		    		header.sideRight.setVisibility(View.INVISIBLE);
		    		if(header.sideLeft.getVisibility()==View.INVISIBLE)
		    		{
		    			header.sideLeft.setVisibility(View.VISIBLE);
		    		}
		    	}
		    	else
		    	{
		    		if(header.sideLeft.getVisibility()==View.INVISIBLE)
		    		{
		    			header.sideLeft.setVisibility(View.VISIBLE);
		    		}
		    		
		    		if(header.sideRight.getVisibility()==View.INVISIBLE)
		    		{
		    			header.sideRight.setVisibility(View.VISIBLE);
		    		}
		    	}
			return false;
		    }
		});
		
		return header;
    }
	
	public static  void addMenus(final Activity instance)
	{
		slide_me = new SimpleSideDrawer(instance);
		slide_me.setLeftBehindContentView(R.layout.left_menu);
		
		int widths = ShareData.getScreenWidth(instance)/3;
		int heights = (int) (widths/2.5);
		
		LinearLayout menuLoginPanelLayout = (LinearLayout) instance.findViewById(R.id.menuLoginPanelLayout);
		LinearLayout.LayoutParams menuLoginPanelLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, heights);
		menuLoginPanelLayout.setLayoutParams(menuLoginPanelLayoutParams);
		
		LinearLayout menuRedPanelLayout = (LinearLayout) instance.findViewById(R.id.menuRedPanelLayout);
		LinearLayout.LayoutParams menuRedPanelLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, heights/2);
		menuRedPanelLayout.setLayoutParams(menuRedPanelLayoutParams);
		menuRedPanelLayout.setGravity(Gravity.CENTER_VERTICAL);
		
		TextView tvRedPanelLayout = (TextView) instance.findViewById(R.id.tvRedPanelLayout);
		LinearLayout.LayoutParams tvRedPanelLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		tvRedPanelLayout.setLayoutParams(tvRedPanelLayoutParams);
		tvRedPanelLayout.setBackgroundColor(Color.parseColor("#d42027"));
		tvRedPanelLayout.setText("BEST OF BONGO");
		tvRedPanelLayout.setTextColor(Color.parseColor("#FFFFFF"));
		tvRedPanelLayout.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)instance.getResources().getDimension(R.dimen.text_size3));
		tvRedPanelLayout.setPadding(ShareData.padding10*2, 0, 0, 0);
		tvRedPanelLayout.setGravity(Gravity.CENTER_VERTICAL);
		
		
		userImageLoader = new ImageLoader(instance);
//		loginPic = (ImageView) instance.findViewById(R.id.loginPic);
//		LinearLayout.LayoutParams loginPicParams = new LinearLayout.LayoutParams(ShareData.getScreenWidth(instance)/8, 
//				ShareData.getScreenWidth(instance)/8);
//		loginPicParams.setMargins(ShareData.padding10*2, ShareData.padding10*2, ShareData.padding10*2, ShareData.padding10*2);
//		loginPic.setLayoutParams(loginPicParams);
		
		settings = (ImageView) instance.findViewById(R.id.settings);
		LinearLayout.LayoutParams settingsParams = new LinearLayout.LayoutParams(ShareData.getScreenWidth(instance)/18, 
				ShareData.getScreenWidth(instance)/18);
		settingsParams.setMargins(ShareData.padding10, ShareData.padding10*2, ShareData.padding10*2, ShareData.padding10*2);
		settingsParams.gravity = Gravity.CENTER_VERTICAL;
		settings.setLayoutParams(settingsParams);
		
    	tvName = (TextView) instance.findViewById(R.id.tvName);
    	LinearLayout.LayoutParams tvNameParams = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT, 5);
    	tvName.setGravity(Gravity.FILL_VERTICAL);
    	tvName.setTextColor(Color.WHITE);
    	tvNameParams.setMargins(ShareData.padding10*2, 0, 0, 0);
    	tvName.setLayoutParams(tvNameParams);
		
    	
		LinearLayout llBongolayout = (LinearLayout)instance.findViewById(R.id.bongoMenuLayout);
		LinearLayout.LayoutParams ivBongoIconParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				ShareData.headerHeight);
		llBongolayout.setLayoutParams(ivBongoIconParams);
		
    	ivBongoIcon = (ImageView) instance.findViewById(R.id.bongoIcon);
//    	LinearLayout.LayoutParams ivBongoIconParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
//    			heights);
//    	ivBongoIconParams.setMargins(ShareData.padding10*2, ShareData.padding10, 0, 0);
//    	ivBongoIcon.setLayoutParams(ivBongoIconParams);
		
		ScrollView sv = (ScrollView)instance.findViewById(R.id.scrollView);
		sv.setBackgroundColor(Color.WHITE);
		sv.setLayoutParams(new LayoutParams(ShareData.getScreenWidth(instance)- ShareData.getScreenWidth(instance)/4,
                LayoutParams.FILL_PARENT));
		
		LinearLayout mainLayout = (LinearLayout)instance.findViewById(R.id.mainLayout);
		for(int a=0; a<6; a++)
		{
			final LinearLayout ll1 = new LinearLayout(instance);
			LinearLayout.LayoutParams llM1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			ll1.setBackgroundColor(Color.parseColor("#ededed"));
			ll1.setLayoutParams(llM1);
			ll1.setId(a);
			
			ll1.setOnClickListener(new View.OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					if(slide_me.isClosed()==false)
					{
						//Toast.makeText(instance.getBaseContext(), "menu:"+v.getId(), Toast.LENGTH_SHORT).show();
						if(v.getId()==0)
						{
							instance.finish();
							instance.startActivity(new Intent(instance.getBaseContext(), StartingPage.class));
							instance.overridePendingTransition( R.anim.animation1, R.anim.animation2 );
						}
						else if(v.getId()==1)
						{
							instance.finish();
							instance.startActivity(new Intent(instance.getBaseContext(), UserProfile.class));
							instance.overridePendingTransition( R.anim.animation1, R.anim.animation2 );
						}
						else if(v.getId()==2)
						{
							CategoryLanding.catagoryName = "movies";
							instance.finish();
							instance.startActivity(new Intent(instance.getBaseContext(), CategoryLanding.class));
							instance.overridePendingTransition( R.anim.animation1, R.anim.animation2 );
						}
						else if(v.getId()==3)
						{
							CategoryLanding.catagoryName = "tv";
							instance.finish();
							instance.startActivity(new Intent(instance.getBaseContext(), CategoryLanding.class));
							instance.overridePendingTransition( R.anim.animation1, R.anim.animation2 );
						}
						else if(v.getId()==4)
						{
							CategoryLanding.catagoryName = "music";
							instance.finish();
							instance.startActivity(new Intent(instance.getBaseContext(), CategoryLanding.class));
							instance.overridePendingTransition( R.anim.animation1, R.anim.animation2 );
						}
						else if(v.getId()==5)
						{
							instance.finish();
							instance.startActivity(new Intent(instance.getBaseContext(), People.class));
							instance.overridePendingTransition( R.anim.animation1, R.anim.animation2 );
						}
					}
				}
			});
			ImageView iv = new ImageView(instance);
			LinearLayout.LayoutParams iv1 = new LinearLayout.LayoutParams(ShareData.getScreenWidth(instance)/12, 
											ShareData.getScreenWidth(instance)/12);
			iv1.setMargins(ShareData.padding10*2, ShareData.padding10*2, ShareData.padding10, ShareData.padding10*2);
			iv.setLayoutParams(iv1);
		
			TextView tv = new TextView(instance);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)instance.getResources().getDimension(R.dimen.text_size2));
			if(a==0)
			{
				tv.setText("Main Menu");
				try 
				{
					iv.setImageResource(R.drawable.menu1);
				} 
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(a==1)
			{
				tv.setText("My Account");
				try 
				{
					iv.setImageResource(R.drawable.myaccount);
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(a==2)
			{
				tv.setText("Movies");
				try {
					iv.setImageResource(R.drawable.movies1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(a==3)
			{
				tv.setText("Tv");
				try {
					iv.setImageResource(R.drawable.tv1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(a==4)
			{
				tv.setText("Music");
				try {
					iv.setImageResource(R.drawable.music1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(a==5)
			{
				tv.setText("People");
				try {
					iv.setImageResource(R.drawable.people1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			tv.setTextColor(Color.BLACK);
			tv.setGravity(Gravity.FILL_VERTICAL);
			LinearLayout.LayoutParams tv1 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT);
			tv.setLayoutParams(tv1);
			
	
			View v = new View(instance);
			v.setBackgroundColor(Color.parseColor("#dbdbdb"));
			LinearLayout.LayoutParams v1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 3);
		
			ll1.addView(iv);
			ll1.addView(tv);
		
			mainLayout.addView(ll1);
			mainLayout.addView(v,v1);

			mainLayout.post(new Runnable() 
			{
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					height= ll1.getHeight();
					//Log.d("AddMenu", "height:"+height);
				}
			});

			
		}
		
		h1=height;
		
//		for(int t=0; t<5; t++)
//		{
//		
//			LinearLayout ll2 = new LinearLayout(instance);
//			LinearLayout.LayoutParams llM2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//			ll2.setBackgroundColor(Color.parseColor("#ededed"));
//			ll2.setOrientation(LinearLayout.VERTICAL);
//			ll2.setLayoutParams(llM2);
//			ll2.setId(t);
//			ll2.setOnClickListener(new View.OnClickListener() 
//			{
//			
//			@Override
//			public void onClick(View v) 
//			{
//				// TODO Auto-generated method stub
//				if(slide_me.isClosed()==false)
//				{
//					menuFlag = true;
//					if(v.getId()==1)
//					{
//						//movie
//						slide_me.toggleLeftDrawer();
//						instance.finish();
//						CategoryLanding.catagoryName = "movies";
//						instance.startActivity(new Intent(instance.getBaseContext(), CategoryLanding.class));
//						instance.overridePendingTransition( R.anim.animation1, R.anim.animation2 );
//					}
//					else if(v.getId()==2)
//					{
//						//tv
//						slide_me.toggleLeftDrawer();
//						instance.finish();
//						CategoryLanding.catagoryName = "tv";
//						instance.startActivity(new Intent(instance.getBaseContext(), CategoryLanding.class));
//						instance.overridePendingTransition( R.anim.animation1, R.anim.animation2 );
//					}
//					else if(v.getId()==3)
//					{
//						slide_me.toggleLeftDrawer();
//						instance.finish();
//						CategoryLanding.catagoryName = "music";
//						instance.startActivity(new Intent(instance.getBaseContext(), CategoryLanding.class));
//						instance.overridePendingTransition( R.anim.animation1, R.anim.animation2 );
//					}
//					else if(v.getId()==4)
//					{
//						//people
//						slide_me.toggleLeftDrawer();
//						instance.finish();
//						instance.startActivity(new Intent(instance.getBaseContext(), People.class));
//						instance.overridePendingTransition( R.anim.animation1, R.anim.animation2 );
//					}
////					else if(v.getId()==5)
////					{
////						//subscribe
////						slide_me.toggleLeftDrawer();
////						instance.finish();
////						instance.startActivity(new Intent(instance.getBaseContext(), Subscribe.class));
////						instance.overridePendingTransition( R.anim.animation1, R.anim.animation2 );
////					}					
//
//				}
//			}
//		});
//		
//		TextView tv1 = new TextView(instance);
//		tv1.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)instance.getResources().getDimension(R.dimen.text_size2));
//		tv1.setTextColor(Color.BLACK);
//		tv1.setGravity(Gravity.FILL_VERTICAL);
//		if(t==0)
//		{
//			tv1.setText("CATEGORIES");
//			tv1.setTextColor(Color.RED);
//		}
//		else if(t==1)
//		{
//			tv1.setText("Movies");
//		}
//		else if(t==2)
//		{
//			tv1.setText("Tv");
//		}
//		else if(t==3)
//		{
//			tv1.setText("Music");
//		}
//		else if(t==4)
//		{
//			tv1.setText("People");
//		}
////		else if(t==5)
////		{
////			tv1.setText("Subscription");
////		}
//	
//		
//		tv1.setPadding(ShareData.padding10*2, ShareData.padding10*2, 0, ShareData.padding10*2);
//		LinearLayout.LayoutParams tv2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,8);
//		tv1.setLayoutParams(tv2);
//		
//		View v3 = new View(instance);
//		v3.setBackgroundColor(Color.parseColor("#dbdbdb"));
//		LinearLayout.LayoutParams vL3 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2);
//		v3.setLayoutParams(vL3);
//		
//		ll2.addView(tv1);
//		ll2.addView(v3);
//		mainLayout.addView(ll2);
//		}
		
		userBanner(instance);
	}
	
	
	public static void userBanner(final Activity instance)
    {
    	tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)instance.getResources().getDimension(R.dimen.text_size2));
		
//    	String pics = ShareData.getSavedString(instance, "pic");
//    	if(ShareData.getSavedString(instance, "pic").length()!=0)
//		{
//			//userImageLoader.DisplayImage(ShareData.getSavedString(instance, "pic"), loginPic);
//			new LoadImageFromURL(loginPic).execute(pics);
//		}
		
		if(!(ShareData.getSavedString(instance, "name").length()==0))
		{
			//tvName.setText(""+ShareData.getSavedString(this, "name"));
			tvName.setText("SIGN OUT");
			tvName.setTypeface(null,Typeface.BOLD);
			tvName.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					if(slide_me.isClosed()==false)
					{
						LoginActivity.LogOut(instance);
						
						FacebookApp.logoutFromFacebook(instance);
						
//						slide_me.toggleLeftDrawer();
						instance.finish();
						if(pageNumber == 1)
						{
							instance.startActivity(new Intent(instance.getBaseContext(), CategoryLanding.class));
						}
						else if(pageNumber == 2)
						{
							instance.startActivity(new Intent(instance.getBaseContext(), People.class));
						}
						else if(pageNumber == 3)
						{
							instance.startActivity(new Intent(instance.getBaseContext(), Subscribe.class));
						}
						else if(AddMenu.pageNumber == 4)
						{
							instance.startActivity(new Intent(instance.getBaseContext(), SingleMoviePage.class));
						}
						else if(AddMenu.pageNumber == 5)
						{
							instance.startActivity(new Intent(instance.getBaseContext(), Movies.class));
						}
						else if(AddMenu.pageNumber == 6)
						{
							instance.startActivity(new Intent(instance.getBaseContext(), ActorProfile.class));
						}
						else if(AddMenu.pageNumber == 7)
						{
							instance.startActivity(new Intent(instance.getBaseContext(), MovieSummary.class));
						}
						instance.overridePendingTransition( R.anim.animation1, R.anim.animation2 );
					}
				}
			});
		}
		else
		{
			tvName.setText("SIGN IN");
			tvName.setTypeface(null,Typeface.BOLD);
			tvName.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					if(slide_me.isClosed()==false)
					{
						instance.finish();
						instance.startActivity(new Intent(instance.getBaseContext(), LoginNew.class));
						instance.overridePendingTransition( R.anim.animation1, R.anim.animation2 );
					}
				}
			});
		}
    }
	
	public static void actionSearch(Activity instance)
	{
		searchQuery = et.getText().toString().trim();
		Log.d("AddMenu", "searchQuery:"+searchQuery);
		ShareData.hideKeyboard1(instance, et);
		//If there is data
		if(searchQuery.length()!=0)
		{
			header.headerLayout.removeAllViews();
			header.headerLayout.addView(header.menuIcon, header.menuLayout);
			header.headerLayout.addView(header.bongoIcon, header.bongoLayout);
			header.headerLayout.addView(header.searchIcon, header.searchLayout);
			//header.searchLayout.setMargins(ShareData.padding10*8, ShareData.padding10*2, ShareData.padding10*5, ShareData.padding10*2);
		
			StartingPage.browseAll = 3;
			
			if(SingleMoviePage.SingleMovieActivityRunning == true)
			{
				 SingleMoviePage.singleMovieInstance.finish();
   		  	}	
			
			instance.finish();
			instance.startActivity(new Intent(instance.getBaseContext(), Movies.class));
			instance.overridePendingTransition(R.anim.animation1, R.anim.animation2);
		}
		//If there is no data
		else
		{
			instance.finish();
			instance.startActivity(new Intent(instance.getBaseContext(), CategoryLanding.class));
			instance.overridePendingTransition(R.anim.animation1, R.anim.animation2);
		}
	}

}
