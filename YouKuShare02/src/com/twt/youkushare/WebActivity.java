package com.twt.youkushare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewDatabase;
import android.widget.FrameLayout;

import com.newqm.sdkoffer.AdView;
import com.newqm.sdkoffer.QMExitListener;
import com.newqm.sdkoffer.QuMiConnect;
import com.newqm.sdkoffer.QuMiNotifier;
public class WebActivity extends Activity {
	WebView mWebView;
	WebViewDatabase mWVD;
	RequestUserInfo mRequestUserInfo;
	String TAG = "WebActivity";
	// String url = "file:///android_asset/index.html";
	String mUrl = "http://www.youku.com/";
	// String url = "http://www.youku.com/user_TLogin/";
	// String mTLoginUrl = "http://www.youku.com/index/mTLogin";
	String mTLoginUrl = "http://www.youku.com/index/mlogin";
	String[] cookies;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cookies = getIntent().getStringArrayExtra("cookies");
		TLog.i(TAG, "cookie:" + cookies.toString());
		init();
		QuMiConnect.ConnectQuMi(this, "559b4ba9252f80da", "77d3e0373e85a348");
		
		AdView adView = new AdView(this);
		FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		param.bottomMargin = 0;
		param.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL; 
		addContentView(adView, param);
	}
	
	protected void onResume() {
		// 从服务器端获取当前用户的虚拟货币.
		// 返回结果在回调函数getUpdatePoints(...)中处理
		Log.i("onResume","onResume");
//		QuMiConnect.ConnectQuMi(this, "2014a9bf65787feb", "25db814614c7dfac");	
		super.onResume();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void init() {
		TLog.i(TAG, "init");
		setContentView(R.layout.activity_web);
		mWebView = (WebView) findViewById(R.id.WebView);
		mWVD = WebViewDatabase.getInstance(this);
		WebSettings nWebSettings = mWebView.getSettings();
		nWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		nWebSettings.setJavaScriptEnabled(true);
		nWebSettings.setPluginState(PluginState.ON);
		nWebSettings.setSavePassword(false);
		mWebView.setWebChromeClient(new YouKuWebChromeClient());
		mWebView.setWebViewClient(new YouKuWebViewClient());

		synCookies(this, mUrl);
		mWebView.loadUrl(mUrl);
	}

	public void synCookies(Context context, String url) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();// 绉婚��
		for (int i = 0; i < cookies.length; i++) {
			cookieManager.setCookie(url, cookies[i]);// cookies������HttpClient涓���峰�����cookie
			CookieSyncManager.getInstance().sync();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web, menu);
		return true;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mWVD.clearUsernamePassword();
		mWVD.clearHttpAuthUsernamePassword();
		mWVD.clearFormData();
		mWebView.clearCache(true);
		mWebView.clearHistory();
		CookieSyncManager.createInstance(mWebView.getContext());
		CookieSyncManager.getInstance().startSync();
		CookieManager.getInstance().removeSessionCookie();

		TLog.i(TAG, "onStop");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack(); // goBack()
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	class YouKuWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

	}

	class YouKuWebChromeClient extends WebChromeClient {

	}

	final class InJavaScriptLocalObj {
		public void showSource(String html) {
			TLog.i("HTML", html);
		}
	}
}
