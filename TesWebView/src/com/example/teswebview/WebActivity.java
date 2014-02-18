package com.example.teswebview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewDatabase;

public class WebActivity extends Activity {
	WebView mWebView;
	WebViewDatabase mWVD;
	String TAG = "webTest";
	// String url = "file:///android_asset/index.html";
	String mUrl = "http://www.youku.com/";
	// String url = "http://www.youku.com/user_login/";
	String mLoginUrl = "http://www.youku.com/index/mlogin";// login url

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		mWebView = (WebView) findViewById(R.id.WebView);
		mWVD = WebViewDatabase.getInstance(this);
		WebSettings nWebSettings = mWebView.getSettings();
		nWebSettings.setJavaScriptEnabled(true);
		nWebSettings.setPluginState(PluginState.ON);
		nWebSettings.setSavePassword(true);
		mWebView.setWebChromeClient(new YouKuWebChromeClient());
		mWebView.setWebViewClient(new YouKuWebViewClient());
		mWebView.loadUrl(mUrl);
		mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");

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
		// String postData = "username=xu20111123@sina.com&password=pi31415926";
		String postData = "username=18221272426&password=123456789";
		mWebView.postUrl(mLoginUrl, postData.getBytes());
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mWVD.clearUsernamePassword();
		Log.i("twt", "onStop");
	}

	@Override
	// 设置回退
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	class YouKuWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onFormResubmission(WebView view, Message dontResend,
				Message resend) {
			// TODO Auto-generated method stub
			Log.d("twt", "onFormResubmission");
			super.onFormResubmission(view, dontResend, resend);
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			// TODO Auto-generated method stub
			Log.d("twt", "onLoadResource");
			Log.d("twt", "url:" + url);
			view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
					+ "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
			super.onLoadResource(view, url);
		}

		@Override
		public void onReceivedLoginRequest(WebView view, String realm,
				String account, String args) {
			// TODO Auto-generated method stub
			Log.d("twt", "onReceivedLoginRequest");
			super.onReceivedLoginRequest(view, realm, account, args);
		}

		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d("twt", "onPageStarted");
			super.onPageStarted(view, url, favicon);
		}

		public void onPageFinished(WebView view, String url) {
			Log.d("twt", "onPageFinished ");
			view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
					+ "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
			// getElementById()
			// view.loadUrl("javascript:window.local_obj.showSource('test'+'document.getElementById('mAccount')[0].setText('aaa');");
			// view.loadUrl("javascript:window.local_obj.showSource('<head>'+document.getElementById('myInput').value='ddd'+'</head>');");

			// view.loadUrl("javascript:var x=document.getElementById('myInput').value = 'aaa'");
			// view.loadUrl("javascript:window.local_obj.showSource('<head>'+document.getElementById('myInput').value+'</head>');");
			if (url.equals("http://www.youku.com/index/mlogin")) {
				mWebView.loadUrl(mUrl);
				return;
			}
			super.onPageFinished(view, url);
		}

	}

	class YouKuWebChromeClient extends WebChromeClient {

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			// TODO Auto-generated method stub
			Log.i("twt", "onJsAlert");
			return super.onJsAlert(view, url, message, result);
		}

		@Override
		public void onReceivedTouchIconUrl(WebView view, String url,
				boolean precomposed) {
			// TODO Auto-generated method stub
			Log.i("twt", "onReceivedTouchIconUrl");
			super.onReceivedTouchIconUrl(view, url, precomposed);
		}

		@Override
		public boolean onCreateWindow(WebView view, boolean isDialog,
				boolean isUserGesture, Message resultMsg) {
			// TODO Auto-generated method stub
			Log.i("twt", "onCreateWindow");
			return super.onCreateWindow(view, isDialog, isUserGesture,
					resultMsg);
		}

		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				JsResult result) {
			// TODO Auto-generated method stub
			Log.i("twt", "onJsConfirm");
			return super.onJsConfirm(view, url, message, result);
		}

		@Override
		public void onRequestFocus(WebView view) {
			// TODO Auto-generated method stub
			Log.i("twt", "onRequestFocus");
			super.onRequestFocus(view);
		}

	}

	final class InJavaScriptLocalObj {
		public void showSource(String html) {
			Log.i("HTML", html);
		}
	}
}
