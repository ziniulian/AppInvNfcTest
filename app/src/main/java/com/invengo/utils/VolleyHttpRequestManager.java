package com.invengo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView.ScaleType;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class VolleyHttpRequestManager {

	private static VolleyHttpRequestManager mHttpManager = null;
	private RequestQueue mQueue = null;
	private VolleyHttpRequestManager(Context context){
		mQueue = Volley.newRequestQueue(context);
	}
	
	public synchronized static VolleyHttpRequestManager getInstance(Context context){
		if(null == mHttpManager){
			mHttpManager = new VolleyHttpRequestManager(context);
		}
		return mHttpManager;
	}
	
	public <T> void addRequest(Request<T> request){
		request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
		mQueue.add(request);
	}
	
	public void doStringRequest(String tag, String url, Listener<String> listener, ErrorListener errorListener){
//		StringRequest request = new StringRequest(url, listener, errorListener);
		StringRequest request = new StringRequest(Request.Method.GET, url, listener, errorListener);
		request.setTag(tag);
		mQueue.add(request);
	}
	
	public void doImageRequest(String tag, String url, Listener<Bitmap> listener, ErrorListener errorListener){
		ImageRequest request = new ImageRequest(url, listener, 600, 750,
				ScaleType.FIT_XY, Config.RGB_565, errorListener);
		request.setTag(tag);
		mQueue.add(request);
	}
	
	public void cancleSingleRequest(String tag){
		mQueue.cancelAll(tag);
	}
	
	public void stop(){
		mQueue.stop();
	}
	
	/*
	 * generator the new path like:path/params[0]/params[1]/...
	 */
	public String getURL(String path, String...params){
		StringBuffer newPath = new StringBuffer();
		newPath.append(path);
		
		for(String param : params){
			newPath.append("/").append(param);
		}
		
		return newPath.toString();
	}
	
	public boolean checkNetState(Context context) {
		ConnectivityManager netManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(null == netManager){
			return false;
		}
		NetworkInfo activeNetworkInfo = netManager.getActiveNetworkInfo();
		if(null == activeNetworkInfo){
			return false;
		}
		return activeNetworkInfo.isConnected();
	}
}
