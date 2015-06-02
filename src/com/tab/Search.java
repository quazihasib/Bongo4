package com.tab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.movies.bongobd.R;
import com.movies.startingPage.StartingPage;

public class Search
{
	public static EditText etSearch;
	public static String search;
	public static PopupWindow mpopup;
	public static RequestQueue requestQueue;
	public static  ArrayAdapter<String> adapter;
	public static String[] searchSuggestions;
	public static String DEBUG_TAG = Search.class.getSimpleName();
	
	public static ArrayAdapter<String> makeJsonObjectRequestForSearchQuery(String urlJsonObj, final Activity instance)
	{
		Log.d(DEBUG_TAG, "urlJsonObj:"+ urlJsonObj);
		requestQueue = Volley.newRequestQueue(instance);
		
		JsonArrayRequest s = new JsonArrayRequest(urlJsonObj, new Response.Listener<JSONArray>() 
		{
			@Override
			public void onResponse(JSONArray arg0)
			{
				// TODO Auto-generated method stub
				searchSuggestions = new String[arg0.length()];
				//Log.d(DEBUG_TAG,"Search Responce:"+ arg0.toString());
				for (int i=0; i<arg0.length(); i++)
				{
					try 
					{
						JSONObject json = arg0.getJSONObject(i);
						String a  = json.toString().trim().replace("srh_txt", "");
						a = a.trim().replaceAll("[{}]","");
						a = a.trim().replace("\"", "");
						a = a.trim().replace(":", "");
						
						if(a.length()>20)
						{
							a = a.substring(0, 19)+"...";
						}
						searchSuggestions[i] = a;
						Log.d(DEBUG_TAG, "suggestions:" + searchSuggestions[i]);
					} 
					catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				adapter = new ArrayAdapter<String>(instance,
						android.R.layout.simple_dropdown_item_1line, searchSuggestions);
				
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
				Search.showSortPopup(instance);
			}
		},
        new Response.ErrorListener() 
		{
            @Override
            public void onErrorResponse(VolleyError error)
            {
                 Log.d(DEBUG_TAG, "error:"+error.getMessage());
            }
        });
		// Adding request to request queue
		requestQueue.add(s);
		return adapter;
	}
	
	
	public static void showSortPopup(final Activity instance) 
	{
		Rect r = new Rect();
		View rootview = instance.getWindow().getDecorView(); 
		rootview.getWindowVisibleDisplayFrame(r);
		

        int screenHeight = ShareData.getScreenHeight(instance);
        int heightDifference = screenHeight - (r.bottom - r.top);
        Log.d("Keyboard Size", "Size keyboard: " + heightDifference);
        
		// inflating popup layout
		View popUpView = instance.getLayoutInflater().inflate(R.layout.dialog_search, null);
		// Creation of popup
		mpopup = new PopupWindow(popUpView, LayoutParams.FILL_PARENT, heightDifference, false); 
		mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
		// Displaying popup
		try 
		{
			if(mpopup!=null && !mpopup.isShowing() && instance!=null)
			{
				mpopup.showAtLocation(popUpView, Gravity.TOP, 0, ShareData.getScreenHeight(instance)/8); 
			}
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		
		final ListView lv= (ListView) popUpView.findViewById(R.id.listItems);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(
				instance,R.layout.search_list_item, R.id.textView1, searchSuggestions)
		{
			 @Override
			 public View getView(int position, View convertView, ViewGroup parent)
			 {
			      View view =super.getView(position, convertView, parent);
			            
			      final TextView textView=(TextView) view.findViewById(R.id.textView1);
			      textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int)instance.getResources().getDimension(R.dimen.text_size2));
			      textView.setTextColor(Color.BLACK);
			      textView.setIncludeFontPadding(false);
			      textView.setGravity(Gravity.CENTER_VERTICAL);
			      RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ShareData.getScreenHeight(instance)/16);
			      textViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			      textView.setLayoutParams(textViewParams);
			      
			      
			      ImageView imView=(ImageView) view.findViewById(R.id.imageView1);
			      try {
					imView.setImageResource(R.drawable.arrow11);
			      } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			      }
			      RelativeLayout.LayoutParams imViewParams = new RelativeLayout.LayoutParams(ShareData.getScreenHeight(instance)/40, ShareData.getScreenHeight(instance)/40);
			      imViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
			      imViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			      imView.setLayoutParams(imViewParams);
			      
//			      ViewGroup.LayoutParams textViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//			      textViewParams.setMargins(ShareData.padding10,  -ShareData.padding10*2, ShareData.padding10,  -ShareData.padding10*2);
//			      textView.setLayoutParams(textViewParams);
			            
			      
			      view.setOnClickListener(new View.OnClickListener() 
			      {
			    	  @Override
			    	  public void onClick(View v) 
			    	  {
			    		  // TODO Auto-generated method stub
			    		  mpopup.dismiss();
			    		  
			    		  Log.d(DEBUG_TAG, "Stpp");
			    		 
			    		//if movie is playing, then close it
//			  			if(SingleMoviePage.webView!=null)
//			  			{
//			  				//webView.loadUrl("about:blank");
//			  				SingleMoviePage.webView.stopLoading();
////			  				webView.setWebChromeClient(null);
////			  				webView.setWebViewClient(null);
//			  				SingleMoviePage.webView.destroy();
//			  				SingleMoviePage.webView = null;
//			  			}
						    
			    		  AddMenu.et.setText(textView.getText());
//								StartingPage.browseAll = 3;
//								AddMenu.searchQuery = AddMenu.et.getText().toString().trim();
//								StartingPage.startInstance.finish();
//								StartingPage.startInstance.startActivity(new Intent(StartingPage.startInstance.getBaseContext(), Movies.class));
//								StartingPage.startInstance.overridePendingTransition(R.anim.animation1, R.anim.animation2);
			    		  AddMenu.actionSearch(StartingPage.startInstance);
			    	  }
			      });
			      return view;
			 }
		};
			    
		lv.setAdapter(adapter);
	}
}
