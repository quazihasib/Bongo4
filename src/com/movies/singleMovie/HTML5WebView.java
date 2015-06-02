package com.movies.singleMovie;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.movies.bongobd.R;
import com.tab.ShareData;

public class HTML5WebView extends WebView 
{
    private Context                             mContext;
    private MyWebChromeClient                   mWebChromeClient;
    private View                                mCustomView;
   // private LinearLayout                         mCustomViewContainer;
    private WebChromeClient.CustomViewCallback  mCustomViewCallback;

    public static  LinearLayout                         mContentView;
    private LinearLayout                         Layout;

    static final String LOGTAG = "HTML5WebView";

    private void init(Context context) 
    {
        mContext = context;     
        Activity a = (Activity) mContext;


        Layout = (LinearLayout) LayoutInflater.from(a).inflate(R.layout.single_movie_page, null);
        mContentView = (LinearLayout) SingleMoviePage.singleMovieInstance.findViewById(R.id.webView1);
       // mCustomViewContainer = (LinearLayout) mBrowserFrameLayout.findViewById(R.id.fullscreen_custom_content);
      
        RelativeLayout.LayoutParams webParams = new RelativeLayout.LayoutParams(ShareData.getScreenWidth(SingleMoviePage.singleMovieInstance), ShareData.getScreenHeight(SingleMoviePage.singleMovieInstance));
        mContentView.setLayoutParams(webParams);
        
        
        // Configure the webview
        WebSettings s = getSettings();
        //s.setBuiltInZoomControls(true);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        //  s.setSavePassword(true);
        s.setSaveFormData(true); 
        s.setJavaScriptEnabled(true);
        mWebChromeClient = new MyWebChromeClient();
        setWebChromeClient(mWebChromeClient);

        setWebViewClient(new WebViewClient());

        //setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        // enable navigator.geolocation 
        // s.setGeolocationEnabled(true);
        // s.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");

        // enable Web Storage: localStorage, sessionStorage
        s.setDomStorageEnabled(true);

        mContentView.addView(this);
    }

    public HTML5WebView(Context context)
    {
        super(context);
        init(context);
    }

    public HTML5WebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public HTML5WebView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

//    public LinearLayout getLayout()
//    {
//        return mLayout;
//    }

    public boolean inCustomView() 
    {
        return (mCustomView != null);
    }

    public void hideCustomView()
    {
        mWebChromeClient.onHideCustomView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) 
        {
            if((mCustomView == null) && canGoBack())
            {
                goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebChromeClient extends WebChromeClient 
    {
        private Bitmap      mDefaultVideoPoster;
        private View        mVideoProgressView;

        @Override
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback)
        {
            //Log.i(LOGTAG, "here in on ShowCustomView");
            HTML5WebView.this.setVisibility(View.GONE);

            // if a view already exists then immediately terminate the new one
            if(mCustomView != null)
            {
                callback.onCustomViewHidden();
                return;
            }

            //mCustomViewContainer.addView(view);
            mCustomView = view;
            mCustomViewCallback = callback;
            //mCustomViewContainer.setVisibility(View.VISIBLE);
        }

//        @Override
//        public void onHideCustomView()
//        {
//            System.out.println("customview hide");
//            if (mCustomView == null)
//                return;        
//
//            // Hide the custom view.
//            mCustomView.setVisibility(View.GONE);
//
//            // Remove the custom view from its container.
//           // mCustomViewContainer.removeView(mCustomView);
//            mCustomView = null;
//            //mCustomViewContainer.setVisibility(View.GONE);
//           // mCustomViewCallback.onCustomViewHidden();
//
//            HTML5WebView.this.setVisibility(View.VISIBLE);
//            HTML5WebView.this.goBack();
//            //Log.i(LOGTAG, "set it to webVew");
//        }


//        @Override
//        public View getVideoLoadingProgressView() 
//        {
//            //Log.i(LOGTAG, "here in on getVideoLoadingPregressView");
//            if(mVideoProgressView == null) 
//            {
//                LayoutInflater inflater = LayoutInflater.from(mContext);
//                mVideoProgressView = inflater.inflate(R.layout.video_loading_progress, null);
//            }
//            return mVideoProgressView; 
//        }
//
//         @Override
//         public void onReceivedTitle(WebView view, String title)
//         {
//            ((Activity) mContext).setTitle(title);
//         }
//
//         @Override
//         public void onProgressChanged(WebView view, int newProgress) 
//         {
//             ((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress*100);
//         }

//         @Override
//         public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) 
//         {
//             callback.invoke(origin, true, false);
//         }
    }

//    static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS =
//        new FrameLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
}